package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * ビジターが型パラメータ定義部へ出入りする際の状態を管理するステートマネージャ．
 * @author kou-tngt
 *
 */
public class TypeParameterStateManager extends StackedAstVisitStateManager<TypeParameterStateManager.STATE> {

    /**
     * 送信する状態変化イベントの種類を表すenum
     * @author kou-tngt
     *
     */
    public enum TYPE_PARAMETER implements StateChangeEventType {
        ENTER_TYPE_PARAMETER_DEF, EXIT_TYPE_PARAMETER_DEF,
        ENTER_TYPE_UPPER_BOUNDS,EXIT_TYPE_UPPER_BOUNDS,
        ENTER_TYPE_LOWER_BOUNDS, EXIT_TYPE_LOWER_BOUNDS
    }
    
    /**
     * 型パラメータ定義部のノードに入った時に呼び出され，
     * 状態変化を引き起こして，状態変化イベントを通知する．
     * 
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StackedAstVisitStateManager#entered(jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent)
     */
    public void entered(AstVisitEvent event){
        super.entered(event);
        
        AstToken token = event.getToken();
        
        if (token.isTypeParameterDefinition()){
            this.state = STATE.IN_PARAMETER_DEF;
            fireStateChangeEvent(TYPE_PARAMETER.ENTER_TYPE_PARAMETER_DEF, event);
        } else if (token.isTypeLowerBoundsDescription()){
            this.state = STATE.IN_LOWER_BOUNDS;
            fireStateChangeEvent(TYPE_PARAMETER.ENTER_TYPE_LOWER_BOUNDS, event);
        } else if (token.isTypeUpperBoundsDescription()){
            this.state = STATE.IN_UPPER_BOUNDS;
            fireStateChangeEvent(TYPE_PARAMETER.ENTER_TYPE_UPPER_BOUNDS, event);            
        }
    }
    
    /**
     * 型パラメータ定義部のノードから出た時に呼び出され，
     * 状態変化を引き起こして，状態変化イベントを通知する．
     * 
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StackedAstVisitStateManager#exited(jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent)
     */
    public void exited(AstVisitEvent event){
        super.exited(event);
        
        AstToken token = event.getToken();
        
        if (token.isTypeParameterDefinition()){
            fireStateChangeEvent(TYPE_PARAMETER.EXIT_TYPE_PARAMETER_DEF, event);
        } else if (token.isTypeLowerBoundsDescription()){
            fireStateChangeEvent(TYPE_PARAMETER.EXIT_TYPE_LOWER_BOUNDS, event);
        } else if (token.isTypeUpperBoundsDescription()){
            fireStateChangeEvent(TYPE_PARAMETER.EXIT_TYPE_UPPER_BOUNDS, event);
        }
    }

    /**
     * 型パラメータ定義部に関連するノードかどうかを判定する
     * 
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StackedAstVisitStateManager#isStateChangeTriggerToken(jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken)
     */
    @Override
    protected boolean isStateChangeTriggerToken(final AstToken token) {
        return token.isTypeParameterDefinition() || token.isTypeLowerBoundsDescription()
            || token.isTypeUpperBoundsDescription();
    }

    /**
     * 現在の状態を返す．
     * @return 現在の状態．
     * 
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StackedAstVisitStateManager#getState()
     */
    @Override
    protected STATE getState() {
        return this.state;
    }

    /**
     * 引数 state　を用いて状態を復元する．
     * @param state 復元する状態
     * 
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StackedAstVisitStateManager#setState(java.lang.Object)
     */
    @Override
    protected void setState(final STATE state) {
        this.state = state;
    }
    
    /**
     * 状態を表すenum
     * 
     * @author kou-tngt
     *
     */
    protected enum STATE{
        OUT,IN_PARAMETER_DEF,IN_UPPER_BOUNDS,IN_LOWER_BOUNDS
    }
    
    /**
     * 現在の状態
     */
    private STATE state = STATE.OUT;
}
