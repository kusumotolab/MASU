package sdl.ist.osaka_u.newmasu.dataManager;

import java.nio.file.Path;
import java.util.Map.Entry;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;

import sdl.ist.osaka_u.newmasu.util.DualHashMap;

public class FileManager {

	final public static DualHashMap<Path, CompilationUnit> rel = new DualHashMap<Path, CompilationUnit>();

	public static int getLOC(Path path) {
		CompilationUnit unit = rel.getCallerMap().get(path);
		return unit.getLineNumber(unit.getLength() - 1) + 1;
	}

	public static ASTNode getCallerNode(IMethodBinding binding) {
		if (binding != null) {
			for (Entry<Path, CompilationUnit> e : rel.getCallerMap().entrySet()) {
				ASTNode node = e.getValue().findDeclaringNode(binding);
				if (node != null) {
					return node;
				}
			}
		}
		return null;
	}

	// bindingからpathをみつけたい!!!!!!!
	private static Path getPathFromBinding(IMethodBinding binding) {
		String key = binding.getDeclaringClass().getQualifiedName();
		System.out.println(key);
		return null;
	}

	// インスタンスの生成を防ぐ
	private FileManager() {
	}
}
