package jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph;

import java.util.Set;

public interface Graph {
    Set<Vertex> getVertices();
    Set<Edge> getEdges();
//    int getVertexId(Vertex v);
//    int getEdgeId(Edge e);
}
