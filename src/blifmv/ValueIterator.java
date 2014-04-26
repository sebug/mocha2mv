package blifmv;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Iterates over the value range of a list of variables.
 * TODO: Change the representation from Strings to
 * something faster.
 */

public class ValueIterator implements Iterator<List<String>> {
	
	private final List<Var> variableRange;
	private int index = 0; // Which element we should return.
	private final int totalNumElements;
	
	public ValueIterator(List<Var> variableRange) {
		this.variableRange = variableRange;
		int i = 1;
		for(Var v: this.variableRange) {
			if( v.getType().getNumElements() <= 0 ) {
				throw new Error("Should not happen: 0 or negative type range.");
			}
			i *= v.getType().getNumElements();
		}
		this.totalNumElements = i;
	}

	/**
	 * Becomes false when we have enumerated all the elements.
	 */
	public boolean hasNext() {
		return this.index < this.totalNumElements;
	}

	public List<String> next() {
		List<String> ret = new LinkedList<String>();
		int tot = this.totalNumElements;
		int pos;
		int rest = this.index;
		
		for(Var x: this.variableRange) {
			tot = tot / x.getType().getNumElements();
			if( tot > 0) {
				pos = rest / tot;
			} else {
				pos = rest;
			}
			rest = rest - pos * tot;
			ret.add(x.getType().get(pos));
		}
		this.index++;
		return ret;
	}

	public void remove() {
		// TODO Auto-generated method stub

	}

}
