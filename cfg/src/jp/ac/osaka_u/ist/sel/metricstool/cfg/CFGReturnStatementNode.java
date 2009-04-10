package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReturnStatementInfo;


/**
 * return•¶‚ğ•\‚·ƒm[ƒh
 * 
 * @author higo
 *
 */
public class CFGReturnStatementNode extends CFGStatementNode {

    /**
     * ¶¬‚·‚éƒm[ƒh‚É‘Î‰‚·‚éreturn•¶‚ğ—^‚¦‚Ä‰Šú‰»
     * 
     * @param returnStatement
     */
    public CFGReturnStatementNode(final ReturnStatementInfo returnStatement) {
        super(returnStatement);
    }
}
