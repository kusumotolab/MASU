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
     * 
     * @param namespace 名前空間名
     * @param className クラス名
     */
    public ExternalClassInfo(final NamespaceInfo namespace, final String className) {

        super(namespace, className);
    }

    /**
     * 完全限定名を与えて，クラス情報オブジェクトを初期化
     * 
     * @param fullQualifiedName 完全限定名
     */
    public ExternalClassInfo(final String[] fullQualifiedName) {

        super(fullQualifiedName);
    }

    /**
     * 名前空間が不明な外部クラスのオブジェクトを初期化
     * 
     * @param className クラス名
     */
    public ExternalClassInfo(final String className) {

        super(NamespaceInfo.UNNKNOWN, className);
    }
}
