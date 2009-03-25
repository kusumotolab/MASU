package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;


/**
 * CFGノードのファクトリを表すインターフェース
 * @author t-miyake
 *
 */
public interface ICFGNodeFactory {

    /**
     * CFGのコントロールノードを生成
     * @param element 生成するCFGノードに対応する文
     * @return CFGノード
     */
    public CFGControlNode makeControlNode(ConditionInfo condition);

    /**
     * CFGのノーマルノードを生成
     * @param element 生成するCFGノードに対応する文
     * @return CFGノード
     */
    public CFGNormalNode<? extends ExecutableElementInfo> makeNormalNode(
            ExecutableElementInfo element);

    /**
     * このファクトリで生成されたノードのうち，引数で指定された文に対応するノードを返す
     * @param element 文
     * @return このファクトリで生成されたノード．対応するノードが生成済みでない場合はnull．
     */
    public CFGNode<? extends ExecutableElementInfo> getNode(ExecutableElementInfo element);
}
