package jp.ac.osaka_u.ist.sel.metricstool.main;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.LoggingPermission;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFile;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFileManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.Java15Lexer;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.Java15Parser;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
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

    /**
     * 
     * @param args 対象ファイルのファイルパス
     * 
     * 現在仮実装． 対象ファイルのデータを格納した後，構文解析を行う．
     */
    public static void main(String[] args) {
        try {
            //MetricsToolSecurityManagerのシングルトンインスタンスを構築し，初期特別権限スレッドになる
            MetricsToolSecurityManager sm = MetricsToolSecurityManager.getInstance();
            
            //システムのセキュリティーマネージャとして登録してみる
            System.setSecurityManager(sm);
            
            //システムに登録できたので，とりあえずロギングパーミッションをグローバルでセット
            //TODO グローバルに与えるパーミッションは設定ファイルで記述できた方がいいかも
            sm.addGlobalPermission(new LoggingPermission("control", null));
            
        } catch (final SecurityException e) {
            //既にセットされているセキュリティマネージャによって，新たなセキュリティマネージャの登録が許可されなかった．
            //システムのセキュリティマネージャとして使わなくても，特別権限スレッドのアクセス制御は問題なく動作するのでとりあえず無視する
            Logger.global
                    .info(("Failed to set system security manager. MetricsToolsecurityManager works only to manage privilege threads."));
        }

        Settings settings = new Settings();
        ArgumentProcessor.processArgs(args, parameterDefs, settings);
        checkParameterValidation();

        // ヘルプモードの場合
        if (Settings.isHelpMode()) {

            printUsage();

            // 情報表示モードの場合
        } else if (Settings.isDisplayMode()) {

            // -l で言語が指定されていない場合は，解析可能言語一覧を表示
            if (Settings.getLanguageString().equals(Settings.INIT)) {

                System.err.println("Available languages;");
                LANGUAGE[] language = LANGUAGE.values();
                for (int i = 0; i < language.length; i++) {
                    System.err.println("\t" + language[0].getName()
                            + ": can be specified with term \"" + language[0].getIdentifierName()
                            + "\"");
                }

                // -l で言語が指定されている場合は，そのプログラミング言語で使用可能なメトリクス一覧を表示
            } else {

                try {
                    LANGUAGE language = Settings.getLanguage();

                    System.err.println("Available metrics for " + language.getName());
                    DefaultPluginLoader loader = new DefaultPluginLoader();
                    for (AbstractPlugin plugin : loader.loadPlugins()) {
                        PluginInfo pluginInfo = plugin.getPluginInfo();
                        if (pluginInfo.isMeasurable(language)) {
                            System.err.println("\t" + pluginInfo.getMetricsName());
                        }
                    }

                    // TODO 利用可能メトリクス一覧を表示
                } catch (UnavailableLanguageException e) {
                    System.err.println(e.getMessage());
                    System.exit(0);
                } catch (PluginLoadException e) {
                    System.err.println(e.getMessage());
                    System.exit(0);
                }
            }

            // 解析モードの場合
        } else if (!Settings.isHelpMode() && !Settings.isDisplayMode()) {

            // 解析対象ファイルを登録
            {
                // ディレクトリから読み込み
                if (!Settings.getTargetDirectory().equals(Settings.INIT)) {
                    registerFilesFromDirectory();

                    // リストファイルから読み込み
                } else if (!Settings.getListFile().equals(Settings.INIT)) {
                    registerFilesFromListFile();
                }
            }

            // 対象ファイルを解析
            {
                for (TargetFile targetFile : TargetFileManager.getInstance()) {
                    try {
                        String name = targetFile.getName();
                        System.out.println("processing " + name);
                        Java15Lexer lexer = new Java15Lexer(new FileInputStream(name));
                        Java15Parser parser = new Java15Parser(lexer);
                        parser.compilationUnit();
                        targetFile.setCorrectSytax(true);

                    } catch (FileNotFoundException e) {
                        System.err.println(e.getMessage());
                    } catch (RecognitionException e) {
                        targetFile.setCorrectSytax(false);
                        System.err.println(e.getMessage());
                        // TODO エラーが起こったことを TargetFileData などに通知する処理が必要
                    } catch (TokenStreamException e) {
                        targetFile.setCorrectSytax(false);
                        System.err.println(e.getMessage());
                        // TODO エラーが起こったことを TargetFileData などに通知する処理が必要
                    }
                }
            }

            // 文法誤りのあるファイル一覧を表示
            {
                System.err.println("The following files includes uncorrect syntax.");
                System.err.println("Any metrics of them were not measured");
                for (TargetFile targetFile : TargetFileManager.getInstance()) {
                    if (!targetFile.isCorrectSyntax()) {
                        System.err.println("\t" + targetFile.getName());
                    }
                }
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
     * 
     * 引数の整合性を確認するためのメソッド． 不正な引数が指定されていた場合，main メソッドには戻らず，この関数内でプログラムを終了する．
     * 
     */
    private static void checkParameterValidation() {

        // ヘルプモードと情報表示モードが同時にオンになっている場合は不正
        if (Settings.isHelpMode() && Settings.isDisplayMode()) {
            System.err.println("-h and -x can\'t be set at the same time!");
            printUsage();
            System.exit(0);
        }

        // ヘルプモードの場合
        if (Settings.isHelpMode()) {
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

        // 情報表示モードの場合
        if (Settings.isDisplayMode()) {
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

        // 解析モードの場合
        if (!Settings.isHelpMode() && !Settings.isDisplayMode()) {

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

            try {
                boolean measureFileMetrics = false;
                boolean measureClassMetrics = false;
                boolean measureMethodMetrics = false;

                DefaultPluginLoader loader = new DefaultPluginLoader();
                for (AbstractPlugin plugin : loader.loadPlugins()) {
                    PluginInfo pluginInfo = plugin.getPluginInfo();
                    switch (pluginInfo.getMetricsType()) {
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

                //ファイルメトリクスを計測する場合は -F オプションが指定されていなければならない
                if (measureFileMetrics && (Settings.getFileMetricsFile().equals(Settings.INIT))) {
                    System.err.println("-F must be used for specifying a file for file metrics!");
                    System.exit(0);
                }

                //クラスメトリクスを計測する場合は -C オプションが指定されていなければならない
                if (measureClassMetrics && (Settings.getClassMetricsFile().equals(Settings.INIT))) {
                    System.err.println("-C must be used for specifying a file for class metrics!");
                    System.exit(0);
                }
                //メソッドメトリクスを計測する場合は -M オプションが指定されていなければならない                
                if (measureMethodMetrics && (Settings.getMethodMetricsFile().equals(Settings.INIT))) {
                    System.err.println("-M must be used for specifying a file for method metrics!");
                    System.exit(0);
                }

            } catch (PluginLoadException e) {
                System.err.println(e.getMessage());
                System.exit(0);
            }
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
            System.err.println("\"" + Settings.getListFile() + "\" is not a valid file!");
            System.exit(0);
        } catch (IOException e) {
            System.err.println("\"" + Settings.getListFile() + "\" can\'t read!");
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
                System.err.println(e.getMessage());
                System.exit(0);
            }

            // ディレクトリでもファイルでもない場合は不正
        } else {
            System.err.println("\"" + file.getAbsolutePath() + "\" is not a vaild file!");
            System.exit(0);
        }
    }
}
