package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * ローカル変数を表すクラス．型を提供するのみ．
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
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

    /**
     * 与えられた変数のSetに含まれているローカル変数をSetとして返す
     * @param variables 変数のSet
     * @return 与えられた変数のSetに含まれるローカル変数のSet
     */
    public static Set<LocalVariableInfo> getLocalVariables(Collection<VariableInfo<?>> variables) {
        final Set<LocalVariableInfo> localVariables = new HashSet<LocalVariableInfo>();
        for (final VariableInfo<?> variable : variables) {
            if (variable instanceof LocalVariableInfo) {
                localVariables.add((LocalVariableInfo) variable);
            }
        }
        return Collections.unmodifiableSet(localVariables);
    }
}
