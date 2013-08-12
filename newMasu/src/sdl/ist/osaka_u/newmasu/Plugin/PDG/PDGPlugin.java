package sdl.ist.osaka_u.newmasu.Plugin.PDG;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import sdl.ist.osaka_u.newmasu.AST.ASTVisitorForInfo;
import sdl.ist.osaka_u.newmasu.Plugin.CFG.CFGPlugin;
import sdl.ist.osaka_u.newmasu.Plugin.CFG.CFGSample;
import sdl.ist.osaka_u.newmasu.Plugin.Plugin;
import sdl.ist.osaka_u.newmasu.Settings;
import sdl.ist.osaka_u.newmasu.data.BindingManager;

import java.nio.file.Paths;
import java.util.Set;

public class PDGPlugin extends CFGPlugin {
    @Override
    protected CFGSample getVisitor(){
        return new PDGSample();
    }
}
