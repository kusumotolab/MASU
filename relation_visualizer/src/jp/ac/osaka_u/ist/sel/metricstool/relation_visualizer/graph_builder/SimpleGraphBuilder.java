package jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph_builder;


import java.util.HashMap;
import java.util.Map;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph.SimpleGraph;
import jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph.ClassVertex;
import jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph.EDGE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph.Edge;
import jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph.FieldVertex;
import jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph.Graph;
import jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph.MethodVertex;
import jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph.Vertex;


/**
 * グラフを構築する.
 * 
 * 頂点が登録されていない辺の追加はできない.
 * 
 * @author rniitani
 */
public class SimpleGraphBuilder {
    /**
     * ～Info クラスのオブジェクトからそれに対応する {@link Vertex} へのマッピング.
     */
    final Map<Object, Vertex> info2vertex;

    /**
     * 生成されるグラフ.
     */
    final SimpleGraph graph;

    /**
     * デフォルトコンストラクタ.
     */
    public SimpleGraphBuilder() {
        this.info2vertex = new HashMap<Object, Vertex>();
        this.graph = new SimpleGraph();
    }

    /**
     * 構築されたグラフ.
     */
    public Graph getResult() {
        return this.graph;
    }

    /**
     * グラフにクラスを追加する.
     * 
     * @param classInfo クラスの情報.
     */
    public void addClass(TargetClassInfo classInfo) {
        if (info2vertex.containsKey(classInfo)) {
            // 既に登録済み
            return;
        }
        Vertex classVertex = new ClassVertex(graph.nextId(), extractClassVertexLabel(classInfo),
                classInfo.getClassName(), classInfo, classInfo);
        addVertex(classInfo, classVertex);
    }

    /**
     * クラスにメソッドを追加する.
     * 
     * @param classInfo クラスの情報.
     * @param methodInfo メソッドの情報.
     */
    public void addMethod(TargetClassInfo classInfo, TargetMethodInfo methodInfo) {
        assert (info2vertex.containsKey(classInfo));

        ClassVertex classVertex = (ClassVertex) info2vertex.get(classInfo);
        MethodVertex methodVertex = new MethodVertex(graph.nextId(), classVertex,
                extractMethodVertexLabel(methodInfo), methodInfo.getMethodName(), methodInfo,
                methodInfo);
        classVertex.addMethod(methodVertex);
        addVertex(methodInfo, methodVertex);
    }

    /**
     * クラスにフィールドを追加する.
     * 
     * @param classInfo クラスの情報.
     * @param fieldInfo フィールドの情報.
     */
    public void addField(TargetClassInfo classInfo, TargetFieldInfo fieldInfo) {
        assert (info2vertex.containsKey(classInfo));

        ClassVertex classVertex = (ClassVertex) info2vertex.get(classInfo);
        FieldVertex fieldVertex = new FieldVertex(graph.nextId(), classVertex,
                extractFieldVertexLabel(fieldInfo), fieldInfo.getName(), fieldInfo, fieldInfo);
        classVertex.addField(fieldVertex);
        addVertex(fieldInfo, fieldVertex);
    }

    private void addVertex(Object info, Vertex v) {
        graph.addVertex(v);
        info2vertex.put(info, v);
    }

    /**
     * 呼び出し関係を追加する.
     * 
     * 頂点がグラフに存在するとは限らない.
     * 存在しなかった場合辺を生成しない.
     * 
     * @param caller 呼び出し元.
     * @param callee 呼び出し先.
     */
    public void addCallRelation(TargetMethodInfo caller, MethodInfo callee) {
        addRelation(EDGE_TYPE.CALL, caller, callee);
    }

    /**
     * 親子クラス関係を追加する.
     * 
     * 頂点がグラフに存在するとは限らない.
     * 存在しなかった場合辺を生成しない.
     * 
     * @param superclass 親クラス.
     * @param subclass 子クラス.
     */
    public void addSuperclassRelation(TargetClassInfo superclass, ClassInfo subclass) {
        addRelation(EDGE_TYPE.SUPERCLASS, subclass, superclass);
    }

    /**
     * 内部クラス関係を追加する.
     * 
     * 頂点がグラフに存在するとは限らない.
     * 存在しなかった場合辺を生成しない.
     * 
     * @param outer 外部クラス.
     * @param inner 内部クラス.
     */
    public void addInnerclassRelation(TargetClassInfo outer, ClassInfo inner) {
        addRelation(EDGE_TYPE.SUPERCLASS, outer, inner);
    }

    /**
     * 関係を追加する.
     * addXXXRelation の実装.
     * 
     * 頂点がグラフに存在するとは限らない.
     * 存在しなかった場合辺を生成しない.
     * 
     * @param type
     * @param from {@link #info2vertex} に登録されているオブジェクト.
     * @param to {@link #info2vertex} に登録されているオブジェクト.
     */
    private void addRelation(EDGE_TYPE type, Object from, Object to) {
        if (!info2vertex.containsKey(from) || !info2vertex.containsKey(to))
            return;

        Vertex fromVertex = info2vertex.get(from);
        Vertex toVertex = info2vertex.get(to);
        Edge edge = new Edge(graph.nextId(), type, fromVertex, toVertex);

        graph.addEdge(edge);
    }

    private String extractClassVertexLabel(TargetClassInfo classInfo) {
        return classInfo.getClassName();
    }

    private String extractMethodVertexLabel(TargetMethodInfo methodInfo) {
        StringBuffer buffer = new StringBuffer();
        // 返値
        buffer.append(methodInfo.getReturnType().getTypeName());
        buffer.append(' ');
        // メソッド名
        buffer.append(methodInfo.getMethodName());
        buffer.append('(');
        // 引数リスト
        // とりあえず型だけ
        // 以下 join 風の処理をしているだけ
        int paramSize = methodInfo.getParameters().size();
        int paramCount = 0;
        for (ParameterInfo param : methodInfo.getParameters()) {
            buffer.append(param.getType().getTypeName());
            paramCount++;
            if (paramCount < paramSize)
                buffer.append(", ");
        }
        buffer.append(')');
        return buffer.toString();
    }

    private String extractFieldVertexLabel(TargetFieldInfo fieldInfo) {
        return fieldInfo.getType().getTypeName() + " " + fieldInfo.getName();
    }
}
