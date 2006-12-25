package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;

public class FieldStateManager extends VariableDefinitionStateManager {
   
    @Override
    protected boolean isDefinitionToken(AstToken token) {
        return token.isFieldDefinition();
    }

}
