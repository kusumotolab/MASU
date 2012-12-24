package sdl.ist.osaka_u.newmasu.test;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import sdl.ist.osaka_u.newmasu.dataManager.VariableManager;

public class DrawPDG {

	public DrawPDG() {

		final ASTParser parser = ASTParser.newParser(AST.JLS4);

		// for(Entry<Path, CompilationUnit> e :
		// FileManager.getClasses().entrySet()){
		// System.out.println("-----------------------");
		// System.out.println("file : " + e.getKey());
		//
		//
		// final Map<String,String> options = JavaCore.getOptions();
		// JavaCore.setComplianceOptions(JavaCore.VERSION_1_7, options);
		// e.getValue().accept(new visitor());
		// }

		System.out.println("----------------------------------");
		Set<Entry<VariableDeclarationStatement, Collection<IBinding>>> set2 = VariableManager.dec
				.getCalleeMap().entrySet();
		for (Entry<VariableDeclarationStatement, Collection<IBinding>> p : set2) {
			System.out.println("1: " + p.getKey());
			System.out.println("2: " + p.getValue());
			for (IBinding n : p.getValue())
				System.out.println((n).getName());
		}

	}
}
