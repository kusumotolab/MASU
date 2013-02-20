package sdl.ist.osaka_u.newmasu.AST;

import java.io.PrintWriter;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import sdl.ist.osaka_u.newmasu.data.ClassInfo;
import sdl.ist.osaka_u.newmasu.data.MethodInfo;
import sdl.ist.osaka_u.newmasu.data.VariableInfo;
import sdl.ist.osaka_u.newmasu.data.dataManager.BindingManager;


public class ClassVisitor extends ASTVisitor {

	PrintWriter pw;

	public ClassVisitor(PrintWriter pw) {
		this.pw = pw;
	}

	public boolean visit(TypeDeclaration node){

		pw.println("*****************************************************");
		pw.println("-------" + node.resolveBinding().getKey() + "-------");

		IBinding binding = node.resolveBinding();
		ClassInfo ci = (ClassInfo) BindingManager.getDeclarationUnit(binding);

		pw.println();
		pw.println("declared field");
		for (VariableInfo info : ci.getFields()) {
			pw.println(info.getName());
			pw.println();
		}

		pw.println();
		pw.println("declared method");
		for (MethodInfo info : ci.getMethods()) {
			pw.println(info.getName());
			pw.println();
		}

		pw.println();
		pw.println("inner class");
		for (ClassInfo info : ci.getInnerClass()) {
			pw.println(info.getName());
			pw.println();
		}

		pw.println();
		pw.println("super class");
		for (ClassInfo info : ci.getSuperClass()) {
			pw.println(info.getName());
			pw.println();
		}

		pw.println();
		pw.println("sub class");
		for (ClassInfo info : ci.getSubClass()) {
			pw.println(info.getName());
			pw.println();
		}

		pw.println();
		pw.println("used class");
		for (ClassInfo info : ci.getUsedClass()) {
			pw.println(info.getName());
			pw.println();
		}

		pw.println();
		pw.println("using class");
		for (ClassInfo info : ci.getUsingClass()) {
			pw.println(info.getName());
			pw.println();
		}
		return true;
	}

}
