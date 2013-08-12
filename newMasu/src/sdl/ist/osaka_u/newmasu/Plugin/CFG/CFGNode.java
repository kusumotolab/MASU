package sdl.ist.osaka_u.newmasu.Plugin.CFG;

import com.sun.tools.javac.util.Pair;

import java.util.*;

public class CFGNode {
    private static Map<Pair<CFGNode,CFGNode>, String> edgeLabel = new HashMap<>();
    private static String trigger = null;
    private static CFGNode triggerNode1 = null, triggerNode2 = null;
    public void setTrigger(CFGNode n1, CFGNode n2, String str){
        trigger = str;
        triggerNode1 = n1;
        triggerNode2 = n2;
    }
    private static void addEdgeLabel(CFGNode node){
        if(triggerNode1==null && triggerNode2!=null)
            edgeLabel.put(Pair.of(node,triggerNode2),trigger);
        else if(triggerNode1!=null && triggerNode2==null)
            edgeLabel.put(Pair.of(triggerNode1, node),trigger);

        trigger = null;
        triggerNode1 = null;
        triggerNode2 = null;
    }



    private String label="";
    private String name="";
    private String shape="";

    private boolean dummy=false;

    private List<CFGNode> children = new ArrayList<>();

    public CFGNode addChild(CFGNode cn){
        CFGNode node = null;

        if(dummy){
            this.label = cn.label;
            this.name = cn.name;
            this.shape = cn.shape;
            this.dummy = false;
            children = new ArrayList<>();
            node = this;
        }
        else{
            children.add(cn);
            node = cn;
        }

        addEdgeLabel(node);
        return node;
    }

    public CFGNode(boolean dummy){
        this("dummy", "dummy", "box");
        this.dummy = true;
    }
    public CFGNode(String label, String name){
        this(label, name, "box");
    }
    public CFGNode(String label, String name, String shape){
        this.label = label.trim();
        this.name = name.trim();
        this.shape = shape;
    }

    private static Set<CFGNode> used;
    public void showChildren(){
        used = new HashSet<>();
        _showChildren();
    }
    private void _showChildren(){
        if(!used.contains(this)){
            used.add(this);
            TestWriter.println(name + " [label=\"" + sanitize(label) + "\", shape=\"" + shape + "\"];");

            for(CFGNode cn : children){
                String edge="";
                if(edgeLabel.containsKey(Pair.of(this, cn)))
                    edge = edgeLabel.get(Pair.of(this, cn));
                TestWriter.println(name + " -> " + cn.name + "[label=\"" + edge + "\"];");
                cn._showChildren();
            }

        }
    }

    private String sanitize(String str){
        str = str.trim();
        str = str.replaceAll("\\\"", "\\\\\\\"");
        return str;
    }

}
