package sdl.ist.osaka_u.newmasu.test;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;

/**
 * Visits elements of the AST tree
 * 
 * @author s-kimura
 * 
 */
public class visitor extends ASTVisitor {


	@Override
	public boolean visit(ExpressionStatement node) {

		System.out.println("exp :: " + node);
		
		return super.visit(node);
	}
	
	@Override
	public boolean visit(MethodDeclaration node) {
		System.out.println("---- Method : " + node.getName() + " ---------");		
		return super.visit(node);
	}
	
	@Override
	public void endVisit(MethodDeclaration node) {
			System.out.println("---- EndMethod : " + node.getName() + " ---------");	
	}

}
