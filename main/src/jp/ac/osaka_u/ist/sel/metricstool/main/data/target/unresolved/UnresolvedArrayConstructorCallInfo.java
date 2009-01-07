package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EmptyExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決配列コンストラクタ呼び出しを表すクラス
 * 
 * @author higo
 *
 */
public class UnresolvedArrayConstructorCallInfo extends
        UnresolvedConstructorCallInfo<UnresolvedArrayTypeInfo, ArrayConstructorCallInfo> {

    /**
     * 配列コンストラクタ呼び出しが実行される参照型を与えてオブジェクトを初期化
     * 
     * @param unresolvedArrayType コンストラクタ呼び出しが実行される型
     */
    public UnresolvedArrayConstructorCallInfo(final UnresolvedArrayTypeInfo unresolvedArrayType) {

        super(unresolvedArrayType);

        this.indexExpression = null;
    }

    /**
     * 配列コンストラクタ呼び出しが実行される型と位置情報を与えて初期化
     * @param unresolvedArrayType コンストラクタ呼び出しが実行される型
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public UnresolvedArrayConstructorCallInfo(final UnresolvedArrayTypeInfo unresolvedArrayType,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        this(unresolvedArrayType);
        this.setFromLine(fromLine);
        this.setFromColumn(fromColumn);
        this.setToLine(toLine);
        this.setToColumn(toColumn);
    }

    /**
     * 名前解決を行う
     */
    @Override
    public ArrayConstructorCallInfo resolve(final TargetClassInfo usingClass,
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
        final UnresolvedTypeInfo<?> unresolvedArrayType = this.getReferenceType();
        final TypeInfo arrayType = unresolvedArrayType.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        assert arrayType instanceof ArrayTypeInfo : "Illegal Type was found";

        // インデックスの式を解決
        final UnresolvedExpressionInfo<?> unresolvedIndexExpression = this.getIndexExpression();
        final ExpressionInfo indexExpression = null != unresolvedIndexExpression ? unresolvedIndexExpression
                .resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                        methodInfoManager)
                : new EmptyExpressionInfo(usingMethod, fromLine, fromColumn + 1, fromLine,
                        fromColumn + 1);

        this.resolvedInfo = new ArrayConstructorCallInfo((ArrayTypeInfo) arrayType,
                indexExpression, usingMethod, fromLine, fromColumn, toLine, toColumn);
        this.resolvedInfo.addArguments(actualParameters);
        this.resolvedInfo.addTypeArguments(typeArguments);
        return this.resolvedInfo;
    }

    /**
     * インデックスの式をセット
     * 
     * @param indexExpression
     */
    public void setIndexExpression(
            final UnresolvedExpressionInfo<? extends ExpressionInfo> indexExpression) {
        this.indexExpression = indexExpression;
    }

    /**
     * インデックスの式を取得
     * 
     * @return インデックスの式
     */
    public UnresolvedExpressionInfo<? extends ExpressionInfo> getIndexExpression() {
        return this.indexExpression;
    }

    /**
     * インデックスの式を保存するための変数
     */
    private UnresolvedExpressionInfo<? extends ExpressionInfo> indexExpression;
}
