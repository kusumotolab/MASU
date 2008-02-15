package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.HashSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決完全限定名クラス参照を表すクラス
 * 
 * @author higo, t-miyake
 *
 */
public final class UnresolvedFullQualifiedNameClassReferenceInfo extends
        UnresolvedClassReferenceInfo {

    /**
     * 完全限定名がわかっている（UnresolvedClassInfoのオブジェクトが存在する）クラスの参照を初期化
     * 
     * @param referencedClass 参照されているクラス
     */
    public UnresolvedFullQualifiedNameClassReferenceInfo(final UnresolvedClassInfo referencedClass) {
        super(new HashSet<AvailableNamespaceInfo>(), referencedClass.getFullQualifiedName());
        this.referencedClass = referencedClass;
    }

    @Override
    public EntityUsageInfo resolveEntityUsage(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == classInfoManager)) {
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

        final String[] fullQualifiedReferenceName = this.getReferenceName();
        ClassInfo referencedClass = classInfoManager.getClassInfo(fullQualifiedReferenceName);

        // 参照されたクラスが登録されていない場合は，ここで登録する
        if (null == referencedClass) {
            referencedClass = new ExternalClassInfo(fullQualifiedReferenceName);
            classInfoManager.add((ExternalClassInfo) referencedClass);
        }

        final ClassTypeInfo referenceType = new ClassTypeInfo(referencedClass);
        for (final UnresolvedTypeInfo unresolvedTypeArgument : this.getTypeArguments()) {
            final TypeInfo typeArgument = unresolvedTypeArgument.resolveType(usingClass,
                    usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
            referenceType.addTypeArgument(typeArgument);
        }
        this.resolvedInfo = new ClassReferenceInfo(referenceType, fromLine, fromColumn, toLine,
                toColumn);
        return this.resolvedInfo;
    }

    /**
     * 参照されているクラスの情報を返す
     * 
     * @return 参照されているクラスの情報
     */
    public UnresolvedClassInfo getReferencedClass() {
        return this.referencedClass;
    }

    /**
     * 参照されているクラスを保存するための変数
     */
    private final UnresolvedClassInfo referencedClass;

}
