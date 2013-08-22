package sdl.ist.osaka_u.newmasu.Plugin.graph;

import org.eclipse.jdt.core.dom.*;
import sdl.ist.osaka_u.newmasu.Plugin.CFG.TestWriter;

import java.util.*;

public class GraphTestVisitor extends ASTVisitor{

    private Branch nowBranch = null;
    private Set<Branch> tree = new LinkedHashSet<>();
    private Set<String> edge = new LinkedHashSet<>();

    public void def(ASTNode node){
        final Node n = new Node(node.toString(),false,"ellipse");
        nowBranch.insert(n);
    }

    @Override
    public boolean visit(IfStatement node){

        final Node condNode = new Node(node.getExpression().toString(), false, "diamond");
        nowBranch.insert(condNode);

        final Node dummy = new Node("dummy", true, "ellipse");
        final Branch previousBranch = nowBranch;
        tree.add(previousBranch);

        final Branch thenBranch = previousBranch.newBranch();
        tree.add(thenBranch);
        nowBranch = thenBranch;
        GraphTestProcessor.get(node.getThenStatement(), this).process(node.getThenStatement());
        nowBranch.insert(dummy);

        final Branch elseBranch = previousBranch.newBranch();
        tree.add(elseBranch);
        nowBranch = elseBranch;
        if( node.getElseStatement() != null )
            GraphTestProcessor.get(node.getElseStatement(), this).process(node.getElseStatement());
        nowBranch.insert(dummy);

        nowBranch = new Branch();
        tree.add(nowBranch);
        nowBranch.insert(dummy);

        return false;
    }

    @Override
    public boolean visit(ForStatement node){
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
        TestWriter.newFile();
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

        tree.clear();
        nowBranch = new Branch();
        tree.add(nowBranch);
        nowBranch.insert( new Node("Start " + label,false, "rect") );

        return true;
    }
    @Override
    public void endVisit(MethodDeclaration node){
        String label = node.getReturnType2().toString() + " " + node.getName() + "(";
        for(Object t : node.parameters()){
            SingleVariableDeclaration s = (SingleVariableDeclaration)t;
            label += " " + s.getType().toString() + " " + s.getName();
        }
        label += ")";
        nowBranch.insert( new Node("End " + label,false, "rect") );

        for(Branch b : tree)
            b.print();
    }
}
