package expr;

import java.util.List;

import blifmv.Var;

/**
 * lhs <= rhs
 *
 */
public class LessEqual extends BinaryRelation {
	public LessEqual(ArithExpression lhs, ArithExpression rhs) {
		super(lhs,rhs);
	}

	@Override
	public String evaluate(List<Var> evaluateRange, List<String> valuation) {
		int a = Integer.parseInt(lhs.evaluate(evaluateRange, valuation));
		int b = Integer.parseInt(rhs.evaluate(evaluateRange, valuation));
		
		if( a <= b ) return "1";
		else return "0";
	}

	@Override
	public String toString() {
		return lhs.toString() + " <= " + rhs.toString();
	}
}
