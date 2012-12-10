package sdl.ist.osaka_u.newmasu.dataManager;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import sdl.ist.osaka_u.newmasu.util.Pair;

public class MethodManager {

	final private static HashMap<IMethodBinding, MethodDeclaration> methods = new HashMap<IMethodBinding, MethodDeclaration>();

	public static HashMap<IMethodBinding, MethodDeclaration> getMethods() {
		return methods;
	}

	public static void addMethod(IMethodBinding name, MethodDeclaration node) {
		methods.put(name, node);
	}

	final private static HashSet<Pair<IMethodBinding, IMethodBinding>> relations = new HashSet<Pair<IMethodBinding, IMethodBinding>>();

	public static HashSet<Pair<IMethodBinding, IMethodBinding>> getRelations() {
		return relations;
	}

	public static void addRelation(IMethodBinding from, IMethodBinding to) {
		relations.add(new Pair<IMethodBinding, IMethodBinding>(from, to));
	}

	public static HashSet<IMethodBinding> getCallHierachy(IMethodBinding to) {
		final HashSet<IMethodBinding> results = new HashSet<IMethodBinding>();
		for (Pair<IMethodBinding, IMethodBinding> p : relations) {
			if (p.getSecond().equals(to)) {
				results.add(p.getFirst());
			}
		}
		return results;
	}
	
	public static HashSet<IMethodBinding> getAllInvokedMethod(IMethodBinding from) {
		final HashSet<IMethodBinding> results = new HashSet<IMethodBinding>();
		for (Pair<IMethodBinding, IMethodBinding> p : relations) {
			if (p.getFirst().equals(from)) {
				results.add(p.getSecond());
			}
		}
		return results;
	}
	
	// インスタンスの生成を防ぐ
	private MethodManager(){}


}
