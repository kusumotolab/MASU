package jp.ac.osaka_u.ist.sel.metricstool.main.data.file;


/**
 * 
 * @author y-higo
 * 
 * 対象ファイルのデータを格納するクラス
 * 
 * since 2006.11.12
 */
public class TargetFile {

    /**
     * 
     * @param name 対象ファイルのパス
     * 
     * 対象ファイルのパスを用いて初期化
     */
    public TargetFile(final String name) {
        this.name = name;
    }

    /**
     * 
     * @return 対象ファイルのパス
     * 
     * 対象ファイルのパスを返す
     */
    public final String getName() {
        return this.name;
    }

    /**
     * 
     * 対象ファイルのパスを保存するための変数
     */
    private final String name;
}
