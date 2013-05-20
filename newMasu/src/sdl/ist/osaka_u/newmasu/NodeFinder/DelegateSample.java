package sdl.ist.osaka_u.newmasu.NodeFinder;

import com.sun.tools.javac.util.Pair;
import org.eclipse.jdt.core.dom.ASTNode;

public class DelegateSample {

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
}
