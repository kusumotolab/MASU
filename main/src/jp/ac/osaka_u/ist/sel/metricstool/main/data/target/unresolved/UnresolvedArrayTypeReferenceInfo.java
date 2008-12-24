package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決配列型参照を表すクラス
 * 
 * @author t-miyake, higo
 *
 */
public final class UnresolvedArrayTypeReferenceInfo extends
        UnresolvedEntityUsageInfo<ArrayTypeReferenceInfo> {

    /**
     * 参照されている未解決配列型を与えて初期化
     * 
     * @param referencedType 参照されている未解決配列型
     */
    public UnresolvedArrayTypeReferenceInfo(final UnresolvedArrayTypeInfo referencedType) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == referencedType) {
            throw new IllegalArgumentException("referencedType is null");
        }

        this.referencedType = referencedType;
        this.resolvedInfo = null;
    }

    @Override
    public ArrayTypeReferenceInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == classInfoManager)) {
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

        // 参照されている配列型を解決
        final UnresolvedArrayTypeInfo unresolvedArrayType = this.getType();
        final ArrayTypeInfo arrayType = unresolvedArrayType.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);

        // 要素使用のオーナー要素を返す
        final UnresolvedExecutableElementInfo<?> unresolvedOwnerExecutableElement = this
                .getOwnerExecutableElement();
        final ExecutableElementInfo ownerExecutableElement = unresolvedOwnerExecutableElement
                .resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                        methodInfoManager);

        this.resolvedInfo = new ArrayTypeReferenceInfo(arrayType, fromLine, fromColumn, toLine,
                toColumn);
        this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);

        return this.resolvedInfo;
    }

    /**
     * 参照されている未解決配列型を返す
     * @return 参照されている未解決配列型
     */
    public UnresolvedArrayTypeInfo getType() {
        return this.referencedType;
    }

    /**
     * 参照されている未解決配列型を保存するための変数
     */
    private final UnresolvedArrayTypeInfo referencedType;

}
