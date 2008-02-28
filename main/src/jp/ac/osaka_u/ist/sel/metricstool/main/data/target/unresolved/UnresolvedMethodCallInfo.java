package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;


/**
 * 未解決メソッド呼び出しを保存するためのクラス
 * 
 * @author higo
 * 
 */
public final class UnresolvedMethodCallInfo extends UnresolvedCallInfo {

    /**
     * メソッド呼び出しが実行される変数の型，メソッド名を与えてオブジェクトを初期化
     * 
     * @param ownerUsage メソッド呼び出しが実行される変数の型
     * @param methodName メソッド名
     */
    public UnresolvedMethodCallInfo(final UnresolvedEntityUsageInfo ownerUsage,
            final String methodName) {

        if ((null == ownerUsage) || (null == methodName)) {
            throw new NullPointerException();
        }

        this.ownerUsage = ownerUsage;
        this.methodName = methodName;
    }

    @Override
    public EntityUsageInfo resolveEntityUsage(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
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

        // 使用位置を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // メソッドのシグネチャを取得
        final String name = this.getName();
        final List<EntityUsageInfo> actualParameters = super.resolveParameters(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

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
                final ClassTypeInfo referenceType = new ClassTypeInfo(externalClassInfo);
                for (final UnresolvedTypeInfo unresolvedTypeArgument : ((UnresolvedClassReferenceInfo) unresolvedOwnerUsage)
                        .getTypeArguments()) {
                    final TypeInfo typeArgument = unresolvedTypeArgument.resolveType(usingClass,
                            usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                    referenceType.addTypeArgument(typeArgument);
                }
                ownerUsage = new ClassReferenceInfo(referenceType, fromLine, fromColumn, toLine,
                        toColumn);
            }
        }

        // -----ここから親の型に応じて処理を分岐
        TypeInfo ownerType = ownerUsage.getType();

        // 型パラメータの場合はその継承型を求める
        if (ownerType instanceof TypeParameterInfo) {
            final TypeInfo extendsType = ((TypeParameterInfo) ownerType).getExtendsType();
            if (null != extendsType) {
                ownerType = extendsType;
            } else {
                assert false : "Here should not be reached";
                final ExternalMethodInfo unknownMethod = new ExternalMethodInfo(name);
                this.resolvedInfo = new MethodCallInfo(ownerType, unknownMethod, fromLine,
                        fromColumn, toLine, toColumn);
                return this.resolvedInfo;
            }
        }

        // 親が解決できなかった場合はどうしようもない
        if (ownerType instanceof UnknownTypeInfo) {

            final ExternalMethodInfo unknownMethod = new ExternalMethodInfo(name);
            this.resolvedInfo = new MethodCallInfo(ownerType, unknownMethod, fromLine, fromColumn,
                    toLine, toColumn);
            return this.resolvedInfo;

            // 親がクラス型だった場合
        } else if (ownerType instanceof ClassTypeInfo) {

            final ClassInfo ownerClass = ((ClassTypeInfo) ownerType).getReferencedClass();
            if (ownerClass instanceof TargetClassInfo) {

                // まずは利用可能なメソッドから検索
                {
                    // 利用可能なメソッド一覧を取得
                    final List<TargetMethodInfo> availableMethods = NameResolver
                            .getAvailableMethods((TargetClassInfo) ownerClass, usingClass);

                    // 利用可能なメソッドから，未解決メソッドと一致するものを検索
                    // メソッド名，引数の型のリストを用いて，このメソッドの呼び出しであるかどうかを判定
                    for (final TargetMethodInfo availableMethod : availableMethods) {

                        // 呼び出し可能なメソッドが見つかった場合
                        if (availableMethod.canCalledWith(name, actualParameters)) {
                            this.resolvedInfo = new MethodCallInfo(ownerType, availableMethod,
                                    fromLine, fromColumn, toLine, toColumn);
                            ((MethodCallInfo) this.resolvedInfo).addParameters(actualParameters);
                            return this.resolvedInfo;
                        }
                    }
                }

                // 利用可能なメソッドが見つからなかった場合は，外部クラスである親クラスがあるはず．
                // そのクラスのメソッドを使用しているとみなす
                {
                    final ExternalClassInfo externalSuperClass = NameResolver
                            .getExternalSuperClass((TargetClassInfo) ownerClass);
                    if (null != externalSuperClass) {

                        final ExternalMethodInfo methodInfo = new ExternalMethodInfo(
                                this.getName(), externalSuperClass);
                        final List<ParameterInfo> dummyParameters = NameResolver
                                .createParameters(actualParameters, methodInfo);
                        methodInfo.addParameters(dummyParameters);
                        methodInfoManager.add(methodInfo);

                        // 外部クラスに新規で外部メソッド変数（ExternalMethodInfo）を追加したので型は不明
                        this.resolvedInfo = new MethodCallInfo(ownerType, methodInfo, fromLine,
                                fromColumn, toLine, toColumn);
                        ((MethodCallInfo) this.resolvedInfo).addParameters(actualParameters);
                        return this.resolvedInfo;
                    }

                    assert false : "Here shouldn't be reached!";
                }

                // 見つからなかった処理を行う
                {
                    err.println("Can't resolve method Call : " + this.getName());

                    final ExternalMethodInfo unknownMethod = new ExternalMethodInfo(name);
                    this.resolvedInfo = new MethodCallInfo(ownerType, unknownMethod, fromLine,
                            fromColumn, toLine, toColumn);
                    return this.resolvedInfo;
                }

                // 親が外部クラス（ExternalClassInfo）だった場合
            } else if (ownerClass instanceof ExternalClassInfo) {

                final ExternalMethodInfo methodInfo = new ExternalMethodInfo(this.getName(),
                        ownerClass);
                final List<ParameterInfo> parameters = NameResolver
                        .createParameters(actualParameters, methodInfo);
                methodInfo.addParameters(parameters);
                methodInfoManager.add(methodInfo);

                // 外部クラスに新規で外部メソッド(ExternalMethodInfo)を追加したので型は不明．
                this.resolvedInfo = new MethodCallInfo(ownerType, methodInfo, fromLine, fromColumn,
                        toLine, toColumn);
                ((MethodCallInfo) this.resolvedInfo).addParameters(actualParameters);
                return this.resolvedInfo;
            }

            // 親が配列だった場合
        } else if (ownerType instanceof ArrayTypeInfo) {

            // XXX Java言語であれば， java.lang.Object に対する呼び出し
            if (Settings.getLanguage().equals(LANGUAGE.JAVA)) {
                final ClassInfo ownerClass = classInfoManager.getClassInfo(new String[] { "java",
                        "lang", "Object" });
                final ExternalMethodInfo methodInfo = new ExternalMethodInfo(this.getName(),
                        ownerClass);
                final List<ParameterInfo> parameters = NameResolver
                        .createParameters(actualParameters, methodInfo);
                methodInfo.addParameters(parameters);
                methodInfoManager.add(methodInfo);

                // 外部クラスに新規で外部メソッドを追加したので型は不明
                this.resolvedInfo = new MethodCallInfo(ownerType, methodInfo, fromLine, fromColumn,
                        toLine, toColumn);
                ((MethodCallInfo) this.resolvedInfo).addParameters(actualParameters);
                return this.resolvedInfo;
            }

            // 親がプリミティブ型だった場合
        } else if (ownerType instanceof PrimitiveTypeInfo) {

            switch (Settings.getLanguage()) {
            // Java の場合はオートボクシングでのメソッド呼び出しが可能
            // TODO 将来的にはこの switch文はとる．なぜなら TypeConverter.getTypeConverter(LANGUAGE)があるから．
            case JAVA:
                final ExternalClassInfo wrapperClass = TypeConverter.getTypeConverter(
                        Settings.getLanguage()).getWrapperClass((PrimitiveTypeInfo) ownerType);
                final ExternalMethodInfo methodInfo = new ExternalMethodInfo(this.getName(),
                        wrapperClass);
                final List<ParameterInfo> parameters = NameResolver
                        .createParameters(actualParameters, methodInfo);
                methodInfo.addParameters(parameters);
                methodInfoManager.add(methodInfo);

                // 外部クラスに新規で外部メソッド(ExternalMethodInfo)を追加したので型は不明．
                this.resolvedInfo = new MethodCallInfo(ownerType, methodInfo, fromLine, fromColumn,
                        toLine, toColumn);
                ((MethodCallInfo) this.resolvedInfo).addParameters(actualParameters);
                return this.resolvedInfo;

            default:
                assert false : "Here shouldn't be reached!";
                final ExternalMethodInfo unknownMethod = new ExternalMethodInfo(name);
                this.resolvedInfo = new MethodCallInfo(ownerType, unknownMethod, fromLine,
                        fromColumn, toLine, toColumn);
                return this.resolvedInfo;
            }
        }

        assert false : "Here shouldn't be reached!";
        final ExternalMethodInfo unknownMethod = new ExternalMethodInfo(name);
        this.resolvedInfo = new MethodCallInfo(ownerType, unknownMethod, fromLine, fromColumn,
                toLine, toColumn);
        return this.resolvedInfo;
    }

    /**
     * メソッド呼び出しが実行される変数の型を返す
     * 
     * @return メソッド呼び出しが実行される変数の型
     */
    public UnresolvedEntityUsageInfo getOwnerClassType() {
        return this.ownerUsage;
    }

    /**
     * メソッド名を返す
     * 
     * @return メソッド名
     */
    public final String getName() {
        return this.methodName;
    }

    /**
     * メソッド名を保存するための変数
     */
    protected String methodName;

    /**
     * メソッド呼び出しが実行される変数の参照を保存するための変数
     */
    private final UnresolvedEntityUsageInfo ownerUsage;

}
