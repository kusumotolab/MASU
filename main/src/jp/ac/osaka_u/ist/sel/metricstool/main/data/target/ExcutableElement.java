package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import java.util.Set;


public interface ExcutableElement extends Position, Comparable<ExcutableElement> {

    /**
     * 式内での変数の使用を返す
     * 
     * @return 式内での変数の使用
     */
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages();
}
