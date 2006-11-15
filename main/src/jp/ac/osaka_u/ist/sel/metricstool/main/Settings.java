package jp.ac.osaka_u.ist.sel.metricstool.main;


/**
 * 
 * @author y-higo
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
     * 
     * @return 解析対象ファイルの記述言語
     * 
     * 解析対象ファイルの記述言語を返す
     * 
     */
    public static String getLanguage() {
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
        return metrics;
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
     * ヘルプモードであるか，そうでないかを記録するための変数
     */
    private static boolean helpMode = false;

    /**
     * 情報表示モードであるか，そうでないかを記録するための変数
     */
    private static boolean displayMode = false;

    /**
     * 解析対象ディレクトリを記録するための変数
     */
    private static String targetDirectory = null;

    /**
     * 解析対象ファイルのパスを記述したファイルのパスを記録するための変数
     */
    private static String listFile = null;

    /**
     * 解析対象ファイルの記述言語を記録するための変数
     */
    private static String language = null;

    /**
     * 計測するメトリクスを記録するための変数
     */
    private static String metrics = null;

    /**
     * ファイルタイプのメトリクスを出力するファイルのパスを記録するための変数
     */
    private static String fileMetricsFile = null;

    /**
     * クラスタイプのメトリクスを出力するファイルのパスを記録するための変数
     */
    private static String classMetricsFile = null;

    /**
     * メソッドタイプのメトリクスを出力するファイルのパスを記録するための変数
     */
    private static String methodMetricsFile = null;

}
