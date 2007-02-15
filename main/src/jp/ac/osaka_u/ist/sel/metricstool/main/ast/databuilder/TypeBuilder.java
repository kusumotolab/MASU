package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.TypeDescriptionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.BuiltinTypeToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


public class TypeBuilder extends CompoundDataBuilder<UnresolvedTypeInfo> {

    public TypeBuilder(final BuildDataManager buildDataManager) {
        if (null == buildDataManager) {
            throw new NullPointerException("nameSpaceManager is null.");
        }

        this.buildDataManager = buildDataManager;
        this.addStateManager(this.stateMachine);
        this.addInnerBuilder(this.identifierBuilder);
        this.identifierBuilder.deactivate();
    }

    @Override
    public void entered(final AstVisitEvent event) {
        super.entered(event);

        if (this.isActive() && this.stateMachine.isEntered()) {
            final AstToken token = event.getToken();

            if (null == this.primitiveType && null == this.voidType && token instanceof BuiltinTypeToken) {
                final BuiltinTypeToken typeToken = (BuiltinTypeToken) token;
                if (typeToken.isPrimitiveType()) {
                    this.primitiveType = typeToken.getType();
                } else if (typeToken.isVoidType()) {
                    this.voidType = typeToken.getType();
                }
            }
        }
    }

    @Override
    public void exited(final AstVisitEvent event) {
        if (this.isActive() && this.stateMachine.isEntered()) {
            final AstToken token = event.getToken();
            if (token.isArrayDeclarator()) {
                this.arrayCount++;
            }
        }

        super.exited(event);
    }

    @Override
    public void stateChangend(final StateChangeEvent<AstVisitEvent> event) {
        if (this.isActive()) {
            final StateChangeEventType type = event.getType();
            if (type.equals(TypeDescriptionStateManager.TYPE_STATE.ENTER_TYPE)) {
                this.identifierBuilder.activate();
            } else if (type.equals(TypeDescriptionStateManager.TYPE_STATE.EXIT_TYPE)) {
                this.identifierBuilder.deactivate();
                this.buildType();
            }
        }
    }

    private void buildType() {
        UnresolvedTypeInfo resultType = null;

        if (null != this.primitiveType) {
            resultType = this.primitiveType;
            this.primitiveType = null;
        } else if (null != this.voidType) {
            resultType = this.voidType;
            this.voidType = null;
        } else if (this.identifierBuilder.hasBuiltData()) {
            final String[] identifier = this.identifierBuilder.popLastBuiltData();

            assert (0 != identifier.length) : "Illegal state: identifier was not built.";

            final String[] trueName = this.buildDataManager.resolveAliase(identifier);

            resultType = new UnresolvedReferenceTypeInfo(this.buildDataManager
                    .getAvailableNameSpaceSet(), trueName);

        } else {
            assert (false) : "Illegal state: type can not built.";
        }

        if (0 < this.arrayCount) {
            resultType = UnresolvedArrayTypeInfo.getType(resultType, this.arrayCount);
        }

        this.arrayCount = 0;
        this.registBuiltData(resultType);
    }

    private int arrayCount;

    private UnresolvedTypeInfo primitiveType;

    private UnresolvedTypeInfo voidType;

    private final IdentifierBuilder identifierBuilder = new IdentifierBuilder();

    private final BuildDataManager buildDataManager;

    private final TypeDescriptionStateManager stateMachine = new TypeDescriptionStateManager();
}
