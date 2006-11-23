package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * フィールドの情報を現すクラス
 * 
 * @author y-higo
 * 
 */
public final class FieldInfo extends VariableInfo {

    /**
     * フィールドオブジェクトを初期化する． フィールド名と型が与えられなければならない．
     * 
     * @param name フィールド名
     * @param type フィールドの型
     */
    public FieldInfo(final String name, final TypeInfo type, final ClassInfo ownerClass) {
        
        super(name, type);
        
        if (null == ownerClass) {
            throw new NullPointerException();
        }
        
        this.ownerClass = ownerClass;
    }

    /**
     * フィールドオブジェクトの順序を定義するメソッド．そのフィールドを定義しているクラスの順序に従う．同じクラス内に定義されている場合は，
     * 
     * @return フィールドの順序関係
     */
    public int compareTo(final FieldInfo fieldInfo) {
        
        if (null == fieldInfo) {
            throw new NullPointerException();
        }
        
        ClassInfo classInfo = this.getOwnerClass();
        ClassInfo correspondClassInfo = this.getOwnerClass();
        int classOrder = classInfo.compareTo(correspondClassInfo);
        if (classOrder != 0) {
            return classOrder;
        } else {
            return super.compareTo(fieldInfo);
        }
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
     * このフィールドを定義しているクラスを保存する変数
     */
    private final ClassInfo ownerClass;

    /**
     * フィールドの修飾子を表す変数
     */
    // TODO 修飾子を表す変数を定義する．
}
