package expr;

import java.util.List;

import blifmv.Var;

/**
 * A logical or.
 *
 */
public class Or extends ArithExpression {
	private final ArithExpression left;
	private final ArithExpression right;
	
	public Or(ArithExpression left, ArithExpression right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public String evaluate(List<Var> evaluateRange, List<String> valuation) {
		String leftVal = left.evaluate(evaluateRange, valuation);
		if( leftVal.equals("1") ) {
			return "1";
		} else {
			return right.evaluate(evaluateRange, valuation);
		}
	}

	@Override
	public String toString() {
		return "(" + left + " | " + right + ")";
	}
}
