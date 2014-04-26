package expr;

/**
 * Represents a binary relation.
 *
 */
public abstract class BinaryRelation extends ArithExpression {
	protected final ArithExpression lhs;
	protected final ArithExpression rhs;
	
	public BinaryRelation(ArithExpression lhs, ArithExpression rhs) {
		super();
		this.lhs = lhs;
		this.rhs = rhs;
	}
}
