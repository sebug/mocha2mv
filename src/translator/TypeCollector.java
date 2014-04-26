package translator;

import mocha.parser.ASTConstDef;
import mocha.parser.ASTConstant;
import mocha.parser.ASTDefList;
import mocha.parser.ASTEnumVal;
import mocha.parser.ASTJudgmentDef;
import mocha.parser.ASTModuleDef;
import mocha.parser.ASTNonEmptyEnumValList;
import mocha.parser.ASTPredicateDef;
import mocha.parser.ASTRangeTypeDecl;
import mocha.parser.ASTSimpTypeDecl;
import mocha.parser.ASTTypeDecl;
import mocha.parser.ASTTypeDef;
import blifmv.ModelCollection;
import blifmv.TypeBuilder;

/**
 * Collects the types from the Reactive Modules file.
 *
 */
public class TypeCollector extends AbstractVisitor {
	
	/**
	 * Root of the tree. Traverse to extract types.
	 */
	public Object visit(ASTDefList astdeflist, Object obj) {
		ModelCollection mc = new ModelCollection();
		astdeflist.childrenAccept(this, mc);
		return mc;
	}
	
	/**
	 * Type definition. We already know the name of the type at this moment.
	 */
	public Object visit(ASTTypeDef asttypedef, Object obj) {
		ModelCollection mc = (ModelCollection)obj;
		String typeName = asttypedef.name;
		TypeBuilder tb = new TypeBuilder(typeName);
		asttypedef.childrenAccept(this, tb);
		mc.addType(typeName, tb.build());
		return null;
	}
	
	/**
	 * Type declaration: Pass along.
	 */
	public Object visit(ASTTypeDecl asttypedecl, Object obj) {
		asttypedecl.childrenAccept(this, obj);
		return null;
	}
	
	public Object visit(ASTSimpTypeDecl astsimptypedecl, Object obj) {
		TypeBuilder tb = (TypeBuilder)obj;
		astsimptypedecl.childrenAccept(this, tb);
		return null;
	}
	
	/**
	 * Enumerative list of values.
	 */
	public Object visit(ASTNonEmptyEnumValList astnonemptyenumvallist,
			Object obj) {
		TypeBuilder tb = (TypeBuilder)obj;
		astnonemptyenumvallist.childrenAccept(this, tb);
		return null;
	}
	
	/**
	 * Possible value for a type.
	 */
	public Object visit(ASTEnumVal astenumval, Object obj) {
		TypeBuilder tb = (TypeBuilder)obj;
		tb.addPossibleValue(astenumval.name);
		return null;
	}
	
	public Object visit(ASTRangeTypeDecl astrangetypedecl, Object obj) {
		TypeBuilder tb = (TypeBuilder)obj;
		if( !astrangetypedecl.lower.equals("0") ) {
			throw new Error("Can only work with types whose lower bound is 0. The lower bound of " + tb.getName()
					+ " is " + astrangetypedecl.lower);
		}
		tb.setSize(Integer.parseInt(astrangetypedecl.upper) + 1);
		return null;
	}
	
	// Constant definitions: We don't rely on them to do enumeration types
	// TODO: Might be a better way to represent enumerations.
	public Object visit(ASTConstDef astconstdef, Object obj) {
		return null;
	}
	
	// Ends
	public Object visit(ASTModuleDef astmoduledef, Object obj) {
		return null;
	}
	
	public Object visit(ASTPredicateDef astpredicatedef, Object obj) {
		return null;
	}
	
	public Object visit(ASTJudgmentDef astjudgmentdef, Object obj) {
		return null;
	}
}
