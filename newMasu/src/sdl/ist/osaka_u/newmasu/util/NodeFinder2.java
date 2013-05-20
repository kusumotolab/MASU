package sdl.ist.osaka_u.newmasu.util;

import com.sun.tools.javac.util.Pair;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.ArrayList;
import java.util.List;

public class NodeFinder2 {

    public interface Delegate<T, R> {
        public R invoke(T arg);
    }

    public static Delegate<ASTNode,ASTNode> simpleParent(){
        return new Delegate<ASTNode, ASTNode>() {
            @Override
            public ASTNode invoke(ASTNode arg) {
                return arg.getParent();
            }
        };
    }

    public static Delegate<Pair<ASTNode,Integer>, Boolean> simpleJudge(){
        return new Delegate<Pair<ASTNode, Integer>, Boolean>() {
            @Override
            public Boolean invoke(Pair<ASTNode, Integer> arg) {
                if( arg.fst.getNodeType() == arg.snd )
                    return true;
                else
                    return false;
            }
        };
    }


    public static <T extends ASTNode> List<T> get(ASTNode node, int target,
                            Delegate<ASTNode,ASTNode> getParent,
                            Delegate<Pair<ASTNode,Integer>, Boolean> judge){

        List<T> res = new ArrayList<>();

        ASTNode parent = getParent.invoke(node);
        while(parent!=null){
            if( judge.invoke(new Pair(parent,target)) )
                res.add((T)parent);
            parent = getParent.invoke(parent);
        }

        return res;
    }
}
