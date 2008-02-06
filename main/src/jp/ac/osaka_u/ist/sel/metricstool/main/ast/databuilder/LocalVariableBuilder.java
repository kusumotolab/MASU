package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.LocalVariableStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;

public class LocalVariableBuilder extends VariableBuilder<UnresolvedLocalVariableInfo>{
    public LocalVariableBuilder(BuildDataManager buildDataManager,ModifiersInterpriter interpriter){
        this(buildDataManager,new ModifiersBuilder(),new TypeBuilder(buildDataManager),
                new NameBuilder(),interpriter);
    }
    
    public LocalVariableBuilder(BuildDataManager buildDataManager,ModifiersBuilder modifiersBuilder,
            TypeBuilder typeBuilder, NameBuilder nameBuilder, ModifiersInterpriter interpriter){
        super(new LocalVariableStateManager(),modifiersBuilder,typeBuilder,nameBuilder);
        
        if (null == buildDataManager){
            throw new NullPointerException("builderManager is null.");
        }
        
        this.buildDataManager = buildDataManager;
        this.interpriter = interpriter;
    }

    @Override
    protected UnresolvedLocalVariableInfo buildVariable(String[] name, UnresolvedTypeInfo type, ModifierInfo[] modifiers) {
        String varName = "";
        if (name.length > 0){
            varName = name[0];
        }
        
        UnresolvedLocalVariableInfo var =  new UnresolvedLocalVariableInfo(varName,type);
        for(ModifierInfo modifier : modifiers){
            var.addModifier(modifier);
        }
        
        if (null != interpriter){
// TODO            interpriter.interpirt(modifiers, var);
        }
        
        if (null != buildDataManager){
            buildDataManager.addLocalVariable(var);
        }
        
        return var;
    }
    
    private final BuildDataManager buildDataManager;
    private final ModifiersInterpriter interpriter;
}
