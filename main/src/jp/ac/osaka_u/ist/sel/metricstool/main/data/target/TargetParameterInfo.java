package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;


/**
 * 対象メソッドの引数を表すクラス
 * 
 * @author y-higo
 *
 */
public final class TargetParameterInfo extends ParameterInfo {

    /**
     * 引数名，引数の型を与えてオブジェクトを初期化
     * 
     * @param modifiers 修飾子の Set
     * @param name 引数名
     * @param type 引数の型
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public TargetParameterInfo(final Set<ModifierInfo> modifiers, final String name,
            final TypeInfo type, final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {
        super(modifiers, name, type, fromLine, fromColumn, toLine, toColumn);
    }
}
