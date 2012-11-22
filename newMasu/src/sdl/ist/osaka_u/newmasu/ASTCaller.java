package sdl.ist.osaka_u.newmasu;

import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;

/**
 * Applies AST parser to given java file
 * 
 * @author s-kimura
 * 
 */
public class ASTCaller {

	final ASTParser parser = ASTParser.newParser(AST.JLS4);

	public ASTCaller() {

		final Set<String> dirPaths = Settings.getInstance().getTargetDirectories();
		final List<String> jarPaths = Settings.getInstance().getLibraries();
		final Set<String> javaPaths = Settings.getInstance().getListFiles();
	
		final String[] sourcePaths = dirPaths.toArray(new String[dirPaths.size()]);
		final String[] classPaths = jarPaths.toArray(new String[jarPaths.size()]);
		final String[] sources = javaPaths.toArray(new String[javaPaths.size()]);
		
		final Map<String,String> options = JavaCore.getOptions();
		JavaCore.setComplianceOptions(JavaCore.VERSION_1_7, options);
		parser.setCompilerOptions(options);
		parser.setResolveBindings(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setBindingsRecovery(true);
		parser.setStatementsRecovery(true);
		parser.setEnvironment(classPaths, sourcePaths, null, false);

		parser.createASTs(sources, null, new String[] {}, new ASTRequestor(),
				new NullProgressMonitor());
	}
}
