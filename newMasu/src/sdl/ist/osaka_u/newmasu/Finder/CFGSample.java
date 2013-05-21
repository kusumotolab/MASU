package sdl.ist.osaka_u.newmasu.Finder;

import org.eclipse.jdt.core.dom.*;
import sdl.ist.osaka_u.newmasu.Experimental.Processor;

import java.util.List;

public class CFGSample extends StatementProcessor {


    public void def(ASTNode node){
        System.out.println(indent + node.toString());
    }

    @Override
    public boolean visit(IfStatement node){

        System.out.println( indent + "if  --  " + ((IfStatement)node).getExpression() );
        System.out.println( indent + "then");
        node.getThenStatement().accept(this);

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
        List<Statement> list = ((Block)node).statements();
        for( Statement s : list)
            Processor.get(s,this).process(s);
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
