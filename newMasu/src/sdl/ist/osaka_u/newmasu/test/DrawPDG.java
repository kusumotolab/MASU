package sdl.ist.osaka_u.newmasu.test;

import java.nio.file.Path;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import sdl.ist.osaka_u.newmasu.dataManager.FileManager;

public class DrawPDG {

	public DrawPDG(){
		
		final ASTParser parser = ASTParser.newParser(AST.JLS4);
		
		for(Entry<Path, CompilationUnit> e : FileManager.getClasses().entrySet()){
			System.out.println("-----------------------");
			System.out.println("file : " + e.getKey());
			

			final Map<String,String> options = JavaCore.getOptions();
			JavaCore.setComplianceOptions(JavaCore.VERSION_1_7, options);
			e.getValue().accept(new visitor());
		}
		
	}
}
