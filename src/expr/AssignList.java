package expr;

import java.util.LinkedList;
import java.util.List;

import blifmv.SequenceType;
import blifmv.Var;

/**
 * Represents a list of assignments to be made for one table
 * which are guarded by a boolean expression.
 *
 */

public class AssignList {
	private final List<Assignment> assignments;
	private final List<List<String>> matchingGuards;
	private final List<Var> inputs;
	private final List<Var> outputs;
	
	public AssignList(List<Assignment> assignments, List<List<String>> matchingGuards,
			List<Var> inputs, List<Var> outputs) {
		this.assignments = assignments;
		this.matchingGuards = matchingGuards;
		this.inputs = inputs;
		this.outputs = outputs;
	}
	
	/**
	 * Evaluates the expression down to one or multiple values.
	 */
	public List<List<String>> evaluate() {
		// First, order the assignments by the output variables.
		List<ArithExpression> expressionsOrderedByOutput = new LinkedList<ArithExpression>();
		for(Var out : this.outputs) {
			// Find the corresponding assignment
			String varname = out.getName();
			ArithExpression matchingExpression = null;
			for(Assignment ass : this.assignments) {
				if( ass.getVariable().getName().equals(varname)) matchingExpression = ass.getExpression();
			}
			expressionsOrderedByOutput.add(matchingExpression); // Even if it is 0.
		}
		
		List<List<String>> ret = new LinkedList<List<String>>();
		for(List<String> matchingGuard : this.matchingGuards) {
			List<List<String>> result = new LinkedList<List<String>>();
			int pos = 0;
			for(ArithExpression expr : expressionsOrderedByOutput) {
				if( expr == null ) {
					// Everything is possible for this variable.
					result.add(this.outputs.get(pos).getType().getAllValues());
				} else {
					// Singleton list.
					List<String> res = new LinkedList<String>();
					String calculatedRes = expr.evaluate(this.inputs, matchingGuard);
					
					// Important: Perform wrap-around for assignment.
					if( this.outputs.get(pos).getType() instanceof SequenceType ) {
						int size = this.outputs.get(pos).getType().getNumElements();
						
						// Now we have to do a modulo size to fit the value in the range.
						int mod = (Integer.parseInt(calculatedRes) % size);
						while( mod < 0) { // Actually we'd want a positive modulo.
							mod += size;
						}
						
						calculatedRes = "" + mod;
					}
					res.add(calculatedRes);
					result.add(res);
				}
				pos++;
			}
			List<List<String>> twisted = twist(result);
			for(List<String> expansion : twisted) {
				List<String> el = new LinkedList<String>();
				el.addAll(matchingGuard);
				el.addAll(expansion);
				ret.add(el);
			}
		}
		return ret;
	}
	
	/**
	 * When before there was a list where each element represented possible values for one variable,
	 * we convert it into a list where each element represents one mapping to all variables.
	 * 
	 * TODO: Check with more than one input.
	 * @param inlist
	 * @return
	 */
	private List<List<String>> twist(List<List<String>> inlist) {
		List<List<String>> ret = new LinkedList<List<String>>();
		
		// Get started with all possibilities for the first element
		if( !inlist.isEmpty() ) {
			for(String s: inlist.get(0)) {
				List<String> singleton = new LinkedList<String>();
				singleton.add(s);
				ret.add(singleton);
			}
			
			// Now populate with the rest.
			inlist.remove(0);
			while(!inlist.isEmpty()) {
				List<List<String>> newRet = new LinkedList<List<String>>();
				for(String s : inlist.get(0)) {
					List<List<String>> withS = new LinkedList<List<String>>();
					for(List<String> orig : ret) {
						List<String> n = new LinkedList<String>();
						for(String o : orig) { // Add all the elements already specified
							n.add(o);
						}
						n.add(s);
						withS.add(n);
					}
					newRet.addAll(withS);
				}
				ret = newRet;
				inlist.remove(0);
			}
		}
		return ret;
	}
}
