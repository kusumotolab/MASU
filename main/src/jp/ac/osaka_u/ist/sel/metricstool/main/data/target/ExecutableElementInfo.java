package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import java.io.Serializable;
import java.util.Set;


public interface ExecutableElementInfo extends Position, Comparable<ExecutableElementInfo>, Serializable{

    /**
     * 式内での変数の使用を返す
     * 
     * @return 式内での変数の使用
     */
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages();
}
