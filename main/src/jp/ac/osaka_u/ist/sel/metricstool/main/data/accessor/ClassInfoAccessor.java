package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import java.util.Iterator;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;


/**
 * このインターフェースは，クラス情報を取得するためのメソッド郡を提供する．
 * 
 * @author y-higo
 *
 */
public interface ClassInfoAccessor extends Iterable<ClassInfo> {

    /**
     * 対象クラスのイテレータを返すメソッド．
     * 
     * @return 対象クラスのイテレータ
     */
    public Iterator<ClassInfo> iterator();

    /**
     * 対象クラスの数を返すメソッド.
     * @return 対象クラスの数
     */
    public int getClassCount();
}
