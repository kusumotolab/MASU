package jp.ac.osaka_u.ist.sdl.scdetector;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.MetricsTool;
import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageListener;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePool;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;


/**
 * プログラム依存グラフの同形なサブグラフ部分をコードクローンとして検出するプログラム
 * 検出対象は引数で与えられる．
 * 
 * @author higo
 */
public class SCDetector extends MetricsTool {

    public static void main(String[] args) {

        // 解析用設定
        try {

            final Class<?> settings = Settings.class;
            final Field language = settings.getDeclaredField("language");
            language.setAccessible(true);
            language.set(null, "java");
            final Field directory = settings.getDeclaredField("targetDirectory");
            directory.setAccessible(true);
            directory.set(null, args[0]);
            final Field verbose = settings.getDeclaredField("verbose");
            verbose.setAccessible(true);
            verbose.set(null, Boolean.TRUE);

            // 情報表示用設定
            final Class<?> metricstool = MetricsTool.class;
            final Field out = metricstool.getDeclaredField("out");
            out.setAccessible(true);
            out.set(null, new DefaultMessagePrinter(new MessageSource() {
                public String getMessageSourceName() {
                    return "scdetector";
                }
            }, MESSAGE_TYPE.OUT));
            final Field err = metricstool.getDeclaredField("err");
            err.setAccessible(true);
            err.set(null, new DefaultMessagePrinter(new MessageSource() {
                public String getMessageSourceName() {
                    return "main";
                }
            }, MESSAGE_TYPE.ERROR));
            MessagePool.getInstance(MESSAGE_TYPE.OUT).addMessageListener(new MessageListener() {
                public void messageReceived(MessageEvent event) {
                    System.out.print(event.getSource().getMessageSourceName() + " > "
                            + event.getMessage());
                }
            });
            MessagePool.getInstance(MESSAGE_TYPE.ERROR).addMessageListener(new MessageListener() {
                public void messageReceived(MessageEvent event) {
                    System.err.print(event.getSource().getMessageSourceName() + " > "
                            + event.getMessage());
                }
            });

        } catch (NoSuchFieldException e) {
            System.out.println(e.getMessage());
        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
        }

        // 対象ディレクトリ以下のJavaファイルを登録し，解析
        final SCDetector scdetector = new SCDetector();
        scdetector.registerFilesFromDirectory();
        scdetector.analyzeTargetFiles();

        // 変数を指定することにより，その変数を使用している文を取得するためのハッシュを構築する
        final Map<VariableInfo<?>, Set<StatementInfo>> variableUsageHashes = new HashMap<VariableInfo<?>, Set<StatementInfo>>();
        for (final TargetMethodInfo method : MethodInfoManager.getInstance().getTargetMethodInfos()) {
            SCDetector.makeVariableUsageHashes(method, variableUsageHashes);
        }

        // 文単位で正規化を行う
        final Map<Integer, List<StatementInfo>> normalizedStatementHashes = new HashMap<Integer, List<StatementInfo>>();
        for (final TargetMethodInfo method : MethodInfoManager.getInstance().getTargetMethodInfos()) {
            SCDetector.makeNormalizedStatementHashes(method, normalizedStatementHashes);
        }

        /*
        for (final Integer hash : normalizedStatementHashes.keySet()) {

            final List<StatementInfo> statements = normalizedStatementHashes.get(hash);
            if (1 < statements.size()) {
                System.out.println("-----BEGIN-----");
                for (final StatementInfo statement : statements) {
                    final String text = statement.getText();
                    System.out.println(text);
                }
                System.out.println("----- END -----");
            }
        }*/

        for (final Integer hash : normalizedStatementHashes.keySet()) {

            final List<StatementInfo> statements = normalizedStatementHashes.get(hash);
            if (1 < statements.size()) {
                for (int i = 0; i < statements.size(); i++) {
                    for (int j = i + 1; j < statements.size(); j++) {

                        final StatementInfo statementA = statements.get(i);
                        final StatementInfo statementB = statements.get(j);

                        final ClonePairInfo clonePair = new ClonePairInfo(statementA, statementB);

                        final Set<VariableUsageInfo<?>> variableUsagesA = statementA
                                .getVariableUsages();
                        final Set<VariableUsageInfo<?>> variableUsagesB = statementB
                                .getVariableUsages();

                        final Set<StatementInfo> relatedStatementsA = new HashSet<StatementInfo>();
                        for (final VariableUsageInfo<?> variableUsageA : variableUsagesA) {
                            final VariableInfo<?> usedVariableA = variableUsageA.getUsedVariable();
                            relatedStatementsA.addAll(variableUsageHashes.get(usedVariableA));
                        }

                        final Set<StatementInfo> relatedStatementsB = new HashSet<StatementInfo>();
                        for (final VariableUsageInfo<?> variableUsageB : variableUsagesB) {
                            final VariableInfo<?> usedVariableB = variableUsageB.getUsedVariable();
                            relatedStatementsB.addAll(variableUsageHashes.get(usedVariableB));
                        }

                        final StatementInfo[] relatedStatementArrayA = relatedStatementsA
                                .toArray(new StatementInfo[] {});
                        final StatementInfo[] relatedStatementArrayB = relatedStatementsB
                                .toArray(new StatementInfo[] {});
                        for (int a = 0; a < relatedStatementsA.size(); a++) {
                            for (int b = 0; b < relatedStatementsB.size(); b++) {

                                if ((relatedStatementArrayA[a] instanceof SingleStatementInfo)
                                        && (relatedStatementArrayB[b] instanceof SingleStatementInfo)) {
                                    final String normalizedStatementA = Conversion
                                            .getNormalizedString((SingleStatementInfo) relatedStatementArrayA[a]);
                                    final String normalizedStatementB = Conversion
                                            .getNormalizedString((SingleStatementInfo) relatedStatementArrayB[b]);
                                    if (normalizedStatementA.hashCode() == normalizedStatementB
                                            .hashCode()) {
                                        clonePair.add(relatedStatementArrayA[a],
                                                relatedStatementArrayB[b]);
                                    }
                                }

                            }
                        }

                        if (1 < clonePair.size()) {
                            System.out.println("-----BEGIN-----");
                            final SortedSet<StatementInfo> cloneA = clonePair.getCloneA();
                            final SortedSet<StatementInfo> cloneB = clonePair.getCloneB();
                            for (final StatementInfo statement : cloneA) {
                                System.out.println(statement.getFromLine() + ":"
                                        + statement.getText());
                            }
                            System.out.println("-----  B  -----");
                            for (final StatementInfo statement : cloneB) {
                                System.out.println(statement.getFromLine() + ":"
                                        + statement.getText());
                            }
                            System.out.println("----- END -----");
                        }
                    }
                }
            }
        }
    }

    private static void makeVariableUsageHashes(final LocalSpaceInfo localSpace,
            final Map<VariableInfo<?>, Set<StatementInfo>> variableUsageHashes) {

        for (final StatementInfo statement : localSpace.getStatements()) {

            if (statement instanceof SingleStatementInfo) {

                final Set<VariableUsageInfo<?>> variableUsages = ((SingleStatementInfo) statement)
                        .getVariableUsages();
                for (final VariableUsageInfo<?> variableUsage : variableUsages) {
                    final VariableInfo<?> usedVariable = variableUsage.getUsedVariable();
                    Set<StatementInfo> statements = variableUsageHashes.get(usedVariable);
                    if (null == statements) {
                        statements = new HashSet<StatementInfo>();
                        variableUsageHashes.put(usedVariable, statements);
                    }
                    statements.add(statement);
                }

            } else if (statement instanceof BlockInfo) {
                SCDetector.makeVariableUsageHashes((BlockInfo) statement, variableUsageHashes);
            }
        }
    }

    private static void makeNormalizedStatementHashes(final LocalSpaceInfo localSpace,
            final Map<Integer, List<StatementInfo>> normalizedStatementHashes) {

        for (final StatementInfo statement : localSpace.getStatements()) {

            if (statement instanceof SingleStatementInfo) {

                final String normalizedStatement = Conversion
                        .getNormalizedString((SingleStatementInfo) statement);
                final int hash = normalizedStatement.hashCode();

                List<StatementInfo> statements = normalizedStatementHashes.get(hash);
                if (null == statements) {
                    statements = new ArrayList<StatementInfo>();
                    normalizedStatementHashes.put(hash, statements);
                }
                statements.add(statement);

            } else if (statement instanceof BlockInfo) {
                SCDetector.makeNormalizedStatementHashes((BlockInfo) statement,
                        normalizedStatementHashes);
            }
        }

    }
}
