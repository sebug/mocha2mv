package expr;

import java.util.List;

import blifmv.Var;


/**
 * Wrapper for a variable occurring freely in the expression. We only store the name,
 * the type will be provided on evaluation.
 *
 */

public class Variable extends ArithExpression {
	public final Var v;
	
	public Variable(Var v) {
		this.v = v;
	}
	
	public Variable prime() {
		return new Variable(this.v.prime());
	}
	
	@Override
	public String toString() {
		return this.v.getName();
	}

	@Override
	public String evaluate(List<Var> evaluateRange, List<String> valuation) {
		// Search for the current value of this variable and return it.
		int pos = -1;
		int i = 0;
		for( Var x : evaluateRange) {
			if( x.equals(v) ) pos = i;
			i++;
		}
		if( pos < 0 ) throw new Error("Did not find variable " + v + " in current valuation.");
		return valuation.get(pos);
	}
}
