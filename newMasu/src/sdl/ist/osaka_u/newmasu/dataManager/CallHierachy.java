package sdl.ist.osaka_u.newmasu.dataManager;

import java.util.HashSet;

import sdl.ist.osaka_u.newmasu.util.Pair;

public class CallHierachy {

	final private static HashSet<Pair<String, String>> relations = new HashSet<Pair<String, String>>();
	public final static HashSet<Pair<String, String>> getRelations() {
		return relations;
	}

	public final static void addRelation(String invocatedMethodName, String parentName) {
		relations.add(new Pair<String, String>(invocatedMethodName, parentName));
	}
	
	public static HashSet<String> getCallHierachy(String methodName){
		
		return new HashSet<String>();
		
	}

}
