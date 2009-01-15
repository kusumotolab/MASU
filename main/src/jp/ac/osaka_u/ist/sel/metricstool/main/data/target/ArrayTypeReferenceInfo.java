package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 配列型参照を表すクラス
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class ArrayTypeReferenceInfo extends ExpressionInfo {

    /**
     * オブジェクトを初期化
     * 
     * @param arrayType 参照されている配列の型
     * @param ownerMethod オーナーメソッド
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public ArrayTypeReferenceInfo(final ArrayTypeInfo arrayType,
            final CallableUnitInfo ownerMethod, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == arrayType) {
            throw new IllegalArgumentException();
        }

        this.arrayType = arrayType;
    }

    /**
     * 型を返す
     */
    @Override
    public TypeInfo getType() {
        return this.arrayType;
    }

    /**
     * 配列の型参照において変数が使用されることはないので空のセットを返す
     * 
     * @return 空のセット
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        return VariableUsageInfo.EmptySet;
    }

    /**
     * 呼び出しのSetを返す
     * 
     * @return 呼び出しのセット
     */
    @Override
    public Set<CallInfo<?>> getCalls() {
        return CallInfo.EmptySet;
    }

    /**
     * この配列型参照のテキスト表現（String型）を返す
     * 
     * @return この配列型のテキスト表現（String型）
     */
    @Override
    public String getText() {
        final TypeInfo type = this.getType();
        return type.getTypeName();
    }

    private final ArrayTypeInfo arrayType;
}
