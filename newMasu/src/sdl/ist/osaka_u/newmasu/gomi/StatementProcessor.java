package sdl.ist.osaka_u.newmasu.gomi;

import org.eclipse.jdt.core.dom.*;

import java.util.List;

public class StatementProcessor extends ASTVisitor {

    @Override
     public boolean visit(Block node){
        List<Statement> statements = node.statements();
        for(Statement s : statements){
            s.accept( this );
        }
        return false;
   }

    /**
     * To prevent instantiation
     */
    protected StatementProcessor(){
    }

}
