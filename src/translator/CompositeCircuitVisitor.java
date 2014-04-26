package translator;

import java.util.LinkedList;
import java.util.List;

import mocha.parser.ASTAtomicModuleDecl;
import mocha.parser.ASTCompModuleDecl;
import mocha.parser.ASTHideModuleDecl;
import mocha.parser.ASTModuleName;
import mocha.parser.ASTNonemptyIdList;
import mocha.parser.ASTParenModuleDecl;
import mocha.parser.ASTRenameModuleDecl;
import blifmv.SubCircuit;

/**
 * This circuit collects information regarding subcircuits.
 *
 */
public class CompositeCircuitVisitor extends AbstractVisitor {
	private final SubCircuit cc;
	
	public CompositeCircuitVisitor(SubCircuit cc) {
		this.cc = cc;
	}

	public Object visit(ASTRenameModuleDecl astrenamemoduledecl, Object obj) {
		List<String> parenModuleNames = new LinkedList<String>();
		astrenamemoduledecl.jjtGetChild(0).jjtAccept(this, parenModuleNames);
		
		// The renames are a pair of a list of variables in the original module to new variable names.
		List<String> fromRename = (List<String>)astrenamemoduledecl.jjtGetChild(1).jjtAccept(this, null);
		List<String> toRename = (List<String>)astrenamemoduledecl.jjtGetChild(2).jjtAccept(this, null);
		for(int i = 0; i < fromRename.size(); i++) {
			cc.addVariableRenaming(fromRename.get(i),toRename.get(i));
		}
		for(String parenModuleName : parenModuleNames) {
			this.cc.addParent(parenModuleName);
		}
		return null;
	}
	
	/**
	 * Instructs the module to hide some variables.
	 */
	public Object visit(ASTHideModuleDecl asthidemoduledecl, Object obj) {
		List<String> variablesToHide = (List<String>)asthidemoduledecl.jjtGetChild(0).jjtAccept(this, null);
		for(String varname : variablesToHide) {
			cc.setHidden(varname);
		}
		// Now look at the modules the model is composed of
		List<String> parenModuleNames = new LinkedList<String>();
		asthidemoduledecl.jjtGetChild(1).jjtAccept(this, parenModuleNames);
		for(String n: parenModuleNames) {
			this.cc.addParent(n);
		}
		return null;
	}
	
	/**
	 * Just forwards to the module name.
	 */
	public Object visit(ASTParenModuleDecl astparenmoduledecl, Object obj) {
		List<String> parenModuleNames = (List<String>)obj;
		astparenmoduledecl.jjtGetChild(0).jjtAccept(this, parenModuleNames);
		return null;
	}
	
	/**
	 * A module name. Used in the parent module declaration, for instance.
	 */
	public Object visit(ASTModuleName astmodulename, Object obj) {
		List<String> ret = (List<String>)obj;
		ret.add(astmodulename.name);
		return ret;
	}
	
	/**
	 * A list of variable identifiers.
	 */
	public Object visit(ASTNonemptyIdList astnonemptyidlist, Object obj) {
		List<String> ret = new LinkedList<String>();
		for(int i = 0; i < astnonemptyidlist.names.size(); i++) {
			ret.add((String)astnonemptyidlist.names.get(i));
		}
		return ret;
	}

	/**
	 * A composite module declaration: This just makes a subcircuit consisting of many parents.
	 */
	public Object visit(ASTCompModuleDecl arg0, Object arg1) {
		List<String> compositeModuleNames = new LinkedList<String>();
		arg0.childrenAccept(this, compositeModuleNames);
		// Add all the module names we found.
		for( String res : compositeModuleNames) {
			this.cc.addParent(res);
		}
		return compositeModuleNames;
	}
	
	// Ends: We ended in a circuit that is not composite at all
	public Object visit(ASTAtomicModuleDecl astatomicmoduledecl, Object obj) {
		return null;
	}
}
