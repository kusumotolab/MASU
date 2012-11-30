package sdl.ist.osaka_u.newmasu.dataManager;

import java.nio.file.Path;
import java.util.HashMap;

import org.eclipse.jdt.core.dom.CompilationUnit;

public class FileManager {
	final private static HashMap<Path, CompilationUnit> classes = new HashMap<Path, CompilationUnit>();

	public final static HashMap<Path, CompilationUnit> getClasses() {
		return classes;
	}

	public final static void addClass(Path path, CompilationUnit node) {
		classes.put(path, node);
	}
	
	public final static int getLOC(Path path){
//		System.out.println(
//				node.getLineNumber(node.getLength()-1) + 1
//				);
		return 0;
	}

	// インスタンスの生成を防ぐ
	private FileManager() {
	}
}
