package sdl.ist.osaka_u.newmasu.Plugin.graph;

import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

public class VariableDecVisitor extends ASTVisitor {
    public List<IVariableBinding> decBindings = new ArrayList<>();
    public List<IVariableBinding> useBindings = new ArrayList<>();

    @Override
    public boolean visit(SimpleName node){
        IBinding b = node.resolveBinding();
        if( b != null && b.getKind() == IBinding.VARIABLE )
            if( node.isDeclaration() )
                decBindings.add((IVariableBinding)b);
            else
                useBindings.add((IVariableBinding)b);
        return true;
    }

    // ignore anonymous class
    @Override
    public boolean visit(AnonymousClassDeclaration node){
        return false;
    }
}
