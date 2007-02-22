package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

public final class UnresolvedFullQualifiedNameReferenceTypeInfo extends UnresolvedReferenceTypeInfo {

    /**
     * 利用可能な名前空間名，参照名を与えて初期化
     * 
     * @param availableNamespaces 名前空間名
     * @param referenceName 参照名
     */
    public UnresolvedFullQualifiedNameReferenceTypeInfo(final AvailableNamespaceInfoSet availableNamespaces,
            final String[] referenceName) {

        super(availableNamespaces, referenceName);
    }
}
