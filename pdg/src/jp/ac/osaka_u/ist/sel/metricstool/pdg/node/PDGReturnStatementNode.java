package jp.ac.osaka_u.ist.sel.metricstool.pdg.node;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReturnStatementInfo;


/**
 * Return•¶‚ğ•\‚·PDGƒm[ƒh
 * 
 * @author higo
 *
 */
public class PDGReturnStatementNode extends PDGStatementNode {

    /**
     * ƒm[ƒh‚ğ¶¬‚·‚éReturn•¶‚ğ—^‚¦‚Ä‰Šú‰»
     * 
     * @param returnStatement
     */
    public PDGReturnStatementNode(final ReturnStatementInfo returnStatement) {
        super(returnStatement);
    }
}
