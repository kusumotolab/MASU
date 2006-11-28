package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ファイル情報を管理するクラス． FileInfo を要素として持つ．
 * 
 * @author y-higo
 * 
 */
public final class FileInfoManager implements Iterable<FileInfo> {

    /**
     * ファイル情報を管理しているインスタンスを返す． シングルトンパターンを持ちている．
     * 
     * @return ファイル情報を管理しているインスタンス
     */
    public static FileInfoManager getInstance() {
        return SINGLETON;
    }

    /**
     * 
     * @param fileInfo 追加するクラス情報
     */
    public void add(final FileInfo fileInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fileInfo) {
            throw new NullPointerException();
        }

        this.fileInfos.add(fileInfo);
    }

    /**
     * ファイル情報の Iterator を返す．この Iterator は unmodifiable であり，変更操作を行うことはできない．
     */
    public Iterator<FileInfo> iterator() {
        SortedSet<FileInfo> unmodifiableFileInfos = Collections
                .unmodifiableSortedSet(this.fileInfos);
        return unmodifiableFileInfos.iterator();
    }

    /**
     * 情報を持っているファイルの個数を返す
     * @return　ファイルの個数
     */
    public int getFileCount() {
        return this.fileInfos.size();
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
    private static final FileInfoManager SINGLETON = new FileInfoManager();

    /**
     * 
     * ファイル情報 (FileInfo) を格納する変数．
     */
    private final SortedSet<FileInfo> fileInfos;
}
