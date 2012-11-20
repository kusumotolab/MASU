package sdl.ist.osaka_u.newmasu;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;

public class ASTRequestor extends FileASTRequestor {

	@Override
	public void acceptAST(String path, CompilationUnit ast) {
		
		final FileInfo fileInfo = new FileInfo(path);
		DataManager.getInstance().getFileInfoManager()
				.add(fileInfo, Thread.currentThread());
		
		System.out.println(" ** parsing ... " + path);

		TestAstVisitor visitor = new TestAstVisitor(fileInfo);
//		IProblem[] problem = ast.getProblems();
//		if (problem.length != 0) {
//			System.out.println("------ " + problem.length
//					+ " Errors or Warnings ------");
//			for (IProblem p : problem) {
//				System.out.println(p.toString());
//			}
//			System.out.println("---------------------------");
//		}
		ast.accept(visitor);
	}
}
