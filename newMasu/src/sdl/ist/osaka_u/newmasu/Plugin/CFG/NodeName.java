package sdl.ist.osaka_u.newmasu.Plugin.CFG;

public class NodeName {
    private static Integer _name = 0;
    private static String getName(){
        return "NODE_" + _name.toString();
    }
    public static String nextName(){
        _name++;
        return getName();
    }
}
