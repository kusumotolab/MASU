package sdl.ist.osaka_u.newmasu.Plugin.graph;

import com.sun.tools.javac.util.Pair;
import org.eclipse.jdt.core.dom.*;
import sdl.ist.osaka_u.newmasu.data.BindingManager;

import java.util.*;

public class GraphVisitor extends ASTVisitor{

    private Map<Pair<Node,Node>, String> edge = new LinkedHashMap<>();
    private Node root = null;
    private Node nowNode = null;
    private List<Node> fields = new ArrayList<>();

    // nodes - possible to be jumped
    private Stack<Node> enter = new Stack<>();
    private Stack<Node> exit = new Stack<>();

    public void def(ASTNode node){
        final Node n = new Node(node.toString(),false,"ellipse", node);
        nowNode = nowNode.addChildren(n);
    }

    @Override
    public boolean visit(IfStatement node){
        nowNode = nowNode.addChildren(new Node("{", true, "ellipse"));

        final Node condNode = new Node(node.getExpression().toString(), false, "diamond", node.getExpression());
        nowNode = nowNode.addChildren(condNode);

        final Node dummy = new Node("}", true, "ellipse");

        // create then branch
        GraphProcessor.get(node.getThenStatement(), this).process(node.getThenStatement());
        nowNode.addChildren(dummy);
        // create edge label
        final Pair<Node,Node> thenEdge = new Pair<>(condNode, condNode.getChildren().get(0));
        edge.put(thenEdge, "true");

        // create else branch
        nowNode = condNode;
        if( node.getElseStatement() != null )
            GraphProcessor.get(node.getElseStatement(), this).process(node.getElseStatement());
        nowNode = nowNode.addChildren(dummy);
        // create edge label
        final Pair<Node,Node> elseEdge = new Pair<>(condNode, condNode.getChildren().get(1));
        edge.put(elseEdge, "false");

        return false;
    }
    /*
    @Override
    public boolean visit(SwitchStatement node){
        nowNode = nowNode.addChildren(new Node("{", true, "ellipse"));

        final Node condNode = new Node(node.getExpression().toString(), false, "diamond", node.getExpression());
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
///*            else if(s instanceof BreakStatement){
                final BreakStatement bs = (BreakStatement)s;
                nowNode.addChildren(dummy);
                nowNode = condNode;
            }
//
            else{
                GraphProcessor.get(s, this).process(s);
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
*/
    @Override
    public boolean visit(ForStatement node){

        nowNode =  nowNode.addChildren(new Node("{", true, "ellipse"));
        for( Object o : node.initializers() ){
            Expression exp = (Expression)o;
            nowNode = nowNode.addChildren(new Node(exp.toString(), false, "ellipse", exp));
        }

        final Node condNode = new Node(node.getExpression().toString(), false, "diamond", node.getExpression());
        nowNode = nowNode.addChildren(condNode);

        final Node dummy = new Node("}", true, "ellipse");

        // set enter & exit node
        enter.push(condNode);
        exit.push(dummy);

        // create then branch
        GraphProcessor.get(node.getBody(), this).process(node.getBody());
        for( Object o : node.initializers() ){
            Expression exp = (Expression)o;
            nowNode = nowNode.addChildren(new Node(exp.toString(), false, "ellipse", exp));
        }
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
        final Node condNode = new Node(node.getExpression().toString(), false, "diamond", node.getExpression());
        nowNode = nowNode.addChildren(condNode);
        final Node dummy = new Node("}", true, "ellipse");

        // set enter & exit node
        enter.push(condNode);
        exit.push(dummy);

        // create then branch
        GraphProcessor.get(node.getBody(), this).process(node.getBody());
        nowNode = nowNode.addChildren(condNode);
        // create edge label
        final Pair<Node,Node> thenEdge = new Pair<>(nowNode, nowNode.getChildren().get(0));
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
        GraphProcessor.get(node.getBody(), this).process(node.getBody());

        final Node condNode = new Node(node.getExpression().toString(), false, "diamond", node.getExpression());
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
        condNode.addBindings( node.getParameter() );
        condNode.addBindings( node.getExpression() );

        nowNode = nowNode.addChildren(condNode);
        final Node dummy = new Node("}", true, "ellipse");

        // set enter & exit node
        enter.push(condNode);
        exit.push(dummy);

        // create then branch
        GraphProcessor.get(node.getBody(), this).process(node.getBody());
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
        nowNode = nowNode.addChildren(new Node(node.toString(), false, "triangle", node));
        nowNode.addChildren(exit.peek());
        return false;
    }
    @Override
    public boolean visit(ReturnStatement node){
        nowNode = nowNode.addChildren(new Node(node.toString(), false, "triangle", node));
        nowNode.addChildren(exit.get(0));
        return false;
    }
    @Override
    public boolean visit(ContinueStatement node){
        nowNode = nowNode.addChildren(new Node(node.toString(), false, "triangle", node));
        nowNode.addChildren(enter.peek());
        return false;
    }

    @Override
    public boolean visit(Block node){
        List<Statement> list = node.statements();
        for( Statement s : list)
            GraphProcessor.get(s, this).process(s);
        return false;
    }

    @Override
    public boolean visit(FieldDeclaration node){
        fields.add(new Node(node.toString(), false, "octagon", node));
        return false;
    }


    @Override public boolean visit(CompilationUnit node){
        Writer.newFile(BindingManager.getRel().getCalleeMap().get(node).getFileName().toString());
        Writer.println("digraph PDG {");
        return true;
    }

    @Override public void endVisit(CompilationUnit node){
        Writer.println("subgraph clusterField {");
        Writer.println("label = \"fields\";");
        for( Node n : fields )
            Writer.println(n.toGraphDefine());
        Writer.println("}");

        Writer.println("}");
    }

    private static int graphCount = 0;
    @Override
    public boolean visit(MethodDeclaration node){
        Writer.println("subgraph cluster" + graphCount++ + "{");

        final StringBuilder sb = new StringBuilder();
        if(!node.isConstructor())
            sb.append(node.getReturnType2().toString() + " ");
        sb.append(node.getName() + "(");
        for(Object t : node.parameters()){
            SingleVariableDeclaration s = (SingleVariableDeclaration)t;
            sb.append(" " + s.getType().toString() + " " + s.getName());
        }
        sb.append(")");

        Writer.println("label = \"" + sb.toString() + "\";");
        final Node start = new Node("Start " + sb.toString(), false, "rect");
        root = start;
        nowNode = root;

        enter.push(start);
        exit.push(new Node("End " + sb.toString(), false, "rect"));

        return true;
    }
    @Override
    public void endVisit(MethodDeclaration node){
        nowNode.addChildren(exit.pop());
        root = enter.pop();

        root.used.clear();
        root.removeDummy(edge);
        final Map<Pair<Node,Node>,String> varEdge = root.createVarEdge(fields);

        assert enter.isEmpty();
        assert exit.isEmpty();

        root.print(edge, varEdge);
        Writer.println("}");
    }
}