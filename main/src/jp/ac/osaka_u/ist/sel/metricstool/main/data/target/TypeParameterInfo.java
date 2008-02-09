package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 型パラメータを表す抽象クラス
 * 
 * @author higo
 * 
 */
public class TypeParameterInfo implements TypeInfo {

    /**
     * 型パラメータ名を与えてオブジェクトを初期化する
     * 
     * @param ownerUnit 型パラメータを宣言しているユニット(クラス or メソッド)
     * @param name 型パラメータ名
     * @param 継承している型
     */
    public TypeParameterInfo(final UnitInfo ownerUnit, final String name, final TypeInfo extendsType) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == ownerUnit) || (null == name)) {
            throw new NullPointerException();
        }

        if ((!(ownerUnit instanceof TargetClassInfo)) && (!(ownerUnit instanceof CallableUnitInfo))) {
            throw new NullPointerException();
        }

        this.ownerUnit = ownerUnit;
        this.name = name;
        this.extendsType = extendsType;
    }

    /**
     * この型パラメータが引数で与えられた型と等しいかどうかを判定する
     * 
     * @param o 比較対象型情報
     * @return 等しい場合は true，等しくない場合は false;
     */
    public boolean equals(final TypeInfo o) {

        if (null == o) {
            return false;
        }

        if (!(o instanceof TypeParameterInfo)) {
            return false;
        }

        return this.getName().equals(((TypeParameterInfo) o).getName());
    }

    /**
     * 型パラメータを宣言しているユニット(クラス or メソッド)を返す
     * 
     * @param 型パラメータを宣言しているユニット(クラス or メソッド)
     */
    public final UnitInfo getOwnerUnit() {
        return this.ownerUnit;
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
     * 基底クラス型を返す
     * 
     * @return 基底クラス型
     */
    public final TypeInfo getExtendsType() {
        return this.extendsType;
    }

    /**
     * * 基底クラスを持つかどうかを返す
     * 
     * @return 基底クラスを持つ場合は true,持たない場合は false
     */
    public final boolean hasExtendsType() {
        return null != this.extendsType;
    }

    /**
     * 型パラメータを所有しているユニットを保存するための変数
     */
    private final UnitInfo ownerUnit;

    /**
     * 型パラメータ名を保存するための変数
     */
    private final String name;

    /**
     * 未解決基底クラス型を保存するための変数
     */
    private final TypeInfo extendsType;
}
