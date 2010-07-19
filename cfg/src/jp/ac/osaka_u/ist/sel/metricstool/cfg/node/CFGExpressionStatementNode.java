package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionStatementInfo;


/**
 * 
 * @author higo
 * 
 */
public class CFGExpressionStatementNode extends CFGNormalNode<ExpressionStatementInfo> {

    /**
     * ¶¬‚·‚éƒm[ƒh‚É‘Î‰‚·‚é•¶‚ğ—^‚¦‚Ä‰Šú‰»
     * 
     * @param statement
     *            ¶¬‚·‚éƒm[ƒh‚É‘Î‰‚·‚é•¶
     */
    CFGExpressionStatementNode(final ExpressionStatementInfo statement) {
        super(statement);
    }
}
