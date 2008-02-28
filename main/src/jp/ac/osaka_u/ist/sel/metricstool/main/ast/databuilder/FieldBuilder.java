package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.FieldStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedUnitInfo;


public class FieldBuilder extends VariableBuilder<UnresolvedFieldInfo, UnresolvedClassInfo> {
    public FieldBuilder(BuildDataManager buildDataManager, ModifiersInterpriter interpriter) {
        this(buildDataManager, new ModifiersBuilder(), new TypeBuilder(buildDataManager),
                new NameBuilder(), interpriter);
    }

    public FieldBuilder(BuildDataManager buildDataManager, ModifiersBuilder modifiersBuilder,
            TypeBuilder typeBuilder, NameBuilder nameBuilder, ModifiersInterpriter interpriter) {
        super(buildDataManager, new FieldStateManager(), modifiersBuilder, typeBuilder, nameBuilder);

        this.interpriter = interpriter;
    }

    @Override
    protected UnresolvedFieldInfo buildVariable(String[] name, UnresolvedTypeInfo type,
            ModifierInfo[] modifiers, UnresolvedClassInfo definitionClass) {
        String varName = "";
        if (name.length > 0) {
            varName = name[0];
        }

        if (null != definitionClass) {
            UnresolvedFieldInfo field = new UnresolvedFieldInfo(varName, type, definitionClass);
            for (ModifierInfo modifier : modifiers) {
                field.addModifier(modifier);
            }

            if (null != interpriter) {
                interpriter.interprit(modifiers, field, field);
            }

            buildDataManager.addField(field);

            return field;
        }
        return null;
    }

    @Override
    protected UnresolvedClassInfo validateDefinitionSpace(
            final UnresolvedUnitInfo<? extends UnitInfo> definitionUnit) {
        return (definitionUnit instanceof UnresolvedClassInfo) ? (UnresolvedClassInfo) definitionUnit : null;
    }

    private final ModifiersInterpriter interpriter;
}
