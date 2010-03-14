package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGControlNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNormalNode;


public interface IPDGNodeFactory {

    PDGControlNode makeControlNode(ConditionInfo condition);

    PDGNormalNode<?> makeNormalNode(Object element);

    PDGNode<?> getNode(Object element);

    SortedSet<PDGNode<?>> getAllNodes();

    void removeNode(Object element);

    void addNode(PDGNode<?> node);
}
