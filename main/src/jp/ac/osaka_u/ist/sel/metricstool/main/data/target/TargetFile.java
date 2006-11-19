package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * 
 * @author y-higo
 * 
 * 対象ファイルのデータを格納するクラス
 * 
 * since 2006.11.12
 */
public class TargetFile implements Comparable<TargetFile> {

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
     * このオブジェクトと対象オブジェクトの順序関係を返す．
     * @param targetFile 比較対象オブジェクト
     * @return 順序関係
     */
    public int compareTo(TargetFile targetFile) {
        String name = this.getName();
        String correspondName = targetFile.getName();
        return name.compareTo(correspondName);
    }

    /**
     * 
     * @param o 比較対象ファイル
     * @return この対象ファイルと比較対象ファイルのファイルパスが等しい場合は true，そうでなければ false
     * 
     * この対象ファイルのファイルパスと，引数で与えられた対象ファイルのパスが等しいかをチェックする．等しい場合は true を返し，そうでない場合は false を返す．
     * 
     */
    public boolean equals(Object o) {
        String thisName = this.getName();
        String correspondName = ((TargetFile) o).getName();
        return thisName.equals(correspondName);
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
     * @return 対象ファイルのハッシュコード
     * 
     * 対象ファイルのハッシュコードを返す
     * 
     */
    public int hashCode() {
        String name = this.getName();
        return name.hashCode();
    }

    /**
     * 
     * 対象ファイルのパスを保存するための変数
     */
    private final String name;
}
