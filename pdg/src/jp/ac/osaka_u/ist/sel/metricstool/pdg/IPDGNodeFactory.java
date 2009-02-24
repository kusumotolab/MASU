package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import java.util.Collection;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;


public interface IPDGNodeFactory {

    public PDGNode<?> makeNode(final Object element);

    public PDGNode<?> getNode(final Object element);

    public Collection<PDGNode<?>> getAllNodes();
}
