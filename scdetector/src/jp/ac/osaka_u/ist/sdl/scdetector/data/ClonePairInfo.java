package jp.ac.osaka_u.ist.sdl.scdetector.data;


import java.util.Iterator;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;


/**
 * クローンペアを表すクラス
 * 
 * @author higo
 *
 */
public class ClonePairInfo implements Cloneable, Comparable<ClonePairInfo> {

    /**
     * コンストラクタ
     */
    public ClonePairInfo() {

        this.codeFragmentA = new CodeCloneInfo();
        this.codeFragmentB = new CodeCloneInfo();

        this.id = number++;
    }

    /**
     * コンストラクタ
     * 
     * @param elementA 初期要素A
     * @param elementB 初期要素B
     */
    public ClonePairInfo(final ExecutableElementInfo elementA, final ExecutableElementInfo elementB) {

        this();

        this.add(elementA, elementB);
    }

    /**
     * クローンペアに要素を追加する
     * 
     * @param elementA 追加要素A
     * @param elementB 追加要素B
     */
    public void add(final ExecutableElementInfo elementA, final ExecutableElementInfo elementB) {
        this.codeFragmentA.add(elementA);
        this.codeFragmentB.add(elementB);
    }

    /**
     * クローンペアに要素の集合を追加する
     * 
     * @param elementsA 要素群A
     * @param elementsB 要素群B
     */
    public void addAll(final SortedSet<ExecutableElementInfo> elementsA,
            final SortedSet<ExecutableElementInfo> elementsB) {
        this.codeFragmentA.addAll(elementsA);
        this.codeFragmentB.addAll(elementsB);
    }

    /**
     *　クローンペアの長さを返す
     *
     * @return　クローンペアの長さ
     */
    public int length() {
        return (this.codeFragmentA.length() + this.codeFragmentB.length()) / 2;
    }

    /**
     * コードクローンAを返す
     * 
     * @return　コードクローンA
     */
    public CodeCloneInfo getCodeFragmentA() {
        return this.codeFragmentA;
    }

    /**
     * コードクローンBを返す
     * 
     * @return　コードクローンB
     */
    public CodeCloneInfo getCodeFragmentB() {
        return this.codeFragmentB;
    }

    /**
     * 引数で与えられたクローンペアに，このクローンペアが包含されているか判定する
     * 
     * @param clonePair 対象クローンペア
     * @return 包含される場合はtrue, そうでない場合はfalse
     */
    public boolean subsumedBy(final ClonePairInfo clonePair) {

        return (this.getCodeFragmentA().subsumedBy(clonePair.getCodeFragmentA()) && this
                .getCodeFragmentB().subsumedBy(clonePair.getCodeFragmentB()))
                || (this.getCodeFragmentB().subsumedBy(clonePair.getCodeFragmentA()) && this
                        .getCodeFragmentA().subsumedBy(clonePair.getCodeFragmentB()));
    }

    /**
     * クローンペアのIDを返す
     * 
     * @return クローンペアのID
     */
    public int getID() {
        return this.id;
    }

    @Override
    public int hashCode() {
        return this.getCodeFragmentA().hashCode() + this.getCodeFragmentB().hashCode();
    }

    @Override
    public boolean equals(Object o) {

        if (null == o) {
            return false;
        }

        if (!(o instanceof ClonePairInfo)) {
            return false;
        }

        final ClonePairInfo target = (ClonePairInfo) o;
        return (this.getCodeFragmentA().equals(target.getCodeFragmentA()) && this
                .getCodeFragmentB().equals(target.getCodeFragmentB()))
                || (this.getCodeFragmentA().equals(target.getCodeFragmentB()) && this
                        .getCodeFragmentB().equals(target.getCodeFragmentA()));
    }

    @Override
    public ClonePairInfo clone() {

        final ClonePairInfo clonePair = new ClonePairInfo();
        final CodeCloneInfo cloneA = this.getCodeFragmentA();
        final CodeCloneInfo cloneB = this.getCodeFragmentB();
        clonePair.addAll(cloneA.getElements(), cloneB.getElements());

        return clonePair;
    }

    @Override
    public int compareTo(final ClonePairInfo clonePair) {

        if (null == clonePair) {
            throw new IllegalArgumentException();
        }

        final CodeCloneInfo thisCodeA = this.getCodeFragmentA();
        final CodeCloneInfo thisCodeB = this.getCodeFragmentB();
        final CodeCloneInfo targetCodeA = clonePair.getCodeFragmentA();
        final CodeCloneInfo targetCodeB = clonePair.getCodeFragmentB();

        // コードクローンを構成する要素数で比較
        if (thisCodeA.length() > targetCodeA.length()) {
            return 1;
        } else if (thisCodeA.length() < targetCodeA.length()) {
            return -1;
        } else if (thisCodeB.length() > targetCodeB.length()) {
            return 1;
        } else if (thisCodeB.length() < targetCodeB.length()) {
            return -1;
        }

        // コードクローンAの位置情報で比較
        {
            Iterator<ExecutableElementInfo> thisIterator = thisCodeA.getElements().iterator();
            Iterator<ExecutableElementInfo> targetIterator = targetCodeA.getElements().iterator();
            while (thisIterator.hasNext() && targetIterator.hasNext()) {
                int order = thisIterator.next().compareTo(targetIterator.next());
                if (0 != order) {
                    return order;
                }
            }
        }

        // コードクローンBの位置情報で比較
        {
            Iterator<ExecutableElementInfo> thisIterator = thisCodeB.getElements().iterator();
            Iterator<ExecutableElementInfo> targetIterator = targetCodeB.getElements().iterator();
            while (thisIterator.hasNext() && targetIterator.hasNext()) {
                int order = thisIterator.next().compareTo(targetIterator.next());
                if (0 != order) {
                    return order;
                }
            }
        }

        return 0;
    }

    final private CodeCloneInfo codeFragmentA;

    final private CodeCloneInfo codeFragmentB;

    final private int id;

    private static int number = 0;
}
