package jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph;


public class Edge {
    final private Vertex from;

    final private Vertex to;

    final private EDGE_TYPE type;

    final private int id;

    public Edge(final int id, final EDGE_TYPE type, final Vertex from, final Vertex to) {
        this.type = type;
        this.id = id;
        this.from = from;
        this.to = to;
    }

    public Vertex getFrom() {
        return from;
    }

    public Vertex getTo() {
        return to;
    }

    public String getLabel() {
        return type.getLabel();
    }

    int getId() {
        return id;
    }
}
