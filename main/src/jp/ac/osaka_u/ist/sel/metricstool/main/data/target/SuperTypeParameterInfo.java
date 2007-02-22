package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public final class SuperTypeParameterInfo extends TypeParameterInfo {

    /**
     * 型パラメータ名，派生クラス型を与えてオブジェクトを初期化
     * 
     * @param name 型パラメータ名
     * @param superType 派生クラス型
     */
    public SuperTypeParameterInfo(final String name, final TypeInfo superType) {

        super(name);

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == superType) {
            throw new NullPointerException();
        }

        this.superType = superType;
    }

    /**
     * 派生クラス型を返す
     * 
     * @return 派生クラス型
     */
    public TypeInfo getSuperType() {
        return this.superType;
    }

    /**
     * 未解決派生クラス型を保存する
     */
    private final TypeInfo superType;
}
