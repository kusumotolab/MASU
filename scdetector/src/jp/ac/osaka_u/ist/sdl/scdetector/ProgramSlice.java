package jp.ac.osaka_u.ist.sdl.scdetector;


import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.IPDGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.PDGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.PDGNode;


public class ProgramSlice {

    static void addDuplicatedElementsWithBackwordSlice(final PDGNode<?> nodeA,
            final PDGNode<?> nodeB, final IPDGNodeFactory pdgNodeFactory,
            final ClonePairInfo clonePair, final HashSet<PDGNode<?>> checkedNodesA,
            final HashSet<PDGNode<?>> checkedNodesB) {

        final Set<PDGEdge> edgesA = nodeA.getBackwardEdges();
        final Set<PDGEdge> edgesB = nodeB.getBackwardEdges();

        for (final PDGEdge edgeA : edgesA) {

            final PDGNode<?> fromNodeA = edgeA.getFromNode();

            if (checkedNodesA.contains(fromNodeA)) {
                continue;
            }

            final ExecutableElementInfo coreA = (ExecutableElementInfo) fromNodeA.getCore();
            final int hashA = Conversion.getNormalizedString(coreA).hashCode();

            for (final PDGEdge edgeB : edgesB) {

                final PDGNode<?> fromNodeB = edgeB.getFromNode();

                if (checkedNodesB.contains(fromNodeB)) {
                    continue;
                }

                final ExecutableElementInfo coreB = (ExecutableElementInfo) fromNodeB.getCore();
                final int hashB = Conversion.getNormalizedString(coreB).hashCode();

                if (hashA == hashB) {

                    clonePair.add(coreA, coreB);
                    checkedNodesA.add(fromNodeA);
                    checkedNodesB.add(fromNodeB);

                    addDuplicatedElementsWithBackwordSlice(fromNodeA, fromNodeB, pdgNodeFactory,
                            clonePair, checkedNodesA, checkedNodesB);

                    if ((coreA instanceof ConditionalBlockInfo)
                            && (coreB instanceof ConditionalBlockInfo)) {

                        addDuplicatedElementsWithBackwordSlice(fromNodeA, fromNodeB,
                                pdgNodeFactory, clonePair, checkedNodesA, checkedNodesB);
                    }
                }
            }
        }
    }

    static void addDuplicatedElementsWithForwordSlice(final PDGNode<?> nodeA,
            final PDGNode<?> nodeB, final IPDGNodeFactory pdgNodeFactory,
            final ClonePairInfo clonePair, final HashSet<PDGNode<?>> checkedNodesA,
            final HashSet<PDGNode<?>> checkedNodesB) {

        final Set<PDGEdge> edgesA = nodeA.getForwardEdges();
        final Set<PDGEdge> edgesB = nodeB.getForwardEdges();

        for (final PDGEdge edgeA : edgesA) {

            final PDGNode<?> toNodeA = edgeA.getToNode();

            if (checkedNodesA.contains(toNodeA)) {
                continue;
            }

            final ExecutableElementInfo coreA = (ExecutableElementInfo) toNodeA.getCore();
            final int hashA = Conversion.getNormalizedString(coreA).hashCode();

            for (final PDGEdge edgeB : edgesB) {

                final PDGNode<?> toNodeB = edgeB.getToNode();

                if (checkedNodesB.contains(toNodeB)) {
                    continue;
                }

                final ExecutableElementInfo coreB = (ExecutableElementInfo) toNodeB.getCore();
                final int hashB = Conversion.getNormalizedString(coreB).hashCode();

                if (hashA == hashB) {

                    clonePair.add(coreA, coreB);
                    checkedNodesA.add(toNodeA);
                    checkedNodesB.add(toNodeB);

                    addDuplicatedElementsWithBackwordSlice(toNodeA, toNodeB, pdgNodeFactory,
                            clonePair, checkedNodesA, checkedNodesB);

                    if ((coreA instanceof ConditionalBlockInfo)
                            && (coreB instanceof ConditionalBlockInfo)) {

                        addDuplicatedElementsWithForwordSlice(toNodeA, toNodeB, pdgNodeFactory,
                                clonePair, checkedNodesA, checkedNodesB);
                    }
                }
            }
        }
    }
}
