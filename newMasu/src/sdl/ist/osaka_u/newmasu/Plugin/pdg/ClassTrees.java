package sdl.ist.osaka_u.newmasu.Plugin.pdg;

import com.sun.tools.javac.util.Pair;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ClassTrees {
    public List<MethodTrees> methods = new ArrayList<>();
    public List<Node> fields = new ArrayList<>();
    public Map<Pair<Node,Node>,String> varEdge = new LinkedHashMap<>();

    public void outputGraph(){

        // create var edge
        for(MethodTrees mt : methods)
            varEdge.putAll(mt.root.createVarEdge(fields, mt.args));

        Writer.println("digraph PDG {");

        Writer.println("subgraph clusterField {");
        Writer.println("label = \"fields\";");
        for( Node n : fields )
            Writer.println(n.toGraphDefine());
        Writer.println("}");

        int graphCount=0;
        for( MethodTrees mt : methods ){
            Writer.println("subgraph cluster" + graphCount++ + "{");
            //Writer.println("label = \"" + sb.toString() + "\";");
            for( Node n : mt.args )
                Writer.println(n.toGraphDefine());

            mt.root.print(mt.edge);
            Writer.println("}");
        }

        for( Map.Entry<Pair<Node,Node>,String> e : varEdge.entrySet() ){
            if(!e.getKey().snd.getDisableEdge()){
                StringBuilder sb = new StringBuilder();
                sb.append(e.getKey().fst.toGraphId() + " -> " + e.getKey().snd.toGraphId());
                sb.append(" [label=\"" + e.getKey().fst.sanitize(e.getValue()) + "\", style=\"dotted\"];");
                Writer.println(sb.toString());
            }
        }

        Writer.println("}");
    }
}
