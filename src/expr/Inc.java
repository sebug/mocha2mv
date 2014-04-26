package expr;

import java.util.List;

import blifmv.Var;

/**
 * Increments the variable by some value.
 * In Mocha, incrementing beyond the range wraps over if i in 0..3,
 * i at the moment 2 and we increment i by 2, we get i = 0. This
 * has to be taken into account in the assignment.
 */
public class Inc extends ArithmeticOp {
	public Inc(ArithExpression what, ArithExpression by) {
		super(what,by);
	}

	@Override
	public String evaluate(List<Var> evaluateRange, List<String> valuation) {
		String expr = what.evaluate(evaluateRange, valuation);
		String incWith = by.evaluate(evaluateRange, valuation);
		
		try {
			int base = Integer.parseInt(expr);
			int by = Integer.parseInt(incWith);
			return "" + (base + by);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new Error("Failure converting to int.");
		}
	}

	@Override
	public String toString() {
		return "(inc " + this.what + " by " + this.by+ ")";
	}
}
