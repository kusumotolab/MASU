package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StackedAstVisitStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;

/**
 * 抽象構文木のビジターがJavaの匿名クラス到達した時の状態を管理する.
 * 
 * インスタンス化文（new文）の中でクラスブロックが開始された時に匿名クラスとみなす.
 * @author kou-tngt
 *
 */
public class JavaAnonymousClassStateManager extends StackedAstVisitStateManager<Boolean>{
    
    public static enum ANONYMOUSCLASS_STATE implements StateChangeEventType {
        ENTER_INSTANTIATION,
        ENTER_ANONYMOUSCLASS,
        EXIT_ANONYMOUSCLASS,
        EXIT_INSTANTIATION;
    }

    @Override
    public void entered(AstVisitEvent event){
        super.entered(event);
        AstToken token = event.getToken();
        if (token.isInstantiation()){
            this.inInstantiation = true;
            fireStateChangeEvent(ANONYMOUSCLASS_STATE.ENTER_INSTANTIATION,event);
        } else if (this.inInstantiation && token.isClassBlock()){
            this.inInstantiation = false;
            fireStateChangeEvent(ANONYMOUSCLASS_STATE.ENTER_ANONYMOUSCLASS,event);
        }
    }
    
    @Override
    public void exited(AstVisitEvent event){
        super.exited(event);
        AstToken token = event.getToken();
        if (token.isInstantiation()){
            fireStateChangeEvent(ANONYMOUSCLASS_STATE.EXIT_INSTANTIATION, event);
        } else if (event.getToken().isClassBlock() && this.inInstantiation){
            fireStateChangeEvent(ANONYMOUSCLASS_STATE.EXIT_ANONYMOUSCLASS,event);
        }
    }
    
    public boolean isInInstantiation(){
        return inInstantiation;
    }
    
    @Override
    protected Boolean getState() {
        return this.inInstantiation;
    }

    @Override
    protected boolean isStateChangeTriggerToken(AstToken token) {
        return token.isClassBlock() || token.isInstantiation();
    }

    @Override
    protected void setState(Boolean state) {
        this.inInstantiation = state;
    }
    
    private boolean inInstantiation = false;
}
