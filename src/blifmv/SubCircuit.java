package blifmv;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import translator.Mocha2MV;


/**
 * Represents a collection of subcircuits of a model.
 *
 */
public class SubCircuit {
	private final Model enclosingModel;
	private List<String> includedModelNames = new LinkedList<String>();
	private Set<Model> includedModels = new HashSet<Model>();
	private final Map<String,String> nameMappings = new HashMap<String,String>();
	private final Set<String> hiddenVariables = new TreeSet<String>();
	
	public SubCircuit(Model enclosingModel) {
		this.enclosingModel = enclosingModel;
	}
	
	/**
	 * Sets the model that we reference in this composite circuit. Note that we
	 * only store the name, because the model might not yet be there.
	 * @param parentModel
	 */
	public void addParent(String parentModel) {
		this.includedModelNames.add(parentModel);
	}
	
	/**
	 * In the module to be built, replace every occurence of fromVar with a
	 * new var with the same type but the name toName.
	 * @param fromVar which variable name to rename
	 * @param toName the new name for the variable.
	 */
	public void addVariableRenaming(String fromVar, String toName) {
		nameMappings.put(fromVar, toName);
		nameMappings.put(fromVar+"'", toName+"'"); // Also rename the primed vars.
	}
	
	/**
	 * Tells the parent which input and output variables it has to set.
	 */
	public void informEnclosingModel() {
		// First, resolve the parent model
		for( String includedModelName: this.includedModelNames) {
			Model current = this.enclosingModel.associatedModelCollection().getModel(includedModelName);
			includedModels.add(current);
			for(Var v: current.getInput()) {
				if( !hiddenVariables.contains(v.getName())) {
					addOnceToInput(possiblyRename(v));
				} else {
					// Private variable.
					addOnceToPrivate(v); // Hidden should not be renamed.
				}
				this.enclosingModel.setControlledElsewhere(possiblyRename(v)); // So we don't need to create a latch.
			}
			// Also add the corresponding outputs
			for(Var v: current.getOutput()) {
				if( !hiddenVariables.contains(v.getName())) {
					addOnceToOutput(possiblyRename(v));
				} else {
					// Remove an eventual occurence as input variable.
					addOnceToPrivate(v);
				}
				this.enclosingModel.setControlledElsewhere(possiblyRename(v));
			}
		}
		
		// Now, see which variables are inputs and outputs: They shall only be outputs because they are used internally
		Set<Var> toRemove = new HashSet<Var>();
		for( Var in: this.enclosingModel.getInput() ) {
			for( Var out: this.enclosingModel.getOutput() ) {
				if( in.unprime().equals(out.unprime()) ) {
					if( Mocha2MV.DEBUG ) {
						System.out.println("Controlled and input: " + in.getName());
					}
					toRemove.add(in);
				}
			}
		}
		this.enclosingModel.getInput().removeAll(toRemove);
		
		// Propagate nondeterminism
		for( Model m : this.includedModels ) {
			for( Var nondet: m.getNondets() ) {
				if( !this.enclosingModel.getNondets().contains(nondet) ) {
					if( Mocha2MV.DEBUG ) {
						System.out.println("Adding nondet var " + nondet + " with " + nondet.getType().getNumElements() + " elements");
					}
					this.enclosingModel.addNondetVar(nondet);
				}
			}
		}
	}
	
	/**
	 * Renames a variable if there is a mapping for these subcircuits.
	 * @param v the variable to possibly rename
	 * @return the possibly renamed variable.
	 */
	private Var possiblyRename(Var v) {
		Var resulting = null;
		if( this.nameMappings.containsKey(v.getName()) ) {
			resulting = v.isPrimed() ? new Var(this.nameMappings.get(v.getName()),v.getType()).prime()
					: new Var(this.nameMappings.get(v.getName()),v.getType());
		} else {
			resulting = v;
		}
		return resulting;
	}
	
	/**
	 * Checks whether a variable is already present in the inputs or outputs.
	 * @param v The variable to check
	 * @return whether the variable is already present in input or output
	 */
	private boolean alreadyThere(Var v) {
		return this.enclosingModel.getInput().contains(v) || this.enclosingModel.getOutput().contains(v);
	}
	
	/**
	 * Adds a variable to the list of private variables.
	 * @param v the variable to add.
	 */
	private void addOnceToPrivate(Var v) {
		if( !this.enclosingModel.getPrivate().contains(v) )
			this.enclosingModel.getPrivate().add(v);
	}
	
	private void addOnceToInput(Var v) {
		if( !this.enclosingModel.getInput().contains(v) )
			this.enclosingModel.getInput().add(v);
	}
	
	private void addOnceToOutput(Var v) {
		if( !this.enclosingModel.getOutput().contains(v))
			this.enclosingModel.getOutput().add(v);
	}

	public void printMV(PrintStream out) {
		for(Model includedModel : this.includedModels) {
			out.print(".subckt " + includedModel.getName() + " " + includedModel.getName() + "_local "); // Has to have a local instatiation
			List<Var> inputsoutputs = new LinkedList<Var>();
			inputsoutputs.addAll(includedModel.getInput());
			inputsoutputs.addAll(includedModel.getOutput());
			for(Var v : inputsoutputs) {
				if( this.nameMappings.containsKey(v.getName())) {
					out.print(v.getName() + " = " + nameMappings.get(v.getName()) + " ");
					out.print(v.prime().getName() + " = " + nameMappings.get(v.prime().getName()) + " ");
				} else {
					out.print(v.getName() + " = " + v.getName() + " "); // has the same name inside.
					out.print(v.prime().getName() + " = " + v.prime().getName() + " "); // has the same name inside.
				}
			}
			
			// Also, add the nondeterministic variables
			for( Var v: includedModel.getNondets() ) {
				out.print(v.getName() + " = " + v.getName() + " ");
			}
			out.println();
		}
	}

	/**
	 * Declares a variable as hidden (therefore it won't be added to the output variables
	 * but to the private ones)
	 * @param varname the name of the variable to hide.
	 */
	public void setHidden(String varname) {
		this.hiddenVariables.add(varname);
	}
}
