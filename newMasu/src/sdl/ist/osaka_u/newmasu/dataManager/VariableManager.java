package sdl.ist.osaka_u.newmasu.dataManager;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.Name;

public class VariableManager {
	final private static HashMap<IBinding, ASTNode> variableDec = new HashMap<IBinding, ASTNode>();

	public final static HashMap<IBinding, ASTNode> getVariableDec() {
		return variableDec;
	}

	public final static void addVariableDec(IBinding bind, ASTNode node) {
		variableDec.put(bind, node);
	}
	
	final private static HashMap<Name, IBinding> variableUse = new HashMap<Name, IBinding>();
	
	public final static HashMap<Name, IBinding> getVariableUses() {
		return variableUse;
	}

	public final static void addVariableUse(Name name, IBinding bind) {
		variableUse.put(name, bind);
	}
	
//	public final static HashMap<IBinding, VariableDeclaration> getVariableInName(IBinding name){
//		final HashMap<IBinding, VariableDeclaration> result = new HashMap<IBinding, VariableDeclaration>();
//		for(Map.Entry<IBinding,VariableDeclaration> pair : variableDec.entrySet()){
//			if(pair.getKey().contains(name + "$"))
//				result.put(pair.getKey(), pair.getValue());
//		}
//		return result;
//	}

	// インスタンスの生成を防ぐ
	private VariableManager(){}
}
