package sdl.ist.osaka_u.newmasu.dataManager;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.VariableDeclaration;

public class VariableManager {
	final private static HashMap<String, VariableDeclaration> variables = new HashMap<String, VariableDeclaration>();

	public final static HashMap<String, VariableDeclaration> getVariables() {
		return variables;
	}

	public final static void addVariable(String name, VariableDeclaration node) {
		variables.put(name, node);
	}
	
	public final static HashMap<String, VariableDeclaration> getVariableInName(String name){
		final HashMap<String, VariableDeclaration> result = new HashMap<String, VariableDeclaration>();
		for(Map.Entry<String,VariableDeclaration> pair : variables.entrySet()){
			if(pair.getKey().contains(name + "$"))
				result.put(pair.getKey(), pair.getValue());
		}
		return result;
	}

	// インスタンスの生成を防ぐ
	private VariableManager(){}
}
