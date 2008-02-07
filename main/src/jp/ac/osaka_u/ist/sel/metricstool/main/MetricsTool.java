package jp.ac.osaka_u.ist.sel.metricstool.main;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.java.Java15AntlrAstTranslator;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.java.JavaAstVisitorManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitorManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.antlr.AntlrAstVisitor;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.ClassMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.FileMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MethodMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricNotRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFile;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFileManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetInnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableUsageInfo;
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
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.Java15Lexer;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.Java15Parser;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.DefaultPluginLauncher;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.PluginLauncher;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.PluginManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin.PluginInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader.DefaultPluginLoader;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader.PluginLoadException;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;

import org.jargp.ArgumentProcessor;
import org.jargp.BoolDef;
import org.jargp.ParameterDef;
import org.jargp.StringDef;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.collections.AST;


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

        MetricsTool metricsTool = new MetricsTool();

        ArgumentProcessor.processArgs(args, parameterDefs, new Settings());

        // 情報表示用のリスナを作成
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

        // ヘルプモードと情報表示モードが同時にオンになっている場合は不正
        if (Settings.isHelpMode() && Settings.isDisplayMode()) {
            err.println("-h and -x can\'t be set at the same time!");
            metricsTool.printUsage();
            System.exit(0);
        }

        if (Settings.isHelpMode()) {
            // ヘルプモードの場合
            metricsTool.doHelpMode();
        } else {
            LANGUAGE language = metricsTool.getLanguage();
            metricsTool.loadPlugins(language, Settings.getMetricStrings());

            if (Settings.isDisplayMode()) {
                // 情報表示モードの場合
                metricsTool.doDisplayMode(language);
            } else {
                // 解析モード
                metricsTool.doAnalysisMode(language);
            }
        }
    }

    /**
     * 引数無しコンストラクタ． セキュリティマネージャの初期化を行う．
     */
    public MetricsTool() {
        initSecurityManager();
    }

    /**
     * {@link #readTargetFiles()} で読み込んだ対象ファイル群を解析する.
     * 
     * @param language 解析対象の言語
     */
    public void analyzeTargetFiles(final LANGUAGE language) {
        // 対象ファイルを解析

        AstVisitorManager<AST> visitorManager = null;
        if (language.equals(LANGUAGE.JAVA)) {
            visitorManager = new JavaAstVisitorManager<AST>(new AntlrAstVisitor(
                    new Java15AntlrAstTranslator()));
        }

        // 対象ファイルのASTから未解決クラス，フィールド，メソッド情報を取得
        {
            out.println("parsing all target files.");
            final int totalFileNumber = TargetFileManager.getInstance().size();
            int currentFileNumber = 1;
            final StringBuffer fileInformationBuffer = new StringBuffer();

            for (TargetFile targetFile : TargetFileManager.getInstance()) {
                try {
                    final String name = targetFile.getName();

                    final FileInfo fileInfo = new FileInfo(name);
                    FileInfoManager.getInstance().add(fileInfo);

                    if (Settings.isVerbose()) {
                        fileInformationBuffer.delete(0, fileInformationBuffer.length());
                        fileInformationBuffer.append("parsing ");
                        fileInformationBuffer.append(name);
                        fileInformationBuffer.append(" [");
                        fileInformationBuffer.append(currentFileNumber++);
                        fileInformationBuffer.append("/");
                        fileInformationBuffer.append(totalFileNumber);
                        fileInformationBuffer.append("]");
                        out.println(fileInformationBuffer.toString());
                    }

                    final Java15Lexer lexer = new Java15Lexer(new FileInputStream(name));
                    final Java15Parser parser = new Java15Parser(lexer);
                    parser.compilationUnit();
                    targetFile.setCorrectSytax(true);

                    if (visitorManager != null) {
                        visitorManager.setPositionManager(parser.getPositionManger());
                        visitorManager.visitStart(parser.getAST());
                    }

                    fileInfo.setLOC(lexer.getLine());

                } catch (FileNotFoundException e) {
                    err.println(e.getMessage());
                } catch (RecognitionException e) {
                    targetFile.setCorrectSytax(false);
                    err.println(e.getMessage());
                    // TODO エラーが起こったことを TargetFileData などに通知する処理が必要
                } catch (TokenStreamException e) {
                    targetFile.setCorrectSytax(false);
                    err.println(e.getMessage());
                    // TODO エラーが起こったことを TargetFileData などに通知する処理が必要
                }
            }
        }

        out.println("resolving definitions and usages.");
        if (Settings.isVerbose()) {
            out.println("STEP1 : resolve class definitions.");
        }
        registClassInfos();
        if (Settings.isVerbose()) {
            out.println("STEP2 : resolve type parameters of classes.");
        }
        resolveTypeParameterOfClassInfos();
        if (Settings.isVerbose()) {
            out.println("STEP3 : resolve class inheritances.");
        }
        addInheritanceInformationToClassInfos();
        if (Settings.isVerbose()) {
            out.println("STEP4 : resolve field definitions.");
        }
        registFieldInfos();
        if (Settings.isVerbose()) {
            out.println("STEP5 : resolve method definitions.");
        }
        registMethodInfos();
        if (Settings.isVerbose()) {
            out.println("STEP6 : resolve method overrides.");
        }
        addOverrideRelation();
        if (Settings.isVerbose()) {
            out.println("STEP7 : resolve field and method usages.");
        }
        addReferenceAssignmentCallRelateion();

        // 文法誤りのあるファイル一覧を表示
        // err.println("The following files includes uncorrect syntax.");
        // err.println("Any metrics of them were not measured");
        for (TargetFile targetFile : TargetFileManager.getInstance()) {
            if (!targetFile.isCorrectSyntax()) {
                err.println("Incorrect syntax file: " + targetFile.getName());
            }
        }

        out.println("finished.");

        {
            /*
             * for (final ClassInfo classInfo :
             * ClassInfoManager.getInstance().getExternalClassInfos()) {
             * out.println(classInfo.getFullQualifiedName(Settings.getLanguage()
             * .getNamespaceDelimiter())); }
             */
        }
    }

    /**
     * 対象言語を取得する.
     * 
     * @return 指定された対象言語.指定されなかった場合はnull
     */
    public LANGUAGE getLanguage() {
        if (Settings.getLanguageString().equals(Settings.INIT)) {
            return null;
        }

        return Settings.getLanguage();
    }

    /**
     * プラグインをロードする. 指定された言語，指定されたメトリクスに関連するプラグインのみを {@link PluginManager}に登録する.
     * 
     * @param language 指定する言語.
     * @param metrics 指定するメトリクスの配列，指定しない場合はnullまたは空の配列
     */
    public void loadPlugins(final LANGUAGE language, final String[] metrics) {
        // 指定言語に対応するプラグインで指定されたメトリクスを計測するプラグインをロードして登録

        // metrics[]が０個じゃないかつ，2つ以上指定されている or １つだけどデフォルトの文字列じゃない
        boolean metricsSpecified = null != metrics && metrics.length != 0
                && (1 < metrics.length || !metrics[0].equals(Settings.INIT));

        final PluginManager pluginManager = PluginManager.getInstance();
        try {
            for (final AbstractPlugin plugin : (new DefaultPluginLoader()).loadPlugins()) {// プラグインを全ロード
                final PluginInfo info = plugin.getPluginInfo();
                if (null == language || info.isMeasurable(language)) {
                    // 対象言語が指定されていない or 対象言語を計測可能
                    if (metricsSpecified) {
                        // メトリクスが指定されているのでこのプラグインと一致するかチェック
                        final String pluginMetricName = info.getMetricName();
                        for (final String metric : metrics) {
                            if (metric.equalsIgnoreCase(pluginMetricName)) {
                                pluginManager.addPlugin(plugin);
                                break;
                            }
                        }
                    } else {
                        // メトリクスが指定されていないのでとりあえず全部登録
                        pluginManager.addPlugin(plugin);
                    }
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
        PluginLauncher launcher = new DefaultPluginLauncher();
        launcher.setMaximumLaunchingNum(1);
        launcher.launchAll(PluginManager.getInstance().getPlugins());

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

        // ディレクトリから読み込み
        if (!Settings.getTargetDirectory().equals(Settings.INIT)) {
            registerFilesFromDirectory();

            // リストファイルから読み込み
        } else if (!Settings.getListFile().equals(Settings.INIT)) {
            registerFilesFromListFile();
        }
    }

    /**
     * メトリクス情報を {@link Settings} に指定されたファイルに出力する.
     */
    public void writeMetrics() {

        // ファイルメトリクスを計測する場合
        if (0 < PluginManager.getInstance().getFileMetricPlugins().size()) {

            try {
                FileMetricsInfoManager manager = FileMetricsInfoManager.getInstance();
                manager.checkMetrics();

                String fileName = Settings.getFileMetricsFile();
                CSVFileMetricsWriter writer = new CSVFileMetricsWriter(fileName);
                writer.write();

            } catch (MetricNotRegisteredException e) {
                System.exit(0);
            }
        }

        // クラスメトリクスを計測する場合
        if (!Settings.getClassMetricsFile().equals(Settings.INIT)) {

            try {
                ClassMetricsInfoManager manager = ClassMetricsInfoManager.getInstance();
                manager.checkMetrics();

                String fileName = Settings.getClassMetricsFile();
                CSVClassMetricsWriter writer = new CSVClassMetricsWriter(fileName);
                writer.write();

            } catch (MetricNotRegisteredException e) {
                System.exit(0);
            }
        }

        // メソッドメトリクスを計測する場合
        if (!Settings.getMethodMetricsFile().equals(Settings.INIT)) {

            try {
                MethodMetricsInfoManager manager = MethodMetricsInfoManager.getInstance();
                manager.checkMetrics();

                String fileName = Settings.getMethodMetricsFile();
                CSVMethodMetricsWriter writer = new CSVMethodMetricsWriter(fileName);
                writer.write();

            } catch (MetricNotRegisteredException e) {
                System.exit(0);
            }

        }
    }

    /**
     * 
     * ヘルプモードの引数の整合性を確認するためのメソッド． 不正な引数が指定されていた場合，main メソッドには戻らず，この関数内でプログラムを終了する．
     * 
     */
    private void checkHelpModeParameterValidation() {
        // -h は他のオプションと同時指定できない
        if ((!Settings.getTargetDirectory().equals(Settings.INIT))
                || (!Settings.getListFile().equals(Settings.INIT))
                || (!Settings.getLanguageString().equals(Settings.INIT))
                || (!Settings.getMetrics().equals(Settings.INIT))
                || (!Settings.getFileMetricsFile().equals(Settings.INIT))
                || (!Settings.getClassMetricsFile().equals(Settings.INIT))
                || (!Settings.getMethodMetricsFile().equals(Settings.INIT))) {
            err.println("-h can\'t be specified with any other options!");
            printUsage();
            System.exit(0);
        }
    }

    /**
     * 
     * 情報表示モードの引数の整合性を確認するためのメソッド． 不正な引数が指定されていた場合，main メソッドには戻らず，この関数内でプログラムを終了する．
     * 
     */
    private void checkDisplayModeParameterValidation() {
        // -d は使えない
        if (!Settings.getTargetDirectory().equals(Settings.INIT)) {
            err.println("-d can\'t be specified in the display mode!");
            printUsage();
            System.exit(0);
        }

        // -i は使えない
        if (!Settings.getListFile().equals(Settings.INIT)) {
            err.println("-i can't be specified in the display mode!");
            printUsage();
            System.exit(0);
        }

        // -F は使えない
        if (!Settings.getFileMetricsFile().equals(Settings.INIT)) {
            err.println("-F can't be specified in the display mode!");
            printUsage();
            System.exit(0);
        }

        // -C は使えない
        if (!Settings.getClassMetricsFile().equals(Settings.INIT)) {
            err.println("-C can't be specified in the display mode!");
            printUsage();
            System.exit(0);
        }

        // -M は使えない
        if (!Settings.getMethodMetricsFile().equals(Settings.INIT)) {
            err.println("-M can't be specified in the display mode!");
            printUsage();
            System.exit(0);
        }
    }

    /**
     * 
     * 解析モードの引数の整合性を確認するためのメソッド． 不正な引数が指定されていた場合，main メソッドには戻らず，この関数内でプログラムを終了する．
     * 
     * @param 指定された言語
     * 
     */
    private void checkAnalysisModeParameterValidation(LANGUAGE language) {
        // -d と -i のどちらも指定されているのは不正
        if (Settings.getTargetDirectory().equals(Settings.INIT)
                && Settings.getListFile().equals(Settings.INIT)) {
            err.println("-d or -i must be specified in the analysis mode!");
            printUsage();
            System.exit(0);
        }

        // -d と -i の両方が指定されているのは不正
        if (!Settings.getTargetDirectory().equals(Settings.INIT)
                && !Settings.getListFile().equals(Settings.INIT)) {
            err.println("-d and -i can't be specified at the same time!");
            printUsage();
            System.exit(0);
        }

        // 言語が指定されなかったのは不正
        if (null == language) {
            err.println("-l must be specified in the analysis mode.");
            printUsage();
            System.exit(0);
        }

        boolean measureFileMetrics = false;
        boolean measureClassMetrics = false;
        boolean measureMethodMetrics = false;

        for (PluginInfo pluginInfo : PluginManager.getInstance().getPluginInfos()) {
            switch (pluginInfo.getMetricType()) {
            case FILE_METRIC:
                measureFileMetrics = true;
                break;
            case CLASS_METRIC:
                measureClassMetrics = true;
                break;
            case METHOD_METRIC:
                measureMethodMetrics = true;
                break;
            }
        }

        // ファイルメトリクスを計測する場合は -F オプションが指定されていなければならない
        if (measureFileMetrics && (Settings.getFileMetricsFile().equals(Settings.INIT))) {
            err.println("-F must be used for specifying a file for file metrics!");
            System.exit(0);
        }

        // クラスメトリクスを計測する場合は -C オプションが指定されていなければならない
        if (measureClassMetrics && (Settings.getClassMetricsFile().equals(Settings.INIT))) {
            err.println("-C must be used for specifying a file for class metrics!");
            System.exit(0);
        }
        // メソッドメトリクスを計測する場合は -M オプションが指定されていなければならない
        if (measureMethodMetrics && (Settings.getMethodMetricsFile().equals(Settings.INIT))) {
            err.println("-M must be used for specifying a file for method metrics!");
            System.exit(0);
        }
    }

    /**
     * 解析モードを実行する.
     * 
     * @param language 対象言語
     */
    private void doAnalysisMode(LANGUAGE language) {
        checkAnalysisModeParameterValidation(language);

        readTargetFiles();
        analyzeTargetFiles(language);
        launchPlugins();
        writeMetrics();
    }

    /**
     * 情報表示モードを実行する
     * 
     * @param language 対象言語
     */
    private void doDisplayMode(LANGUAGE language) {
        checkDisplayModeParameterValidation();

        // -l で言語が指定されていない場合は，解析可能言語一覧を表示
        if (null == language) {
            err.println("Available languages;");
            LANGUAGE[] languages = LANGUAGE.values();
            for (int i = 0; i < languages.length; i++) {
                err.println("\t" + languages[0].getName() + ": can be specified with term \""
                        + languages[0].getIdentifierName() + "\"");
            }

            // -l で言語が指定されている場合は，そのプログラミング言語で使用可能なメトリクス一覧を表示
        } else {
            err.println("Available metrics for " + language.getName());
            for (AbstractPlugin plugin : PluginManager.getInstance().getPlugins()) {
                PluginInfo pluginInfo = plugin.getPluginInfo();
                if (pluginInfo.isMeasurable(language)) {
                    err.println("\t" + pluginInfo.getMetricName());
                }
            }
            // TODO 利用可能メトリクス一覧を表示
        }
    }

    /**
     * ヘルプモードを実行する.
     */
    private void doHelpMode() {
        checkHelpModeParameterValidation();

        printUsage();
    }

    /**
     * {@link MetricsToolSecurityManager} の初期化を行う. システムに登録できれば，システムのセキュリティマネージャにも登録する.
     */
    private final void initSecurityManager() {
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
     * ツールの使い方（コマンドラインオプション）を表示する．
     * 
     */
    private void printUsage() {

        err.println();
        err.println("Available options:");
        err.println("\t-d: root directory that you are going to analysis.");
        err.println("\t-i: List file including file paths that you are going to analysis.");
        err.println("\t-l: Programming language of the target files.");
        err.println("\t-m: Metrics that you want to get. Metrics names are separated with \',\'.");
        err.println("\t-v: Output progress verbosely.");
        err.println("\t-C: File path that the class type metrics are output");
        err.println("\t-F: File path that the file type metrics are output.");
        err.println("\t-M: File path that the method type metrics are output");

        err.println();
        err.println("Usage:");
        err.println("\t<Help Mode>");
        err.println("\tMetricsTool -h");
        err.println();
        err.println("\t<Display Mode>");
        err.println("\tMetricsTool -x -l");
        err.println("\tMetricsTool -x -l language -m");
        err.println();
        err.println("\t<Analysis Mode>");
        err
                .println("\tMetricsTool -d directory -l language -m metrics1,metrics2 -C file1 -F file2 -M file3");
        err
                .println("\tMetricsTool -i listFile -l language -m metrics1,metrics2 -C file1 -F file2 -M file3");
    }

    /**
     * 
     * リストファイルから対象ファイルを登録する． 読み込みエラーが発生した場合は，このメソッド内でプログラムを終了する．
     */
    private void registerFilesFromListFile() {

        try {

            TargetFileManager targetFiles = TargetFileManager.getInstance();
            for (BufferedReader reader = new BufferedReader(new FileReader(Settings.getListFile())); reader
                    .ready();) {
                String line = reader.readLine();
                TargetFile targetFile = new TargetFile(line);
                targetFiles.add(targetFile);
            }

        } catch (FileNotFoundException e) {
            err.println("\"" + Settings.getListFile() + "\" is not a valid file!");
            System.exit(0);
        } catch (IOException e) {
            err.println("\"" + Settings.getListFile() + "\" can\'t read!");
            System.exit(0);
        }
    }

    /**
     * 
     * registerFilesFromDirectory(File file)を呼び出すのみ． mainメソッドで new File(Settings.getTargetDirectory)
     * するのが気持ち悪かったため作成．
     * 
     */
    private void registerFilesFromDirectory() {

        File targetDirectory = new File(Settings.getTargetDirectory());
        registerFilesFromDirectory(targetDirectory);
    }

    /**
     * 
     * @param file 対象ファイルまたはディレクトリ
     * 
     * 対象がディレクトリの場合は，その子に対して再帰的に処理をする． 対象がファイルの場合は，対象言語のソースファイルであれば，登録処理を行う．
     */
    private void registerFilesFromDirectory(File file) {

        // ディレクトリならば，再帰的に処理
        if (file.isDirectory()) {
            File[] subfiles = file.listFiles();
            for (int i = 0; i < subfiles.length; i++) {
                registerFilesFromDirectory(subfiles[i]);
            }

            // ファイルならば，拡張子が対象言語と一致すれば登録
        } else if (file.isFile()) {

            final LANGUAGE language = Settings.getLanguage();
            final String extension = language.getExtension();
            final String path = file.getAbsolutePath();
            if (path.endsWith(extension)) {
                final TargetFileManager targetFiles = TargetFileManager.getInstance();
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
     * 引数の仕様を Jargp に渡すための配列．
     */
    private static ParameterDef[] parameterDefs = {
            new BoolDef('h', "helpMode", "display usage", true),
            new BoolDef('x', "displayMode", "display available language or metrics", true),
            new BoolDef('v', "verbose", "output progress verbosely", true),
            new StringDef('d', "targetDirectory", "Target directory"),
            new StringDef('i', "listFile", "List file including paths of target files"),
            new StringDef('l', "language", "Programming language"),
            new StringDef('m', "metrics", "Measured metrics"),
            new StringDef('F', "fileMetricsFile", "File storing file metrics"),
            new StringDef('C', "classMetricsFile", "File storing class metrics"),
            new StringDef('M', "methodMetricsFile", "File storing method metrics") };

    /**
     * 出力メッセージ出力用のプリンタ
     */
    private static final MessagePrinter out = new DefaultMessagePrinter(new MessageSource() {
        public String getMessageSourceName() {
            return "main";
        }
    }, MESSAGE_TYPE.OUT);

    /**
     * エラーメッセージ出力用のプリンタ
     */
    private static final MessagePrinter err = new DefaultMessagePrinter(new MessageSource() {
        public String getMessageSourceName() {
            return "main";
        }
    }, MESSAGE_TYPE.ERROR);

    /**
     * クラスの定義を ClassInfoManager に登録する．AST パースの後に呼び出さなければならない．
     */
    private void registClassInfos() {

        // 未解決クラス情報マネージャ， クラス情報マネージャを取得
        final UnresolvedClassInfoManager unresolvedClassInfoManager = UnresolvedClassInfoManager
                .getInstance();
        final ClassInfoManager classInfoManager = ClassInfoManager.getInstance();

        // 各未解決クラスに対して
        for (final UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager
                .getClassInfos()) {

            //　クラス情報を解決
            final TargetClassInfo classInfo = unresolvedClassInfo.resolveUnit(null, null,
                    classInfoManager, null, null);

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
                classInfoManager.add(classInfo);
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

        final TargetInnerClassInfo classInfo = (TargetInnerClassInfo) unresolvedClassInfo
                .resolveUnit(outerClass, null, classInfoManager, null, null);

        // このクラスのインナークラスに対して再帰的に処理
        for (final UnresolvedClassInfo unresolvedInnerClassInfo : unresolvedClassInfo
                .getInnerClasses()) {

            //　インナークラス情報を解決
            final TargetInnerClassInfo innerClass = registInnerClassInfo(unresolvedInnerClassInfo,
                    classInfo, classInfoManager);

            // 解決されたインナークラス情報を登録
            classInfo.addInnerClass(innerClass);
            classInfoManager.add(classInfo);
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
        final UnresolvedClassInfoManager unresolvedClassInfoManager = UnresolvedClassInfoManager
                .getInstance();
        final ClassInfoManager classInfoManager = ClassInfoManager.getInstance();

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
        final TargetClassInfo classInfo = unresolvedClassInfo.getResolvedUnit();
        assert null != classInfo : "classInfo shouldn't be null!";

        // 未解決クラス情報から未解決型パラメータを取得し，型解決を行った後，解決済みクラス情報に付与する
        for (final UnresolvedTypeParameterInfo unresolvedTypeParameter : unresolvedClassInfo
                .getTypeParameters()) {

            final TypeInfo typeParameter = unresolvedTypeParameter.resolveType(classInfo, null,
                    classInfoManager, null, null);
            classInfo.addTypeParameter((TypeParameterInfo) typeParameter);
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
        final UnresolvedClassInfoManager unresolvedClassInfoManager = UnresolvedClassInfoManager
                .getInstance();
        final ClassInfoManager classInfoManager = ClassInfoManager.getInstance();

        // 名前解決不可能クラスを保存するためのリスト
        final List<UnresolvedClassInfo> unresolvableClasses = new LinkedList<UnresolvedClassInfo>();

        // 各 Unresolvedクラスに対して
        for (UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager.getClassInfos()) {
            addInheritanceInformationToClassInfo(unresolvedClassInfo, classInfoManager,
                    unresolvableClasses);
        }

        // 名前解決不可能クラスを解析する
        for (int i = 0; i < 100; i++) {

            CLASSLOOP: for (final Iterator<UnresolvedClassInfo> classIterator = unresolvableClasses
                    .iterator(); classIterator.hasNext();) {

                // ClassInfo を取得
                final UnresolvedClassInfo unresolvedClassInfo = classIterator.next();
                final TargetClassInfo classInfo = unresolvedClassInfo.getResolvedUnit();
                assert null != classInfo : "classInfo shouldn't be null!";

                // 各親クラス名に対して
                for (final UnresolvedClassTypeInfo unresolvedSuperClassType : unresolvedClassInfo
                        .getSuperClasses()) {

                    TypeInfo superClassType = unresolvedSuperClassType.resolveType(classInfo, null,
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
            final List<UnresolvedClassInfo> unresolvableClasses) {

        // ClassInfo を取得
        final TargetClassInfo classInfo = unresolvedClassInfo.getResolvedUnit();
        assert null != classInfo : "classInfo shouldn't be null!";

        // 各親クラス名に対して
        for (final UnresolvedClassTypeInfo unresolvedSuperClassType : unresolvedClassInfo
                .getSuperClasses()) {

            TypeInfo superClassType = unresolvedSuperClassType.resolveType(classInfo, null,
                    classInfoManager, null, null);

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
                    unresolvableClasses);
        }
    }

    /**
     * フィールドの定義を FieldInfoManager に登録する． registClassInfos の後に呼び出さなければならない
     * 
     */
    private void registFieldInfos() {

        // Unresolved クラス情報マネージャ，クラス情報マネージャ，フィールド情報マネージャを取得
        final UnresolvedClassInfoManager unresolvedClassInfoManager = UnresolvedClassInfoManager
                .getInstance();
        final ClassInfoManager classInfoManager = ClassInfoManager.getInstance();
        final FieldInfoManager fieldInfoManager = FieldInfoManager.getInstance();

        // 各 Unresolvedクラスに対して
        for (final UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager
                .getClassInfos()) {
            registFieldInfos(unresolvedClassInfo, classInfoManager, fieldInfoManager);
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
            final ClassInfoManager classInfoManager, final FieldInfoManager fieldInfoManager) {

        // ClassInfo を取得
        final TargetClassInfo ownerClass = unresolvedClassInfo.getResolvedUnit();
        assert null != ownerClass : "ownerClass shouldn't be null!";

        // 各未解決フィールドに対して
        for (final UnresolvedFieldInfo unresolvedFieldInfo : unresolvedClassInfo.getDefinedFields()) {

            unresolvedFieldInfo.resolveUnit(ownerClass, null, classInfoManager, fieldInfoManager,
                    null);
        }

        // 各インナークラスに対して
        for (final UnresolvedClassInfo unresolvedInnerClassInfo : unresolvedClassInfo
                .getInnerClasses()) {
            registFieldInfos(unresolvedInnerClassInfo, classInfoManager, fieldInfoManager);
        }
    }

    /**
     * メソッドの定義を MethodInfoManager に登録する．registClassInfos の後に呼び出さなければならない．
     */
    private void registMethodInfos() {

        // Unresolved クラス情報マネージャ， クラス情報マネージャ，メソッド情報マネージャを取得
        final UnresolvedClassInfoManager unresolvedClassInfoManager = UnresolvedClassInfoManager
                .getInstance();
        final ClassInfoManager classInfoManager = ClassInfoManager.getInstance();
        final MethodInfoManager methodInfoManager = MethodInfoManager.getInstance();

        // 各 Unresolvedクラスに対して
        for (final UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager
                .getClassInfos()) {
            registMethodInfos(unresolvedClassInfo, classInfoManager, methodInfoManager);
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
            final ClassInfoManager classInfoManager, final MethodInfoManager methodInfoManager) {

        // ClassInfo を取得
        final TargetClassInfo ownerClass = unresolvedClassInfo.getResolvedUnit();

        // 各未解決メソッドに対して
        for (final UnresolvedMethodInfo unresolvedMethodInfo : unresolvedClassInfo
                .getDefinedMethods()) {

            // メソッド情報を解決
            final TargetMethodInfo methodInfo = unresolvedMethodInfo.resolveUnit(ownerClass, null,
                    classInfoManager, null, methodInfoManager);

            // メソッド情報を登録
            ownerClass.addDefinedMethod(methodInfo);
            methodInfoManager.add(methodInfo);
        }

        // 各未解決コンストラクタに対して
        for (final UnresolvedConstructorInfo unresolvedConstructorInfo : unresolvedClassInfo
                .getDefinedConstructors()) {

            //　コンストラクタ情報を解決
            final TargetConstructorInfo constructorInfo = unresolvedConstructorInfo.resolveUnit(
                    ownerClass, null, classInfoManager, null, methodInfoManager);
            methodInfoManager.add(constructorInfo);

            // コンストラクタ情報を登録            
            ownerClass.addDefinedConstructor(constructorInfo);
            methodInfoManager.add(constructorInfo);

        }

        // 各 Unresolvedクラスに対して
        for (final UnresolvedClassInfo unresolvedInnerClassInfo : unresolvedClassInfo
                .getInnerClasses()) {
            registMethodInfos(unresolvedInnerClassInfo, classInfoManager, methodInfoManager);
        }
    }

    /**
     * メソッドオーバーライド情報を各MethodInfoに追加する．addInheritanceInfomationToClassInfos の後 かつ registMethodInfos
     * の後に呼び出さなければならない
     */
    private void addOverrideRelation() {

        // 全ての対象クラスに対して
        for (TargetClassInfo classInfo : ClassInfoManager.getInstance().getTargetClassInfos()) {
            addOverrideRelation(classInfo);
        }
    }

    /**
     * メソッドオーバーライド情報を各MethodInfoに追加する．引数で指定したクラスのメソッドについて処理を行う
     * 
     * @param classInfo 対象クラス
     */
    private void addOverrideRelation(final TargetClassInfo classInfo) {

        // 各親クラスに対して
        for (final ClassInfo superClassInfo : ClassTypeInfo.convert(classInfo.getSuperClasses())) {

            // 各対象クラスの各メソッドについて，親クラスのメソッドをオーバーライドしているかを調査
            for (final MethodInfo methodInfo : classInfo.getDefinedMethods()) {
                addOverrideRelation(superClassInfo, methodInfo);
            }
        }

        // 各インナークラスに対して
        for (ClassInfo innerClassInfo : classInfo.getInnerClasses()) {
            addOverrideRelation((TargetClassInfo) innerClassInfo);
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
            throw new NullPointerException();
        }

        if (!(classInfo instanceof TargetClassInfo)) {
            return;
        }

        for (final TargetMethodInfo methodInfo : ((TargetClassInfo) classInfo).getDefinedMethods()) {

            // メソッド名が違う場合はオーバーライドされない
            if (!methodInfo.getMethodName().equals(overrider.getMethodName())) {
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
    private void addReferenceAssignmentCallRelateion() {

        final UnresolvedClassInfoManager unresolvedClassInfoManager = UnresolvedClassInfoManager
                .getInstance();
        final ClassInfoManager classInfoManager = ClassInfoManager.getInstance();
        final FieldInfoManager fieldInfoManager = FieldInfoManager.getInstance();
        final MethodInfoManager methodInfoManager = MethodInfoManager.getInstance();

        // 各未解決クラス情報 に対して
        for (final UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager
                .getClassInfos()) {
            addReferenceAssignmentCallRelation(unresolvedClassInfo, classInfoManager,
                    fieldInfoManager, methodInfoManager);
        }
    }

    /**
     * エンティティ（フィールドやクラス）の代入・参照，メソッドの呼び出し関係を追加する．
     * 
     * @param unresolvedClassInfo 解決対象クラス
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     * @param resolvedCache 解決済み呼び出し情報のキャッシュ
     */
    private void addReferenceAssignmentCallRelation(final UnresolvedClassInfo unresolvedClassInfo,
            final ClassInfoManager classInfoManager, final FieldInfoManager fieldInfoManager,
            final MethodInfoManager methodInfoManager) {

        // 未解決クラス情報から，解決済みクラス情報を取得
        // final TargetClassInfo ownerClass = unresolvedClassInfo.getResolvedUnit();

        // 各未解決メソッド情報に対して
        for (final UnresolvedMethodInfo unresolvedMethodInfo : unresolvedClassInfo
                .getDefinedMethods()) {

            // 未解決メソッド情報内の利用関係を解決
            this.addReferenceAssignmentCallRelation(unresolvedMethodInfo, unresolvedClassInfo,
                    classInfoManager, fieldInfoManager, methodInfoManager);
        }

        // 各インナークラスに対して
        for (final UnresolvedClassInfo unresolvedInnerClassInfo : unresolvedClassInfo
                .getInnerClasses()) {
            addReferenceAssignmentCallRelation(unresolvedInnerClassInfo, classInfoManager,
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
     * @param resolvedCache 解決済み呼び出し情報のキャッシュ
     */
    private void addReferenceAssignmentCallRelation(
            final UnresolvedLocalSpaceInfo<?> unresolvedLocalSpace,
            final UnresolvedClassInfo unresolvedClassInfo, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 未解決メソッド情報から，解決済みメソッド情報を取得
        final LocalSpaceInfo localSpace = unresolvedLocalSpace.getResolvedUnit();
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

        // 各未解決フィールド使用の名前解決処理
        for (final UnresolvedVariableUsageInfo unresolvedVariableUsage : unresolvedLocalSpace
                .getVariableUsages()) {

            // 未解決変数使用を解決
            final EntityUsageInfo variableUsage = unresolvedVariableUsage.resolveEntityUsage(
                    ownerClass, ownerMethod, classInfoManager, fieldInfoManager, methodInfoManager);

            // 名前解決でき場合は登録
            if (variableUsage instanceof VariableUsageInfo) {
                ownerMethod.addVariableUsage((VariableUsageInfo<?>) variableUsage);

                // フィールドの場合は，利用関係情報を取る
                if (variableUsage instanceof FieldUsageInfo) {
                    final boolean reference = ((FieldUsageInfo) variableUsage).isReference();
                    final FieldInfo usedField = ((FieldUsageInfo) variableUsage).getUsedVariable();
                    if (reference) {
                        usedField.addReferencer(ownerMethod);
                    } else {
                        usedField.addAssignmenter(ownerMethod);
                    }
                }
            }
        }

        // 各未解決メソッド呼び出しの解決処理
        for (final UnresolvedCallInfo unresolvedCall : unresolvedLocalSpace.getCalls()) {

            final EntityUsageInfo memberCall = unresolvedCall.resolveEntityUsage(ownerClass,
                    ownerMethod, classInfoManager, fieldInfoManager, methodInfoManager);

            // メソッドおよびコンストラクタ呼び出しが解決できた場合
            if (memberCall instanceof MethodCallInfo) {
                ownerMethod.addCall((MethodCallInfo) memberCall);
                ((MethodCallInfo) memberCall).getCallee().addCaller(ownerMethod);
            } else if (memberCall instanceof ConstructorCallInfo) {
                ownerMethod.addCall((ConstructorCallInfo) memberCall);
            }
        }

        //　各インナーブロックについて
        for (final UnresolvedBlockInfo<?> unresolvedBlockInfo : unresolvedLocalSpace.getInnerBlocks()) {
            
            // 未解決メソッド情報内の利用関係を解決
            this.addReferenceAssignmentCallRelation(unresolvedBlockInfo, unresolvedClassInfo,
                    classInfoManager, fieldInfoManager, methodInfoManager);
        }
    }
}
