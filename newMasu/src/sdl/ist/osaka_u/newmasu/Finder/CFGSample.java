package sdl.ist.osaka_u.newmasu.Finder;

import org.eclipse.jdt.core.dom.*;
import sdl.ist.osaka_u.newmasu.data.dataManager.FileManager;

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
        TestWriter.newFile();
        TestWriter.println("digraph CFG {");
        return true;
    }
    @Override public void endVisit(CompilationUnit node){
        TestWriter.println("}");
    }


    private Queue<ASTNode> graphQueue = new LinkedList<>();
    private void addQueue(ASTNode node){
        if(graphQueue.size()==1){
            ASTNode prev = graphQueue.poll();
            TestWriter.println(nameMap.get(prev) + " -> " + nameMap.get(node) + ";");
        }
        graphQueue.add(node);
    }

    //////////////////////////////////////////////////////////////////////////////////

    public void def(ASTNode node){
        System.out.println(indent + node.toString());
        nameMapping(node);
        TestWriter.println(getName() + " [label=\"" + node.toString().trim() + "\"];");

        addQueue(node);
    }


    @Override
    public boolean visit(IfStatement node){

        System.out.println(indent + "if  --  " + node.getExpression());
        System.out.println( indent + "then");
        node.getThenStatement().accept(this);

        nameMapping(node);
        TestWriter.println(getName() + " [label=\"" + node.getExpression().toString().trim() + "\", shape=\"diamond\"];");
        addQueue(node);

        if(node.getElseStatement()!=null){
            System.out.println( indent + "else");
            node.getElseStatement().accept(this);
        }

        return false;
    }

    @Override
    public boolean visit(Block node){

        System.out.println( indent + "{");
        indent += "  ";
        List<Statement> list = node.statements();
        for( Statement s : list)
            DefaultProcessor.get(s, this).process(s);  // To process default nodes
        indent = indent.substring(0,indent.length()-2);
        System.out.println( indent + "}");

        return false;
    }

    /*
    private boolean isVisited = false;

    @Override
    public boolean visit(IfStatement node){
        isVisited = true;
        System.out.println(indent + "If Statement");
        System.out.println(indent + node.getExpression().toString() );
        node.getThenStatement().accept(new CFGSample(indent));
        return false;
    }

/*
    @Override
    public boolean visit(ForStatement node){
        isVisited = true;
        System.out.println(indent + "For Statement");
        System.out.println(indent + node.getExpression().toString() );
        return true;
    }


    @Override
    public void endVisit(Block node){
        System.out.println( indent + "*}" );
        delIndent();
    }


    @Override
    public boolean visit(Block node){

        System.out.println( indent + "{*" );
        addIndent();

        List<Statement> statements = node.statements();
        int i=0;
        for(Statement s : statements){

            isVisited=false;
            s.accept( this );
            if(isVisited){
                System.out.println( s.getLength() + " is visited");
            }
            if(!isVisited){
                System.out.print( indent + i +  ": " + s.toString() );
            }
            i++;
        }

        return false;
    }


    public CFGSample(String ind){
        indent = ind;
    }
    public CFGSample(){
    }
*/
    private String indent = "";
    private void addIndent(){
        indent += "  ";
    }
    private void delIndent(){
        if(indent.length() > 2)
            indent = indent.substring(0,indent.length()-2);
    }
}
