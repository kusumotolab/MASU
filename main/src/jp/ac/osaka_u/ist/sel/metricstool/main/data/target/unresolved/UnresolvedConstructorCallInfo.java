package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決コンストラクタ呼び出しを保存するためのクラス
 * 
 * @author t-miyake, higo
 *
 */
public final class UnresolvedConstructorCallInfo extends UnresolvedCallInfo<ConstructorCallInfo> {

    /**
     * コンストラクタ呼び出しが実行される参照型と名前を与えてオブジェクトを初期化
     * 
     * @param unresolvedReferenceType コンストラクタ呼び出しが実行される型
     */
    public UnresolvedConstructorCallInfo(
            final UnresolvedReferenceTypeInfo<?> unresolvedReferenceType) {

        if (null == unresolvedReferenceType) {
            throw new IllegalArgumentException();
        }

        this.unresolvedReferenceType = unresolvedReferenceType;
    }

    @Override
    public ConstructorCallInfo resolve(final TargetClassInfo usingClass,
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
            return this.getResolved();
        }

        //　位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // コンストラクタのシグネチャを取得
        final List<ExpressionInfo> actualParameters = super.resolveArguments(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        final List<ReferenceTypeInfo> typeArguments = super.resolveTypeArguments(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        //　コンストラクタの型を解決
        final TypeInfo referenceType = this.getReferenceType().resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        if (!(referenceType instanceof ReferenceTypeInfo)) {
            assert false : "Error handling must be inserted!";
        }

        // 要素使用のオーナー要素を返す
        final UnresolvedExecutableElementInfo<?> unresolvedOwnerExecutableElement = this
                .getOwnerExecutableElement();
        final ExecutableElementInfo ownerExecutableElement = unresolvedOwnerExecutableElement
                .resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                        methodInfoManager);

        this.resolvedInfo = new ConstructorCallInfo(ownerExecutableElement,
                (ReferenceTypeInfo) referenceType, fromLine, fromColumn, toLine, toColumn);
        this.resolvedInfo.addArguments(actualParameters);
        this.resolvedInfo.addTypeArguments(typeArguments);
        return this.resolvedInfo;
    }

    /**
     * この未解決コンストラクタ呼び出しの型を返す
     * 
     * @return この未解決コンストラクタ呼び出しの型
     */
    public UnresolvedReferenceTypeInfo<?> getReferenceType() {
        return this.unresolvedReferenceType;
    }

    private final UnresolvedReferenceTypeInfo<?> unresolvedReferenceType;

}
