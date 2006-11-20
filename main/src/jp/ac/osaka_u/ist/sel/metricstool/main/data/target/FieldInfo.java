package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * フィールドの情報を現すクラス
 * 
 * @author y-higo
 * 
 */
public class FieldInfo implements Comparable<FieldInfo> {

    /**
     * フィールドオブジェクトを初期化する． フィールド名と型が与えられなければならない．
     * 
     * @param name フィールド名
     * @param type フィールドの型
     */
    public FieldInfo(String name, TypeInfo type, ClassInfo ownerClass) {
        this.name = name;
        this.type = type;
        this.ownerClass = ownerClass;
    }

    /**
     * フィールドオブジェクトの順序を定義するメソッド．そのフィールドを定義しているクラスの順序に従う．同じクラス内に定義されている場合は，
     * 
     * @return フィールドの順序関係
     */
    public int compareTo(FieldInfo fieldInfo) {
        ClassInfo classInfo = this.getOwnerClass();
        ClassInfo correspondClassInfo = this.getOwnerClass();
        int classOrder = classInfo.compareTo(correspondClassInfo);
        if (classOrder != 0) {
            return classOrder;
        } else {
            String fieldName = this.getName();
            String correspondFieldName = this.getName();
            return fieldName.compareTo(correspondFieldName);
        }
    }

    /**
     * フィールド名を返す
     * 
     * @return フィールド名
     */
    public String getName() {
        return this.name;
    }

    /**
     * フィールドの型を返す
     * 
     * @return フィールドの型
     */
    public TypeInfo getType() {
        return this.type;
    }

    /**
     * このフィールドを定義しているクラスを返す
     * 
     * @return このフィールドを定義しているクラス
     */
    public ClassInfo getOwnerClass() {
        return this.ownerClass;
    }

    /**
     * フィールド名を表す変数
     */
    private final String name;

    /**
     * フィールドの型を表す変数
     */
    private final TypeInfo type;

    /**
     * このフィールドを定義しているクラスを保存する変数
     */
    private final ClassInfo ownerClass;

    /**
     * フィールドの修飾子を表す変数
     */
    // TODO 修飾子を表す変数を定義する．
}
