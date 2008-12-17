package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;


/**
 * 未解決式を表すインターフェース
 * 
 * @author higo
 *
 * @param <T> 解決済みの型
 */
public interface UnresolvedExpressionInfo<T extends ExpressionInfo> extends
        UnresolvedConditionableInfo<T> {

}
