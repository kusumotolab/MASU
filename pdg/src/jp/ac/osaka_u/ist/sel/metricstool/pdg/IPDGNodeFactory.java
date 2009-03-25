package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import java.util.Collection;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;


public interface IPDGNodeFactory {

    PDGControlNode makeControlNode(ConditionInfo condition);

    PDGNormalNode<?> makeNormalNode(Object element);

    PDGNode<?> getNode(Object element);

    Collection<PDGNode<?>> getAllNodes();
}
