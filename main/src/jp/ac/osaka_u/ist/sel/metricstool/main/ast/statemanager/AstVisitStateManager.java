package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitListener;

/**
 * 抽象構文木のビジターがどのような状態にあるかを管理するインタフェース.
 * 
 * @author kou-tngt
 *
 */
public interface AstVisitStateManager extends StateManager<AstVisitEvent>, AstVisitListener{

}
