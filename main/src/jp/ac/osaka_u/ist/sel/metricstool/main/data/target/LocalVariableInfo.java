package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;


/**
 * ローカル変数を表すクラス．型を提供するのみ．
 * 
 * @author higo
 * 
 */
public final class LocalVariableInfo extends VariableInfo<LocalSpaceInfo> {

    /**
     * ローカル変数オブジェクトを初期化する．変数名と変数の型が必要．
     * 
     * @param modifiers 修飾子の Set
     * @param name ローカル変数名
     * @param type ローカル変数の型
     * @param definitionSpace この変数を定義しているブロック
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public LocalVariableInfo(final Set<ModifierInfo> modifiers, final String name,
            final TypeInfo type, final LocalSpaceInfo definitionSpace, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(modifiers, name, type, definitionSpace, fromLine, fromColumn, toLine, toColumn);
    }

}
