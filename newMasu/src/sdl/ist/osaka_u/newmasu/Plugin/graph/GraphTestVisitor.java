package sdl.ist.osaka_u.newmasu.Plugin.graph;

import com.sun.tools.javac.util.Pair;
import org.eclipse.jdt.core.dom.*;
import sdl.ist.osaka_u.newmasu.Plugin.CFG.TestWriter;

import java.util.*;

public class GraphTestVisitor extends ASTVisitor{

    private Branch nowBranch = null;
    private Set<Branch> allBranch = new LinkedHashSet<>();
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

      nowBranch.insert(new Node("{", true, "ellipse"));

        final Node condNode = new Node(node.getExpression().toString(), false, "diamond");
        nowBranch.insert(condNode);

        final Node dummy = new Node("}", true, "ellipse");
        final Branch previousBranch = nowBranch;
        //allBranch.add(previousBranch);

        // create then branch
        final Branch thenBranch = previousBranch.newBranch();
        allBranch.add(thenBranch);
        nowBranch = thenBranch;
        GraphTestProcessor.get(node.getThenStatement(), this).process(node.getThenStatement());
        nowBranch.insert(dummy);
        // create edge label
        final Pair<Node,Node> thenEdge = new Pair<>(thenBranch.getPath().get(0), thenBranch.getPath().get(1));
        edge.put(thenEdge, "true");

        // create else branch
        final Branch elseBranch = previousBranch.newBranch();
        allBranch.add(elseBranch);
        nowBranch = elseBranch;
        if( node.getElseStatement() != null )
            GraphTestProcessor.get(node.getElseStatement(), this).process(node.getElseStatement());
        nowBranch.insert(dummy);
        // create edge label
        final Pair<Node,Node> elseEdge = new Pair<>(elseBranch.getPath().get(0), elseBranch.getPath().get(1));
        edge.put(elseEdge, "false");

        nowBranch = new Branch();
        allBranch.add(nowBranch);
        nowBranch.insert(dummy);

        return false;
    }

    @Override
    public boolean visit(ForStatement node){

        nowBranch.insert(new Node("{", true, "ellipse"));

        nowBranch.insert(new Node(node.initializers().toString(), false, "ellipse"));

        Node condNode = new Node(node.getExpression().toString(), false, "diamond");
        nowBranch.insert(condNode);

        //final Node dummy = new Node("dummy", true, "ellipse");
        final Node dummy = new Node("}", true, "ellipse");
        final Branch previousBranch = nowBranch;
        //allBranch.add(previousBranch);

        // set enter & exit node
        enter.push(condNode);
        exit.push(dummy);

        // create then branch
        final Branch thenBranch = previousBranch.newBranch();
        allBranch.add(thenBranch);
        nowBranch = thenBranch;
        GraphTestProcessor.get(node.getBody(), this).process(node.getBody());
        nowBranch.insert(new Node(node.updaters().toString(), false, "ellipse"));
        nowBranch.insert(condNode);
        // create edge label
        final Pair<Node,Node> thenEdge = new Pair<>(thenBranch.getPath().get(0), thenBranch.getPath().get(1));
        edge.put(thenEdge, "true");

        // create else branch
        final Branch elseBranch = previousBranch.newBranch();
        allBranch.add(elseBranch);
        elseBranch.insert(dummy);
        // create edge label
        final Pair<Node,Node> elseEdge = new Pair<>(elseBranch.getPath().get(0), elseBranch.getPath().get(1));
        edge.put(elseEdge, "false");

        nowBranch = new Branch();
        allBranch.add(nowBranch);
        nowBranch.insert(dummy);

        // pop enter & exit node
        enter.pop();
        exit.pop();

        return false;
    }
    @Override
    public boolean visit(WhileStatement node){

        nowBranch.insert(new Node("{", true, "ellipse"));

        final Node condNode = new Node(node.getExpression().toString(), false, "diamond");
        nowBranch.insert(condNode);

        //final Node dummy = new Node("dummy", true, "ellipse");
        final Node dummy = new Node("}", true, "ellipse");
        final Branch previousBranch = nowBranch;
        //allBranch.add(previousBranch);

        // set enter & exit node
        enter.push(condNode);
        exit.push(dummy);

        // create then branch
        final Branch thenBranch = previousBranch.newBranch();
        allBranch.add(thenBranch);
        nowBranch = thenBranch;
        GraphTestProcessor.get(node.getBody(), this).process(node.getBody());
        nowBranch.insert(condNode);
        // create edge label
        final Pair<Node,Node> thenEdge = new Pair<>(thenBranch.getPath().get(0), thenBranch.getPath().get(1));
        edge.put(thenEdge, "true");

        // create else branch
        final Branch elseBranch = previousBranch.newBranch();
        allBranch.add(elseBranch);
        elseBranch.insert(dummy);
        // create edge label
        final Pair<Node,Node> elseEdge = new Pair<>(elseBranch.getPath().get(0), elseBranch.getPath().get(1));
        edge.put(elseEdge, "false");

        nowBranch = new Branch();
        allBranch.add(nowBranch);
        nowBranch.insert(dummy);

        // pop enter & exit node
        enter.pop();
        exit.pop();

        return false;
    }
    @Override
    public boolean visit(BreakStatement node){
        nowBranch.insert(new Node(node.toString(), false, "triangle"));
        nowBranch.insert(exit.peek());
        nowBranch = new Branch();
        allBranch.add(nowBranch);
        return false;
    }
    @Override
    public boolean visit(ReturnStatement node){
        nowBranch.insert(new Node(node.toString(), false, "triangle"));
        nowBranch.insert(exit.get(0));
//        nowBranch.getPath().getLast().setId(exit.get(0).getId());
        nowBranch = new Branch();
        allBranch.add(nowBranch);
        return false;
    }
    @Override
    public boolean visit(ContinueStatement node){
        nowBranch.insert(new Node(node.toString(), false, "triangle"));
        nowBranch.insert(enter.peek());
        nowBranch = new Branch();
        allBranch.add(nowBranch);
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

        allBranch.clear();
        nowBranch = new Branch();
        allBranch.add(nowBranch);

        final Node start = new Node("Start " + label, false, "rect");
        nowBranch.insert( start );
        enter.push(start);
        exit.push(new Node("End " + label, false, "rect"));

        return true;
    }
    @Override
    public void endVisit(MethodDeclaration node){
/*
        // exit(0) is references many places, so the ObjectID of exit(0) should not be changed.
        // the id of exit(0) should be the id of the previous node.
        final Node exit0 = exit.pop();
        final Node last = nowBranch.getPath().getLast();
        int id = exit0.getId();
        if(last.getDummy())
            id = last.getId();
        exit0.setId(id);
  */
        nowBranch.insert(exit.pop());

        // This may cause the duplication of the id of nodes, so self-loop in End should be removed.
/*

        for(Branch b : allBranch){
            if(b.getPath().size()>=2 &&
                    b.getPath().get(b.getPath().size()-2).getId()
                            == b.getPath().getLast().getId()){
                b.getPath().removeLast();
            }
        }

*/
        Node ent = enter.pop();
        final Tree root = new Tree(ent);
        Map<Node, Tree> node2tree = new LinkedHashMap<>();
        node2tree.put(ent, root);

        for(Branch b : allBranch){
            for(Node n : b.getPath()){
                if(!node2tree.containsKey(n))
                    node2tree.put(n, new Tree(n));
            }
        }
/*
        for( Map.Entry<Node,Tree> e : node2tree.entrySet()){
            System.out.println(e.getKey().getLabel());
        }
*/
        for(Branch b : allBranch){
            for(int i=0; i<b.getPath().size(); i++){
                if(b.getPath().size()-i < 2)
                    break;
                Tree t1 = node2tree.get(b.getPath().get(i));
                Tree t2 = node2tree.get(b.getPath().get(i+1));
                t1.addChildren(t2);
            }
        }

        root.used.clear();
//        root.dump();

        Map<Pair<Tree,Tree>,String> treeEdge = new LinkedHashMap<>();
        for(Map.Entry<Pair<Node,Node>,String> e : edge.entrySet()){
            String s = e.getValue();
            Pair<Tree,Tree> t = new Pair<>(node2tree.get(e.getKey().fst),
                    node2tree.get(e.getKey().snd));
            treeEdge.put(t,s);
        }

        root.used.clear();
        root.removeDummy(treeEdge);
        root.used.clear();
        root.dump();

//        for(Branch b : allBranch)
//            b.removeEdge(edge);

        assert enter.isEmpty();
        assert exit.isEmpty();

        root.used.clear();
        root.print(treeEdge);
/*
        for(Branch b : allBranch)
            b.print(edge);
            */
    }
}
