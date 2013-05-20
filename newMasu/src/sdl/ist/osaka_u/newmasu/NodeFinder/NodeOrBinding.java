package sdl.ist.osaka_u.newmasu.NodeFinder;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IBinding;

public class NodeOrBinding {
    private boolean isNode = false;
    public boolean isNode(){ return isNode; }
    private boolean isBinding = false;
    public boolean isBinding(){ return isBinding; }

    ASTNode node = null;
    public ASTNode getNode(){
        if(!isNode){
            System.out.println("No Node in NodeOrBinding instance.");
            return null;
        }
        return node;
    }
    IBinding binding = null;
    public IBinding getBinding(){
        if(!isBinding){
            System.out.println("No Binding in NodeOrBinding instance.");
            return null;
        }
        return binding;
    }

    public NodeOrBinding(ASTNode node){
        isNode = true; isBinding = false;
        this.node = node;
    }
    public  NodeOrBinding(IBinding binding){
        isBinding = true; isNode = false;
        this.binding = binding;
    }
}
