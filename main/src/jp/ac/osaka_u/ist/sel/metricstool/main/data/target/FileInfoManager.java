package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ファイル情報を管理するクラス． FileInfo を要素として持つ．
 * 
 * @author higo
 * 
 */
public final class FileInfoManager {

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
     * 現在解析中のファイル情報を返す
     * 
     * @return 現在解析中のファイル情報．解析が始まっていない場合はnull，解析が終了している場合は最後に解析したファイル
     */
    public FileInfo getCurrentFile() {
        return this.fileInfos.size() > 0 ? this.fileInfos.last() : null;
    }

    /**
     * ファイル情報の SortedSet を返す．
     * 
     * @return ファイル情報の SortedSet
     */
    public SortedSet<FileInfo> getFileInfos() {
        return Collections.unmodifiableSortedSet(this.fileInfos);
    }

    /**
     * 情報を持っているファイルの個数を返す
     * 
     * @return ファイルの個数
     */
    public int getFileCount() {
        return this.fileInfos.size();
    }

    /**
     * 登録されているファイルの総行数を返す
     * 
     * @return 登録されているファイルの総行数
     */
    public int getTotalLOC() {
        int loc = 0;
        for (final FileInfo file : this.getFileInfos()) {
            loc += file.getLOC();
        }
        return loc;
    }

    /**
     * ファイル情報をクリア
     */
    public void clear() {
        this.fileInfos.clear();
    }

    /**
     * 
     * コンストラクタ． シングルトンパターンで実装しているために private がついている．
     */
    public FileInfoManager() {
        this.fileInfos = new TreeSet<FileInfo>();
    }

    /**
     * 
     * ファイル情報 (FileInfo) を格納する変数．
     */
    private final SortedSet<FileInfo> fileInfos;
}
