package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.io.Serializable;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.StringArrayComparator;


/**
 * 名前空間名を表すクラス
 * 
 * @author higo
 */
public final class NamespaceInfo implements Comparable<NamespaceInfo>, Serializable {

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
     * @param namespace 比較対象名前空間名
     * @return 名前空間の順序
     */
    public int compareTo(final NamespaceInfo namespace) {

        if (null == namespace) {
            throw new NullPointerException();
        }

        return StringArrayComparator.SINGLETON.compare(this.getName(), namespace.getName());
    }

    /**
     * 名前空間の比較を行う．等しい場合は true，そうでない場合 false を返す
     */
    @Override
    public boolean equals(final Object o) {

        if (null == o) {
            throw new NullPointerException();
        }

        if (!(o instanceof NamespaceInfo)) {
            return false;
        }

        // 名前空間の長さで比較
        final String[] name = this.getName();
        final String[] correspondName = ((NamespaceInfo) o).getName();
        if (name.length != correspondName.length) {
            return false;
        }

        // 各要素を個別に比較
        for (int i = 0; i < name.length; i++) {
            if (!name[i].equals(correspondName[i])) {
                return false;
            }
        }
        return true;
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
    public final static NamespaceInfo UNKNOWN = new NamespaceInfo(new String[] { "unknown" });

    /**
     * 名前空間名を返す
     * 
     * @param delimiter 名前の区切り文字
     * @return 名前空間をつないだ String
     */
    public String getName(final String delimiter) {

        if (null == delimiter) {
            throw new NullPointerException();
        }

        String[] names = this.getName();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < names.length; i++) {
            buffer.append(names[i]);
            buffer.append(delimiter);
        }

        return buffer.toString();

    }

    /**
     * 名前空間を表す変数
     */
    private final String[] name;

}
