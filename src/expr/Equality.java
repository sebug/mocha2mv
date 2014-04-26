package expr;

import java.util.List;

import blifmv.Var;


/**
 * Equality between two boolean expressions: only matches if left hand side and right
 * hand side are equal.
 *
 */
public class Equality extends BinaryRelation {
	
	public Equality(ArithExpression lhs, ArithExpression rhs) {
		super(lhs,rhs);
	}
	
	@Override
	public String toString() {
		return "(" + lhs + " = " + rhs + ")";
	}

	@Override
	public String evaluate(List<Var> evaluateRange, List<String> valuation) {
		if( lhs.evaluate(evaluateRange, valuation).equals(rhs.evaluate(evaluateRange, valuation))) return "1";
		else return "0";
	}
}
