package expr;

import java.util.List;

import blifmv.Var;

/**
 * Decrements a number by some value.
 *
 */
public class Dec extends ArithmeticOp {
	
	public Dec(ArithExpression what, ArithExpression by) {
		super(what,by);
	}

	@Override
	public String evaluate(List<Var> evaluateRange, List<String> valuation) {
		String expr = what.evaluate(evaluateRange, valuation);
		String incWith = by.evaluate(evaluateRange, valuation);
		
		try {
			int base = Integer.parseInt(expr);
			int by = Integer.parseInt(incWith);
			return "" + (base - by);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new Error("Failure converting to int.");
		}
	}

	@Override
	public String toString() {
		return "(dec " + this.what + " by " + this.by + ")";
	}
}
