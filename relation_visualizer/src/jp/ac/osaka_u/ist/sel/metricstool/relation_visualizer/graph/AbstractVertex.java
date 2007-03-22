package jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Member;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Visualizable;


/**
 * {@link Visualizable} と {@link Member} の情報を保持・実装した {@link Vertex} の抽象クラス.
 * 
 * {@link Visualizable} と {@link Member} の情報は可視化の表示の変更に使用される.
 * 通常2つのメンバは同じオブジェクトを指す.
 * 
 * @author rniitani
 */
abstract public class AbstractVertex implements Vertex, Visualizable, Member {

    /** ID */
    final private int id;
    /** 表示される文字列 */
    final private String label;
    /** 整列用文字列 */
    final private String sortKey;
    /** 可視性 */
    final private Visualizable visualizable;
    /** メンバとしての位置づけ */
    final private Member member;

    public AbstractVertex(final int id, final String label, final String sortKey,
            final Visualizable visualizable, final Member member) {
        this.id = id;
        this.label = label;
        this.sortKey = sortKey;
        this.visualizable = visualizable;
        this.member = member;
    }
    
    /**
     * ID を返す.
     */
    public int getId() {
        return id;
    }

    /**
     * クラス名を返す.
     */
    public String getLabel() {
        return label;
    }

    /**
     * 整列用文字列を返す.
     * @return
     */
    public String getSortKey() {
        return sortKey;
    }

    /**
     * コンストラクタで渡された {@link Member} の値.
     */
    public boolean isInstanceMember() {
        return member.isInstanceMember();
    }

    /**
     * コンストラクタで渡された {@link Member} の値.
     */
    public boolean isStaticMember() {
        return member.isStaticMember();
    }

    /**
     * コンストラクタで渡された {@link Visualizable} の値.
     */
    public boolean isInheritanceVisible() {
        return visualizable.isInheritanceVisible();
    }

    /**
     * コンストラクタで渡された {@link Visualizable} の値.
     */
    public boolean isNamespaceVisible() {
        return visualizable.isNamespaceVisible();
    }

    /**
     * コンストラクタで渡された {@link Visualizable} の値.
     */
    public boolean isPrivateVisible() {
        return visualizable.isPrivateVisible();
    }

    /**
     * コンストラクタで渡された {@link Visualizable} の値.
     */
    public boolean isPublicVisible() {
        return visualizable.isPublicVisible();
    }

}