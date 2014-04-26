package blifmv;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a set of variables associated with a type.
 *
 */
public class TypedVarSet {
	private final List<String> vars = new LinkedList<String>();
	private String type;
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return this.type;
	}
	
	public void addVar(String v) {
		vars.add(v);
	}
	
	public List<String> getVars() {
		return this.vars;
	}
	
	public int size() {
		return this.vars.size();
	}
}
