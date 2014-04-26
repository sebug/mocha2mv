package expr;

import java.util.List;

import blifmv.Var;

public class NumberConstant extends ArithExpression {
	private final int value;
	
	public NumberConstant(int value) {
		this.value = value;
	}

	@Override
	public String evaluate(List<Var> evaluateRange, List<String> valuation) {
		return "" + value;
	}
	
	@Override
	public String toString() {
		return "" + value;
	}
}
