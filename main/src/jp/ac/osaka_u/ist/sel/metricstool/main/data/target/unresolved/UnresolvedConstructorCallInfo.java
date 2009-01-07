package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決コンストラクタ呼び出しを保存するためのクラス
 * 
 * @author t-miyake, higo
 *
 */
public class UnresolvedConstructorCallInfo extends UnresolvedCallInfo<ConstructorCallInfo> {

    /**
     * コンストラクタ呼び出しが実行される参照型を与えてオブジェクトを初期化
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

    /**
     * コンストラクタ呼び出しが実行される参照型を与えて初期化
     * @param unresolvedReferenceType コンストラクタ呼び出しが実行される型
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public UnresolvedConstructorCallInfo(
            final UnresolvedReferenceTypeInfo<?> unresolvedReferenceType, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        this(unresolvedReferenceType);

        this.setFromLine(fromLine);
        this.setFromColumn(fromColumn);
        this.setToLine(toLine);
        this.setToColumn(toColumn);
    }

    /**
     * 名前解決を行う
     */
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
        final UnresolvedTypeInfo<?> unresolvedReferenceType = this.getReferenceType();
        final TypeInfo referenceType = unresolvedReferenceType.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        assert referenceType instanceof ClassTypeInfo : "Illegal type was found";

        final List<TargetConstructorInfo> constructors = NameResolver
                .getAvailableConstructors((ClassTypeInfo) referenceType);

        for (final ConstructorInfo constructor : constructors) {

            if (constructor.canCalledWith(actualParameters)) {
                this.resolvedInfo = new ConstructorCallInfo((ReferenceTypeInfo) referenceType,
                        constructor, usingMethod, fromLine, fromColumn, toLine, toColumn);
                this.resolvedInfo.addArguments(actualParameters);
                this.resolvedInfo.addTypeArguments(typeArguments);
                return this.resolvedInfo;
            }
        }

        // 対象クラスに定義されたコンストラクタで該当するものがないので，外部クラスに定義されたコンストラクタを呼び出していることにする
        {
            ClassInfo classInfo = ((ClassTypeInfo) referenceType).getReferencedClass();
            if (classInfo instanceof TargetClassInfo) {
                classInfo = NameResolver.getExternalSuperClass((TargetClassInfo) classInfo);
            }
            final ExternalConstructorInfo constructor = new ExternalConstructorInfo(classInfo);
            this.resolvedInfo = new ConstructorCallInfo((ReferenceTypeInfo) referenceType,
                    constructor, usingMethod, fromLine, fromColumn, toLine, toColumn);
            this.resolvedInfo.addArguments(actualParameters);
            this.resolvedInfo.addTypeArguments(typeArguments);
            return this.resolvedInfo;
        }
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
