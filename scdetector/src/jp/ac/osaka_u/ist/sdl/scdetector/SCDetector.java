package jp.ac.osaka_u.ist.sdl.scdetector;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.MetricsTool;
import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;


/**
 * プログラム依存グラフの同形なサブグラフ部分をコードクローンとして検出するプログラム
 * 検出対象は引数で与えられる．
 * 
 * @author higo
 */
public class SCDetector extends MetricsTool {

    public static void main(String[] args) {

        try {

            //　コマンドライン引数を処理
            final Options options = new Options();

            final Option d = new Option("d", "directory", true, "target directory");
            d.setArgName("directory");
            d.setArgs(1);
            d.setRequired(true);
            options.addOption(d);

            final Option l = new Option("l", "language", true,
                    "programming language of analyzed source code");
            l.setArgName("language");
            l.setArgs(1);
            l.setRequired(true);
            options.addOption(l);

            final Option o = new Option("o", "output", true, "output file");
            o.setArgName("output file");
            o.setArgs(1);
            o.setRequired(true);
            options.addOption(o);

            final Option pv = new Option("pv", true, "parameterize variables");
            pv.setArgName("parameterization level");
            pv.setArgs(1);
            pv.setRequired(false);
            pv.setType(Integer.class);
            options.addOption(pv);

            final CommandLineParser parser = new PosixParser();
            final CommandLine cmd = parser.parse(options, args);

            Configuration.INSTANCE.setD(cmd.getOptionValue("d"));
            Configuration.INSTANCE.setL(cmd.getOptionValue("l"));
            Configuration.INSTANCE.setO(cmd.getOptionValue("o"));

            if (cmd.hasOption("pv")) {
                Configuration.INSTANCE.setPV(Integer.valueOf(cmd.getOptionValue("pv")));
            }

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

        try {

            // 解析用設定
            final Class<?> settings = Settings.class;
            final Field language = settings.getDeclaredField("language");
            language.setAccessible(true);
            language.set(null, Configuration.INSTANCE.getL());
            final Field directory = settings.getDeclaredField("targetDirectory");
            directory.setAccessible(true);
            directory.set(null, Configuration.INSTANCE.getD());
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
        final Map<Integer, List<StatementInfo>> normalizedStatements = new HashMap<Integer, List<StatementInfo>>();
        for (final TargetMethodInfo method : MethodInfoManager.getInstance().getTargetMethodInfos()) {
            SCDetector.makeNormalizedStatementHashes(method, normalizedStatements);
        }

        // 文単位のハッシュデータを作成
        final Map<StatementInfo, Integer> statementHash = new HashMap<StatementInfo, Integer>();
        for (final Integer hash : normalizedStatements.keySet()) {
            final List<StatementInfo> statements = normalizedStatements.get(hash);
            for (final StatementInfo statement : statements) {
                statementHash.put(statement, hash);
            }

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

        final Set<ClonePairInfo> clonePairs = new HashSet<ClonePairInfo>();

        int index = 0;
        for (final Integer hash : normalizedStatements.keySet()) {

            System.out.println("===" + index++ + "/" + normalizedStatements.size() + "===");

            final List<StatementInfo> statements = normalizedStatements.get(hash);

            System.out.println(statements.size());

            if ((1 < statements.size()) && (statements.size() < 10)) {
                for (int i = 0; i < statements.size(); i++) {
                    for (int j = i + 1; j < statements.size(); j++) {

                        final StatementInfo statementA = statements.get(i);
                        final StatementInfo statementB = statements.get(j);

                        final ClonePairInfo clonePair = new ClonePairInfo(statementA, statementB);

                        final Set<VariableInfo<?>> usedVariableHashesA = new HashSet<VariableInfo<?>>();
                        final Set<VariableInfo<?>> usedVariableHashesB = new HashSet<VariableInfo<?>>();

                        SCDetector.performBackwordSlice((SingleStatementInfo) statementA,
                                (SingleStatementInfo) statementB, clonePair, variableUsageHashes,
                                statementHash, usedVariableHashesA, usedVariableHashesB);

                        if (1 < clonePair.size()) {
                            clonePairs.add(clonePair);

                            System.out.println("-----BEGIN-----");
                            System.out.println("-----  A  -----");
                            final SortedSet<ExecutableElement> cloneA = clonePair.getCloneA();
                            final SortedSet<ExecutableElement> cloneB = clonePair.getCloneB();
                            for (final ExecutableElement statement : cloneA) {
                                if (statement instanceof StatementInfo) {
                                    System.out.println(statement.getFromLine() + ":"
                                            + ((StatementInfo) statement).getText());
                                } else if (statement instanceof ConditionInfo) {
                                    System.out.println(statement.getFromLine() + ":"
                                            + ((ConditionInfo) statement).getText());
                                }
                            }
                            System.out.println("-----  B  -----");
                            for (final ExecutableElement statement : cloneB) {
                                if (statement instanceof StatementInfo) {
                                    System.out.println(statement.getFromLine() + ":"
                                            + ((StatementInfo) statement).getText());
                                } else if (statement instanceof ConditionInfo) {
                                    System.out.println(statement.getFromLine() + ":"
                                            + ((ConditionInfo) statement).getText());
                                }
                            }
                            System.out.println("----- END -----");
                        }
                    }
                }
            }
        }

        try {

            final ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
                    Configuration.INSTANCE.getO()));
            oos.writeObject(clonePairs);

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
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

    private static void performBackwordSlice(final SingleStatementInfo statementA,
            final SingleStatementInfo statementB, final ClonePairInfo clonePair,
            final Map<VariableInfo<?>, Set<StatementInfo>> variableUsageHashes,
            final Map<StatementInfo, Integer> statementHash,
            final Set<VariableInfo<?>> usedVariableHashesA,
            final Set<VariableInfo<?>> usedVariableHashesB) {

        final Set<VariableUsageInfo<?>> variableUsagesA = statementA.getVariableUsages();
        final Set<VariableUsageInfo<?>> variableUsagesB = statementB.getVariableUsages();

        final SortedSet<StatementInfo> relatedStatementsA = new TreeSet<StatementInfo>();
        final SortedSet<StatementInfo> relatedStatementsB = new TreeSet<StatementInfo>();

        for (final VariableUsageInfo<?> variableUsage : variableUsagesA) {
            final VariableInfo<?> usedVariable = variableUsage.getUsedVariable();
            if (!usedVariableHashesA.contains(usedVariable) && !(usedVariable instanceof FieldInfo)) {
                relatedStatementsA.addAll(variableUsageHashes.get(usedVariable));
                usedVariableHashesA.add(usedVariable);
            }
        }

        for (final VariableUsageInfo<?> variableUsage : variableUsagesB) {
            final VariableInfo<?> usedVariable = variableUsage.getUsedVariable();
            if (!usedVariableHashesB.contains(usedVariable) && !(usedVariable instanceof FieldInfo)) {
                relatedStatementsB.addAll(variableUsageHashes.get(usedVariable));
                usedVariableHashesB.add(usedVariable);
            }
        }

        final StatementInfo[] relatedStatementArrayA = relatedStatementsA
                .toArray(new StatementInfo[] {});
        final StatementInfo[] relatedStatementArrayB = relatedStatementsB
                .toArray(new StatementInfo[] {});

        for (int a = 0; a < relatedStatementArrayA.length; a++) {

            if (relatedStatementArrayA[a] == statementA) {
                break;
            }

            for (int b = 0; b < relatedStatementArrayB.length; b++) {

                if (relatedStatementArrayB[b] == statementB) {
                    break;
                }

                if ((relatedStatementArrayA[a] instanceof SingleStatementInfo)
                        && (relatedStatementArrayB[b] instanceof SingleStatementInfo)) {

                    final int hashA = statementHash.get(relatedStatementArrayA[a]);
                    final int hashB = statementHash.get(relatedStatementArrayB[b]);
                    if (hashA == hashB) {
                        clonePair.add(relatedStatementArrayA[a], relatedStatementArrayB[b]);

                        SCDetector.performBackwordSlice(
                                (SingleStatementInfo) relatedStatementArrayA[a],
                                (SingleStatementInfo) relatedStatementArrayB[b], clonePair,
                                variableUsageHashes, statementHash, usedVariableHashesA,
                                usedVariableHashesB);
                    }

                } else if ((relatedStatementArrayA[a] instanceof ConditionalBlockInfo)
                        && (relatedStatementArrayB[b] instanceof ConditionalBlockInfo)) {

                    final ConditionInfo conditionA = ((ConditionalBlockInfo) relatedStatementArrayA[a])
                            .getCondition();
                    final ConditionInfo conditionB = ((ConditionalBlockInfo) relatedStatementArrayB[b])
                            .getCondition();

                    final int hashA = conditionA.getText().hashCode();
                    final int hashB = conditionB.getText().hashCode();

                    if (hashA == hashB) {

                        clonePair.add(conditionA, conditionB);

                        SCDetector.performForwardSlice(
                                (ConditionalBlockInfo) relatedStatementArrayA[a],
                                (ConditionalBlockInfo) relatedStatementArrayB[b], clonePair,
                                variableUsageHashes, statementHash, usedVariableHashesA,
                                usedVariableHashesB);
                    }
                }
            }
        }

        /*
        // Heuristisc: 両方の文における変数使用の数が同じときのみスライスを取る
        if (variableUsagesA.size() == variableUsagesB.size()) {

            final Iterator<VariableUsageInfo<?>> iteratorA = variableUsagesA.iterator();
            final Iterator<VariableUsageInfo<?>> iteratorB = variableUsagesB.iterator();

            while (iteratorA.hasNext() && iteratorB.hasNext()) {

                final SortedSet<StatementInfo> relatedStatementsA = new TreeSet<StatementInfo>();
                final SortedSet<StatementInfo> relatedStatementsB = new TreeSet<StatementInfo>();

                final VariableUsageInfo<?> variableUsageA = iteratorA.next();
                final VariableUsageInfo<?> variableUsageB = iteratorB.next();

                final VariableInfo<?> usedVariableA = variableUsageA.getUsedVariable();
                final VariableInfo<?> usedVariableB = variableUsageB.getUsedVariable();

                // 一時的にローカル変数と引数のみをスライスに利用するようにしている
                // 最終的には，すべての種類の変数を用いて，同じメソッド内の文をとってくるように変更
                if (!(usedVariableA instanceof FieldInfo) && !(usedVariableB instanceof FieldInfo)) {
                    relatedStatementsA.addAll(variableUsageHashes.get(usedVariableA));
                    relatedStatementsB.addAll(variableUsageHashes.get(usedVariableB));
                }
            }
        }*/
    }

    private static void performForwardSlice(final ConditionalBlockInfo blockA,
            final ConditionalBlockInfo blockB, final ClonePairInfo clonePair,
            final Map<VariableInfo<?>, Set<StatementInfo>> variableUsageHashes,
            final Map<StatementInfo, Integer> statementHash,
            final Set<VariableInfo<?>> usedVariableHashesA,
            final Set<VariableInfo<?>> usedVariableHashesB) {

        final ConditionInfo conditionA = blockA.getCondition();
        final ConditionInfo conditionB = blockB.getCondition();

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

        final SortedSet<StatementInfo> innerStatementA = blockA.getStatements();
        final SortedSet<StatementInfo> innerStatementB = blockB.getStatements();

        final StatementInfo[] innerStatementArrayA = innerStatementA
                .toArray(new StatementInfo[] {});
        final StatementInfo[] innerStatementArrayB = innerStatementB
                .toArray(new StatementInfo[] {});

        for (int i = 0; i < innerStatementArrayA.length; i++) {
            for (int j = 0; j < innerStatementArrayB.length; j++) {

                if ((innerStatementArrayA[i] instanceof SingleStatementInfo)
                        && (innerStatementArrayB[j] instanceof SingleStatementInfo)) {

                    final int hashA = statementHash.get(innerStatementArrayA[i]);
                    final int hashB = statementHash.get(innerStatementArrayB[j]);

                    if ((hashA == hashB)
                            && SCDetector.isUsed(usedVariablesA, innerStatementArrayA[i])
                            && SCDetector.isUsed(usedVariablesB, innerStatementArrayB[j])) {
                    }

                } else if ((innerStatementArrayA[i] instanceof ConditionalBlockInfo)
                        && (innerStatementArrayB[j] instanceof ConditionalBlockInfo)) {

                    final ConditionInfo innerConditionA = ((ConditionalBlockInfo) innerStatementArrayA[i])
                            .getCondition();
                    final ConditionInfo innerConditionB = ((ConditionalBlockInfo) innerStatementArrayB[j])
                            .getCondition();

                    final int hashA = innerConditionA.getText().hashCode();
                    final int hashB = innerConditionB.getText().hashCode();

                    if ((hashA == hashB) && SCDetector.isUsed(usedVariablesA, innerConditionA)
                            && SCDetector.isUsed(usedVariablesB, innerConditionB)) {
                        clonePair.add(innerConditionA, innerConditionB);

                        SCDetector.performForwardSlice(
                                (ConditionalBlockInfo) innerStatementArrayA[i],
                                (ConditionalBlockInfo) innerStatementArrayB[j], clonePair,
                                variableUsageHashes, statementHash, usedVariableHashesA,
                                usedVariableHashesB);
                    }
                }
            }
        }
    }

    private static boolean isUsed(final Set<VariableInfo<?>> variables,
            final ExecutableElement executableElement) {

        final Set<VariableUsageInfo<?>> variableUsages = executableElement.getVariableUsages();
        for (final VariableUsageInfo<?> variableUsage : variableUsages) {
            final VariableInfo<?> usedVariable = variableUsage.getUsedVariable();
            if (variables.contains(usedVariable)) {
                return true;
            }
        }

        return false;
    }
}
