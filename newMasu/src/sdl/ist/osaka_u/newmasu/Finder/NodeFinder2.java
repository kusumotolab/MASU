package sdl.ist.osaka_u.newmasu.Finder;

import com.sun.tools.javac.util.Pair;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

public class NodeFinder2 {

    // ASTNode wrapper
    public static <T extends ASTNode> List<T> get(ASTNode node, int target,
                                                  Delegate<NodeOrBinding,List<NodeOrBinding>> getNext,
                                                  Delegate<Pair<NodeOrBinding,Integer>, Boolean> judge){
        return get(new NodeOrBinding(node), target, getNext, judge);
    }

    // IBinding wrapper
    public static <T extends IBinding> List<T> get(IBinding node, int target,
                                                  Delegate<NodeOrBinding,List<NodeOrBinding>> getNext,
                                                  Delegate<Pair<NodeOrBinding,Integer>, Boolean> judge){
        return get(new NodeOrBinding(node), target, getNext, judge);
    }


    private static <T> void upper( List<NodeOrBinding> parents, int target,
                               Delegate<NodeOrBinding,List<NodeOrBinding>> getNext,
                               Delegate<Pair<NodeOrBinding,Integer>, Boolean> judge,
                               List<T> res){

        for( NodeOrBinding p : parents ){
            while( p!=null && (
                    (p.isValidNode() || p.isValidBinding() )
            ) ){
                if( judge.invoke(new Pair(p,target)) ){
                    if(p.isValidNode()) res.add( (T)p.getNode() );
                    else                res.add((T)p.getBinding());
                }
                upper(getNext.invoke(p), target, getNext, judge, res);
//                parents = getNext.invoke(p);
            }
        }

    }

    public static <T> List<T> get(NodeOrBinding node, int target,
                            Delegate<NodeOrBinding,List<NodeOrBinding>> getNext,
                            Delegate<Pair<NodeOrBinding,Integer>, Boolean> judge){

        List<T> res = new ArrayList<>();

        List<NodeOrBinding> parents = getNext.invoke(node);
        upper(parents,target,getNext,judge, res);

/*        for( NodeOrBinding p : parents ){
            while( p!=null && (
                    (p.isValidNode() || p.isValidBinding() )
            ) ){
                if( judge.invoke(new Pair(p,target)) ){
                    if(p.isValidNode()) res.add( (T)p.getNode() );
                    else                res.add((T)p.getBinding());
                }
                parents = getNext.invoke(p);
            }
        }
*/
        return res;
    }
}
