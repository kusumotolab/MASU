package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * 配列型を表すためのクラス．
 * 
 * @author y-higo
 * 
 */
public class ArrayTypeInfo implements TypeInfo {

    /**
     * 型名を返す
     */
    public String getName() {
        TypeInfo elementType = this.getElementType();
        int dimension = this.getDimension();

        StringBuffer buffer = new StringBuffer();
        buffer.append(elementType.getName());
        for (int i = 0; i < dimension; i++) {
            buffer.append("[]");
        }
        return buffer.toString();
    }

    /**
     * 配列の要素の型を返す
     * 
     * @return 配列の要素の型
     */
    public TypeInfo getElementType() {
        return this.type;
    }

    /**
     * 配列の次元を返す
     * 
     * @return 配列の次元
     */
    public int getDimension() {
        return this.dimension;
    }

    /**
     * オブジェクトの初期化を行う．配列の要素の型と配列の次元が与えられなければならない
     * 
     * @param type 配列の要素の型
     * @param dimension 配列の事件
     */
    public ArrayTypeInfo(TypeInfo type, int dimension) {
        this.type = type;
        this.dimension = dimension;
    }

    /**
     * 配列の要素の型を保存する変数
     */
    private final TypeInfo type;

    /**
     * 配列の次元を保存する変数
     */
    private final int dimension;

}
