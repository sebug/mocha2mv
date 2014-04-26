package blifmv;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Builds the list of variables from a TypedNonemptyVarList subtree.
 *
 */
public class VarListBuilder {
	private Type varsType;
	private Map<String,Type> types;
	private List<String> varNames = new LinkedList<String>();
	
	public VarListBuilder(Map<String,Type> types) {
		this.types = types;
	}
	
	public void setType(String t) {
		this.varsType = types.get(t);
	}
	
	public void addVarName(String name) {
		this.varNames.add(name);
	}
	
	public void setAnonymousRangeType(int size) {
		Type t = new SequenceType("0.."+(size - 1),size);
		types.put("0.."+(size - 1), t);
		this.varsType = t;
	}
	
	/**
	 * Builds a list of variables.
	 * @return the list of variables.
	 */
	public List<Var> build() {
		List<Var> acc = new LinkedList<Var>();
		for(String name : varNames) {
			acc.add(new Var(name,varsType));
		}
		return acc;
	}
}
