package jp.ac.osaka_u.ist.sdl.scdetector;


import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.MetricsTool;
import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
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

        final Map<Integer, Set<StatementInfo>> normalizedStatementHashes = new HashMap<Integer, Set<StatementInfo>>();

        // 文単位で正規化を行う
        for (final TargetMethodInfo method : MethodInfoManager.getInstance().getTargetMethodInfos()) {
            SCDetector.makeNormalizedStatementHashes(method, normalizedStatementHashes);
        }

        for (final Integer hash : normalizedStatementHashes.keySet()) {

            final Set<StatementInfo> statements = normalizedStatementHashes.get(hash);
            if (1 < statements.size()) {
                System.out.println("-----BEGIN-----");
                for (final StatementInfo statement : statements) {
                    final String text = statement.getText();
                    System.out.println(text);
                }
                System.out.println("----- END -----");
            }
        }
    }

    private static void makeNormalizedStatementHashes(final LocalSpaceInfo localSpace,
            final Map<Integer, Set<StatementInfo>> normalizedStatementHashes) {

        for (final StatementInfo statement : localSpace.getStatements()) {

            if (statement instanceof SingleStatementInfo) {

                final String normalizedStatement = Conversion
                        .getNormalizedString((SingleStatementInfo) statement);
                final int hash = normalizedStatement.hashCode();

                Set<StatementInfo> statements = normalizedStatementHashes.get(hash);
                if (null == statements) {
                    statements = new HashSet<StatementInfo>();
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
