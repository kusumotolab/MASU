package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public abstract class ClassInfo implements TypeInfo, Comparable<ClassInfo> {

    /**
     * 名前空間名とクラス名からオブジェクトを生成する
     * 
     * @param namespace 名前空間名
     * @param className クラス名
     * 
     */
    public ClassInfo(final NamespaceInfo namespace, final String className) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == namespace) || (null == className)) {
            throw new NullPointerException();
        }

        this.namespace = namespace;
        this.className = className;
    }

    /**
     * クラス名を保存するための変数
     */
    private final String className;

    /**
     * 名前空間名を保存するための変数
     */
    private final NamespaceInfo namespace;

    /**
     * このクラスのクラス名を返す
     * 
     * @return クラス名
     */
    public String getClassName() {
        return this.className;
    }

    /**
     * このクラスの名前空間名を返す
     * 
     * @return 名前空間名
     */
    public NamespaceInfo getNamespace() {
        return this.namespace;
    }

    /**
     * クラスオブジェクトの順序関係を定義するメソッド． 現在は，名前空間名順序を用いている．名前空間名が同じ場合は，クラス名（String）の順序になる．
     */
    public final int compareTo(final ClassInfo classInfo) {
    
        if (null == classInfo) {
            throw new NullPointerException();
        }
    
        NamespaceInfo namespace = this.getNamespace();
        NamespaceInfo correspondNamespace = classInfo.getNamespace();
        int namespaceOrder = namespace.compareTo(correspondNamespace);
        if (namespaceOrder != 0) {
            return namespaceOrder;
        } else {
            String name = this.getClassName();
            String correspondName = classInfo.getClassName();
            return name.compareTo(correspondName);
        }
    }

    /**
     * このクラスの名前を返す． ここの名前とは，名前空間名 + クラス名を表す．
     */
    public final String getFullQualifiedtName() {
        NamespaceInfo namespace = this.getNamespace();
        StringBuffer buffer = new StringBuffer();
        buffer.append(namespace.getName());
        buffer.append(this.getClassName());
        return buffer.toString();
    }
    
    /**
     * このクラスの型名を返す
     */
    public final String getName(){
        return this.getFullQualifiedtName();
    }
}
