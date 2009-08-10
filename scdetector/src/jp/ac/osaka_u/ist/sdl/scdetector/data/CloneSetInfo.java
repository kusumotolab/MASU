package jp.ac.osaka_u.ist.sdl.scdetector.data;


import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * クローンセットを表すクラス
 * 
 * @author higo
 *
 */
public class CloneSetInfo implements Comparable<CloneSetInfo> {

    /**
     * コンストラクタ
     */
    public CloneSetInfo() {
        this.codeclones = new HashSet<CodeCloneInfo>();
        this.id = number++;
    }

    /**
     * コードクローンを追加する
     * 
     * @param codeclone 追加するコードクローン
     * @return 追加した場合はtrue,　すでに含まれており追加しなかった場合はfalse
     */
    public boolean add(final CodeCloneInfo codeclone) {
        return this.codeclones.add(codeclone);
    }

    /**
     * コードクローン群を追加する
     * 
     * @param codeclones 追加するコードクローン群
     */
    public void addAll(final Collection<CodeCloneInfo> codeclones) {

        for (final CodeCloneInfo codeFragment : codeclones) {
            this.add(codeFragment);
        }
    }

    /**
     * クローンセットを構成するコードクローン群を返す
     * 
     * @return　クローンセットを構成するコードクローン群
     */
    public Set<CodeCloneInfo> getCodeClones() {
        return Collections.unmodifiableSet(this.codeclones);
    }

    /**
     * クローンセットのIDを返す
     * 
     * @return　クローンセットのID
     */
    public int getID() {
        return this.id;
    }

    /**
     * クローンセットに含まれるコードクローンの数を返す
     * 
     * @return　クローンセットに含まれるコードクローンの数
     */
    public int getNumberOfCodeclones() {
        return this.codeclones.size();
    }

    /**
     * クローンセットに含まれるギャップの数を返す
     * 
     * @return　クローンセットに含まれるギャップの数
     */
    public int getGapsNumber() {

        int gap = 0;

        for (final CodeCloneInfo codeFragment : this.getCodeClones()) {
            gap += codeFragment.getGapsNumber();
        }

        return gap;
    }

    /**
     * クローンセットの長さ（含まれるコードクローンの大きさ）を返す
     * 
     * @return　クローンセットの長さ（含まれるコードクローンの大きさ）
     */
    public int getLength() {
        int total = 0;
        for (final CodeCloneInfo codeFragment : this.getCodeClones()) {
            total += codeFragment.length();
        }

        return total / this.getNumberOfCodeclones();
    }

    @Override
    public int compareTo(CloneSetInfo o) {

        if (this.getID() < o.getID()) {
            return -1;
        } else if (this.getID() > o.getID()) {
            return 1;
        } else {
            return 0;
        }
    }

    final private Set<CodeCloneInfo> codeclones;

    final private int id;

    private static int number = 0;
}
