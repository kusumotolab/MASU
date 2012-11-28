package sdl.ist.osaka_u.newmasu.AST;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import sdl.ist.osaka_u.newmasu.dataManager.AnonymousIDManager;
import sdl.ist.osaka_u.newmasu.dataManager.CallHierachy;
import sdl.ist.osaka_u.newmasu.util.Pair;

/**
 * Visits elements of the AST tree
 * 
 * @author s-kimura
 * 
 */
public class ASTVisitorImpl extends ASTVisitor {

	private CompilationUnit unit = null;
	private String filePath = null;
	private CallHierachy callHierachy = null;

	public ASTVisitorImpl(String path, CallHierachy callHierachy) {
		this.filePath = path;
		this.callHierachy = callHierachy;
	}

	@Override
	public boolean visit(CompilationUnit node) {
		this.unit = node;

		// System.out.println("[[[ " + node.);

		return super.visit(node);
	}

	@Override
	public boolean visit(MethodInvocation node) {

		IMethodBinding binding = node.resolveMethodBinding();

		if (binding == null) {
			System.err.println("Unresolved bindings : " + node);
		} else {
			callHierachy.addRelation(getFullQualifiedNameFromBinding(binding),
					getFullQualifiedName(node));
		}

		return super.visit(node);
	}

	@Override
	public boolean visit(TypeDeclaration node) {

		//
		// final ITypeBinding bind = node.resolveBinding();
		// System.out.println(bind.getQualifiedName());

		return super.visit(node);
	}

	@Override
	public boolean visit(AnonymousClassDeclaration node) {
		// ITypeBinding binding = node.resolveBinding();
		// IMethodBinding mBind = null;
		//
		// String name = "";
		// while (binding != null) {
		// String className = binding.getQualifiedName().equals("") ? "."
		// + binding.getName() : binding.getQualifiedName();
		// if (binding.isAnonymous()) {
		// className = "." + AnonymousIDManager.getID(binding);
		// }
		// name = className + name;
		//
		// mBind = binding.getDeclaringMethod();
		// if (mBind != null)
		// name = "#" + mBind.getName() + name;
		//
		// binding = binding.getDeclaringClass();
		// }
		//
		// System.out.println(name);
		// //
		// // final ITypeBinding bind = node.resolveBinding();
		// // System.out.println(bind.getQualifiedName());

//		String str = getFullQualifiedName(node, "");
//		System.out.println(str);

		return super.visit(node);
	}
	

	@Override
	public boolean visit(MethodDeclaration node) {
		String str = getFullQualifiedName(node, "");
		System.out.println(str);
		
		return super.visit(node);
	}

	// ////////////////////////////////////////////////////////

	private String getFullQualifiedName(ASTNode node, String str) {

		if (node == null)
			return str;

		switch (node.getNodeType()) {
		case ASTNode.ANONYMOUS_CLASS_DECLARATION: {
			AnonymousClassDeclaration anon = (AnonymousClassDeclaration) node;
			ITypeBinding binding = anon.resolveBinding();
			if (binding != null)
				str = "." + AnonymousIDManager.getID(binding) + str;
			break;
		}

		case ASTNode.METHOD_DECLARATION: {
			MethodDeclaration md = (MethodDeclaration) node;
			IMethodBinding binding = md.resolveBinding();
			if (binding != null)
				str = "#" + binding.getName() + str;
			break;
		}

		case ASTNode.TYPE_DECLARATION: {
			TypeDeclaration type = (TypeDeclaration) node;
			ITypeBinding binding = type.resolveBinding();

			// 匿名クラス内のクラスはgetQualifiedName()が""になるので，getName()で取得
			if (binding != null)
				str = binding.getQualifiedName().equals("") ? "."
						+ binding.getName() : binding.getQualifiedName() + str;
			break;
		}

		default:
			// None
			break;
		}

		return getFullQualifiedName(node.getParent(), str);
	}

	private String getFullQualifiedName(MethodDeclaration node) {
		IMethodBinding bind = node.resolveBinding();
		if (bind == null) {
			System.err.println("Unable to get parent method bindings");
			return null;
		}
		return getFullQualifiedNameFromBinding(bind);
	}

	private String getFullQualifiedName(AnonymousClassDeclaration node) {
		ITypeBinding binding = node.resolveBinding();
		IMethodBinding mBind = null;

		String name = "";
		while (binding != null) {
			String className = binding.getQualifiedName().equals("") ? "."
					+ binding.getName() : binding.getQualifiedName();
			if (binding.isAnonymous()) {
				className = "." + AnonymousIDManager.getID(binding);
			}
			name = className + name;

			mBind = binding.getDeclaringMethod();
			if (mBind != null)
				name = "#" + mBind.getName() + name;

			binding = binding.getDeclaringClass();
		}

		System.out.println(name);
		return name;
	}

	// private String getFullQualifiedName(TypeDeclaration node) {
	//
	// ITypeBinding bind = node.resolveBinding();
	//
	// if (bind == null) {
	// if (bind.isAnonymous()) {
	// if (bind)
	// return getFullQualifiedName(bind);
	// } else if (bind.isInterface()) {
	// return bind.getQualifiedName();
	// } else { // enumとかも
	// return bind.getQualifiedName();
	// }
	// }
	// return node.getName();
	// }

	/**
	 * ASTNodeの完全修飾名を取得
	 */
	private String getFullQualifiedName(ASTNode element) {
		ASTNode node = element;
		while (true) {
			node = node.getParent();
			if (node == null) {
				System.err.println("Unable to get parent method");
				return null;
			}

			else if (node.getNodeType() == ASTNode.ANNOTATION_TYPE_DECLARATION) {

			}

			else if (node.getNodeType() == ASTNode.TYPE_DECLARATION) {
				TypeDeclaration td = (TypeDeclaration) node;
				ITypeBinding bind = td.resolveBinding();
				return bind.getQualifiedName();
			}

			else if (node.getNodeType() == ASTNode.METHOD_DECLARATION) {
				MethodDeclaration md = (MethodDeclaration) node;
				IMethodBinding bind = md.resolveBinding();
				if (bind == null) {
					System.err.println("Unable to get parent method bindings");
					return null;
				}
				return getFullQualifiedNameFromBinding(bind);
			}

			else if (node.getNodeType() == ASTNode.FIELD_DECLARATION) {
				FieldDeclaration fd = (FieldDeclaration) node;
				String className = getFullQualifiedName(fd);
				return className + "." + node;
			}
		}
	}

	// /**
	// * ASTNodeの完全修飾名を取得
	// */
	// private String getFullQualifiedName(ASTNode element) {
	// ASTNode node = element;
	// while (true) {
	// node = node.getParent();
	// if (node == null) {
	// System.err.println("Unable to get parent method");
	// return null;
	// } else if (node.getNodeType() == ASTNode.TYPE_DECLARATION) {
	// TypeDeclaration td = (TypeDeclaration) node;
	// ITypeBinding bind = td.resolveBinding();
	// return bind.getQualifiedName();
	// }
	//
	// else if (node.getNodeType() == ASTNode.METHOD_DECLARATION) {
	// MethodDeclaration md = (MethodDeclaration) node;
	// IMethodBinding bind = md.resolveBinding();
	// if (bind == null) {
	// System.err.println("Unable to get parent method bindings");
	// return null;
	// }
	// return getFullQualifiedNameFromBinding(bind);
	// }
	//
	// else if (node.getNodeType() == ASTNode.FIELD_DECLARATION) {
	// FieldDeclaration fd = (FieldDeclaration) node;
	// String className = getFullQualifiedName(fd);
	// return className + "." + node;
	// }
	// }
	// }

	/**
	 * 呼び出し対象のメソッドを一意に特定するキーを生成する <class-name> "#" <method-name> "(" ( <type> (
	 * "," <type> )* )? ")"
	 */
	private String getFullQualifiedNameFromBinding(IMethodBinding binding) {
		StringBuilder buf = new StringBuilder();
		// メソッドを定義しているクラスを取得
		ITypeBinding erasure = binding.getDeclaringClass().getErasure();
		String typeName = erasure.getQualifiedName();
		buf.append(typeName);
		// 型とメソッド名の区切り文字を追加 ("#")
		buf.append('#');
		// メソッド名を追加
		buf.append(binding.getName());
		// メソッド名と引数一覧の区切り文字を追加 ("(")
		buf.append('(');
		// 引数の型一覧を追加
		ITypeBinding[] params = binding.getParameterTypes();
		if (params.length > 0) {
			buf.append(params[0].getErasure().getQualifiedName());
			for (int i = 1; i < params.length; i++) {
				buf.append(',');
				buf.append(params[i].getErasure().getQualifiedName());
			}
		}
		buf.append(')');

		return buf.toString();
	}

}
