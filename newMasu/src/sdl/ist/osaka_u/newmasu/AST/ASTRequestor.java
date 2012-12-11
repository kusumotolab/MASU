package sdl.ist.osaka_u.newmasu.AST;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;
import org.eclipse.jdt.core.dom.ITypeBinding;

import sdl.ist.osaka_u.newmasu.dataManager.ClassManager;
import sdl.ist.osaka_u.newmasu.util.Pair;

public class ASTRequestor extends FileASTRequestor {

	@Override
	public void acceptAST(String path, CompilationUnit ast) {

		System.out.println(" ** parsing -  " + path);

		ASTVisitorImpl visitor = new ASTVisitorImpl(Paths.get(path));
		IProblem[] problem = ast.getProblems();
		if (problem.length != 0) {
			System.out.println("------ " + problem.length
					+ " Errors and Warnings ------");
			for (IProblem p : problem) {
				if (p.isError())
					System.out.println(p.toString());
			}
			System.out.println("---------------------------");
		}
		ast.accept(visitor);

		// System.out.println("----------------------------------");
		// HashSet<Pair<String, String>> set = MethodManager.getRelations();
		// for( Pair<String, String> p : set )
		// {
		// System.out.println(p);
		// }
		// System.out.println("----------------------------------");

		System.out.println("----------------------------------");
		HashMap<ITypeBinding, ASTNode> set = ClassManager.getClasses();
		for (Map.Entry<ITypeBinding, ASTNode> p : set.entrySet()) {
			System.out.println("-----" + p.getKey());
		}
		
		System.out.println("----------------------------------");
		HashSet<Pair<ITypeBinding, ITypeBinding>> set2 = ClassManager.getInjeritances();
		for (Pair<ITypeBinding, ITypeBinding> p : set2) {
			System.out.println("1: " + p.getFirst());
			System.out.println("2: " + p.getSecond());
		}

	}
}
