package jp.ac.osaka_u.ist.sdl.scdetector;


import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Position;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;
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

            final Position coreA = (Position) fromNodeA.getCore();
            final int hashA = Conversion.getNormalizedString(coreA).hashCode();

            for (final PDGEdge edgeB : edgesB) {

                final PDGNode<?> fromNodeB = edgeB.getFromNode();

                if (checkedNodesB.contains(fromNodeB)) {
                    continue;
                }

                final Position coreB = (Position) fromNodeB.getCore();
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

            final Position coreA = (Position) toNodeA.getCore();
            final int hashA = Conversion.getNormalizedString(coreA).hashCode();

            for (final PDGEdge edgeB : edgesB) {

                final PDGNode<?> toNodeB = edgeB.getToNode();

                if (checkedNodesB.contains(toNodeB)) {
                    continue;
                }

                final Position coreB = (Position) toNodeB.getCore();
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

    /**
     * 文(statementA，statementB)を基点としてバックワードスライスを行い，
     * スライスに現れるハッシュ値の一致する文をクローンを構成する文として取得する
     * 
     * @param elementA
     * @param elementB
     * @param clonePair
     * @param usedVariableHashesA
     * @param usedVariableHashesB
     * @param clonePairs
     */
    /*
    static void performBackwordSlice(final ExecutableElementInfo elementA,
            final ExecutableElementInfo elementB, final ClonePairInfo clonePair,
            final Set<VariableInfo<?>> unmodifiableUsedVariableHashesA,
            final Set<VariableInfo<?>> unmodifiableUsedVariableHashesB,
            final Set<ClonePairInfo> clonePairs) {

        //調査した変数のハッシュを作成
        final Set<VariableInfo<?>> usedVariableHashesA = new HashSet<VariableInfo<?>>(
                unmodifiableUsedVariableHashesA);
        final Set<VariableInfo<?>> usedVariableHashesB = new HashSet<VariableInfo<?>>(
                unmodifiableUsedVariableHashesB);

        // スライス基点の変数利用を取得
        final Set<VariableUsageInfo<?>> variableUsagesA = elementA.getVariableUsages();
        final Set<VariableUsageInfo<?>> variableUsagesB = elementB.getVariableUsages();

        // スライス追加用のSetを宣言
        final SortedSet<ExecutableElementInfo> relatedElementsA = new TreeSet<ExecutableElementInfo>();
        final SortedSet<ExecutableElementInfo> relatedElementsB = new TreeSet<ExecutableElementInfo>();

        //　スライス基点(elementA)で利用されている変数に対して，その変数に対して代入を行っている文をスライス追加用のSetに格納する
        for (final VariableUsageInfo<?> variableUsage : variableUsagesA) {
            final VariableInfo<?> usedVariable = variableUsage.getUsedVariable();
            if (!usedVariableHashesA.contains(usedVariable)
                    && VariableHashMap.INSTANCE.containsKey(usedVariable)) {
                relatedElementsA.addAll(VariableHashMap.INSTANCE.get(usedVariable));
                usedVariableHashesA.add(usedVariable);
            }
        }

        //　スライス基点(elementB)で利用されている変数に対して，その変数に対して代入を行っている文をスライス追加用のSetに格納する
        for (final VariableUsageInfo<?> variableUsage : variableUsagesB) {
            final VariableInfo<?> usedVariable = variableUsage.getUsedVariable();
            if (!usedVariableHashesB.contains(usedVariable)
                    && VariableHashMap.INSTANCE.containsKey(usedVariable)) {
                relatedElementsB.addAll(VariableHashMap.INSTANCE.get(usedVariable));
                usedVariableHashesB.add(usedVariable);
            }
        }

        final ExecutableElementInfo[] relatedElementArrayA = relatedElementsA
                .toArray(new ExecutableElementInfo[] {});
        final ExecutableElementInfo[] relatedElementArrayB = relatedElementsB
                .toArray(new ExecutableElementInfo[] {});

        for (int a = 0; a < relatedElementArrayA.length; a++) {

            // クローンが存在しないExecutableElementについては調べる必要がない
            {
                final int hashA = NormalizedStatementHashMap.INSTANCE
                        .getHash(relatedElementArrayA[a]);
                if (NormalizedStatementHashMap.INSTANCE.get(hashA).size() < 2) {
                    continue;
                }
            }

            // Backwordスライスなので，自分よりも下にあるExecutableElementについては調べる必要がない
            if (relatedElementArrayA[a] == elementA) {
                break;
            }

            for (int b = 0; b < relatedElementArrayB.length; b++) {

                // クローンが存在しないExecutableElementについては調べる必要がない
                {
                    final int hashB = NormalizedStatementHashMap.INSTANCE
                            .getHash(relatedElementArrayB[b]);
                    if (NormalizedStatementHashMap.INSTANCE.get(hashB).size() < 2) {
                        continue;
                    }
                }

                // Backwordスライスなので，自分よりも下にあるExecutableElementについては調べる必要がない
                if (relatedElementArrayB[b] == elementB) {
                    break;
                }

                if ((relatedElementArrayA[a] instanceof SingleStatementInfo)
                        && (relatedElementArrayB[b] instanceof SingleStatementInfo)) {

                    final int hashA = NormalizedStatementHashMap.INSTANCE
                            .getHash(relatedElementArrayA[a]);
                    final int hashB = NormalizedStatementHashMap.INSTANCE
                            .getHash(relatedElementArrayB[b]);
                    if (hashA == hashB) {

                        clonePair.add(relatedElementArrayA[a], relatedElementArrayB[b]);

                        ProgramSlice.performBackwordSlice(relatedElementArrayA[a],
                                relatedElementArrayB[b], clonePair, Collections
                                        .unmodifiableSet(usedVariableHashesA), Collections
                                        .unmodifiableSet(usedVariableHashesB), clonePairs);

                        ProgramSlice.checkOwnerSpace(relatedElementArrayA[a],
                                relatedElementArrayB[b], clonePair, Collections
                                        .unmodifiableSet(usedVariableHashesA), Collections
                                        .unmodifiableSet(usedVariableHashesB), clonePairs);
                    }

                } else if ((relatedElementArrayA[a] instanceof ConditionInfo)
                        && (relatedElementArrayB[b] instanceof ConditionInfo)) {

                    final ConditionInfo conditionA = (ConditionInfo) relatedElementArrayA[a];
                    final ConditionInfo conditionB = (ConditionInfo) relatedElementArrayB[b];

                    final int hashA = conditionA.getText().hashCode();
                    final int hashB = conditionB.getText().hashCode();

                    if (hashA == hashB) {
                        clonePair.add(conditionA, conditionB);

                        ProgramSlice.performBackwordSlice(conditionA, conditionB, clonePair,
                                Collections.unmodifiableSet(usedVariableHashesA), Collections
                                        .unmodifiableSet(usedVariableHashesB), clonePairs);
                        ProgramSlice.performForwardSlice(conditionA, conditionB, clonePair,
                                Collections.unmodifiableSet(usedVariableHashesA), Collections
                                        .unmodifiableSet(usedVariableHashesB), clonePairs);
                        ProgramSlice.checkOwnerSpace(conditionA, conditionB, clonePair, Collections
                                .unmodifiableSet(usedVariableHashesA), Collections
                                .unmodifiableSet(usedVariableHashesB), clonePairs);
                    }
                }
            }
        }
    }

    private static void performForwardSlice(final ConditionInfo conditionA,
            final ConditionInfo conditionB, final ClonePairInfo clonePair,
            final Set<VariableInfo<?>> unmodifiableUsedVariableHashesA,
            final Set<VariableInfo<?>> unmodifiableUsedVariableHashesB,
            final Set<ClonePairInfo> clonePairs) {

        final Set<VariableUsageInfo<?>> variableUsagesA = conditionA.getVariableUsages();
        final Set<VariableUsageInfo<?>> variableUsagesB = conditionB.getVariableUsages();

        final Set<VariableInfo<?>> usedVariablesA = VariableUsageInfo
                .getUsedVariables(variableUsagesA);
        final Set<VariableInfo<?>> usedVariablesB = VariableUsageInfo
                .getUsedVariables(variableUsagesB);

        final ConditionalBlockInfo ownerBlockA = ConditionHashMap.INSTANCE.get(conditionA);
        final ConditionalBlockInfo ownerBlockB = ConditionHashMap.INSTANCE.get(conditionB);

        final SortedSet<ExecutableElementInfo> innerElementA = ProgramSlice
                .getAllInnerExecutableElementInfo(ownerBlockA);
        final SortedSet<ExecutableElementInfo> innerElementB = ProgramSlice
                .getAllInnerExecutableElementInfo(ownerBlockB);

        final ExecutableElementInfo[] innerElementArrayA = innerElementA
                .toArray(new ExecutableElementInfo[] {});
        final ExecutableElementInfo[] innerElementArrayB = innerElementB
                .toArray(new ExecutableElementInfo[] {});

        for (int a = 0; a < innerElementArrayA.length; a++) {

            //　クローンがないExecutableElementについては調べなくてよい            
            final int hashA = NormalizedStatementHashMap.INSTANCE.getHash(innerElementArrayA[a]);
            if (NormalizedStatementHashMap.INSTANCE.get(hashA).size() < 2) {
                continue;
            }

            for (int b = 0; b < innerElementArrayB.length; b++) {

                //　クローンがないExecutableElementについては調べなくてよい
                final int hashB = NormalizedStatementHashMap.INSTANCE
                        .getHash(innerElementArrayB[b]);
                if (NormalizedStatementHashMap.INSTANCE.get(hashB).size() < 2) {
                    continue;
                }

                if ((hashA == hashB) && ProgramSlice.isUsed(usedVariablesA, innerElementArrayA[a])
                        && ProgramSlice.isUsed(usedVariablesB, innerElementArrayB[b])) {

                    clonePair.add(innerElementArrayA[a], innerElementArrayB[b]);

                    ProgramSlice.performBackwordSlice(conditionA, conditionB, clonePair,
                            unmodifiableUsedVariableHashesA, unmodifiableUsedVariableHashesB,
                            clonePairs);
                }
            }
        }
    }

    private static void checkOwnerSpace(final ExecutableElementInfo element1,
            final ExecutableElementInfo element2, final ClonePairInfo clonePair,
            final Set<VariableInfo<?>> unmodifiableUsedVariableHashesA,
            final Set<VariableInfo<?>> unmodifiableUsedVariableHashesB,
            final Set<ClonePairInfo> clonePairs) {

        final LocalSpaceInfo ownerSpace1 = element1.getOwnerSpace();
        final LocalSpaceInfo ownerSpace2 = element2.getOwnerSpace();

        if ((ownerSpace1 instanceof ConditionalBlockInfo)
                && (ownerSpace2 instanceof ConditionalBlockInfo)) {

            final ConditionInfo condition1 = ((ConditionalBlockInfo) ownerSpace1)
                    .getConditionalClause().getCondition();
            final ConditionInfo condition2 = ((ConditionalBlockInfo) ownerSpace2)
                    .getConditionalClause().getCondition();
            final int hash1 = NormalizedStatementHashMap.INSTANCE.getHash(condition1);
            final int hash2 = NormalizedStatementHashMap.INSTANCE.getHash(condition2);

            if (hash1 == hash2) {
                clonePair.add(condition1, condition2);

                ProgramSlice.performBackwordSlice(condition1, condition2, clonePair,
                        unmodifiableUsedVariableHashesA, unmodifiableUsedVariableHashesB,
                        clonePairs);
                ProgramSlice.performForwardSlice(condition1, condition2, clonePair,
                        unmodifiableUsedVariableHashesA, unmodifiableUsedVariableHashesB,
                        clonePairs);
            }
        }
    }*/

    /**
     * 第一引数で与えられた変数を，第二引数で指定された式が使っているかを調べる
     * 
     * @param variables 
     * @param ExecutableElementInfo
     * @return 使っている場合はtrue,　使っていない場合はfalse
     */
    private static boolean isUsed(final Set<VariableInfo<?>> variables,
            final ExecutableElementInfo ExecutableElementInfo) {

        final Set<VariableUsageInfo<?>> variableUsages = ExecutableElementInfo.getVariableUsages();
        for (final VariableUsageInfo<?> variableUsage : variableUsages) {
            final VariableInfo<?> usedVariable = variableUsage.getUsedVariable();
            if (variables.contains(usedVariable)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 引数で与えられたブロック文の内側にある全てのExecutableElementのSortedSetを返す
     * 
     * @param block
     * @return
     */
    private static SortedSet<ExecutableElementInfo> getAllInnerExecutableElementInfo(
            final BlockInfo block) {

        final SortedSet<ExecutableElementInfo> elements = new TreeSet<ExecutableElementInfo>();
        for (final StatementInfo statement : block.getStatements()) {
            if (statement instanceof SingleStatementInfo) {
                elements.add(statement);
            } else if (statement instanceof BlockInfo) {
                elements.addAll(ProgramSlice
                        .getAllInnerExecutableElementInfo((BlockInfo) statement));

                if (statement instanceof ConditionalBlockInfo) {
                    elements.add(((ConditionalBlockInfo) statement).getConditionalClause()
                            .getCondition());
                }
            }
        }

        return elements;
    }
}
