package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.CompoundDataBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.IdentifierBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ImportType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.OperatorToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


public class JavaMemberImportBuilder extends CompoundDataBuilder<Object> {

    public JavaMemberImportBuilder(BuildDataManager buildDataManager) {
        this(buildDataManager, new IdentifierBuilder());
    }

    public JavaMemberImportBuilder(BuildDataManager buildDataManager,
            IdentifierBuilder identifierBuilder) {
        if (null == buildDataManager) {
            throw new NullPointerException("builderManager is null.");
        }

        if (null == identifierBuilder) {
            throw new NullPointerException("identifierBuilde is null.");
        }

        this.buildDataManager = buildDataManager;
        this.identifierBuilder = identifierBuilder;
        addInnerBuilder(identifierBuilder);
        addStateManager(this.stateManager);
    }

    @Override
    public void entered(AstVisitEvent event) {
        super.entered(event);

        if (this.isActive() && stateManager.isEntered()) {
            if (event.getToken().equals(OperatorToken.ARITHMETICH_BINOMIAL)) {//*‚ª—ˆ‚½
                lastTokenIsAsterisk = true;
            } else {
                lastTokenIsAsterisk = false;
            }
        }
    }

    @Override
    public void stateChanged(StateChangeEvent<AstVisitEvent> event) {
        StateChangeEventType type = event.getType();
        if (type.equals(JavaMemberImportStateManager.IMPORT_STATE_CHANGE.ENTER_MEMBER_IMPORT)) {
            identifierBuilder.activate();
        } else if (type.equals(JavaMemberImportStateManager.IMPORT_STATE_CHANGE.EXIT_MEMBER_IMPORT)) {
            identifierBuilder.deactivate();
            registImportData();
        }
    }

    private void registImportData() {
        String[] importedElement = identifierBuilder.popLastBuiltData();
        int length = importedElement.length;
        if (length > 0) {
            if (lastTokenIsAsterisk) {//Using name space
                String[] tmp = new String[length];
                System.arraycopy(importedElement, 0, tmp, 0, length);
                buildDataManager.addUsingNameSpace(tmp, ImportType.Member);
            } else {//Aliase 
                String aliase = importedElement[length - 1];
                buildDataManager.addUsingAliase(aliase, importedElement, ImportType.Member);
            }
        }

        lastTokenIsAsterisk = false;
    }

    private boolean lastTokenIsAsterisk = false;;

    private final BuildDataManager buildDataManager;

    private final IdentifierBuilder identifierBuilder;

    private final JavaMemberImportStateManager stateManager = new JavaMemberImportStateManager();;
}
