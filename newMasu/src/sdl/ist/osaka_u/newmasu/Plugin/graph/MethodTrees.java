package sdl.ist.osaka_u.newmasu.Plugin.graph;

import com.sun.tools.javac.util.Pair;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MethodTrees {
    public Node root = null;
    public List<Node> args = new ArrayList<>();
    public Map<Pair<Node,Node>,String> edge = new LinkedHashMap<>();
}
