package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;


final class CFGEmptyNode extends CFGNode<StatementInfo> {

    CFGEmptyNode(final StatementInfo statement) {
        super(statement);
    }

    @Override
    public void addForwardNode(final CFGNode<? extends StatementInfo> forwardNode) {
        for(final CFGNode<? extends StatementInfo> backward : this.getBackwardNodes()) {
            backward.addForwardNode(forwardNode);
        }
    }



}
