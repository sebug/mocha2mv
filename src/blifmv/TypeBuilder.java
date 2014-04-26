package blifmv;

import java.util.LinkedList;
import java.util.List;

/**
 * Builds a type after having traversed the tree.
 *
 */
public class TypeBuilder {
	private int size = 2;
	private List<String> possibleValues = new LinkedList<String>();
	private boolean isSeq = true;
	private String name;
	
	public TypeBuilder(String name) {
		this.name = name;
	}
	
	public Type build() {
		if( isSeq ) {
			return new SequenceType(this.name,size);
		} else {
			return new EnumType(this.name,possibleValues);
		}
	}
	
	/**
	 * If we add a size, then we will build a sequence type.
	 * @param s size
	 */
	public void setSize(int s) {
		this.isSeq = true;
		this.size = s;
	}
	
	/**
	 * If we add a possible value, then it's an enum type.
	 * @param s possible value
	 */
	public void addPossibleValue(String s) {
		this.isSeq = false;
		possibleValues.add(s);
	}
	
	public String getName() {
		return this.name;
	}
}
