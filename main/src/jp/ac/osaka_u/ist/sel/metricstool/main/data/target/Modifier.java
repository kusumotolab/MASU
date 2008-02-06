package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;

/**
 * 修飾子を持つことができることを表すインターフェース
 * 
 * @author higo
 *
 */
public interface Modifier {

    Set<ModifierInfo> getModifiers();
}
