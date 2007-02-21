package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * <T extends A> などの extends を用いた型パラメータを表すクラス
 * 
 * @author y-higo
 * 
 */
public final class ExtendsTypeParameterInfo extends TypeParameterInfo {

    /**
     * 型パラメータ名，基底クラスを与えて初期化
     * @param name
     * @param extendsType
     */
    ExtendsTypeParameterInfo(final String name, final ClassInfo extendsType) {
        super(name);

        if (null == extendsType) {
            throw new NullPointerException();
        }

        this.extendsType = extendsType;

    }

    /**
     * 未解決基底クラス型を返す
     * 
     * @return 未解決基底クラス型
     */
    public ClassInfo getExtendsType() {
        return this.extendsType;
    }

    /**
     * 基底クラス型を保存する
     */
    private final ClassInfo extendsType;

}
