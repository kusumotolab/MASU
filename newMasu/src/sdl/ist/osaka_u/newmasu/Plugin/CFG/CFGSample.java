package sdl.ist.osaka_u.newmasu.Plugin.CFG;

import com.sun.tools.javac.util.Pair;
import org.eclipse.jdt.core.dom.*;
import sdl.ist.osaka_u.newmasu.data.BindingManager;

import java.util.*;

public class CFGSample extends ASTVisitor {

    protected CFGNode root = new CFGNode("root","R");
    protected CFGNode nowNode = root;

    public void def(ASTNode node){
        CFGNode cn = new CFGNode(node.toString(), NodeName.nextName());
        nowNode = nowNode.addChild(cn);
    }

    @Override
    public boolean visit(IfStatement node){
        CFGNode cif = new CFGNode(node.getExpression().toString(), NodeName.nextName(), "diamond");
        nowNode = nowNode.addChild(cif);

        CFGNode dummy = new CFGNode(true);

        nowNode = cif;
        cif.setTrigger(cif, null, "then");
        CFGSampleProcessor.get(node.getThenStatement(), this).process(node.getThenStatement());
        nowNode.addChild(dummy);

        if(node.getElseStatement()!=null){
            nowNode = cif;
            cif.setTrigger(cif, null, "else");
            CFGSampleProcessor.get(node.getElseStatement(), this).process(node.getElseStatement());
            nowNode.addChild(dummy);
        }
        else{
            cif.setTrigger(cif, null, "else");
            cif.addChild(dummy);
        }

        nowNode = dummy;

        return false;
    }

    @Override
    public boolean visit(ForStatement node){
        CFGNode cond = new CFGNode(
                node.initializers().toString() + " " + node.getExpression().toString() + " " + node.updaters().toString(),
                NodeName.nextName(), "diamond");
        nowNode = nowNode.addChild(cond);

        nowNode = cond;
        cond.setTrigger(cond, null, "then");
        CFGSampleProcessor.get(node.getBody(), this).process(node.getBody());
        nowNode.addChild(cond);

        nowNode = cond;
        cond.setTrigger(cond, null, "else");

        return false;
    }

    @Override
    public boolean visit(Block node){
        List<Statement> list = node.statements();
        for( Statement s : list)
            CFGSampleProcessor.get(s, this).process(s);
        return false;
    }

    @Override public boolean visit(CompilationUnit node){
        TestWriter.newFile(node.getPackage().getName().toString() + "." +
                BindingManager.getRel().getCalleeMap().get(node).getFileName() );
        TestWriter.println("digraph CFG {");
        return true;
    }
    @Override public void endVisit(CompilationUnit node){
        TestWriter.println("}");
    }

    @Override
    public boolean visit(MethodDeclaration node){
        String label = node.getReturnType2().toString() + " " + node.getName() + "(";
        for(Object t : node.parameters()){
            SingleVariableDeclaration s = (SingleVariableDeclaration)t;
            label += " " + s.getType().toString() + " " + s.getName();
        }
        label += ")";
        root = new CFGNode(label, NodeName.nextName(), "Msquare");
        nowNode = root;
        return true;
    }
    @Override
    public void endVisit(MethodDeclaration node){
        nowNode.addChild(new CFGNode("End " + node.getName().toString(), NodeName.nextName(), "Msquare"));
        root.showChildren();
    }
}
