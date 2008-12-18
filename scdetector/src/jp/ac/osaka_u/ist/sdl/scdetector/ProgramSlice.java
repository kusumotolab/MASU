package jp.ac.osaka_u.ist.sdl.scdetector;


import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


public class ProgramSlice {

    /**
     * 文(statementA，statementB)を基点としてバックワードスライスを行い，
     * スライスに現れるハッシュ値の一致する文をクローンを構成する文として取得する
     * 
     * @param elementA
     * @param elementB
     * @param clonePair
     * @param usedVariableHashesA
     * @param usedVariableHashesB
     */
    static void performBackwordSlice(final ExecutableElementInfo elementA,
            final ExecutableElementInfo elementB, final ClonePairInfo clonePair,
            final Set<VariableInfo<?>> usedVariableHashesA,
            final Set<VariableInfo<?>> usedVariableHashesB) {

        // スライス基点の変数利用を取得
        final Set<VariableUsageInfo<?>> variableUsagesA = elementA.getVariableUsages();
        final Set<VariableUsageInfo<?>> variableUsagesB = elementB.getVariableUsages();

        // スライス追加用のSetを宣言
        final SortedSet<ExecutableElementInfo> relatedElementsA = new TreeSet<ExecutableElementInfo>();
        final SortedSet<ExecutableElementInfo> relatedElementsB = new TreeSet<ExecutableElementInfo>();

        //　スライス基点(elementA)の変数利用が参照であれば，その変数に対して代入を行っている文をスライス追加用のSetに格納する
        for (final VariableUsageInfo<?> variableUsage : variableUsagesA) {
            if (variableUsage.isReference()) {
                final VariableInfo<?> usedVariable = variableUsage.getUsedVariable();
                if (!usedVariableHashesA.contains(usedVariable)
                        && !(usedVariable instanceof FieldInfo)
                        && AssignedVariableHashMap.INSTANCE.containsKey(usedVariable)) {
                    relatedElementsA.addAll(AssignedVariableHashMap.INSTANCE.get(usedVariable));
                    usedVariableHashesA.add(usedVariable);
                }
            }
        }

        //　スライス基点(elementB)の変数利用が参照であれば，その変数に対して代入を行っている文をスライス追加用のSetに格納する
        for (final VariableUsageInfo<?> variableUsage : variableUsagesB) {
            final VariableInfo<?> usedVariable = variableUsage.getUsedVariable();
            if (variableUsage.isReference()) {
                if (!usedVariableHashesB.contains(usedVariable)
                        && !(usedVariable instanceof FieldInfo)
                        && AssignedVariableHashMap.INSTANCE.containsKey(usedVariable)) {
                    relatedElementsB.addAll(AssignedVariableHashMap.INSTANCE.get(usedVariable));
                    usedVariableHashesB.add(usedVariable);
                }
            }
        }

        final ExecutableElementInfo[] relatedElementArrayA = relatedElementsA
                .toArray(new ExecutableElementInfo[] {});
        final ExecutableElementInfo[] relatedElementArrayB = relatedElementsB
                .toArray(new ExecutableElementInfo[] {});

        for (int a = 0; a < relatedElementArrayA.length; a++) {

            // クローンが存在しないExecutableElementについては調べる必要がない
            {
                final int hashA = NormalizedElementHashMap.INSTANCE
                        .getHash(relatedElementArrayA[a]);
                if (NormalizedElementHashMap.INSTANCE.get(hashA).size() < 2) {
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
                    final int hashB = NormalizedElementHashMap.INSTANCE
                            .getHash(relatedElementArrayB[b]);
                    if (NormalizedElementHashMap.INSTANCE.get(hashB).size() < 2) {
                        continue;
                    }
                }

                // Backwordスライスなので，自分よりも下にあるExecutableElementについては調べる必要がない
                if (relatedElementArrayB[b] == elementB) {
                    break;
                }

                if ((relatedElementArrayA[a] instanceof SingleStatementInfo)
                        && (relatedElementArrayB[b] instanceof SingleStatementInfo)) {

                    final int hashA = NormalizedElementHashMap.INSTANCE
                            .getHash(relatedElementArrayA[a]);
                    final int hashB = NormalizedElementHashMap.INSTANCE
                            .getHash(relatedElementArrayB[b]);
                    if (hashA == hashB) {
                        clonePair.add(relatedElementArrayA[a], relatedElementArrayB[b]);

                        ProgramSlice.performBackwordSlice(relatedElementArrayA[a],
                                relatedElementArrayB[b], clonePair, usedVariableHashesA,
                                usedVariableHashesB);
                    }

                } else if ((relatedElementArrayA[a] instanceof ConditionInfo)
                        && (relatedElementArrayB[b] instanceof ConditionInfo)) {

                    final ConditionInfo conditionA = (ConditionInfo) relatedElementArrayA[a];
                    final ConditionInfo conditionB = (ConditionInfo) relatedElementArrayB[b];

                    final int hashA = conditionA.getText().hashCode();
                    final int hashB = conditionB.getText().hashCode();

                    if (hashA == hashB) {
                        clonePair.add(conditionA, conditionB);

                        ProgramSlice.performForwardSlice(conditionA, conditionB, clonePair,
                                usedVariableHashesA, usedVariableHashesB);
                    }
                }
            }
        }
    }

    static void performForwardSlice(final ConditionInfo conditionA, final ConditionInfo conditionB,
            final ClonePairInfo clonePair, final Set<VariableInfo<?>> usedVariableHashesA,
            final Set<VariableInfo<?>> usedVariableHashesB) {

        final Set<VariableUsageInfo<?>> variableUsagesA = conditionA.getVariableUsages();
        final Set<VariableUsageInfo<?>> variableUsagesB = conditionB.getVariableUsages();

        final Set<VariableInfo<?>> usedVariablesA = new HashSet<VariableInfo<?>>();
        for (final VariableUsageInfo<?> variableUsage : variableUsagesA) {
            final VariableInfo<?> variable = variableUsage.getUsedVariable();
            usedVariablesA.add(variable);
        }

        final Set<VariableInfo<?>> usedVariablesB = new HashSet<VariableInfo<?>>();
        for (final VariableUsageInfo<?> variableUsage : variableUsagesB) {
            final VariableInfo<?> variable = variableUsage.getUsedVariable();
            usedVariablesB.add(variable);
        }

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

        for (int i = 0; i < innerElementArrayA.length; i++) {

            //　クローンがないExecutableElementについては調べなくてよい
            final int hashA = NormalizedElementHashMap.INSTANCE.getHash(innerElementArrayA[i]);
            if (NormalizedElementHashMap.INSTANCE.get(hashA).size() < 2) {
                continue;
            }

            for (int j = 0; j < innerElementArrayB.length; j++) {

                //　クローンがないExecutableElementについては調べなくてよい
                final int hashB = NormalizedElementHashMap.INSTANCE.getHash(innerElementArrayB[j]);
                if (NormalizedElementHashMap.INSTANCE.get(hashB).size() < 2) {
                    continue;
                }

                if ((hashA == hashB) && ProgramSlice.isUsed(usedVariablesA, innerElementArrayA[i])
                        && ProgramSlice.isUsed(usedVariablesB, innerElementArrayB[j])) {

                    clonePair.add(innerElementArrayA[i], innerElementArrayB[j]);
                }
            }
        }
    }

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

    private static SortedSet<ExecutableElementInfo> getAllInnerExecutableElementInfo(
            final BlockInfo block) {

        final SortedSet<ExecutableElementInfo> elements = new TreeSet<ExecutableElementInfo>();
        for (final StatementInfo statement : block.getStatements()) {
            if (statement instanceof SingleStatementInfo) {
                elements.add(statement);
            } else if (statement instanceof BlockInfo) {
                elements.addAll(ProgramSlice
                        .getAllInnerExecutableElementInfo((BlockInfo) statement));
            }
        }

        return elements;
    }
}
