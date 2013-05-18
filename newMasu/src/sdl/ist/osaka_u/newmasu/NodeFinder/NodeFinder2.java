package sdl.ist.osaka_u.newmasu.NodeFinder;

import com.sun.tools.javac.util.Pair;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.ArrayList;
import java.util.List;

public class NodeFinder2 {

    public static Delegate<NodeOrBinding,NodeOrBinding> simpleParent(){
        return new Delegate<NodeOrBinding, NodeOrBinding>() {
            @Override
            public NodeOrBinding invoke(NodeOrBinding arg) {
                if(arg.isNode())
                    return new NodeOrBinding((arg.getNode().getParent()));
                else
                    return null;
            }
        };
    }

    public static Delegate<Pair<NodeOrBinding,Integer>, Boolean> simpleJudge(){
        return new Delegate<Pair<NodeOrBinding, Integer>, Boolean>() {
            @Override
            public Boolean invoke(Pair<NodeOrBinding, Integer> arg) {
                if( arg.fst.isNode() && arg.fst.getNode().getNodeType() == arg.snd )
                    return true;
                else
                    return false;
            }
        };
    }


    public static Delegate<NodeOrBinding,NodeOrBinding> getInherited(){
        return new Delegate<NodeOrBinding, NodeOrBinding>() {
            @Override
            public NodeOrBinding invoke(NodeOrBinding arg) {
                if(arg.isNode()){
                    if( arg.getNode().getNodeType() == ASTNode.TYPE_DECLARATION ){
                        TypeDeclaration td = (TypeDeclaration)arg.getNode();
                        return new NodeOrBinding(td.resolveBinding().getSuperclass());
                    }
                    else{
                        return new NodeOrBinding(arg.getNode().getParent());
                    }
                }
                else{
                    if( arg.getBinding().getKind() == IBinding.TYPE ){
                        ITypeBinding bind = (ITypeBinding)arg.getBinding();
                        return new NodeOrBinding(bind.getSuperclass());
                    }
                    else
                        return null;
                }
            }
        };
    }

    public static Delegate<Pair<NodeOrBinding,Integer>, Boolean> bindingNullJudge(){
        return new Delegate<Pair<NodeOrBinding, Integer>, Boolean>() {
            @Override
            public Boolean invoke(Pair<NodeOrBinding, Integer> arg) {
                if( arg.fst.isBinding() && arg.fst.getBinding() != null )
                    return true;
                else
                    return false;
            }
        };
    }


    // ASTNode wrapper
    public static <T extends ASTNode> List<T> get(ASTNode node, int target,
                                                  Delegate<NodeOrBinding,NodeOrBinding> getNext,
                                                  Delegate<Pair<NodeOrBinding,Integer>, Boolean> judge){
        return get(new NodeOrBinding(node), target, getNext, judge);
    }
/*
    // IBinding wrapper
    public static <T extends IBinding> List<T> get(IBinding node, int target,
                                                  Delegate<NodeOrBinding,NodeOrBinding> getNext,
                                                  Delegate<Pair<NodeOrBinding,Integer>, Boolean> judge){
        return get(new NodeOrBinding(node), target, getNext, judge);
    }
*/

    public static <T extends ASTNode> List<T> get(NodeOrBinding node, int target,
                            Delegate<NodeOrBinding,NodeOrBinding> getNext,
                            Delegate<Pair<NodeOrBinding,Integer>, Boolean> judge){

        List<T> res = new ArrayList<>();

        NodeOrBinding parent = getNext.invoke(node);
        while( parent!=null && (
                (parent.isNode() && parent.getNode()!=null) ||
                (parent.isBinding()) && parent.getBinding()!=null) ){
            if( judge.invoke(new Pair(parent,target)) ){
                if(parent.isNode()) res.add( (T)parent.getNode() );
                else                res.add((T)parent.getBinding());
            }
            parent = getNext.invoke(parent);
        }

        return res;
    }
}
