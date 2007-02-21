package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ASTパースの際，参照型変数の利用可能な名前空間名，または完全限定名を表すクラス
 * 
 * @author y-higo
 * 
 */
public final class AvailableNamespaceInfo {

    /**
     * 利用可能名前空間名とそれ以下のクラス全てのクラスが利用可能かどうかを表すbooleanを与えてオブジェクトを初期化.
     * <p>
     * import aaa.bbb.ccc.DDD； // new AvailableNamespace({"aaa","bbb","ccc","DDD"}, false); <br>
     * import aaa.bbb.ccc.*; // new AvailableNamespace({"aaa","bbb","ccc"},true); <br>
     * </p>
     * 
     * @param namespace 利用可能名前空間名
     * @param allClasses 全てのクラスが利用可能かどうか
     */
    public AvailableNamespaceInfo(final String[] namespace, final boolean allClasses) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == namespace) {
            throw new NullPointerException();
        }

        this.importName = namespace;
        this.allClasses = allClasses;
    }

    /**
     * 対象オブジェクトと等しいかどうかを返す
     * 
     * @param o 対象オブジェクト
     * @return 等しい場合 true，そうでない場合 false
     */
    @Override
    public boolean equals(Object o) {

        if (null == o) {
            throw new NullPointerException();
        }

        if (!(o instanceof AvailableNamespaceInfo)) {
            return false;
        }

        String[] importName = this.getImportName();
        String[] correspondImportName = ((AvailableNamespaceInfo) o).getImportName();
        if (importName.length != correspondImportName.length) {
            return false;
        }

        for (int i = 0; i < importName.length; i++) {
            if (!importName[i].equals(correspondImportName[i])) {
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
    public String[] getImportName() {
        return this.importName;
    }

    /**
     * 名前空間名を返す．
     * 
     * @return 名前空間名
     */
    public String[] getNamespace() {

        final String[] importName = this.getImportName();
        if (this.isAllClasses()) {
            return importName;
        } else {
            final String[] namespace = new String[importName.length - 1];
            System.arraycopy(importName, 0, namespace, 0, importName.length - 1);
            return namespace;
        }
    }

    /**
     * このオブジェクトのハッシュコードを返す
     * 
     * @return このオブジェクトのハッシュコード
     */
    @Override
    public int hashCode() {

        int hash = 0;
        String[] namespace = this.getNamespace();
        for (int i = 0; i < namespace.length; i++) {
            hash += namespace.hashCode();
        }

        return hash;
    }

    /**
     * 全てのクラスが利用可能かどうか
     * 
     * @return 利用可能である場合は true, そうでない場合は false
     */
    public boolean isAllClasses() {
        return this.allClasses;
    }

    /**
     * 名前空間名を表す変数
     */
    private final String[] importName;

    /**
     * 全てのクラスが利用可能かどうかを表す変数
     */
    private final boolean allClasses;
}
