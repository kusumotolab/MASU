package sdl.ist.osaka_u.newmasu.Plugin.graph;

import org.eclipse.jdt.core.dom.*;
import sdl.ist.osaka_u.newmasu.Plugin.CFG.CFGNode;
import sdl.ist.osaka_u.newmasu.Plugin.CFG.CFGSampleProcessor;
import sdl.ist.osaka_u.newmasu.Plugin.CFG.NodeName;
import sdl.ist.osaka_u.newmasu.Plugin.CFG.TestWriter;
import sdl.ist.osaka_u.newmasu.data.BindingManager;

import java.util.List;

public class GraphTestVisitor extends ASTVisitor{

    //protected CFGNode root = new CFGNode("root","R");
    //protected CFGNode nowNode = root;

    private Tree mainBranch = new Tree(new Node("S"), new Node("E"));
    private Tree nowBranch = mainBranch;

    public void def(ASTNode node){
        //CFGNode cn = new CFGNode(node.toString(), NodeName.nextName());
        //nowNode = nowNode.addChild(cn);
        final Node n = new Node(node.toString());
        nowBranch.insert(n);
    }

    @Override
    public boolean visit(IfStatement node){

        final ConditionNode condNode = new ConditionNode();
        condNode.setLabel(node.getExpression().toString());
        nowBranch.insert(condNode);

        final Tree tmpBranch = nowBranch;

        final Tree thenBranch = nowBranch.newBranch();
        nowBranch = thenBranch;
        GraphTestProcessor.get(node.getThenStatement(), this).process(node.getThenStatement());
        condNode.thenTree = thenBranch;
        nowBranch = tmpBranch;

        if( node.getElseStatement() != null ){
            final Tree elseBranch = nowBranch.newBranch();
            nowBranch = elseBranch;
            GraphTestProcessor.get(node.getElseStatement(), this).process(node.getElseStatement());
            condNode.elseTree = elseBranch;
            nowBranch = tmpBranch;
        }
        /*
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
*/
        return false;
    }

    @Override
    public boolean visit(ForStatement node){
        /*
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
*/
        return false;
    }

    @Override
    public boolean visit(Block node){
        List<Statement> list = node.statements();
        for( Statement s : list)
            GraphTestProcessor.get(s, this).process(s);
        return false;
    }



    @Override public boolean visit(CompilationUnit node){
        //mainBranch = new Tree(new Node("S"), new Node("E"));
        //nowBranch = mainBranch;
        /*
        TestWriter.newFile(node.getPackage().getName().toString() + "." +
                BindingManager.getRel().getCalleeMap().get(node).getFileName() );
        TestWriter.println("digraph CFG {");
        */
        return true;

    }

    @Override public void endVisit(CompilationUnit node){
        //TestWriter.println("}");
        //mainBranch.print();
    }

    @Override
    public boolean visit(MethodDeclaration node){


        String label = node.getReturnType2().toString() + " " + node.getName() + "(";
        for(Object t : node.parameters()){
            SingleVariableDeclaration s = (SingleVariableDeclaration)t;
            label += " " + s.getType().toString() + " " + s.getName();
        }
        label += ")";
        /*
        root = new CFGNode(label, NodeName.nextName(), "Msquare");
        nowNode = root;
        */

        mainBranch = new Tree(new Node(label), new Node("End " + label));
        nowBranch = mainBranch;
        return true;
    }
    @Override
    public void endVisit(MethodDeclaration node){

        /*
        nowNode.addChild(new CFGNode("End " + node.getName().toString(), NodeName.nextName(), "Msquare"));
        root.showChildren();
        */
        mainBranch.print();
    }


}
