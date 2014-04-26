package expr;

import java.util.List;

import blifmv.Var;

public class And extends ArithExpression {
	private final ArithExpression left;
	private final ArithExpression right;
	
	public And(ArithExpression left, ArithExpression right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public String evaluate(List<Var> evaluateRange, List<String> valuation) {
		String leftVal = left.evaluate(evaluateRange, valuation);
		if( !leftVal.equals("1")) {
			// Short-circuit
			return "0";
		} else {
			String rightVal = right.evaluate(evaluateRange, valuation);
			return rightVal;
		}
	}

	@Override
	public String toString() {
		return "(" + left + " & " + right + ")";
	}
}
