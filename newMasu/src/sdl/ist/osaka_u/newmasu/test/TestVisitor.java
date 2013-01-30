package sdl.ist.osaka_u.newmasu.test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Argument;

import sdl.ist.osaka_u.newmasu.accessor.MethodInfoAccessor;
import sdl.ist.osaka_u.newmasu.accessor.VariableInfoAccessor;
import sdl.ist.osaka_u.newmasu.dataManager.BindingManager;
import sdl.ist.osaka_u.newmasu.util.NodeFinder;

public class TestVisitor extends ASTVisitor {
	String name;

	// public boolean visit(TypeDeclaration node) {
	//
	// name = node.getName().toString();
	// Set<ASTNode> set = new HashSet<ASTNode>();
	//
	// System.out.println("******************************");
	// System.out.println("-------extend " + name + " ------");
	//
	// set = BindingManager.getExtendedClass(node);
	//
	// for (ASTNode ast : set) {
	// System.out.println(ast.toString());
	// }
	//
	// System.out.println();
	//
	// System.out.println("******************************");
	// System.out.println("-------" + name + " extends------");
	//
	// set = BindingManager.getExtendingClass(node);
	//
	// for (ASTNode ast : set) {
	// System.out.println(ast.toString());
	// }
	//
	// System.out.println();
	//
	// System.out.println("******************************");
	// System.out.println("-------inner " + name + "------");
	//
	// set = BindingManager.getInnerClass(node);
	//
	// for (ASTNode ast : set) {
	// System.out.println(ast.toString());
	// }
	//
	// System.out.println();
	//
	//
	// return true;
	// }

	public boolean visit(MethodDeclaration node) {

		System.out.println("******************************");

		// StringBuilder sb = new StringBuilder();
		// sb.append(node.getName().toString());
		// sb.append("(");
		// if (node.parameters() != null) {
		// for (Object o : node.parameters()) {
		// sb.append(o.toString());
		// sb.append(",");
		// }
		// if (node.parameters().size() > 0)
		// sb.deleteCharAt(sb.length() - 1);
		// }
		// sb.append(")");
		// name = sb.toString();
		//
		// System.out.println("-------call " + name + "------");
		//
		// for (ASTNode ast : BindingManager.getCalleeMethods(node)) {
		// System.out.println(ast.toString());
		// }
		//
		// System.out.println("-------" + name + " call------");
		//
		// for (ASTNode ast : BindingManager.getCallerMethods(node)) {
		// System.out.println(ast.toString());
		// }
		//
		// System.out.println("-------" + name + "------");
		// System.out.println("++overriding++");
		// for (ASTNode ast : BindingManager.getOverridingMethod(node)) {
		// System.out.println(ast.toString());
		// }
		//
		// System.out.println("++overrided++");
		// for (ASTNode ast : BindingManager.getOverridedMethod(node)) {
		// System.out.println(ast.toString());
		// }

		System.out.println("-------" + node.getName() + "------");

		for (ASTNode ast : MethodInfoAccessor.getAssignedFields(node)){
			SimpleName vname = (SimpleName) ast;
			System.out.println(VariableInfoAccessor.getDeclaringStatement(vname));
		}


//		List<IVariableBinding> list = (List<IVariableBinding>) node
//				.getProperty("Variable");
//		if (list != null)
//			for (Object o : list) {
//				IVariableBinding vb = (IVariableBinding) o;
//				if (vb.isField()) {
//					for (ASTNode a : BindingManager.getRef().get(vb)) {
//						System.out.println(NodeFinder.getDeclaringNode(a));
//					}
//				} else {
//					ASTNode a = BindingManager.getDec().get(vb);
//					System.out.println(NodeFinder.getDeclaringNode(a));
//
//				}
//
//				System.out.println(vb + " : " + vb.isField());
//			}

		System.out.println();
		return true;
	}

	// public boolean visit(SimpleName node) {
	//
	// IBinding binding = node.resolveBinding();
	// if (binding != null && binding.getKind() == IBinding.VARIABLE) {
	//
	// if (node.isDeclaration()) {
	// System.out.println("******************************");
	// System.out.println("-------use " + node.toString() + "------");
	// for (ASTNode ast : BindingManager.getCalleeVariable(node)) {
	// System.out.println(ast);
	// }
	// } else {
	// System.out.println("******************************");
	// System.out.println("-------" + node.toString()
	// + " declared------");
	// ASTNode ast = BindingManager.getCallerVariable(node);
	// System.out.println(ast);
	// }
	//
	// System.out.println();
	// }
	// return true;
	// }

}
