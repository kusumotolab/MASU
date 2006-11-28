package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import java.util.Iterator;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfoManager;


/**
 * プラグインが FileInfo にアクセスするために用いるインターフェース
 * 
 * @author y-higo
 *
 */
public class DefaultFileInfoAccessor implements FileInfoAccessor {

    /**
     * FileInfo のイテレータを返す． このイテレータは参照専用であり変更処理を行うことはできない．
     * 
     * @return FileInfo のイテレータ
     */
    public Iterator<FileInfo> iterator() {
        FileInfoManager fileInfoManager = FileInfoManager.getInstance();
        return fileInfoManager.iterator();
    }

    /**
     * 対象ファイルの数を返すメソッド.
     * @return 対象ファイルの数
     */
    public int getFileCount() {
        return FileInfoManager.getInstance().getFileCount();
    }

}
