package sdl.ist.osaka_u.newmasu.Plugin.CallGraph;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.eclipse.jdt.core.BindingKey;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IMethodBinding;

import sdl.ist.osaka_u.newmasu.Plugin.Plugin;
import sdl.ist.osaka_u.newmasu.data.BindingManager;

public class CallGraphPlugin implements Plugin {

    private CallGraphNode root = new CallGraphNode();
    private CallGraphNode nowNode = root;

    private Map<IMethodBinding, Integer> inCount = new HashMap<>();
    private Map<IMethodBinding, Integer> outCount = new HashMap<>();

    private Path outputPath = Paths.get("/dev/null");
    private Path ioCountOutputPath = Paths.get("/dev/null");

    public CallGraphPlugin(Path op){
        outputPath = op.resolve("cg.dot");
        ioCountOutputPath = op.resolve("inOut.csv");
    }
    public CallGraphPlugin(){}

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
        }

        ShowCallGraph();

        try {
            BufferedWriter ioCountWriter = Files.newBufferedWriter(ioCountOutputPath, Charset.defaultCharset());
            for(Map.Entry<IMethodBinding, Integer> entry: inCount.entrySet()){
                System.out.println(entry.getKey().getKey());
                ioCountWriter.write("in:" + entry.getKey().getKey() + ":" + entry.getValue() + System.lineSeparator());
            }
            for(Map.Entry<IMethodBinding, Integer> entry: outCount.entrySet()){
                ioCountWriter.write("out:" + entry.getKey().getKey() + ":" + entry.getValue() + System.lineSeparator());
            }
            ioCountWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class CallGraphNode{
        public IMethodBinding bind = null;
        public List<CallGraphNode> children = new ArrayList<>();
    }
    private void ShowCallGraph(){
        File file = outputPath.toFile();
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
                if(node.bind!=null){
                    if(!inCount.containsKey(node.bind))
                        inCount.put(node.bind, 0);
                    if(!outCount.containsKey(node.bind))
                        outCount.put(node.bind, 0);
                }
                if(c.bind!=null){
                    if(!inCount.containsKey(c.bind))
                        inCount.put(c.bind, 0);
                    if(!outCount.containsKey(c.bind))
                        outCount.put(c.bind, 0);
                }

                pw.println( " " + c.bind.getKey().hashCode()  +
                        " [label=\"" + c.bind.getDeclaringClass().getName() + "." + c.bind.getName() + "\"]");
                if( node.bind != null){
                    pw.println( "  " + node.bind.getKey().hashCode()+ " -> " + c.bind.getKey().hashCode() );

                    if( inCount.containsKey(node.bind) ){
                        Integer inIt = inCount.get(node.bind);
                        inCount.put(node.bind, ++inIt);
                    }
                    else{
                        inCount.put(node.bind, 1);
                    }

                    if( outCount.containsKey(c.bind) ){
                        Integer outIt = outCount.get(c.bind);
                        outCount.put(c.bind, ++outIt);
                    }
                    else{
                        outCount.put(c.bind, 1);
                    }
                }
                _ShowCallGraph(c);
            }
        }
    }
}
