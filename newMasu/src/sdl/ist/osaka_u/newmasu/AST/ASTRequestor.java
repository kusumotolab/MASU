package sdl.ist.osaka_u.newmasu.AST;

import java.util.HashSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;

import sdl.ist.osaka_u.newmasu.dataManager.CallHierachy;
import sdl.ist.osaka_u.newmasu.util.Pair;

public class ASTRequestor extends FileASTRequestor {
	

	final CallHierachy callHierachy = new CallHierachy();


	@Override
	public void acceptAST(String path, CompilationUnit ast) {
		
		final FileInfo fileInfo = new FileInfo(path);
		DataManager.getInstance().getFileInfoManager()
				.add(fileInfo, Thread.currentThread());
		
		System.out.println(" ** parsing ... " + path);

		ASTVisitorImpl visitor = new ASTVisitorImpl(path, callHierachy);
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
		

		System.out.println("----------------------------------");
		HashSet<Pair<String, String>> set = callHierachy.getRelations();
		for( Pair<String, String> p : set )
		{
			System.out.println(p);
		}
		System.out.println("----------------------------------");
	}
}
