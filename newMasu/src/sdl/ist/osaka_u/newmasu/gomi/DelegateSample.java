package sdl.ist.osaka_u.newmasu.gomi;

import com.sun.tools.javac.util.Pair;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

public class DelegateSample {

    public static Delegate<NodeOrBinding,List<NodeOrBinding>> simpleParent(){
        return new Delegate<NodeOrBinding, List<NodeOrBinding>>() {
            @Override
            public List<NodeOrBinding> invoke(NodeOrBinding arg) {
                if(arg.isValidNode()){
                    NodeOrBinding nd = new NodeOrBinding(arg.getNode().getParent());
                    ArrayList<NodeOrBinding> list = new ArrayList<>();
                    list.add(nd);
                    return list;
                }
                else
                    return null;
            }
        };
    }

    public static Delegate<Pair<NodeOrBinding,Integer>, Boolean> simpleJudge(){
        return new Delegate<Pair<NodeOrBinding, Integer>, Boolean>() {
            @Override
            public Boolean invoke(Pair<NodeOrBinding, Integer> arg) {
                if( arg.fst.isValidNode() && arg.fst.getNode().getNodeType() == arg.snd )
                    return true;
                else
                    return false;
            }
        };
    }


    public static Delegate<NodeOrBinding,List<NodeOrBinding>> getInherited(){
        return new Delegate<NodeOrBinding, List<NodeOrBinding>>() {
            @Override
            public List<NodeOrBinding> invoke(NodeOrBinding arg) {
                List<NodeOrBinding> list = new ArrayList<>();

                if(arg.isValidNode()){
                    if( arg.getNode().getNodeType() == ASTNode.TYPE_DECLARATION ){
                        TypeDeclaration td = (TypeDeclaration)arg.getNode();
                        ITypeBinding bind = td.resolveBinding();
                        list.add(new NodeOrBinding(bind.getSuperclass()));
                        for(ITypeBinding b : bind.getInterfaces())
                            list.add(new NodeOrBinding(b));
                        return list;
                    }
                    else{
                        list.add(new NodeOrBinding(arg.getNode().getParent()));
                        return  list;
                    }
                }
                else if(arg.isValidBinding()){
                    if( arg.getBinding().getKind() == IBinding.TYPE ){
                        ITypeBinding bind = (ITypeBinding)arg.getBinding();
                        list.add(new NodeOrBinding(bind.getSuperclass()));
                        return list;
                    }
                    else
                        return null;
                }
                else
                    return null;
            }
        };
    }

    public static Delegate<Pair<NodeOrBinding,Integer>, Boolean> bindingNullJudge(){
        return new Delegate<Pair<NodeOrBinding, Integer>, Boolean>() {
            @Override
            public Boolean invoke(Pair<NodeOrBinding, Integer> arg) {
                if( arg.fst.isValidBinding() && arg.fst.getBinding() != null )
                    return true;
                else
                    return false;
            }
        };
    }
}
