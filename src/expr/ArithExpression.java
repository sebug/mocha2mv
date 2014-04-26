package expr;

import java.util.List;

import blifmv.Var;

/**
 * Represents an expression that can be evaluated on a range of input values.
 */
public abstract class ArithExpression {
	/**
	 * Evaluates the expression on a given valuation. Returns "1" if this gives back true, otherwise
	 * "0".
	 * @param evaluateRange Over which variables to evaluate
	 * @param valuation The current assigned values to the different variables.
	 * @return Whether the boolean formula is satisfied under this valuation.
	 */
	public abstract String evaluate(List<Var> evaluateRange, List<String> valuation);
}
