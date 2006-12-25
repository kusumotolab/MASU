package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * 定義部つきブロックの状態を管理する抽象クラス.
 * <p>
 * クラス定義部、メソッド定義部、名前空間定義部などの状態管理に利用される.
 * ブロック無しの場合やブロック内でさらに入れ子に宣言部が現れるような構造も扱う.
 * <p>
 * 状態遷移パターン
 * パターン1: 定義のみの場合
 * INIT --（定義ノードの中に入る）--> DEFINITION --（定義ノードから出る）--> INIT
 * パターン2: 定義の後に関連するブロックが続く場合
 * INIT --（定義ノードの中に入る）--> DEFINITION --（ブロックに入る）--> BLOCK --（ブロックから出る）--> DEFINITION --（定義ノードから出る）--> INIT
 * パターン3: 入れ子になる場合
 * INIT --（定義ノードの中に入る）--> DEFINITION --（ブロックに入る）--> BLOCK --（定義ノードの中に入る）--> DEFINITION --（ブロックに入る）-->
 * BLOCK --> ... --（ブロックから出る）--> DEFINITION --（定義ノードから出る）--> BLOCK --（ブロックから出る）--> DEFINITION --（定義ノードから出る）--> INIT
 * 
 * @author kou-tngt
 */
public abstract class DeclaredBlockStateManager extends
        StackedAstVisitStateManager<DeclaredBlockStateManager.STATE> {

    @Override
    public void entered(final AstVisitEvent event) {
        //状態をスタックへ記録
        super.entered(event);

        final AstToken token = event.getToken();

        if (this.isDefinitionToken(token)) {
            this.state = STATE.DECLARE;
            this.fireStateChangeEvent(this.getDefinitionEnterEventType(),event);
        } else if (this.isBlockToken(token) && STATE.DECLARE == this.state) {
            this.state = STATE.BLOCK;
            this.fireStateChangeEvent(this.getBlockEnterEventType(),event);
        }
    }

    @Override
    public void exited(final AstVisitEvent event) {
        //スタックの一番上の状態に戻す
        super.exited(event);

        final AstToken token = event.getToken();

        if (this.isDefinitionToken(token)) {
            this.fireStateChangeEvent(this.getDefinitionExitEventType(),event);
        } else if (this.isBlockToken(token) && STATE.DECLARE == this.state) {
            this.fireStateChangeEvent(this.getBlockExitEventType(),event);
        }
    }

    protected abstract StateChangeEventType getBlockEnterEventType();

    protected abstract StateChangeEventType getBlockExitEventType();

    protected abstract StateChangeEventType getDefinitionEnterEventType();

    protected abstract StateChangeEventType getDefinitionExitEventType();

    protected abstract boolean isDefinitionToken(AstToken token);

    protected boolean isBlockToken(final AstToken token) {
        return token.isBlock();
    }

    public boolean isInBlock() {
        return STATE.BLOCK == this.state;
    }

    public boolean isInDefinition() {
        return STATE.DECLARE == this.state || this.isInBlock();
    }

    public boolean isInPreDeclaration() {
        return STATE.DECLARE == this.state;
    }

    @Override
    protected STATE getState() {
        return this.state;
    }

    @Override
    protected boolean isStateChangeTriggerToken(final AstToken token) {
        return isBlockToken(token)|| this.isDefinitionToken(token);
    }

    @Override
    protected void setState(final STATE state) {
        this.state = state;
    };

    protected static enum STATE {
        INIT, DECLARE, BLOCK
    }

    private STATE state = STATE.INIT;

}
