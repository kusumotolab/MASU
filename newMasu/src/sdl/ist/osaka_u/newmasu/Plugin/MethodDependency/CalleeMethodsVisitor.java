package sdl.ist.osaka_u.newmasu.Plugin.MethodDependency;

import org.eclipse.jdt.core.dom.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CalleeMethodsVisitor extends ASTVisitor {

    public Map<IMethodBinding, IVariableBinding> variables = new HashMap<>();

    private IMethodBinding nowMethod = null;

    @Override
    public boolean visit(MethodDeclaration node){
//        System.out.println("method --" + node.getName());
        nowMethod = node.resolveBinding();
        return true;
    }

    @Override
    public boolean visit(Assignment node){
//        System.out.println(node.toString());
        Expression exp = node.getLeftHandSide();
        AssigneeVisitor vis = new AssigneeVisitor();
        exp.accept(vis);
        for( IVariableBinding bind: vis.name )
            variables.put(nowMethod, bind);
        return true;
    }
}
