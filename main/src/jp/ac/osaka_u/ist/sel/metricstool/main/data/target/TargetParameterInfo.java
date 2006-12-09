package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

/**
 * 対象メソッドの引数を表すクラス
 * 
 * @author y-higo
 *
 */
public final class TargetParameterInfo extends ParameterInfo {

    /**
     * 引数名，引数の型を与えてオブジェクトを初期化
     * 
     * @param name 引数名
     * @param type 引数の型
     */
    public TargetParameterInfo(final String name, final TypeInfo type){
        super(name, type);
    }
}
