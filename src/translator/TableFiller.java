package translator;

import java.util.LinkedList;
import java.util.List;

import mocha.parser.ASTAtom;
import mocha.parser.ASTAtomList;
import mocha.parser.ASTAtomVarDecl;
import mocha.parser.ASTAtomVarDeclList;
import mocha.parser.ASTAtomVarList;
import mocha.parser.ASTDefaultGuardedCmd;
import mocha.parser.ASTInitUpdateDecl;
import mocha.parser.ASTModuleVarDeclList;
import mocha.parser.ASTName;
import blifmv.Model;
import blifmv.Table;
import blifmv.Var;

/**
 * Creates the table for a model out of an atom list.
 *
 */
public class TableFiller extends AbstractVisitor {
	private Model m; // The model that we will try to fill.
	
	private final static int GET_ATOM_NAME = 0;
	private final static int GET_CONTROLS_NAMES = 1;
	private final static int GET_AWAITS_NAMES = 2;
	private final static int GET_READS_NAMES = 3;
	private final static int GET_INIT_DECLARATIONS = 4;
	private final static int GET_UPDATE_DECLARATIONS = 5;
	
	private int state = GET_ATOM_NAME;
	
	public TableFiller(Model m) {
		this.m = m;
	}
	
	public Object visit(ASTAtomList astatomlist, Object obj) {
		astatomlist.childrenAccept(this, null);
		return null;
	}
	
	/**
	 * Parsing one atom will be done in the following way:
	 * 1) Get controlled, awaited and read variables in
	 * 2) Add the transformations in the body
	 * 3) Add additional transitions if lazy or event.
	 */
	public Object visit(ASTAtom astatom, Object obj) {
		state = GET_ATOM_NAME;
		Table t = new Table(astatom.isLazy,astatom.isEvent,m);
		astatom.childrenAccept(this, t);
		m.addTable(t);
		return null;
	}
	
	/**
	 * Involved in the setting of different names, for atom name,
	 * variable name, etc.
	 */
	public Object visit(ASTName astname, Object obj) {
		Table t = (Table)obj;
		switch(state) {
		case GET_ATOM_NAME:
			t.setTableName(astname.name);
			break;
		case GET_AWAITS_NAMES:
			Var v = m.getInputVar(astname.name);
			if (v == null) v = m.getOutputVar(astname.name); // Might also be in the output.
			if( v != null ) {
				t.addInput(v.prime()); // The primed version will be input.
			} else {
				throw new Error("Did not find the awaited variable " + astname.name);
			}
			break;
		case GET_CONTROLS_NAMES:
			Var v2 = m.getOutputVar(astname.name);
			if( v2 != null ) {
				t.addOutput(v2.prime());
				// if we are lazy, then we also have to read this variable
				// in the input
				t.addInput(v2.unprime());
			}
			break;
		case GET_READS_NAMES:
			Var v3 = m.getInputVar(astname.name);
			// Might also come from output
			if( v3 == null) v3 = m.getOutputVar(astname.name);
			if( v3 != null ) {
				t.addInput(v3);
			}
		}
		return null;
	}
	
	// Forward
	public Object visit(ASTAtomVarDeclList astatomvardecllist, Object obj) {
		Table t = (Table)obj;
		astatomvardecllist.childrenAccept(this, t);
		return null;
	}
	
	public Object visit(ASTAtomVarDecl astatomvardecl, Object obj) {
		Table t = (Table)obj;
		switch( astatomvardecl.type ) {
		case ASTAtomVarDecl.AWAITS:
			state = GET_AWAITS_NAMES;
			break;
		case ASTAtomVarDecl.CONTROLS:
			state = GET_CONTROLS_NAMES;
			break;
		default:
			state = GET_READS_NAMES;
		}
		astatomvardecl.childrenAccept(this, t);
		return null;
	}
	
	// Forward
	public Object visit(ASTAtomVarList astatomvarlist, Object obj) {
		Table t = (Table)obj; // Downcasting hell, but avoids going too far without knowing with what.
		astatomvarlist.childrenAccept(this, t);
		return null;
	}
	
	// Ends
	public Object visit(ASTModuleVarDeclList astmodulevardecllist, Object obj) {
		return null;
	}
	
	public Object visit(ASTInitUpdateDecl astinitupdatedecl, Object obj) {
		Table t = (Table)obj;
		state = GET_INIT_DECLARATIONS;
		astinitupdatedecl.jjtGetChild(0).jjtAccept(this, t);
		if( astinitupdatedecl.jjtGetNumChildren() > 1) { // Also update declarations, otherwise just initupdate
			state = GET_UPDATE_DECLARATIONS;
			astinitupdatedecl.jjtGetChild(1).jjtAccept(this, t);
		}
		return null;
	}
	
	public Object visit(ASTDefaultGuardedCmd astdefaultguardedcmd, Object obj) {
		Table t = (Table)obj;
		if( state == GET_INIT_DECLARATIONS) {
			CommandVisitor resetCommands = new CommandVisitor(t.getSafeInputVars(),t.getOutputVars());
			List<List<String>> inittable = new LinkedList<List<String>>();
			for(int i = 0; i < astdefaultguardedcmd.jjtGetNumChildren(); i++) {
				inittable.addAll((List<List<String>>)astdefaultguardedcmd.jjtGetChild(i).jjtAccept(resetCommands, t));
			}
			t.setResetAction(inittable);
		} else if (state == GET_UPDATE_DECLARATIONS) {
			CommandVisitor updateCommands = new CommandVisitor(t.getInputVars(),t.getOutputVars());
			List<List<String>> updatetable = new LinkedList<List<String>>();
			for(int i = 0; i < astdefaultguardedcmd.jjtGetNumChildren(); i++) {
				updatetable.addAll((List<List<String>>)astdefaultguardedcmd.jjtGetChild(i).jjtAccept(updateCommands, t));
			}
			t.setUpdateAction(updatetable);
		}
		return null;
	}
}
