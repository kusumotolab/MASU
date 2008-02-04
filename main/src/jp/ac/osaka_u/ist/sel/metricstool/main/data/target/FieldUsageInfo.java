package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * フィールドの使用を表すクラス
 * 
 * @author higo
 * 
 */
public final class FieldUsageInfo extends VariableUsageInfo<FieldInfo> {

    /**
     * 使用されているフィールドを与えてオブジェクトを初期化
     * 
     * @param usedField 使用されているフィールド
     * @param reference 参照である場合は true, 代入である場合は false
     */
    public FieldUsageInfo(final FieldInfo usedField, final boolean reference, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(usedField, reference, fromLine, fromColumn, toLine, toColumn);
    }
}
