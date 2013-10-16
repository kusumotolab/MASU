package sdl.ist.osaka_u.newmasu.Plugin.pdg;

import com.sun.tools.javac.util.Pair;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MethodTree {
    public ASTNode rawNode = null;
    public Node root = null;
    public List<Node> args = new ArrayList<>();
    public Map<Pair<Node,Node>,String> edge = new LinkedHashMap<>();
}
