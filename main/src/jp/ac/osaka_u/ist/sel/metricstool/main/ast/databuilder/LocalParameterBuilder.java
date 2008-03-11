package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.LocalParameterStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedConditionalClauseInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableDecralationStatementInfo;


public class LocalParameterBuilder
        extends
        InitializableVariableBuilder<UnresolvedLocalVariableInfo, UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo>> {

    public LocalParameterBuilder(BuildDataManager buildDataManager,
            final ExpressionElementManager expressionManager, ModifiersBuilder modifiersBuilder,
            TypeBuilder typeBuilder, NameBuilder nameBuilder, ModifiersInterpriter interpriter) {
        super(buildDataManager, expressionManager, new LocalParameterStateManager(),
                modifiersBuilder, typeBuilder, nameBuilder);

        this.interpriter = interpriter;
    }

    @Override
    protected UnresolvedLocalVariableInfo buildVariable(final String[] name,
            final UnresolvedTypeInfo type, final ModifierInfo[] modifiers,
            final UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> deifnitionSpace,
            final int startLine, final int startColumn, final int endLine, final int endColumn) {
        String varName = "";
        if (name.length > 0) {
            varName = name[0];
        }

        final UnresolvedExpressionInfo<? extends ExpressionInfo> initializationExpression = this.builtInitializerStack
                .pop();

        UnresolvedLocalVariableInfo local = new UnresolvedLocalVariableInfo(varName, type,
                deifnitionSpace, initializationExpression, startLine, startColumn, endLine,
                endColumn);
        for (ModifierInfo modifier : modifiers) {
            local.addModifier(modifier);
        }

        if (null != interpriter) {
            // TODO           interpriter.interpirt(modifiers, parameter);
        }

        if (null != buildDataManager) {
            buildDataManager.addLocalParameter(local);
        }
        
        final UnresolvedVariableDecralationStatementInfo declarationStatement = new UnresolvedVariableDecralationStatementInfo(
                local, initializationExpression);
        
        final UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> currentLocal = this.buildDataManager.getCurrentLocalSpace();
        if(null != currentLocal) {
            currentLocal.addStatement(declarationStatement);
        }


        return local;
    }

    @Override
    protected UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> validateDefinitionSpace(
            UnresolvedUnitInfo<? extends UnitInfo> definitionUnit) {
        if (definitionUnit instanceof UnresolvedBlockInfo) {
            return (UnresolvedBlockInfo<? extends BlockInfo>) definitionUnit;
        } else if (definitionUnit instanceof UnresolvedConditionalClauseInfo) {
            return (UnresolvedConditionalClauseInfo) definitionUnit;
        } else {
            return null;
        }
    }

    private final ModifiersInterpriter interpriter;
}
