package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.ClassDefinitionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.NameStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.ModifiersDefinitionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.ClassDefinitionStateManager.CLASS_STATE_CHANGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;


public class ClassBuilder extends CompoundDataBuilder<UnresolvedClassInfo> {

    public ClassBuilder(BuildDataManager targetDataManager,ModifiersInterpriter interpriter) {
        this(targetDataManager, new ModifiersBuilder(), new NameBuilder(),interpriter);
    }

    public ClassBuilder(BuildDataManager targetDataManager, ModifiersBuilder modifiersBuilder,
            NameBuilder nameBuilder,ModifiersInterpriter interpriter) {
        
        if (null == targetDataManager) {
            throw new NullPointerException("builderManager is null.");
        }

        addStateManager(classStateManager);
        addStateManager(new ModifiersDefinitionStateManager());
        addStateManager(new NameStateManager());

        this.buildManager = targetDataManager;

        this.modifiersBuilder = modifiersBuilder;
        this.nameBuilder = nameBuilder;
        
        this.interpriter = interpriter;
        
        addInnerBuilder(modifiersBuilder);
        addInnerBuilder(nameBuilder);
        modifiersBuilder.deactivate();
        nameBuilder.deactivate();
    }

    public void stateChangend(final StateChangeEvent<AstVisitEvent> event) {
        StateChangeEventType type = event.getType();
        if (type.equals(CLASS_STATE_CHANGE.ENTER_CLASS_DEF)) {
            AstVisitEvent trigger = event.getTrigger();
            startClassDefinition(trigger.getStartLine(),trigger.getStartColumn(),
                    trigger.getEndLine(),trigger.getEndColumn());
           
        } else if (type.equals(CLASS_STATE_CHANGE.EXIT_CLASS_DEF)) {
            endClassDefinition();
            isClassNameBuit = false;
        } else if (type.equals(CLASS_STATE_CHANGE.ENTER_CLASS_BLOCK)){
            isClassNameBuit = false;
            if (null != buildManager){
                buildManager.enterClassBlock();
            }
        } else if (isActive() && classStateManager.isInPreDeclaration()) {
            if (type.equals(ModifiersDefinitionStateManager.MODIFIERS_STATE.ENTER_MODIFIERS_DEF)) {
                modifiersBuilder.activate();
            } else if (type.equals(ModifiersDefinitionStateManager.MODIFIERS_STATE.EXIT_MODIFIERS_DEF)) {
                modifiersBuilder.deactivate();
                registModifiers(modifiersBuilder.popLastBuiltData());
                modifiersBuilder.clearBuiltData();
            } else if (type.equals(NameStateManager.NAME_STATE.ENTER_NAME) && !isClassNameBuit) {
                nameBuilder.activate();
            } else if (type.equals(NameStateManager.NAME_STATE.EXIT_NAME) && !isClassNameBuit) {
                nameBuilder.deactivate();
                registClassName(nameBuilder.getFirstBuiltData());
                nameBuilder.clearBuiltData();
                isClassNameBuit = true;
            }
        }
    }

    protected void endClassDefinition() {
        buildManager.endClassDefinition();
        if (!buildingClassStack.isEmpty()){
            buildingClassStack.pop();
        }
    }

    protected UnresolvedClassInfo getBuildingClassInfo() {
        if (this.buildingClassStack.isEmpty()){
            return null;
        } else {
            return this.buildingClassStack.peek();
        }
    }

    protected void startClassDefinition(int startLine, int startColumn, int endLine, int endColumn) {
        UnresolvedClassInfo classInfo = new UnresolvedClassInfo();
        
        classInfo.setFromLine(startLine);
        classInfo.setFromColumn(startColumn);
        classInfo.setToLine(endLine);
        classInfo.setToColumn(endColumn);
        
        this.buildingClassStack.push(classInfo);
        registClassInfo(classInfo);
    }

    protected void registClassInfo(UnresolvedClassInfo classInfo) {
        this.registBuiltData(classInfo);
        
        if (null != buildManager){
            buildManager.startClassDefinition(classInfo);
        }
    }

    protected void registClassName(String[] name) {
        if (!buildingClassStack.isEmpty() && 0 < name.length) {
            buildingClassStack.peek().setClassName(name[0]);
        }
    }

    protected void registModifiers(ModifierInfo[] modifiers) {
        if (!buildingClassStack.isEmpty()) {
            UnresolvedClassInfo buildingclass = buildingClassStack.peek();
            for (ModifierInfo modifier : modifiers) {
                buildingclass.addModifier(modifier);
            }
            
            if (null != interpriter){
                interpriter.interprit(modifiers, buildingclass,null);
            }
        }
    }

    private final ClassDefinitionStateManager classStateManager = new ClassDefinitionStateManager();

    private final BuildDataManager buildManager;
    
    private final ModifiersBuilder modifiersBuilder;
    
    private final NameBuilder nameBuilder;
    
    private final ModifiersInterpriter interpriter;

    private final Stack<UnresolvedClassInfo> buildingClassStack = new Stack<UnresolvedClassInfo>();
    
    private boolean isClassNameBuit = false;
}
