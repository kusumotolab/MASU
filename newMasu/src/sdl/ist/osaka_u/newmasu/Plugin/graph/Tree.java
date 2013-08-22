package sdl.ist.osaka_u.newmasu.Plugin.graph;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Tree {

    public LinkedList<Node> path = new LinkedList<>();

    //public Tree(){
        //path.add(new Node("S"));
        //path.add(new Node("E"));
    //}

    public Tree(Node first, Node last){
        path.add(first);
        path.add(last);
    }

    public Tree newBranch(){
        return new Tree(path.get(path.size()-2), path.getLast());
    }

    public void insert(Node node){
        path.add(path.size()-1, node);
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
