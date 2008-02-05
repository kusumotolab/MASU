package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.HashSet;


/**
 * 未解決完全限定名クラス参照を表すクラス
 * 
 * @author higo, t-miyake
 *
 */
public final class UnresolvedFullQualifiedNameClassReferenceInfo extends
        UnresolvedClassReferenceInfo {

    /**
     * 完全限定名がわかっている（UnresolvedClassInfoのオブジェクトが存在する）クラスの参照を初期化
     * 
     * @param referencedClass 参照されているクラス
     */
    public UnresolvedFullQualifiedNameClassReferenceInfo(final UnresolvedClassInfo referencedClass) {
        super(new HashSet<AvailableNamespaceInfo>(), referencedClass.getFullQualifiedName());
        this.referencedClass = referencedClass;
    }

    /**
     * 参照されているクラスの情報を返す
     * 
     * @return 参照されているクラスの情報
     */
    public UnresolvedClassInfo getReferencedClass() {
        return this.referencedClass;
    }

    /**
     * 参照されているクラスを保存するための変数
     */
    private final UnresolvedClassInfo referencedClass;

}
