package sdl.ist.osaka_u.newmasu.Plugin.graph;

import com.sun.tools.javac.util.Pair;
import org.eclipse.jdt.core.dom.*;
import sdl.ist.osaka_u.newmasu.Plugin.CFG.TestWriter;

import java.util.*;

public class GraphTestVisitor extends ASTVisitor{

    private Branch nowBranch = null;
    private Set<Branch> tree = new LinkedHashSet<>();
    private Map<Pair<Node,Node>, String> edge = new LinkedHashMap<>();

    // nodes - possible to be jumped
    private Stack<Node> enter = new Stack<>();
    private Stack<Node> exit = new Stack<>();

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

        // create then branch
        final Branch thenBranch = previousBranch.newBranch();
        tree.add(thenBranch);
        nowBranch = thenBranch;
        GraphTestProcessor.get(node.getThenStatement(), this).process(node.getThenStatement());
        nowBranch.insert(dummy);
        // create edge label
        final Pair<Node,Node> thenEdge = new Pair<>(thenBranch.getPath().get(0), thenBranch.getPath().get(1));
        edge.put(thenEdge, "true");

        // create else branch
        final Branch elseBranch = previousBranch.newBranch();
        tree.add(elseBranch);
        nowBranch = elseBranch;
        if( node.getElseStatement() != null )
            GraphTestProcessor.get(node.getElseStatement(), this).process(node.getElseStatement());
        nowBranch.insert(dummy);
        // create edge label
        final Pair<Node,Node> elseEdge = new Pair<>(elseBranch.getPath().get(0), elseBranch.getPath().get(1));
        edge.put(elseEdge, "false");

        nowBranch = new Branch();
        tree.add(nowBranch);
        nowBranch.insert(dummy);

        return false;
    }

    @Override
    public boolean visit(ForStatement node){

        nowBranch.insert(new Node(node.initializers().toString(), false, "ellipse"));

        final Node condNode = new Node(node.getExpression().toString(), false, "diamond");
        nowBranch.insert(condNode);

        final Node dummy = new Node("dummy", true, "ellipse");
        final Branch previousBranch = nowBranch;
        tree.add(previousBranch);

        // set enter & exit node
        enter.push(condNode);
        exit.push(dummy);

        // create then branch
        final Branch thenBranch = previousBranch.newBranch();
        tree.add(thenBranch);
        nowBranch = thenBranch;
        GraphTestProcessor.get(node.getBody(), this).process(node.getBody());
        nowBranch.insert(new Node(node.updaters().toString(), false, "ellipse"));
        nowBranch.insert(condNode);
        // create edge label
        final Pair<Node,Node> thenEdge = new Pair<>(thenBranch.getPath().get(0), thenBranch.getPath().get(1));
        edge.put(thenEdge, "true");

        // create else branch
        final Branch elseBranch = previousBranch.newBranch();
        tree.add(elseBranch);
        elseBranch.insert(dummy);
        // create edge label
        final Pair<Node,Node> elseEdge = new Pair<>(elseBranch.getPath().get(0), elseBranch.getPath().get(1));
        edge.put(elseEdge, "false");

        nowBranch = new Branch();
        tree.add(nowBranch);
        nowBranch.insert(dummy);

        // pop enter & exit node
        enter.pop();
        exit.pop();

        return false;
    }
    @Override
    public boolean visit(BreakStatement node){
        nowBranch.insert(new Node("break", false, "triangle"));
        nowBranch.insert(exit.peek());
        nowBranch = new Branch();
        tree.add(nowBranch);
        return false;
    }
    @Override
    public boolean visit(ReturnStatement node){
        nowBranch.insert(new Node("return", false, "triangle"));
        nowBranch.insert(exit.get(0));
//        nowBranch.getPath().getLast().setId(exit.get(0).getId());
        nowBranch = new Branch();
        tree.add(nowBranch);
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

        // set exit node
        exit.push(new Node("End " + label, false, "rect"));

        return true;
    }
    @Override
    public void endVisit(MethodDeclaration node){

        // exit(0) is references many places, so the ObjectID of exit(0) should not be changed.
        // the id of exit(0) should be the id of the previous node.
        final Node exit0 = exit.pop();
        final Node last = nowBranch.getPath().getLast();
        int id = exit0.getId();
        if(last.getDummy())
            id = last.getId();
        exit0.setId(id);
        nowBranch.insert(exit0);

        // This may cause the duplication of the id of nodes, so self-loop in End should be removed.


        for(Branch b : tree){
            if(b.getPath().size()>=2 &&
                    b.getPath().get(b.getPath().size()-2).getId()
                            == b.getPath().getLast().getId()){
                b.getPath().removeLast();
            }
        }




        assert enter.isEmpty();
        assert exit.isEmpty();

        for(Branch b : tree)
            b.print(edge);
    }
}
