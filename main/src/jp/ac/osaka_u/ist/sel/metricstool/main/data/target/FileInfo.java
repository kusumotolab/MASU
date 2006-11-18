package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * 
 * @author y-higo
 * 
 * ファイルの情報を表すクラス．
 */
final public class FileInfo {

    /**
     * 指定されたファイル名のオブジェクトを初期化する．
     * 
     * @param name ファイル名
     */
    public FileInfo(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     */
    public int getLOC() {
        return loc;
    }

    /**
     * ファイル名を返す． 現在フルパスで返すが，ディレクトリとファイル名を分けた方が良いかも．
     * 
     * @return ファイル名
     */
    public String getName() {
        return name;
    }

    /**
     * ファイルのハッシュコードを返す．ハッシュコードはファイル名（フルパス）を用いて計算される
     * 
     * @return このファイルのハッシュコード
     */
    @Override
    public int hashCode() {
        String name = this.getName();
        return name.hashCode();
    }

    /**
     * 変数 loc の setter．行数情報をセットする．
     * 
     * @param loc 行数
     */
    public void setLOC(int loc) {
        this.loc = loc;
    }

    /**
     * ファイルの行数を表す変数．
     */
    private int loc;

    /**
     * ファイル名を表す変数. ハッシュコードの計算に使っている．
     */
    private final String name;

    // TODO 宣言されているクラスの情報を追加
    // TODO importしているクラスの情報を追加
    // TODO includeしているファイルの情報を追加
}
