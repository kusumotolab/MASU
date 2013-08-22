package sdl.ist.osaka_u.newmasu.Plugin.PDG;

import com.sun.tools.javac.util.Pair;
import org.eclipse.jdt.core.dom.*;
import sdl.ist.osaka_u.newmasu.Plugin.CFG.CFGSample;
import sdl.ist.osaka_u.newmasu.data.BindingManager;

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
