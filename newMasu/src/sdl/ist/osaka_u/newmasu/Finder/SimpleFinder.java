package sdl.ist.osaka_u.newmasu.Finder;

import org.eclipse.jdt.core.dom.ASTNode;

import java.util.ArrayList;
import java.util.List;

public class SimpleFinder {

    /**
     * nodeより上層にあるtargetを列挙
     * @param node
     * @return
     */
    public static <T extends ASTNode> List<T> getUpper(ASTNode node, int target){
        List<T> res = new ArrayList<>();
        for(ASTNode parent = node.getParent(); parent!=null; parent = parent.getParent()){
            if(parent.getNodeType() == target)
                res.add((T)parent);
        }
        return res;
    }


}
