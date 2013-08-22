package sdl.ist.osaka_u.newmasu.Plugin.graph;

import java.util.*;

public class Branch {

    private LinkedList<Node> path = new LinkedList<>();

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

    static Map<Node,Node> replaceNodes = new HashMap<>();
    public void insert(Node node){
        if(!path.isEmpty() && path.getLast().getDummy() && node.getDummy())
            replaceNodes.put(path.getLast(), node);
        else if(!path.isEmpty() && path.getLast().getDummy())
            path.getLast().copy(node);
        else
            path.add(path.size(),node);
    }


    private static Set<Node> usedNodeInToGraph = new HashSet<>();
    public void print(){
        usedNodeInToGraph.clear();
        System.out.println(toGraph());
    }
    public String toGraph(){
        StringBuilder sb = new StringBuilder();
        Node prev = null;
        for(Node node : path){
            if(replaceNodes.containsKey(node))
                node = replaceNodes.get(node);
            if(!usedNodeInToGraph.contains(node)){
                usedNodeInToGraph.add(node);
                sb.append(node.toGraph());
            }
            sb.append(System.lineSeparator());
            if(prev!=null)
                sb.append(prev.toGraphId() + " -> " + node.toGraphId());
            prev = node;
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}
