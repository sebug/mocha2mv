package blifmv;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Represents a BLIF-MV model
 *
 */
public class Model {
	private final ModelCollection mc;
	private String name;
	private final List<Var> inputs = new LinkedList<Var>();
	private final List<Var> outputs = new LinkedList<Var>();
	private final List<Var> private_vars = new LinkedList<Var>();
	private final List<Var> nondet_vars = new LinkedList<Var>();
	private final List<Table> tables = new LinkedList<Table>();
	
	private List<SubCircuit> composition = new LinkedList<SubCircuit>();
	private Set<String> controlledElsewhere = new TreeSet<String>();
	private Set<String> outputsOfSubcircuits = new TreeSet<String>();
	
	public Model(ModelCollection mc) {
		this.mc = mc;
	}
	
	/**
	 * Sets the name of the model
	 * @param n the new name
	 */
	public void setName(String n) {
		this.name = n;
	}
	
	/**
	 * @return the name of the model
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Adds a table controlling outputs to this model.
	 * @param t the table to add.
	 */
	public void addTable(Table t) {
		this.tables.add(t);
	}
	
	/**
	 * @return the list of input variables
	 */
	public List<Var> getInput() {
		return this.inputs;
	}
	
	/**
	 * @return the list of output variables
	 */
	public List<Var> getOutput() {
		return this.outputs;
	}
	
	/**
	 * Gives back the nondeterministic variables that were used.
	 * @return the list of nondeterministic variables used in this module.
	 */
	public List<Var> getNondets() {
		return this.nondet_vars;
	}
	
	public Var getInputVar(String name) {
		Var ret = null;
		for(Var v : this.inputs) {
			if( v.getName().equals(name) ) {
				ret = v;
			}
		}
		// Also check the private variables.
		if( ret == null ) {
			for( Var v : this.private_vars) {
				if( v.getName().equals(name)) {
					ret = v;
				}
			}
		}
		return ret;
	}
	
	public Var getOutputVar(String name) {
		Var ret = null;
		for(Var v : this.outputs) {
			if( v.getName().equals(name) ) {
				ret = v;
			}
		}
		// Also check the private variables.
		if( ret == null ) {
			for( Var v : this.private_vars) {
				if( v.getName().equals(name)) {
					ret = v;
				}
			}
		}
		return ret;
	}
	
	/**
	 * Prints the BLIF-MV representation of this model.
	 * @param out Where to print
	 */
	public void printMV(PrintStream out) {
		out.println(".model " + this.name);
		if( this.inputs.size() > 0) {
			out.print(".inputs ");
			List<String> inputvars = new LinkedList<String>();
			for(Var v: this.inputs) {
				if( !this.outputs.contains(v) && !this.outputsOfSubcircuits.contains(v.getName())) {
					inputvars.add(v.unprime().getName());
					inputvars.add(v.prime().getName());
				} // else they will be handled internally.
			}
			out.println(join(inputvars," "));
		}
		if( this.nondet_vars.size() > 0 ) {
			out.println("# Non-deterministic pseudo-inputs");
			out.print(".inputs ");
			for(Var v: this.nondet_vars) {
				out.print(v.getName()+ " ");
			}
			out.println();
		}
		if( this.outputs.size() > 0) {
			out.print(".outputs ");
			List<String> outputvars = new LinkedList<String>();
			for(Var v: this.outputs) {
				outputvars.add(v.unprime().getName());
				outputvars.add(v.prime().getName());
			}
			out.println(join(outputvars," "));
		}
		// Now print the multi-value directives
		Set<Var> all = new HashSet<Var>();
		all.addAll(this.inputs);
		all.addAll(this.outputs);
		all.addAll(this.private_vars);
		List<TypedVarSet> mvs = new LinkedList<TypedVarSet>();
		for(Var v : all) {
			out.println(".mv " + v.getName() + "," + v.prime().getName() + " " + v.getType().getTypeRepresentation());
		}
		
		if( !this.nondet_vars.isEmpty() ) {
			// We have multivariate nondeterminism vars
			for( Var v: this.nondet_vars) {
				out.println(".mv " + v.getName() + " " + v.getType().getTypeRepresentation());
			}
		}
		
		// Print the latches
		for(Var v: outputs) {
			if( !controlledElsewhere.contains(v.getName())) {
				out.println(".latch " + v.prime().getName() + " " + v.getName());
			}
		}
		// Also for the private variables
		for(Var v: this.private_vars) {
			if( !controlledElsewhere.contains(v.getName())) {
				out.println(".latch " + v.prime().getName() + " " + v.getName());
			}
		}
		
		// Before the tables, print the subcircuits
		for(SubCircuit sc : this.composition) {
			sc.printMV(out);
		}
		
		// Also print all the tables
		for(Table t : this.tables) {
			t.printMV(out);
		}
		out.println(".end");
	}
	
	/**
	 * Joins a list.
	 * @param elems
	 * @param spacer
	 * @return
	 */
	private String join(List<String> elems, String spacer) {
		StringBuffer sb = new StringBuffer();
		int i = elems.size();
		for(String el : elems ) {
			i--;
			sb.append(el);
			if( i > 0) {
				sb.append(spacer);
			}
		}
		return sb.toString();
	}
	
	/**
	 * Returns the primed versions of the variables given as inputs.
	 * @param vars The variables to prime
	 * @return the primed vars.
	 */
	private List<String> primes(List<String> vars) {
		List<String> primes = new LinkedList<String>();
		for(String v : vars) {
			primes.add(v + "'");
		}
		return primes;
	}
	
	@Override
	public String toString() {
		return this.name;
	}

	public List<Var> getPrivate() {
		return this.private_vars;
	}

	/**
	 * Adds a pseudo-input for a table to simulate nondeterminism.
	 * @param myNondetVar the variable to be used.
	 */
	public void addNondetVar(Var myNondetVar) {
		this.nondet_vars.add(myNondetVar);
	}

	public void addSubcircuit(SubCircuit cc) {
		this.composition.add(cc);
		cc.informEnclosingModel();
	}
	
	/**
	 * The model collection where this model is referred in.
	 * @return
	 */
	public ModelCollection associatedModelCollection() {
		return this.mc;
	}

	/**
	 * Notes that the variable is controlled elsewhere.
	 * @param v the variable.
	 */
	public void setControlledElsewhere(Var v) {
		controlledElsewhere.add(v.getName());
	}
	
	/**
	 * Declares that a variable is an output of a subcircuit.
	 * @param v the variable that is output of a subcircuit.
	 */
	public void setOutputOfSubcircuit(Var v) {
		this.outputsOfSubcircuits.add(v.getName());
	}
}
