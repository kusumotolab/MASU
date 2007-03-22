package jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph_builder;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph.ClassVertex;
import jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph.Edge;
import jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph.FieldVertex;
import jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph.Graph;
import jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph.MethodVertex;
import jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph.Vertex;


/**
 * Graphvizの形式でグラフを出力する.
 * @author rniitani
 */
public class GraphvizWriter {
    final PrintWriter writer;

    final Graph graph;

    /**
     * 指定されたファイルに出力.
     * 
     * @param graph
     * @param file
     * @throws IOException
     */
    static public void write(final File file, final Graph graph) throws IOException {
        GraphvizWriter graphvizWriter = new GraphvizWriter(file, graph);
        graphvizWriter.writeGraph();
    }

    private GraphvizWriter(final File file, final Graph graph) throws IOException {
        this.writer = new PrintWriter(new BufferedWriter(new FileWriter(file)), true);
        this.graph = graph;
    }

    private void writeGraph() throws IOException {
        writer.println("digraph G {");
        for (Vertex v : graph.getVertices()) {
            // TODO クラス以外の対応
            if (v instanceof ClassVertex) {
                writeClass((ClassVertex) v);
            }
        }
        for (Edge e : graph.getEdges()) {
            writeEdge(e);
        }
        writer.println("}");
        writer.close();
    }

    /**
     * クラス頂点を出力する.
     * 
     * クラスのIDは Graphviz の頂点に.
     * メソッドやフィールドは Graphviz のポートに.
     * 
     * @param v
     */
    private void writeClass(ClassVertex v) {
        writer.println(v.getId() + " [");
        writer.println("shape=\"plaintext\"");
        writer.println("label=<");
        writer.println("<TABLE BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\">");
        writer.println("<TR><TD ALIGN=\"LEFT\">" + v.getLabel() + "</TD></TR>");

        // メソッド
        writer.println("<TR><TD ALIGN=\"LEFT\">");
        writeMethods(v);
        writer.println("</TD></TR>");

        // フィールド
        writer.println("<TR><TD ALIGN=\"LEFT\">");
        writeFields(v);
        writer.println("</TD></TR>");

        writer.println("</TABLE>");
        writer.println(">];");
    }

    private void writeMethods(ClassVertex classVertex) {
        if (classVertex.getMethods().size() <= 0)
            return;
        writer.println("<TABLE BORDER=\"0\" CELLBORDER=\"0\">");
        for (MethodVertex methodVertex : classVertex.getMethods()) {
            writer.println("<TR><TD ALIGN=\"LEFT\" PORT=\"" + methodVertex.getId() + "\">"
                    + methodVertex.getLabel() + "</TD></TR>");
        }
        writer.println("</TABLE>");
    }

    private void writeFields(ClassVertex classVertex) {
        if (classVertex.getFields().size() <= 0)
            return;
        writer.println("<TABLE BORDER=\"0\" CELLBORDER=\"0\">");
        for (FieldVertex fieldVertex : classVertex.getFields()) {
            writer.println("<TR><TD ALIGN=\"LEFT\" PORT=\"" + fieldVertex.getId() + "\">"
                    + fieldVertex.getLabel() + "</TD></TR>");
        }
        writer.println("</TABLE>");
    }

    private void writeEdge(Edge edge) {
        writeVertexOfEdge(edge.getFrom());
        writer.print(" -> ");
        writeVertexOfEdge(edge.getTo());
        writer.println(" [");
        writer.println("label = \"" + edge.getLabel() + "\"");
        writer.println("];");
    }

    /**
     * 辺を出力するときの頂点片方を出力.
     * 
     * 頂点名とポートの両方必要なものと頂点名だけでいいものがある.
     * @param vertex
     */
    private void writeVertexOfEdge(Vertex vertex) {
        if (vertex instanceof MethodVertex) {
            MethodVertex v = (MethodVertex) vertex;
            writer.print(v.getOwnerClass().getId() + ":" + v.getId());
        } else if (vertex instanceof FieldVertex) {
            FieldVertex v = (FieldVertex) vertex;
            writer.print(v.getOwnerClass().getId() + ":" + v.getId());
        } else {
            writer.print(vertex.getId());
            
        }
    }
}
