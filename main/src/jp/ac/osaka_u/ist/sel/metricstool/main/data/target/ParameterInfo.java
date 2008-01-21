package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;


/**
 * 引数を表すためのクラス． 型を提供するのみ．
 * 
 * @author higo
 * 
 */
public abstract class ParameterInfo extends VariableInfo {

    /**
     * 引数オブジェクトを初期化する．名前と型が必要．
     * 
     * @param modifiers 修飾子の Set
     * @param name 引数名
     * @param type 引数の型
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public ParameterInfo(final Set<ModifierInfo> modifiers, final String name, final TypeInfo type,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(modifiers, name, type, fromLine, fromColumn, toLine, toColumn);
    }
}
