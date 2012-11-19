package sdl.ist.osaka_u.newmasu;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;


public class TestAstVisitor extends ASTVisitor {

	public boolean visit(MethodInvocation node){

		IMethodBinding binding = node.resolveMethodBinding();
		if (binding == null) {
			System.out.println("binding(MD) = null");
		} else {
			System.out.println(binding.getMethodDeclaration().toString());
		}

		return super.visit(node);
	}


	public  boolean visit(MethodDeclaration node){

		IMethodBinding binding = node.resolveBinding();
		System.out.println("MethodDeclaration");
		if (binding == null) {
			System.out.println("binding(MD) = null");
		} else {
			System.out.println(binding.getKey());
		}
		System.out.println();

		return super.visit(node);
	}


}
