package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決フィールド使用を保存するためのクラス
 * 
 * @author y-higo
 * 
 */
public final class UnresolvedFieldUsage implements Comparable<UnresolvedFieldUsage> {

    /**
     * フィールド使用が実行される変数の型名と変数名を与えてオブジェクトを初期化
     * 
     * @param ownerClassName フィールド使用が実行される変数の型名
     * @param fieldName 変数名
     */
    public UnresolvedFieldUsage(final String[] ownerClassName, final String fieldName) {
        
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == ownerClassName) || (null == fieldName)){
            throw new NullPointerException();
        }
        
        this.ownerClassName = ownerClassName;
        this.fieldName = fieldName;
    }

    /**
     * フィールド使用が実行される変数の型名を返す
     * 
     * @return フィールド使用が実行される変数の型名
     */
    public String[] getOwnerClassName() {
        return this.ownerClassName;
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
     * フィールド使用の順序を定義
     * 
     * @return フィールド使用の順序
     */
    public int compareTo(final UnresolvedFieldUsage unresolvedFieldUsage) {

        if (null == unresolvedFieldUsage) {
            throw new NullPointerException();
        }

        // フィールド名で比較
        int fieldNameOrder = this.getFieldName().compareTo(unresolvedFieldUsage.getFieldName());
        if (0 != fieldNameOrder) {
            return fieldNameOrder;
        }

        // メソッド呼び出しが行われている変数の型で比較
        final String[] ownerClassName = this.getOwnerClassName();
        final String[] correspondOwnerClassName = unresolvedFieldUsage.getOwnerClassName();
        if (ownerClassName.length > correspondOwnerClassName.length) {
            return 1;
        } else if (ownerClassName.length < correspondOwnerClassName.length) {
            return -1;
        } else {
            for (int i = 0; i < ownerClassName.length; i++) {
                final int stringOrder = ownerClassName[i].compareTo(correspondOwnerClassName[i]);
                if (0 != stringOrder) {
                    return stringOrder;
                }
            }

            return 0;
        }
    }

    /**
     * フィールド使用が実行される変数の型名を保存するための変数
     */
    private final String[] ownerClassName;

    /**
     * フィールド名を保存するための変数
     */
    private final String fieldName;
}
