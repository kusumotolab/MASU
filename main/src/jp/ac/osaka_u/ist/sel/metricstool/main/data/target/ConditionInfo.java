package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * if文やfor文の条件を表すクラス
 * 
 * @author higo
 *
 */
public interface ConditionInfo extends ExecutableElementInfo {

    /**
     * この条件のテキスト表現（String型）を返す
     * 
     * @return この条件のテキスト表現（String型）
     */
    String getText();
}
