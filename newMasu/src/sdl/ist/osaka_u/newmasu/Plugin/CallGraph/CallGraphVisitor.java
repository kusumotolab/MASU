package sdl.ist.osaka_u.newmasu.Plugin.CallGraph;

import org.eclipse.jdt.core.dom.*;

import java.util.HashSet;
import java.util.Set;

public class CallGraphVisitor extends ASTVisitor {
    private Set<IMethodBinding> methods = new HashSet<>();
    public Set<IMethodBinding> getCalleeMethods(){
        return methods;
    }

    @Override
    public boolean visit(MethodInvocation node){
        IMethodBinding bind = node.resolveMethodBinding();
        if(bind!=null)
            methods.add(bind);
        return true;
    }
}
