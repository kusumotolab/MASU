package jp.ac.osaka_u.ist.sel.metricstool.main;


import java.util.StringTokenizer;

import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.UnavailableLanguageException;


/**
 * 
 * @author higo
 * 
 * 実行時の引数情報を格納するためのクラス
 * 
 */
public class Settings {

    /**
     * 
     * @return ヘルプモードであるか，そうでないか
     * 
     * ヘルプモードであるかどうかを返す． ヘルプモードの時 true，そうでない時，false
     * 
     */
    public static boolean isHelpMode() {
        return helpMode;
    }

    /**
     * 
     * @return 情報表示モードであるか，そうでないか
     * 
     * 情報表示モードであるかどうかを返す． 情報表示モードの時 true，そうでない時，false
     * 
     */
    public static boolean isDisplayMode() {
        return displayMode;
    }

    /**
     * 冗長出力を行うかどうかを返す
     * 
     * @return 行う場合は true, 行わない場合は false
     */
    public static boolean isVerbose() {
        return verbose;
    }

    /**
     * 
     * @return 解析対象ディレクトリ
     * 
     * 解析対象ディレクトリを返す．
     * 
     */
    public static String getTargetDirectory() {
        return targetDirectory;
    }

    /**
     * 解析対象ファイルの記述言語を返す
     * 
     * @return 解析対象ファイルの記述言語
     * @throws UnavailableLanguageException 利用不可能な言語が指定されている場合にスローされる
     */
    public static LANGUAGE getLanguage() throws UnavailableLanguageException {

        if (language.equalsIgnoreCase("java") || language.equalsIgnoreCase("java15")) {
            return LANGUAGE.JAVA15;
        } else if (language.equalsIgnoreCase("java14")) {
            return LANGUAGE.JAVA14;
        } else if (language.equalsIgnoreCase("java13")) {
            return LANGUAGE.JAVA13;
            // }else if (language.equalsIgnoreCase("cpp")) {
            // return LANGUAGE.C_PLUS_PLUS;
            // }else if (language.equalsIgnoreCase("csharp")) {
            // return LANGUAGE.C_SHARP
        }

        throw new UnavailableLanguageException("\"" + language
                + "\" is not an available programming language!");
    }

    /**
     * 
     * @return language 引数で，-l の後ろで指定されたプログラミング言語を表す文字列
     * 
     * -l の後ろで指定されたプログラミング言語を表す文字列を返す． 有効でない文字列であっても，そのまま返す．
     */
    public static String getLanguageString() {
        return language;
    }

    /**
     * 
     * @return 解析対象ファイルのパスを記述しているファイル
     * 
     * 解析対象ファイルのパスを記述しているファイルのパスを返す
     * 
     */
    public static String getListFile() {
        return listFile;
    }

    /**
     * 
     * @return 計測するメトリクス
     * 
     * 計測するメトリクス一覧を返す
     * 
     */
    public static String getMetrics() {
        // TODO メトリクスを表すクラスを作って，その型で返すべき？
        return metrics;
    }

    /**
     * 
     * @return -m で指定されたメトリクス名一覧
     * 
     * -m で指定されたメトリクス名一覧を返す．計測可能でないメトリクス名もそのまま返す．
     */
    public static String[] getMetricStrings() {
        StringTokenizer tokenizer = new StringTokenizer(metrics, ",", false);
        String[] metricStrings = new String[tokenizer.countTokens()];
        for (int i = 0; i < metricStrings.length; i++) {
            metricStrings[i] = tokenizer.nextToken();
        }
        return metricStrings;
    }

    /**
     * 
     * @return ファイルタイプのメトリクスを出力するファイル
     * 
     * ファイルタイプのメトリクスを出力するファイルのパスを返す
     * 
     */
    public static String getFileMetricsFile() {
        return fileMetricsFile;
    }

    /**
     * 
     * @return クラスタイプのメトリクスを出力するファイル
     * 
     * クラスタイプのメトリクスを出力するファイルのパスを返す
     * 
     */
    public static String getClassMetricsFile() {
        return classMetricsFile;
    }

    /**
     * 
     * @return メソッドタイプのメトリクスを出力するファイル
     * 
     * メソッドタイプのメトリクスを出力するファイルのパスを返す
     * 
     */
    public static String getMethodMetricsFile() {
        return methodMetricsFile;
    }

    /**
     * 文字列の引数の初期化に使われる定数
     */
    public static final String INIT = "INIT";

    /**
     * ヘルプモードであるか，そうでないかを記録するための変数
     */
    private static boolean helpMode = false;

    /**
     * 情報表示モードであるか，そうでないかを記録するための変数
     */
    private static boolean displayMode = false;

    /**
     * 冗長出力モードかどうかを記録するための変数
     */
    private static boolean verbose = false;

    /**
     * 解析対象ディレクトリを記録するための変数
     */
    private static String targetDirectory = INIT;

    /**
     * 解析対象ファイルのパスを記述したファイルのパスを記録するための変数
     */
    private static String listFile = INIT;

    /**
     * 解析対象ファイルの記述言語を記録するための変数
     */
    private static String language = INIT;

    /**
     * 計測するメトリクスを記録するための変数
     */
    private static String metrics = INIT;

    /**
     * ファイルタイプのメトリクスを出力するファイルのパスを記録するための変数
     */
    private static String fileMetricsFile = INIT;

    /**
     * クラスタイプのメトリクスを出力するファイルのパスを記録するための変数
     */
    private static String classMetricsFile = INIT;

    /**
     * メソッドタイプのメトリクスを出力するファイルのパスを記録するための変数
     */
    private static String methodMetricsFile = INIT;
}
