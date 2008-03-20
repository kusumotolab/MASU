package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import java.util.Set;

/**
 * 式文の情報を表すクラス
 * 
 * @author t-miyake
 *
 */
public class ExpressionStatementInfo extends SingleStatementInfo {

    /**
     * 式と位置情報を与えて初期化
     * 
     * @param expression 式文を構成する式
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public ExpressionStatementInfo(final ExpressionInfo expression, final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);

        if(null == expression) {
            throw new IllegalArgumentException("expression is null");
        }
        
        this.expression = expression;
    }

    
    /**
     * 式文を構成する式を返す
     * 
     * @return 式文を構成する式
     */
    public final ExpressionInfo getExpression() {
        return this.expression;
    }
    
    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo#getVariableUsages()
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        return this.getExpression().getVariableUsages();
    }
    
    /**
     * 式文を構成する式を保存するための変数
     */
    private final ExpressionInfo expression;

}
