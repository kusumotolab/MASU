package jp.ac.osaka_u.ist.sdl.scdetector;


import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sdl.scdetector.data.ClonePairInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.IPDGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.PDGControlNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.PDGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.PDGNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.PDGParameterNode;


public class ProgramSlice {

    static void addDuplicatedElementsWithBackwordSlice(final PDGNode<?> nodeA,
            final PDGNode<?> nodeB, final IPDGNodeFactory pdgNodeFactory,
            final Set<ClonePairInfo> clonePairs, final ClonePairInfo clonePair,
            final HashSet<PDGNode<?>> checkedNodesA, final HashSet<PDGNode<?>> checkedNodesB) {

        final Set<PDGEdge> edgesA = nodeA.getBackwardEdges();
        final Set<PDGEdge> edgesB = nodeB.getBackwardEdges();

        for (final PDGEdge edgeA : edgesA) {

            final PDGNode<?> fromNodeA = edgeA.getFromNode();

            if (checkedNodesA.contains(fromNodeA)) {
                continue;
            }

            //ParameterNodeであればとばす，将来は変更の必要あり
            if (fromNodeA instanceof PDGParameterNode) {
                continue;
            }

            final ExecutableElementInfo coreA = (ExecutableElementInfo) fromNodeA.getCore();
            final int hashA = Conversion.getNormalizedString(coreA).hashCode();

            for (final PDGEdge edgeB : edgesB) {

                final PDGNode<?> fromNodeB = edgeB.getFromNode();

                if (checkedNodesB.contains(fromNodeB)) {
                    continue;
                }

                //ParameterNodeであればとばす，将来は変更の必要あり
                if (fromNodeB instanceof PDGParameterNode) {
                    continue;
                }

                final ExecutableElementInfo coreB = (ExecutableElementInfo) fromNodeB.getCore();
                final int hashB = Conversion.getNormalizedString(coreB).hashCode();

                if (hashA == hashB) {

                    clonePair.add(coreA, coreB);
                    checkedNodesA.add(fromNodeA);
                    checkedNodesB.add(fromNodeB);

                    addDuplicatedElementsWithBackwordSlice(fromNodeA, fromNodeB, pdgNodeFactory,
                            clonePairs, clonePair, checkedNodesA, checkedNodesB);

                    if ((fromNodeA instanceof PDGControlNode)
                            && (fromNodeB instanceof PDGControlNode)) {

                        addDuplicatedElementsWithForwordSlice(fromNodeA, fromNodeB, pdgNodeFactory,
                                clonePairs, clonePair, checkedNodesA, checkedNodesB);
                    }
                }
            }
        }
    }

    static void addDuplicatedElementsWithForwordSlice(final PDGNode<?> nodeA,
            final PDGNode<?> nodeB, final IPDGNodeFactory pdgNodeFactory,
            final Set<ClonePairInfo> clonePairs, final ClonePairInfo clonePair,
            final HashSet<PDGNode<?>> checkedNodesA, final HashSet<PDGNode<?>> checkedNodesB) {

        final Set<PDGEdge> edgesA = nodeA.getForwardEdges();
        final Set<PDGEdge> edgesB = nodeB.getForwardEdges();

        for (final PDGEdge edgeA : edgesA) {

            final PDGNode<?> toNodeA = edgeA.getToNode();

            if (checkedNodesA.contains(toNodeA)) {
                continue;
            }

            //ParameterNodeであればとばす，将来は変更の必要あり
            if (toNodeA instanceof PDGParameterNode) {
                continue;
            }

            final ExecutableElementInfo coreA = (ExecutableElementInfo) toNodeA.getCore();
            final int hashA = Conversion.getNormalizedString(coreA).hashCode();

            for (final PDGEdge edgeB : edgesB) {

                final PDGNode<?> toNodeB = edgeB.getToNode();

                if (checkedNodesB.contains(toNodeB)) {
                    continue;
                }

                //ParameterNodeであればとばす，将来は変更の必要あり
                if (toNodeB instanceof PDGParameterNode) {
                    continue;
                }

                final ExecutableElementInfo coreB = (ExecutableElementInfo) toNodeB.getCore();
                final int hashB = Conversion.getNormalizedString(coreB).hashCode();

                if (hashA == hashB) {

                    clonePair.add(coreA, coreB);
                    checkedNodesA.add(toNodeA);
                    checkedNodesB.add(toNodeB);

                    addDuplicatedElementsWithBackwordSlice(toNodeA, toNodeB, pdgNodeFactory,
                            clonePairs, clonePair, checkedNodesA, checkedNodesB);

                    if ((toNodeA instanceof PDGControlNode) && (toNodeB instanceof PDGControlNode)) {

                        addDuplicatedElementsWithForwordSlice(toNodeA, toNodeB, pdgNodeFactory,
                                clonePairs, clonePair, checkedNodesA, checkedNodesB);
                    }
                }
            }
        }
    }
}
