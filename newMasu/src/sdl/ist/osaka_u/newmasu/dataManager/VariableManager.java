package sdl.ist.osaka_u.newmasu.dataManager;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import sdl.ist.osaka_u.newmasu.util.DualMultiMap;
import sdl.ist.osaka_u.newmasu.util.Output;

public class VariableManager {

	// Declaration: name binding - VariableDeclarationExpression

	final public static DualMultiMap<IBinding, VariableDeclarationStatement> dec = new DualMultiMap<>();

	public static DualMultiMap<IBinding, VariableDeclarationStatement> getDec() {
		return dec;
	}

	// Use : name binding - Expression

	final public static DualMultiMap<IBinding, ExpressionStatement> use = new DualMultiMap<>();

	public static DualMultiMap<IBinding, ExpressionStatement> getUse() {
		return use;
	}
	
	// TODO: cannot get Expression for mismatching of type
	public static VariableDeclarationStatement getVDExpression(ASTNode node){
		while(true){
			if(node == null){
				Output.err("cannot get variable declaration node");
				break;
			}
			else if(node.getNodeType()==ASTNode.VARIABLE_DECLARATION_STATEMENT 
//					|| node.getNodeType() == ASTNode.FIELD_DECLARATION
					)
				return (VariableDeclarationStatement)node;
			else
				node = node.getParent();
		}
		return null;
	}
	
	public static ExpressionStatement getExpression(ASTNode node){
		while(true){
			if(node == null){
				Output.err("cannot get expression node");
				break;
			}
			else if(node.getNodeType()==ASTNode.EXPRESSION_STATEMENT)
				return (ExpressionStatement)node;
			else
				node = node.getParent();
		}
		return null;
	}

	// final private static HashMap<IBinding, ASTNode> variableDec = new
	// HashMap<IBinding, ASTNode>();
	//
	// public final static HashMap<IBinding, ASTNode> getVariableDec() {
	// return variableDec;
	// }
	//
	// public final static void addVariableDec(IBinding bind, ASTNode node) {
	// variableDec.put(bind, node);
	// }
	//
	// final private static HashMap<Name, IBinding> variableUse = new
	// HashMap<Name, IBinding>();
	//
	// public final static HashMap<Name, IBinding> getVariableUses() {
	// return variableUse;
	// }
	//
	// public final static void addVariableUse(Name name, IBinding bind) {
	// variableUse.put(name, bind);
	// }
	//
	// // public final static HashMap<IBinding, VariableDeclaration>
	// getVariableInName(IBinding name){
	// // final HashMap<IBinding, VariableDeclaration> result = new
	// HashMap<IBinding, VariableDeclaration>();
	// // for(Map.Entry<IBinding,VariableDeclaration> pair :
	// variableDec.entrySet()){
	// // if(pair.getKey().contains(name + "$"))
	// // result.put(pair.getKey(), pair.getValue());
	// // }
	// // return result;
	// // }

	// インスタンスの生成を防ぐ
	private VariableManager() {
	}
}
