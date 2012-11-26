package sdl.ist.osaka_u.newmasu.callhierachy;

import java.util.HashSet;

import sdl.ist.osaka_u.newmasu.util.Pair;

public class CallHierachy {

	final private HashSet<Pair<String, String>> relations = new HashSet<Pair<String, String>>();
	public final HashSet<Pair<String, String>> getRelations() {
		return relations;
	}

	public final void addRelation(String invocatedMethodName, String parentName) {
		relations.add(new Pair<String, String>(invocatedMethodName, parentName));
	}

}
