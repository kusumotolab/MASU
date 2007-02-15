package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;


/**
 * ビジターがクラスの親クラス記述部に到達した時に状態遷移し，
 * 状態変化イベントを発行するステートマネージャ
 * @author kou-tngt
 *
 */
public class InheritanceDefinitionStateManager extends EnterExitStateManager {

    /**
     * 発行する状態遷移イベントのイベントタイプを表すenum
     * @author kou-tngt
     *
     */
    public static enum INHERITANCE_STATE implements StateChangeEventType {
        ENTER_INHERITANCE_DEF, EXIT_INHERITANCE_DEF
    }

    /**
     * 親クラス記述部の中に入った時の状態変化イベントの種類を返す．
     * @return　親クラス記述部の中に入った時の状態変化イベントの種類
     */
    @Override
    public StateChangeEventType getEnterEventType() {
        return INHERITANCE_STATE.ENTER_INHERITANCE_DEF;
    }

    /**
     * 親クラス記述部から出た時の状態変化イベントの種類を返す．
     * @return　親クラス記述部から出た時の状態変化イベントの種類
     */
    @Override
    public StateChangeEventType getExitEventType() {
        return INHERITANCE_STATE.EXIT_INHERITANCE_DEF;
    }

    /**
     * 引数で与えられたトークンが修飾子記述部を表すかどうかを返す.
     * 判定にはtoken.isInheritanceDescription()メソッドを用いる．
     * 
     * @param token 親クラス記述部を表すかどうかを調べるトークン
     * @return 親クラス記述部を表す場合はtrue
     */
    @Override
    protected boolean isStateChangeTriggerToken(final AstToken token) {
        return token.isInheritanceDescription();
    }

}
