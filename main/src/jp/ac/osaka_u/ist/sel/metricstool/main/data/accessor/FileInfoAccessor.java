package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import java.util.Iterator;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;


/**
 * このインターフェースは，ファイル情報を取得するためのメソッド郡を提供する．
 * 
 * @author y-higo
 *
 */
public interface FileInfoAccessor extends Iterable<FileInfo> {

    /**
     * 対象ファイルのイテレータを返すメソッド．
     * 
     * @return 対象ファイルのイテレータ
     */
    public Iterator<FileInfo> iterator();

    /**
     * 対象ファイルの数を返すメソッド.
     * @return 対象ファイルの数
     */
    public int getFileCount();
}
