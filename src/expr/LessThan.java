package expr;

import java.util.List;

import blifmv.Var;

/**
 * lhs < rhs
 *
 */
public class LessThan extends BinaryRelation {
	public LessThan(ArithExpression lhs, ArithExpression rhs) {
		super(lhs,rhs);
	}

	@Override
	public String evaluate(List<Var> evaluateRange, List<String> valuation) {
		try {
			int a = Integer.parseInt(lhs.evaluate(evaluateRange, valuation));
			int b = Integer.parseInt(rhs.evaluate(evaluateRange, valuation));
			
			if( a < b) return "1";
			else return "0";
		} catch (NumberFormatException e) {
			// Could not extract a number. Clearly, the both sides are not equal then.
			return "0";
		}
	}
	
	@Override
	public String toString() {
		return lhs.toString() + " < " + rhs.toString();
	}
}
