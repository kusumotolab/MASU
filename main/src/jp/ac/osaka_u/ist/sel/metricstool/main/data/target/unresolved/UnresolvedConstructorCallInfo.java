package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決コンストラクタ呼び出しを保存するためのクラス
 * 
 * @author t-miyake, higo
 *
 */
public final class UnresolvedConstructorCallInfo extends UnresolvedMemberCallInfo {

    /**
     * コンストラクタ呼び出しが実行される参照型と名前を与えてオブジェクトを初期化
     * 
     * @param unresolvedReferenceType コンストラクタ呼び出しが実行される型
     */
    public UnresolvedConstructorCallInfo(final UnresolvedReferenceTypeInfo unresolvedReferenceType) {

        super();

        if (null == unresolvedReferenceType) {
            throw new IllegalArgumentException();
        }

        this.unresolvedReferenceType = unresolvedReferenceType;
    }

    @Override
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

        // コンストラクタのシグネチャを取得
        final String name = this.getName();
        final List<EntityUsageInfo> actualParameters = super.resolveParameters(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        //　コンストラクタの型を解決
        final TypeInfo classType = this.getClassType().resolveType(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        if (!(classType instanceof ClassTypeInfo)) {
            assert false : "Error handling must be inserted!";
        }

        final ClassInfo referencedClass = ((ClassTypeInfo) classType).getReferencedClass();
        if (referencedClass instanceof TargetClassInfo) {

            // まずは利用可能なメソッドから検索
            {
                // 利用可能なメソッド一覧を取得
                final List<TargetMethodInfo> availableMethods = NameResolver.getAvailableMethods(
                        (TargetClassInfo) referencedClass, usingClass);

                // 利用可能なメソッドから，未解決メソッドと一致するものを検索
                // メソッド名，引数の型のリストを用いて，このメソッドの呼び出しであるかどうかを判定
                for (final TargetMethodInfo availableMethod : availableMethods) {

                    // 呼び出し可能なメソッドが見つかった場合
                    if (availableMethod.canCalledWith(name, actualParameters)) {
                        this.resolvedInfo = new ConstructorCallInfo(availableMethod);
                        return this.resolvedInfo;
                    }
                }
            }

            // 利用可能なメソッドが見つからなかった場合は，外部クラスである親クラスがあるはず．
            // そのクラスのメソッドを使用しているとみなす
            {
                final ExternalClassInfo externalSuperClass = NameResolver
                        .getExternalSuperClass((TargetClassInfo) referencedClass);
                if (null != externalSuperClass) {

                    final ExternalMethodInfo methodInfo = new ExternalMethodInfo(this.getName(),
                            externalSuperClass, true);
                    final List<ParameterInfo> dummyParameters = NameResolver
                            .createParameters(actualParameters);
                    methodInfo.addParameters(dummyParameters);
                    methodInfoManager.add(methodInfo);

                    // 外部クラスに新規で外部メソッド変数（ExternalMethodInfo）を追加したので型は不明
                    this.resolvedInfo = new ConstructorCallInfo(methodInfo);
                    return this.resolvedInfo;
                }

                assert false : "Here shouldn't be reached!";
            }

            // 見つからなかった処理を行う
            {
                err.println("Can't resolve method Call : " + this.getName());

                usingMethod.addUnresolvedUsage(this);

                this.resolvedInfo = UnknownEntityUsageInfo.getInstance();
                return this.resolvedInfo;
            }

        } else if (referencedClass instanceof ExternalClassInfo) {

            final ExternalMethodInfo methodInfo = new ExternalMethodInfo(this.getName(),
                    referencedClass, true);
            final List<ParameterInfo> parameters = NameResolver.createParameters(actualParameters);
            methodInfo.addParameters(parameters);
            methodInfoManager.add(methodInfo);

            // 外部クラスに新規で外部メソッド(ExternalMethodInfo)を追加したので型は不明．
            this.resolvedInfo = new ConstructorCallInfo(methodInfo);
            return this.resolvedInfo;
        }

        // TODO 
        return null;
    }

    public UnresolvedReferenceTypeInfo getClassType() {
        return this.unresolvedReferenceType;
    }

    private final UnresolvedReferenceTypeInfo unresolvedReferenceType;

}
