package sdl.ist.osaka_u.newmasu.gomi.PDG;

import sdl.ist.osaka_u.newmasu.gomi.CFG.CFGPlugin;
import sdl.ist.osaka_u.newmasu.gomi.CFG.CFGSample;

public class PDGPlugin extends CFGPlugin {
    @Override
    protected CFGSample getVisitor(){
        return new PDGSample();
    }
}
