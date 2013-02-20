package sdl.ist.osaka_u.newmasu.util;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ExternalNodeBuilder {

	//
	public static SimpleName createSimpleName(IVariableBinding binding){

		AST ast = AST.newAST(AST.JLS4);
		SimpleName simpleName = ast.newSimpleName(getName(binding.getName()));

		return simpleName;
	}

	public static MethodDeclaration createdMethodDeclaration(IMethodBinding binding){

		AST ast = AST.newAST(AST.JLS4);

		MethodDeclaration methodDeclaration = ast.newMethodDeclaration();
		methodDeclaration.setName(ast.newSimpleName(getName(binding.getName())));

		return methodDeclaration;
	}

//	public static FieldDeclaration createdFieldDeclaration(IMethodBinding binding){
//
//		AST ast = AST.newAST(AST.JLS4);
//
//		VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
//		fragment.setName(ast.newSimpleName(binding.getName()));
//		FieldDeclaration fieldDeclaration = ast.newFieldDeclaration(ast.newVariableDeclarationFragment());
//
//		return fieldDeclaration;
//	}
//
//	public static Initializer createdInitializer(IMethodBinding binding){
//
//		AST ast = AST.newAST(AST.JLS4);
//
//		Initializer initializer = ast.newInitializer();
//
//		return initializer;
//	}
//
////	public static EnumconsDeclaration createdMethodDeclaration(IMethodBinding binding){
////
////		AST ast = AST.newAST(AST.JLS4);
////
////		MethodDeclaration methodDeclaration = ast.newMethodDeclaration();
////		methodDeclaration.setName(ast.newSimpleName(binding.getName()));
////
////		return methodDeclaration;
////	}

	public static TypeDeclaration createTypedeDeclaration(ITypeBinding binding){

		AST ast = AST.newAST(AST.JLS4);

		TypeDeclaration typeDeclaration = ast.newTypeDeclaration();
		String name = binding.getName();
		typeDeclaration.setName(ast.newSimpleName(getName(binding.getName())));

		return typeDeclaration;
	}

	public static EnumDeclaration createEnumDeclaration(ITypeBinding binding){

		AST ast = AST.newAST(AST.JLS4);

		EnumDeclaration enumDeclaration = ast.newEnumDeclaration();
		enumDeclaration.setName(ast.newSimpleName(getName(binding.getName())));

		return enumDeclaration;
	}

	private static String getName(String name){
		String[] split = name.split("<");
		return split[0];
	}


}
