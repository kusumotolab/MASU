package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * ASTビジターが何らかの変数定義部とその初期化部に到達した時に状態遷移を起こすステートマシンを実装する抽象クラス.
 * 
 * サブクラスは{@link #isDefinitionToken(AstToken)}メソッドを実装し，対応する変数定義部ノードを表すトークンを指定しなければならない.
 * @author kou-tngt
 *
 */
public abstract class VariableDefinitionStateManager extends
        StackedAstVisitStateManager<VariableDefinitionStateManager.STATE> {

    /**
     * 状態変化の種類を表すenum
     * 
     * @author kou-tngt
     *
     */
    public static enum VARIABLE_STATE implements StateChangeEventType {
        ENTER_VARIABLE_DEF, ENTER_VARIABLE_INITIALIZER, EXIT_VARIABLE_INITIALIZER, EXIT_VARIABLE_DEF;
    }

    /**
     * ビジターがASTノードの中に入った時のイベント通知を受け取り，
     * 
     * @param event イベント
     */
    @Override
    public void entered(final AstVisitEvent event) {
        final AstToken token = event.getToken();

        if (isStateChangeTriggerEvent(event)) {
            //現在の状態を保存
            super.entered(event);

            if (this.isDefinitionToken(token)) {
                //定義部に入ったので状態遷移をしてイベントを発行する
                this.state = STATE.DEFINITION;
                this.fireStateChangeEvent(VARIABLE_STATE.ENTER_VARIABLE_DEF, event);
            } else if (token.isAssignmentOperator() && STATE.DEFINITION == this.state) {
                //定義部内に代入演算子があったので，初期化部へと常態遷移をしてイベントを発行する
                this.state = STATE.INITIALIZER;
                this.fireStateChangeEvent(VARIABLE_STATE.ENTER_VARIABLE_INITIALIZER, event);
            }
        }
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StackedAstVisitStateManager#exited(jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent)
     */
    @Override
    public void exited(final AstVisitEvent event) {
        final AstToken token = event.getToken();

        if (isStateChangeTriggerEvent(event)) {
            //状態を復元
            super.exited(event);

            if (this.isDefinitionToken(token)) {
                //定義部から出たのでイベントを発行する
                this.fireStateChangeEvent(VARIABLE_STATE.EXIT_VARIABLE_DEF, event);
            } else if (token.isAssignmentOperator() && STATE.DEFINITION == this.state) {
                //初期化部から出たのでイベントを発行する
                this.fireStateChangeEvent(VARIABLE_STATE.EXIT_VARIABLE_INITIALIZER, event);
            }
        }
    }

    /**
     * ビジターが対応する変数定義部にいるかどうかを返す.
     * @return 変数定義部にいる場合はtrue
     */
    public boolean isInDefinition() {
        return STATE.DEFINITION == this.state;
    }

    /**
     * ビジターが対応する変数初期化部にいるかどうかを返す.
     * @return　変数初期化部にいる場合はtrue
     */
    public boolean isInInitializer() {
        return STATE.INITIALIZER == this.state;
    }

    /**
     * 現在の状態を返す.
     * @return 現在の状態
     */
    @Override
    protected STATE getState() {
        return this.state;
    }

    /**
     * トークンが対応する変数定義部かどうかを返す抽象メソッド．
     * このメソッドをオーバーライドすることで，サブクラスが対応する変数の種類を指定することができる．
     * 
     * @param token　変数定義部かどうか判定するトークン
     * @return 変数定義部であればtrue.
     */
    protected abstract boolean isDefinitionToken(AstToken token);

    /**
     * 代入演算子，または変数定義部を表すトークンかどうかを返す.
     * 
     * @param 代入演算子，または変数定義部を表すトークンかどうかを判定するトークン
     * @return 代入演算子，または変数定義部を表すトークンであればtrueを返す
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StackedAstVisitStateManager#isStateChangeTriggerEvent(jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken)
     */
    @Override
    protected boolean isStateChangeTriggerEvent(final AstVisitEvent event) {
        AstToken token = event.getToken();
        return token.isAssignmentOperator() || this.isDefinitionToken(token);
    }

    /**
     * 状態を復元する.
     * @param state 復元に用いる状態
     */
    @Override
    protected void setState(final STATE state) {
        this.state = state;
    }

    /**
     * 状態を表すenum
     * @author kou-tngt
     */
    protected static enum STATE {
        OUT, DEFINITION, INITIALIZER
    }

    /**
     * 現在の状態を保持する
     */
    private STATE state = STATE.OUT;
}
