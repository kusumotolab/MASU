package sdl.ist.osaka_u.newmasu.accessor;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import sdl.ist.osaka_u.newmasu.dataManager.BindingManager;

public class ClassInfoAccessor extends UnitInfoAccessor {

	/**
	 * 対象クラスのスーパークラスを取得する
	 *
	 * @param node
	 *            対象となるクラス
	 * @return スーパークラスのset
	 */
	public static Set<ASTNode> getSuperClass(TypeDeclaration node) {

		return BindingManager.getExtendingClass(node);
	}

	/**
	 * 対象クラスのサブクラスを取得する
	 *
	 * @param node
	 *            対象となるクラス
	 * @return サブクラスのset
	 */
	public static Set<ASTNode> getSubClass(TypeDeclaration node) {

		return BindingManager.getExtendedClass(node);
	}

	/**
	 * 対象クラスが参照しているクラスのノードを取得する
	 *
	 * @param node
	 *            対象となるクラス
	 * @return 対象クラスが参照しているクラスのset
	 */
	public static Set<ASTNode> getUsingClass(TypeDeclaration node) {

		Set<ASTNode> set = new HashSet<ASTNode>();
		ITypeBinding binding = node.resolveBinding();

		if(binding != null){
			for (ITypeBinding tBinding : binding.getDeclaredTypes()){
				ASTNode ast = BindingManager.getDec().get(tBinding);
				set.add(ast);
			}
		}

		return set;
	}

	/**
	 * 対象クラスを参照しているクラスのノードを取得する
	 *
	 * @param node
	 *            対象となるクラス
	 * @return 対象クラスを参照しているクラスのset
	 */
	public static Set<ASTNode> getUsedClass(TypeDeclaration node) {

		Set<ASTNode> set = new HashSet<ASTNode>();
		ITypeBinding binding = node.resolveBinding();

		if(binding != null){
			for(ASTNode ast : BindingManager.getRef().get(binding)){
				set.add(ast);
			}
		}

		return null;
	}

	/**
	 * 対象クラスのインナークラスのノードを取得する
	 *
	 * @param node
	 *            対象となるクラス
	 * @return インナークラスのset
	 */
	public static Set<ASTNode> getInnerClass(TypeDeclaration node) {

		return BindingManager.getInnerClass(node);
	}

	/**
	 * 対象クラスで定義されているメソッドのノードを取得する
	 *
	 * @param node
	 *            対象となるクラス
	 * @return 定義されているメソッドのset
	 */
	public static Set<ASTNode> getDeclaredMethod(TypeDeclaration node) {

		Set<ASTNode> set = new HashSet<ASTNode>();
		ITypeBinding tBinding = node.resolveBinding();

		if (tBinding != null) {
			for (IMethodBinding mBinding : tBinding.getDeclaredMethods()) {
				set.add(BindingManager.getDec().get(mBinding));
			}
		}

		return set;
	}

	/**
	 * 対象クラスで定義されているフィールドのノードを取得する
	 *
	 * @param node
	 *            対象となるクラス
	 * @return 定義されているフィールドのset
	 */
	public static Set<ASTNode> getDeclaredField(TypeDeclaration node) {

		Set<ASTNode> set = new HashSet<ASTNode>();
		ITypeBinding tBinding = node.resolveBinding();

		if (tBinding != null) {
			for (IVariableBinding vBinding : tBinding.getDeclaredFields()) {
				set.add(BindingManager.getDec().get(vBinding));
			}
		}

		return set;
	}

	private ClassInfoAccessor() {

	}
}
