package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedEntityUsageInfo;
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
 * @author higo
 * 
 */
public final class TargetMethodInfo extends MethodInfo implements Visualizable, Member, Position {

    /**
     * メソッドオブジェクトを初期化する．
     * 
     * @param modifiers 修飾子
     * @param name メソッド名
     * @param ownerClass 所有しているクラス
     * @param constructor コンストラクタかどうか．コンストラクタの場合は true,そうでない場合は false．
     * @param privateVisible クラス内からのみ参照可能
     * @param namespaceVisible 同じ名前空間から参照可能
     * @param inheritanceVisible 子クラスから参照可能
     * @param publicVisible どこからでも参照可能
     * @param instance インスタンスメンバーかどうか
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public TargetMethodInfo(final Set<ModifierInfo> modifiers, final String name,
            final ClassInfo ownerClass, final boolean constructor, final boolean privateVisible,
            final boolean namespaceVisible, final boolean inheritanceVisible,
            final boolean publicVisible, final boolean instance, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(name, ownerClass, constructor, fromLine, fromColumn, toLine, toColumn);

        if (null == modifiers) {
            throw new NullPointerException();
        }

        this.modifiers = new HashSet<ModifierInfo>();
        this.typeParameters = new LinkedList<TypeParameterInfo>();
        this.unresolvedUsage = new HashSet<UnresolvedEntityUsageInfo>();

        this.modifiers.addAll(modifiers);

        this.privateVisible = privateVisible;
        this.namespaceVisible = namespaceVisible;
        this.inheritanceVisible = inheritanceVisible;
        this.publicVisible = publicVisible;

        this.instance = instance;
    }

    /**
     * 引数で指定された型パラメータを追加する
     * 
     * @param typeParameter 追加する型パラメータ
     */
    public void addTypeParameter(final TypeParameterInfo typeParameter) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeParameter) {
            throw new NullPointerException();
        }

        this.typeParameters.add(typeParameter);
    }

    /**
     * このメソッド内で，名前解決できなかったクラス参照，フィールド参照・代入，メソッド呼び出しを追加する． プラグインから呼ぶとランタイムエラー．
     * 
     * @param entityUsage 名前解決できなかったクラス参照，フィールド参照・代入，メソッド呼び出し
     */
    public void addUnresolvedUsage(final UnresolvedEntityUsageInfo entityUsage) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == entityUsage) {
            throw new NullPointerException();
        }

        this.unresolvedUsage.add(entityUsage);
    }

    /**
     * 修飾子の Set を返す
     * 
     * @return 修飾子の Set
     */
    public Set<ModifierInfo> getModifiers() {
        return Collections.unmodifiableSet(this.modifiers);
    }

    /**
     * このメソッドの行数を返す
     * 
     * @return このメソッドの行数
     */
    public int getLOC() {
        return this.getToLine() - this.getFromLine() + 1;
    }

    /**
     * このメソッドが参照しているフィールドの SortedSet を返す．
     * 
     * @return このメソッドが参照しているフィールドの SortedSet
     */
    public SortedSet<FieldInfo> getReferencees() {

        final SortedSet<FieldInfo> referencees = new TreeSet<FieldInfo>();
        for (final FieldUsageInfo fieldUsage : this.getFieldUsages()) {
            if (fieldUsage.isReference()) {
                referencees.add(fieldUsage.getUsedVariable());
            }
        }

        return Collections.unmodifiableSortedSet(referencees);
    }

    /**
     * このメソッドが代入しているフィールドの SortedSet を返す．
     * 
     * @return このメソッドが代入しているフィールドの SortedSet
     */
    public SortedSet<FieldInfo> getAssignmentees() {
        final SortedSet<FieldInfo> assignmentees = new TreeSet<FieldInfo>();
        for (final FieldUsageInfo fieldUsage : this.getFieldUsages()) {
            if (fieldUsage.isAssignment()) {
                assignmentees.add(fieldUsage.getUsedVariable());
            }
        }

        return Collections.unmodifiableSortedSet(assignmentees);
    }

    /**
     * このクラスの型パラメータの List を返す．
     * 
     * @return このクラスの型パラメータの List
     */
    public List<TypeParameterInfo> getTypeParameters() {
        return Collections.unmodifiableList(this.typeParameters);
    }

    /**
     * このメソッド内で，名前解決できなかったクラス参照，フィールド参照・代入，メソッド呼び出しの Set を返す．
     * 
     * @return このメソッド内で，名前解決できなかったクラス参照，フィールド参照・代入，メソッド呼び出しの Set
     */
    public Set<UnresolvedEntityUsageInfo> getUnresolvedUsages() {
        return Collections.unmodifiableSet(this.unresolvedUsage);
    }

    /**
     * 子クラスから参照可能かどうかを返す
     * 
     * @return 子クラスから参照可能な場合は true, そうでない場合は false
     */
    public boolean isInheritanceVisible() {
        return this.inheritanceVisible;
    }

    /**
     * 同じ名前空間から参照可能かどうかを返す
     * 
     * @return 同じ名前空間から参照可能な場合は true, そうでない場合は false
     */
    public boolean isNamespaceVisible() {
        return this.namespaceVisible;
    }

    /**
     * クラス内からのみ参照可能かどうかを返す
     * 
     * @return クラス内からのみ参照可能な場合は true, そうでない場合は false
     */
    public boolean isPrivateVisible() {
        return this.privateVisible;
    }

    /**
     * どこからでも参照可能かどうかを返す
     * 
     * @return どこからでも参照可能な場合は true, そうでない場合は false
     */
    public boolean isPublicVisible() {
        return this.publicVisible;
    }

    /**
     * インスタンスメンバーかどうかを返す
     * 
     * @return インスタンスメンバーの場合 true，そうでない場合 false
     */
    public boolean isInstanceMember() {
        return this.instance;
    }

    /**
     * スタティックメンバーかどうかを返す
     * 
     * @return スタティックメンバーの場合 true，そうでない場合 false
     */
    public boolean isStaticMember() {
        return !this.instance;
    }

    /**
     * 修飾子を保存するための変数
     */
    private final Set<ModifierInfo> modifiers;

    /**
     * 型パラメータを保存する変数
     */
    private final List<TypeParameterInfo> typeParameters;

    /**
     * 名前解決できなかったクラス参照，フィールド参照・代入，メソッド呼び出しなどを保存するための変数
     */
    private final Set<UnresolvedEntityUsageInfo> unresolvedUsage;

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
