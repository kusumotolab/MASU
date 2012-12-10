package sdl.ist.osaka_u.newmasu.dataManager;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ITypeBinding;

public class ClassManager {
	final private static HashMap<ITypeBinding, ASTNode> classes = new HashMap<ITypeBinding, ASTNode>();

	public static HashMap<ITypeBinding, ASTNode> getClasses() {
		return classes;
	}

	public static void addClass(ITypeBinding name, ASTNode node) {
		classes.put(name, node);
	}

	// インスタンスの生成を防ぐ
	private ClassManager() {
	}
}
