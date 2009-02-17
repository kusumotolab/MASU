package jp.ac.osaka_u.ist.sel.metricstool.pdg;

import java.util.Collection;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;

public interface IPDGNodeFactory {

    public PDGNode<?> makeNode(final StatementInfo statement);
    
    public PDGNode<?> getNode(final StatementInfo statement);
    
    public Collection<PDGNode<?>> getAllNode();
}
