package expr;

import java.util.Iterator;
import java.util.List;

import blifmv.Var;

/**
 * Represents the modifier ?
 * Returns true if the primed version of the variable has a different
 * value as the unprimed one.
 *
 */
public class HasChanged extends ArithExpression {
	private final Var concernedVar;
	
	public HasChanged(Var concernedVar) {
		this.concernedVar = concernedVar;
	}

	@Override
	public String evaluate(List<Var> evaluateRange, List<String> valuation) {
		String valueBefore = null;
		String valueNow = null;
		
		Iterator<Var> varIt = evaluateRange.iterator();
		Iterator<String> valIt = valuation.iterator();
		
		while( varIt.hasNext() && valIt.hasNext() ) {
			Var currentVar = varIt.next();
			String value = valIt.next();
			if( currentVar.equals(this.concernedVar.unprime()) ) {
				valueBefore = value;
			}
			if( currentVar.equals(this.concernedVar.prime()) ) {
				valueNow = value;
			}
		}
		
		if( valueBefore == null || valueNow == null ) {
			throw new Error("Expected to get a valuation for " + concernedVar.unprime().getName() + " and " +
					concernedVar.prime().getName() + " but did not get both.");
		}

		return valueBefore.equals(valueNow) ? "0" : "1"; // 1 If different value.
	}

	@Override
	public String toString() {
		return this.concernedVar.unprime().getName() + "?";
	}
}
