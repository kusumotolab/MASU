package jp.ac.osaka_u.ist.sdl.scdetector;


import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentMap;

import jp.ac.osaka_u.ist.sdl.scdetector.data.ClonePairInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.Configuration;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.SLICE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CaseEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.DoBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForeachBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.IfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LabelInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SwitchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SynchronizedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.WhileBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;


public class DetectionThread implements Runnable {

    private final List<PDGNode<?>> nodeList;

    private final ConcurrentMap<TwoClassHash, SortedSet<ClonePairInfo>> clonePairs;

    DetectionThread(final List<PDGNode<?>> nodeList,
            final ConcurrentMap<TwoClassHash, SortedSet<ClonePairInfo>> clonePairs2) {
        this.nodeList = nodeList;
        this.clonePairs = clonePairs2;
    }

    @Override
    public void run() {
        for (int i = 0; i < this.nodeList.size(); i++) {
            J: for (int j = i + 1; j < this.nodeList.size(); j++) {

                final PDGNode<?> nodeA = this.nodeList.get(i);
                final PDGNode<?> nodeB = this.nodeList.get(j);
                final ExecutableElementInfo elementA = nodeA.getCore();
                final ExecutableElementInfo elementB = nodeB.getCore();
                final ClonePairInfo clonePair = new ClonePairInfo(elementA, elementB);

                final HashSet<PDGNode<?>> checkedNodesA = new HashSet<PDGNode<?>>();
                final HashSet<PDGNode<?>> checkedNodesB = new HashSet<PDGNode<?>>();
                checkedNodesA.add(nodeA);
                checkedNodesB.add(nodeB);

                increaseNumberOfPairs();

                if (Configuration.INSTANCE.getT().contains(SLICE_TYPE.BACKWARD)) {
                    ProgramSlice.addDuplicatedElementsWithBackwordSlice(nodeA, nodeB, clonePair,
                            checkedNodesA, checkedNodesB);
                }
                if (Configuration.INSTANCE.getT().contains(SLICE_TYPE.FORWARD)) {
                    ProgramSlice.addDuplicatedElementsWithForwordSlice(nodeA, nodeB, clonePair,
                            checkedNodesA, checkedNodesB);
                }

                if (Configuration.INSTANCE.getS() <= clonePair.length()) {
                    SortedSet<ClonePairInfo> pairs = this.clonePairs
                            .get(new TwoClassHash(clonePair));
                    if (null == pairs) {
                        pairs = Collections
                                .<ClonePairInfo> synchronizedSortedSet(new TreeSet<ClonePairInfo>());
                        this.clonePairs.put(new TwoClassHash(clonePair), pairs);
                    }
                    pairs.add(clonePair);
                }
            }
        }
    }

    static private SortedSet<ExecutableElementInfo> getAllElements(final LocalSpaceInfo localSpace) {

        final SortedSet<ExecutableElementInfo> elements = new TreeSet<ExecutableElementInfo>();
        for (final StatementInfo statement : localSpace.getStatements()) {

            if (statement instanceof SingleStatementInfo || statement instanceof CaseEntryInfo
                    || statement instanceof LabelInfo) {
                elements.add(statement);
            }

            else if (statement instanceof BlockInfo) {
                elements.addAll(getAllElements((BlockInfo) statement));

                if (statement instanceof DoBlockInfo) {
                    elements.add(((DoBlockInfo) statement).getConditionalClause().getCondition());
                }

                if (statement instanceof IfBlockInfo) {
                    elements.add(((IfBlockInfo) statement).getConditionalClause().getCondition());
                }

                if (statement instanceof ForBlockInfo) {
                    elements.add(((ForBlockInfo) statement).getConditionalClause().getCondition());
                    elements.addAll(((ForBlockInfo) statement).getInitializerExpressions());
                    elements.addAll(((ForBlockInfo) statement).getIteratorExpressions());
                }

                if (statement instanceof ForeachBlockInfo) {
                    elements.add(((ForeachBlockInfo) statement).getConditionalClause()
                            .getCondition());
                }

                if (statement instanceof SwitchBlockInfo) {
                    elements.add(((SwitchBlockInfo) statement).getConditionalClause()
                            .getCondition());
                }

                if (statement instanceof WhileBlockInfo) {
                    elements
                            .add(((WhileBlockInfo) statement).getConditionalClause().getCondition());
                }

                if (statement instanceof SynchronizedBlockInfo) {
                    elements.add(((SynchronizedBlockInfo) statement).getSynchronizedExpression());
                }
            }
        }

        return elements;
    }

    synchronized static void increaseNumberOfPairs() {
        numberOfPairs++;
    }

    synchronized static void increaseNumberOfComparison() {
        numberOfComparion++;
    }

    static int numberOfPairs;

    static long numberOfComparion;
}
