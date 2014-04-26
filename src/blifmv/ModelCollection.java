package blifmv;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Represents the contents of a BLIF-MV file
 *
 */

public class ModelCollection {
	private final List<Model> models;
	
	private final Map<String, Type> types;
	
	public ModelCollection() {
		this.models = new LinkedList<Model>();
		this.types = new HashMap<String, Type>();
		
		// Now, add primitive types
		this.types.put("bool", new SequenceType("bool",2));
		this.types.put("event", new SequenceType("event",2));
	}
	
	public void addModel(Model modelname) {
		models.add(modelname);
	}
	
	/**
	 * Adds a type to the list of types in this model. Note that this is just needed to resolve the output.
	 * @param tpe
	 * @param possibleValues
	 */
	public void addType(String tname, Type tpe) {
		this.types.put(tname, tpe);
	}
	
	/**
	 * Returns the model associated with this name.
	 * @param modelname name of the model
	 * @return the model with this name.
	 */
	public Model getModel(String modelname) {
		Model res = null;
		for(Model m: this.models) {
			if(m.getName() != null && m.getName().equals(modelname)) {
				res = m;
			}
		}
		return res;
	}
	
	public Map<String,Type> getTypes() {
		return this.types;
	}
	
	public Type getPossibleValues(String tpe) {
		Type vals = this.types.get(tpe);
		return vals;
	}
	
	public void printMV(PrintStream out) {
		for(Model m : models) {
			m.printMV(out);
			out.println();
		}
	}
}
