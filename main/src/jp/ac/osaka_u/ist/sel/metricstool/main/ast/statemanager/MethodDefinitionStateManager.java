package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;


/**
 * メソッド定義部とその後のブロックに対するビジターの状態を管理し，状態遷移イベントを発行するクラス．
 * 
 * @author kou-tngt
 *
 */
public class MethodDefinitionStateManager extends DeclaredBlockStateManager {

    /**
     * 発行する状態遷移イベントの種類を表すEnum
     * 
     * @author kou-tngt
     *
     */
    public static enum METHOD_STATE_CHANGE implements StateChangeEventType {
        ENTER_METHOD_DEF, EXIT_METHOD_DEF,

        ENTER_METHOD_BLOCK, EXIT_METHOD_BLOCK, ;
    }

    /**
     * メソッド定義部に続くブロックに入った時に発行する状態変化イベントタイプを返す．
     * @return メソッド定義部に続くブロックに入った時に発行する状態変化イベントのイベントタイプ
     */
    @Override
    protected StateChangeEventType getBlockEnterEventType() {
        return METHOD_STATE_CHANGE.ENTER_METHOD_BLOCK;
    }

    /**
     * メソッド定義部に続くブロックから出た時に発行する状態変化イベントタイプを返す．
     * @return メソッド定義部に続くブロックから出た時に発行する状態変化イベントのイベントタイプ
     */
    @Override
    protected StateChangeEventType getBlockExitEventType() {
        return METHOD_STATE_CHANGE.EXIT_METHOD_BLOCK;
    }

    /**
     * メソッド定義部に入った時に発行する状態変化イベントタイプを返す．
     * @return メソッド定義部にに入った時に発行する状態変化イベントのイベントタイプ
     */
    @Override
    protected StateChangeEventType getDefinitionEnterEventType() {
        return METHOD_STATE_CHANGE.ENTER_METHOD_DEF;
    }

    /**
     * メソッド定義部から出た時に発行する状態変化イベントタイプを返す．
     * @return メソッド定義部から出た時に発行する状態変化イベントのイベントタイプ
     */
    @Override
    protected StateChangeEventType getDefinitionExitEventType() {
        return METHOD_STATE_CHANGE.EXIT_METHOD_DEF;
    }

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
