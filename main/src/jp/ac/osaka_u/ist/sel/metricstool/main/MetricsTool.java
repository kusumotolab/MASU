package jp.ac.osaka_u.ist.sel.metricstool.main;


import java.io.File;

import org.jargp.ArgumentProcessor;
import org.jargp.BoolDef;
import org.jargp.ParameterDef;
import org.jargp.StringDef;


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

        Settings settings = new Settings();
        ArgumentProcessor.processArgs(args, parameterDefs, settings);
        checkParameterValidation();

        /*
         * TargetFileData targetFiles = TargetFileData.getInstance(); for (int i = 0; i <
         * args.length; i++) { targetFiles.add(new TargetFile(args[i])); }
         * 
         * try { for (TargetFile targetFile : targetFiles) { String name = targetFile.getName();
         * System.out.println("processing " + name); Java15Lexer lexer = new Java15Lexer(new
         * FileInputStream(name)); Java15Parser parser = new Java15Parser(lexer);
         * parser.compilationUnit(); } } catch (Exception e) { e.printStackTrace(); }
         */

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

        
        
        // -d で指定されたターゲットディレクトリのチェック
        if (!Settings.getTargetDirectory().equals(Settings.INIT)) {
            String directoryPath = Settings.getTargetDirectory();
            File directory = new File(directoryPath);
            if (!directory.isDirectory()) {
                System.err.println("\"directoryPath\" is not a valid directory!");
                System.exit(0);
            }
        }

        // -i で指定されたリストファイルのチェック
        if (!Settings.getListFile().equals(Settings.INIT)) {
            String filePath = Settings.getTargetDirectory();
            File file = new File(filePath);
            if (!file.isFile()) {
                System.err.println("\"filePath\" is not a valid file!");
                System.exit(0);
            }
        }

        // -l で指定された言語が解析可能言語であるかをチェック
        if (!Settings.getLanguage().equals(Settings.INIT)) {
            // TODO
        }

        // -m で指定されたメトリクスが算出可能なメトリクスであるかをチェック
        if (!Settings.getMetrics().equals(Settings.INIT)) {
            // TODO
        }

        // -C で指定されたリストファイルのチェック
        if (!Settings.getClassMetricsFile().equals(Settings.INIT)) {
            String filePath = Settings.getTargetDirectory();
            File file = new File(filePath);
            if (!file.isFile()) {
                System.err.println("\"filePath\" is not a valid file!");
                System.exit(0);
            }
        }

        // -F で指定されたリストファイルのチェック
        if (!Settings.getFileMetricsFile().equals(Settings.INIT)) {
            String filePath = Settings.getTargetDirectory();
            File file = new File(filePath);
            if (!file.isFile()) {
                System.err.println("\"filePath\" is not a valid file!");
                System.exit(0);
            }
        }

        // -M で指定されたリストファイルのチェック
        if (!Settings.getMethodMetricsFile().equals(Settings.INIT)) {
            String filePath = Settings.getTargetDirectory();
            File file = new File(filePath);
            if (!file.isFile()) {
                System.err.println("\"filePath\" is not a valid file!");
                System.exit(0);
            }
        }

        // ヘルプモードの場合
        if (Settings.isHelpMode()) {
            // -h は他のオプションと同時指定できない
            if ((!Settings.getTargetDirectory().equals(Settings.INIT))
                    || (!Settings.getListFile().equals(Settings.INIT))
                    || (!Settings.getLanguage().equals(Settings.INIT))
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
            if (Settings.getFileMetricsFile().equals(Settings.INIT)) {
                System.err.println("-C can't be specified in the display mode!");
                printUsage();
                System.exit(0);
            }

            // -M は使えない
            if (!Settings.getFileMetricsFile().equals(Settings.INIT)) {
                System.err.println("-M can't be specified in the display mode!");
                printUsage();
                System.exit(0);
            }

            // -m が指定されている場合，-l で解析可能言語が指定されていなければならない
            if (!Settings.getMetrics().equals(Settings.INIT)) {
                System.err
                        .println("available language must be specified by -l when -m is specified in the display mode!");
                printUsage();
                System.exit(0);
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
                
                // ファイルメトリクスを算出する場合は，-F が指定されていない時は不正
                // TODO
                
                // クラスメトリクスを算出する場合は，-C が指定されていない時は不正
                // TODO
                
                // メソッドメトリクスを算出する場合は，-M が指定されていない時は不正
                // TODO
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
}
