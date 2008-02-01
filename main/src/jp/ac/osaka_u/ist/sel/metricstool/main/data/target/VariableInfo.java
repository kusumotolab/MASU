package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * フィールド，引数，ローカル変数の共通の親クラス． 以下の情報を持つ．
 * <ul>
 * <li>変数名</li>
 * <li>型</li>
 * <li>修飾子</li>
 * <li>位置情報</li>
 * 
 * @author higo
 * 
 */
public abstract class VariableInfo extends UnitInfo implements Comparable<VariableInfo> {

    /**
     * 変数の順序を定義するメソッド．変数名（String）に従う．
     * 
     * @return 変数の順序関係
     */
    public final int compareTo(final VariableInfo variable) {

        if (null == variable) {
            throw new NullPointerException();
        }

        String variableName = this.getName();
        String correspondVariableName = variable.getName();
        return variableName.compareTo(correspondVariableName);
    }

    /**
     * 修飾子の Set を返す
     * 
     * @return 修飾子の Set
     */
    public final Set<ModifierInfo> getModifiers() {
        return Collections.unmodifiableSet(this.modifiers);
    }

    /**
     * 変数名を返す
     * 
     * @return 変数名
     */
    public final String getName() {
        return this.name;
    }

    /**
     * 変数の型を返す
     * 
     * @return 変数の型
     */
    public final TypeInfo getType() {
        return this.type;
    }

    /**
     * 変数オブジェクトを初期化する
     * 
     * @param modifiers 修飾子の Set
     * @param name 変数名
     * @param type 変数の型
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    VariableInfo(final Set<ModifierInfo> modifiers, final String name, final TypeInfo type,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == modifiers) || (null == name) || (null == type)) {
            throw new NullPointerException();
        }

        this.name = name;
        this.type = type;
        this.modifiers = new HashSet<ModifierInfo>();
        this.modifiers.addAll(modifiers);
    }

    /**
     * 修飾子を保存するための変数
     */
    private final Set<ModifierInfo> modifiers;

    /**
     * 変数名を表す変数
     */
    private final String name;

    /**
     * 変数の型を表す変数
     */
    private final TypeInfo type;
}
