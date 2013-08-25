package sdl.ist.osaka_u.newmasu.Plugin.graph;

import com.sun.tools.javac.util.Pair;
import sdl.ist.osaka_u.newmasu.Plugin.CFG.TestWriter;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.sun.tools.javac.util.Pair;

public class Tree {
    private Node my = null;
    private Set<Tree> children = new LinkedHashSet<>();

    public Tree(Node n){
        my = n;
    }

    public Node getMy() {
        return my;
    }

    public void setMy(Node my) {
        this.my = my;
    }

    public Set<Tree> getChildren() {
        return children;
    }

    public void addChildren(Tree child) {
        this.children.add(child);
    }

    public static Set<Tree> used = new LinkedHashSet<>();
    public void dump(){
        if(used.contains(this))
            return;
        used.add(this);
        System.out.println(my.getLabel());
        for(Tree t : children)
            t.dump();
    }

    public void removeDummy(Map<Pair<Tree,Tree>,String> edge){
        if(used.contains(this))
            return;
        used.add(this);


        // dummy node must have 0 or 1 children
        while(!children.isEmpty()){
            // if children contains dummy
            boolean hasDummy = false;
            for(Tree c : children)
                if(c.getMy().getDummy())
                    hasDummy = true;
            if(!hasDummy)
                break;

            Pair<Tree,Tree> del = null;
            for(Tree c1 : children){
                for(Tree c2 : c1.children){
                    if(c1.getMy().getDummy()){
                        del = new Pair<>(c1,c2);
                        final Pair<Tree,Tree> e1 = new Pair<>(this, c1);
                        final Pair<Tree,Tree> e2 = new Pair<>(c1, c2);
                        final Pair<Tree,Tree> rep = new Pair<>(this, c2);
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

        for(Tree t : children)
            t.removeDummy(edge);
    }




    private static Set<Tree> usedNodeInToGraph = new LinkedHashSet<>();
    public void print(Map<Pair<Tree,Tree>,String> edge){
        usedNodeInToGraph.clear();
        TestWriter.println(toGraph(edge));
    }
    public String toGraph(Map<Pair<Tree,Tree>,String> edge){
        if(usedNodeInToGraph.contains(this))
            return "";
        usedNodeInToGraph.add(this);

        StringBuilder sb = new StringBuilder();
        sb.append(my.toGraph());
        sb.append(System.lineSeparator());

        for(Tree c : children){
            sb.append(my.toGraphId() + " -> " + c.my.toGraphId());

            StringBuilder edgesb = new StringBuilder();
            edgesb.append(" [label=\"");
            Pair<Tree,Tree> p = new Pair<>(this,c);
            if(edge.containsKey(p)){
                edgesb.append(edge.get(p));
            }
            edgesb.append("\"];");
            sb.append(edgesb.toString());

            sb.append(System.lineSeparator());
        }

        for(Tree f : children)
            sb.append( f.toGraph(edge) );

        return sb.toString();
    }
}
