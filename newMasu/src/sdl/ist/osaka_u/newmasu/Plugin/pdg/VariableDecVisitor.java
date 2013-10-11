package sdl.ist.osaka_u.newmasu.Plugin.pdg;

import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

public class VariableDecVisitor extends ASTVisitor {
    public List<IVariableBinding> decBindings = new ArrayList<>();
    public List<IVariableBinding> useBindings = new ArrayList<>();
/*
    @Override
    public boolean visit(Assignment node){
        AssignSimpleName visit = new AssignSimpleName();
        node.getLeftHandSide().accept(visit);
        decBindings.addAll(visit.dec);
        node.getRightHandSide().accept(this);
        return false;
    }
    private class AssignSimpleName extends ASTVisitor {
        public List<IVariableBinding> dec = new ArrayList<>();
        @Override
        public boolean visit(SimpleName node){
            IBinding b = node.resolveBinding();
            if( b != null && b.getKind() == IBinding.VARIABLE )
                dec.add((IVariableBinding)b);
            return true;
        }
    }
*/
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


