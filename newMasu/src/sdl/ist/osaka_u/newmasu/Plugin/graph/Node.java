package sdl.ist.osaka_u.newmasu.Plugin.graph;

public class Node {
    private String label = "";
//    private List<Node> children = new ArrayList<>();

    public void copy(Node node){
        label = node.label;
//        children = node.children;
    }

    /*
        public List<Node> getChildren() {
            return children;
        }
    */
    public String getLabel() {
        return label;
    }

    public void setLabel(String str){ label = str; }

    public Node(String name){label=name;}
    public Node(){}

    public String toGraph(){
        return toGraphId() + " [label=\"" + toGraphLabel() + "\"];";
    }

    public String toGraphLabel(){
        return label.trim();
    }
    public String toGraphId(){
        return Integer.toString(this.hashCode());
    }
}