package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * フィールドの情報を現すクラス． 以下の情報をもつ．
 * <ul>
 * <li>フィールド名</li>
 * <li>フィールドの型</li>
 * <li>フィールドの修飾子</li>
 * <li>フィールドを定義しているクラス</li>
 * <li>フィールドを参照しているメソッド群</li>
 * <li>フィールドに対して代入を行っているメソッド群</li>
 * </ul>
 * 
 * @author y-higo
 * 
 */
public final class FieldInfo extends VariableInfo {

    /**
     * フィールドオブジェクトを初期化する． フィールド名と型，定義しているクラスが与えられなければならない．
     * 
     * @param name フィールド名
     * @param type フィールドの型
     * @param ownerClass フィールドを定義しているクラス
     */
    public FieldInfo(final String name, final TypeInfo type, final ClassInfo ownerClass) {

        super(name, type);

        if (null == ownerClass) {
            throw new NullPointerException();
        }

        this.ownerClass = ownerClass;
        this.referencers = new TreeSet<MethodInfo>();
        this.assignmenters = new TreeSet<MethodInfo>();
    }

    /**
     * このフィールドを参照しているメソッドを追加する
     * 
     * @param referencer このフィールドを参照しているメソッド
     */
    public void addReferencer(final MethodInfo referencer) {

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
    public void addAssignmenter(final MethodInfo assignmenter) {

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
    public int compareTo(final FieldInfo fieldInfo) {

        if (null == fieldInfo) {
            throw new NullPointerException();
        }

        ClassInfo classInfo = this.getOwnerClass();
        ClassInfo correspondClassInfo = this.getOwnerClass();
        int classOrder = classInfo.compareTo(correspondClassInfo);
        if (classOrder != 0) {
            return classOrder;
        } else {
            return super.compareTo(fieldInfo);
        }
    }

    /**
     * このフィールドを定義しているクラスを返す
     * 
     * @return このフィールドを定義しているクラス
     */
    public ClassInfo getOwnerClass() {
        return this.ownerClass;
    }

    /**
     * このフィールドを参照しているメソッドの SortedSet を返す．
     * 
     * @return このフィールドを参照しているメソッドの SortedSet
     */
    public SortedSet<MethodInfo> getReferences() {
        return Collections.unmodifiableSortedSet(this.referencers);
    }

    /**
     * このフィールドに対して代入を行っているメソッドの SortedSet を返す．
     * 
     * @return このフィールドに対して代入を行っているメソッドの SortedSet
     */
    public SortedSet<MethodInfo> getAssignmenters() {
        return Collections.unmodifiableSortedSet(this.assignmenters);
    }

    /**
     * このフィールドを定義しているクラスを保存する変数
     */
    private final ClassInfo ownerClass;

    /**
     * このフィールドを参照しているメソッド群を保存するための変数
     */
    private final SortedSet<MethodInfo> referencers;

    /**
     * このフィールドに対して代入を行っているメソッド群を保存するための変数
     */
    private final SortedSet<MethodInfo> assignmenters;

    /**
     * フィールドの修飾子を表す変数
     */
    // TODO 修飾子を表す変数を定義する．
}
