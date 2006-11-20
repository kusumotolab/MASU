package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * 名前空間名を表すクラス
 * 
 * @author y-higo
 */
public class NamespaceInfo implements Comparable<NamespaceInfo> {

    /**
     * 名前空間オブジェクトを初期化する．名前空間名が与えられなければならない．
     * 
     * @param name
     */
    public NamespaceInfo(String name) {
        this.name = name;
    }

    /**
     * 名前空間名の順序を定義するメソッド．現在は名前空間を表す String クラスの compareTo を用いている．
     */
    public int compareTo(NamespaceInfo namespaceInfo) {
        String name = this.getName();
        String correspondName = namespaceInfo.getName();
        return name.compareTo(correspondName);
    }

    /**
     * 名前空間名を返す
     * 
     * @return 名前空間名
     */
    public String getName() {
        return this.name;
    }

    /**
     * 名前空間を表す変数
     */
    private final String name;

}
