package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * ローカル変数の使用を表すクラス
 * 
 * @author higo
 * 
 */
public final class LocalVariableUsageInfo extends VariableUsageInfo<LocalVariableInfo> {

    /**
     * 使用されているローカル変数を与えてオブジェクトを初期化
     * 
     * @param usedLocalVariable 使用されているローカル変数
     * @param reference 参照である場合は true, 代入である場合は false
     */
    public LocalVariableUsageInfo(final LocalVariableInfo usedLocalVariable,
            final boolean reference, final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {

        super(usedLocalVariable, reference, fromLine, fromColumn, toLine, toColumn);
    }

    @Override
    public TypeInfo getType() {
        final LocalVariableInfo usedVariable = this.getUsedVariable();
        final TypeInfo usedVariableType = usedVariable.getType();
        return usedVariableType;
    }
}
