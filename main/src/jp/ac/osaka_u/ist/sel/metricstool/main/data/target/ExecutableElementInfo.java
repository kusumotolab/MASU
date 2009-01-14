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
     * 変数の使用のSetを返す
     * 
     * @return 変数の使用のSet
     */
    Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages();


    /**
     * 定義されている変数のSetを返す
     * 
     * @return 文の中で定義されている変数のSet
     */
    Set<VariableInfo<? extends UnitInfo>> getDefinedVariables();
    
    /**
     * メソッド呼び出しを返す
     * 
     * @return メソッド呼び出し
     */
    Set<CallInfo<?>> getCalls();
}