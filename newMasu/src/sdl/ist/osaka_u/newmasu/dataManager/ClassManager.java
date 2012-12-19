package sdl.ist.osaka_u.newmasu.dataManager;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import sdl.ist.osaka_u.newmasu.util.IshrSingleMap;
import sdl.ist.osaka_u.newmasu.util.Output;
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
	
	
	
	final public static IshrSingleMap<IBinding, ASTNode, MethodInvocation> rel = new IshrSingleMap<IBinding, ASTNode, MethodInvocation>() {

		@Override
		protected ASTNode getNode(final MethodInvocation node) {
			ASTNode n = node;
			while (n.getNodeType() != ASTNode.METHOD_DECLARATION
					&& n.getNodeType() != ASTNode.TYPE_DECLARATION)
				n = n.getParent();
			return n;
		}

		@Override
		protected IBinding getBind(final ASTNode md) {
			IBinding bind = null;
			if (md.getNodeType() == ASTNode.METHOD_DECLARATION)
				bind = ((MethodDeclaration) md).resolveBinding();
			else if (md.getNodeType() == ASTNode.TYPE_DECLARATION)
				bind = ((TypeDeclaration) md).resolveBinding();
			else
				Output.err("Undefined Node Type in IDM");
			return bind;
		}
	};
	
	

	// インスタンスの生成を防ぐ
	private ClassManager() {
	}
}
