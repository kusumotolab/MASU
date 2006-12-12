package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 対象メソッドの情報を保有するクラス． 以下の情報を持つ．
 * <ul>
 * <li>メソッド名</li>
 * <li>修飾子</li>
 * <li>返り値の型</li>
 * <li>引数のリスト</li>
 * <li>行数</li>
 * <li>コントロールグラフ（しばらくは未実装）</li>
 * <li>ローカル変数</li>
 * <li>所属しているクラス</li>
 * <li>呼び出しているメソッド</li>
 * <li>呼び出されているメソッド</li>
 * <li>オーバーライドしているメソッド</li>
 * <li>オーバーライドされているメソッド</li>
 * <li>参照しているフィールド</li>
 * <li>代入しているフィールド</li>
 * </ul>
 * 
 * @author y-higo
 * 
 */
public final class TargetMethodInfo extends MethodInfo {

    /**
     * メソッドオブジェクトを初期化する． 以下の情報が引数として与えられなければならない．
     * <ul>
     * <li>メソッド名</li>
     * <li>修飾子</li>
     * <li>シグネチャ</li>
     * <li>所有しているクラス</li>
     * <li>コンストラクタかどうか</li>
     * </ul>
     * 
     * @param modifier 修飾子
     * @param name メソッド名
     * @param returnType 返り値の型．コンストラクタの場合は，そのクラスの型を与える．
     * @param ownerClass 所有しているクラス
     * @param constructor コンストラクタかどうか．コンストラクタの場合は true,そうでない場合は false．
     */
    public TargetMethodInfo(final ModifierInfo modifier, final String name,
            final TypeInfo returnType, final ClassInfo ownerClass, final boolean constructor,
            final int loc) {

        super(name, returnType, ownerClass, constructor);

        if (null == modifier) {
            throw new NullPointerException();
        }

        if (loc < 0) {
            throw new IllegalArgumentException("LOC must be 0 or more!");
        }

        this.modifier = modifier;
        this.loc = loc;
        this.localVariables = new TreeSet<LocalVariableInfo>();

        this.referencees = new TreeSet<FieldInfo>();
        this.assignmentees = new TreeSet<FieldInfo>();
    }

    /**
     * このメソッドで定義されているローカル変数を追加する． public 宣言してあるが， プラグインからの呼び出しははじく．
     * 
     * @param localVariable 追加する引数
     */
    public void addLocalVariable(final LocalVariableInfo localVariable) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == localVariable) {
            throw new NullPointerException();
        }

        this.localVariables.add(localVariable);
    }

    /**
     * このメソッドが参照している変数を追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param referencee 追加する参照されている変数
     */
    public void addReferencee(final FieldInfo referencee) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == referencee) {
            throw new NullPointerException();
        }

        this.referencees.add(referencee);
    }

    /**
     * このメソッドが代入を行っている変数を追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param assignmentee 追加する代入されている変数
     */
    public void addAssignmentee(final FieldInfo assignmentee) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == assignmentee) {
            throw new NullPointerException();
        }

        this.assignmentees.add(assignmentee);
    }

    /**
     * このメソッドで定義されているローカル変数の SortedSet を返す．
     * 
     * @return このメソッドで定義されているローカル変数の SortedSet
     */
    public SortedSet<LocalVariableInfo> getLocalVariables() {
        return Collections.unmodifiableSortedSet(this.localVariables);
    }

    /**
     * 修飾子を返す
     * 
     * @return 修飾子
     */
    public ModifierInfo getModifier() {
        return this.modifier;
    }

    /**
     * このメソッドの行数を返す
     * 
     * @return このメソッドの行数
     */
    public int getLOC() {
        return this.loc;
    }

    /**
     * このメソッドが参照しているフィールドの SortedSet を返す．
     * 
     * @return このメソッドが参照しているフィールドの SortedSet
     */
    public SortedSet<FieldInfo> getReferencees() {
        return Collections.unmodifiableSortedSet(this.referencees);
    }

    /**
     * このメソッドが代入しているフィールドの SortedSet を返す．
     * 
     * @return このメソッドが代入しているフィールドの SortedSet
     */
    public SortedSet<FieldInfo> getAssignmentees() {
        return Collections.unmodifiableSortedSet(this.assignmentees);
    }

    /**
     * 修飾子を保存するための変数
     */
    private final ModifierInfo modifier;

    /**
     * 行数を保存するための変数
     */
    private final int loc;

    /**
     * このメソッドの内部で定義されているローカル変数
     */
    private final SortedSet<LocalVariableInfo> localVariables;

    /**
     * 参照しているフィールド一覧を保存するための変数
     */
    private final SortedSet<FieldInfo> referencees;

    /**
     * 代入しているフィールド一覧を保存するための変数
     */
    private final SortedSet<FieldInfo> assignmentees;

}
