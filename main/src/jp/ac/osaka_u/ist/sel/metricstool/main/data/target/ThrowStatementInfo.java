package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * throw文の情報を保有するクラス
 * 
 * @author t-miyake
 *
 */
public class ThrowStatementInfo extends SingleStatementInfo {

    /**
     * throw文によって投げられる例外を表す式と位置情報を与えて初期化
     * 
     * @param thrownEpression throw文によって投げられる例外を表す式
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public ThrowStatementInfo(ExpressionInfo thrownEpression, int fromLine, int fromColumn,
            int toLine, int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);
        
        if(null == thrownEpression) {
            throw new IllegalArgumentException("thrownExpression is null");
        }
        this.thrownEpression = thrownEpression;
    }

    /**
     * throw文によって投げられる例外を表す式を返す
     * 
     * @return throw文によって投げられる例外を表す式
     */
    public final ExpressionInfo getThrownExpression() {
        return this.thrownEpression;
    }
    
    /**
     * throw文によって投げられる例外を表す式
     */
    private final ExpressionInfo thrownEpression;

}
