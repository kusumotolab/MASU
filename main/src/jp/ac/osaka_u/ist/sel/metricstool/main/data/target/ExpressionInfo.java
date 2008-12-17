package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

/**
 * 式を表すインターフェース
 * 
 * @author higo
 *
 */
public interface ExpressionInfo extends ConditionableInfo {

    TypeInfo getType();
    
    String getText();
}
