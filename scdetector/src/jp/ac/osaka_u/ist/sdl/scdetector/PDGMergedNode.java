package jp.ac.osaka_u.ist.sdl.scdetector;


import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.PDG;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGControlNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNormalNode;


public class PDGMergedNode<T extends ExecutableElementInfo> extends PDGNormalNode<T> {

    public static void merge(final PDG pdg) {

        for (final PDGNode<?> node : pdg.getAllNodes()) {

            // 制御ノードの次がノーマルノードであれば圧縮可能かどうかを調べる
            if (node instanceof PDGControlNode) {
                for (final PDGEdge edge : node.getForwardEdges()) {
                    final PDGNode<?> toNode = edge.getToNode();
                    if (toNode instanceof PDGNormalNode<?>) {

                    }
                }
            }
        }

        // エンターノードの次がノーマルノードである場合も圧縮可能かどうかを調べる
        final PDGNode<?> enterNode = pdg.getEnterNodes().first();
        for (final PDGEdge edge : enterNode.getForwardEdges()) {
            final PDGNode<?> toNode = edge.getToNode();
            if (toNode instanceof PDGNormalNode<?>) {

            }
        }
    }

    private static void merge(final PDGNormalNode<?> node) {

        final int hash = Conversion.getNormalizedString(node.getCore()).hashCode();
        final PDGMergedNode<?> mergedNode = new PDGMergedNode();
        mergedNode.addNode(node);

        PDGNode<?> toNode = node.getForwardEdges().first().getToNode();
        if (!(toNode instanceof PDGNormalNode<?>)) { // このtoNodeがノーマルでない場合は何もしなくていい
            return;
        }
        int toHash = Conversion.getNormalizedString(toNode.getCore()).hashCode();

        while (hash == toHash) {
            mergedNode.addNode((PDGNormalNode<?>) toNode);
            toNode = node.getForwardEdges().first().getToNode();
            if(!(toNode instanceof PDGNormalNode<?>)){
                
            }
            toHash = Conversion.getNormalizedString(toNode.getCore()).hashCode();
        }
    }

    public PDGMergedNode() {
        this.originalNodes = new LinkedList<PDGNormalNode<?>>();
    }

    @Override
    public SortedSet<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SortedSet<VariableInfo<? extends UnitInfo>> getReferencedVariables() {
        // TODO Auto-generated method stub
        return null;
    }

    public void addNode(final PDGNormalNode<?> node) {
        this.originalNodes.add(node);
    }

    private final List<PDGNormalNode<?>> originalNodes;
}
