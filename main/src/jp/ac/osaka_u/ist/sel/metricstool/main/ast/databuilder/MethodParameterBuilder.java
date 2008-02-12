package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.MethodParameterStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;

public class MethodParameterBuilder extends VariableBuilder<UnresolvedParameterInfo>{
    
    public MethodParameterBuilder(BuildDataManager buildDataManager,ModifiersInterpriter interpriter){
        this(buildDataManager,new ModifiersBuilder(),new TypeBuilder(buildDataManager),
                new NameBuilder(),interpriter);
    }
    
    public MethodParameterBuilder(BuildDataManager buildDataManager,ModifiersBuilder modifiersBuilder,
            TypeBuilder typeBuilder, NameBuilder nameBuilder, ModifiersInterpriter interpriter){
        super(new MethodParameterStateManager(),modifiersBuilder,typeBuilder,nameBuilder);
        
        if (null == buildDataManager){
            throw new NullPointerException("builderManager is null.");
        }
        
        this.buildDataManager = buildDataManager;
        this.interpriter = interpriter;
    }

    @Override
    protected UnresolvedParameterInfo buildVariable(String[] name, UnresolvedTypeInfo type, ModifierInfo[] modifiers) {
        String varName = "";
        if (name.length > 0){
            varName = name[0];
        }
        
        final int index = buildDataManager.getCurrentParameterCount();
        UnresolvedParameterInfo parameter = new UnresolvedParameterInfo(varName, type, index);
        
        for(ModifierInfo modifier : modifiers){
            parameter.addModifier(modifier);
        }
        
        if (null != interpriter){
 // TODO           interpriter.interpirt(modifiers, parameter);
        }
        
        if (null != buildDataManager){
            buildDataManager.addMethodParameter(parameter);
        }
        
        return parameter;
    }
    
    private final BuildDataManager buildDataManager;
    private final ModifiersInterpriter interpriter;
}
