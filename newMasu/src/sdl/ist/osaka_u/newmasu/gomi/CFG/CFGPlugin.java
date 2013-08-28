package sdl.ist.osaka_u.newmasu.gomi.CFG;

import org.eclipse.jdt.core.dom.CompilationUnit;
import sdl.ist.osaka_u.newmasu.Plugin.Plugin;
import sdl.ist.osaka_u.newmasu.Settings;
import sdl.ist.osaka_u.newmasu.data.BindingManager;

import java.nio.file.Paths;
import java.util.Set;

public class CFGPlugin implements Plugin {
    @Override
    public void run() {
        Set<String> paths = Settings.getInstance().getListFiles();
        for(String path : paths){
            CompilationUnit unit = BindingManager.getRel().getCallerMap().get(Paths.get(path));
            unit.accept(getVisitor());
        }
    }

    protected CFGSample getVisitor(){
        return new CFGSample();
    }
}
