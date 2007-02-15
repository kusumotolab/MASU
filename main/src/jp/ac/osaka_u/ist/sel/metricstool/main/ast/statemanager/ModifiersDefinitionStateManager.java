package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;


/**
 * ASTビジターが修飾子記述部に到達した時に状態遷移し，状態変化イベントを発行するステートマネージャ．
 * 
 * @author kou-tngt
 *
 */
public class ModifiersDefinitionStateManager extends EnterExitStateManager {

    /**
     * 通知する状態変化イベントの種類を表すenum
     * 
     * @author kou-tngt
     *
     */
    public static enum MODIFIERS_STATE implements StateChangeEventType {
        ENTER_MODIFIERS_DEF, EXIT_MODIFIERS_DEF
    }

    /**
     * 修飾子記述部の中に入った時の状態変化イベントの種類を返す．
     * @return　修飾子記述部の中に入った時の状態変化イベントの種類
     */
    @Override
    public StateChangeEventType getEnterEventType() {
        return MODIFIERS_STATE.ENTER_MODIFIERS_DEF;
    }

    /**
     * 修飾子記述部から出た時の状態変化イベントの種類を返す．
     * @return　修飾子記述部から出た時の状態変化イベントの種類
     */
    @Override
    public StateChangeEventType getExitEventType() {
        return MODIFIERS_STATE.EXIT_MODIFIERS_DEF;
    }

    /**
     * 引数で与えられたトークンが修飾子記述部を表すかどうかを返す.
     * 判定にはtoken.isModifiersDefinition()メソッドを用いる．
     * 
     * @param token 修飾子記述部を表すかどうかを調べるトークン
     * @return 修飾子記述部を表す場合はtrue
     */
    @Override
    protected boolean isStateChangeTriggerToken(final AstToken token) {
        return token.isModifiersDefinition();
    }
}
