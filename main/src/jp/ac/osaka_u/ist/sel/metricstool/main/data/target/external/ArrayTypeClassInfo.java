package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external;


import java.util.HashSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.NamespaceInfo;


/**
 * 配列型の定義を表すためのクラス．
 * できれば使いたくはない．
 * 
 * @author higo
 *
 */
public final class ArrayTypeClassInfo extends ClassInfo {

    /**
     * 配列の型を与えて，オブジェクトを初期化
     * 
     * @param arrayType 配列の型
     */
    public ArrayTypeClassInfo(final ArrayTypeInfo arrayType) {

        super(new HashSet<ModifierInfo>(), NamespaceInfo.UNKNOWN, NONAME, 0, 0, 0, 0);

        if (null == arrayType) {
            throw new IllegalArgumentException();
        }
        this.arrayType = arrayType;
    }

    /**
     * 配列の型を返す
     * 
     * @return 配列の型
     */
    public ArrayTypeInfo getArrayType() {
        return this.arrayType;
    }

    private final ArrayTypeInfo arrayType;

    public static final String NONAME = new String("noname");
}
