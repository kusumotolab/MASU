package sdl.ist.osaka_u.newmasu.accessor;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.SimpleName;

import sdl.ist.osaka_u.newmasu.dataManager.BindingManager;
import sdl.ist.osaka_u.newmasu.util.NodeFinder;

public class FieldInfoAccessor extends VariableInfoAccessor {

	/**
	 * 対象となるフィールド変数が定義されているクラスのノードを取得する
	 *
	 * @param node
	 *            対象となるフィールド変数
	 * @return 定義されているクラスのノード
	 */
	public static ASTNode getDeclaringClass(SimpleName node) {

		IBinding binding = node.resolveBinding();

		if (binding != null) {
			if (binding.getKind() == IBinding.VARIABLE) {
				IVariableBinding vb = (IVariableBinding) binding;
				if (vb.isField()) {

					ITypeBinding tBinding = vb.getDeclaringClass();

					return BindingManager.getDec().get(tBinding);
				} else {
					System.err.println("Not field");
				}
			}
		} else {
			System.err.println("binding error in getDeclaringClass");
		}

		return null;

	}

	/**
	 * 対象となるフィールド変数が参照されているメソッドのノードを取得する
	 *
	 * @param node
	 *            対象となるフィールド変数
	 * @return 参照されているメソッドのノード
	 */
	public static Set<ASTNode> getUsedMethod(SimpleName node) {

		Set<ASTNode> set = new HashSet<ASTNode>();
		IBinding binding = node.resolveBinding();

		if (binding != null)
			if (binding.getKind() == IBinding.VARIABLE) {
				IVariableBinding vb = (IVariableBinding) binding;
				if (vb.isField()) {
					for (ASTNode ast : BindingManager.getRef().get(vb)) {
						Object obj = ast.getProperty("Assignment");
						if (obj == null) {
							set.add(NodeFinder.getMethodNode(ast));
						}
					}
				} else {
					System.err.println("Not field");
				}
			}

		return set;
	}

	/**
	 * 対象となるフィールド変数が代入されているメソッドのノードを取得する
	 *
	 * @param node
	 *            対象となるフィールド変数
	 * @return 参照されているメソッドのノード
	 */
	public static Set<ASTNode> getAssignedMethod(SimpleName node) {

		Set<ASTNode> set = new HashSet<ASTNode>();
		IBinding binding = node.resolveBinding();

		if (binding != null)
			if (binding.getKind() == IBinding.VARIABLE) {
				IVariableBinding vb = (IVariableBinding) binding;
				if (!vb.isField()) {
					for (ASTNode ast : BindingManager.getRef().get(vb)) {
						Object obj = ast.getProperty("Assignment");
						if (obj != null) {
							set.add(NodeFinder.getMethodNode(ast));
						}
					}
				} else {
					System.err.println("Not field");
				}
			}

		return set;
	}

	private FieldInfoAccessor() {

	}
}
