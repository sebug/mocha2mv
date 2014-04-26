package expr;

import java.util.List;

import blifmv.Var;


/**
 * y' := if (y) then inc y by 1 else y
 * Represents an if-then-else expression.
 *
 */
public class If extends ArithExpression {
	private final ArithExpression test;
	private final ArithExpression thenBody;
	private final ArithExpression elseBody;
	
	public If(ArithExpression test, ArithExpression thenBody, ArithExpression elseBody) {
		this.test = test;
		this.thenBody = thenBody;
		this.elseBody = elseBody;
	}

	@Override
	public String evaluate(List<Var> evaluateRange, List<String> valuation) {
		String test = this.test.evaluate(evaluateRange, valuation);
		if( test.equals("1")) {
			// true, execute then
			return this.thenBody.evaluate(evaluateRange, valuation);
		} else {
			// false, execute else
			return this.elseBody.evaluate(evaluateRange, valuation);
		}
	}

	@Override
	public String toString() {
		return "if ( " + this.test + " ) then " + this.thenBody + " else " + this.elseBody + " fi ";
	}
}
