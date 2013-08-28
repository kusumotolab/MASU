package sdl.ist.osaka_u.newmasu.Plugin.MethodDependency;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.SimpleName;

import java.util.HashSet;
import java.util.Set;

public class AssigneeVisitor extends ASTVisitor {

    public Set<IVariableBinding> name = new HashSet<>();

    @Override
    public boolean visit(SimpleName node){
        IVariableBinding bind = (IVariableBinding) node.resolveBinding();
        if(bind.isField())
            name.add(bind);
        return false;
    }
}
