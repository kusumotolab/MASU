package sdl.ist.osaka_u.newmasu.dataManager;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.ASTNode;

public class ClassManager {
	final private static HashMap<String, ASTNode> classes = new HashMap<String, ASTNode>();

	public static HashMap<String, ASTNode> getClasses() {
		return classes;
	}

	public static void addClass(String name, ASTNode node) {
		classes.put(name, node);
	}

	// インスタンスの生成を防ぐ
	private ClassManager() {
	}
}
