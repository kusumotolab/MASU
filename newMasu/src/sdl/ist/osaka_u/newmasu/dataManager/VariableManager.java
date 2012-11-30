package sdl.ist.osaka_u.newmasu.dataManager;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.VariableDeclaration;

public class VariableManager {
	final private static HashMap<String, VariableDeclaration> variables = new HashMap<String, VariableDeclaration>();

	public final static HashMap<String, VariableDeclaration> getVariables() {
		return variables;
	}

	public final static void addVariable(String name, VariableDeclaration node) {
		variables.put(name, node);
	}

	// インスタンスの生成を防ぐ
	private VariableManager(){}
}
