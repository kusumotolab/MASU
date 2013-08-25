package sdl.ist.osaka_u.newmasu.Plugin.graph;

import com.sun.javafx.geom.Edge;
import com.sun.tools.javac.util.Pair;
import sdl.ist.osaka_u.newmasu.Plugin.CFG.TestWriter;

import java.util.*;

public class Branch {

    private final LinkedList<Node> path = new LinkedList<>();

    public Branch(Node first, Node last){
        path.add(first);
        path.add(last);
    }
    public Branch(){}

    public Branch newBranch(){
        final Branch b = new Branch();
        b.path.add(path.getLast());
        return b;
    }

    public LinkedList<Node> getPath(){ return path;}

//    private static Map<Node,Node> replaceNodes = new LinkedHashMap<>();
    public void insert(Node node){
        /*
        if(!path.isEmpty() && path.getLast().getDummy() && node.getDummy()){
            //replaceNodes.put(path.getLast(), node);
            int id = path.getLast().getId();
            path.getLast().copy(node);
            path.getLast().setId(node.getId());
            path.add(node);
        }
        else if(!path.isEmpty() && path.getLast().getDummy()){
            // path.getLast().copy(node);
            path.getLast().copy(node);
        path.getLast().setId(node.getId());
        }
        else
        */
            //path.add(path.size(),node);
        path.add(node);
    }

    public void removeEdge(Map<Pair<Node,Node>,String> edge){
        for(int i=0; i<path.size(); i++){
            if(path.size()-i < 3)
                break;
            if(path.get(i+1).getDummy()){
                final Pair<Node,Node> p1 = new Pair<>(path.get(i), path.get(i+1));
                final Pair<Node,Node> p2 = new Pair<>(path.get(i+1), path.get(i+2));
                final Pair<Node, Node> np = new Pair<>(path.get(i), path.get(i+2));
                if(edge.containsKey(p1)){
                    final String v = edge.get(p1);
                    edge.remove(p1);
                    edge.put(np,v);
                }
                if(edge.containsKey(p2)){
                    final String v = edge.get(p2);
                    edge.remove(p2);
                    edge.put(np,v);
                }
                path.remove(i+1);
            }
        }
    }


    private static Set<Node> usedNodeInToGraph = new LinkedHashSet<>();
    public void print(Map<Pair<Node,Node>,String> edge){
        usedNodeInToGraph.clear();
        TestWriter.println(toGraph(edge));
    }
    public String toGraph(Map<Pair<Node,Node>,String> edge){
        StringBuilder sb = new StringBuilder();
        Node prev = null;
        for(Node node : path){
//            if(replaceNodes.containsKey(node))
//                node = replaceNodes.get(node);
            if(!usedNodeInToGraph.contains(node)){
                usedNodeInToGraph.add(node);
                sb.append(node.toGraph());
            }
            sb.append(System.lineSeparator());
            if(prev!=null){
                sb.append(prev.toGraphId() + " -> " + node.toGraphId());
                final Pair<Integer,Integer> target = new Pair<>(prev.getId(),node.getId());

                StringBuilder edgesb = new StringBuilder();
                edgesb.append(" [label=\"");
                for(Map.Entry<Pair<Node,Node>,String> e : edge.entrySet()){
                    if(e.getKey().fst.getId() == target.fst &&
                            e.getKey().snd.getId() == target.snd)
                        edgesb.append(e.getValue());
                }
                edgesb.append("\"];");
                sb.append(edgesb.toString());
            }
            prev = node;
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}
