package expr;

/**
 * An arithmetic operation like increment or decrement.
 *
 */
public abstract class ArithmeticOp extends ArithExpression {
	protected final ArithExpression what; // Base expression
	protected final ArithExpression by; // applied to by
	
	public ArithmeticOp(ArithExpression what, ArithExpression by) {
		this.what = what;
		this.by = by;
	}
}
