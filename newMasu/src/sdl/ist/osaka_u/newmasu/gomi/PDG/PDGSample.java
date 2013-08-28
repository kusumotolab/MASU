package sdl.ist.osaka_u.newmasu.gomi.PDG;

import org.eclipse.jdt.core.dom.*;
import sdl.ist.osaka_u.newmasu.gomi.CFG.CFGSample;

import java.util.*;

public class PDGSample extends CFGSample {

    @Override
    public boolean visit(FieldDeclaration node){
        System.out.println("hoge");
        def(node);
        return false;
    }
    @Override
    public boolean visit(Block node){
        List<Statement> list = node.statements();
        for( Statement s : list)
            PDGSampleProcessor.get(s, this).process(s);
        return false;
    }
}
