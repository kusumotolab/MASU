package sdl.ist.osaka_u.newmasu;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import sdl.ist.osaka_u.newmasu.callhierachy.CallHierachy;
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
	

	final CallHierachy callHierachy = new CallHierachy();

	public ASTVisitorImpl(String path) {
		filePath = path;
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
		
		if( binding == null ) {
			System.err.println("Unresolved bindings : " + node );
		}
		else{
			callHierachy.addRelation(getFullQualifiedNameFromBinding(binding), getParentDeclaration(node));
		}

		return super.visit(node);
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		// final ITypeBinding bind = node.resolveBinding();
		// System.out.println(bind.getQualifiedName());
		return super.visit(node);
	}

	/**
	 * MethodDeclaration型か，FieldDeclaration型になるまでさかのぼる
	 */
	private String getParentDeclaration(ASTNode element) {
		ASTNode node = element;
		while (true) {
			node = node.getParent();
			if (node == null) {
				System.err.println("Unable to get parent method");
				return null;
			} else if (node.getNodeType() == ASTNode.TYPE_DECLARATION) {
				TypeDeclaration td = (TypeDeclaration) node;
				ITypeBinding bind = td.resolveBinding();
				return bind.getQualifiedName();
			} else if (node.getNodeType() == ASTNode.METHOD_DECLARATION) {
				MethodDeclaration md = (MethodDeclaration) node;
				IMethodBinding bind = md.resolveBinding();
				if (bind == null) {
					System.err.println("Unable to get parent method bindings");
					return null;
				}
				return getFullQualifiedNameFromBinding(bind);
			} else if (node.getNodeType() == ASTNode.FIELD_DECLARATION) {
				FieldDeclaration fd = (FieldDeclaration) node;
				String className = getParentDeclaration(fd);
				return className + "." + node;
			}
		}
	}

	/**
	 * 呼び出し対象のメソッドを一意に特定するキーを生成する <class-name> "#" <method-name> "(" ( <type> (
	 * "," <type> )* )? ")"
	 */
	private String getFullQualifiedNameFromBinding(IMethodBinding binding) {
		StringBuilder buf = new StringBuilder();
		// メソッドを宣言する型を文字列へ変換して追加
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
