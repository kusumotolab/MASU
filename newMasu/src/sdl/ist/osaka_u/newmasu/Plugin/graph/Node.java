package sdl.ist.osaka_u.newmasu.Plugin.graph;

import com.sun.tools.javac.util.Pair;
import sdl.ist.osaka_u.newmasu.Plugin.CFG.TestWriter;

import java.util.*;

public class Node {

    private String label = "";
    private Boolean isDummy = false;
    private String shape = "ellipse";
    public void copy(Node node){
        label = node.label;
        isDummy = node.isDummy;
        shape = node.shape;
    }

    private List<Node> children = new LinkedList<>();

    public List<Node> getChildren() {
        return children;
    }
    public Node addChildren(Node node){
        children.add(node);
        return node;
    }

    public String getLabel() { return label; }
    public void setLabel(String str){ label = str; }

    public Boolean getDummy() { return isDummy; }
    public void setDummy(Boolean dummy) { isDummy = dummy; }

    public String getShape() { return shape; }
    public void setShape(String shape) { this.shape = shape; }

    public Node(String name, Boolean dummy, String shape){
        label=name; isDummy=dummy; this.shape=shape;
    }

    public String toGraphDefine(){
        return toGraphId() + " [label=\"" + toGraphLabel() + "\", shape=\"" + shape + "\"];";
    }

    public String toGraphLabel(){
        return sanitize(label.trim());
    }
    public String toGraphId(){
        return Integer.toString(hashCode());
    }




    public static Set<Node> used = new LinkedHashSet<>();
    public void removeDummy(Map<Pair<Node,Node>,String> edge){
        used.clear();
        __removeDummyWorker(edge);
    }
    private void __removeDummyWorker(Map<Pair<Node,Node>,String> edge){
        if(used.contains(this))
            return;
        used.add(this);

        // dummy node must have 0 or 1 children
        while(!children.isEmpty()){
            // if children contains dummy
            boolean hasDummy = false;
            for(Node c : children)
                if(c.getDummy())
                    hasDummy = true;
            if(!hasDummy)
                break;

            Pair<Node,Node> del = null;
            for(Node c1 : children){
                for(Node c2 : c1.children){
                    if(c1.getDummy()){
                        del = new Pair<>(c1,c2);
                        final Pair<Node,Node> e1 = new Pair<>(this, c1);
                        final Pair<Node,Node> e2 = new Pair<>(c1, c2);
                        final Pair<Node,Node> rep = new Pair<>(this, c2);
                        if(edge.containsKey(e1)){
                            final String s = edge.get(e1);
                            edge.remove(e1);
                            edge.put(rep,s);
                        }
                        if(edge.containsKey(e2)){
                            final String s = edge.get(e2);
                            edge.remove(e2);
                            edge.put(rep,s);
                        }
                        break;
                    }
                }
                if(del!=null)
                    break;
            }

            if(del!=null){
                children.remove(del.fst);
                children.add(del.snd);
            }
        }

        for(Node t : children)
            t.__removeDummyWorker(edge);
    }



    private static Set<Node> usedNodeInToGraph = new LinkedHashSet<>();
    public void print(Map<Pair<Node,Node>,String> edge){
        usedNodeInToGraph.clear();
        TestWriter.println(__toGraph(edge));
    }
    private String __toGraph(Map<Pair<Node,Node>,String> edge){
        if(usedNodeInToGraph.contains(this))
            return "";
        usedNodeInToGraph.add(this);

        StringBuilder sb = new StringBuilder();
        sb.append(toGraphDefine());
        sb.append(System.lineSeparator());

        for(Node c : children){
            sb.append(toGraphId() + " -> " + c.toGraphId());

            StringBuilder edgesb = new StringBuilder();
            edgesb.append(" [label=\"");
            Pair<Node,Node> p = new Pair<>(this,c);
            if(edge.containsKey(p)){
                edgesb.append(sanitize(edge.get(p)));
            }
            edgesb.append("\"];");
            sb.append(edgesb.toString());

            sb.append(System.lineSeparator());
        }

        for(Node f : children)
            sb.append( f.__toGraph(edge) );

        return sb.toString();
    }

    private String sanitize(String str){
        final StringBuilder sb = new StringBuilder();
        for(char c : str.toCharArray()){
            if(c=='"')
                sb.append('\\');
            sb.append(c);
        }
        return sb.toString();
    }
}