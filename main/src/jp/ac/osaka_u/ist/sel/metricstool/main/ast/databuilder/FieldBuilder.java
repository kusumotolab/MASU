package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.FieldStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;

public class FieldBuilder extends VariableBuilder<UnresolvedFieldInfo>{
    public FieldBuilder(BuildDataManager buildDataManager,ModifiersInterpriter interpriter){
        this(buildDataManager,new ModifiersBuilder(),new TypeBuilder(buildDataManager),
                new NameBuilder(),interpriter);
    }
    
    public FieldBuilder(BuildDataManager buildDataManager,ModifiersBuilder modifiersBuilder,
            TypeBuilder typeBuilder, NameBuilder nameBuilder, ModifiersInterpriter interpriter){
        super(new FieldStateManager(),modifiersBuilder,typeBuilder,nameBuilder);
        
        if (null == buildDataManager){
            throw new NullPointerException("builderManager is null.");
        }
        
        this.buildDataManager = buildDataManager;
        this.interpriter = interpriter;
    }

    @Override
    protected UnresolvedFieldInfo buildVariable(String[] name, UnresolvedTypeInfo type, ModifierInfo[] modifiers) {
        String varName = "";
        if (name.length > 0){
            varName = name[0];
        }
        
        UnresolvedClassInfo classInfo = buildDataManager.getCurrentClass();
        if (null != classInfo){
            UnresolvedFieldInfo field = new UnresolvedFieldInfo(varName,type,classInfo);
            for(ModifierInfo modifier : modifiers){
                field.addModifier(modifier);
            }
            
            if (null != interpriter){
                interpriter.interprit(modifiers, field,field);
            }
            
            buildDataManager.addField(field);
            
            return field;
        }
        return null;
    }
    
    private final BuildDataManager buildDataManager;
    private final ModifiersInterpriter interpriter;
}
