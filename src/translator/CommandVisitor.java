package translator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import sun.tools.tree.BooleanExpression;

import mocha.parser.ASTArrayExpression;
import mocha.parser.ASTAssign;
import mocha.parser.ASTAssignLhs;
import mocha.parser.ASTAssignList;
import mocha.parser.ASTAssignRhs;
import mocha.parser.ASTBitwiseExpression;
import mocha.parser.ASTBoolExpression;
import mocha.parser.ASTBoolVal;
import mocha.parser.ASTConditionalExpression;
import mocha.parser.ASTConstant;
import mocha.parser.ASTEventAssign;
import mocha.parser.ASTGuardedAssignList;
import mocha.parser.ASTModifier;
import mocha.parser.ASTPrimaryExpression;
import mocha.parser.ASTRangeExpression;
import mocha.parser.ASTRelationalExpression;
import mocha.parser.ASTShiftExpression;
import mocha.parser.ASTUnaryExpression;
import blifmv.ValueIterator;
import blifmv.Var;
import expr.And;
import expr.ArithExpression;
import expr.AssignList;
import expr.Assignment;
import expr.BooleanConstant;
import expr.Dec;
import expr.EnumerationValue;
import expr.Equality;
import expr.GreaterThan;
import expr.HasChanged;
import expr.If;
import expr.Inc;
import expr.LessEqual;
import expr.LessThan;
import expr.Not;
import expr.NumberConstant;
import expr.OperationIndicator;
import expr.Or;
import expr.Variable;

/**
 * Visits an init or update command. From this, he gets the possible input-output combinations.
 *
 */
public class CommandVisitor extends AbstractVisitor {
	private List<Var> inputRange; // Variables involved in the input of the atom
	private List<Var> outputRange; // Output produced from this module
	
	public CommandVisitor(List<Var> inputRange, List<Var> outputRange) {
		this.inputRange = inputRange;
		this.outputRange = outputRange;
	}
	
	/**
	 * ASTGuardedAssignList is the list of guard,command pairs. We want to collect them and
	 * afterwards add nondeterministic variables.
	 */
	public Object visit(ASTGuardedAssignList astguardedassignlist, Object obj) {
		// Left hand side is the guard
		ArithExpression bex = (ArithExpression)astguardedassignlist.jjtGetChild(0).jjtAccept(this, null);
		if( Mocha2MV.DEBUG ) {
			System.out.println("Extracted boolean expression: " + bex);
		}
		
		// Right hand side is the assignments
		List<Assignment> assignments = new LinkedList<Assignment>(); // If no assignments are to be made, then
		// We should assign nondeterministically?
		if( astguardedassignlist.jjtGetNumChildren() > 1) { // We have a list of assignments as second argument.
			assignments = (List<Assignment>)astguardedassignlist.jjtGetChild(1).jjtAccept(this, null);
		}
		
		List<List<String>> matchingGuards = new LinkedList<List<String>>();
		
		Iterator<List<String>> iter = new ValueIterator(this.inputRange);
		while(iter.hasNext()) {
			List<String> valuation = iter.next();
			String result = bex.evaluate(this.inputRange, valuation);
			if( result.equals("1")) { // true
				matchingGuards.add(valuation);
			}
		}
		
		AssignList guardedAssign = new AssignList(assignments,matchingGuards,this.inputRange,this.outputRange);
		
		return guardedAssign.evaluate();
	}
	
	/**
	 * One boolean expression. We expect to add one ValueSet to the list.
	 */
	public Object visit(ASTBoolExpression astboolexpression, Object obj) {
		ArithExpression ret = null;
		// This might well be more than one additional element!
		// a & b & c for example.
		ArithExpression first = (ArithExpression)astboolexpression.jjtGetChild(0).jjtAccept(this, null);
		List<ArithExpression> others = new LinkedList<ArithExpression>();
		for(int i = 1; i < astboolexpression.jjtGetNumChildren(); i++) {
			others.add((ArithExpression)astboolexpression.jjtGetChild(i).jjtAccept(this, null));
		}
		// Now go through the list, accumulating ands or ors.
		ret = first;
		for(ArithExpression el : others) {
			// TODO: is the associativity right this way?
			// I presume that this is left-associative, so this binding works. otherwise we would have to do
			// a reverse.
			if( el instanceof OperationIndicator ) { // Dynamic typing hell.
				OperationIndicator oi = (OperationIndicator)el;
				switch( oi.getOperator() ) {
				case ASTBoolExpression.AND:
					ret = new And(ret,el);
					break;
				case ASTBoolExpression.OR:
					ret = new Or(ret,el);
				}
			}
		}

		return ret;
	}
	
	/**
	 * Relational expression. Which relation is used can only be determined by looking at the children.
	 * UnaryExpression then indicates which operator is chosen.
	 */
	public Object visit(ASTRelationalExpression astrelationalexpression, Object obj) {
		ArithExpression rel;
		ArithExpression lhs = (ArithExpression)astrelationalexpression.jjtGetChild(0).jjtAccept(this, null);
		ArithExpression rhs = null;
		if( astrelationalexpression.jjtGetNumChildren() > 1) {
			rhs = (ArithExpression)astrelationalexpression.jjtGetChild(1).jjtAccept(this, null);
		}
		
		if( rhs == null ) {
			rel = new OperationIndicator(astrelationalexpression.operator,lhs);
			return rel;
		}
		
		if( astrelationalexpression.operator == 0 ) { // No operator
			if( rhs != null ) {
				// The operation is indicated by the unary expression.
				if( rhs instanceof OperationIndicator ) {
					OperationIndicator oi = (OperationIndicator)rhs;
					switch(oi.getOperator()) {
					case 5:
						rel = new Equality(lhs,rhs);
						break;
					case 4:
						rel = new LessThan(lhs,rhs);
						break;
					case 2:
						rel = new LessEqual(lhs,rhs);
						System.err.println("Less equal");
						System.err.println(astrelationalexpression.jjtGetNumChildren());
						break;
					case 3:
						// gt
						rel = new GreaterThan(lhs,rhs);
						break;
					default:
						rel = new Equality(lhs,rhs);
					}
				} else {
					rel =  new Equality(lhs,rhs);
				}
			} else {
				rel = lhs;
			}
		} else {
			throw new Error("Not handled yet: relational expression with operator." + astrelationalexpression.operator);
		}
		return rel;
	}
	
	/**
	 * A unary expression. What might that be?
	 */
	public Object visit(ASTUnaryExpression astunaryexpression, Object obj) {
		ArithExpression expr;
		if( astunaryexpression.operator == 0) {
			// No Operator
			if( astunaryexpression.isNegative ) { // Prepend with a not.
				expr = new Not((ArithExpression)astunaryexpression.jjtGetChild(0).jjtAccept(this, null));
			} else {
				expr = (ArithExpression)astunaryexpression.jjtGetChild(0).jjtAccept(this, null);
			}
		} else {
			expr = new OperationIndicator(
					astunaryexpression.operator,
					(ArithExpression)astunaryexpression.jjtGetChild(0).jjtAccept(this, null));
		}
		return expr;
	}
	
	/**
	 * A primary expression. Seems to be named.
	 * We do some horrible assumptions here: If this is not a variable in the
	 * corresponding model, then it is an enumerated value.
	 * This means that a variable cannot be named the same as a type value!
	 */
	public Object visit(ASTPrimaryExpression astprimaryexpression, Object obj) {
		ArithExpression expr = null;
		if( astprimaryexpression.name == null ) {
			// Forward
			expr = (ArithExpression)astprimaryexpression.jjtGetChild(0).jjtAccept(this, null);
			if( expr == null ) {
				throw new Error("Got a null.");
			}
		} else {
			// Go through input range, search for variable.
			Var associatedVar = getInputVar(astprimaryexpression.name);
			if( associatedVar == null ) associatedVar = getOutputVar(astprimaryexpression.name); // Also search in the output variables.
			if( associatedVar == null) {
				// Last possibility: Only the primed version is used as input.
				// In this case, we search for that one
				// TODO: Let the input be typechecked before so that we don't produce illegal solutions here.
				associatedVar = getInputVar(astprimaryexpression.name+"'");
			}
			if( associatedVar == null) {
				// In this case it is a value.
				expr = new EnumerationValue(astprimaryexpression.name);
			} else {
				expr = (ArithExpression)astprimaryexpression.jjtGetChild(0).jjtAccept(this, new Variable(associatedVar));
			}
		}
		return expr;
	}
	
	/**
	 * A constant. If it is boolean, then it will have a child with the boolean value.
	 */
	public Object visit(ASTConstant astconstant, Object obj) {
		int t = astconstant.type;
		return t == ASTConstant.NUM ? new NumberConstant(Integer.parseInt(astconstant.name)) :
			(ArithExpression)astconstant.jjtGetChild(0).jjtAccept(this, null); // Have to use information provided by children.
	}
	
	/**
	 * True or false.
	 */
	public Object visit(ASTBoolVal astboolval, Object obj) {
		if( astboolval.value == 0) return BooleanConstant.FALSE;
		else return BooleanConstant.TRUE;
	}
	
	
	/**
	 * Array expression. Might be for all or for some.
	 */
	public Object visit(ASTArrayExpression astarrayexpression, Object obj) {
		return (ArithExpression)astarrayexpression.jjtGetChild(0).jjtAccept(this, null);
	}
	
	/**
	 * Range expression. May be inc or dec.
	 * First child is the variable to be incremented, the
	 * second is by how much.
	 */
	public Object visit(ASTRangeExpression astrangeexpression, Object obj) {
		if( astrangeexpression.operator > 0) {
			if( astrangeexpression.jjtGetNumChildren() != 2) {
				throw new Error("Expected two children for inc or dec.");
			}
			ArithExpression what = (ArithExpression)astrangeexpression.jjtGetChild(0).jjtAccept(this, null);
			ArithExpression by = (ArithExpression)astrangeexpression.jjtGetChild(1).jjtAccept(this, null);
			return astrangeexpression.operator == ASTRangeExpression.INC ?
				new Inc(what,by) : new Dec(what,by);
		} else {
			return (ArithExpression)astrangeexpression.jjtGetChild(0).jjtAccept(this, null); // Forward.
		}
	}
	
	/**
	 * Shift expression
	 */
	public Object visit(ASTShiftExpression astshiftexpression, Object obj) {
		if( astshiftexpression.operator > 0) {
			throw new Error("Don't know what to do with shift expression operator > 0.");
		} else {
			return (ArithExpression)astshiftexpression.jjtGetChild(0).jjtAccept(this, null);
		}
	}
	
	/**
	 * Bitwise expression: AND, OR, XOR
	 */
	public Object visit(ASTBitwiseExpression astbitwiseexpression, Object obj) {
		if( astbitwiseexpression.operator > 0) {
			throw new Error("Not yet implemented: Bitwise expression with operator " + astbitwiseexpression.operator);
		} else {
			// Forward.
			return (ArithExpression)astbitwiseexpression.jjtGetChild(0).jjtAccept(this, null);
		}
	}
	
	/**
	 * Conditional expression. If t1 then t2 else t3
	 * More.
	 */
	public Object visit(ASTConditionalExpression astconditionalexpression, Object obj) {
		if( astconditionalexpression.jjtGetNumChildren() == 3 ) { // if-then-else
			ArithExpression test = (ArithExpression)astconditionalexpression.jjtGetChild(0).jjtAccept(this, null);
			ArithExpression blockThen = (ArithExpression)astconditionalexpression.jjtGetChild(1).jjtAccept(this, null);
			ArithExpression blockElse = (ArithExpression)astconditionalexpression.jjtGetChild(2).jjtAccept(this, null);
			return new If(test,blockThen,blockElse);
		} else {
			// Forward
			return (ArithExpression)astconditionalexpression.jjtGetChild(0).jjtAccept(this, null);
		}
	}
	
	/**
	 * Modifier. Is either ? or '
	 */
	public Object visit(ASTModifier astmodifier, Object obj) {
		if( astmodifier.isAsk) {
			if( obj != null && obj instanceof Variable) {
				return new HasChanged(((Variable)obj).v);
			} else {
				throw new Error("Expected to get a variable as argument to ?");
			}
		} else if ( astmodifier.isPrimed ) {
			if( obj != null && obj instanceof Variable) {
				return ((Variable)obj).prime();
			} else {
				throw new Error("Expected variable as argument to be primed.");
			}
		} else {
			// Did we get a variable as argument
			if( obj != null && obj instanceof Variable) {
				return obj; // Return unchanged
			} else {
				// Forward
				return (ArithExpression)astmodifier.jjtGetChild(0).jjtAccept(this, null);
			}
		}
	}
	
	/**
	 * Gets a variable for which we only know the name.
	 * @param name
	 * @return
	 */
	private Var getInputVar(String name) {
		Var ret = null;
		for( Var v : this.inputRange) {
			if( v.getName().equals(name) ) {
				ret = v;
			}
		}
		return ret;
	}
	
	/**
	 * Gets an output variable name.
	 * @param name the name of the variable.
	 * @return the variable.
	 */
	private Var getOutputVar(String name) {
		Var ret = null;
		for(Var v : this.outputRange) {
			if(v.getName().equals(name)) { // Might also be primed.
				ret = v;
			}
		}
		return ret;
	}
	
	// Assignments from here on.
	/**
	 * The assignment list associated with this boolean expression:
	 * Traverses and evaluates all assignments.
	 */
	public Object visit(ASTAssignList astassignlist, Object obj) {
		List<Assignment> ret = new LinkedList<Assignment>();
		for(int i = 0; i < astassignlist.jjtGetNumChildren(); i++) {
			ret.add((Assignment)astassignlist.jjtGetChild(i).jjtAccept(this, null));
		}
		return ret;
	}
	
	/**
	 * An assignment to one variable.
	 */
	public Object visit(ASTAssign astassign, Object obj) {
		// Left is the variable to be assigned.
		if( astassign.jjtGetNumChildren() > 1) {
			Var associatedVar = (Var)astassign.jjtGetChild(0).jjtAccept(this, null);
			
			// Right is an arithmetic expression to be evaluated.
			ArithExpression assignExpr =
				(ArithExpression)astassign.jjtGetChild(1).jjtAccept(this, null);

			return new Assignment(associatedVar,assignExpr);
		} else {
			// The child is the assignment (mostly event assign)
			return (Assignment)astassign.jjtGetChild(0).jjtAccept(this, null);
		}
	}
	
	/**
	 * Assignment of the form x!
	 * Translates to x' := ~x
	 */
	public Object visit(ASTEventAssign asteventassign, Object obj) {
		String associatedVarName = asteventassign.name;
		Var associatedVar = getOutputVar(associatedVarName + "'");
		if( associatedVar == null ) throw new Error("Did not find variable name " + associatedVarName + " for !");
		return new Assignment(associatedVar.prime(),new Not(new Variable(associatedVar.unprime())));
	}
	
	/**
	 * The left hand side of an assignment. Can basically only be a variable.
	 */
	public Object visit(ASTAssignLhs astassignlhs, Object obj) {
		String variableName = astassignlhs.name;
		Var ret = getOutputVar(variableName + "'"); // The prime because we necessarily only can update the current value.
		if( ret == null ) throw new Error("Did not find variable name " + variableName);
		return ret;
	}
	
	/**
	 * Assignment right hand side. Attention: Might be nondeterministic.
	 */
	public Object visit(ASTAssignRhs astassignrhs, Object obj) {
		if( astassignrhs.isNondet ) {
			return null;
		} else {
			return astassignrhs.jjtGetChild(0).jjtAccept(this, null);
		}
	}
}
