package expr;

import java.util.List;

import blifmv.Var;

/**
 * Represents an enumeration value used in a named sequence.
 *
 */
public class EnumerationValue extends ArithExpression {
	private final String value;
	
	public EnumerationValue(String value) {
		this.value = value;
	}

	@Override
	public String evaluate(List<Var> evaluateRange, List<String> valuation) {
		return value;
	}

	@Override
	public String toString() {
		return this.value;
	}
}
