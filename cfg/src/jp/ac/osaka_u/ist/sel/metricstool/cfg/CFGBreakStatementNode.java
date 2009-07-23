package jp.ac.osaka_u.ist.sel.metricstool.cfg;

import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BreakStatementInfo;

public class CFGBreakStatementNode extends CFGJumpStatementNode {

    CFGBreakStatementNode(final BreakStatementInfo breakStatement){
        super(breakStatement);
    }
}
