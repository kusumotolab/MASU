package sdl.ist.osaka_u.newmasu.dataManager;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ITypeBinding;

import sdl.ist.osaka_u.newmasu.util.Pair;

public class ClassManager {
	final private static HashMap<ITypeBinding, ASTNode> classes = new HashMap<ITypeBinding, ASTNode>();

	public static HashMap<ITypeBinding, ASTNode> getClasses() {
		return classes;
	}

	public static void addClass(ITypeBinding name, ASTNode node) {
		classes.put(name, node);
	}
	
	
	final private static HashSet<Pair<ITypeBinding, ITypeBinding>> inheritances = new HashSet<Pair<ITypeBinding, ITypeBinding>>();

	public static HashSet<Pair<ITypeBinding, ITypeBinding>> getInjeritances() {
		return inheritances;
	}

	public static void addInjeritance(ITypeBinding child, ITypeBinding parent) {
		inheritances.add(new Pair<ITypeBinding, ITypeBinding>(child, parent));
	}

	// インスタンスの生成を防ぐ
	private ClassManager() {
	}
}
