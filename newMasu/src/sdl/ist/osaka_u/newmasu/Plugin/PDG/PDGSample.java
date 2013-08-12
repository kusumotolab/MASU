package sdl.ist.osaka_u.newmasu.Plugin.PDG;

import com.sun.tools.javac.util.Pair;
import org.eclipse.jdt.core.dom.*;
import sdl.ist.osaka_u.newmasu.Plugin.CFG.CFGSample;
import sdl.ist.osaka_u.newmasu.data.BindingManager;

import java.util.*;

public class PDGSample extends CFGSample {

    @Override
    public boolean visit(FieldDeclaration node){
        def(node);
        return false;
    }

}
