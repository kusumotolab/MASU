package jp.ac.osaka_u.ist.sdl.scdetector;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.MetricsTool;
import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
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

            {
                final Option d = new Option("d", "directory", true, "target directory");
                d.setArgName("directory");
                d.setArgs(1);
                d.setRequired(true);
                options.addOption(d);
            }

            {
                final Option l = new Option("l", "language", true,
                        "programming language of analyzed source code");
                l.setArgName("language");
                l.setArgs(1);
                l.setRequired(true);
                options.addOption(l);
            }

            {
                final Option o = new Option("o", "output", true, "output file");
                o.setArgName("output file");
                o.setArgs(1);
                o.setRequired(true);
                options.addOption(o);
            }

            {
                final Option s = new Option("s", "size", true, "lower size of detected clone");
                s.setArgName("size");
                s.setArgs(1);
                s.setRequired(true);
                options.addOption(s);
            }

            {
                final Option pv = new Option("pv", true, "parameterize variables");
                pv.setArgName("variable parameterization level");
                pv.setArgs(1);
                pv.setRequired(false);
                pv.setType(Integer.class);
                options.addOption(pv);
            }

            {
                final Option pm = new Option("pm", true, "parameterize method invocation");
                pm.setArgName("method invocation parameterization level");
                pm.setArgs(1);
                pm.setRequired(false);
                pm.setType(Integer.class);
                options.addOption(pm);
            }

            {
                final Option pc = new Option("pc", true, "parameterize constructor invocation");
                pc.setArgName("constructor invocation parameterization level");
                pc.setArgs(1);
                pc.setRequired(false);
                pc.setType(Integer.class);
                options.addOption(pc);
            }

            {
                final Option fi = new Option("fi", false,
                        "filtering out code clones included in other code clones");
                fi.setRequired(false);
                options.addOption(fi);
            }

            {
                final Option fj = new Option("fj", false,
                        "filtering out code clones whose start statement and end statement are the same");
                fj.setRequired(false);
                options.addOption(fj);
            }

            {
                final Option fk = new Option("fk", true,
                        "code clones whose elements are overlapped more than the specified threshold");
                fk.setArgName("threshold");
                fk.setArgs(1);
                fk.setRequired(false);
                fk.setType(Integer.class);
                options.addOption(fk);
            }

            final CommandLineParser parser = new PosixParser();
            final CommandLine cmd = parser.parse(options, args);

            Configuration.INSTANCE.setD(cmd.getOptionValue("d"));
            Configuration.INSTANCE.setL(cmd.getOptionValue("l"));
            Configuration.INSTANCE.setO(cmd.getOptionValue("o"));
            Configuration.INSTANCE.setS(Integer.valueOf(cmd.getOptionValue("s")));
            if (cmd.hasOption("pv")) {
                Configuration.INSTANCE.setPV(Integer.valueOf(cmd.getOptionValue("pv")));
            }
            if (cmd.hasOption("pm")) {
                Configuration.INSTANCE.setPM(Integer.valueOf(cmd.getOptionValue("pm")));
            }
            if (cmd.hasOption("pc")) {
                Configuration.INSTANCE.setPC(Integer.valueOf(cmd.getOptionValue("pc")));
            }
            Configuration.INSTANCE.setFI(cmd.hasOption("fi"));
            Configuration.INSTANCE.setFJ(cmd.hasOption("fj"));
            if (cmd.hasOption("fk")) {
                Configuration.INSTANCE.setFK(Integer.valueOf(cmd.getOptionValue("fk")));
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

        // 変数を指定することにより，その変数に対して代入を行っている文を取得するためのハッシュを構築する
        // ただし，条件節については，変数を参照している場合もハッシュを構築する
        for (final TargetMethodInfo method : DataManager.getInstance().getMethodInfoManager()
                .getTargetMethodInfos()) {
            AssignedVariableHashMap.INSTANCE.makeHash(method);
        }

        // ExecutableElement単位で正規化データを構築する
        for (final TargetMethodInfo method : DataManager.getInstance().getMethodInfoManager()
                .getTargetMethodInfos()) {
            NormalizedElementHashMap.INSTANCE.makeHash(method);
        }

        // ConditionInfo から その所有者である ConditionalBlockInfo を取得するためのデータを作成(Forward Slice　用)
        for (final TargetMethodInfo method : DataManager.getInstance().getMethodInfoManager()
                .getTargetMethodInfos()) {
            ConditionHashMap.INSTANCE.makeHash(method);
        }

        // ハッシュ値が同じ2つの文を基点にしてコードクローンを検出
        final Set<ClonePairInfo> clonePairs = new HashSet<ClonePairInfo>();
        for (final Integer hash : NormalizedElementHashMap.INSTANCE.keySet()) {

            final List<ExecutableElementInfo> elements = NormalizedElementHashMap.INSTANCE
                    .get(hash);

            for (int i = 0; i < elements.size(); i++) {
                for (int j = i + 1; j < elements.size(); j++) {

                    final ExecutableElementInfo elementA = elements.get(i);
                    final ExecutableElementInfo elementB = elements.get(j);

                    final ClonePairInfo clonePair = new ClonePairInfo(elementA, elementB);

                    final Set<VariableInfo<?>> usedVariableHashesA = new HashSet<VariableInfo<?>>();
                    final Set<VariableInfo<?>> usedVariableHashesB = new HashSet<VariableInfo<?>>();

                    ProgramSlice.performBackwordSlice(elementA, elementB, clonePair,
                            usedVariableHashesA, usedVariableHashesB);

                    if (Configuration.INSTANCE.getS() <= clonePair.size()) {
                        clonePairs.add(clonePair);
                    }
                }
            }
        }

        final Set<ClonePairInfo> refinedClonePairs = new HashSet<ClonePairInfo>();
        CLONEPAIR: for (final ClonePairInfo clonePair : clonePairs) {
            COUNTERCLONEPAIR: for (final ClonePairInfo counterClonePair : clonePairs) {

                if (clonePair == counterClonePair) {
                    continue COUNTERCLONEPAIR;
                }

                //他のクローンペアに内包されるクローンペアを除去する
                if (Configuration.INSTANCE.getFI()) {
                    if (clonePair.includedBy(counterClonePair)) {
                        continue CLONEPAIR;
                    }
                }

                if (Configuration.INSTANCE.getFJ()) {
                    final SortedSet<ExecutableElementInfo> cloneA = clonePair.getCloneA();
                    final SortedSet<ExecutableElementInfo> cloneB = clonePair.getCloneB();
                    if ((cloneA.first() == cloneB.first()) && (cloneA.last() == cloneB.last())) {
                        continue CLONEPAIR;
                    }
                }

                {
                    final SortedSet<ExecutableElementInfo> cloneA = clonePair.getCloneA();
                    final SortedSet<ExecutableElementInfo> cloneB = clonePair.getCloneB();
                    int sharedElementCount = 0;
                    for (final ExecutableElementInfo element : cloneA) {
                        if (cloneB.contains(element)) {
                            sharedElementCount++;
                        }
                    }
                    if (Configuration.INSTANCE.getFK() < ((100 * 2 * sharedElementCount) / (cloneA
                            .size() + cloneB.size()))) {
                        continue CLONEPAIR;
                    }
                }
            }
            refinedClonePairs.add(clonePair);
        }

        System.out.println(refinedClonePairs.size() + ":" + clonePairs.size());

        try {

            final ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
                    Configuration.INSTANCE.getO()));
            oos.writeObject(refinedClonePairs);

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
