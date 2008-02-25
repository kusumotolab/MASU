package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * フィールドオブジェクトを表すクラス． 以下の情報をもつ．
 * <ul>
 * <li>フィールド名</li>
 * <li>フィールドの型</li>
 * <li>フィールドの修飾子</li>
 * <li>フィールドを定義しているクラス</li>
 * <li>フィールドを参照しているメソッド群</li>
 * <li>フィールドに対して代入を行っているメソッド群</li>
 * </ul>
 * 
 * @author higo
 */
public abstract class FieldInfo extends VariableInfo<FieldUsageInfo> {

    /**
     * フィールドオブジェクトを初期化する． フィールド名と型，定義しているクラスが与えられなければならない．
     * 
     * @param name フィールド名
     * @param type フィールドの型
     * @param ownerClass フィールドを定義しているクラス
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public FieldInfo(final Set<ModifierInfo> modifiers, final String name, final TypeInfo type,
            final ClassInfo ownerClass, final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {

        super(modifiers, name, type, fromLine, fromColumn, toLine, toColumn);

        if (null == ownerClass) {
            throw new NullPointerException();
        }

        this.ownerClass = ownerClass;
        this.referencers = new TreeSet<CallableUnitInfo>();
        this.assignmenters = new TreeSet<CallableUnitInfo>();
    }

    /**
     * このフィールドを参照しているメソッドまたはコンストラクタを追加する
     * 
     * @param referencer このフィールドを参照しているメソッドまたはコンストラクタ
     */
    public final void addReferencer(final CallableUnitInfo referencer) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == referencer) {
            throw new NullPointerException();
        }

        this.referencers.add(referencer);
    }

    /**
     * このフィールドに対して代入を行っているメソッドを追加する
     * 
     * @param assignmenter このフィールドに対して代入を行っているメソッド
     */
    public final void addAssignmenter(final CallableUnitInfo assignmenter) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == assignmenter) {
            throw new NullPointerException();
        }

        this.assignmenters.add(assignmenter);
    }

    /**
     * フィールドオブジェクトの順序を定義するメソッド．そのフィールドを定義しているクラスの順序に従う．同じクラス内に定義されている場合は，
     * 
     * @return フィールドの順序関係
     */
    public final int compareTo(final TargetFieldInfo fieldInfo) {

        if (null == fieldInfo) {
            throw new NullPointerException();
        }

        final ClassInfo classInfo = this.getOwnerClass();
        final ClassInfo correspondClassInfo = this.getOwnerClass();
        final int classOrder = classInfo.compareTo(correspondClassInfo);
        return 0 != classOrder ? classOrder : super.compareTo(fieldInfo);
    }

    /**
     * このフィールドを定義しているクラスを返す
     * 
     * @return このフィールドを定義しているクラス
     */
    public final ClassInfo getOwnerClass() {
        return this.ownerClass;
    }

    /**
     * このフィールドを参照しているメソッドまたはコンストラクタの SortedSet を返す．
     * 
     * @return このフィールドを参照しているメソッドまたはコンストラクタの SortedSet
     */
    public final SortedSet<CallableUnitInfo> getReferences() {
        return Collections.unmodifiableSortedSet(this.referencers);
    }

    /**
     * このフィールドに対して代入を行っているメソッドまたはコンストラクタの SortedSet を返す．
     * 
     * @return このフィールドに対して代入を行っているメソッドまたはコンストラクタの SortedSet
     */
    public final SortedSet<CallableUnitInfo> getAssignmenters() {
        return Collections.unmodifiableSortedSet(this.assignmenters);
    }

    /**
     * このフィールドを定義しているクラスを保存する変数
     */
    protected final ClassInfo ownerClass;

    /**
     * このフィールドを参照しているメソッド群を保存するための変数
     */
    protected final SortedSet<CallableUnitInfo> referencers;

    /**
     * このフィールドに対して代入を行っているメソッド群を保存するための変数
     */
    protected final SortedSet<CallableUnitInfo> assignmenters;

}
