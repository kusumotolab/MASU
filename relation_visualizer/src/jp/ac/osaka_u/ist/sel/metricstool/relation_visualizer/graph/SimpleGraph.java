package jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * {@link Vertex} と {@link Edge} とIDの重複を管理しているだけの単純なグラフ.
 * 
 * それぞれ固有のIDを持っている.
 * 
 * @author rniitani
 */
public class SimpleGraph implements Graph {
    private Set<Edge> edges = new HashSet<Edge>();
    private Set<Vertex> vertices = new HashSet<Vertex>();
    private Set<Integer> registeredIds = new HashSet<Integer>();
    private int nextId = 0;
    
    /**
     * 全ての {@link Edge} の集合を返す.
     */
    public Set<Edge> getEdges() {
        return Collections.unmodifiableSet(edges);
    }

    /**
     * 全ての {@link Vertex} の集合を返す.
     */
    public Set<Vertex> getVertices() {
        return Collections.unmodifiableSet(vertices);
    }
    
//    /**
//     * {@link Edge} に対応するIDを返す.
//     */
//    public int getEdgeId(Edge e) {
//        return edges.get(e);
//    }
//
//    /**
//     * {@link Vertex} に対応するIDを返す.
//     */
//    public int getVertexId(Vertex v) {
//        return vertices.get(v);
//    }

     public int nextId() {
        return nextId++;
    }

     /**
     * {@link Edge} の追加.
     * @param edge
     */
    public void addEdge(Edge edge) {
        assert(vertices.contains(edge.getFrom()));
        assert(vertices.contains(edge.getTo()));
        assert(!registeredIds.contains(edge.getId()));
        
        edges.add(edge);
    }

    /**
     * {@link Vertex} の追加.
     * @param vertex
     */
    public void addVertex(Vertex vertex) {
        assert(!registeredIds.contains(vertex.getId()));
        vertices.add(vertex);
    }
    
}
