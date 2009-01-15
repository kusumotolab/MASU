package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 呼び出し可能な単位(メソッドやコンストラクタ)を表すクラス
 * 
 * @author higo
 */

public abstract class CallableUnitInfo extends LocalSpaceInfo implements Visualizable, Modifier,
        TypeParameterizable, Comparable<CallableUnitInfo> {

    /**
     * オブジェクトを初期化する
     * 
     * @param modifiers 修飾子のSet
     * @param ownerClass 所有クラス
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    CallableUnitInfo(final Set<ModifierInfo> modifiers, final ClassInfo ownerClass,
            final boolean privateVisible, final boolean namespaceVisible,
            final boolean inheritanceVisible, final boolean publicVisible, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(ownerClass, fromLine, fromColumn, toLine, toColumn);

        this.privateVisible = privateVisible;
        this.namespaceVisible = namespaceVisible;
        this.inheritanceVisible = inheritanceVisible;
        this.publicVisible = publicVisible;

        this.parameters = new LinkedList<ParameterInfo>();

        this.typeParameters = new LinkedList<TypeParameterInfo>();
        this.typeParameterUsages = new HashMap<TypeParameterInfo, TypeInfo>();

        this.unresolvedUsage = new HashSet<UnresolvedExpressionInfo<?>>();

        this.callers = new TreeSet<CallableUnitInfo>();

        this.modifiers = new HashSet<ModifierInfo>();
        this.modifiers.addAll(modifiers);
    }

    /**
     * 定義された変数のSetを返す
     * 
     * @return 定義された変数のSet
     */
    @Override
    public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        final Set<VariableInfo<? extends UnitInfo>> definedVariables = new HashSet<VariableInfo<? extends UnitInfo>>();
        definedVariables.addAll(super.getDefinedVariables());
        definedVariables.addAll(this.getParameters());
        return Collections.unmodifiableSet(definedVariables);
    }

    /**
     * メソッド間の順序関係を定義するメソッド．以下の順序で順序を決める．
     * <ol>
     * <li>メソッドを定義しているクラスの名前空間名</li>
     * <li>メソッドを定義しているクラスのクラス名</li>
     * <li>メソッド名</li>
     * <li>メソッドの引数の個数</li>
     * <li>メソッドの引数の型（第一引数から順番に）</li>
     */
    @Override
    public int compareTo(final CallableUnitInfo target) {

        if (null == target) {
            throw new IllegalArgumentException();
        }

        // クラスオブジェクトの compareTo を用いる．
        // クラスの名前空間名，クラス名が比較に用いられている．
        final ClassInfo ownerClass = this.getOwnerClass();
        final ClassInfo correspondOwnerClass = target.getOwnerClass();
        final int classOrder = ownerClass.compareTo(correspondOwnerClass);
        if (classOrder != 0) {
            return classOrder;
        }

        return this.compareArgumentsTo(target);
    }

    /**
     * 引数の型による比較を行う
     * 
     * @param target 比較対象ユニット
     * @return
     */
    public int compareArgumentsTo(final CallableUnitInfo target) {
        // 引数の個数で比較
        final int parameterNumber = this.getParameterNumber();
        final int correspondParameterNumber = target.getParameterNumber();
        if (parameterNumber < correspondParameterNumber) {
            return 1;
        } else if (parameterNumber > correspondParameterNumber) {
            return -1;
        } else {

            // 引数の型で比較．第一引数から順番に．
            final Iterator<ParameterInfo> parameterIterator = this.getParameters().iterator();
            final Iterator<ParameterInfo> correspondParameterIterator = target.getParameters()
                    .iterator();
            while (parameterIterator.hasNext() && correspondParameterIterator.hasNext()) {
                final ParameterInfo parameter = parameterIterator.next();
                final ParameterInfo correspondParameter = correspondParameterIterator.next();
                final String typeName = parameter.getType().getTypeName();
                final String correspondTypeName = correspondParameter.getType().getTypeName();
                final int typeOrder = typeName.compareTo(correspondTypeName);
                if (typeOrder != 0) {
                    return typeOrder;
                }
            }

            return 0;
        }
    }

    /**
     * このオブジェクトが，引数で与えられた情報を使って呼び出すことができるかどうかを判定する．
     * 
     * @param actualParameters 実引数のリスト
     * @return 呼び出せる場合は true，そうでない場合は false
     */
    boolean canCalledWith(final List<ExpressionInfo> actualParameters) {

        if (null == actualParameters) {
            throw new IllegalArgumentException();
        }

        // 引数の数が等しくない場合は該当しない
        final List<ParameterInfo> dummyParameters = this.getParameters();
        if (dummyParameters.size() != actualParameters.size()) {
            return false;
        }

        // 引数の型を先頭からチェック等しくない場合は該当しない
        final Iterator<ParameterInfo> dummyParameterIterator = dummyParameters.iterator();
        final Iterator<ExpressionInfo> actualParameterIterator = actualParameters.iterator();
        NEXT_PARAMETER: while (dummyParameterIterator.hasNext()
                && actualParameterIterator.hasNext()) {
            final ParameterInfo dummyParameter = dummyParameterIterator.next();
            final ExpressionInfo actualParameter = actualParameterIterator.next();

            TypeInfo actualParameterType = actualParameter.getType();

            // 型パラメータの場合はその継承型を求める
            if (actualParameterType instanceof TypeParameterInfo) {
                final TypeInfo extendsType = ((TypeParameterInfo) actualParameterType)
                        .getExtendsType();
                if (null != extendsType) {
                    actualParameterType = extendsType;
                } else {
                    assert false : "Here should not be reached";
                }
            }

            // 実引数が参照型の場合
            if (actualParameterType instanceof ClassTypeInfo) {

                // 実引数の型のクラスを取得
                final ClassInfo actualParameterClass = ((ClassTypeInfo) actualParameterType)
                        .getReferencedClass();

                // 仮引数が参照型でない場合は該当しない
                if (!(dummyParameter.getType() instanceof ClassTypeInfo)) {
                    return false;
                }

                // 仮引数の型のクラスを取得
                final ClassInfo dummyParameterClass = ((ClassTypeInfo) dummyParameter.getType())
                        .getReferencedClass();

                // 仮引数，実引数共に対象クラスである場合は，その継承関係を考慮する．つまり，実引数が仮引数のサブクラスでない場合は，呼び出し可能ではない
                if ((actualParameterClass instanceof TargetClassInfo)
                        && (dummyParameterClass instanceof TargetClassInfo)) {

                    // 実引数が仮引数と同じ参照型（クラス）でもなく，仮引数のサブクラスでもない場合は該当しない
                    if (actualParameterClass.equals(dummyParameterClass)) {
                        continue NEXT_PARAMETER;

                    } else if (actualParameterClass.isSubClass(dummyParameterClass)) {
                        continue NEXT_PARAMETER;

                    } else {
                        return false;
                    }

                    // 仮引数，実引数共に外部クラスである場合は，等しい場合のみ呼び出し可能とする
                } else if ((actualParameterClass instanceof ExternalClassInfo)
                        && (dummyParameterClass instanceof ExternalClassInfo)) {

                    if (actualParameterClass.equals(dummyParameterClass)) {
                        continue NEXT_PARAMETER;
                    }

                    return false;

                    // 仮引数が外部クラス，実引数が対象クラスの場合は，実引数が仮引数のサブクラスである場合，呼び出し可能とする
                } else if ((actualParameterClass instanceof TargetClassInfo)
                        && (dummyParameterClass instanceof ExternalClassInfo)) {

                    if (actualParameterClass.isSubClass(dummyParameterClass)) {
                        continue NEXT_PARAMETER;
                    }

                    return false;

                    // 仮引数が対象クラス，実引数が外部クラスの場合は，呼び出し不可能とする
                } else {
                    return false;
                }

                // 実引数がプリミティブ型の場合
            } else if (actualParameterType instanceof PrimitiveTypeInfo) {

                // PrimitiveTypeInfo#equals を使って等価性の判定．
                // 等しくない場合は該当しない
                // プリミティブタイプStringでdummmyTypeの型名もStringなら等価
                // TODO クラス名がStringであるがjava.lang.Stringではない場合，判定ミスがおこる．
                if (actualParameterType.equals(dummyParameter.getType())) {
                    continue NEXT_PARAMETER;
                }

                return false;

                // 実引数が配列型の場合
            } else if (actualParameterType instanceof ArrayTypeInfo) {

                if (!(dummyParameter.getType() instanceof ArrayTypeInfo)) {
                    return false;
                }

                if (!actualParameter.getType().equals(dummyParameter.getType())) {
                    return false;
                }

                continue NEXT_PARAMETER;
                // TODO Java言語の場合は，仮引数が java.lang.object でもOKな処理が必要

                // 実引数が null の場合
            } else if (actualParameter instanceof NullUsageInfo) {

                // 仮引数が参照型でない場合は該当しない
                if (!(dummyParameter.getType() instanceof ClassInfo)) {
                    return false;
                }

                continue NEXT_PARAMETER;
                // TODO Java言語の場合は，仮引数が配列型の場合でもOKな処理が必要

                // 実引数の型が解決できなかった場合
            } else if (actualParameterType instanceof UnknownTypeInfo) {

                // 実引数の型が不明な場合は，仮引数の型が何であろうともOKにしている
                continue NEXT_PARAMETER;

            } else {
                assert false : "Here shouldn't be reached!";
            }
        }

        return true;
    }

    /**
     * このメソッドの引数の List を返す．
     * 
     * @return このメソッドの引数の List
     */
    public final List<ParameterInfo> getParameters() {
        return Collections.unmodifiableList(this.parameters);
    }

    /**
     * このメソッドの引数の数を返す
     * 
     * @return このメソッドの引数の数
     */
    public final int getParameterNumber() {
        return this.parameters.size();
    }

    /**
     * 型パラメータの使用を追加する
     * 
     * @param typeParameterInfo 型パラメータ 
     * @param usedType 型パラメータに代入されている型
     */
    @Override
    public void addTypeParameterUsage(final TypeParameterInfo typeParameterInfo,
            final TypeInfo usedType) {

        if ((null == typeParameterInfo) || (null == usedType)) {
            throw new IllegalArgumentException();
        }

        this.typeParameterUsages.put(typeParameterInfo, usedType);
    }

    /**
     * 型パラメータ使用のマップを返す
     * 
     * @return 型パラメータ使用のマップ
     */
    @Override
    public Map<TypeParameterInfo, TypeInfo> getTypeParameterUsages() {
        return Collections.unmodifiableMap(this.typeParameterUsages);
    }

    /**
     * 引数で指定された型パラメータを追加する
     * 
     * @param typeParameter 追加する型パラメータ
     */
    @Override
    public void addTypeParameter(final TypeParameterInfo typeParameter) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeParameter) {
            throw new NullPointerException();
        }

        this.typeParameters.add(typeParameter);
    }

    /**
     * 指定されたインデックスの型パラメータを返す
     * 
     * @param index 型パラメータのインデックス
     * @return　指定されたインデックスの型パラメータ
     */
    @Override
    public TypeParameterInfo getTypeParameter(final int index) {
        return this.typeParameters.get(index);
    }

    /**
     * このクラスの型パラメータの List を返す．
     * 
     * @return このクラスの型パラメータの List
     */
    @Override
    public List<TypeParameterInfo> getTypeParameters() {
        return Collections.unmodifiableList(this.typeParameters);
    }

    /**
     * この呼び出しユニット内で，名前解決できなかったクラス参照，フィールド参照・代入，メソッド呼び出しを追加する． プラグインから呼ぶとランタイムエラー．
     * 
     * @param entityUsage 名前解決できなかったクラス参照，フィールド参照・代入，メソッド呼び出し
     */
    public void addUnresolvedUsage(final UnresolvedExpressionInfo<?> entityUsage) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == entityUsage) {
            throw new NullPointerException();
        }

        this.unresolvedUsage.add(entityUsage);
    }

    /**
     * この呼び出しユニット内で，名前解決できなかったクラス参照，フィールド参照・代入，メソッド呼び出しの Set を返す．
     * 
     * @return このメソッド内で，名前解決できなかったクラス参照，フィールド参照・代入，メソッド呼び出しの Set
     */
    public Set<UnresolvedExpressionInfo<?>> getUnresolvedUsages() {
        return Collections.unmodifiableSet(this.unresolvedUsage);
    }

    /**
     * 修飾子の Set を返す
     * 
     * @return 修飾子の Set
     */
    @Override
    public Set<ModifierInfo> getModifiers() {
        return Collections.unmodifiableSet(this.modifiers);
    }

    /**
     * 子クラスから参照可能かどうかを返す
     * 
     * @return 子クラスから参照可能な場合は true, そうでない場合は false
     */
    @Override
    public boolean isInheritanceVisible() {
        return this.inheritanceVisible;
    }

    /**
     * 同じ名前空間から参照可能かどうかを返す
     * 
     * @return 同じ名前空間から参照可能な場合は true, そうでない場合は false
     */
    @Override
    public boolean isNamespaceVisible() {
        return this.namespaceVisible;
    }

    /**
     * クラス内からのみ参照可能かどうかを返す
     * 
     * @return クラス内からのみ参照可能な場合は true, そうでない場合は false
     */
    @Override
    public boolean isPrivateVisible() {
        return this.privateVisible;
    }

    /**
     * どこからでも参照可能かどうかを返す
     * 
     * @return どこからでも参照可能な場合は true, そうでない場合は false
     */
    @Override
    public boolean isPublicVisible() {
        return this.publicVisible;
    }

    /**
     * このメソッドを呼び出しているメソッドまたはコンストラクタを追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param caller 追加する呼び出すメソッド
     */
    public final void addCaller(final CallableUnitInfo caller) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == caller) {
            throw new NullPointerException();
        }

        this.callers.add(caller);
    }

    /**
     * このメソッドを呼び出しているメソッドまたはコンストラクタの SortedSet を返す．
     * 
     * @return このメソッドを呼び出しているメソッドの SortedSet
     */
    public final SortedSet<CallableUnitInfo> getCallers() {
        return Collections.unmodifiableSortedSet(this.callers);
    }

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
     * 修飾子を保存するための変数
     */
    private final Set<ModifierInfo> modifiers;

    /**
     * 型パラメータを保存する変数
     */
    private final List<TypeParameterInfo> typeParameters;

    /**
     * このクラスで使用されている型パラメータと実際に型パラメータに代入されている型のペア.
     * このクラスで定義されている型パラメータではない．
     */
    private final Map<TypeParameterInfo, TypeInfo> typeParameterUsages;

    /**
     * 引数のリストの保存するための変数
     */
    protected final List<ParameterInfo> parameters;

    /**
     * このメソッドを呼び出しているメソッド一覧を保存するための変数
     */
    private final SortedSet<CallableUnitInfo> callers;

    /**
     * 名前解決できなかったクラス参照，フィールド参照・代入，メソッド呼び出しなどを保存するための変数
     */
    private final transient Set<UnresolvedExpressionInfo<?>> unresolvedUsage;
}
