package sdl.ist.osaka_u.newmasu.Finder;

import com.sun.tools.javac.util.Pair;
import org.eclipse.jdt.core.dom.*;
import java.util.*;

public class CFGSample extends ASTVisitor {

    private Integer _name = 0;
    private String getName(){
        return "NODE_" + _name.toString();
    }
    private String nextName(){
        _name++;
        return getName();
    }

    private static class CFGNode{
        private String label="";
        private String name="";
        private String shape="";

        private boolean dummy=false;

        private List<CFGNode> children = new ArrayList<>();

        public CFGNode addChild(CFGNode cn){
            CFGNode node = null;

            if(dummy){
                this.label = cn.label;
                this.name = cn.name;
                this.shape = cn.shape;
                this.dummy = false;
                children = new ArrayList<>();
                node = this;
            }
            else{
                children.add(cn);
                node = cn;
            }

            addEdgeLabel(node);
            return node;
        }

        public CFGNode(boolean dummy){
            this("dummy", "dummy", "box");
            this.dummy = true;
        }
        public CFGNode(String label, String name){
            this(label, name, "box");
        }
        public CFGNode(String label, String name, String shape){
            this.label = label.trim();
            this.name = name.trim();
            this.shape = shape;
        }

        private static Set<CFGNode> used;
        public void showChildren(){
            used = new HashSet<>();
            _showChildren();
        }
        private void _showChildren(){
            if(!used.contains(this)){
                used.add(this);
                TestWriter.println(name + " [label=\"" + sanitize(label) + "\", shape=\"" + shape + "\"];");

                for(CFGNode cn : children){
                    String edge="";
                    if(edgeLabel.containsKey(Pair.of(this, cn)))
                        edge = edgeLabel.get(Pair.of(this, cn));
                    TestWriter.println(name + " -> " + cn.name + "[label=\"" + edge + "\"];");
                    cn._showChildren();
                }

            }
        }

        private String sanitize(String str){
            str = str.trim();
            str = str.replaceAll("\\\"", "\\\\\\\"");
            return str;
        }
    }


    private static Map<Pair<CFGNode,CFGNode>, String> edgeLabel = new HashMap<>();
    private static String trigger = null;
    private static CFGNode triggerNode1 = null, triggerNode2 = null;
    public void setTrigger(CFGNode n1, CFGNode n2, String str){
        trigger = str;
        triggerNode1 = n1;
        triggerNode2 = n2;
    }
    private static void addEdgeLabel(CFGNode node){
        if(triggerNode1==null && triggerNode2!=null)
            edgeLabel.put(Pair.of(node,triggerNode2),trigger);
        else if(triggerNode1!=null && triggerNode2==null)
            edgeLabel.put(Pair.of(triggerNode1,node),trigger);

        trigger = null;
        triggerNode1 = null;
        triggerNode2 = null;
    }


    private CFGNode root = new CFGNode("root","R");
    private CFGNode nowNode = root;

    //////////////////////////////////////////////////////////////////////////////////

    public void def(ASTNode node){
        CFGNode cn = new CFGNode(node.toString(), nextName());
        nowNode = nowNode.addChild(cn);
    }

    @Override
    public boolean visit(IfStatement node){
        CFGNode cif = new CFGNode(node.getExpression().toString(), nextName(), "diamond");
        nowNode = nowNode.addChild(cif);

        CFGNode dummy = new CFGNode(true);

        nowNode = cif;
        setTrigger(cif, null, "then");
        DefaultProcessor.get(node.getThenStatement(), this).process(node.getThenStatement());
        nowNode.addChild(dummy);

        if(node.getElseStatement()!=null){
            nowNode = cif;
            setTrigger(cif, null, "else");
            DefaultProcessor.get(node.getElseStatement(), this).process(node.getElseStatement());
            nowNode.addChild(dummy);
        }

        nowNode = dummy;
        return false;
    }

    @Override
    public boolean visit(ForStatement node){
        CFGNode cond = new CFGNode(
                node.initializers().toString() + " " + node.getExpression().toString() + " " + node.updaters().toString(),
                nextName(), "diamond");
        nowNode = nowNode.addChild(cond);

        nowNode = cond;
        setTrigger(cond, null, "then");
        DefaultProcessor.get(node.getBody(), this).process(node.getBody());
        nowNode.addChild(cond);

        nowNode = cond;
        setTrigger(cond, null, "else");

        return false;
    }

    @Override
    public boolean visit(Block node){
        List<Statement> list = node.statements();
        for( Statement s : list)
            DefaultProcessor.get(s, this).process(s);
        return false;
    }

    @Override public boolean visit(CompilationUnit node){
        TestWriter.newFile();
        TestWriter.println("digraph CFG {");
        return true;
    }
    @Override public void endVisit(CompilationUnit node){
        TestWriter.println("}");
    }

    @Override
    public boolean visit(MethodDeclaration node){
        root = new CFGNode(node.getName().toString(),nextName(), "Msquare");
        nowNode = root;
        return true;
    }
    @Override
    public void endVisit(MethodDeclaration node){
        nowNode.addChild(new CFGNode("End " + node.getName().toString(), nextName(), "Msquare"));
        root.showChildren();
    }
}
