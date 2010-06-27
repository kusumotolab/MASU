package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 
 * @author higo
 * 
 * 対象ファイルを格納するためのクラス． TargetFile を要素として持つ．
 * 
 * since 2006.11.12
 */
public final class TargetFileManager implements Iterable<TargetFile> {

    /**
     * 
     * @param targetFile 追加する対象ファイル (TargetFile)
     */
    public void add(final TargetFile targetFile) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == targetFile) {
            throw new NullPointerException();
        }

        this.targetFiles.add(targetFile);
    }

    public Iterator<TargetFile> iterator() {
        Set<TargetFile> unmodifiableTargetFiles = Collections.unmodifiableSet(this.targetFiles);
        return unmodifiableTargetFiles.iterator();
    }

    /**
     * 対象ファイルの数を返す
     * 
     * @return 対象ファイルの数
     */
    public int size() {
        return this.targetFiles.size();
    }

    /**
     * 対象ファイルをクリア
     */
    public void clear() {
        this.targetFiles.clear();
    }

    /**
     * 登録されている対象ファイルのSortedSetを返す
     * 
     * @return 登録されている対象ファイルのSortedSet
     */
    public SortedSet<TargetFile> getFiles() {
        return Collections.unmodifiableSortedSet(this.targetFiles);
    }

    /**
     * 
     * コンストラクタ． 
     * 以前は HashSet を用いていたが，同じディレクトリのファイルはまとめて返すほうがよいので，TreeSet に変更した．
     */
    public TargetFileManager() {
        this.targetFiles = new TreeSet<TargetFile>();
    }

    /**
     * 
     * 対象ファイル (TargetFile) を格納する変数．
     */
    private final SortedSet<TargetFile> targetFiles;
}
