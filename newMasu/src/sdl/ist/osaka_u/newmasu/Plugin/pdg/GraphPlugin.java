package sdl.ist.osaka_u.newmasu.Plugin.pdg;

import org.eclipse.jdt.core.dom.CompilationUnit;
import sdl.ist.osaka_u.newmasu.Plugin.Plugin;
import sdl.ist.osaka_u.newmasu.Settings;
import sdl.ist.osaka_u.newmasu.data.BindingManager;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GraphPlugin implements Plugin {

    private Map<String,ClassTree> classTrees = new HashMap<>();
    public Map<String,ClassTree> getClassTree(){ return classTrees; }

    @Override
    public void run() {
        System.out.println("Graph Plugin");
        Set<String> paths = Settings.getInstance().getListFiles();
        for(String path : paths){
            System.out.println(path.toString() + " processing...");
            CompilationUnit unit = BindingManager.getRel().getCallerMap().get(Paths.get(path));
            GraphVisitor gv = new GraphVisitor();
            unit.accept(gv);
            classTrees.put(path, gv.classTrees);
        }
    }
}
