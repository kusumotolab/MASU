package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.HashSet;
import java.util.Set;


/**
 * 空の式を表すクラス
 * return ; や for ( ; ; ) などに用いる
 * 
 * @author higo
 *
 */
public final class EmptyExpressionInfo extends ExpressionInfo {

    /**
     * @param ownerMethod オーナーメソッド
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public EmptyExpressionInfo(final CallableUnitInfo ownerMethod, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * void 型を返す
     * 
     * return void型 
     */
    @Override
    public TypeInfo getType() {
        return VoidTypeInfo.getInstance();
    }

    /**
     * 長さ0のStringを返す
     * 
     * return 長さ0のString
     */
    @Override
    public String getText() {
        return "";
    }

    /**
     * 使用されている変数のSetを返す．
     * 実際はなにも使用されていないので，空のSetが返される．
     * 
     * @return 使用されている変数のSet
     */
    @Override
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        return new HashSet<VariableUsageInfo<?>>();
    }

}
