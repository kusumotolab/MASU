package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public class UnresolvedClassConstructorCallInfo extends
        UnresolvedConstructorCallInfo<UnresolvedClassTypeInfo, ClassConstructorCallInfo> {

    public UnresolvedClassConstructorCallInfo(final UnresolvedClassTypeInfo classType) {
        super(classType);
    }

    public UnresolvedClassConstructorCallInfo(final UnresolvedClassTypeInfo classType,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(classType, fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * 名前解決を行う
     */
    @Override
    public ClassConstructorCallInfo resolve(final TargetClassInfo usingClass,
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
        final UnresolvedClassTypeInfo unresolvedReferenceType = this.getReferenceType();
        final ClassTypeInfo classType = unresolvedReferenceType.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);

        final List<TargetConstructorInfo> constructors = NameResolver
                .getAvailableConstructors((ClassTypeInfo) classType);

        for (final ConstructorInfo constructor : constructors) {

            if (constructor.canCalledWith(actualParameters)) {
                this.resolvedInfo = new ClassConstructorCallInfo(classType, constructor,
                        usingMethod, fromLine, fromColumn, toLine, toColumn);
                this.resolvedInfo.addArguments(actualParameters);
                this.resolvedInfo.addTypeArguments(typeArguments);
                return this.resolvedInfo;
            }
        }

        // 対象クラスに定義されたコンストラクタで該当するものがないので，外部クラスに定義されたコンストラクタを呼び出していることにする
        {
            ClassInfo classInfo = ((ClassTypeInfo) classType).getReferencedClass();
            if (classInfo instanceof TargetClassInfo) {
                final ExternalClassInfo externalSuperClass = NameResolver
                        .getExternalSuperClass((TargetClassInfo) classInfo);
            }
            final ExternalConstructorInfo constructor = new ExternalConstructorInfo(classInfo);
            this.resolvedInfo = new ClassConstructorCallInfo(classType, constructor, usingMethod,
                    fromLine, fromColumn, toLine, toColumn);
            this.resolvedInfo.addArguments(actualParameters);
            this.resolvedInfo.addTypeArguments(typeArguments);
            return this.resolvedInfo;
        }
    }
}
