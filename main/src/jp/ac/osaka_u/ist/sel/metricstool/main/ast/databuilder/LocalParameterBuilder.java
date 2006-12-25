package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.LocalParameterStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableInfo;

public class LocalParameterBuilder extends VariableBuilder<UnresolvedVariableInfo>{
    
    public LocalParameterBuilder(BuildDataManager buildDataManager,ModifiersInterpriter interpriter) {
        super(buildDataManager,new LocalParameterStateManager());
        
        this.buildDataManager = buildDataManager;
        this.interpriter = interpriter;
    }

    @Override
    protected UnresolvedLocalVariableInfo buildVariable(String[] name, UnresolvedTypeInfo type, ModifierInfo[] modifiers) {
        String varName = "";
        if (name.length > 0){
            varName = name[0];
        }
        
        UnresolvedLocalVariableInfo local = new UnresolvedLocalVariableInfo(varName,type);
        for(ModifierInfo modifier : modifiers){
            local.addModifiar(modifier);
        }
        
        if (null != interpriter){
 // TODO           interpriter.interpirt(modifiers, parameter);
        }
        
        if (null != buildDataManager){
            buildDataManager.addLocalParameter(local);
        }
        
        return local;
    }
    
    private final BuildDataManager buildDataManager;
    private final ModifiersInterpriter interpriter;
}
