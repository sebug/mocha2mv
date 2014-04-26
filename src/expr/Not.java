package expr;

import java.util.List;

import blifmv.Var;

/**
 * Boolean not.
 *
 */
public class Not extends ArithExpression {
	private final ArithExpression subexpression;
	
	public Not(ArithExpression subexpression) {
		this.subexpression = subexpression;
	}
	
	@Override
	public String toString() {
		return "~(" + subexpression + ")";
	}

	@Override
	public String evaluate(List<Var> evaluateRange, List<String> valuation) {
		if( subexpression.evaluate(evaluateRange, valuation).equals("1")) return "0";
		else return "1";
	}
}
