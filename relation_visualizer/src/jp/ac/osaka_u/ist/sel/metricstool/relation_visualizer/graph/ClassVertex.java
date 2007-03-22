package jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph;


import java.util.Collections;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Member;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Visualizable;


public class ClassVertex extends AbstractVertex {
    final private SortedSet<MethodVertex> methods;

    final private SortedSet<FieldVertex> fields;

    /**
     * コンストラクタ.
     * 
     * @param classInfo
     *      クラスの情報.
     *      必要な情報はここから取得する.
     */
    public ClassVertex(final int id, final String label, final String sortKey,
            final Visualizable visualizable, final Member member) {
        super(id, label, sortKey, visualizable, member);
        // 名前でソート
        this.methods = new TreeSet<MethodVertex>(new Comparator<MethodVertex>() {
            public int compare(MethodVertex lhs, MethodVertex rhs) {
                return lhs.getLabel().compareTo(rhs.getLabel());
            }

        });
        // 名前でソート
        this.fields = new TreeSet<FieldVertex>(new Comparator<FieldVertex>() {
            public int compare(FieldVertex lhs, FieldVertex rhs) {
                return lhs.getLabel().compareTo(rhs.getLabel());
            }

        });
    }

    /**
     * メソッド一覧.
     */
    public SortedSet<MethodVertex> getMethods() {
        return Collections.unmodifiableSortedSet(methods);
    }

    /**
     * フィールド一覧.
     */
    public SortedSet<FieldVertex> getFields() {
        return Collections.unmodifiableSortedSet(fields);
    }

    /**
     * メソッドを追加する.
     * @param vertex メソッド頂点
     */
    public void addMethod(MethodVertex vertex) {
        methods.add(vertex);
    }

    /**
     * フィールドを追加する.
     * @param vertex フィールド頂点
     */
    public void addField(FieldVertex vertex) {
        fields.add(vertex);
    }

    /**
     * クラスは内部にグラフを持たない.
     * @return false
     */
    public boolean hasSubgraphs() {
        return false;
    }

    /**
     * クラスは内部に頂点を持つ.
     * @return true
     */
    public boolean hasSubvertices() {
        return true;
    }

}
