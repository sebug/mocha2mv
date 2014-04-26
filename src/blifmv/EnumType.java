package blifmv;

import java.util.List;

public class EnumType extends Type {
	private final List<String> possibleValues;
	
	public EnumType(String name, List<String> possibleValues) {
		super(name);
		this.possibleValues = possibleValues;
	}
	
	@Override
	public int getNumElements() {
		return possibleValues.size();
	}

	public List<String> getPossibleValues() {
		return this.possibleValues;
	}

	@Override
	public String getTypeRepresentation() {
		StringBuffer ret = new StringBuffer(""+this.getNumElements());
		for( String s : this.possibleValues) {
			ret.append(" " + s);
		}
		return ret.toString();
	}
	
	@Override
	public String getDefaultValue() {
		if( possibleValues.size() > 0 ) {
			return possibleValues.get(0);
		} else {
			return "0";
		}
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(String s : this.possibleValues) {
			sb.append(s + " ");
		}
		return sb.toString();
	}
	
	@Override
	public String get(int index) {
		return this.possibleValues.get(index);
	}
	
	@Override
	public int getValPos(String value) {
		int pos = -1;
		int i = 0;
		for(String pv: this.possibleValues) {
			if( pv.equals(value)) {
				pos = i;
			}
			i++;
		}
		return pos;
	}

	@Override
	public List<String> getAllValues() {
		return this.possibleValues;
	}
}
