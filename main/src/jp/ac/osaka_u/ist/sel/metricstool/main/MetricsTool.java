package jp.ac.osaka_u.ist.sel.metricstool.main;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.ClassMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.FieldMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.FileMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MethodMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricNotRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalClauseInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.InnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.InstanceInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.JavaPredefinedModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StaticInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFile;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFileManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetInnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.JavaUnresolvedExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.JavaUnresolvedExternalFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.JavaUnresolvedExternalMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedInstanceInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedStaticInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.CSVClassMetricsWriter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.CSVFileMetricsWriter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.CSVMethodMetricsWriter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageListener;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePool;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.asm.JavaByteCodeNameResolver;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.asm.JavaByteCodeParser;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.DefaultPluginLauncher;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.PluginLauncher;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.PluginManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin.PluginInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader.DefaultPluginLoader;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader.PluginLoadException;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.objectweb.asm.ClassReader;


/**
 * 
 * @author higo
 * 
 * MetricsToolのメインクラス． 現在は仮実装．
 * 
 * since 2006.11.12
 * 
 */
public class MetricsTool {

    /**
     * 
     * @param args 対象ファイルのファイルパス
     * 
     * 現在仮実装． 対象ファイルのデータを格納した後，構文解析を行う．
     */
    public static void main(String[] args) {

        initSecurityManager();

        // 情報表示用のリスナを作成
        final MessageListener outListener = new MessageListener() {
            public void messageReceived(MessageEvent event) {
                System.out.print(event.getSource().getMessageSourceName() + " > "
                        + event.getMessage());
            }
        };
        final MessageListener errListener = new MessageListener() {
            public void messageReceived(MessageEvent event) {
                System.out.print(event.getSource().getMessageSourceName() + " > "
                        + event.getMessage());
            }
        };
        MessagePool.getInstance(MESSAGE_TYPE.OUT).addMessageListener(outListener);
        MessagePool.getInstance(MESSAGE_TYPE.ERROR).addMessageListener(errListener);

        final Options options = new Options();

        {
            final Option h = new Option("h", "help", false, "display usage");
            h.setRequired(false);
            options.addOption(h);
        }

        {
            final Option v = new Option("v", "verbose", false, "output progress verbosely");
            v.setRequired(false);
            options.addOption(v);
        }

        {
            final Option d = new Option("d", "directores", true,
                    "specify target directories (separate with comma \',\' if you specify multiple directories");
            d.setArgName("directories");
            d.setArgs(1);
            d.setRequired(false);
            options.addOption(d);
        }

        {
            final Option i = new Option(
                    "i",
                    "input",
                    true,
                    "specify the input that contains the list of target files (separate with comma \',\' if you specify multiple inputs)");
            i.setArgName("input");
            i.setArgs(1);
            i.setRequired(false);
            options.addOption(i);
        }

        {
            final Option l = new Option("l", "language", true, "specify programming language");
            l.setArgName("input");
            l.setArgs(1);
            l.setRequired(false);
            options.addOption(l);
        }

        {
            final Option m = new Option("m", "metrics", true,
                    "specify measured metrics with comma separeted format (e.g., -m rfc,dit,lcom)");
            m.setArgName("metrics");
            m.setArgs(1);
            m.setRequired(false);
            options.addOption(m);
        }

        {
            final Option F = new Option("F", "FileMetricsFile", true,
                    "specify file that measured FILE metrics were stored into");
            F.setArgName("file metrics file");
            F.setArgs(1);
            F.setRequired(false);
            options.addOption(F);
        }

        {
            final Option C = new Option("C", "ClassMetricsFile", true,
                    "specify file that measured CLASS metrics were stored into");
            C.setArgName("class metrics file");
            C.setArgs(1);
            C.setRequired(false);
            options.addOption(C);
        }

        {
            final Option M = new Option("M", "MethodMetricsFile", true,
                    "specify file that measured METHOD metrics were stored into");
            M.setArgName("method metrics file");
            M.setArgs(1);
            M.setRequired(false);
            options.addOption(M);
        }

        {
            final Option A = new Option("A", "AttributeMetricsFile", true,
                    "specify file that measured ATTRIBUTE metrics were stored into");
            A.setArgName("attribute metrics file");
            A.setArgs(1);
            A.setRequired(false);
            options.addOption(A);
        }

        {
            final Option s = new Option("s", "AnalyzeStatement", false,
                    "specify this option if you don't need statement information");
            s.setRequired(false);
            options.addOption(s);
        }

        {
            final Option b = new Option("b", "libraries", true,
                    "specify libraries (.jar file or .class file or directory that contains .jar and .class files)");
            b.setArgName("libraries");
            b.setArgs(1);
            b.setRequired(false);
            options.addOption(b);
        }

        {
            final Option t = new Option("t", "threads", true,
                    "specify thread number used for multi-thread processings");
            t.setArgName("number");
            t.setArgs(1);
            t.setRequired(false);
            options.addOption(t);
        }

        final MetricsTool metricsTool = new MetricsTool();

        try {

            final CommandLineParser parser = new PosixParser();
            final CommandLine cmd = parser.parse(options, args);

            // "-h"が指定されている場合はヘルプを表示して終了
            // このとき，他のオプションは全て無視される
            if (cmd.hasOption("h") || (0 == args.length)) {
                final HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("MetricsTool", options, true);

                // -l で言語が指定されていない場合は，解析可能言語一覧を表示
                if (!cmd.hasOption("l")) {
                    err.println("Available languages;");
                    for (final LANGUAGE language : LANGUAGE.values()) {
                        err.println("\t" + language.getName() + ": can be specified with term \""
                                + language.getIdentifierName() + "\"");
                    }

                    // -l で言語が指定されている場合は，そのプログラミング言語で使用可能なメトリクス一覧を表示
                } else {
                    Settings.getInstance().setLanguage(cmd.getOptionValue("l"));
                    err.println("Available metrics for "
                            + Settings.getInstance().getLanguage().getName());
                    metricsTool.loadPlugins(Settings.getInstance().getMetrics());
                    for (final AbstractPlugin plugin : DataManager.getInstance().getPluginManager()
                            .getPlugins()) {
                        final PluginInfo pluginInfo = plugin.getPluginInfo();
                        if (pluginInfo.isMeasurable(Settings.getInstance().getLanguage())) {
                            err.println("\t" + pluginInfo.getMetricName());
                        }
                    }
                }

                System.exit(0);
            }

            Settings.getInstance().setVerbose(cmd.hasOption("v"));
            if (cmd.hasOption("d")) {
                final StringTokenizer tokenizer = new StringTokenizer(cmd.getOptionValue("d"), ",");
                while (tokenizer.hasMoreElements()) {
                    final String directory = tokenizer.nextToken();
                    Settings.getInstance().addTargetDirectory(directory);
                }
            }
            if (cmd.hasOption("i")) {
                final StringTokenizer tokenizer = new StringTokenizer(cmd.getOptionValue("i"), ",");
                while (tokenizer.hasMoreElements()) {
                    final String listFile = tokenizer.nextToken();
                    Settings.getInstance().addListFile(listFile);
                }
            }
            Settings.getInstance().setLanguage(cmd.getOptionValue("l"));
            if (cmd.hasOption("m")) {
                Settings.getInstance().setMetrics(cmd.getOptionValue("m"));
            }
            if (cmd.hasOption("F")) {
                Settings.getInstance().setFileMetricsFile(cmd.getOptionValue("F"));
            }
            if (cmd.hasOption("C")) {
                Settings.getInstance().setClassMetricsFile(cmd.getOptionValue("C"));
            }
            if (cmd.hasOption("M")) {
                Settings.getInstance().setMethodMetricsFile(cmd.getOptionValue("M"));
            }
            if (cmd.hasOption("A")) {
                Settings.getInstance().setFieldMetricsFile(cmd.getOptionValue("A"));
            }
            Settings.getInstance().setStatement(!cmd.hasOption("s"));
            if (cmd.hasOption("b")) {
                final StringTokenizer tokenizer = new StringTokenizer(cmd.getOptionValue("b"), ",");
                while (tokenizer.hasMoreElements()) {
                    final String library = tokenizer.nextToken();
                    Settings.getInstance().addLibrary(library);
                }
            }
            if (cmd.hasOption("t")) {
                Settings.getInstance().setThreadNumber(Integer.parseInt(cmd.getOptionValue("t")));
            }

            metricsTool.loadPlugins(Settings.getInstance().getMetrics());

            // コマンドライン引数が正しいかどうかチェックする
            {
                // -d と -i のどちらも指定されているのは不正
                if (!cmd.hasOption("d") && !cmd.hasOption("l")) {
                    err.println("-d and/or -i must be specified in the analysis mode!");
                    System.exit(0);
                }

                // 言語が指定されなかったのは不正
                if (!cmd.hasOption("l")) {
                    err.println("-l must be specified for analysis");
                    System.exit(0);
                }

                {
                    // ファイルメトリクスを計測する場合は -F オプションが指定されていなければならない
                    if ((0 < DataManager.getInstance().getPluginManager().getFileMetricPlugins()
                            .size())
                            && !cmd.hasOption("F")) {
                        err.println("-F must be specified for file metrics!");
                        System.exit(0);
                    }

                    // クラスメトリクスを計測する場合は -C オプションが指定されていなければならない
                    if ((0 < DataManager.getInstance().getPluginManager().getClassMetricPlugins()
                            .size())
                            && !cmd.hasOption("C")) {
                        err.println("-C must be specified for class metrics!");
                        System.exit(0);
                    }
                    // メソッドメトリクスを計測する場合は -M オプションが指定されていなければならない
                    if ((0 < DataManager.getInstance().getPluginManager().getMethodMetricPlugins()
                            .size())
                            && !cmd.hasOption("M")) {
                        err.println("-M must be specified for method metrics!");
                        System.exit(0);
                    }

                    // フィールドメトリクスを計測する場合は -A オプションが指定されていなければならない
                    if ((0 < DataManager.getInstance().getPluginManager().getFieldMetricPlugins()
                            .size())
                            && !cmd.hasOption("A")) {
                        err.println("-A must be specified for field metrics!");
                        System.exit(0);
                    }
                }

                {
                    // ファイルメトリクスを計測しないのに -F　オプションが指定されている場合は無視する旨を通知
                    if ((0 == DataManager.getInstance().getPluginManager().getFileMetricPlugins()
                            .size())
                            && cmd.hasOption("F")) {
                        err.println("No file metric is specified. -F is ignored.");
                    }

                    // クラスメトリクスを計測しないのに -C　オプションが指定されている場合は無視する旨を通知
                    if ((0 == DataManager.getInstance().getPluginManager().getClassMetricPlugins()
                            .size())
                            && cmd.hasOption("C")) {
                        err.println("No class metric is specified. -C is ignored.");
                    }

                    // メソッドメトリクスを計測しないのに -M　オプションが指定されている場合は無視する旨を通知
                    if ((0 == DataManager.getInstance().getPluginManager().getMethodMetricPlugins()
                            .size())
                            && cmd.hasOption("M")) {
                        err.println("No method metric is specified. -M is ignored.");
                    }

                    // フィールドメトリクスを計測しないのに -A　オプションが指定されている場合は無視する旨を通知
                    if ((0 == DataManager.getInstance().getPluginManager().getFieldMetricPlugins()
                            .size())
                            && cmd.hasOption("A")) {
                        err.println("No field metric is specified. -A is ignored.");
                    }
                }
            }

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

        final long start = System.nanoTime();

        metricsTool.analyzeLibraries();
        metricsTool.readTargetFiles();
        metricsTool.analyzeTargetFiles();
        metricsTool.launchPlugins();
        metricsTool.writeMetrics();

        out.println("successfully finished.");

        final long end = System.nanoTime();

        if (Settings.getInstance().isVerbose()) {
            out.println("elapsed time: " + (end - start) / 1000000000 + " seconds");
            out.println("number of analyzed files: "
                    + DataManager.getInstance().getFileInfoManager().getFileInfos().size());

            int loc = 0;
            for (final FileInfo file : DataManager.getInstance().getFileInfoManager()
                    .getFileInfos()) {
                loc += file.getLOC();
            }
            out.println("analyzed lines of code: " + loc);

        }
        MessagePool.getInstance(MESSAGE_TYPE.OUT).removeMessageListener(outListener);
        MessagePool.getInstance(MESSAGE_TYPE.ERROR).removeMessageListener(errListener);
    }

    /**
     * 引数無しコンストラクタ． セキュリティマネージャの初期化を行う．
     */
    public MetricsTool() {

    }

    /**
     * ライブラリを解析し，その情報をExternalClassInfoとして登録する．
     * readTargetFiles()の前に呼び出されなければならない
     */
    public void analyzeLibraries() {

        final Settings settings = Settings.getInstance();

        // java言語の場合
        if (settings.getLanguage().equals(LANGUAGE.JAVA15)
                || settings.getLanguage().equals(LANGUAGE.JAVA14)
                || settings.getLanguage().equals(LANGUAGE.JAVA13)) {

            this.analyzeJavaLibraries();
        }

        else if (settings.getLanguage().equals(LANGUAGE.CSHARP)) {

        }
    }

    private void analyzeJavaLibraries() {

        final Set<JavaUnresolvedExternalClassInfo> unresolvedExternalClasses = new HashSet<JavaUnresolvedExternalClassInfo>();

        // バイトコードから読み込み
        for (final String path : Settings.getInstance().getLibraries()) {
            readJavaLibraries(new File(path), unresolvedExternalClasses);
        }

        // クラスそのもののみ名前解決（型は解決しない）
        final ClassInfoManager classInfoManager = DataManager.getInstance().getClassInfoManager();
        for (final JavaUnresolvedExternalClassInfo unresolvedClassInfo : unresolvedExternalClasses) {

            final String unresolvedName = unresolvedClassInfo.getName();
            final String[] name = JavaByteCodeNameResolver.resolveName(unresolvedName);
            final Set<String> unresolvedModifiers = unresolvedClassInfo.getModifiers();
            final boolean isPublicVisible = unresolvedModifiers
                    .contains(JavaPredefinedModifierInfo.PUBLIC_STRING);
            final boolean isNamespaceVisible = !unresolvedModifiers
                    .contains(JavaPredefinedModifierInfo.PRIVATE_STRING);
            final boolean isInheritanceVisible = !unresolvedModifiers
                    .contains(JavaPredefinedModifierInfo.PROTECTED_STRING);
            final boolean isPrivateVisible = unresolvedModifiers
                    .contains(JavaPredefinedModifierInfo.PRIVATE_STRING);
            final boolean isStatic = unresolvedModifiers
                    .contains(JavaPredefinedModifierInfo.STATIC_STRING);
            final boolean isInterface = unresolvedClassInfo.isInterface();
            final Set<ModifierInfo> modifiers = new HashSet<ModifierInfo>();
            for (final String unresolvedModifier : unresolvedModifiers) {
                modifiers.add(JavaPredefinedModifierInfo.getModifierInfo(unresolvedModifier));
            }
            final ExternalClassInfo classInfo = new ExternalClassInfo(modifiers, name,
                    isPrivateVisible, isNamespaceVisible, isInheritanceVisible, isPublicVisible,
                    !isStatic, isInterface);
            classInfoManager.add(classInfo);
        }

        //　各クラスで表われている型を解決していく
        for (final JavaUnresolvedExternalClassInfo unresolvedClassInfo : unresolvedExternalClasses) {

            // まずは，解決済みオブジェクトを取得            
            final String unresolvedClassName = unresolvedClassInfo.getName();
            final String[] className = JavaByteCodeNameResolver.resolveName(unresolvedClassName);
            final ExternalClassInfo classInfo = (ExternalClassInfo) classInfoManager
                    .getClassInfo(className);

            // 型パラメータがあれば解決
            {
                final List<String> unresolvedTypeParameters = unresolvedClassInfo
                        .getTypeParameters();
                for (int index = 0; index < unresolvedTypeParameters.size(); index++) {
                    final String unresolvedTypeParameter = unresolvedTypeParameters.get(index);
                    TypeParameterInfo typeParameter = JavaByteCodeNameResolver
                            .resolveTypeParameter(unresolvedTypeParameter, index, classInfo);
                    classInfo.addTypeParameter(typeParameter);
                }
            }

            // 親クラスがあれば解決
            {
                final String unresolvedSuperName = unresolvedClassInfo.getSuperName();
                if (null != unresolvedSuperName) {
                    final String[] superName = JavaByteCodeNameResolver
                            .resolveName(unresolvedSuperName);
                    ExternalClassInfo superClassInfo = (ExternalClassInfo) classInfoManager
                            .getClassInfo(superName);
                    if (null == superClassInfo) {
                        superClassInfo = new ExternalClassInfo(superName);
                        classInfoManager.add(superClassInfo);
                    }
                    final ClassTypeInfo superClassType = new ClassTypeInfo(superClassInfo);
                    classInfo.addSuperClass(superClassType);
                }
            }

            // インターフェースがあれば解決
            for (final String unresolvedInterfaceName : unresolvedClassInfo.getInterfaces()) {
                final String[] interfaceName = JavaByteCodeNameResolver
                        .resolveName(unresolvedInterfaceName);
                ExternalClassInfo interfaceInfo = (ExternalClassInfo) classInfoManager
                        .getClassInfo(interfaceName);
                if (null == interfaceInfo) {
                    interfaceInfo = new ExternalClassInfo(interfaceName);
                    classInfoManager.add(interfaceInfo);
                }
                final ClassTypeInfo superClassType = new ClassTypeInfo(interfaceInfo);
                classInfo.addSuperClass(superClassType);
            }

            // フィールドの解決            
            for (final JavaUnresolvedExternalFieldInfo unresolvedField : unresolvedClassInfo
                    .getFields()) {

                final String fieldName = unresolvedField.getName();
                final String unresolvedType = unresolvedField.getType();
                final TypeInfo fieldType = JavaByteCodeNameResolver.resolveType(unresolvedType,
                        null, null);
                final Set<String> unresolvedModifiers = unresolvedField.getModifiers();
                final boolean isPublicVisible = unresolvedModifiers
                        .contains(JavaPredefinedModifierInfo.PUBLIC_STRING);
                final boolean isNamespaceVisible = !unresolvedModifiers
                        .contains(JavaPredefinedModifierInfo.PRIVATE_STRING);
                final boolean isInheritanceVisible = !unresolvedModifiers
                        .contains(JavaPredefinedModifierInfo.PROTECTED_STRING);
                final boolean isPrivateVisible = unresolvedModifiers
                        .contains(JavaPredefinedModifierInfo.PRIVATE_STRING);
                final boolean isStatic = unresolvedModifiers
                        .contains(JavaPredefinedModifierInfo.STATIC_STRING);
                final Set<ModifierInfo> modifiers = new HashSet<ModifierInfo>();
                for (final String unresolvedModifier : unresolvedModifiers) {
                    modifiers.add(JavaPredefinedModifierInfo.getModifierInfo(unresolvedModifier));
                }
                final ExternalFieldInfo field = new ExternalFieldInfo(modifiers, fieldName,
                        fieldType, classInfo, isPrivateVisible, isNamespaceVisible,
                        isInheritanceVisible, isPublicVisible, isStatic);
                classInfo.addDefinedField(field);
            }

            // メソッドの解決
            for (final JavaUnresolvedExternalMethodInfo unresolvedMethod : unresolvedClassInfo
                    .getMethods()) {

                final String name = unresolvedMethod.getName();

                final Set<String> unresolvedModifiers = unresolvedMethod.getModifiers();
                final boolean isPublicVisible = unresolvedModifiers
                        .contains(JavaPredefinedModifierInfo.PUBLIC_STRING);
                final boolean isNamespaceVisible = !unresolvedModifiers
                        .contains(JavaPredefinedModifierInfo.PRIVATE_STRING);
                final boolean isInheritanceVisible = !unresolvedModifiers
                        .contains(JavaPredefinedModifierInfo.PROTECTED_STRING);
                final boolean isPrivateVisible = unresolvedModifiers
                        .contains(JavaPredefinedModifierInfo.PRIVATE_STRING);
                final boolean isStatic = unresolvedModifiers
                        .contains(JavaPredefinedModifierInfo.STATIC_STRING);
                final Set<ModifierInfo> modifiers = new HashSet<ModifierInfo>();
                for (final String unresolvedModifier : unresolvedModifiers) {
                    modifiers.add(JavaPredefinedModifierInfo.getModifierInfo(unresolvedModifier));
                }

                // コンストラクタのとき
                if (name.equals("<init>")) {

                    final ExternalConstructorInfo constructor = new ExternalConstructorInfo(
                            modifiers, classInfo, isPrivateVisible, isNamespaceVisible,
                            isInheritanceVisible, isPublicVisible);

                    // 型パラメータの解決
                    final List<String> unresolvedTypeParameters = unresolvedMethod
                            .getTypeParameters();
                    for (int index = 0; index < unresolvedTypeParameters.size(); index++) {
                        final String unresolvedTypeParameter = unresolvedTypeParameters.get(index);
                        TypeParameterInfo typeParameter = (TypeParameterInfo) JavaByteCodeNameResolver
                                .resolveTypeParameter(unresolvedTypeParameter, index, constructor);
                        constructor.addTypeParameter(typeParameter);
                    }

                    final List<String> unresolvedParameters = unresolvedMethod.getArgumentTypes();
                    for (final String unresolvedParameter : unresolvedParameters) {
                        final TypeInfo parameterType = JavaByteCodeNameResolver.resolveType(
                                unresolvedParameter, null, null);
                        final ExternalParameterInfo parameter = new ExternalParameterInfo(
                                parameterType, constructor);
                        constructor.addParameter(parameter);
                    }

                    classInfo.addDefinedConstructor(constructor);
                }

                // メソッドのとき
                else {

                    final ExternalMethodInfo method = new ExternalMethodInfo(modifiers, name,
                            classInfo, isPrivateVisible, isNamespaceVisible, isInheritanceVisible,
                            isPublicVisible, !isStatic);

                    // 型パラメータの解決
                    final List<String> unresolvedTypeParameters = unresolvedMethod
                            .getTypeParameters();
                    for (int index = 0; index < unresolvedTypeParameters.size(); index++) {
                        final String unresolvedTypeParameter = unresolvedTypeParameters.get(index);
                        TypeParameterInfo typeParameter = JavaByteCodeNameResolver
                                .resolveTypeParameter(unresolvedTypeParameter, index, method);
                        method.addTypeParameter(typeParameter);
                    }

                    final String unresolvedReturnType = unresolvedMethod.getReturnType();
                    final TypeInfo returnType = JavaByteCodeNameResolver.resolveType(
                            unresolvedReturnType, null, null);
                    method.setReturnType(returnType);

                    final List<String> unresolvedParameters = unresolvedMethod.getArgumentTypes();
                    for (final String unresolvedParameter : unresolvedParameters) {
                        final TypeInfo parameterType = JavaByteCodeNameResolver.resolveType(
                                unresolvedParameter, null, null);
                        final ExternalParameterInfo parameter = new ExternalParameterInfo(
                                parameterType, method);
                        method.addParameter(parameter);
                    }

                    classInfo.addDefinedMethod(method);
                }
            }
        }
    }

    private void readJavaLibraries(final File file,
            final Set<JavaUnresolvedExternalClassInfo> unresolvedExternalClasses) {

        try {

            // jarファイルの場合
            if (file.isFile() && file.getName().endsWith(".jar")) {

                final JarFile jar = new JarFile(file);
                for (final Enumeration<JarEntry> entries = jar.entries(); entries.hasMoreElements();) {
                    final JarEntry entry = entries.nextElement();
                    if (entry.getName().endsWith(".class")) {

                        final ClassReader reader = new ClassReader(jar.getInputStream(entry));
                        final JavaByteCodeParser parser = new JavaByteCodeParser();
                        reader.accept(parser, ClassReader.SKIP_CODE);
                        unresolvedExternalClasses.add(parser.getClassInfo());
                    }
                }
            }

            // classファイルの場合
            else if (file.isFile() && file.getName().endsWith(".class")) {

                final ClassReader reader = new ClassReader(new FileInputStream(file));
                final JavaByteCodeParser parser = new JavaByteCodeParser();
                reader.accept(parser, ClassReader.SKIP_CODE);
                unresolvedExternalClasses.add(parser.getClassInfo());
            }

            // ディレクトリの場合
            else if (file.isDirectory()) {

                for (final File subfile : file.listFiles()) {

                    if (subfile.isFile()) {
                        final String name = subfile.getName();
                        if (name.endsWith(".jar") || name.endsWith(".class")) {
                            readJavaLibraries(subfile, unresolvedExternalClasses);
                        }
                    }

                    else if (subfile.isDirectory()) {
                        readJavaLibraries(subfile, unresolvedExternalClasses);
                    }
                }
            }

            //上記以外の場合は正しくないファイルがJavaのライブラリとして指定されていることになり，終了            
            else {
                err.println("file <" + file.getAbsolutePath()
                        + "> is inappropriate as a Java library.");
                System.exit(0);
            }
        }

        // ライブラリの読み込みで例外が発生した場合はプログラムを終了
        catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * {@link #readTargetFiles()} で読み込んだ対象ファイル群を解析する.
     * 
     */
    public void analyzeTargetFiles() {

        // 対象ファイルのASTから未解決クラス，フィールド，メソッド情報を取得
        {
            out.println("parsing all target files.");
            final int totalFileNumber = DataManager.getInstance().getTargetFileManager().size();
            int currentFileNumber = 1;
            final List<Thread> threads = new LinkedList<Thread>();

            for (final TargetFile targetFile : DataManager.getInstance().getTargetFileManager()) {

                final Thread thread = new Thread(new TargetFileParser(targetFile,
                        currentFileNumber++, totalFileNumber, out, err));

                MetricsToolSecurityManager.getInstance().addPrivilegeThread(thread);
                threads.add(thread);
                thread.start();

                while (Settings.getInstance().getThreadNumber() < Thread.activeCount()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            // 全てのスレッドが終わるのを待つ
            for (final Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        out.println("resolving definitions and usages.");
        if (Settings.getInstance().isVerbose()) {
            out.println("STEP1 : resolve class definitions.");
        }
        registClassInfos();
        if (Settings.getInstance().isVerbose()) {
            out.println("STEP2 : resolve type parameters of classes.");
        }
        resolveTypeParameterOfClassInfos();
        if (Settings.getInstance().isVerbose()) {
            out.println("STEP3 : resolve class inheritances.");
        }
        addInheritanceInformationToClassInfos();
        if (Settings.getInstance().isVerbose()) {
            out.println("STEP4 : resolve field definitions.");
        }
        registFieldInfos();
        if (Settings.getInstance().isVerbose()) {
            out.println("STEP5 : resolve method definitions.");
        }
        registMethodInfos();
        if (Settings.getInstance().isVerbose()) {
            out.println("STEP6 : resolve outer unit for inner classes.");
        }
        addOuterUnitInfos();
        if (Settings.getInstance().isVerbose()) {
            out.println("STEP7 : resolve type parameter usages.");
        }
        addClassTypeParameterInfos();
        addMethodTypeParameterInfos();
        if (Settings.getInstance().isVerbose()) {
            out.println("STEP8 : resolve method overrides.");
        }
        addOverrideRelation();
        if (Settings.getInstance().isStatement()) {
            if (Settings.getInstance().isVerbose()) {
                out.println("STEP9 : resolve field and method usages.");
            }
            addMethodInsideInfomation();
        }

        // 文法誤りのあるファイル一覧を表示
        // err.println("The following files includes uncorrect syntax.");
        // err.println("Any metrics of them were not measured");
        for (final TargetFile targetFile : DataManager.getInstance().getTargetFileManager()) {
            if (!targetFile.isCorrectSyntax()) {
                err.println("Incorrect syntax file: " + targetFile.getName());
            }
        }
    }

    /**
     * プラグインをロードする. 指定された言語，指定されたメトリクスに関連するプラグインのみを {@link PluginManager}に登録する.
     * null が指定された場合は対象言語において計測可能な全てのメトリクスを登録する
     * 
     * @param metrics 指定するメトリクスの配列，指定しない場合はnull
     */
    public void loadPlugins(final String[] metrics) {

        final PluginManager pluginManager = DataManager.getInstance().getPluginManager();
        final Settings settings = Settings.getInstance();
        try {
            for (final AbstractPlugin plugin : (new DefaultPluginLoader()).loadPlugins()) {// プラグインを全ロード
                final PluginInfo info = plugin.getPluginInfo();

                // 対象言語で計測可能でなければ登録しない
                if (!info.isMeasurable(settings.getLanguage())) {
                    continue;
                }

                if (null != metrics) {
                    // メトリクスが指定されているのでこのプラグインと一致するかチェック
                    final String pluginMetricName = info.getMetricName();
                    for (final String metric : metrics) {
                        if (metric.equalsIgnoreCase(pluginMetricName)) {
                            pluginManager.addPlugin(plugin);
                            break;
                        }
                    }

                    // メトリクスが指定されていないのでとりあえず全部登録
                } else {
                    pluginManager.addPlugin(plugin);
                }
            }
        } catch (PluginLoadException e) {
            err.println(e.getMessage());
            System.exit(0);
        }
    }

    /**
     * ロード済みのプラグインを実行する.
     */
    public void launchPlugins() {

        out.println("calculating metrics.");

        PluginLauncher launcher = new DefaultPluginLauncher();
        launcher.setMaximumLaunchingNum(1);
        launcher.launchAll(DataManager.getInstance().getPluginManager().getPlugins());

        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // 気にしない
            }
        } while (0 < launcher.getCurrentLaunchingNum() + launcher.getLaunchWaitingTaskNum());

        launcher.stopLaunching();
    }

    /**
     * {@link Settings}に指定された場所から解析対象ファイルを読み込んで登録する
     */
    public void readTargetFiles() {

        out.println("building target file list.");

        final Settings settings = Settings.getInstance();

        // ディレクトリから読み込み
        for (final String directory : settings.getTargetDirectories()) {
            registerFilesFromDirectory(new File(directory));
        }

        // リストファイルから読み込み
        for (final String file : settings.getListFiles()) {

            try {

                final TargetFileManager targetFiles = DataManager.getInstance()
                        .getTargetFileManager();
                final BufferedReader reader = new BufferedReader(new FileReader(file));
                while (reader.ready()) {
                    final String line = reader.readLine();
                    final TargetFile targetFile = new TargetFile(line);
                    targetFiles.add(targetFile);
                }
                reader.close();
            } catch (FileNotFoundException e) {
                err.println("\"" + file + "\" is not a valid file!");
                System.exit(0);
            } catch (IOException e) {
                err.println("\"" + file + "\" can\'t read!");
                System.exit(0);
            }
        }
    }

    /**
     * メトリクス情報を {@link Settings} に指定されたファイルに出力する.
     */
    public void writeMetrics() {

        final PluginManager pluginManager = DataManager.getInstance().getPluginManager();
        final Settings settings = Settings.getInstance();

        // ファイルメトリクスを計測する場合
        if (0 < pluginManager.getFileMetricPlugins().size()) {

            try {
                final FileMetricsInfoManager manager = DataManager.getInstance()
                        .getFileMetricsInfoManager();
                manager.checkMetrics();

                final String fileName = settings.getFileMetricsFile();
                final CSVFileMetricsWriter writer = new CSVFileMetricsWriter(fileName);
                writer.write();

            } catch (MetricNotRegisteredException e) {
                err.println(e.getMessage());
                err.println("File metrics can't be output!");
            }
        }

        // クラスメトリクスを計測する場合
        if (0 < pluginManager.getClassMetricPlugins().size()) {

            try {
                final ClassMetricsInfoManager manager = DataManager.getInstance()
                        .getClassMetricsInfoManager();
                manager.checkMetrics();

                final String fileName = settings.getClassMetricsFile();
                final CSVClassMetricsWriter writer = new CSVClassMetricsWriter(fileName);
                writer.write();

            } catch (MetricNotRegisteredException e) {
                err.println(e.getMessage());
                err.println("Class metrics can't be output!");
            }
        }

        // メソッドメトリクスを計測する場合
        if (0 < pluginManager.getMethodMetricPlugins().size()) {

            try {
                final MethodMetricsInfoManager manager = DataManager.getInstance()
                        .getMethodMetricsInfoManager();
                manager.checkMetrics();

                final String fileName = settings.getMethodMetricsFile();
                final CSVMethodMetricsWriter writer = new CSVMethodMetricsWriter(fileName);
                writer.write();

            } catch (MetricNotRegisteredException e) {
                err.println(e.getMessage());
                err.println("Method metrics can't be output!");
            }

        }

        // フィールドメトリクスを計測する場合
        if (0 < pluginManager.getFieldMetricPlugins().size()) {

            try {
                final FieldMetricsInfoManager manager = DataManager.getInstance()
                        .getFieldMetricsInfoManager();
                manager.checkMetrics();

                final String fileName = settings.getMethodMetricsFile();
                final CSVMethodMetricsWriter writer = new CSVMethodMetricsWriter(fileName);
                writer.write();

            } catch (MetricNotRegisteredException e) {
                err.println(e.getMessage());
                err.println("Field metrics can't be output!");
            }
        }
    }

    /**
     * {@link MetricsToolSecurityManager} の初期化を行う. システムに登録できれば，システムのセキュリティマネージャにも登録する.
     */
    private static final void initSecurityManager() {
        try {
            // MetricsToolSecurityManagerのシングルトンインスタンスを構築し，初期特別権限スレッドになる
            System.setSecurityManager(MetricsToolSecurityManager.getInstance());
        } catch (final SecurityException e) {
            // 既にセットされているセキュリティマネージャによって，新たなセキュリティマネージャの登録が許可されなかった．
            // システムのセキュリティマネージャとして使わなくても，特別権限スレッドのアクセス制御は問題なく動作するのでとりあえず無視する
            err
                    .println("Failed to set system security manager. MetricsToolsecurityManager works only to manage privilege threads.");
        }
    }

    /**
     * 
     * @param file 対象ファイルまたはディレクトリ
     * 
     * 対象がディレクトリの場合は，その子に対して再帰的に処理をする． 対象がファイルの場合は，対象言語のソースファイルであれば，登録処理を行う．
     */
    private void registerFilesFromDirectory(final File file) {

        // ディレクトリならば，再帰的に処理
        if (file.isDirectory()) {
            File[] subfiles = file.listFiles();
            for (int i = 0; i < subfiles.length; i++) {
                registerFilesFromDirectory(subfiles[i]);
            }

            // ファイルならば，拡張子が対象言語と一致すれば登録
        } else if (file.isFile()) {

            final LANGUAGE language = Settings.getInstance().getLanguage();
            final String extension = language.getExtension();
            final String path = file.getAbsolutePath();
            if (path.endsWith(extension)) {
                final TargetFileManager targetFiles = DataManager.getInstance()
                        .getTargetFileManager();
                final TargetFile targetFile = new TargetFile(path);
                targetFiles.add(targetFile);
            }

            // ディレクトリでもファイルでもない場合は不正
        } else {
            err.println("\"" + file.getAbsolutePath() + "\" is not a vaild file!");
            System.exit(0);
        }
    }

    /**
     * 出力メッセージ出力用のプリンタ
     */
    protected static MessagePrinter out = new DefaultMessagePrinter(new MessageSource() {
        public String getMessageSourceName() {
            return "main";
        }
    }, MESSAGE_TYPE.OUT);

    /**
     * エラーメッセージ出力用のプリンタ
     */
    protected static MessagePrinter err = new DefaultMessagePrinter(new MessageSource() {
        public String getMessageSourceName() {
            return "main";
        }
    }, MESSAGE_TYPE.ERROR);

    /**
     * クラスの定義を ClassInfoManager に登録する．AST パースの後に呼び出さなければならない．
     */
    private void registClassInfos() {

        // 未解決クラス情報マネージャ， クラス情報マネージャを取得
        final UnresolvedClassInfoManager unresolvedClassInfoManager = DataManager.getInstance()
                .getUnresolvedClassInfoManager();
        final ClassInfoManager classInfoManager = DataManager.getInstance().getClassInfoManager();

        // 各未解決クラスに対して
        for (final UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager
                .getClassInfos()) {

            final FileInfo fileInfo = unresolvedClassInfo.getFileInfo();

            //　クラス情報を解決
            final TargetClassInfo classInfo = unresolvedClassInfo.resolve(null, null,
                    classInfoManager, null, null);

            fileInfo.addDefinedClass(classInfo);

            // 解決されたクラス情報を登録
            classInfoManager.add(classInfo);

            // 各インナークラスに対して処理
            for (final UnresolvedClassInfo unresolvedInnerClassInfo : unresolvedClassInfo
                    .getInnerClasses()) {

                //　インナークラス情報を解決
                final TargetInnerClassInfo innerClass = registInnerClassInfo(
                        unresolvedInnerClassInfo, classInfo, classInfoManager);

                // 解決されたインナークラス情報を登録
                classInfo.addInnerClass(innerClass);
                classInfoManager.add(innerClass);
            }
        }
    }

    /**
     * インナークラスの定義を ClassInfoManager に登録する． registClassInfos からのみ呼ばれるべきである．
     * 
     * @param unresolvedClassInfo 名前解決されるインナークラスオブジェクト
     * @param outerClass 外側のクラス
     * @param classInfoManager インナークラスを登録するクラスマネージャ
     * @return 生成したインナークラスの ClassInfo
     */
    private TargetInnerClassInfo registInnerClassInfo(
            final UnresolvedClassInfo unresolvedClassInfo, final TargetClassInfo outerClass,
            final ClassInfoManager classInfoManager) {

        final TargetInnerClassInfo classInfo = (TargetInnerClassInfo) unresolvedClassInfo.resolve(
                outerClass, null, classInfoManager, null, null);

        // このクラスのインナークラスに対して再帰的に処理
        for (final UnresolvedClassInfo unresolvedInnerClassInfo : unresolvedClassInfo
                .getInnerClasses()) {

            //　インナークラス情報を解決
            final TargetInnerClassInfo innerClass = registInnerClassInfo(unresolvedInnerClassInfo,
                    classInfo, classInfoManager);

            // 解決されたインナークラス情報を登録
            classInfo.addInnerClass(innerClass);
            classInfoManager.add(innerClass);
        }

        // このクラスの ClassInfo を返す
        return classInfo;
    }

    /**
     * クラスの型パラメータを名前解決する．registClassInfos の後， 且つaddInheritanceInformationToClassInfo
     * の前に呼び出さなければならない．
     * 
     */
    private void resolveTypeParameterOfClassInfos() {

        // 未解決クラス情報マネージャ， 解決済みクラスマネージャを取得
        final UnresolvedClassInfoManager unresolvedClassInfoManager = DataManager.getInstance()
                .getUnresolvedClassInfoManager();
        final ClassInfoManager classInfoManager = DataManager.getInstance().getClassInfoManager();

        // 各未解決クラスに対して
        for (final UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager
                .getClassInfos()) {
            resolveTypeParameterOfClassInfos(unresolvedClassInfo, classInfoManager);
        }
    }

    /**
     * クラスの型パラメータを名前解決する． resolveTypeParameterOfClassInfo() 中からのみ呼び出されるべき
     * 
     * @param unresolvedClassInfo 名前解決する型パラメータを持つクラス
     * @param classInfoManager 名前解決に用いるクラスマネージャ
     */
    private void resolveTypeParameterOfClassInfos(final UnresolvedClassInfo unresolvedClassInfo,
            final ClassInfoManager classInfoManager) {

        // 解決済みクラス情報を取得
        final TargetClassInfo classInfo = unresolvedClassInfo.getResolved();
        assert null != classInfo : "classInfo shouldn't be null!";

        // 未解決クラス情報から未解決型パラメータを取得し，型解決を行った後，解決済みクラス情報に付与する
        for (final UnresolvedTypeParameterInfo unresolvedTypeParameter : unresolvedClassInfo
                .getTypeParameters()) {

            final TypeParameterInfo typeParameter = unresolvedTypeParameter.resolve(classInfo,
                    null, classInfoManager, null, null);
            classInfo.addTypeParameter(typeParameter);
        }

        // 各未解決インナークラスに対して
        for (final UnresolvedClassInfo unresolvedInnerClassInfo : unresolvedClassInfo
                .getInnerClasses()) {
            resolveTypeParameterOfClassInfos(unresolvedInnerClassInfo, classInfoManager);
        }
    }

    /**
     * クラスの継承情報を ClassInfo に追加する．一度目の AST パースの後，かつ registClassInfos の後によびださなければならない．
     */
    private void addInheritanceInformationToClassInfos() {

        // Unresolved クラス情報マネージャ， クラス情報マネージャを取得
        final UnresolvedClassInfoManager unresolvedClassInfoManager = DataManager.getInstance()
                .getUnresolvedClassInfoManager();
        final ClassInfoManager classInfoManager = DataManager.getInstance().getClassInfoManager();
        final FieldInfoManager fieldInfoManager = DataManager.getInstance().getFieldInfoManager();
        final MethodInfoManager methodInfoManager = DataManager.getInstance()
                .getMethodInfoManager();

        // 名前解決不可能クラスを保存するためのリスト
        final List<UnresolvedClassInfo> unresolvableClasses = new LinkedList<UnresolvedClassInfo>();

        // 各 Unresolvedクラスに対して
        for (UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager.getClassInfos()) {
            addInheritanceInformationToClassInfo(unresolvedClassInfo, classInfoManager,
                    fieldInfoManager, methodInfoManager, unresolvableClasses);
        }

        // 名前解決不可能クラスを解析する
        for (int i = 0; i < 100; i++) {

            CLASSLOOP: for (final Iterator<UnresolvedClassInfo> classIterator = unresolvableClasses
                    .iterator(); classIterator.hasNext();) {

                // ClassInfo を取得
                final UnresolvedClassInfo unresolvedClassInfo = classIterator.next();
                final TargetClassInfo classInfo = unresolvedClassInfo.getResolved();
                assert null != classInfo : "classInfo shouldn't be null!";

                // 各親クラス名に対して
                for (final UnresolvedClassTypeInfo unresolvedSuperClassType : unresolvedClassInfo
                        .getSuperClasses()) {

                    TypeInfo superClassType = unresolvedSuperClassType.resolve(classInfo, null,
                            classInfoManager, null, null);

                    // null でない場合は名前解決に成功したとみなす
                    if (null != superClassType) {

                        // 見つからなかった場合は名前空間名がUNKNOWNなクラスを登録する
                        if (superClassType instanceof UnknownTypeInfo) {
                            final ExternalClassInfo superClass = new ExternalClassInfo(
                                    unresolvedSuperClassType.getTypeName());
                            classInfoManager.add(superClass);
                            superClassType = new ClassTypeInfo(superClass);
                        }

                        classInfo.addSuperClass((ClassTypeInfo) superClassType);
                        ((ClassTypeInfo) superClassType).getReferencedClass()
                                .addSubClass(classInfo);

                        // null な場合は名前解決に失敗したとみなすので unresolvedClassInfo は unresolvableClasses
                        // から削除しない
                    } else {
                        continue CLASSLOOP;
                    }
                }

                classIterator.remove();
            }

            // 無作為に unresolvableClasses を入れ替え
            Collections.shuffle(unresolvableClasses);
        }

        if (0 < unresolvableClasses.size()) {
            err.println("There are " + unresolvableClasses.size()
                    + " unresolvable class inheritance");
        }
    }

    /**
     * クラスの継承情報を InnerClassInfo に追加する．addInheritanceInformationToClassInfos の中からのみ呼び出されるべき
     * 
     * @param unresolvedClassInfo 継承関係を追加する（未解決）クラス情報
     * @param classInfoManager 名前解決に用いるクラスマネージャ
     */
    private void addInheritanceInformationToClassInfo(
            final UnresolvedClassInfo unresolvedClassInfo, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager,
            final List<UnresolvedClassInfo> unresolvableClasses) {

        // ClassInfo を取得
        final TargetClassInfo classInfo = unresolvedClassInfo.getResolved();
        assert null != classInfo : "classInfo shouldn't be null!";

        // 各親クラス名に対して
        for (final UnresolvedClassTypeInfo unresolvedSuperClassType : unresolvedClassInfo
                .getSuperClasses()) {

            TypeInfo superClassType = unresolvedSuperClassType.resolve(classInfo, null,
                    classInfoManager, fieldInfoManager, methodInfoManager);

            // null だった場合は解決不可能リストに一時的に格納
            if (null == superClassType) {

                unresolvableClasses.add(unresolvedClassInfo);

            } else {

                // 見つからなかった場合は名前空間名がUNKNOWNなクラスを登録する
                if (superClassType instanceof UnknownTypeInfo) {
                    final ExternalClassInfo superClass = new ExternalClassInfo(
                            unresolvedSuperClassType.getTypeName());
                    classInfoManager.add(superClass);
                }

                classInfo.addSuperClass((ClassTypeInfo) superClassType);
                ((ClassTypeInfo) superClassType).getReferencedClass().addSubClass(classInfo);
            }
        }

        // 各インナークラスに対して
        for (final UnresolvedClassInfo unresolvedInnerClassInfo : unresolvedClassInfo
                .getInnerClasses()) {
            addInheritanceInformationToClassInfo(unresolvedInnerClassInfo, classInfoManager,
                    fieldInfoManager, methodInfoManager, unresolvableClasses);
        }
    }

    /**
     * フィールドの定義を FieldInfoManager に登録する． registClassInfos の後に呼び出さなければならない
     * 
     */
    private void registFieldInfos() {

        // Unresolved クラス情報マネージャ，クラス情報マネージャ，フィールド情報マネージャを取得
        final UnresolvedClassInfoManager unresolvedClassInfoManager = DataManager.getInstance()
                .getUnresolvedClassInfoManager();
        final ClassInfoManager classInfoManager = DataManager.getInstance().getClassInfoManager();
        final FieldInfoManager fieldInfoManager = DataManager.getInstance().getFieldInfoManager();
        final MethodInfoManager methodInfoManager = DataManager.getInstance()
                .getMethodInfoManager();

        // 各 Unresolvedクラスに対して
        for (final UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager
                .getClassInfos()) {
            registFieldInfos(unresolvedClassInfo, classInfoManager, fieldInfoManager,
                    methodInfoManager);
        }
    }

    /**
     * フィールドの定義を FieldInfoManager に登録する．
     * 
     * @param unresolvedClassInfo フィールド解決対象クラス
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     */
    private void registFieldInfos(final UnresolvedClassInfo unresolvedClassInfo,
            final ClassInfoManager classInfoManager, final FieldInfoManager fieldInfoManager,
            final MethodInfoManager methodInfoManager) {

        // ClassInfo を取得
        final TargetClassInfo ownerClass = unresolvedClassInfo.getResolved();
        assert null != ownerClass : "ownerClass shouldn't be null!";

        // 各未解決フィールドに対して
        for (final UnresolvedFieldInfo unresolvedFieldInfo : unresolvedClassInfo.getDefinedFields()) {

            unresolvedFieldInfo.resolve(ownerClass, null, classInfoManager, fieldInfoManager,
                    methodInfoManager);
        }

        // 各インナークラスに対して
        for (final UnresolvedClassInfo unresolvedInnerClassInfo : unresolvedClassInfo
                .getInnerClasses()) {
            registFieldInfos(unresolvedInnerClassInfo, classInfoManager, fieldInfoManager,
                    methodInfoManager);
        }
    }

    /**
     * メソッドの定義を MethodInfoManager に登録する．registClassInfos の後に呼び出さなければならない．
     */
    private void registMethodInfos() {

        // Unresolved クラス情報マネージャ， クラス情報マネージャ，メソッド情報マネージャを取得
        final UnresolvedClassInfoManager unresolvedClassInfoManager = DataManager.getInstance()
                .getUnresolvedClassInfoManager();
        final ClassInfoManager classInfoManager = DataManager.getInstance().getClassInfoManager();
        final FieldInfoManager fieldInfoManager = DataManager.getInstance().getFieldInfoManager();
        final MethodInfoManager methodInfoManager = DataManager.getInstance()
                .getMethodInfoManager();

        // 各 Unresolvedクラスに対して
        for (final UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager
                .getClassInfos()) {
            registMethodInfos(unresolvedClassInfo, classInfoManager, fieldInfoManager,
                    methodInfoManager);
        }
    }

    /**
     * 未解決メソッド定義情報を解決し，メソッドマネージャに登録する．
     * 
     * @param unresolvedClassInfo メソッド解決対象クラス
     * @param classInfoManager 用いるクラスマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     */
    private void registMethodInfos(final UnresolvedClassInfo unresolvedClassInfo,
            final ClassInfoManager classInfoManager, final FieldInfoManager fieldInfoManager,
            final MethodInfoManager methodInfoManager) {

        // ClassInfo を取得
        final TargetClassInfo ownerClass = unresolvedClassInfo.getResolved();

        // 各未解決メソッドに対して
        for (final UnresolvedMethodInfo unresolvedMethodInfo : unresolvedClassInfo
                .getDefinedMethods()) {

            // メソッド情報を解決
            final TargetMethodInfo methodInfo = unresolvedMethodInfo.resolve(ownerClass, null,
                    classInfoManager, fieldInfoManager, methodInfoManager);

            // メソッド情報を登録
            ownerClass.addDefinedMethod(methodInfo);
            methodInfoManager.add(methodInfo);
        }

        // 各未解決コンストラクタに対して
        for (final UnresolvedConstructorInfo unresolvedConstructorInfo : unresolvedClassInfo
                .getDefinedConstructors()) {

            //　コンストラクタ情報を解決
            final TargetConstructorInfo constructorInfo = unresolvedConstructorInfo.resolve(
                    ownerClass, null, classInfoManager, fieldInfoManager, methodInfoManager);
            methodInfoManager.add(constructorInfo);

            // コンストラクタ情報を登録            
            ownerClass.addDefinedConstructor(constructorInfo);
            methodInfoManager.add(constructorInfo);

        }

        // for unresolved instance initializers
        for (final UnresolvedInstanceInitializerInfo unresolvedInstanceInitializer : unresolvedClassInfo
                .getInstanceInitializers()) {
            // resolve
            final InstanceInitializerInfo instanceInitializer = unresolvedInstanceInitializer
                    .resolve(ownerClass, null, classInfoManager, fieldInfoManager,
                            methodInfoManager);

            // register
            ownerClass.addInstanceInitializer(instanceInitializer);
        }

        // for unresolved static initializers
        for (final UnresolvedStaticInitializerInfo unresolvedStaticInitializer : unresolvedClassInfo
                .getStaticInitializers()) {
            // resolve
            final StaticInitializerInfo staticInitializer = unresolvedStaticInitializer.resolve(
                    ownerClass, null, classInfoManager, fieldInfoManager, methodInfoManager);

            // register
            ownerClass.addStaticInitializer(staticInitializer);
        }

        // 未解決コンストラクタが0の場合は，デフォルトコンストラクタを追加
        if (0 == unresolvedClassInfo.getDefinedConstructors().size()) {
            final TargetConstructorInfo defaultConstructor = new TargetConstructorInfo(
                    new HashSet<ModifierInfo>(), ownerClass, false, true, false, false, 0, 0, 0, 0);
            ownerClass.addDefinedConstructor(defaultConstructor);
            methodInfoManager.add(defaultConstructor);
        }

        // 各 Unresolvedクラスに対して
        for (final UnresolvedClassInfo unresolvedInnerClassInfo : unresolvedClassInfo
                .getInnerClasses()) {
            registMethodInfos(unresolvedInnerClassInfo, classInfoManager, fieldInfoManager,
                    methodInfoManager);
        }
    }

    private void addOuterUnitInfos() {

        // Unresolved クラス情報マネージャ， クラス情報マネージャ，メソッド情報マネージャを取得
        final UnresolvedClassInfoManager unresolvedClassInfoManager = DataManager.getInstance()
                .getUnresolvedClassInfoManager();
        final ClassInfoManager classInfoManager = DataManager.getInstance().getClassInfoManager();
        final FieldInfoManager fieldInfoManager = DataManager.getInstance().getFieldInfoManager();
        final MethodInfoManager methodInfoManager = DataManager.getInstance()
                .getMethodInfoManager();

        // 各 Unresolvedクラスに対して
        for (final UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager
                .getClassInfos()) {
            addOuterUnitInfos(unresolvedClassInfo, classInfoManager, fieldInfoManager,
                    methodInfoManager);
        }
    }

    private void addOuterUnitInfos(final UnresolvedClassInfo unresolvedClassInfo,
            final ClassInfoManager classInfoManager, final FieldInfoManager fieldInfoManager,
            final MethodInfoManager methodInfoManager) {

        // ClassInfo を取得
        final TargetClassInfo classInfo = unresolvedClassInfo.getResolved();
        if (classInfo instanceof InnerClassInfo) {

            final TargetInnerClassInfo innerClass = (TargetInnerClassInfo) classInfo;
            final UnresolvedUnitInfo<?> unresolvedOuterUnit = unresolvedClassInfo.getOuterUnit();
            final UnitInfo outerUnit = unresolvedOuterUnit.resolve(classInfo, null,
                    classInfoManager, fieldInfoManager, methodInfoManager);
            try {
                final Class<?> cls = Class
                        .forName("jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetInnerClassInfo");
                final Field filed = cls.getDeclaredField("outerUnit");

                filed.setAccessible(true);
                filed.set(innerClass, outerUnit);
                
            } catch (ClassNotFoundException e) {
                assert false : "Illegal state: TargetInnerClassInfo was not found";
            } catch (NoSuchFieldException e) {
                assert false : "Illegal state: outerUnit wad not found";
            } catch (IllegalAccessException e) {
                assert false;
            }
        }

        // インナークラスに対して実行
        for (final UnresolvedClassInfo innerClass : unresolvedClassInfo.getInnerClasses()) {
            addOuterUnitInfos(innerClass, classInfoManager, fieldInfoManager, methodInfoManager);
        }
    }

    private void addClassTypeParameterInfos() {

        for (final ClassInfo classInfo : DataManager.getInstance().getClassInfoManager()
                .getTargetClassInfos()) {
            addClassTypeParameterInfos(classInfo);
        }
    }

    private void addClassTypeParameterInfos(final ClassInfo classInfo) {

        final List<ClassTypeInfo> superClassTypes = classInfo.getSuperClasses();
        for (final ClassTypeInfo superClassType : superClassTypes) {

            final ClassInfo superClassInfo = superClassType.getReferencedClass();
            if (superClassInfo instanceof TargetClassInfo) {
                addClassTypeParameterInfos((ClassInfo) superClassInfo);

                // 親クラス以上における型パラメータの使用を取得
                final Map<TypeParameterInfo, TypeInfo> typeParameterUsages = ((TargetClassInfo) superClassInfo)
                        .getTypeParameterUsages();
                for (final TypeParameterInfo typeParameterInfo : typeParameterUsages.keySet()) {
                    final TypeInfo usedType = typeParameterUsages.get(typeParameterInfo);
                    classInfo.addTypeParameterUsage(typeParameterInfo, usedType);
                }

                // このクラスにおける型パラメータの使用を取得
                final List<TypeInfo> typeArguments = superClassType.getTypeArguments();
                for (int index = 0; index < typeArguments.size(); index++) {
                    final TypeInfo usedType = typeArguments.get(index);
                    final TypeParameterInfo typeParameterInfo = ((TargetClassInfo) superClassInfo)
                            .getTypeParameter(index);
                    classInfo.addTypeParameterUsage(typeParameterInfo, usedType);
                }
            }
        }
    }

    private void addMethodTypeParameterInfos() {

        for (final TargetMethodInfo methodInfo : DataManager.getInstance().getMethodInfoManager()
                .getTargetMethodInfos()) {
            addMethodTypeParameterInfos(methodInfo);
        }
    }

    private void addMethodTypeParameterInfos(final TargetMethodInfo methodInfo) {

        //　まず，オーナークラスにおける型パラメータ使用の情報を追加する
        {
            final TargetClassInfo ownerClassInfo = (TargetClassInfo) methodInfo.getOwnerClass();
            final Map<TypeParameterInfo, TypeInfo> typeParameterUsages = ownerClassInfo
                    .getTypeParameterUsages();
            for (final TypeParameterInfo typeParameterInfo : typeParameterUsages.keySet()) {
                final TypeInfo usedType = typeParameterUsages.get(typeParameterInfo);
                methodInfo.addTypeParameterUsage(typeParameterInfo, usedType);
            }
        }

        // TODO メソッド内における型パラメータ使用を追加すべき？
    }

    /**
     * メソッドオーバーライド情報を各MethodInfoに追加する．addInheritanceInfomationToClassInfos の後 かつ registMethodInfos
     * の後に呼び出さなければならない
     */
    private void addOverrideRelation() {

        // 全ての外部クラスに対して
        for (final ExternalClassInfo classInfo : DataManager.getInstance().getClassInfoManager()
                .getExternalClassInfos()) {
            addOverrideRelation(classInfo);

        }

        // 全ての対象クラスに対して
        for (final TargetClassInfo classInfo : DataManager.getInstance().getClassInfoManager()
                .getTargetClassInfos()) {
            addOverrideRelation(classInfo);
        }
    }

    /**
     * メソッドオーバーライド情報を各MethodInfoに追加する．引数で指定したクラスのメソッドについて処理を行う
     * 
     * @param classInfo 対象クラス
     */
    private void addOverrideRelation(final ClassInfo classInfo) {

        // 各親クラスに対して
        for (final ClassInfo superClassInfo : ClassTypeInfo.convert(classInfo.getSuperClasses())) {

            // 各対象クラスの各メソッドについて，親クラスのメソッドをオーバーライドしているかを調査
            for (final MethodInfo methodInfo : classInfo.getDefinedMethods()) {
                addOverrideRelation(superClassInfo, methodInfo);
            }
        }

        // 各インナークラスに対して
        for (InnerClassInfo innerClassInfo : classInfo.getInnerClasses()) {
            addOverrideRelation((ClassInfo) innerClassInfo);
        }
    }

    /**
     * メソッドオーバーライド情報を追加する．引数で指定されたクラスで定義されているメソッドに対して操作を行う.
     * AddOverrideInformationToMethodInfos()の中からのみ呼び出される．
     * 
     * @param classInfo クラス情報
     * @param overrider オーバーライド対象のメソッド
     */
    private void addOverrideRelation(final ClassInfo classInfo, final MethodInfo overrider) {

        if ((null == classInfo) || (null == overrider)) {
            throw new IllegalArgumentException();
        }

        for (final MethodInfo methodInfo : classInfo.getDefinedMethods()) {

            // メソッド名が違う場合はオーバーライドされない
            if (!methodInfo.getMethodName().equals(overrider.getMethodName())) {
                continue;
            }

            if (0 != methodInfo.compareArgumentsTo(overrider)) {
                continue;
            }

            // オーバーライド関係を登録する
            overrider.addOverridee(methodInfo);
            methodInfo.addOverrider(overrider);

            // 直接のオーバーライド関係しか抽出しないので，このクラスの親クラスは調査しない
            return;
        }

        // 親クラス群に対して再帰的に処理
        for (final ClassInfo superClassInfo : ClassTypeInfo.convert(classInfo.getSuperClasses())) {
            addOverrideRelation(superClassInfo, overrider);
        }
    }

    /**
     * エンティティ（フィールドやクラス）の代入・参照，メソッドの呼び出し関係を追加する．
     */
    private void addMethodInsideInfomation() {

        final UnresolvedClassInfoManager unresolvedClassInfoManager = DataManager.getInstance()
                .getUnresolvedClassInfoManager();
        final ClassInfoManager classInfoManager = DataManager.getInstance().getClassInfoManager();
        final FieldInfoManager fieldInfoManager = DataManager.getInstance().getFieldInfoManager();
        final MethodInfoManager methodInfoManager = DataManager.getInstance()
                .getMethodInfoManager();

        // 各未解決クラス情報 に対して
        for (final UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager
                .getClassInfos()) {
            addMethodInsideInformation(unresolvedClassInfo, classInfoManager, fieldInfoManager,
                    methodInfoManager);
        }
    }

    /**
     * エンティティ（フィールドやクラス）の代入・参照，メソッドの呼び出し関係を追加する．
     * 
     * @param unresolvedClassInfo 解決対象クラス
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     */
    private void addMethodInsideInformation(final UnresolvedClassInfo unresolvedClassInfo,
            final ClassInfoManager classInfoManager, final FieldInfoManager fieldInfoManager,
            final MethodInfoManager methodInfoManager) {

        final TargetClassInfo classInfo = unresolvedClassInfo.getResolved();
        // 未解決フィールド情報に対して
        for (final UnresolvedFieldInfo unresolvedFieldInfo : unresolvedClassInfo.getDefinedFields()) {
            final TargetFieldInfo fieldInfo = unresolvedFieldInfo.getResolved();
            if (null != unresolvedFieldInfo.getInitilizer()) {
                final CallableUnitInfo initializerUnit = fieldInfo.isInstanceMember() ? classInfo
                        .getImplicitInstanceInitializer() : classInfo
                        .getImplicitStaticInitializer();
                final ExpressionInfo initializerExpression = unresolvedFieldInfo.getInitilizer()
                        .resolve(classInfo, initializerUnit, classInfoManager, fieldInfoManager,
                                methodInfoManager);
                fieldInfo.setInitializer(initializerExpression);
            }
        }

        // 各未解決メソッド情報に対して
        for (final UnresolvedMethodInfo unresolvedMethodInfo : unresolvedClassInfo
                .getDefinedMethods()) {

            final TargetMethodInfo method = unresolvedMethodInfo.getResolved();
            unresolvedMethodInfo.resolveInnerBlock(classInfo, method, classInfoManager,
                    fieldInfoManager, methodInfoManager);
        }

        // 各未解決コンストラクタ情報に対して
        for (final UnresolvedConstructorInfo unresolvedConstructorInfo : unresolvedClassInfo
                .getDefinedConstructors()) {

            final TargetConstructorInfo constructor = unresolvedConstructorInfo.getResolved();
            unresolvedConstructorInfo.resolveInnerBlock(classInfo, constructor, classInfoManager,
                    fieldInfoManager, methodInfoManager);
        }

        // resolve UnresolvedInstanceInitializers and register them
        for (final UnresolvedInstanceInitializerInfo unresolvedInstanceInitializer : unresolvedClassInfo
                .getInstanceInitializers()) {

            final InstanceInitializerInfo initializer = unresolvedInstanceInitializer.getResolved();
            unresolvedInstanceInitializer.resolveInnerBlock(classInfo, initializer,
                    classInfoManager, fieldInfoManager, methodInfoManager);
        }

        // resolve UnresolvedStaticInitializers and register them
        for (final UnresolvedStaticInitializerInfo unresolvedStaticInitializer : unresolvedClassInfo
                .getStaticInitializers()) {

            final StaticInitializerInfo initializer = unresolvedStaticInitializer.getResolved();
            unresolvedStaticInitializer.resolveInnerBlock(classInfo, initializer, classInfoManager,
                    fieldInfoManager, methodInfoManager);
        }

        // 各インナークラスに対して
        for (final UnresolvedClassInfo unresolvedInnerClassInfo : unresolvedClassInfo
                .getInnerClasses()) {
            addMethodInsideInformation(unresolvedInnerClassInfo, classInfoManager,
                    fieldInfoManager, methodInfoManager);
        }
    }

    /**
     * エンティティ（フィールドやクラス）の代入・参照，メソッドの呼び出し関係を追加する．
     * 
     * @param unresolvedLocalSpace 解析対象未解決ローカル領域
     * @param unresolvedClassInfo 解決対象クラス
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     */
    private void addReferenceAssignmentCallRelation(
            final UnresolvedLocalSpaceInfo<?> unresolvedLocalSpace,
            final UnresolvedClassInfo unresolvedClassInfo, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 未解決メソッド情報から，解決済みメソッド情報を取得
        final LocalSpaceInfo localSpace = unresolvedLocalSpace.getResolved();
        assert null != localSpace : "UnresolvedLocalSpaceInfo#getResolvedInfo is null!";

        // 所有クラスを取得
        final TargetClassInfo ownerClass = (TargetClassInfo) localSpace.getOwnerClass();
        final CallableUnitInfo ownerMethod;
        if (localSpace instanceof CallableUnitInfo) {
            ownerMethod = (CallableUnitInfo) localSpace;
        } else if (localSpace instanceof BlockInfo) {
            ownerMethod = ((BlockInfo) localSpace).getOwnerMethod();
        } else {
            ownerMethod = null;
            assert false : "Here shouldn't be reached!";
        }

        // 条件文の場合，未解決条件式の名前解決処理
        if (localSpace instanceof ConditionalBlockInfo) {
            final UnresolvedConditionalBlockInfo<?> unresolvedConditionalBlock = (UnresolvedConditionalBlockInfo<?>) unresolvedLocalSpace;

            if (null != unresolvedConditionalBlock.getConditionalClause()) {
                final ConditionalClauseInfo conditionalClause = unresolvedConditionalBlock
                        .getConditionalClause().resolve(ownerClass, ownerMethod, classInfoManager,
                                fieldInfoManager, methodInfoManager);

                try {
                    final Class<?> cls = Class
                            .forName("jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo");
                    final Field filed = cls.getDeclaredField("conditionalClause");

                    filed.setAccessible(true);
                    filed.set(localSpace, conditionalClause);
                } catch (ClassNotFoundException e) {
                    assert false : "Illegal state: ConditionalBlockInfo is not found";
                } catch (NoSuchFieldException e) {
                    assert false : "Illegal state: conditionalClause is not found";
                } catch (IllegalAccessException e) {
                    assert false;
                }
            } else {
                assert false;
            }
        }

        // 各未解決文情報の名前解決処理
        for (final UnresolvedStatementInfo<? extends StatementInfo> unresolvedStatement : unresolvedLocalSpace
                .getStatements()) {
            if (!(unresolvedStatement instanceof UnresolvedBlockInfo<?>)) {
                final StatementInfo statement = unresolvedStatement.resolve(ownerClass,
                        ownerMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                localSpace.addStatement(statement);
            }
        }

        //　各インナーブロックについて
        for (final UnresolvedStatementInfo<?> unresolvedStatement : unresolvedLocalSpace
                .getStatements()) {

            // 未解決メソッド情報内の利用関係を解決
            if (unresolvedStatement instanceof UnresolvedBlockInfo<?>) {

                this.addReferenceAssignmentCallRelation(
                        (UnresolvedBlockInfo<?>) unresolvedStatement, unresolvedClassInfo,
                        classInfoManager, fieldInfoManager, methodInfoManager);
            }
        }

    }
}
