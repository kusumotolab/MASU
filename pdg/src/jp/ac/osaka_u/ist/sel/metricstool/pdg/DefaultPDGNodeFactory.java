package jp.ac.osaka_u.ist.sel.metricstool.pdg;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;

public class DefaultPDGNodeFactory implements IPDGNodeFactory {

    private final Map<StatementInfo, PDGNode<?>> statementToNodeMap;
    
    public DefaultPDGNodeFactory() {
        this.statementToNodeMap = new HashMap<StatementInfo, PDGNode<?>>();
    }
    
    @Override
    public PDGNode<?> makeNode(StatementInfo statement) {
        PDGNode<?> node = this.getNode(statement);
        
        if(null != node) {
            return node;
        }
                
        if(statement instanceof SingleStatementInfo) {
            node = new PDGStatementNode((SingleStatementInfo) statement);
            this.statementToNodeMap.put(statement, node);
        } else if(statement instanceof ConditionalBlockInfo) {
            node = new PDGControlNode((ConditionalBlockInfo) statement);
            this.statementToNodeMap.put(statement, node);
        } else {
            node = null;
        }
        
        return node;
    }
    
    public PDGNode<?> getNode(final StatementInfo statement) {
        return this.statementToNodeMap.get(statement);
    }
    
    @Override
    public Collection<PDGNode<?>> getAllNode() {
        return Collections.unmodifiableCollection(this.statementToNodeMap.values());
    }
    
    public int getNodeCount() {
        return this.statementToNodeMap.size();
    }
}
