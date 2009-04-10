package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.JumpStatementInfo;


abstract public class CFGJumpStatementNode extends CFGStatementNode {

    CFGJumpStatementNode(final JumpStatementInfo jumpStatement) {
        super(jumpStatement);
    }
}
