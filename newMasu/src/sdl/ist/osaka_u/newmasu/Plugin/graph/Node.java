package sdl.ist.osaka_u.newmasu.Plugin.graph;

public class Node {

    private String label = "";
    private Boolean isDummy = false;
    private String shape = "ellipse";
    public void copy(Node node){
        label = node.label;
        isDummy = node.isDummy;
        shape = node.shape;
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
    public Node(){
    }

    public String toGraph(){
        return toGraphId() + " [label=\"" + toGraphLabel() + "\", shape=\"" + shape + "\"];";
    }

    public String toGraphLabel(){
        return label.trim();
    }
    public String toGraphId(){
        return  Integer.toString(hashCode());
    }
}