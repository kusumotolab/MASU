package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * if文やfor文の条件を表すクラス
 * 
 * @author higo
 *
 */
public interface ConditionInfo extends ExecutableElementInfo {
    
    /**
     * この条件を所有しているExecutableElementInfoを返す
     * 
     * @return
     */
    ExecutableElementInfo getOwnerExecutableElement();
}
