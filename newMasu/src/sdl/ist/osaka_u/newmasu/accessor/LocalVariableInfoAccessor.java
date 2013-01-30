package sdl.ist.osaka_u.newmasu.accessor;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.SimpleName;

import sdl.ist.osaka_u.newmasu.dataManager.BindingManager;

public class LocalVariableInfoAccessor extends VariableInfoAccessor {

	/**
	 * 対象となるローカル変数の型を取得する
	 *
	 * @param node
	 * 対象となるローカル変数
	 * @return
	 * 参照型ならば宣言ノード，primitive型ならばPrimitiveTypeノード
	 */
	public static ASTNode getType(SimpleName node){

		IBinding binding = node.resolveBinding();
		if(binding != null){
			if(binding.getKind() == IBinding.VARIABLE){
				IVariableBinding vBinding = (IVariableBinding) binding;
				ITypeBinding tBinding = vBinding.getType();

				if(tBinding.isPrimitive()){
					System.out.println(tBinding);
				}else{

					ASTNode ast = BindingManager.getDec().get(tBinding);

					if(ast != null){
						return ast;
					}else{
						System.err.println("binding to class file");
					}
				}
			}else{
				System.err.println("Not local variable");
			}
		}else{
			System.err.println("binding error in getDeclaringMethod");

		}
		return null;
	}

	/**
	 * 対象となるローカル変数が定義されているメソッドのノードを取得する
	 *
	 * @param node
	 *            対象となるローカル変数
	 * @return 定義されているメソッド
	 */
	public static ASTNode getDeclaringMethod(SimpleName node) {

		IBinding binding = node.resolveBinding();

		if (binding != null) {
			if (binding.getKind() == IBinding.VARIABLE) {

				IVariableBinding vBinding = (IVariableBinding) binding;
				if (!vBinding.isField()) {
					IMethodBinding mBinding = vBinding.getDeclaringMethod();
					return BindingManager.getDec().get(mBinding);
				}
			}

			System.err.println("Not local variable");
			return null;
		}

		System.err.println("binding error in getDeclaringMethod");
		return null;
	}

	/**
	 * 対象となるローカル変数が参照されている文のノードを取得する
	 *
	 * @param node
	 *            対象となるローカル変数
	 * @return 参照されている文のノード
	 */
	public static Set<ASTNode> getUsedStatement(SimpleName node) {

		Set<ASTNode> set = new HashSet<ASTNode>();
		IBinding binding = node.resolveBinding();

		if (binding != null)
			if (binding.getKind() == IBinding.VARIABLE) {
				IVariableBinding vb = (IVariableBinding) binding;
				if (!vb.isField()) {
					for(ASTNode ast : BindingManager.getRef().get(vb)){
						Object obj = ast.getProperty("Assignment");
						if (obj == null) {
							set.add(ast);
						}
					}
				} else{
					System.err.println("Not local variable");
				}
			}

		return set;
	}

	/**
	 * 対象となるローカル変数が代入されている文のノードを取得する
	 *
	 * @param node
	 *            対象となるローカル変数
	 * @return 代入されている文のノード
	 */
	public static Set<ASTNode> getAssignedStatement(SimpleName node) {

		Set<ASTNode> set = new HashSet<ASTNode>();
		IBinding binding = node.resolveBinding();

		if (binding != null) {
			if (binding.getKind() == IBinding.VARIABLE) {
				IVariableBinding vBinding = (IVariableBinding) binding;
				if (!vBinding.isField()) {
					for(ASTNode ast : BindingManager.getRef().get(vBinding)){
						Object obj = ast.getProperty("Assignment");
						if (obj != null) {
							set.add(ast);
						}
					}
					ASTNode decNode = BindingManager.getDec().get(vBinding);
					Object obj2 = decNode.getProperty("Assignment");
					if (obj2 != null) {
						set.add(decNode);
					}
				} else{
					System.err.println("Not local variable");
				}
			}
		}

		return set;
	}

	private LocalVariableInfoAccessor() {

	}
}
