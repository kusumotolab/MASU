package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import java.util.Iterator;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;


/**
 * このインターフェースは，メソッド情報を取得するためのメソッド郡を提供する．
 * 
 * @author y-higo
 *
 */
public interface MethodInfoAccessor extends Iterable<MethodInfo> {

    /**
     * 対象メソッドのイテレータを返すメソッド．
     * 
     * @return 対象メソッドのイテレータ
     */
    public Iterator<MethodInfo> iterator();

    /**
     * 対象メソッドのの数を返すメソッド.
     * @return 対象メソッドの数
     */
    public int getMethodCount();
}
