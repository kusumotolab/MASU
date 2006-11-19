package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;


/**
 * 
 * @author y-higo
 * 
 * 対象ファイルを格納するためのクラス． TargetFile を要素として持つ．
 * 
 * since 2006.11.12
 */
public class TargetFileData implements Iterable<TargetFile> {

    /**
     * 
     * @return 対象ファイルを格納している Set を返す．
     * 
     * シングルトンパターンを用いて実装している．
     */
    public static TargetFileData getInstance() {
        if (TARGET_FILE_DATA == null) {
            TARGET_FILE_DATA = new TargetFileData();
        }
        return TARGET_FILE_DATA;
    }

    /**
     * 
     * @param targetFile 追加する対象ファイル (TargetFile)
     */
    public void add(final TargetFile targetFile) {
        this.targetFiles.add(targetFile);
    }

    public Iterator<TargetFile> iterator() {
        return this.targetFiles.iterator();
    }

    /**
     * 
     * コンストラクタ． シングルトンパターンで実装しているために private がついている
     * 以前は HashSet を用いていたが，同じディレクトリのファイルはまとめて返すほうがよいので，TreeSet に変更した．
     */
    private TargetFileData() {
        /*this.targetFiles = new HashSet<TargetFile>();*/
        this.targetFiles = new TreeSet<TargetFile>();
    }

    /**
     * 
     * シングルトンパターンを実装するための変数．
     */
    private static TargetFileData TARGET_FILE_DATA = null;

    /**
     * 
     * 対象ファイル (TargetFile) を格納する変数．
     */
    private final Set<TargetFile> targetFiles;
}
