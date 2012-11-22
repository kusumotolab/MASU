package sdl.ist.osaka_u.newmasu;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * Visits elements of the AST tree
 * 
 * @author s-kimura
 * 
 */
public class ASTVisitorImpl extends ASTVisitor {

	private CompilationUnit unit = null;
	private String filePath = null;

	public ASTVisitorImpl(String path) {
		filePath = path;
	}

	@Override
	public boolean visit(CompilationUnit node) {
		this.unit = node;
		return super.visit(node);
	}

	@Override
	public boolean visit(MethodInvocation node) {
		IMethodBinding bind = node.resolveMethodBinding();
		if (bind == null) {
			System.err.println("Unresolved  " + filePath);
			System.err.println("    " + node);
		} else {
			System.out.println(" " + bind.toString());
			System.out.println("   "
					+ bind.getDeclaringClass().getQualifiedName());
			// System.out.println("t " + bind.getReturnType());
		}
		return super.visit(node);
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		// final ITypeBinding bind = node.resolveBinding();
		// System.out.println(bind.getQualifiedName());
		return super.visit(node);
	}

}
