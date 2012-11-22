package sdl.ist.osaka_u.newmasu.callhierachy;

import java.util.HashSet;

import org.eclipse.jdt.core.dom.IMethodBinding;

import sdl.ist.osaka_u.newmasu.util.Pair;

public class CallHierachy {
	
	final private HashSet<Pair<String,String>> relations = new HashSet<Pair<String,String>>();
	public final HashSet<Pair<String, String>> getRelations() {
		return relations;
	}
	
	public final void addRelation(IMethodBinding bind1, IMethodBinding bind2){
		
	}

}
