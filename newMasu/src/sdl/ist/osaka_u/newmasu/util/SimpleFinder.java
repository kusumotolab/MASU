package sdl.ist.osaka_u.newmasu.util;

import org.eclipse.jdt.core.dom.ASTNode;

import java.util.ArrayList;
import java.util.List;

public class SimpleFinder {

    public enum Target{
        Statement,
        Declaration,

    }



    /**
     * nodeより上層にあるtargetを列挙
     */
    public static <T extends ASTNode> List<T> getUpper(ASTNode node, List<Integer> target){
        List<T> res = new ArrayList<>();
        for(ASTNode parent = node.getParent(); parent!=null; parent = parent.getParent()){
            for(int t : target)
                if(parent.getNodeType() == t)
                    res.add((T)parent);
        }
        return res;
    }

    /**
     * nodeより下にあるtargetを列挙
     * targetはenum型
     */
    public static <T extends ASTNode> List<T> getLower(ASTNode node, Target target){
        List<T> res = new ArrayList<>();



        return res;
    }





    public boolean isTarget(ASTNode node, Target t){
        if(t.equals(Target.Statement)){
            switch (node.getNodeType()) {
                case ASTNode.ASSERT_STATEMENT:
                case ASTNode.BREAK_STATEMENT:
                case ASTNode.CONSTRUCTOR_INVOCATION:
                case ASTNode.CONTINUE_STATEMENT:
                case ASTNode.DO_STATEMENT:
                case ASTNode.EMPTY_STATEMENT:
                case ASTNode.ENHANCED_FOR_STATEMENT:
                case ASTNode.EXPRESSION_STATEMENT:
                case ASTNode.FOR_STATEMENT:
                case ASTNode.IF_STATEMENT:
                case ASTNode.LABELED_STATEMENT:
                case ASTNode.RETURN_STATEMENT:
                case ASTNode.SUPER_CONSTRUCTOR_INVOCATION:
                case ASTNode.SWITCH_CASE:
                case ASTNode.SWITCH_STATEMENT:
                case ASTNode.SYNCHRONIZED_STATEMENT:
                case ASTNode.THROW_STATEMENT:
                case ASTNode.TRY_STATEMENT:
                case ASTNode.TYPE_DECLARATION_STATEMENT:
                case ASTNode.VARIABLE_DECLARATION_STATEMENT:
                case ASTNode.WHILE_STATEMENT:
                    return true;
                default:
                    return false;
            }
        }
        else if(t.equals(Target.Declaration)){
            switch (node.getNodeType()){
                case ASTNode.METHOD_DECLARATION:
                case ASTNode.FIELD_DECLARATION:
                case ASTNode.INITIALIZER:
                case ASTNode.ENUM_CONSTANT_DECLARATION:

                case ASTNode.ENUM_DECLARATION:
                case ASTNode.TYPE_DECLARATION:

                case ASTNode.PACKAGE_DECLARATION:
                case ASTNode.IMPORT_DECLARATION:
                    return true;
                default:
                    return false;
            }
        }


        else
            return false;
    }

}

