package sdl.ist.osaka_u.newmasu;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VoidTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMethodInfo;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * Visits elements of the AST tree
 *
 * @author s-kimura
 *
 */
public class ASTVisitorImpl extends ASTVisitor {

	private CompilationUnit unit = null;

	public ASTVisitorImpl(CompilationUnit unit) {
		this.unit = unit;
	}

	private String packageName = null;
	private String className = null;
	private UnresolvedClassInfo classInfo = null;

	@Override
	public boolean visit(PackageDeclaration elem) {
		packageName = elem.getName().toString();
		return super.visit(elem);
	}

	private String[] getFullQualifiedName() {

		String fullQualifiedName = packageName + "." + className;
		return fullQualifiedName.split(".");
	}

	@Override
	public boolean visit(TypeDeclaration elem) {
		className = elem.getName().toString();

		if (elem.isInterface()) {

		} else {

			final FileInfo fileInfo = new FileInfo("./target/Main.java");

			classInfo = new UnresolvedClassInfo(fileInfo, null);
			classInfo.setNamespace(packageName.split("."));
			classInfo.setClassName(className);

			// final NamespaceInfo nameSpaceInfo = new
			// NamespaceInfo(packageName.split("."));
			//
			// classInfo = new TargetClassInfo(
			// new HashSet<ModifierInfo>(),
			// nameSpaceInfo,
			// className,
			// false,
			// false,
			// fileInfo,
			// unit.getLineNumber(elem.getStartPosition()),
			// 0,
			// unit.getLineNumber(elem.getStartPosition() + elem.getLength()),
			// 0);
			//
			// DataManager.getInstance().getClassInfoManager().add(classInfo);

		}

		return super.visit(elem);
	}

	@Override
	public boolean visit(MethodDeclaration elem) {
		
		IMethodBinding binding = elem.resolveBinding();
		if (binding == null) {
			System.out.println("binding(MD) = null");
		} else {
			System.out.println(binding.getMethodDeclaration().toString());
		}


		UnresolvedMethodInfo methodInfo = new UnresolvedMethodInfo(classInfo,
				unit.getLineNumber(elem.getStartPosition()), 0,
				unit.getLineNumber(elem.getStartPosition() + elem.getLength()),
				0);
		methodInfo.setMethodName(elem.getName().toString());

		methodInfo.setReturnType(VoidTypeInfo.getInstance());

		// final TargetMethodInfo methodInfo = new TargetMethodInfo(
		// new HashSet<ModifierInfo>(),
		// elem.getName().toString(),
		// false,
		// unit.getLineNumber(elem.getStartPosition()),
		// 0,
		// unit.getLineNumber(elem.getStartPosition() + elem.getLength()),
		// 0);
		//
		// methodInfo.setOuterUnit(classInfo);

		classInfo.addDefinedMethod(methodInfo);

		// DataManager.getInstance().getMethodInfoManager().add(methodInfo);

		return super.visit(elem);
	}

	@Override
	public void endVisit(TypeDeclaration elem) {

		DataManager.getInstance().getUnresolvedClassInfoManager()
				.addClass(classInfo);
	}

	@Override
	public boolean visit(MethodInvocation node) {

		IMethodBinding binding = node.resolveMethodBinding();
		if (binding == null) {
			System.out.println("binding = null");
		} else {
			System.out.println(binding.getMethodDeclaration().toString());
		}

		return super.visit(node);
	}

}
