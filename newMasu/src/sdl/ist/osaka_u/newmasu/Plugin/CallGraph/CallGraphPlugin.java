package sdl.ist.osaka_u.newmasu.Plugin.CallGraph;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import sdl.ist.osaka_u.newmasu.Plugin.MethodDependency.CalleeMethodsVisitor;
import sdl.ist.osaka_u.newmasu.Plugin.Plugin;
import sdl.ist.osaka_u.newmasu.Settings;
import sdl.ist.osaka_u.newmasu.data.BindingManager;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

public class CallGraphPlugin implements Plugin {

    private CallGraphNode root = new CallGraphNode();
    private CallGraphNode nowNode = root;

    @Override
    public void run() {

        root.bind = null;

        Map<IMethodBinding, ASTNode> methods = BindingManager.getAllMethod();
        for( Map.Entry<IMethodBinding, ASTNode> e: methods.entrySet() ){
            final IMethodBinding bind = e.getKey();
            final ASTNode node = e.getValue();

            CallGraphNode parent = nowNode;
            CallGraphNode child = new CallGraphNode();
            child.bind = bind;
            nowNode.children.add(child);
            nowNode = child;

            System.out.println("----------------------------------");
            System.out.println(
                    bind.getDeclaringClass().getName() + "  " +
                            bind.getName()
            );

            CallGraphVisitor visitor = new CallGraphVisitor();
            node.accept(visitor);
            Set<IMethodBinding> caleeBindings = visitor.getCalleeMethods();

            for( IMethodBinding b : caleeBindings){
                CallGraphNode c = new CallGraphNode();
                c.bind = b;
                nowNode.children.add(c);
                System.out.println(" " + b.getName());
            }

            nowNode = parent;

/*            System.out.println(
                    NodeFinder.<TypeDeclaration>getOneParentNode(node)
            );
            */
        }

        ShowCallGraph();
    }


    class CallGraphNode{
        public IMethodBinding bind = null;
        public List<CallGraphNode> children = new ArrayList<>();
    }
    private void ShowCallGraph(){
        File file = new File("sample/cg.dot");
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        pw.println("digraph callgraph {");
        _ShowCallGraph(root);
        pw.println("}");
        pw.close();
    }
    private PrintWriter pw = null;
    private Set<CallGraphNode> used = new HashSet<>();
    private void _ShowCallGraph(CallGraphNode node){
        if(!used.contains(node)){
            used.add(node);
            for( CallGraphNode c : node.children ){
                pw.println( " " + c.bind.getKey().hashCode()  + " [label=\"" + c.bind.getName() + "\"]");
                if( node.bind != null){
                    pw.println( "  " + node.bind.getKey() .hashCode()+ " -> " + c.bind.getKey().hashCode() );
                }
                _ShowCallGraph(c);
            }
        }
    }
}
