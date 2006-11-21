package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * ローカル変数を表すクラス．型を提供するのみ．
 * 
 * @author y-higo
 * 
 */
public final class LocalVariableInfo extends VariableInfo {

    /**
     * ローカル変数オブジェクトを初期化する．変数名と変数の型が必要．
     * 
     * @param name ローカル変数名
     * @param type ローカル変数の型
     */
    public LocalVariableInfo(String name, TypeInfo type) {
        super(name, type);
    }
}
