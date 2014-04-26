package expr;

import java.util.List;

import blifmv.Var;

/**
 * Wrapper for a boolean value to occur inside an expression.
 *
 */
public class BooleanConstant extends ArithExpression {
	private final boolean value;
	
	private BooleanConstant(boolean value) {
		this.value = value;
	}
	
	public static final BooleanConstant TRUE = new BooleanConstant(true);
	
	public static final BooleanConstant FALSE = new BooleanConstant(false);
	
	@Override
	public String toString() {
		return value ? "true" : "false";
	}

	@Override
	public String evaluate(List<Var> evaluateRange, List<String> valuation) {
		if( value ) {
			return "1";
		} else {
			return "0";
		}
	}
}
