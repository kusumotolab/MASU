package sdl.ist.osaka_u.newmasu.dataManager;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import sdl.ist.osaka_u.newmasu.util.Pair;

public class MethodManager {

	final private static HashMap<String, MethodDeclaration> methods = new HashMap<String, MethodDeclaration>();

	public static HashMap<String, MethodDeclaration> getMethods() {
		return methods;
	}

	public static void addMethod(String name, MethodDeclaration node) {
		methods.put(name, node);
	}

	final private static HashSet<Pair<String, String>> relations = new HashSet<Pair<String, String>>();

	public static HashSet<Pair<String, String>> getRelations() {
		return relations;
	}

	public static void addRelation(String from, String to) {
		relations.add(new Pair<String, String>(from, to));
	}

	public static HashSet<String> getCallHierachy(String to) {
		final HashSet<String> results = new HashSet<String>();
		for (Pair<String, String> p : relations) {
			if (p.getSecond().equals(to)) {
				results.add(p.getFirst());
			}
		}
		return results;
	}
	
	public static HashSet<String> getAllInvokedMethod(String from) {
		final HashSet<String> results = new HashSet<String>();
		for (Pair<String, String> p : relations) {
			if (p.getFirst().equals(from)) {
				results.add(p.getSecond());
			}
		}
		return results;
	}
	
	// インスタンスの生成を防ぐ
	private MethodManager(){}


}
