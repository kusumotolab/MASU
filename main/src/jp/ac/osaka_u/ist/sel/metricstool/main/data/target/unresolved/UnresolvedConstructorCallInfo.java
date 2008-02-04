package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
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

        
        //　位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // コンストラクタのシグネチャを取得
        final List<EntityUsageInfo> actualParameters = super.resolveParameters(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        //　コンストラクタの型を解決
        final TypeInfo referenceType = this.getReferenceType().resolveType(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        if (!(referenceType instanceof ReferenceTypeInfo)) {
            assert false : "Error handling must be inserted!";
        }

        // クラス型のコンストラクタ呼び出しの場合
        if (referenceType instanceof ClassTypeInfo) {

            final ClassInfo referencedClass = ((ClassTypeInfo) referenceType).getReferencedClass();
            if (referencedClass instanceof TargetClassInfo) {

                // まずは利用可能なメソッドから検索
                {
                    // 利用可能なメソッド一覧を取得
                    final List<TargetMethodInfo> availableMethods = NameResolver
                            .getAvailableMethods((TargetClassInfo) referencedClass, usingClass);

                    // 利用可能なメソッド(コンストラクタ)から，未解決メソッドと一致するものを検索
                    // 引数の型のリストを用いて，このコンストラクタメソッドの呼び出しであるかどうかを判定
                    for (final TargetMethodInfo availableMethod : availableMethods) {

                        // 呼び出し可能なメソッドが見つかった場合
                        if (availableMethod.canCalledWith(actualParameters)) {
                            this.resolvedInfo = new ConstructorCallInfo(availableMethod, fromLine, fromColumn, toLine, toColumn);
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

                        final ExternalMethodInfo methodInfo = new ExternalMethodInfo(
                                externalSuperClass.getClassName(), externalSuperClass, true);
                        final List<ParameterInfo> dummyParameters = NameResolver
                                .createParameters(actualParameters);
                        methodInfo.addParameters(dummyParameters);
                        methodInfoManager.add(methodInfo);

                        // 外部クラスに新規で外部メソッド変数（ExternalMethodInfo）を追加したので型は不明
                        this.resolvedInfo = new ConstructorCallInfo(methodInfo, fromLine, fromColumn, toLine, toColumn);
                        return this.resolvedInfo;
                    }

                    assert false : "Here shouldn't be reached!";
                }

                // 見つからなかった処理を行う
                {
                    err.println("Can't resolve method Call : " + this.toString());

                    usingMethod.addUnresolvedUsage(this);

                    this.resolvedInfo = new UnknownEntityUsageInfo(fromLine, fromColumn, toLine, toColumn);
                    return this.resolvedInfo;
                }

            } else if (referencedClass instanceof ExternalClassInfo) {

                final ExternalMethodInfo methodInfo = new ExternalMethodInfo(referencedClass
                        .getClassName(), referencedClass, true);
                final List<ParameterInfo> parameters = NameResolver
                        .createParameters(actualParameters);
                methodInfo.addParameters(parameters);
                methodInfoManager.add(methodInfo);

                // 外部クラスに新規で外部メソッド(ExternalMethodInfo)を追加したので型は不明．
                this.resolvedInfo = new ConstructorCallInfo(methodInfo, fromLine, fromColumn, toLine, toColumn);
                return this.resolvedInfo;
            }

            //　配列型のコンストラクタ呼び出しの場合
        } else if (referenceType instanceof ArrayTypeInfo) {
            // TODO 配列のコンストラクタを表すクラスが必要
        }

        // TODO 
        return null;
    }

    public UnresolvedReferenceTypeInfo getReferenceType() {
        return this.unresolvedReferenceType;
    }

    private final UnresolvedReferenceTypeInfo unresolvedReferenceType;

}
