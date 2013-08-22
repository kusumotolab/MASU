package sdl.ist.osaka_u.newmasu.Plugin.graph;

import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GraphTestVisitor extends ASTVisitor{

    private Branch nowBranch = null;
    private Set<Branch> tree = new HashSet<>();
    private Set<String> edge = new HashSet<>();

    public void def(ASTNode node){
        final Node n = new Node(node.toString(),false,"ellipse");
        nowBranch.insert(n);
    }

    @Override
    public boolean visit(IfStatement node){

        final Node condNode = new Node();
        condNode.setLabel(node.getExpression().toString());
        condNode.setShape("diamond");
        nowBranch.insert(condNode);

        final Node dummy = new Node("dummy", true, "ellipse");

        final Branch tmpBranch = nowBranch;

        final Branch thenBranch = nowBranch.newBranch();
        nowBranch = thenBranch;
        GraphTestProcessor.get(node.getThenStatement(), this).process(node.getThenStatement());
        nowBranch.insert(dummy);
        tree.add(thenBranch);
        nowBranch = tmpBranch;

        if( node.getElseStatement() != null ){
            final Branch elseBranch = nowBranch.newBranch();
            nowBranch = elseBranch;
            GraphTestProcessor.get(node.getElseStatement(), this).process(node.getElseStatement());
            nowBranch.insert(dummy);
            tree.add(elseBranch);
            nowBranch = tmpBranch;
        }
        else{
            final Branch elseBranch = nowBranch.newBranch();
            elseBranch.insert(dummy);
            tree.add(elseBranch);
            nowBranch = tmpBranch;
        }

        tree.add(nowBranch);

        nowBranch = new Branch();
        nowBranch.insert(dummy);
        tree.add(nowBranch);

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
        System.out.println("digraph CFG {");
        return true;
    }

    @Override public void endVisit(CompilationUnit node){
        System.out.println("}");
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
        nowBranch.insert( new Node("Start " + label,false, "rect") );
        tree.add(nowBranch);

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
