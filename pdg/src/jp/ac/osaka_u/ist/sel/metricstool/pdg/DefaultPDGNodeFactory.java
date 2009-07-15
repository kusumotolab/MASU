package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CaseEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReturnStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGCaseEntryNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGControlNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGExpressionNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNormalNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGParameterNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGReturnStatementNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGStatementNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGVariableDeclarationStatementNode;


public class DefaultPDGNodeFactory implements IPDGNodeFactory {

    private final Map<Object, PDGNode<?>> elementToNodeMap;

    public DefaultPDGNodeFactory() {
        this.elementToNodeMap = new HashMap<Object, PDGNode<?>>();
    }

    @Override
    public PDGControlNode makeControlNode(final ConditionInfo condition) {

        if (null == condition) {
            throw new IllegalArgumentException();
        }

        PDGControlNode node = (PDGControlNode) this.getNode(condition);
        if (null != node) {
            return node;
        }

        node = new PDGControlNode(condition);
        this.elementToNodeMap.put(condition, node);

        return node;
    }

    @Override
    public PDGNormalNode<?> makeNormalNode(final Object element) {

        if (null == element) {
            throw new IllegalArgumentException();
        }

        PDGNormalNode<?> node = (PDGNormalNode<?>) this.getNode(element);
        if (null != node) {
            return node;
        }

        if (element instanceof ReturnStatementInfo) {
            node = new PDGReturnStatementNode((ReturnStatementInfo) element);
        } else if (element instanceof VariableDeclarationStatementInfo) {
            node = new PDGVariableDeclarationStatementNode(
                    (VariableDeclarationStatementInfo) element);
        } else if (element instanceof SingleStatementInfo) {
            node = new PDGStatementNode((SingleStatementInfo) element);
        } else if (element instanceof ParameterInfo) {
            node = new PDGParameterNode((ParameterInfo) element);
        } else if (element instanceof ConditionInfo) {
            node = new PDGExpressionNode((ConditionInfo) element);
        } else if (element instanceof CaseEntryInfo) {
            node = new PDGCaseEntryNode((CaseEntryInfo) element);
        } else {
            return null;
        }
        this.elementToNodeMap.put(element, node);

        return node;
    }

    public PDGNode<?> getNode(final Object element) {

        if (null == element) {
            throw new IllegalArgumentException();
        }

        return this.elementToNodeMap.get(element);
    }

    @Override
    public SortedSet<PDGNode<?>> getAllNodes() {
        final SortedSet<PDGNode<?>> nodes = new TreeSet<PDGNode<?>>();
        nodes.addAll(this.elementToNodeMap.values());
        return nodes;
    }

    public int getNodeCount() {
        return this.elementToNodeMap.size();
    }
}
