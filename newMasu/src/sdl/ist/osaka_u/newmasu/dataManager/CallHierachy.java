package sdl.ist.osaka_u.newmasu.dataManager;

import java.util.HashSet;

import sdl.ist.osaka_u.newmasu.util.Pair;

public class CallHierachy {

	final private static HashSet<Pair<String, String>> relations = new HashSet<Pair<String, String>>();
	public final static HashSet<Pair<String, String>> getRelations() {
		return relations;
	}

	public final static void addRelation(String from, String to) {
		relations.add(new Pair<String, String>(from, to));
	}
	
	public static HashSet<String> getCallHierachy(String methodName){
		
//		HashSet<String>
		
		return new HashSet<String>();
	}

}
