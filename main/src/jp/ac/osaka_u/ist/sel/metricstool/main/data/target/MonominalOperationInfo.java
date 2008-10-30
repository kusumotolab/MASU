package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * 一項演算を保存するためのクラス
 * 
 * @author t-miyake
 *
 */
public final class MonominalOperationInfo extends EntityUsageInfo {

    /**
     * オペランド、一項演算の結果の型、位置情報を与えて初期化
     * @param operand オペランド
     * @param type 一項演算の結果の型
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public MonominalOperationInfo(final EntityUsageInfo operand, final OPERATOR operator,
            final boolean isPreposed, final PrimitiveTypeInfo type, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        if(null == operand || null == operator || null == type) {
            throw new IllegalArgumentException();
        }
        
        this.operand = operand;
        this.operator = operator;
        this.isPreposed = isPreposed;
        this.type = type;
    }

    @Override
    public TypeInfo getType() {
        return this.type;
    }

    /**
     * オペランドを返す
     * @return オペランド
     */
    public final EntityUsageInfo getOperand() {
        return this.operand;
    }

    /**
     * 一項演算の演算子を返す．
     * @return 演算子
     */
    public final OPERATOR getOperator() {
        return this.operator;
    }

    /**
     * 演算子が前置されているかどうか返す
     * @return 演算子が前置されているならtrue
     */
    public final boolean isPreposed() {
        return this.isPreposed;
    }
    
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        final SortedSet<VariableUsageInfo<?>> variableUsages = new TreeSet<VariableUsageInfo<?>>();
        variableUsages.addAll(this.getOperand().getVariableUsages());
        return variableUsages;
    }

    /**
     * オペランドを保存するための変数
     */
    private final EntityUsageInfo operand;

    /**
     * 一項演算の演算子を保存するための変数
     */
    private final OPERATOR operator;

    /**
     * 一項演算の結果の型を保存するための変数
     */
    private final PrimitiveTypeInfo type;
    
    /**
     * 演算子が前置しているかどうかを示す変数
     */
    private final boolean isPreposed;
}
