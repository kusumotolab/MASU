package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * void 型を表すクラス．
 * 
 * @author y-higo
 * 
 */
public class VoidTypeInfo implements TypeInfo {

    /**
     * void 型の名前を返す．
     */
    public String getName() {
        return VOID_STRING;
    }

    /**
     * void 型の型名を表す定数
     */
    public static final String VOID_STRING = new String("void");

}
