package blifmv;

import java.util.LinkedList;
import java.util.List;

/**
 * Like in Mocha, the sequences have to start with 0, otherwise
 * we can't represent them succinctly.
 *
 */
public class SequenceType extends Type {
	private final int size;
	
	public SequenceType(String name, int size) {
		super(name);
		this.size = size;
	}
	
	@Override
	public int getNumElements() {
		// TODO Auto-generated method stub
		return this.size;
	}

	@Override
	public String getTypeRepresentation() {
		return "" + this.size;
	}
	
	@Override
	public String getDefaultValue() {
		return "0";
	}
	
	@Override
	public String toString() {
		return "0 .. " + (this.size - 1);
	}
	
	@Override
	public String get(int index) {
		assert(index < size); // Otherwise we would access an element that does not exist
		return "" + index;
	}
	
	@Override
	public int getValPos(String value) {
		int v = Integer.parseInt(value);
		if( v >= 0 && v < size) {
			return v;
		} else {
			return -1; // Value not found.
		}
	}

	@Override
	public List<String> getAllValues() {
		List<String> ret = new LinkedList<String>();
		for(int i = 0; i < size; i++) {
			ret.add(""+i);
		}
		return ret;
	}
}
