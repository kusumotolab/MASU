package sdl.ist.osaka_u.newmasu.AST;

import java.nio.file.Paths;

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;

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

	}
}
