package sdl.ist.osaka_u.newmasu.Plugin.MethodDependency;

import org.eclipse.jdt.core.dom.*;
import sdl.ist.osaka_u.newmasu.Plugin.Plugin;
import sdl.ist.osaka_u.newmasu.data.BindingManager;
import sdl.ist.osaka_u.newmasu.data.MethodDeclarationInfo;
import sdl.ist.osaka_u.newmasu.util.NodeFinder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
