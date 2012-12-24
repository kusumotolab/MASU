package sdl.ist.osaka_u.newmasu.dataManager;

import java.nio.file.Path;

import org.eclipse.jdt.core.dom.CompilationUnit;

import sdl.ist.osaka_u.newmasu.util.DualHashMap;

public class FileManager {

	final public static DualHashMap<Path, CompilationUnit> rel = new DualHashMap<Path, CompilationUnit>();

	public static int getLOC(Path path) {
		CompilationUnit unit = rel.getCallerMap().get(path);
		return unit.getLineNumber(unit.getLength() - 1) + 1;
	}

	// インスタンスの生成を防ぐ
	private FileManager() {
	}
}
