package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決フィールド使用を保存するためのクラス
 * 
 * @author y-higo
 * 
 */
public final class UnresolvedFieldUsage implements UnresolvedTypeInfo {

    /**
     * フィールド使用が実行される変数の型名と変数名，利用可能な名前空間を与えてオブジェクトを初期化
     * 
     * @param availableNamespaces 利用可能な名前空間
     * @param ownerClassType フィールド使用が実行される変数の型名
     * @param fieldName 変数名
     */
    public UnresolvedFieldUsage(final AvailableNamespaceInfoSet availableNamespaces,
            final UnresolvedTypeInfo ownerClassType, final String fieldName) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == availableNamespaces) || (null == ownerClassType) || (null == fieldName)) {
            throw new NullPointerException();
        }

        this.availableNamespaces = availableNamespaces;
        this.ownerClassType = ownerClassType;
        this.fieldName = fieldName;
    }

    /**
     * 使用可能な名前空間を返す
     * 
     * @return 使用可能な名前空間を返す
     */
    public AvailableNamespaceInfoSet getAvailableNamespaces() {
        return this.availableNamespaces;
    }

    /**
     * フィールド使用が実行される変数の未解決型名を返す
     * 
     * @return フィールド使用が実行される変数の未解決型名
     */
    public UnresolvedTypeInfo getOwnerClassType() {
        return this.ownerClassType;
    }

    /**
     * フィールド名を返す
     * 
     * @return フィールド名
     */
    public String getFieldName() {
        return this.fieldName;
    }

    /**
     * このフィールド使用の型（返り値みたいなもの）を返す
     * 
     * @return このフィールド使用の型（返り値みたいなもの）
     */
    public String getTypeName() {
        return UnresolvedTypeInfo.UNRESOLVED;
    }

    /**
     * 使用可能な名前空間を保存するための変数
     */
    private final AvailableNamespaceInfoSet availableNamespaces;

    /**
     * フィールド使用が実行される変数の未解決型名を保存するための変数
     */
    private final UnresolvedTypeInfo ownerClassType;

    /**
     * フィールド名を保存するための変数
     */
    private final String fieldName;
}
