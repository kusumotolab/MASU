package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.InheritanceDefinitionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.TypeDescriptionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


public class InheritanceBuilder extends CompoundDataBuilder<UnresolvedReferenceTypeInfo> {
    
    public InheritanceBuilder(BuildDataManager buildManager){
        this(buildManager,new TypeBuilder(buildManager));
    }

    public InheritanceBuilder(final BuildDataManager buildDataManager, TypeBuilder typebuilder) {

        this.buildDataManager = buildDataManager;
        
        this.typeBuilder = typebuilder;
        
        addInnerBuilder(typebuilder);
        typebuilder.deactivate();
        
        this.addStateManager(inheritanceStateManager);
        this.addStateManager(typeStateManager);
    }

    @Override
    public void stateChangend(final StateChangeEvent<AstVisitEvent> event) {
        final StateChangeEventType type = event.getType();
        if (isActive() && inheritanceStateManager.isEntered()){
            if (type.equals(TypeDescriptionStateManager.TYPE_STATE.ENTER_TYPE)){
                typeBuilder.activate();
            } else if (type.equals(TypeDescriptionStateManager.TYPE_STATE.EXIT_TYPE)){
                typeBuilder.deactivate();
                buildInheritance();
            }
        }
    }
    
    private void buildInheritance(){
        UnresolvedTypeInfo type = typeBuilder.popLastBuiltData();
        
        if (type instanceof UnresolvedReferenceTypeInfo){
            UnresolvedReferenceTypeInfo referenceType = (UnresolvedReferenceTypeInfo)type;
            registBuiltData(referenceType);
            
            if (null != buildDataManager){
                UnresolvedClassInfo classInfo = buildDataManager.getCurrentClass();
                if (null != classInfo){
                    classInfo.addSuperClass(referenceType);
                }
            }
        }
    }

    private final TypeBuilder typeBuilder;
    
    private final InheritanceDefinitionStateManager inheritanceStateManager = new InheritanceDefinitionStateManager();
    private final TypeDescriptionStateManager typeStateManager = new TypeDescriptionStateManager();
    
    private final BuildDataManager buildDataManager;

}
