package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.LocalVariableStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedUnitInfo;


public class LocalVariableBuilder
        extends
        VariableBuilder<UnresolvedLocalVariableInfo, UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo>> {

    public LocalVariableBuilder(BuildDataManager buildDataManager, ModifiersInterpriter interpriter) {
        this(buildDataManager, new ModifiersBuilder(), new TypeBuilder(buildDataManager),
                new NameBuilder(), interpriter);
    }

    public LocalVariableBuilder(BuildDataManager buildDataManager,
            ModifiersBuilder modifiersBuilder, TypeBuilder typeBuilder, NameBuilder nameBuilder,
            ModifiersInterpriter interpriter) {
        super(buildDataManager, new LocalVariableStateManager(), modifiersBuilder, typeBuilder,
                nameBuilder);

        this.interpriter = interpriter;
    }

    @Override
    protected UnresolvedLocalVariableInfo buildVariable(String[] name, UnresolvedTypeInfo type,
            ModifierInfo[] modifiers,
            UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> definitionSpace,
            final int startLine, final int startColumn, final int endLine, final int endColumn) {
        String varName = "";
        if (name.length > 0) {
            varName = name[0];
        }

        UnresolvedLocalVariableInfo var = new UnresolvedLocalVariableInfo(varName, type,
                definitionSpace, startLine, startColumn, endLine, endColumn);
        for (ModifierInfo modifier : modifiers) {
            var.addModifier(modifier);
        }

        if (null != interpriter) {
            // TODO            interpriter.interpirt(modifiers, var);
        }

        if (null != buildDataManager) {
            buildDataManager.addLocalVariable(var);
        }

        return var;
    }

    @Override
    protected UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> validateDefinitionSpace(
            UnresolvedUnitInfo<? extends UnitInfo> definitionUnit) {
        return definitionUnit instanceof UnresolvedLocalSpaceInfo ? (UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo>) definitionUnit
                : null;
    }

    private final ModifiersInterpriter interpriter;
}
