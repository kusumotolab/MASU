package sdl.ist.osaka_u.newmasu.Plugin.graph;

public class ConditionNode extends Node {
    public Tree thenTree = null;
    public Tree elseTree = null;

    public String toGraph(){


        StringBuilder sb = new StringBuilder();

        /*
        sb.append( "<" + getLabel() + ">" );
        sb.append( System.lineSeparator() );
        sb.append( "then: " );
        sb.append(thenTree);

        if(elseTree!=null){
            sb.append( "else: " );
            sb.append(elseTree);
        }

        sb.append( "if end" );
        return sb.toString();
        */

        sb.append( toGraphId() + " [label=\"" + toGraphLabel() + "\"];" );

        if(elseTree!=null)
            sb.append( thenTree.toGraph() + elseTree.toGraph() );
        else
            sb.append( thenTree.toGraph() );

        return sb.toString();
    }
}