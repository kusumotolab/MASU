package sdl.ist.osaka_u.newmasu.Plugin.graph;

import com.sun.tools.javac.util.Pair;
import org.eclipse.jdt.core.dom.*;
import sdl.ist.osaka_u.newmasu.Plugin.CFG.TestWriter;

import java.util.*;

public class GraphTestVisitor extends ASTVisitor{

    private Map<Pair<Node,Node>, String> edge = new LinkedHashMap<>();
    private Node root = null;
    private Node nowNode = null;

    // nodes - possible to be jumped
    private Stack<Node> enter = new Stack<>();
    private Stack<Node> exit = new Stack<>();

    public void def(ASTNode node){
        final Node n = new Node(node.toString(),false,"ellipse");
        nowNode = nowNode.addChildren(n);
    }

    @Override
    public boolean visit(IfStatement node){
        nowNode = nowNode.addChildren(new Node("{", true, "ellipse"));

        final Node condNode = new Node(node.getExpression().toString(), false, "diamond");
        nowNode = nowNode.addChildren(condNode);

        final Node dummy = new Node("}", true, "ellipse");

        // create then branch
        GraphTestProcessor.get(node.getThenStatement(), this).process(node.getThenStatement());
        nowNode.addChildren(dummy);
        // create edge label
        final Pair<Node,Node> thenEdge = new Pair<>(condNode, condNode.getChildren().get(0));
        edge.put(thenEdge, "true");

        // create else branch
        nowNode = condNode;
        if( node.getElseStatement() != null )
            GraphTestProcessor.get(node.getElseStatement(), this).process(node.getElseStatement());
        nowNode = nowNode.addChildren(dummy);
        // create edge label
        final Pair<Node,Node> elseEdge = new Pair<>(condNode, condNode.getChildren().get(1));
        edge.put(elseEdge, "false");

        return false;
    }
    @Override
    public boolean visit(SwitchStatement node){
        nowNode = nowNode.addChildren(new Node("{", true, "ellipse"));

        final Node condNode = new Node(node.getExpression().toString(), false, "diamond");
        nowNode = nowNode.addChildren(condNode);

        final Node dummy = new Node("}", true, "ellipse");

        final List<String> caseLabel = new ArrayList<>();
        for(Object tmp : node.statements()){
            final Statement s = (Statement)tmp;
            System.out.println(s.toString());
            if(s instanceof SwitchCase){
                final SwitchCase sc = (SwitchCase)s;

                if(sc.getExpression()!=null)
                    caseLabel.add(sc.getExpression().toString());
                else
                    caseLabel.add("default");

                nowNode = condNode;
            }
/*            else if(s instanceof BreakStatement){
                final BreakStatement bs = (BreakStatement)s;
                nowNode.addChildren(dummy);
                nowNode = condNode;
            }
*/
            else{
                GraphTestProcessor.get(s, this).process(s);
            }

        }

        if(caseLabel.size()==0)
            nowNode = nowNode.addChildren(dummy);  // condNode.add
        else{
            for(int i=0; i<caseLabel.size(); i++){
                // create edge label
                final Pair<Node,Node> thenEdge = new Pair<>(condNode, condNode.getChildren().get(i));
                edge.put(thenEdge, caseLabel.get(i));
            }
        }
        nowNode = dummy;

        return false;
    }
    @Override
    public boolean visit(ForStatement node){

        nowNode =  nowNode.addChildren(new Node("{", true, "ellipse"));
        nowNode = nowNode.addChildren(new Node(node.initializers().toString(), false, "ellipse"));

        final Node condNode = new Node(node.getExpression().toString(), false, "diamond");
        nowNode = nowNode.addChildren(condNode);

        final Node dummy = new Node("}", true, "ellipse");

        // set enter & exit node
        enter.push(condNode);
        exit.push(dummy);

        // create then branch
        GraphTestProcessor.get(node.getBody(), this).process(node.getBody());
        nowNode = nowNode.addChildren(new Node(node.updaters().toString(), false, "ellipse"));
        nowNode = nowNode.addChildren(condNode);
        // create edge label
        final Pair<Node,Node> thenEdge = new Pair<>(
                nowNode, nowNode.getChildren().get(0));
        edge.put(thenEdge, "true");

        // create else branch
        condNode.addChildren(dummy);
        nowNode = dummy;
        // create edge label
        final Pair<Node,Node> elseEdge = new Pair<>(condNode, dummy);
        edge.put(elseEdge, "false");

        // pop enter & exit node
        enter.pop();
        exit.pop();

        return false;
    }
    @Override
    public boolean visit(WhileStatement node){
        nowNode =  nowNode.addChildren(new Node("{", true, "ellipse"));
        final Node condNode = new Node(node.getExpression().toString(), false, "diamond");
        nowNode = nowNode.addChildren(condNode);
        final Node dummy = new Node("}", true, "ellipse");

        // set enter & exit node
        enter.push(condNode);
        exit.push(dummy);

        // create then branch
        GraphTestProcessor.get(node.getBody(), this).process(node.getBody());
        nowNode = nowNode.addChildren(condNode);
        // create edge label
        final Pair<Node,Node> thenEdge = new Pair<>(
                nowNode, nowNode.getChildren().get(0));
        edge.put(thenEdge, "true");

        // create else branch
        condNode.addChildren(dummy);
        nowNode = dummy;
        // create edge label
        final Pair<Node,Node> elseEdge = new Pair<>(
                condNode, dummy);
        edge.put(elseEdge, "false");

        // pop enter & exit node
        enter.pop();
        exit.pop();

        return false;
    }
    @Override
    public boolean visit(DoStatement node){
        final Node doStart = new Node("do {", true, "ellipse");
        nowNode = nowNode.addChildren(doStart);
        final Node dummy = new Node("}", true, "ellipse");

        // set enter & exit node
        enter.push(doStart);
        exit.push(dummy);

        // create then branch
        GraphTestProcessor.get(node.getBody(), this).process(node.getBody());

        final Node condNode = new Node(node.getExpression().toString(), false, "diamond");
        nowNode = nowNode.addChildren(condNode);
        nowNode = nowNode.addChildren(doStart);
        // create edge label
        final Pair<Node,Node> thenEdge = new Pair<>(condNode,doStart);
        edge.put(thenEdge, "true");

        // create else branch
        condNode.addChildren(dummy);
        nowNode = dummy;
        // create edge label
        final Pair<Node,Node> elseEdge = new Pair<>(
                condNode, dummy);
        edge.put(elseEdge, "false");

        // pop enter & exit node
        enter.pop();
        exit.pop();

        return false;
    }
    @Override
    public boolean visit(EnhancedForStatement node){
        nowNode =  nowNode.addChildren(new Node("{", true, "ellipse"));
        final Node condNode = new Node(
                node.getParameter().toString() + " <- " + node.getExpression().toString(),
                false, "diamond");
        nowNode = nowNode.addChildren(condNode);
        final Node dummy = new Node("}", true, "ellipse");

        // set enter & exit node
        enter.push(condNode);
        exit.push(dummy);

        // create then branch
        GraphTestProcessor.get(node.getBody(), this).process(node.getBody());
        nowNode = nowNode.addChildren(condNode);
        // create edge label
        final Pair<Node,Node> thenEdge = new Pair<>(
                nowNode, nowNode.getChildren().get(0));
        edge.put(thenEdge, "true");

        // create else branch
        condNode.addChildren(dummy);
        nowNode = dummy;
        // create edge label
        final Pair<Node,Node> elseEdge = new Pair<>(
                condNode, dummy);
        edge.put(elseEdge, "false");

        // pop enter & exit node
        enter.pop();
        exit.pop();

        return false;
    }


    @Override
    public boolean visit(BreakStatement node){
        nowNode = nowNode.addChildren(new Node(node.toString(), false, "triangle"));
        nowNode.addChildren(exit.peek());
        return false;
    }
    @Override
    public boolean visit(ReturnStatement node){
        nowNode = nowNode.addChildren(new Node(node.toString(), false, "triangle"));
        nowNode.addChildren(exit.get(0));
        return false;
    }
    @Override
    public boolean visit(ContinueStatement node){
        nowNode = nowNode.addChildren(new Node(node.toString(), false, "triangle"));
        nowNode.addChildren(enter.peek());
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

        final Node start = new Node("Start " + label, false, "rect");
        root = start;
        nowNode = root;

        enter.push(start);
        exit.push(new Node("End " + label, false, "rect"));

        return true;
    }
    @Override
    public void endVisit(MethodDeclaration node){
        nowNode.addChildren(exit.pop());
        root = enter.pop();

        root.used.clear();
        root.removeDummy(edge);

        assert enter.isEmpty();
        assert exit.isEmpty();

        root.print(edge);
    }
}
