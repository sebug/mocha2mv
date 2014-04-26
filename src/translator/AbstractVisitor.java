package translator;

import mocha.parser.ASTArrayAssignRhs;
import mocha.parser.ASTArrayExpression;
import mocha.parser.ASTArrayTypeDecl;
import mocha.parser.ASTAssign;
import mocha.parser.ASTAssignLhs;
import mocha.parser.ASTAssignList;
import mocha.parser.ASTAssignRhs;
import mocha.parser.ASTAtlFormula;
import mocha.parser.ASTAtlFormulaDef;
import mocha.parser.ASTAtlFormulaName;
import mocha.parser.ASTAtlPath;
import mocha.parser.ASTAtlTemporalFormula;
import mocha.parser.ASTAtom;
import mocha.parser.ASTAtomList;
import mocha.parser.ASTAtomVarDecl;
import mocha.parser.ASTAtomVarDeclList;
import mocha.parser.ASTAtomVarList;
import mocha.parser.ASTAtomicConstraintDecl;
import mocha.parser.ASTAtomicModuleDecl;
import mocha.parser.ASTBitvectorTypeDecl;
import mocha.parser.ASTBitwiseExpression;
import mocha.parser.ASTBoolExpression;
import mocha.parser.ASTBoolVal;
import mocha.parser.ASTCompConstraintDecl;
import mocha.parser.ASTCompModuleDecl;
import mocha.parser.ASTConditionalExpression;
import mocha.parser.ASTConstDef;
import mocha.parser.ASTConstant;
import mocha.parser.ASTConstraintDef;
import mocha.parser.ASTConstraintInitUpdateDecl;
import mocha.parser.ASTConstraintName;
import mocha.parser.ASTConstraintVarDecl;
import mocha.parser.ASTConstraintVarDeclList;
import mocha.parser.ASTDefList;
import mocha.parser.ASTDefaultGuardedCmd;
import mocha.parser.ASTEnumVal;
import mocha.parser.ASTEventAssign;
import mocha.parser.ASTGuardedAssignList;
import mocha.parser.ASTHideConstraintDecl;
import mocha.parser.ASTHideModuleDecl;
import mocha.parser.ASTInitUpdateDecl;
import mocha.parser.ASTJudgmentDef;
import mocha.parser.ASTModifier;
import mocha.parser.ASTModuleDef;
import mocha.parser.ASTModuleName;
import mocha.parser.ASTModuleVarDecl;
import mocha.parser.ASTModuleVarDeclList;
import mocha.parser.ASTName;
import mocha.parser.ASTNonEmptyEnumValList;
import mocha.parser.ASTNonemptyIdList;
import mocha.parser.ASTNonemptyVarList;
import mocha.parser.ASTParenConstraintDecl;
import mocha.parser.ASTParenModuleDecl;
import mocha.parser.ASTPredicateDef;
import mocha.parser.ASTPredicateName;
import mocha.parser.ASTPrimaryExpression;
import mocha.parser.ASTRangeExpression;
import mocha.parser.ASTRangeTypeDecl;
import mocha.parser.ASTRefinesJudgment;
import mocha.parser.ASTRelationalExpression;
import mocha.parser.ASTRenameConstraintDecl;
import mocha.parser.ASTRenameModuleDecl;
import mocha.parser.ASTSampleConstraintDecl;
import mocha.parser.ASTSatisfiesJudgment;
import mocha.parser.ASTShiftExpression;
import mocha.parser.ASTSimpTypeDecl;
import mocha.parser.ASTTypeDecl;
import mocha.parser.ASTTypeDef;
import mocha.parser.ASTTypedNonemptyVarList;
import mocha.parser.ASTTypedVarList;
import mocha.parser.ASTUnaryExpression;
import mocha.parser.PrsParserConstants;
import mocha.parser.PrsParserVisitor;
import mocha.parser.SimpleNode;

/**
 * Default implementation for all visitor methods: Fail.
 *
 */
public abstract class AbstractVisitor implements PrsParserVisitor {

	public Object visit(ASTArrayAssignRhs arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTArrayAssignRhs visitor not implemented.");
	}

	public Object visit(ASTArrayExpression arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTArrayExpression visitor not implemented.");
	}

	public Object visit(ASTArrayTypeDecl arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTArrayTypeDecl visitor not implemented.");
	}

	public Object visit(ASTAssign arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTAssign visitor not implemented.");
	}

	public Object visit(ASTAssignLhs arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTAssignLhs visitor not implemented.");
	}

	public Object visit(ASTAssignList arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTAssignList visitor not implemented.");
	}

	public Object visit(ASTAssignRhs arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTAssignRhs visitor not implemented.");
	}

	public Object visit(ASTAtlFormula arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTAtlFormula visitor not implemented.");
	}

	public Object visit(ASTAtlFormulaDef arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTAtlFormulaDef visitor not implemented.");
	}

	public Object visit(ASTAtlFormulaName arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTAtlFormulaName visitor not implemented.");
	}

	public Object visit(ASTAtlPath arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTAtlPath visitor not implemented.");
	}

	public Object visit(ASTAtlTemporalFormula arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTAtlTemporalFormula visitor not implemented.");
	}

	public Object visit(ASTAtom arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTAtom visitor not implemented.");
	}

	public Object visit(ASTAtomList arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTAtomList visitor not implemented.");
	}

	public Object visit(ASTAtomVarDecl arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTAtomVarDecl visitor not implemented.");
	}

	public Object visit(ASTAtomVarDeclList arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTAtomVarDeclList visitor not implemented.");
	}

	public Object visit(ASTAtomVarList arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTAtomVarList visitor not implemented.");
	}

	public Object visit(ASTAtomicConstraintDecl arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTAtomicConstraintDecl visitor not implemented.");
	}

	public Object visit(ASTAtomicModuleDecl arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTAtomicModuleDecl visitor not implemented.");
	}

	public Object visit(ASTBitvectorTypeDecl arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTBitvectorTypeDecl visitor not implemented.");
	}

	public Object visit(ASTBitwiseExpression arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTBitwiseExpression visitor not implemented.");
	}

	public Object visit(ASTBoolExpression arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTBoolExpression visitor not implemented.");
	}

	public Object visit(ASTBoolVal arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTBoolVal visitor not implemented.");
	}

	public Object visit(ASTCompConstraintDecl arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTCompConstraintDecl visitor not implemented.");
	}

	public Object visit(ASTCompModuleDecl arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTCompModuleDecl visitor not implemented.");
	}

	public Object visit(ASTConditionalExpression arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTConditionalExpression visitor not implemented.");
	}

	public Object visit(ASTConstDef arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTConstDef visitor not implemented.");
	}

	public Object visit(ASTConstant arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTConstant visitor not implemented.");
	}

	public Object visit(ASTConstraintDef arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTConstraintDef visitor not implemented.");
	}

	public Object visit(ASTConstraintInitUpdateDecl arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTConstraintInitUpdateDecl visitor not implemented.");
	}

	public Object visit(ASTConstraintName arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTConstraintName visitor not implemented.");
	}

	public Object visit(ASTConstraintVarDecl arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTConstraintVarDecl visitor not implemented.");
	}

	public Object visit(ASTConstraintVarDeclList arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTConstraintVarDeclList visitor not implemented.");
	}

	public Object visit(ASTDefList arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTDefList visitor not implemented.");
	}

	public Object visit(ASTDefaultGuardedCmd arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTDefaultGuardedCmd visitor not implemented.");
	}

	public Object visit(ASTEnumVal arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTEnumVal visitor not implemented.");
	}

	public Object visit(ASTEventAssign arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTEventAssign visitor not implemented.");
	}

	public Object visit(ASTGuardedAssignList arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTGuardedAssignList visitor not implemented.");
	}

	public Object visit(ASTHideConstraintDecl arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTHideConstraintDecl visitor not implemented.");
	}

	public Object visit(ASTHideModuleDecl arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTHideModuleDecl visitor not implemented.");
	}

	public Object visit(ASTInitUpdateDecl arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTInitUpdateDecl visitor not implemented.");
	}

	public Object visit(ASTJudgmentDef arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTJudgmentDef visitor not implemented.");
	}

	public Object visit(ASTModifier arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTModifier visitor not implemented.");
	}

	public Object visit(ASTModuleDef arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTModuleDef visitor not implemented.");
	}

	public Object visit(ASTModuleName arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTModuleName visitor not implemented.");
	}

	public Object visit(ASTModuleVarDecl arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTModuleVarDecl visitor not implemented.");
	}

	public Object visit(ASTModuleVarDeclList arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTModuleVarDeclList visitor not implemented.");
	}

	public Object visit(ASTName arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTName visitor not implemented.");
	}

	public Object visit(ASTNonEmptyEnumValList arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTNonEmptyEnumValList visitor not implemented.");
	}

	public Object visit(ASTNonemptyIdList arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTNonemptyIdList visitor not implemented.");
	}

	public Object visit(ASTNonemptyVarList arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTNonemptyVarList visitor not implemented.");
	}

	public Object visit(ASTParenConstraintDecl arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTParenConstraintDecl visitor not implemented.");
	}

	public Object visit(ASTParenModuleDecl arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTParenModuleDecl visitor not implemented.");
	}

	public Object visit(ASTPredicateDef arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTPredicateDef visitor not implemented.");
	}

	public Object visit(ASTPredicateName arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTPredicateName visitor not implemented.");
	}

	public Object visit(ASTPrimaryExpression arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTPrimaryExpression visitor not implemented.");
	}

	public Object visit(ASTRangeExpression arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTRangeExpression visitor not implemented.");
	}

	public Object visit(ASTRangeTypeDecl arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTRangeTypeDecl visitor not implemented.");
	}

	public Object visit(ASTRefinesJudgment arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTRefinesJudgment visitor not implemented.");
	}

	public Object visit(ASTRelationalExpression arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTRelationalExpression visitor not implemented.");
	}

	public Object visit(ASTRenameConstraintDecl arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTRenameConstraintDecl visitor not implemented.");
	}

	public Object visit(ASTRenameModuleDecl arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTRenameModuleDecl visitor not implemented.");
	}

	public Object visit(ASTSampleConstraintDecl arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTSampleConstraintDecl visitor not implemented.");
	}

	public Object visit(ASTSatisfiesJudgment arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTSatisfiesJudgment visitor not implemented.");
	}

	public Object visit(ASTShiftExpression arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTShiftExpression visitor not implemented.");
	}

	public Object visit(ASTSimpTypeDecl arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTSimpTypeDecl visitor not implemented.");
	}

	public Object visit(ASTTypeDecl arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTTypeDecl visitor not implemented.");
	}

	public Object visit(ASTTypeDef arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTTypeDef visitor not implemented.");
	}

	public Object visit(ASTTypedNonemptyVarList arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTTypedNonemptyVarList visitor not implemented.");
	}

	public Object visit(ASTTypedVarList arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTTypedVarList visitor not implemented.");
	}

	public Object visit(ASTUnaryExpression arg0, Object arg1) {
		throw new UnsupportedOperationException("ASTUnaryExpression visitor not implemented.");
	}

	public Object visit(SimpleNode arg0, Object arg1) {
		throw new UnsupportedOperationException("SimpleNode visitor not implemented.");
	}

}
