package jp.ac.osaka_u.ist.sel.metricstool.main;


import java.io.FileInputStream;

import org.jargp.ArgumentProcessor;
import org.jargp.BoolDef;
import org.jargp.ParameterDef;
import org.jargp.StringDef;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFile;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFileData;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.Java15Lexer;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.Java15Parser;


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
        printParameters();

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

    private static void printParameters() {
        System.out.printf("helpMode", Settings.isHelpMode());
        System.out.printf("displayMode", Settings.isDisplayMode());
        System.out.printf("targetDirectory", Settings.getTargetDirectory());
        System.out.printf("listFile", Settings.getListFile());
        System.out.printf("language", Settings.getLanguage());
        System.out.printf("metrics", Settings.getMetrics());
        System.out.printf("fileMetricsFile", Settings.getFileMetricsFile());
        System.out.printf("classMetricsFile", Settings.getClassMetricsFile());
        System.out.printf("methodMetricsFile", Settings.getMethodMetricsFile());

    }
}
