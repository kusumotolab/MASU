package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import java.util.Set;


/**
 * @author higo
 *
 */
public interface StatementInfo extends Position, Comparable<StatementInfo> {

    /**
     * 文内での変数の使用を返す
     * 
     * @return 文内での変数の使用
     */
    public Set<VariableUsageInfo<?>> getVariableUsages();
}
