package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import java.util.Iterator;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;


/**
 * このインターフェースは，クラス情報を取得するためのメソッド郡を提供する．
 * 
 * @author y-higo
 *
 */
public interface ClassInfoAccessor {

    /**
     * 対象クラスのイテレータを返すメソッド．
     * 
     * @return 対象クラスのイテレータ
     */
    public Iterator<ClassInfo> classInfoIterator();
}
