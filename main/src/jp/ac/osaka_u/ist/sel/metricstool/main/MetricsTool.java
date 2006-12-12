package jp.ac.osaka_u.ist.sel.metricstool.main;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.ClassMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.FileMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MethodMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricNotRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFile;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFileManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetInnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.NameResolver;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldUsage;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMethodCall;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
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
import jp.ac.osaka_u.ist.sel.metricstool.main.util.UnavailableLanguageException;

import org.jargp.ArgumentProcessor;
import org.jargp.BoolDef;
import org.jargp.ParameterDef;
import org.jargp.StringDef;

import antlr.RecognitionException;
import antlr.TokenStreamException;


/**
 * 
 * @author y-higo
 * 
 * MetricsToolのメインクラス． 現在は仮実装．
 * 
 * since 2006.11.12
 * 
 */
public class MetricsTool {

    static {
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

        initSecurityManager();
    }

    /**
     * 
     * @param args 対象ファイルのファイルパス
     * 
     * 現在仮実装． 対象ファイルのデータを格納した後，構文解析を行う．
     */
    public static void main(String[] args) {
        ArgumentProcessor.processArgs(args, parameterDefs, new Settings());

        // ヘルプモードと情報表示モードが同時にオンになっている場合は不正
        if (Settings.isHelpMode() && Settings.isDisplayMode()) {
            System.err.println("-h and -x can\'t be set at the same time!");
            printUsage();
            System.exit(0);
        }

        if (Settings.isHelpMode()) {
            // ヘルプモードの場合
            doHelpMode();
        } else {
            LANGUAGE language = getLanguage();
            loadPlugins(language, Settings.getMetricStrings());

            if (Settings.isDisplayMode()) {
                // 情報表示モードの場合
                doDisplayMode(language);
            } else {
                // 解析モード
                doAnalysisMode(language);
            }
        }
    }

    /**
     * 読み込んだ対象ファイル群を解析する.
     * 
     * @param language 解析対象の言語
     */
    private static void analysisTargetFiles(final LANGUAGE language) {
        // 対象ファイルを解析
        // TODO ここか専用の解析部で，言語ごとに分岐
        for (TargetFile targetFile : TargetFileManager.getInstance()) {
            try {
                String name = targetFile.getName();

                out.println("processing " + name);
                Java15Lexer lexer = new Java15Lexer(new FileInputStream(name));
                Java15Parser parser = new Java15Parser(lexer);
                parser.compilationUnit();
                targetFile.setCorrectSytax(true);

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

        // 文法誤りのあるファイル一覧を表示
        err.println("The following files includes uncorrect syntax.");
        err.println("Any metrics of them were not measured");
        for (TargetFile targetFile : TargetFileManager.getInstance()) {
            if (!targetFile.isCorrectSyntax()) {
                err.println("\t" + targetFile.getName());
            }
        }
    }

    /**
     * 
     * ヘルプモードの引数の整合性を確認するためのメソッド． 不正な引数が指定されていた場合，main メソッドには戻らず，この関数内でプログラムを終了する．
     * 
     */
    private static void checkHelpModeParameterValidation() {
        // -h は他のオプションと同時指定できない
        if ((!Settings.getTargetDirectory().equals(Settings.INIT))
                || (!Settings.getListFile().equals(Settings.INIT))
                || (!Settings.getLanguageString().equals(Settings.INIT))
                || (!Settings.getMetrics().equals(Settings.INIT))
                || (!Settings.getFileMetricsFile().equals(Settings.INIT))
                || (!Settings.getClassMetricsFile().equals(Settings.INIT))
                || (!Settings.getMethodMetricsFile().equals(Settings.INIT))) {
            System.err.println("-h can\'t be specified with any other options!");
            printUsage();
            System.exit(0);
        }
    }

    /**
     * 
     * 情報表示モードの引数の整合性を確認するためのメソッド． 不正な引数が指定されていた場合，main メソッドには戻らず，この関数内でプログラムを終了する．
     * 
     */
    private static void checkDisplayModeParameterValidation() {
        // -d は使えない
        if (!Settings.getTargetDirectory().equals(Settings.INIT)) {
            System.err.println("-d can\'t be specified in the display mode!");
            printUsage();
            System.exit(0);
        }

        // -i は使えない
        if (!Settings.getListFile().equals(Settings.INIT)) {
            System.err.println("-i can't be specified in the display mode!");
            printUsage();
            System.exit(0);
        }

        // -F は使えない
        if (!Settings.getFileMetricsFile().equals(Settings.INIT)) {
            System.err.println("-F can't be specified in the display mode!");
            printUsage();
            System.exit(0);
        }

        // -C は使えない
        if (!Settings.getClassMetricsFile().equals(Settings.INIT)) {
            System.err.println("-C can't be specified in the display mode!");
            printUsage();
            System.exit(0);
        }

        // -M は使えない
        if (!Settings.getMethodMetricsFile().equals(Settings.INIT)) {
            System.err.println("-M can't be specified in the display mode!");
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
    private static void checkAnalysisModeParameterValidation(LANGUAGE language) {
        // -d と -i のどちらも指定されているのは不正
        if (Settings.getTargetDirectory().equals(Settings.INIT)
                && Settings.getListFile().equals(Settings.INIT)) {
            System.err.println("-d or -i must be specified in the analysis mode!");
            printUsage();
            System.exit(0);
        }

        // -d と -i の両方が指定されているのは不正
        if (!Settings.getTargetDirectory().equals(Settings.INIT)
                && !Settings.getListFile().equals(Settings.INIT)) {
            System.err.println("-d and -i can't be specified at the same time!");
            printUsage();
            System.exit(0);
        }

        // 言語が指定されなかったのは不正
        if (null == language) {
            System.err.println("-l must be specified in the analysis mode.");
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
            System.err.println("-F must be used for specifying a file for file metrics!");
            System.exit(0);
        }

        // クラスメトリクスを計測する場合は -C オプションが指定されていなければならない
        if (measureClassMetrics && (Settings.getClassMetricsFile().equals(Settings.INIT))) {
            System.err.println("-C must be used for specifying a file for class metrics!");
            System.exit(0);
        }
        // メソッドメトリクスを計測する場合は -M オプションが指定されていなければならない
        if (measureMethodMetrics && (Settings.getMethodMetricsFile().equals(Settings.INIT))) {
            System.err.println("-M must be used for specifying a file for method metrics!");
            System.exit(0);
        }
    }

    /**
     * 解析モードを実行する.
     * 
     * @param language 対象言語
     */
    private static void doAnalysisMode(LANGUAGE language) {
        checkAnalysisModeParameterValidation(language);

        readTargetFiles();
        analysisTargetFiles(language);
        launchPlugins();
        writeMetrics();
    }

    /**
     * 情報表示モードを実行する
     * 
     * @param language 対象言語
     */
    private static void doDisplayMode(LANGUAGE language) {
        checkDisplayModeParameterValidation();

        // -l で言語が指定されていない場合は，解析可能言語一覧を表示
        if (null == language) {
            System.err.println("Available languages;");
            LANGUAGE[] languages = LANGUAGE.values();
            for (int i = 0; i < languages.length; i++) {
                System.err.println("\t" + languages[0].getName()
                        + ": can be specified with term \"" + languages[0].getIdentifierName()
                        + "\"");
            }

            // -l で言語が指定されている場合は，そのプログラミング言語で使用可能なメトリクス一覧を表示
        } else {
            System.err.println("Available metrics for " + language.getName());
            for (AbstractPlugin plugin : PluginManager.getInstance().getPlugins()) {
                PluginInfo pluginInfo = plugin.getPluginInfo();
                if (pluginInfo.isMeasurable(language)) {
                    System.err.println("\t" + pluginInfo.getMetricName());
                }
            }
            // TODO 利用可能メトリクス一覧を表示
        }
    }

    /**
     * ヘルプモードを実行する.
     */
    private static void doHelpMode() {
        checkHelpModeParameterValidation();

        printUsage();
    }

    /**
     * 対象言語を取得する.
     * 
     * @return 指定された対象言語.指定されなかった場合はnull
     */
    private static LANGUAGE getLanguage() {
        if (Settings.getLanguageString().equals(Settings.INIT)) {
            return null;
        }

        try {
            return Settings.getLanguage();
        } catch (UnavailableLanguageException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }

        return null;// 来ないけど書かないとコンパイル通らないので
    }

    /**
     * {@link MetricsToolSecurityManager} の初期化を行う. システムに登録できれば，システムのセキュリティマネージャにも登録する.
     */
    private static void initSecurityManager() {
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
     * ロード済みのプラグインを実行する.
     */
    private static void launchPlugins() {
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
     * プラグインをロードする. 指定された言語，指定されたメトリクスに関連するプラグインのみを {@link PluginManager}に登録する.
     * 
     * @param language 指定しされた言語.
     */
    private static void loadPlugins(final LANGUAGE language, final String[] metrics) {
        // 指定言語に対応するプラグインで指定されたメトリクスを計測するプラグインをロードして登録

        // metrics[]が０個じゃないかつ，2つ以上指定されている or １つだけどデフォルトの文字列じゃない
        boolean metricsSpecified = metrics.length != 0
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
                            if (metric.equals(pluginMetricName)) {
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
     * 
     * ツールの使い方（コマンドラインオプション）を表示する．
     * 
     */
    private static void printUsage() {

        System.err.println();
        System.err.println("Available options:");
        System.err.println("\t-d: root directory that you are going to analysis.");
        System.err.println("\t-i: List file including file paths that you are going to analysis.");
        System.err.println("\t-l: Programming language of the target files.");
        System.err
                .println("\t-m: Metrics that you want to get. Metrics names are separated with \',\'.");
        System.err.println("\t-C: File path that the class type metrics are output");
        System.err.println("\t-F: File path that the file type metrics are output.");
        System.err.println("\t-M: File path that the method type metrics are output");

        System.err.println();
        System.err.println("Usage:");
        System.err.println("\t<Help Mode>");
        System.err.println("\tMetricsTool -h");
        System.err.println();
        System.err.println("\t<Display Mode>");
        System.err.println("\tMetricsTool -x -l");
        System.err.println("\tMetricsTool -x -l language -m");
        System.err.println();
        System.err.println("\t<Analysis Mode>");
        System.err
                .println("\tMetricsTool -d directory -l language -m metrics1,metrics2 -C file1 -F file2 -M file3");
        System.err
                .println("\tMetricsTool -l listFile -l language -m metrics1,metrics2 -C file1 -F file2 -M file3");
    }

    /**
     * 解析対象ファイルを登録
     */
    private static void readTargetFiles() {

        // ディレクトリから読み込み
        if (!Settings.getTargetDirectory().equals(Settings.INIT)) {
            registerFilesFromDirectory();

            // リストファイルから読み込み
        } else if (!Settings.getListFile().equals(Settings.INIT)) {
            registerFilesFromListFile();
        }
    }

    /**
     * 
     * リストファイルから対象ファイルを登録する． 読み込みエラーが発生した場合は，このメソッド内でプログラムを終了する．
     */
    private static void registerFilesFromListFile() {

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
    private static void registerFilesFromDirectory() {

        File targetDirectory = new File(Settings.getTargetDirectory());
        registerFilesFromDirectory(targetDirectory);
    }

    /**
     * 
     * @param file 対象ファイルまたはディレクトリ
     * 
     * 対象がディレクトリの場合は，その子に対して再帰的に処理をする． 対象がファイルの場合は，対象言語のソースファイルであれば，登録処理を行う．
     */
    private static void registerFilesFromDirectory(File file) {

        // ディレクトリならば，再帰的に処理
        if (file.isDirectory()) {
            File[] subfiles = file.listFiles();
            for (int i = 0; i < subfiles.length; i++) {
                registerFilesFromDirectory(subfiles[i]);
            }

            // ファイルならば，拡張子が対象言語と一致すれば登録
        } else if (file.isFile()) {

            try {
                LANGUAGE language = Settings.getLanguage();
                String extension = language.getExtension();
                String path = file.getAbsolutePath();
                if (path.endsWith(extension)) {
                    TargetFileManager targetFiles = TargetFileManager.getInstance();
                    TargetFile targetFile = new TargetFile(path);
                    targetFiles.add(targetFile);
                }
            } catch (UnavailableLanguageException e) {
                err.println(e.getMessage());
                System.exit(0);
            }

            // ディレクトリでもファイルでもない場合は不正
        } else {
            err.println("\"" + file.getAbsolutePath() + "\" is not a vaild file!");
            System.exit(0);
        }
    }

    /**
     * メトリクス情報をファイルに出力.
     */
    private static void writeMetrics() {
        if (!Settings.getFileMetricsFile().equals(Settings.INIT)) {

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
     * 引数の仕様を Jargp に渡すための配列．
     */
    private static ParameterDef[] parameterDefs = {
            new BoolDef('h', "helpMode", "display usage", true),
            new BoolDef('x', "displayMode", "display available language or metrics", true),
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
    private static void registClassInfos() {

        // Unresolved クラス情報マネージャ， クラス情報マネージャを取得
        UnresolvedClassInfoManager unresolvedClassInfoManager = UnresolvedClassInfoManager
                .getInstance();
        ClassInfoManager classInfoManager = ClassInfoManager.getInstance();

        // 各 Unresolvedクラスに対して
        for (UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager.getClassInfos()) {

            // 修飾子，完全限定名，行数を取得
            ModifierInfo modifier = unresolvedClassInfo.getModifier();
            String[] fullQualifiedName = unresolvedClassInfo.getFullQualifiedName();
            int loc = unresolvedClassInfo.getLOC();

            // ClassInfo オブジェクトを作成し，ClassInfoManagerに登録
            TargetClassInfo classInfo = new TargetClassInfo(modifier, fullQualifiedName, loc);
            classInfoManager.add(classInfo);

            for (UnresolvedClassInfo unresolvedInnerClassInfo : unresolvedClassInfo
                    .getInnerClasses()) {
                final TargetInnerClassInfo innerClass = registInnerClassInfo(
                        unresolvedInnerClassInfo, classInfo, classInfoManager);
                classInfo.addInnerClass(innerClass);
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
    private static TargetInnerClassInfo registInnerClassInfo(
            final UnresolvedClassInfo unresolvedClassInfo, final TargetClassInfo outerClass,
            final ClassInfoManager classInfoManager) {

        // 修飾子，完全限定名，行数を取得
        ModifierInfo modifier = unresolvedClassInfo.getModifier();
        String[] fullQualifiedName = unresolvedClassInfo.getFullQualifiedName();
        int loc = unresolvedClassInfo.getLOC();

        // ClassInfo オブジェクトを生成し，ClassInfoマネージャに登録
        TargetInnerClassInfo classInfo = new TargetInnerClassInfo(modifier, fullQualifiedName,
                outerClass, loc);
        classInfoManager.add(classInfo);

        // このクラスのインナークラスに対して再帰的に処理
        for (UnresolvedClassInfo unresolvedInnerClassInfo : unresolvedClassInfo.getInnerClasses()) {
            final TargetInnerClassInfo innerClass = registInnerClassInfo(unresolvedInnerClassInfo,
                    classInfo, classInfoManager);
            classInfo.addInnerClass(innerClass);
        }

        // このクラスの ClassInfo を返す
        return classInfo;
    }

    /**
     * クラスの継承情報を ClassInfo に追加する．一度目の AST パースの後，かつ registClassInfos の後によびださなければならない．
     */
    private static void addInheritanceInformationToClassInfos() {

        // Unresolved クラス情報マネージャ， クラス情報マネージャを取得
        UnresolvedClassInfoManager unresolvedClassInfoManager = UnresolvedClassInfoManager
                .getInstance();
        ClassInfoManager classInfoManager = ClassInfoManager.getInstance();

        // 各 Unresolvedクラスに対して
        for (UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager.getClassInfos()) {

            // ClassInfo を取得
            ClassInfo classInfo = NameResolver.resolveClassInfo(unresolvedClassInfo,
                    classInfoManager);

            // 各Unresolvedな親クラス名に対して
            for (UnresolvedTypeInfo unresolvedSuperClassType : unresolvedClassInfo
                    .getSuperClasses()) {
                final ClassInfo superClass = (ClassInfo) NameResolver.resolveTypeInfo(
                        unresolvedSuperClassType, classInfoManager);
                classInfo.addSuperClass(superClass);
                superClass.addSubClass(classInfo);
            }
        }
    }

    /**
     * フィールドの定義を FieldInfoManager に登録する． registClassInfos の後に呼び出さなければならない
     * 
     */
    private static void registFieldInfos() {

        // Unresolved クラス情報マネージャ，クラス情報マネージャ，フィールド情報マネージャを取得
        UnresolvedClassInfoManager unresolvedClassInfoManager = UnresolvedClassInfoManager
                .getInstance();
        ClassInfoManager classInfoManager = ClassInfoManager.getInstance();
        FieldInfoManager fieldInfoManager = FieldInfoManager.getInstance();

        // 各 Unresolvedクラスに対して
        for (UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager.getClassInfos()) {

            // ClassInfo を取得
            ClassInfo ownerClass = NameResolver.resolveClassInfo(unresolvedClassInfo,
                    classInfoManager);
            if (!(ownerClass instanceof TargetClassInfo)) {
                throw new IllegalArgumentException(ownerClass.toString()
                        + " must be an instance of TargetClassInfo!");
            }

            // Unresolvedクラスに定義されている各Unresolvedフィールドに対して
            for (UnresolvedFieldInfo unresolvedFieldInfo : unresolvedClassInfo.getDefinedFields()) {

                // フィールドの修飾子を取得
                ModifierInfo modifier = unresolvedFieldInfo.getModifier();

                // フィールド名を取得
                String fieldName = unresolvedFieldInfo.getName();

                // フィールドの型を取得
                UnresolvedTypeInfo unresolvedFieldType = unresolvedFieldInfo.getType();
                TypeInfo fieldType = NameResolver.resolveTypeInfo(unresolvedFieldType,
                        classInfoManager);

                // TODO フィールドの修飾子に関する処理を追加

                // フィールドオブジェクトを生成
                TargetFieldInfo fieldInfo = new TargetFieldInfo(modifier, fieldName, fieldType,
                        ownerClass);

                // フィールド情報を追加
                ((TargetClassInfo) ownerClass).addDefinedField(fieldInfo);
                fieldInfoManager.add(fieldInfo);
            }
        }
    }

    /**
     * メソッドの定義を MethodInfoManager に登録する．registClassInfos の後に呼び出さなければならない．
     */
    private static void registMethodInfos() {

        // Unresolved クラス情報マネージャ， クラス情報マネージャ，メソッド情報マネージャを取得
        UnresolvedClassInfoManager unresolvedClassInfoManager = UnresolvedClassInfoManager
                .getInstance();
        ClassInfoManager classInfoManager = ClassInfoManager.getInstance();
        MethodInfoManager methodInfoManager = MethodInfoManager.getInstance();

        // 各 Unresolvedクラスに対して
        for (UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager.getClassInfos()) {

            // ClassInfo を取得
            ClassInfo ownerClass = NameResolver.resolveClassInfo(unresolvedClassInfo,
                    classInfoManager);
            if (!(ownerClass instanceof TargetClassInfo)) {
                throw new IllegalArgumentException(ownerClass.toString()
                        + " must be an instance of TargetClassInfo");
            }

            // Unresolvedクラスに定義されている各Unresolvedメソッドに対して
            for (UnresolvedMethodInfo unresolvedMethodInfo : unresolvedClassInfo
                    .getDefinedMethods()) {

                ModifierInfo modifier = unresolvedMethodInfo.getModifier();

                // メソッド名を取得
                String methodName = unresolvedMethodInfo.getMethodName();

                // TODO メソッドの修飾子に関する処理を追加

                // メソッドの返り値を取得
                UnresolvedTypeInfo unresolvedReturnType = unresolvedMethodInfo.getReturnType();
                TypeInfo returnType = NameResolver.resolveTypeInfo(unresolvedReturnType,
                        classInfoManager);

                // 行数，コンストラクタかどうかを取得
                int loc = unresolvedMethodInfo.getLOC();
                boolean constructor = unresolvedMethodInfo.isConstructor();

                // MethodInfo オブジェクトを生成し，引数を追加していく
                TargetMethodInfo methodInfo = new TargetMethodInfo(modifier, methodName,
                        returnType, ownerClass, constructor, loc);
                for (UnresolvedParameterInfo unresolvedParameterInfo : unresolvedMethodInfo
                        .getParameterInfos()) {

                    // パラメータ名を取得
                    String parameterName = unresolvedParameterInfo.getName();

                    // パラメータの型を取得
                    UnresolvedTypeInfo unresolvedParameterType = unresolvedParameterInfo.getType();
                    TypeInfo parameterType = NameResolver.resolveTypeInfo(unresolvedParameterType,
                            classInfoManager);

                    // パラメータオブジェクトを生成し，メソッドに追加
                    TargetParameterInfo parameterInfo = new TargetParameterInfo(parameterName,
                            parameterType);
                    methodInfo.addParameter(parameterInfo);
                }

                // UnresovedLocalVariableInfo の名前解決を行い，MethodInfo に追加していく
                for (UnresolvedLocalVariableInfo unresolvedLocalVariable : unresolvedMethodInfo
                        .getLocalVariables()) {

                    // 変数名を取得
                    String variableName = unresolvedLocalVariable.getName();

                    // 変数の型を取得
                    UnresolvedTypeInfo unresolvedVariableType = unresolvedLocalVariable.getType();
                    TypeInfo variableType = NameResolver.resolveTypeInfo(unresolvedVariableType,
                            classInfoManager);

                    // ローカル変数オブジェクトを生成し，MethodInfoに追加
                    LocalVariableInfo localVariable = new LocalVariableInfo(variableName,
                            variableType);
                    methodInfo.addLocalVariable(localVariable);
                }

                // メソッド情報を追加
                ((TargetClassInfo) ownerClass).addDefinedMethod(methodInfo);
                methodInfoManager.add(methodInfo);
            }
        }

    }

    /**
     * メソッドオーバーライド情報を各MethodInfoに追加する．addInheritanceInfomationToClassInfos の後 かつ registMethodInfos
     * の後に呼び出さなければならない
     */
    private static void addOverrideInformationToMethodInfos() {

        // 全ての対象クラスに対して
        for (TargetClassInfo classInfo : ClassInfoManager.getInstance().getTargetClassInfos()) {

            // 各対象クラスの親クラスに対して
            for (ClassInfo superClassInfo : classInfo.getSuperClasses()) {

                // 各対象クラスの各メソッドについて，親クラスのメソッドをオー払い度しているかを調査
                for (MethodInfo methodInfo : classInfo.getDefinedMethods()) {
                    addOverrideInformationToMethodInfos(superClassInfo, methodInfo);
                }
            }
        }
    }

    /**
     * メソッドオーバーライド情報を追加する．引数で指定されたクラスで定義されているメソッドに対して操作を行う.
     * AddOverrideInformationToMethodInfos()の中からのみ呼び出される．
     * 
     * @param classInfo クラス情報
     * @param overrider オーバーライド対象のメソッド
     */
    private static void addOverrideInformationToMethodInfos(final ClassInfo classInfo,
            final MethodInfo overrider) {

        if ((null == classInfo) || (null == overrider)) {
            throw new NullPointerException();
        }

        if (!(classInfo instanceof TargetClassInfo)) {
            return;
        }

        for (TargetMethodInfo methodInfo : ((TargetClassInfo) classInfo).getDefinedMethods()) {

            if (methodInfo.isSameSignature(overrider)) {
                overrider.addOverridee(methodInfo);
                methodInfo.addOverrider(overrider);

                // 直接のオーバーライド関係しか抽出しないので，このクラスの親クラスは調査しない
                return;
            }
        }

        // 親クラス群に対して再帰的に処理
        for (ClassInfo superClassInfo : classInfo.getSuperClasses()) {
            addOverrideInformationToMethodInfos(superClassInfo, overrider);
        }
    }

    /**
     * フィールドの使用関係を追加する．
     */
    private static void addUseInformationToFieldInfos() {

        UnresolvedClassInfoManager unresolvedClassInfoManager = UnresolvedClassInfoManager
                .getInstance();
        ClassInfoManager classInfoManager = ClassInfoManager.getInstance();
        FieldInfoManager fieldInfoManager = FieldInfoManager.getInstance();

        // 各UnresolvedClassInfo に対して
        for (UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager.getClassInfos()) {

            for (UnresolvedMethodInfo unresolvedMethodInfo : unresolvedClassInfo
                    .getDefinedMethods()) {

                // UnresolvedMethodInfo から MethodInfo を取得
                TargetMethodInfo methodInfo = NameResolver.resolveMethodInfo(unresolvedMethodInfo,
                        classInfoManager);

                // 各UnresolvedMethodInfo の各Unresolvedフィールド参照 に対して
                for (UnresolvedFieldUsage fieldUsage : unresolvedMethodInfo.getFieldReferences()) {

                    // UnresolvedFieldUsage から FieldInfo を取得
                    FieldInfo referencee = NameResolver.resolveFieldUsage(fieldUsage,
                            classInfoManager, fieldInfoManager);
                    methodInfo.addReferencee(referencee);
                    referencee.addReferencer(methodInfo);
                }

                // UnresolvedMethodInfo の各Unresolvedフィールド代入に対して
                for (UnresolvedFieldUsage fieldUsage : unresolvedMethodInfo.getFieldAssignments()) {

                    // UnresolvedFieldUsage から FieldInfo を取得
                    FieldInfo assignmentee = NameResolver.resolveFieldUsage(fieldUsage,
                            classInfoManager, fieldInfoManager);
                    methodInfo.addAssignmentee(assignmentee);
                    assignmentee.addAssignmenter(methodInfo);
                }
            }
        }
    }

    /**
     * メソッドの呼び出し関係情報を追加する．registMethodInfosの後に呼び出さなければならない.
     */
    private static void addCallInformationToMethodInfos() {

        UnresolvedClassInfoManager unresolvedClassInfoManager = UnresolvedClassInfoManager
                .getInstance();
        ClassInfoManager classInfoManager = ClassInfoManager.getInstance();
        MethodInfoManager methodInfoManager = MethodInfoManager.getInstance();

        // 各 UnresolvedClassInfo に対して
        for (UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager.getClassInfos()) {

            // 各 UnresolvedClassInfo の各UnresolvedMethodInfo に対して
            for (UnresolvedMethodInfo unresolvedMethodInfo : unresolvedClassInfo
                    .getDefinedMethods()) {

                // UnresolvedMethodInfo から MethodInfo を取得
                MethodInfo caller = NameResolver.resolveMethodInfo(unresolvedMethodInfo,
                        classInfoManager);

                // 各UnresolvedMethodInfo での各メソッド呼び出し情報(UnresolvedMethodCall)に対して
                for (UnresolvedMethodCall methodCall : unresolvedMethodInfo.getMethodCalls()) {

                    // UnresolvedMethodUsage から MethodInfo を取得
                    MethodInfo callee = NameResolver.resolveMethodCall(methodCall,
                            classInfoManager, methodInfoManager);
                    caller.addCallee(callee);
                    callee.addCaller(caller);
                }
            }
        }
    }
}
