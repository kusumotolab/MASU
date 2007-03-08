package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;


/**
 * 未解決メソッド呼び出しを保存するためのクラス
 * 
 * @author y-higo
 * 
 */
public final class UnresolvedMethodCallInfo implements UnresolvedEntityUsageInfo {

    /**
     * メソッド呼び出しが実行される変数の型，メソッド名を与えてオブジェクトを初期化
     * 
     * @param ownerClassType メソッド呼び出しが実行される変数の型
     * @param methodName メソッド名
     */
    public UnresolvedMethodCallInfo(final UnresolvedEntityUsageInfo ownerClassType,
            final String methodName, final boolean constructor) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == ownerClassType) || (null == methodName)) {
            throw new NullPointerException();
        }

        this.ownerClassType = ownerClassType;
        this.methodName = methodName;
        this.constructor = constructor;
        this.typeParameterUsages = new LinkedList<UnresolvedTypeParameterUsageInfo>();
        this.parameterTypes = new LinkedList<UnresolvedEntityUsageInfo>();

        this.resolvedInfo = null;
    }

    /**
     * この未解決メソッド呼び出しがすでに解決されているかどうかを返す
     * 
     * @return 既に解決されている場合は true，そうでない場合は false
     */
    public boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * 解決済みメソッド呼び出し情報を返す
     * 
     * @return 解決済みメソッド呼び出し情報
     * @throw 解決されていない場合にスローされる
     */
    public EntityUsageInfo getResolvedEntityUsage() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }

    public EntityUsageInfo resolveEntityUsage(final TargetClassInfo usingClass,
            final TargetMethodInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolvedEntityUsage();
        }

        // メソッドのシグネチャを取得
        final String methodName = this.getMethodName();
        final boolean constructor = this.isConstructor();
        final List<UnresolvedEntityUsageInfo> unresolvedParameters = this.getParameters();

        // メソッドの未解決引数を解決
        final List<TypeInfo> parameterTypes = new LinkedList<TypeInfo>();
        for (final UnresolvedEntityUsageInfo unresolvedParameter : unresolvedParameters) {
            EntityUsageInfo parameter = unresolvedParameter.resolveEntityUsage(usingClass,
                    usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
            assert parameter != null : "resolveEntityUsage returned null!";
            if (parameter instanceof UnknownEntityUsageInfo) {
                if (unresolvedParameter instanceof UnresolvedClassReferenceInfo) {
                    final ExternalClassInfo externalClassInfo = NameResolver
                            .createExternalClassInfo((UnresolvedClassReferenceInfo) unresolvedParameter);
                    classInfoManager.add(externalClassInfo);
                    parameter = new ClassReferenceInfo(externalClassInfo);
                } else {
                    assert false : "Here shouldn't be reached!";
                }
            }
            parameterTypes.add(parameter.getType());
        }

        // 親の型を解決
        final UnresolvedEntityUsageInfo unresolvedOwnerUsage = this.getOwnerClassType();
        EntityUsageInfo ownerUsage = unresolvedOwnerUsage.resolveEntityUsage(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        assert ownerUsage != null : "resolveEntityUsage returned null!";
        if (ownerUsage instanceof UnknownEntityUsageInfo) {
            if (unresolvedOwnerUsage instanceof UnresolvedClassReferenceInfo) {
                final ExternalClassInfo externalClassInfo = NameResolver
                        .createExternalClassInfo((UnresolvedClassReferenceInfo) unresolvedOwnerUsage);
                classInfoManager.add(externalClassInfo);
                ownerUsage = new ClassReferenceInfo(externalClassInfo);
            }
        }

        // -----ここから親の型に応じて処理を分岐
        // 親が解決できなかった場合はどうしようもない
        if (ownerUsage.getType() instanceof UnknownTypeInfo) {

            // 見つからなかった処理を行う
            usingMethod.addUnresolvedUsage(this);

            this.resolvedInfo = UnknownEntityUsageInfo.getInstance();
            return this.resolvedInfo;

            // 親が対象クラス(TargetClassInfo)だった場合
        } else if (ownerUsage.getType() instanceof TargetClassInfo) {

            // まずは利用可能なメソッドから検索
            {
                // 利用可能なメソッド一覧を取得
                final List<TargetMethodInfo> availableMethods = NameResolver.getAvailableMethods(
                        (TargetClassInfo) ownerUsage.getType(), usingClass);

                // 利用可能なメソッドから，未解決メソッドと一致するものを検索
                // メソッド名，引数の型のリストを用いて，このメソッドの呼び出しであるかどうかを判定
                for (final TargetMethodInfo availableMethod : availableMethods) {

                    // 呼び出し可能なメソッドが見つかった場合
                    if (availableMethod.canCalledWith(methodName, parameterTypes)) {
                        usingMethod.addCallee(availableMethod);
                        availableMethod.addCaller(usingMethod);

                        this.resolvedInfo = new MethodCallInfo(availableMethod);
                        return this.resolvedInfo;
                    }
                }
            }

            // 利用可能なメソッドが見つからなかった場合は，外部クラスである親クラスがあるはず．
            // そのクラスのメソッドを使用しているとみなす
            {
                final ExternalClassInfo externalSuperClass = NameResolver
                        .getExternalSuperClass((TargetClassInfo) ownerUsage.getType());
                if (null != externalSuperClass) {

                    final ExternalMethodInfo methodInfo = new ExternalMethodInfo(methodName,
                            externalSuperClass, constructor);
                    final List<ParameterInfo> parameters = NameResolver
                            .createParameters(parameterTypes);
                    methodInfo.addParameters(parameters);

                    usingMethod.addCallee(methodInfo);
                    methodInfo.addCaller(usingMethod);
                    methodInfoManager.add(methodInfo);

                    // 外部クラスに新規で外部メソッド変数（ExternalMethodInfo）を追加したので型は不明
                    this.resolvedInfo = new MethodCallInfo(methodInfo);
                    return this.resolvedInfo;
                }

                assert false : "Here shouldn't be reached!";
            }

            // 見つからなかった処理を行う
            {
                err.println("Can't resolve method Call : " + this.getMethodName());

                usingMethod.addUnresolvedUsage(this);

                this.resolvedInfo = UnknownEntityUsageInfo.getInstance();
                return this.resolvedInfo;
            }

            // 親が外部クラス（ExternalClassInfo）だった場合
        } else if (ownerUsage.getType() instanceof ExternalClassInfo) {

            final ExternalMethodInfo methodInfo = new ExternalMethodInfo(methodName,
                    (ExternalClassInfo) ownerUsage.getType(), constructor);
            final List<ParameterInfo> parameters = NameResolver.createParameters(parameterTypes);
            methodInfo.addParameters(parameters);

            usingMethod.addCallee(methodInfo);
            methodInfo.addCaller(usingMethod);
            methodInfoManager.add(methodInfo);

            // 外部クラスに新規で外部メソッド(ExternalMethodInfo)を追加したので型は不明．
            this.resolvedInfo = new MethodCallInfo(methodInfo);
            return this.resolvedInfo;

            // 親が配列だった場合
        } else if (ownerUsage.getType() instanceof ArrayTypeInfo) {

            // XXX Java言語であれば， java.lang.Object に対する呼び出し
            if (Settings.getLanguage().equals(LANGUAGE.JAVA)) {
                final ClassInfo ownerClass = classInfoManager.getClassInfo(new String[] { "java",
                        "lang", "Object" });
                final ExternalMethodInfo methodInfo = new ExternalMethodInfo(methodName,
                        ownerClass, false);
                final List<ParameterInfo> parameters = NameResolver
                        .createParameters(parameterTypes);
                methodInfo.addParameters(parameters);

                usingMethod.addCallee(methodInfo);
                methodInfo.addCaller(usingMethod);
                methodInfoManager.add(methodInfo);

                // 外部クラスに新規で外部メソッドを追加したので型は不明
                this.resolvedInfo = new MethodCallInfo(methodInfo);
                return this.resolvedInfo;
            }

            // 親がプリミティブ型だった場合
        } else if (ownerUsage.getType() instanceof PrimitiveTypeInfo) {

            switch (Settings.getLanguage()) {
            // Java の場合はオートボクシングでのメソッド呼び出しが可能
            // TODO 将来的にはこの switch文はとる．なぜなら TypeConverter.getTypeConverter(LANGUAGE)があるから．
            case JAVA:
                final ExternalClassInfo wrapperClass = TypeConverter.getTypeConverter(
                        Settings.getLanguage()).getWrapperClass(
                        (PrimitiveTypeInfo) ownerUsage.getType());
                final ExternalMethodInfo methodInfo = new ExternalMethodInfo(methodName,
                        wrapperClass, constructor);
                final List<ParameterInfo> parameters = NameResolver
                        .createParameters(parameterTypes);
                methodInfo.addParameters(parameters);

                usingMethod.addCallee(methodInfo);
                methodInfo.addCaller(usingMethod);
                methodInfoManager.add(methodInfo);

                // 外部クラスに新規で外部メソッド(ExternalMethodInfo)を追加したので型は不明．
                this.resolvedInfo = new MethodCallInfo(methodInfo);
                return this.resolvedInfo;

            default:
                assert false : "Here shouldn't be reached!";
                this.resolvedInfo = UnknownEntityUsageInfo.getInstance();
                return this.resolvedInfo;
            }
        }

        assert false : "Here shouldn't be reached!";
        this.resolvedInfo = UnknownEntityUsageInfo.getInstance();
        return this.resolvedInfo;
    }

    /**
     * 型パラメータ使用を追加する
     * 
     * @param typeParameterUsage 追加する型パラメータ使用
     */
    public void addTypeParameterUsage(final UnresolvedTypeParameterUsageInfo typeParameterUsage) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeParameterUsage) {
            throw new NullPointerException();
        }

        this.typeParameterUsages.add(typeParameterUsage);
    }

    /**
     * 引数を追加
     * 
     * @param typeInfo
     */
    public void addParameter(final UnresolvedEntityUsageInfo typeInfo) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeInfo) {
            throw new NullPointerException();
        }

        this.parameterTypes.add(typeInfo);
    }

    /**
     * 引数の List を返す
     * 
     * @return 引数の List
     */
    public List<UnresolvedEntityUsageInfo> getParameters() {
        return Collections.unmodifiableList(this.parameterTypes);
    }

    /**
     * 型パラメータ使用の List を返す
     * 
     * @return 型パラメータ使用の List
     */
    public List<UnresolvedTypeParameterUsageInfo> getTypeParameterUsages() {
        return Collections.unmodifiableList(this.typeParameterUsages);
    }

    /**
     * メソッド呼び出しが実行される変数の型を返す
     * 
     * @return メソッド呼び出しが実行される変数の型
     */
    public UnresolvedEntityUsageInfo getOwnerClassType() {
        return this.ownerClassType;
    }

    /**
     * コンストラクタかどうかを返す
     * 
     * @return コンストラクタである場合は true，そうでない場合は false
     */
    public boolean isConstructor() {
        return this.constructor;
    }

    /**
     * メソッド名を返す
     * 
     * @return メソッド名
     */
    public String getMethodName() {
        return this.methodName;
    }

    /**
     * メソッド呼び出しが実行される変数の型を保存するための変数
     */
    private final UnresolvedEntityUsageInfo ownerClassType;

    /**
     * メソッド名を保存するための変数
     */
    private final String methodName;

    /**
     * 型パラメータ使用を保存するための変数
     */
    private final List<UnresolvedTypeParameterUsageInfo> typeParameterUsages;

    /**
     * 引数を保存するための変数
     */
    private final List<UnresolvedEntityUsageInfo> parameterTypes;

    /**
     * 呼び出しがコンストラクタかどうかを保存するための変数
     */
    private final boolean constructor;

    /**
     * 解決済みメソッド呼び出し情報を保存するための変数
     */
    private EntityUsageInfo resolvedInfo;

    /**
     * エラーメッセージ出力用のプリンタ
     */
    private static final MessagePrinter err = new DefaultMessagePrinter(new MessageSource() {
        public String getMessageSourceName() {
            return "UnresolvedMethodCall";
        }
    }, MESSAGE_TYPE.ERROR);
}
