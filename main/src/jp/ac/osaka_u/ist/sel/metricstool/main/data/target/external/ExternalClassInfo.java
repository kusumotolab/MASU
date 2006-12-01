package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.NamespaceInfo;


/**
 * 外部クラス情報を表すクラス
 * 
 * @author y-higo
 *
 */
public class ExternalClassInfo extends ClassInfo {

    /**
     * 名前空間名とクラス名を与えて，オブジェクトを初期化
     * @param namespace 名前空間名
     * @param className クラス名
     */
    public ExternalClassInfo(final NamespaceInfo namespace, final String className) {

        super(namespace, className);
    }
}
