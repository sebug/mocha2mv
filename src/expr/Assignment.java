package expr;

import blifmv.Var;

/**
 * Represents the assignment to one variable.
 *
 */
public class Assignment {
	private final Var leftHandVariable;
	private final ArithExpression assignExpr;
	
	public Assignment(Var leftHandVariable, ArithExpression assignExpr) {
		this.leftHandVariable = leftHandVariable;
		this.assignExpr = assignExpr;
	}
	
	/**
	 * Gets the variable that is associated with this assignment.
	 * @return the variable of this assignment.
	 */
	public Var getVariable() {
		return this.leftHandVariable;
	}
	
	/**
	 * Gets the expression that is used in this assignment.
	 * @return the assignment expression.
	 */
	public ArithExpression getExpression() {
		return this.assignExpr;
	}
}
