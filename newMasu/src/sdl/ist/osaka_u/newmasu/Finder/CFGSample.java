package sdl.ist.osaka_u.newmasu.Finder;

import org.eclipse.jdt.core.dom.*;
import java.util.*;

public class CFGSample extends ASTVisitor {

    private Map<ASTNode, String> nameMap = new HashMap<>();
    private Integer _name = 0;
    private String getName(){
        return "NODE_" + _name.toString();
    }
    private String nextName(){
        _name++;
        return getName();
    }
    private void nameMapping(ASTNode node){
        nameMap.put(node, nextName());
    }

    @Override public boolean visit(CompilationUnit node){
//        TestWriter.newFile();
//        TestWriter.println("digraph CFG {");
        return true;
    }
    @Override public void endVisit(CompilationUnit node){
//        TestWriter.println("}");

        nowNode.addChild(new CFGNode("End", "END", "box"));

        TestWriter.newFile();
        TestWriter.println("digraph CFG {");
        root.showChildren();
        TestWriter.println("}");
    }

    private static class CFGNode{
        private String label="";
        private String name="";
        private String shape="";
        private String edge="";

        private boolean dummy=false;

        private List<CFGNode> children = new ArrayList<>();

        public CFGNode addChild(CFGNode cn){
            if(dummy){
                this.label = cn.label;
                this.name = cn.name;
                this.shape = cn.shape;
                this.edge = cn.edge;
                this.dummy = false;
                children = new ArrayList<>();
                return this;
            }
            else{
                children.add(cn);
                return cn;
            }
        }

        public CFGNode(boolean dummy){
            this("dummy", "dummy", "box");
            this.dummy = true;
        }
        public CFGNode(String label, String name){
            this(label, name, "box", "");
        }
        public CFGNode(String label, String name, String shape){
            this(label, name, shape, "");
        }
        public CFGNode(String label, String name, String shape, String edge){
            this.label = label.trim();
            this.name = name.trim();
            this.shape = shape;
            this.edge = edge;
        }

        private static Set<CFGNode> used;
        public void showChildren(){
            used = new HashSet<>();
            _showChildren();
        }

        private void _showChildren(){
            if(!used.contains(this)){
                used.add(this);
                TestWriter.println(name + " [label=\"" + label + "\", shape=\"" + shape + "\"];");

                for(CFGNode cn : children){
                    TestWriter.println(name + " -> " + cn.name + "[label=\"" + edge + "\"];");
                    System.out.println(label + " -> " + cn.label);
                    cn._showChildren();
                }

            }
        }
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

//        CFGNode cthen = new CFGNode(node.getThenStatement().toString(), nextName());
        nowNode = cif;
        DefaultProcessor.get(node.getThenStatement(), this).process(node.getThenStatement());
        nowNode.addChild(dummy);

        if(node.getElseStatement()!=null){
//            CFGNode celse = new CFGNode(node.getElseStatement().toString(), nextName());
            nowNode = cif;
            DefaultProcessor.get(node.getElseStatement(), this).process(node.getElseStatement());
            nowNode.addChild(dummy);
        }

        nowNode = dummy;
        return false;
    }

    @Override
    public boolean visit(ForStatement node){
        CFGNode cond = new CFGNode(node.getExpression().toString(), nextName(), "ellipse");
        nowNode = nowNode.addChild(cond);

        return false;
    }

    @Override
    public boolean visit(Block node){
        List<Statement> list = node.statements();
        for( Statement s : list)
            DefaultProcessor.get(s, this).process(s);
        return false;
    }

    /*
    private String indent = "";
    private void addIndent(){
        indent += "  ";
    }
    private void delIndent(){
        if(indent.length() > 2)
            indent = indent.substring(0,indent.length()-2);
    }
    */
}
