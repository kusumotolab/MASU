package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 型パラメータを表す抽象クラス
 * 
 * @author y-higo
 * 
 */
public abstract class TypeParameterInfo implements TypeInfo {

    /**
     * 型パラメータ名を与えてオブジェクトを初期化する
     * 
     * @param name 型パラメータ名
     */
    public TypeParameterInfo(final String name) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == name) {
            throw new NullPointerException();
        }

        this.name = name;
    }

    /**
     * この型パラメータが引数で与えられた型と等しいかどうかを判定する
     * 
     * @param typeInfo 比較対象型情報
     * @return 等しい場合は true，等しくない場合は false;
     */
    public boolean equals(final TypeInfo typeInfo) {

        if (null == typeInfo) {
            return false;
        }

        if (!(typeInfo instanceof TypeParameterInfo)) {
            return false;
        }

        return this.getName().equals(((TypeParameterInfo) typeInfo).getName());
    }

    /**
     * 型パラメータ名を返す
     * 
     * @return 型パラメータ名
     */
    public final String getName() {
        return this.name;
    }

    /**
     * 型名（実際には型パラメータ名）を返す．
     * 
     * @return 型名
     */
    public final String getTypeName() {
        return this.name;
    }

    /**
     * 型パラメータ名を保存するための変数
     */
    private final String name;
}
