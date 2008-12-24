package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * 式を表すインターフェース
 * 
 * @author higo
 *
 */
public interface ExpressionInfo extends ConditionInfo {

    /**
     * この式の型を返す
     * 
     * @return この式の型
     */
    TypeInfo getType();

    /**
     * このExecutableElementの直接のオーナーであるExecutableElementを返す
     * 
     * @return このExecutableElementの直接のオーナーであるExecutableElement
     */
    ExecutableElementInfo getOwnerExecutableElement();

    void setOwnerExecutableElement(ExecutableElementInfo ownerExecutableElement);

    /**
     * このExecutableElementのオーナーであるStatementInfoを返す
     * 
     * @return このExecutableElementのオーナーであるStatementInfo
     */
    StatementInfo getOwnerStatement();
}
