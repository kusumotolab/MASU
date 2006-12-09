package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;


/**
 * 外部クラスに定義されているメソッド情報を保存するためのクラス
 * 
 * @author y-higo
 */
public class ExternalMethodInfo extends MethodInfo {

    /**
     * 外部クラスに定義されているメソッドオブジェクトを初期化する
     * 
     * @param methodName メソッド名
     * @param ownerClass このメソッドを定義しているクラス
     * @param constructor コンストラクタかどうか
     */
    public ExternalMethodInfo(final String methodName, final ClassInfo ownerClass,
            final boolean constructor) {

        super(methodName, UnknownTypeInfo.getInstance(), ownerClass, constructor);
    }

}
