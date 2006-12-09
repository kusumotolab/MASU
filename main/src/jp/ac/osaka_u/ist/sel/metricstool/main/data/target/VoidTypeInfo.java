package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * void 型を表すクラス．
 * 
 * @author y-higo
 * 
 */
public final class VoidTypeInfo implements TypeInfo {

    /**
     * このクラスの単一オブジェクトを返す
     * 
     * @return このクラスの単一オブジェクト
     */
    public static VoidTypeInfo getInstance(){
        return SINGLETON;
    }
    
    /**
     * void 型の名前を返す．
     */
    public String getName() {
        return VOID_STRING;
    }

    /**
     * 等しいかどうかのチェックを行う
     */
    public boolean equals(final TypeInfo typeInfo){
        
        if (null == typeInfo){
            throw new NullPointerException();
        }
        
        if (typeInfo instanceof VoidTypeInfo){
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * void 型の型名を表す定数
     */
    public static final String VOID_STRING = new String("void");

    /**
     * このクラスの単一オブジェクトを保存するための定数
     */
    private static final VoidTypeInfo SINGLETON = new VoidTypeInfo();
}
