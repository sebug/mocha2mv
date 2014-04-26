package translator;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mocha.parser.ASTAtomList;
import mocha.parser.ASTAtomicModuleDecl;
import mocha.parser.ASTCompModuleDecl;
import mocha.parser.ASTConstDef;
import mocha.parser.ASTDefList;
import mocha.parser.ASTEnumVal;
import mocha.parser.ASTHideModuleDecl;
import mocha.parser.ASTJudgmentDef;
import mocha.parser.ASTModuleDef;
import mocha.parser.ASTModuleName;
import mocha.parser.ASTModuleVarDecl;
import mocha.parser.ASTModuleVarDeclList;
import mocha.parser.ASTName;
import mocha.parser.ASTNonEmptyEnumValList;
import mocha.parser.ASTNonemptyVarList;
import mocha.parser.ASTParenModuleDecl;
import mocha.parser.ASTPredicateDef;
import mocha.parser.ASTRangeTypeDecl;
import mocha.parser.ASTRenameModuleDecl;
import mocha.parser.ASTSimpTypeDecl;
import mocha.parser.ASTTypeDecl;
import mocha.parser.ASTTypeDef;
import mocha.parser.ASTTypedNonemptyVarList;
import mocha.parser.ASTTypedVarList;
import blifmv.SubCircuit;
import blifmv.Model;
import blifmv.ModelCollection;
import blifmv.Type;
import blifmv.Var;
import blifmv.VarListBuilder;

public class TranslatorVisitor extends AbstractVisitor {
	// The types that we may use.
	Map<String, Type> types;
	
	/**
	 * Visits the root node of the tree. Returns a ModelCollection
	 */
	public Object visit(ASTDefList astdeflist, Object obj) {
		ModelCollection mc = (ModelCollection)obj;
		this.types = mc.getTypes();
		astdeflist.childrenAccept(this, mc);
		return mc;
	}
	
	/**
	 * Visits a module definition. Returns a Model.
	 */
	public Object visit(ASTModuleDef astmoduledef, Object obj) {
		ModelCollection mc = (ModelCollection)obj;
		Model m = new Model(mc);
		astmoduledef.childrenAccept(this, m);
		mc.addModel(m);
		
		// Now, also collect the subcircuits
		if( astmoduledef.jjtGetNumChildren() > 1) {
			SubCircuit cc = new SubCircuit(m);
			CompositeCircuitVisitor ccv = new CompositeCircuitVisitor(cc);
			astmoduledef.jjtGetChild(1).jjtAccept(ccv, null);
			m.addSubcircuit(cc);
		}
		return m;
	}
	
	/**
	 * Visits a module name. Takes a Model as argument.
	 */
	public Object visit(ASTModuleName astmodulename, Object obj) {
		Model m = (Model)obj;
		m.setName(astmodulename.name);
		return null;
	}
	
	/**
	 * The contents of an atomic module
	 */
	public Object visit(ASTAtomicModuleDecl astatomicmoduledecl, Object obj) {
		Model m = (Model)obj;
		astatomicmoduledecl.childrenAccept(this, m);
		astatomicmoduledecl.childrenAccept(new TableFiller(m), null);
		return null;
	}
	
	/**
	 * The contents of a composite module
	 */
	public Object visit(ASTCompModuleDecl astcompmoduledecl, Object obj) {
		Model m = (Model)obj;
		astcompmoduledecl.childrenAccept(this, m);
		return null;
	}
	
	/**
	 * Variable declarations of a module
	 */
	public Object visit(ASTModuleVarDeclList astmodulevardecllist, Object obj) {
		Model m = (Model)obj;
		astmodulevardecllist.childrenAccept(this, m);
		return null;
	}
	
	/**
	 * Variables of one type
	 */
	public Object visit(ASTModuleVarDecl astmodulevardecl, Object obj) {
		Model m = (Model)obj;
		List<Var> vars = new LinkedList<Var>();
		astmodulevardecl.childrenAccept(this, vars);
		switch(astmodulevardecl.type) {
		case ASTModuleVarDecl.INTERFACE:
			m.getOutput().addAll(vars);
			break;
		case ASTModuleVarDecl.EXTERNAL: // Populate the inputs of the module.
			m.getInput().addAll(vars);
			break;
		case ASTModuleVarDecl.PRIVATE:
			m.getPrivate().addAll(vars);
		}
		return null;
	}
	
	/**
	 * A list of vars.
	 */
	public Object visit(ASTTypedVarList asttypedvarlist, Object obj) {
		List<Var> vars = (List<Var>)obj;
		asttypedvarlist.childrenAccept(this, vars);
		return null;
	}
	
	/**
	 * A nonempty list of vars. This seems to group different variables with the same type.
	 * For our case, we just go through it, getting back the filled list.
	 */
	public Object visit(ASTTypedNonemptyVarList asttypednonemptyvarlist,
			Object obj) {
		List<Var> vars = (List<Var>)obj;
		VarListBuilder vb = new VarListBuilder(this.types);
		asttypednonemptyvarlist.childrenAccept(this, vb);
		vars.addAll(vb.build());
		return null;
	}
	
	/**
	 * Lists of names.
	 */
	public Object visit(ASTNonemptyVarList astnonemptyvarlist, Object obj) {
		VarListBuilder vb = (VarListBuilder)obj;
		astnonemptyvarlist.childrenAccept(this, vb);
		return null;
	}
	
	/**
	 * Name of a variable. Takes a list of variables as argument
	 */
	public Object visit(ASTName astname, Object obj) {
		VarListBuilder vb = (VarListBuilder)obj;
		vb.addVarName(astname.name);
		return null;
	}
	
	/**
	 * A type definition: Ignore
	 */
	public Object visit(ASTTypeDef asttypedef, Object obj) {
		return null;
	}
	
	/**
	 * Type declaration: Ignore
	 */
	public Object visit(ASTTypeDecl asttypedecl, Object obj) {
		VarListBuilder vb = (VarListBuilder)obj;
		if( asttypedecl.name != null ) { 
			vb.setType(asttypedecl.name);
		} else {
			asttypedecl.childrenAccept(this, vb);
		}
		return null;
	}
	
	/**
	 * Simple type declaration: Just forward
	 */
	public Object visit(ASTSimpTypeDecl astsimptypedecl, Object obj) {
		VarListBuilder vb = (VarListBuilder)obj;
		if( astsimptypedecl.isBool ) vb.setType("bool");
		if( astsimptypedecl.isEvent) vb.setType("event");
		astsimptypedecl.childrenAccept(this, obj);
		return null;
	}
	
	public Object visit(ASTRangeTypeDecl astrangetypedecl, Object obj) {
		VarListBuilder vb = (VarListBuilder)obj;
		vb.setAnonymousRangeType(Integer.parseInt(astrangetypedecl.upper) + 1);
		astrangetypedecl.childrenAccept(this, vb);
		return null;
	}
	
	/**
	 * Enumeration of values. Just forward.
	 */
	public Object visit(ASTNonEmptyEnumValList astnonemptyenumvallist,
			Object obj) {
		List<String> possibleValues = (List<String>)obj;
		astnonemptyenumvallist.childrenAccept(this, possibleValues);
		return null;
	}
	
	/**
	 * Enumeration value: store in list, return.
	 */
	public Object visit(ASTEnumVal astenumval, Object obj) {
		List<String> possibleValues = (List<String>)obj;
		possibleValues.add(astenumval.name);
		return null;
	}
	
	/**
	 * A renaming: This will be implemented as a subcircuit. However, we already know of this
	 * because we already instructed the parent to collect subcircuits here.
	 */
	public Object visit(ASTRenameModuleDecl astrenamemoduledecl, Object obj) {
		return null;
	}
	
	/**
	 * A hiding: We don't want to see a certain number of variables anymore.
	 * This also is already visited by the collector invoked on modeldef.
	 */
	public Object visit(ASTHideModuleDecl asthidemoduledecl, Object obj) {
		return null;
	}
	
	// Ends
	public Object visit(ASTConstDef astconstdef, Object obj) {
		return null;
	}
	
	public Object visit(ASTAtomList astatomlist, Object obj) {
		return null;
	}
	
	public Object visit(ASTParenModuleDecl astparenmoduledecl, Object obj) {
		return null;
	}
	
	public Object visit(ASTPredicateDef astpredicatedef, Object obj) {
		return null;
	}
	
	public Object visit(ASTJudgmentDef astjudgmentdef, Object obj) {
		return null;
	}
}
