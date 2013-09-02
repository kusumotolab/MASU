package sdl.ist.osaka_u.newmasu.Plugin.MethodDependency;

import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import sdl.ist.osaka_u.newmasu.Plugin.Plugin;
import sdl.ist.osaka_u.newmasu.data.BindingManager;

public class MethodDependencyPlugin implements Plugin {

    @Override
    public void run() {

        Map<IMethodBinding, ASTNode> methods = BindingManager.getAllMethod();
        for( Map.Entry<IMethodBinding, ASTNode> e: methods.entrySet() ){
            CalleeMethodsVisitor ce = new CalleeMethodsVisitor();
            e.getValue().accept(ce);
            for( Map.Entry<IMethodBinding, IVariableBinding> mb : ce.variables.entrySet() ){
                System.out.println( mb.getKey() + "  "  + mb.getValue());
            }
        }

        /*
        Map<IVariableBinding, ASTNode> variables = BindingManager.getAllVariable();
        for( Map.Entry<IVariableBinding, ASTNode> e: variables.entrySet() ){
            System.out.println(e.getKey().toString());
            if(e.getKey().isField())
                System.out.println("   field   ");
        }
        */

    }
}
