package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import java.util.Set;

public interface ExpressionInfo extends Position, Comparable<ExpressionInfo>{

    /**
     * 式内での変数の使用を返す
     * 
     * @return 式内での変数の使用
     */
    public Set<VariableUsageInfo<?>> getVariableUsages();
}
