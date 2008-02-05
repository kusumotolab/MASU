package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;

public class MethodStateManager extends CallableUnitStateManager {

    /**
     * 引数のトークンがメソッド定義部を表すかどうかを返す．
     * token.isMethodDefinition()メソッドを用いて判定する．
     * 
     * @param token　メソッド定義部を表すかどうかを調べたいASTトークン
     * @return メソッド定義部を表すトークンであればtrue
     */
    @Override
    protected boolean isDefinitionToken(final AstToken token) {
        return token.isMethodDefinition();
    }

}
