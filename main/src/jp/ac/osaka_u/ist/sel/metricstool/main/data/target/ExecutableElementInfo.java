package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.io.Serializable;
import java.util.Set;


/**
 * 実行可能な単位を表す要素
 * 
 * @author higo
 *
 */
public interface ExecutableElementInfo extends Position, Comparable<ExecutableElementInfo>,
        Serializable {

    /**
     * 変数の使用を返す
     * 
     * @return 式内での変数の使用
     */
    Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages();

    /**
     * メソッド呼び出しを返す
     * 
     * @return メソッド呼び出し
     */
    Set<CallInfo<?>> getCalls();
}