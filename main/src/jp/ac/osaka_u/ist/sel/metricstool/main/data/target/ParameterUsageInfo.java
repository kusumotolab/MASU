package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * 引数の使用を表すクラス
 * 
 * @author higo
 * 
 */
public final class ParameterUsageInfo extends VariableUsageInfo<ParameterInfo> {

    /**
     * 使用されている引数を与えてオブジェクトを初期化
     * 
     * @param usedParameter 使用されている引数
     * @param reference 参照である場合は true, 代入である場合は false
     */
    public ParameterUsageInfo(final ParameterInfo usedParameter, final boolean reference,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(usedParameter, reference, fromLine, fromColumn, toLine, toColumn);
    }

    @Override
    public TypeInfo getType() {
        final ParameterInfo parameter = this.getUsedVariable();
        final TypeInfo usedVariableType = parameter.getType();
        return usedVariableType;
    }
}
