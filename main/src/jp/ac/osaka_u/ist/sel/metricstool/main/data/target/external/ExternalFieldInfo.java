package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;


/**
 * 外部クラスに定義されているフィールドの情報を保存するためのクラス．
 * 
 * @author y-higo
 */
public class ExternalFieldInfo extends FieldInfo {

    /**
     * 名前と定義しているクラス情報を与えて初期化． 型は不明．
     * 
     * @param name フィールド名
     * @param ownerClass フィールドを定義しているクラス
     */
    public ExternalFieldInfo(final String name, final ClassInfo ownerClass) {
        super(name, UnknownTypeInfo.getInstance(), ownerClass);
    }

}
