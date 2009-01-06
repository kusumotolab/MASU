package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * 配列コンストラクタ呼び出しを表すクラス
 * 
 * @author higo
 *
 */
public final class ArrayConstructorCallInfo extends ConstructorCallInfo {

    /**
     * 型を与えて配列コンストラクタ呼び出しを初期化
     * 
     * @param arrayType 呼び出しの型
     * @param indexExpression インデックスの式
     * @param ownerMethod オーナーメソッド 
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列 
     */
    public ArrayConstructorCallInfo(final ArrayTypeInfo arrayType,
            final ExpressionInfo indexExpression, final CallableUnitInfo ownerMethod,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(arrayType, null, ownerMethod, fromLine, fromColumn, toLine, toColumn);

        if (null == indexExpression) {
            throw new IllegalArgumentException();
        }
        this.indexExpression = indexExpression;
    }

    /**
     * インデックスの式を取得
     * 
     * @return インデックスの式 
     */
    public ExpressionInfo getIndexExpression() {
        return this.indexExpression;
    }

    private final ExpressionInfo indexExpression;
}
