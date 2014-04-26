package expr;

import java.util.List;

import blifmv.Var;

/**
 * Indicates that the subexpression is unary with a determined operator.
 * @author sgfeller
 *
 */
public class OperationIndicator extends ArithExpression {
	private final int operator;
	private final ArithExpression body;
	
	public OperationIndicator(int operator, ArithExpression body) {
		this.operator = operator;
		this.body = body;
	}
	
	public int getOperator() {
		return this.operator;
	}

	@Override
	public String evaluate(List<Var> evaluateRange, List<String> valuation) {
		// Just pass on to the child
		return this.body.evaluate(evaluateRange, valuation);
	}

	@Override
	public String toString() {
		return this.body.toString();
	}
}
