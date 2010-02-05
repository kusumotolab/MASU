package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricMeasurable;
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
@SuppressWarnings("serial")
public abstract class FieldInfo extends VariableInfo<ClassInfo<?, ?, ?, ?>> implements
        MetricMeasurable, Member, Visualizable, StaticOrInstance {

    /**
     * フィールドオブジェクトを初期化する． フィールド名と型，定義しているクラスが与えられなければならない．
     * 
     * @param modifiers 修飾子のセット
     * @param name フィールド名
     * @param type フィールドの型
     * @param definitionClass フィールドを定義しているクラス
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    FieldInfo(final Set<ModifierInfo> modifiers, final String name, final TypeInfo type,
            final ClassInfo<?, ?, ?, ?> definitionClass, final boolean privateVisible,
            final boolean namespaceVisible, final boolean inheritanceVisible,
            final boolean publicVisible, final boolean instance, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(modifiers, name, type, definitionClass, fromLine, fromColumn, toLine, toColumn);

        if (null == definitionClass) {
            throw new NullPointerException();
        }

        this.privateVisible = privateVisible;
        this.namespaceVisible = namespaceVisible;
        this.inheritanceVisible = inheritanceVisible;
        this.publicVisible = publicVisible;
        this.instance = instance;

        this.ownerClass = definitionClass;
        this.referencers = new TreeSet<CallableUnitInfo>();
        this.assignmenters = new TreeSet<CallableUnitInfo>();
    }

    /**
     * 与えられた変数のSetに含まれているフィールドをSetとして返す
     * @param variables 変数のSet
     * @return 与えられた変数のSetに含まれるフィールドのSet
     */
    public static Set<FieldInfo> getLocalVariables(Collection<VariableInfo<?>> variables) {
        final Set<FieldInfo> fields = new HashSet<FieldInfo>();
        for (final VariableInfo<?> variable : variables) {
            if (variable instanceof FieldInfo) {
                fields.add((FieldInfo) variable);
            }
        }
        return Collections.unmodifiableSet(fields);
    }

    /**
     * メトリクス計測対象としての名前を返す
     * 
     * @return メトリクス計測対象としての名前
     */
    public final String getMeasuredUnitName() {

        final StringBuilder sb = new StringBuilder(this.getName());
        sb.append("#");
        sb.append(this.getOwnerClass().getFullQualifiedName(
                Settings.getInstance().getLanguage().getNamespaceDelimiter()));
        return sb.toString();
    }

    /**
     * このフィールドを参照しているメソッドまたはコンストラクタを追加する
     * 
     * @param referencer このフィールドを参照しているメソッドまたはコンストラクタ
     */
    public final void addReferencer(final CallableUnitInfo referencer) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == referencer) {
            return;
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
     * @param fieldInfo 比較対象オブジェクト
     * @return フィールドの順序関係
     */
    //    @Override
    //    public final int compareTo(final TargetFieldInfo fieldInfo) {
    //
    //        if (null == fieldInfo) {
    //            throw new NullPointerException();
    //        }
    //        final ClassInfo classInfo = this.getOwnerClass();
    //        final ClassInfo correspondClassInfo = this.getOwnerClass();
    //        final int classOrder = classInfo.compareTo(correspondClassInfo);
    //        return 0 != classOrder ? classOrder : super.compareTo(fieldInfo);
    //    }
    /**
     * このフィールドを定義しているクラスを返す
     * 
     * @return このフィールドを定義しているクラス
     */
    public final ClassInfo<?, ?, ?, ?> getOwnerClass() {
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
     * 子クラスから参照可能かどうかを返す
     * 
     * @return 子クラスから参照可能な場合は true, そうでない場合は false
     */
    public final boolean isInheritanceVisible() {
        return this.inheritanceVisible;
    }

    /**
     * 同じ名前空間から参照可能かどうかを返す
     * 
     * @return 同じ名前空間から参照可能な場合は true, そうでない場合は false
     */
    public final boolean isNamespaceVisible() {
        return this.namespaceVisible;
    }

    /**
     * クラス内からのみ参照可能かどうかを返す
     * 
     * @return クラス内からのみ参照可能な場合は true, そうでない場合は false
     */
    public final boolean isPrivateVisible() {
        return this.privateVisible;
    }

    /**
     * どこからでも参照可能かどうかを返す
     * 
     * @return どこからでも参照可能な場合は true, そうでない場合は false
     */
    public final boolean isPublicVisible() {
        return this.publicVisible;
    }

    /**
     * インスタンスメンバーかどうかを返す
     * 
     * @return インスタンスメンバーの場合 true，そうでない場合 false
     */
    public final boolean isInstanceMember() {
        return this.instance;
    }

    /**
     * スタティックメンバーかどうかを返す
     * 
     * @return スタティックメンバーの場合 true，そうでない場合 false
     */
    public final boolean isStaticMember() {
        return !this.instance;
    }

    /**
     * このフィールドを定義しているクラスを保存する変数
     */
    protected final ClassInfo<?, ?, ?, ?> ownerClass;

    /**
     * このフィールドを参照しているメソッド群を保存するための変数
     */
    protected final SortedSet<CallableUnitInfo> referencers;

    /**
     * このフィールドに対して代入を行っているメソッド群を保存するための変数
     */
    protected final SortedSet<CallableUnitInfo> assignmenters;

    /**
     * クラス内からのみ参照可能かどうか保存するための変数
     */
    private final boolean privateVisible;

    /**
     * 同じ名前空間から参照可能かどうか保存するための変数
     */
    private final boolean namespaceVisible;

    /**
     * 子クラスから参照可能かどうか保存するための変数
     */
    private final boolean inheritanceVisible;

    /**
     * どこからでも参照可能かどうか保存するための変数
     */
    private final boolean publicVisible;

    /**
     * インスタンスメンバーかどうかを保存するための変数
     */
    private final boolean instance;
}
