package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 名前空間名を表すクラス
 * 
 * @author y-higo
 */
public final class NamespaceInfo implements Comparable<NamespaceInfo> {

    /**
     * 名前空間オブジェクトを初期化する．名前空間名が与えられなければならない．
     * 
     * @param name
     */
    public NamespaceInfo(final String[] name) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == name) {
            throw new NullPointerException();
        }

        this.name = name;
    }

    /**
     * 名前空間名の順序を定義するメソッド．現在は名前空間を表す String クラスの compareTo を用いている．
     * 
     * @param namaspace 比較対象名前空間名
     * @return 名前空間の順序
     */
    public int compareTo(final NamespaceInfo namespace) {

        if (null == namespace) {
            throw new NullPointerException();
        }

        String name = this.getName(".");
        String correspondName = namespace.getName(".");
        return name.compareTo(correspondName);
    }

    /**
     * 名前空間名を返す
     * 
     * @return 名前空間名
     */
    public String[] getName() {
        return this.name;
    }

    /**
     * 不明な名前空間名を表す定数
     */
    public final static NamespaceInfo UNNKNOWN = new NamespaceInfo(new String[] { "unknown" });

    /**
     * 名前空間名を返す
     * 
     * @param separator 名前間のセパレータ
     * @return 名前空間をつないだ String
     */
    public String getName(final String separator) {

        if (null == separator){
            throw new  NullPointerException();
        }
        
        String[] names = this.getName();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < names.length; i++) {
            buffer.append(names[i]);
            buffer.append(separator);
        }

        return buffer.toString();

    }

    /**
     * 名前空間を表す変数
     */
    private final String[] name;

}
