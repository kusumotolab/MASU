package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;

public class ConstructorStateManager extends CallableUnitStateManager {

    /**
     * 引数のトークンがコンストラクタ定義部を表すかどうかを返す．
     * token.isConstructorDefinition()メソッドを用いて判定する．
     * 
     * @param token　コンストラクタ定義部を表すかどうかを調べたいASTトークン
     * @return コンストラクタ定義部を表すトークンであればtrue
     */
    @Override
    protected boolean isDefinitionToken(final AstToken token) {
        return token.isConstructorDefinition();
    }

}
