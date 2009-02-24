package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;


public class DefaultPDGNodeFactory implements IPDGNodeFactory {

    private final Map<Object, PDGNode<?>> elementToNodeMap;

    public DefaultPDGNodeFactory() {
        this.elementToNodeMap = new HashMap<Object, PDGNode<?>>();
    }

    @Override
    public PDGNode<?> makeNode(final Object element) {

        PDGNode<?> node = this.getNode(element);

        if (null != node) {
            return node;
        }

        if (element instanceof SingleStatementInfo) {
            node = new PDGStatementNode((SingleStatementInfo) element);
        } else if (element instanceof ConditionalBlockInfo) {
            node = new PDGControlNode((ConditionalBlockInfo) element);
        } else if (element instanceof ParameterInfo) {
            node = new PDGParameterNode((ParameterInfo) element);
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
    public Collection<PDGNode<?>> getAllNodes() {
        return Collections.unmodifiableCollection(this.elementToNodeMap.values());
    }

    public int getNodeCount() {
        return this.elementToNodeMap.size();
    }
}
