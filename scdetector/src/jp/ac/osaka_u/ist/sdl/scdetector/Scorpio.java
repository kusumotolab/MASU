package jp.ac.osaka_u.ist.sdl.scdetector;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sdl.scdetector.data.ClonePairInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.data.CloneSetInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.data.CodeCloneInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.data.NodePairListInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.PDGController;
import jp.ac.osaka_u.ist.sdl.scdetector.io.XMLWriter;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.CALL_NORMALIZATION;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.CAST_NORMALIZATION;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.CONTROL_FILTER;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.Configuration;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.DEPENDENCY_TYPE;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.LITERAL_NORMALIZATION;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.MERGE;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.OPERATION_NORMALIZATION;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.PDG_TYPE;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.REFERENCE_NORMALIZATION;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.SLICE_TYPE;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.SMALL_METHOD;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.VARIABLE_NORMALIZATION;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.VERBOSE;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.DefaultCFGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.main.MetricsTool;
import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageListener;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePool;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.DefaultPDGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.IPDGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.InterProceduralPDG;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.InterproceduralEdgeBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.IntraProceduralPDG;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.PDG;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;


/**
 * プログラム依存グラフの同形なサブグラフ部分をコードクローンとして検出するプログラム 検出対象は引数で与えられる．
 * 
 * @author higo
 */
public class Scorpio extends MetricsTool {

    public static final String ID = "SCORPIO";

    public static void main(String[] args) {

        try {

            // コマンドライン引数を処理
            final Options options = new Options();

            {
                final Option b = new Option("b", "libraries", true,
                        "specify libraries (.jar file or .class file or directory that contains .jar and .class files)");
                b.setArgName("libraries");
                b.setArgs(1);
                b.setRequired(false);
                options.addOption(b);
            }

            {
                final Option c = new Option("c", "count", true,
                        "count for filtering out repeated statements");
                c.setArgName("count");
                c.setArgs(1);
                c.setRequired(false);
                options.addOption(c);
            }

            {
                final Option d = new Option("d", "directory", true,
                        "specify target directories (separate with comma \',\' if you specify multiple directories");
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
                final Option e = new Option("e", "merge", true, "merge");
                e.setArgName("merge");
                e.setArgs(1);
                e.setRequired(false);
                options.addOption(e);
            }

            {
                final Option m = new Option("m", "method", true, "small method");
                m.setArgName("method");
                m.setArgs(1);
                m.setRequired(false);
                options.addOption(m);
            }

            {
                final Option o = new Option("o", "output", true, "output file");
                o.setArgName("output file");
                o.setArgs(1);
                o.setRequired(true);
                options.addOption(o);
            }

            {
                final Option p = new Option("p", "pdg", true, "pdg type");
                p.setArgName("pdg");
                p.setArgs(1);
                p.setRequired(false);
                options.addOption(p);
            }

            {
                final Option q = new Option("q", "dependency", true, "dependency type");
                q.setArgName("dependency");
                q.setArgs(1);
                q.setRequired(false);
                options.addOption(q);
            }

            {
                final Option s = new Option("s", "size", true, "lower size of detected clone");
                s.setArgName("size");
                s.setArgs(1);
                s.setRequired(false);
                options.addOption(s);
            }

            {
                final Option t = new Option("t", "slice", true, "slice type");
                t.setArgName("slice");
                t.setArgs(1);
                t.setRequired(false);
                options.addOption(t);
            }

            {
                final Option u = new Option("u", "control", true, "control node");
                u.setArgName("control");
                u.setArgs(1);
                u.setRequired(false);
                options.addOption(u);
            }

            {
                final Option v = new Option("v", "verbose", true, "verbose output");
                v.setArgName("verbose");
                v.setArgs(1);
                v.setRequired(false);
                options.addOption(v);
            }

            {
                final Option w = new Option("w", "thread", true, "number of threads");
                w.setArgName("thread");
                w.setArgs(1);
                w.setRequired(false);
                options.addOption(w);
            }

            {
                final Option x = new Option("x", "distance", true, "distance of data dependency");
                x.setArgName("distance");
                x.setArgs(1);
                x.setRequired(false);
                options.addOption(x);
            }

            {
                final Option y = new Option("y", "distance", true, "distance of control dependency");
                y.setArgName("distance");
                y.setArgs(1);
                y.setRequired(false);
                options.addOption(y);
            }

            {
                final Option z = new Option("z", "distance", true,
                        "distance of execution dependency");
                z.setArgName("distance");
                z.setArgs(1);
                z.setRequired(false);
                options.addOption(z);
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
                final Option pi = new Option("pi", true, "parameterize invocations");
                pi.setArgName("invocation parameterization level");
                pi.setArgs(1);
                pi.setRequired(false);
                pi.setType(Integer.class);
                options.addOption(pi);
            }

            {
                final Option po = new Option("po", true, "parameterize operations");
                po.setArgName("operation parameterization level");
                po.setArgs(1);
                po.setRequired(false);
                po.setType(Integer.class);
                options.addOption(po);
            }

            {
                final Option pl = new Option("pl", true, "parameterize literals");
                pl.setArgName("literal parameterization level");
                pl.setArgs(1);
                pl.setRequired(false);
                pl.setType(Integer.class);
                options.addOption(pl);
            }

            {
                final Option pc = new Option("pc", true, "parameterize casts");
                pc.setArgName("cast parameterization level");
                pc.setArgs(1);
                pc.setRequired(false);
                pc.setType(Integer.class);
                options.addOption(pc);
            }

            {
                final Option pr = new Option("pr", false, "parameterize class references");
                pr.setRequired(false);
                options.addOption(pr);
            }

            final CommandLineParser parser = new PosixParser();
            final CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("b")) {
                final StringTokenizer tokenizer = new StringTokenizer(cmd.getOptionValue("b"), ",");
                while (tokenizer.hasMoreElements()) {
                    final String library = tokenizer.nextToken();
                    Settings.getInstance().addLibrary(library);
                }
            }
            if (cmd.hasOption("c")) {
                Configuration.INSTANCE.setC(Integer.valueOf(cmd.getOptionValue("c")));
            }
            {
                final StringTokenizer tokenizer = new StringTokenizer(cmd.getOptionValue("d"), ",");
                while (tokenizer.hasMoreElements()) {
                    final String directory = tokenizer.nextToken();
                    Configuration.INSTANCE.addD(directory);
                }
            }
            if (cmd.hasOption("e")) {
                final String merge = cmd.getOptionValue("e");
                if (merge.equalsIgnoreCase("yes")) {
                    Configuration.INSTANCE.setE(MERGE.TRUE);
                } else if (merge.equalsIgnoreCase("no")) {
                    Configuration.INSTANCE.setE(MERGE.FALSE);
                } else {
                    err.println("Unknown option : " + merge);
                    err.println("\"-m\" option must have \"yes\" or \"no\"");
                    System.exit(0);
                }
            }
            Configuration.INSTANCE.setL(cmd.getOptionValue("l"));
            if (cmd.hasOption("m")) {
                final String smallmethod = cmd.getOptionValue("m");
                if (smallmethod.equalsIgnoreCase("hashed")) {
                    Configuration.INSTANCE.setM(SMALL_METHOD.HASHED);
                } else if (smallmethod.equalsIgnoreCase("unhashed")) {
                    Configuration.INSTANCE.setM(SMALL_METHOD.UNHASHED);
                } else {
                    err.println("Unknown option : " + smallmethod);
                    err.println("\"-m\" option must have \"hashed\" or \"unhashed\"");
                    System.exit(0);
                }
            }
            Configuration.INSTANCE.setO(cmd.getOptionValue("o"));
            if (cmd.hasOption("p")) {
                final String pdg = cmd.getOptionValue("p");
                if (pdg.equalsIgnoreCase("intra")) {
                    Configuration.INSTANCE.setP(PDG_TYPE.INTRA);
                } else if (pdg.equalsIgnoreCase("inter")) {
                    Configuration.INSTANCE.setP(PDG_TYPE.INTER);
                } else {
                    err.println("Unknown option : " + pdg);
                    err.println("\"-p\" option must have \"intra\" or \"inter\"");
                    System.exit(0);
                }
            }
            if (cmd.hasOption("q")) {
                Configuration.INSTANCE.resetQ();
                final StringTokenizer tokenizer = new StringTokenizer(cmd.getOptionValue("q"), ",");
                while (tokenizer.hasMoreTokens()) {
                    final String dependency = tokenizer.nextToken();
                    if (dependency.equalsIgnoreCase("data")) {
                        Configuration.INSTANCE.addQ(DEPENDENCY_TYPE.DATA);
                    } else if (dependency.equalsIgnoreCase("control")) {
                        Configuration.INSTANCE.addQ(DEPENDENCY_TYPE.CONTROL);
                    } else if (dependency.equalsIgnoreCase("execution")) {
                        Configuration.INSTANCE.addQ(DEPENDENCY_TYPE.EXECUTION);
                    } else {
                        err.println("Unknown option : " + dependency);
                        err
                                .println("\"-q\" option must have \"data\", \"control\", or \"execution\"");
                        System.exit(0);
                    }
                }

            }
            if (cmd.hasOption("s")) {
                Configuration.INSTANCE.setS(Integer.valueOf(cmd.getOptionValue("s")));
            }
            if (cmd.hasOption("t")) {
                Configuration.INSTANCE.resetT();
                final StringTokenizer tokenizer = new StringTokenizer(cmd.getOptionValue("t"), ",");
                while (tokenizer.hasMoreTokens()) {
                    final String slice = tokenizer.nextToken();
                    if (slice.equalsIgnoreCase("backward")) {
                        Configuration.INSTANCE.addT(SLICE_TYPE.BACKWARD);
                    } else if (slice.equalsIgnoreCase("forward")) {
                        Configuration.INSTANCE.addT(SLICE_TYPE.FORWARD);
                    } else {
                        err.println("Unknown option : " + slice);
                        err.println("\"-t\" option must have \"backward\", or \"forward\"");
                        System.exit(0);
                    }
                }
            }
            if (cmd.hasOption("u")) {
                final String control = cmd.getOptionValue("u");
                if (control.equalsIgnoreCase("yes")) {
                    Configuration.INSTANCE.setU(CONTROL_FILTER.USE);
                } else if (control.equalsIgnoreCase("no")) {
                    Configuration.INSTANCE.setU(CONTROL_FILTER.NO_USE);
                } else {
                    err.println("Unknown option : " + control);
                    err.println("\"-u\" option must have \"yes\" or \"no\"");
                    System.exit(0);
                }
            }
            if (cmd.hasOption("s")) {
                Configuration.INSTANCE.setS(Integer.valueOf(cmd.getOptionValue("s")));
            }
            if (cmd.hasOption("v")) {
                final String verbose = cmd.getOptionValue("v");
                if (verbose.equalsIgnoreCase("yes")) {
                    Configuration.INSTANCE.setV(VERBOSE.TRUE);
                } else if (verbose.equalsIgnoreCase("no")) {
                    Configuration.INSTANCE.setV(VERBOSE.FALSE);
                } else {
                    err.println("Unknown option : " + verbose);
                    err.println("\"-v\" option must have \"yes\" or \"no\"");
                    System.exit(0);
                }
            }
            if (cmd.hasOption("w")) {
                Configuration.INSTANCE.setW(Integer.valueOf(cmd.getOptionValue("w")));
            }
            if (cmd.hasOption("x")) {
                Configuration.INSTANCE.setX(Integer.valueOf(cmd.getOptionValue("x")));
            }
            if (cmd.hasOption("y")) {
                Configuration.INSTANCE.setY(Integer.valueOf(cmd.getOptionValue("y")));
            }
            if (cmd.hasOption("z")) {
                Configuration.INSTANCE.setZ(Integer.valueOf(cmd.getOptionValue("z")));
            }
            if (cmd.hasOption("pv")) {
                final String text = cmd.getOptionValue("pv");
                if (text.equalsIgnoreCase("no")) {
                    Configuration.INSTANCE.setPV(VARIABLE_NORMALIZATION.NO);
                } else if (text.equalsIgnoreCase("type")) {
                    Configuration.INSTANCE.setPV(VARIABLE_NORMALIZATION.TYPE);
                } else if (text.equalsIgnoreCase("all")) {
                    Configuration.INSTANCE.setPV(VARIABLE_NORMALIZATION.ALL);
                } else {
                    err.println("Unknown option : " + text);
                    err.println("\"-pv\" option must have \"no\", \"type\", or \"all\"");
                    System.exit(0);
                }
            }
            if (cmd.hasOption("pi")) {
                final String text = cmd.getOptionValue("pi");
                if (text.equalsIgnoreCase("no")) {
                    Configuration.INSTANCE.setPI(CALL_NORMALIZATION.NO);
                } else if (text.equalsIgnoreCase("type_with_arg")) {
                    Configuration.INSTANCE.setPI(CALL_NORMALIZATION.TYPE_WITH_ARG);
                } else if (text.equalsIgnoreCase("type_without_arg")) {
                    Configuration.INSTANCE.setPI(CALL_NORMALIZATION.TYPE_WITHOUT_ARG);
                } else if (text.equalsIgnoreCase("all")) {
                    Configuration.INSTANCE.setPI(CALL_NORMALIZATION.ALL);
                } else {
                    err.println("Unknown option : " + text);
                    err
                            .println("\"-pi\" option must have \"no\", \"type_with_arg\", \"type_without_arg\", or \"all\"");
                    System.exit(0);
                }
            }
            if (cmd.hasOption("po")) {
                final String text = cmd.getOptionValue("po");
                if (text.equalsIgnoreCase("no")) {
                    Configuration.INSTANCE.setPO(OPERATION_NORMALIZATION.NO);
                } else if (text.equalsIgnoreCase("type")) {
                    Configuration.INSTANCE.setPO(OPERATION_NORMALIZATION.TYPE);
                } else if (text.equalsIgnoreCase("all")) {
                    Configuration.INSTANCE.setPO(OPERATION_NORMALIZATION.ALL);
                } else {
                    err.println("Unknown option : " + text);
                    err.println("\"-po\" option must have \"no\", \"type\", or \"all\"");
                    System.exit(0);
                }
            }
            if (cmd.hasOption("pl")) {
                final String text = cmd.getOptionValue("pl");
                if (text.equalsIgnoreCase("no")) {
                    Configuration.INSTANCE.setPL(LITERAL_NORMALIZATION.NO);
                } else if (text.equalsIgnoreCase("type")) {
                    Configuration.INSTANCE.setPL(LITERAL_NORMALIZATION.TYPE);
                } else if (text.equalsIgnoreCase("all")) {
                    Configuration.INSTANCE.setPL(LITERAL_NORMALIZATION.ALL);
                } else {
                    err.println("Unknown option : " + text);
                    err.println("\"-pl\" option must have \"no\", \"type\", or \"all\"");
                    System.exit(0);
                }
            }
            if (cmd.hasOption("pc")) {
                final String text = cmd.getOptionValue("pc");
                if (text.equalsIgnoreCase("no")) {
                    Configuration.INSTANCE.setPC(CAST_NORMALIZATION.NO);
                } else if (text.equalsIgnoreCase("type")) {
                    Configuration.INSTANCE.setPC(CAST_NORMALIZATION.TYPE);
                } else if (text.equalsIgnoreCase("all")) {
                    Configuration.INSTANCE.setPC(CAST_NORMALIZATION.ALL);
                } else {
                    err.println("Unknown option : " + text);
                    err.println("\"-pc\" option must have \"no\", \"type\", or \"all\"");
                    System.exit(0);
                }
            }
            if (cmd.hasOption("pr")) {
                final String text = cmd.getOptionValue("pr");
                if (text.equalsIgnoreCase("no")) {
                    Configuration.INSTANCE.setPR(REFERENCE_NORMALIZATION.NO);
                } else if (text.equalsIgnoreCase("all")) {
                    Configuration.INSTANCE.setPR(REFERENCE_NORMALIZATION.ALL);
                } else {
                    err.println("Unknown option : " + text);
                    err.println("\"-pr\" option must have \"no\" or \"all\"");
                    System.exit(0);
                }
            }

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

        try {

            // 解析用設定
            Settings.getInstance().setLanguage(Configuration.INSTANCE.getL());
            for (final String directory : Configuration.INSTANCE.getD()) {
                Settings.getInstance().addTargetDirectory(directory);
            }
            Settings.getInstance().setVerbose(true);

            // 情報表示用設定
            final Class<?> metricstool = MetricsTool.class;
            final Field out = metricstool.getDeclaredField("out");
            out.setAccessible(true);
            out.set(null, new DefaultMessagePrinter(new MessageSource() {
                public String getMessageSourceName() {
                    return "scorpio";
                }
            }, MESSAGE_TYPE.OUT));
            final Field err = metricstool.getDeclaredField("err");
            err.setAccessible(true);
            err.set(null, new DefaultMessagePrinter(new MessageSource() {
                public String getMessageSourceName() {
                    return "scorpio";
                }
            }, MESSAGE_TYPE.ERROR));
            if (Configuration.INSTANCE.getV().isVerbose()) {
                MessagePool.getInstance(MESSAGE_TYPE.OUT).addMessageListener(new MessageListener() {
                    public void messageReceived(MessageEvent event) {
                        System.out.print(event.getSource().getMessageSourceName() + " > "
                                + event.getMessage());
                    }
                });
                MessagePool.getInstance(MESSAGE_TYPE.ERROR).addMessageListener(
                        new MessageListener() {
                            public void messageReceived(MessageEvent event) {
                                System.err.print(event.getSource().getMessageSourceName() + " > "
                                        + event.getMessage());
                            }
                        });
            }

        } catch (NoSuchFieldException e) {
            System.out.println(e.getMessage());
        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
        }

        final long start = System.nanoTime();

        // 対象ディレクトリ以下のJavaファイルを登録し，解析
        final Scorpio scorpio = new Scorpio();
        scorpio.analyzeLibraries();
        scorpio.readTargetFiles();
        scorpio.analyzeTargetFiles();

        int totalline = 0;
        for (final FileInfo file : DataManager.getInstance().getFileInfoManager().getFileInfos()) {
            totalline += file.getLOC();
        }

        // PDGのノード集合を定義
        out.println("buildeing PDGs ...");
        final IPDGNodeFactory pdgNodeFactory = new DefaultPDGNodeFactory();
        final boolean data = Configuration.INSTANCE.getQ().contains(DEPENDENCY_TYPE.DATA);
        final boolean control = Configuration.INSTANCE.getQ().contains(DEPENDENCY_TYPE.CONTROL);
        final boolean execution = Configuration.INSTANCE.getQ().contains(DEPENDENCY_TYPE.EXECUTION);
        final int dataDistance = Configuration.INSTANCE.getX();
        final int controlDistance = Configuration.INSTANCE.getY();
        final int executionDistance = Configuration.INSTANCE.getZ();
        switch (Configuration.INSTANCE.getP()) {

        case INTRA: // 各メソッドのPDGを構築
            final List<Thread> threads = new LinkedList<Thread>();
            for (final TargetMethodInfo method : DataManager.getInstance().getMethodInfoManager()
                    .getTargetMethodInfos()) {

                final IntraProceduralPDG pdg = new IntraProceduralPDG(method, pdgNodeFactory,
                        new DefaultCFGNodeFactory(), data, control, execution, true, dataDistance,
                        controlDistance, executionDistance);
                PDGController.getInstance(Scorpio.ID).put(method, pdg);

                /*
                 * final Thread thread = new Thread(new
                 * PDGBuildingThread(method, pdgNodeFactory, cfgNodeFactory,
                 * data, control, execution, distance)); threads.add(thread);
                 * thread.start();
                 * 
                 * while (Configuration.INSTANCE.getW() < Thread.activeCount())
                 * ;
                 */
            }

            // コンストラクタのPDGを構築
            for (final TargetConstructorInfo constructor : DataManager.getInstance()
                    .getMethodInfoManager().getTargetConstructorInfos()) {

                final IntraProceduralPDG pdg = new IntraProceduralPDG(constructor, pdgNodeFactory,
                        new DefaultCFGNodeFactory(), data, control, execution, true, dataDistance,
                        controlDistance, executionDistance);
                PDGController.getInstance(Scorpio.ID).put(constructor, pdg);

                /*
                 * final Thread thread = new Thread(new
                 * PDGBuildingThread(constructor, pdgNodeFactory,
                 * cfgNodeFactory, data, control, execution, distance));
                 * threads.add(thread); thread.start();
                 * 
                 * while (Configuration.INSTANCE.getW() < Thread.activeCount())
                 * ;
                 */
            }

            // 全てのスレッドが終わるのを待つ
            for (final Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            break;
        case INTER:
            // 各メソッドのPDGを構築
            for (final TargetMethodInfo method : DataManager.getInstance().getMethodInfoManager()
                    .getTargetMethodInfos()) {
                final InterProceduralPDG pdg = new InterProceduralPDG(method, pdgNodeFactory,
                        new DefaultCFGNodeFactory(), data, control, execution);
                PDGController.getInstance(Scorpio.ID).put(method, pdg);
            }
            // コンストラクタのPDGを構築
            for (final TargetConstructorInfo constructor : DataManager.getInstance()
                    .getMethodInfoManager().getTargetConstructorInfos()) {
                final InterProceduralPDG pdg = new InterProceduralPDG(constructor, pdgNodeFactory,
                        new DefaultCFGNodeFactory(), data, control, execution);

                PDGController.getInstance(Scorpio.ID).put(constructor, pdg);
            }
            // メソッド呼び出し依存関係を構築
            for (final PDG pdg : PDGController.getInstance(Scorpio.ID).getPDGs()) {
                InterProceduralPDG interPDG = (InterProceduralPDG) pdg;
                (new InterproceduralEdgeBuilder(interPDG)).addEdges();
            }
            break;
        default:
            assert false : "Here shouldn't be reached!";
        }

        // 頂点集約が指定されている場合は，PDGを変換する
        if (Configuration.INSTANCE.getE().equals(MERGE.TRUE)) {
            for (final PDG pdg : PDGController.getInstance(Scorpio.ID).getPDGs()) {
                final PDGNode<?> enterNode = pdg.getEnterNodes().first();
            }
        }

        // PDGノードのハッシュデータを構築する
        out.println("constructing PDG nodes hashtable ...");
        final SortedMap<Integer, List<PDGNode<?>>> pdgNodeMap = new TreeMap<Integer, List<PDGNode<?>>>();

        ALLNODE: for (final PDGNode<?> pdgNode : pdgNodeFactory.getAllNodes()) {

            // 小さいメソッドは登録しない
            switch (Configuration.INSTANCE.getM()) {
            case UNHASHED:
                final PDG pdg = PDGController.getInstance(Scorpio.ID).getPDG(pdgNode);
                if (pdg.getNumberOfNodes() < Configuration.INSTANCE.getS()) {
                    continue ALLNODE;
                }
                break;
            default:
            }

            final ExecutableElementInfo element = pdgNode.getCore();
            final int hash = Conversion.getNormalizedString(element).hashCode();
            List<PDGNode<?>> pdgNodeList = pdgNodeMap.get(hash);
            if (null == pdgNodeList) {
                pdgNodeList = new ArrayList<PDGNode<?>>();
                pdgNodeMap.put(hash, pdgNodeList);
            }
            pdgNodeList.add(pdgNode);
        }

        // ハッシュ値が同じ2つのStatementInfoを基点にしてコードクローンを検出
        out.println("detecting code clones from PDGs ... (the number of PDG nodes is "
                + pdgNodeFactory.getAllNodes().size() + ")");
        final Map<TwoClassHash, SortedSet<ClonePairInfo>> clonePairs = new HashMap<TwoClassHash, SortedSet<ClonePairInfo>>();
        final List<Thread> threads = new LinkedList<Thread>();
        final NodePairListInfo nodePairList = new NodePairListInfo(pdgNodeMap.values());

        for (int threadsNumber = 1; threadsNumber <= Configuration.INSTANCE.getW(); threadsNumber++) {
            final Thread thread = new Thread(new DetectionThread(threadsNumber, nodePairList,
                    clonePairs));
            threads.add(thread);
            thread.start();
        }

        // 全てのスレッドが終わるのを待つ
        for (final Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        threads.clear();

        out.println("filtering out uninterested clone pairs ...");

        final Set<ClonePairInfo> refinedClonePairs = Collections
                .synchronizedSet(new TreeSet<ClonePairInfo>());
        for (final SortedSet<ClonePairInfo> pairs : clonePairs.values()) {

            final Thread thread = new Thread(new CloneFilteringThread(pairs, refinedClonePairs));
            threads.add(thread);
            thread.start();

            while (Configuration.INSTANCE.getW() < Thread.activeCount())
                ;
        }
        // 全てのスレッドが終わるのを待つ
        for (final Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        threads.clear();

        out.println("converting clone pairs to clone sets ...");
        final SortedSet<CloneSetInfo> cloneSets = new TreeSet<CloneSetInfo>();
        {

            final Map<CodeCloneInfo, CloneSetInfo> cloneSetBag = new HashMap<CodeCloneInfo, CloneSetInfo>();

            for (final ClonePairInfo clonePair : refinedClonePairs) {

                final CodeCloneInfo cloneA = clonePair.getCodeFragmentA();
                final CodeCloneInfo cloneB = clonePair.getCodeFragmentB();

                final CloneSetInfo cloneSetA = cloneSetBag.get(cloneA);
                final CloneSetInfo cloneSetB = cloneSetBag.get(cloneB);

                // コード片A，Bともすでに登録されている場合
                if ((null != cloneSetA) && (null != cloneSetB)) {

                    // A と Bの所属するクローンセットが違う場合は，統合する
                    if (cloneSetA != cloneSetB) {
                        final CloneSetInfo cloneSetC = new CloneSetInfo();
                        cloneSetC.addAll(cloneSetA.getCodeClones());
                        cloneSetC.addAll(cloneSetB.getCodeClones());

                        for (final CodeCloneInfo codeFragment : cloneSetA.getCodeClones()) {
                            cloneSetBag.remove(codeFragment);
                        }
                        for (final CodeCloneInfo codeFragment : cloneSetB.getCodeClones()) {
                            cloneSetBag.remove(codeFragment);
                        }

                        for (final CodeCloneInfo codeFragment : cloneSetC.getCodeClones()) {
                            cloneSetBag.put(codeFragment, cloneSetC);
                        }
                    }

                } else if ((null != cloneSetA) && (null == cloneSetB)) {

                    cloneSetA.add(cloneB);
                    cloneSetBag.put(cloneB, cloneSetA);

                } else if ((null == cloneSetA) && (null != cloneSetB)) {

                    cloneSetB.add(cloneA);
                    cloneSetBag.put(cloneA, cloneSetB);

                } else {

                    final CloneSetInfo cloneSet = new CloneSetInfo();
                    cloneSet.add(cloneA);
                    cloneSet.add(cloneB);

                    cloneSetBag.put(cloneA, cloneSet);
                    cloneSetBag.put(cloneB, cloneSet);

                }
            }

            for (final CloneSetInfo cloneSet : cloneSetBag.values()) {
                if (1 < cloneSet.getNumberOfCodeclones()) {
                    cloneSets.add(cloneSet);
                }
            }
        }

        final XMLWriter writer = new XMLWriter(Configuration.INSTANCE.getO(), DataManager
                .getInstance().getFileInfoManager().getFileInfos(), cloneSets, pdgNodeFactory);
        /*
         * final BellonWriter writer = new
         * BellonWriter(Configuration.INSTANCE.getO(), DataManager
         * 
         * .getInstance().getFileInfoManager().getFileInfos(), cloneSets);
         */
        writer.write();
        writer.close();
        /*
         * out.println(DetectionThread.numberOfPairs + " : " +
         * DetectionThread.numberOfComparion);
         */
        out.println("successifully finished.");

        final long time = System.nanoTime() - start;
        out.println("elapsed time: " + time / (float) 1000000000);
    }
}