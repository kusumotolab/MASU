package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;


/**
 * ファイル情報を管理するクラス． FileInfo を要素として持つ．
 * 
 * @author y-higo
 * 
 */
public class FileInfoManager implements Iterable<FileInfo> {

    /**
     * ファイル情報を管理しているインスタンスを返す． シングルトンパターンを持ちている．
     * 
     * @return ファイル情報を管理しているインスタンス
     */
    public static FileInfoManager getInstance() {
        if (FILE_INFO_DATA == null) {
            FILE_INFO_DATA = new FileInfoManager();
        }
        return FILE_INFO_DATA;
    }

    /**
     * 
     * @param fileInfo 追加するクラス情報
     */
    public void add(final FileInfo fileInfo) {
        this.fileInfos.add(fileInfo);
    }

    /**
     * ファイル情報の Iterator を返す．この Iterator は unmodifiable であり，変更操作を行うことはできない．
     */
    public Iterator<FileInfo> iterator() {
        Set<FileInfo> unmodifiableFileInfos = Collections.unmodifiableSet(this.fileInfos);
        return unmodifiableFileInfos.iterator();
    }

    /**
     * 
     * コンストラクタ． シングルトンパターンで実装しているために private がついている．
     */
    private FileInfoManager() {
        this.fileInfos = new TreeSet<FileInfo>();
    }

    /**
     * 
     * シングルトンパターンを実装するための変数．
     */
    private static FileInfoManager FILE_INFO_DATA = null;

    /**
     * 
     * ファイル情報 (FileInfo) を格納する変数．
     */
    private final Set<FileInfo> fileInfos;
}
