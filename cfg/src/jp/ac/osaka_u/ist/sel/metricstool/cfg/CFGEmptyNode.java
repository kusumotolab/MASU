package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;


final class CFGEmptyNode extends CFGNode<ExecutableElementInfo> {

    CFGEmptyNode(final ExecutableElementInfo statement) {
        super(statement);
        this.text = statement.getText() + "<" + statement.getFromLine() + ">";
    }

    @Override
    public void addForwardNode(final CFGNode<? extends ExecutableElementInfo> forwardNode) {
        for (final CFGNode<? extends ExecutableElementInfo> backward : this.getBackwardNodes()) {
            backward.addForwardNode(forwardNode);
        }
    }
}
