package sdl.ist.osaka_u.newmasu.AST;

import java.nio.file.Path;
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import sdl.ist.osaka_u.newmasu.dataManager.AnonymousIDManager;
import sdl.ist.osaka_u.newmasu.dataManager.ClassManager;
import sdl.ist.osaka_u.newmasu.dataManager.FileManager;
import sdl.ist.osaka_u.newmasu.dataManager.MethodManager;
import sdl.ist.osaka_u.newmasu.dataManager.VariableManager;
import sdl.ist.osaka_u.newmasu.util.Output;

/**
 * Visits elements of the AST tree
 * 
 * @author s-kimura
 * 
 */
public class ASTVisitorImpl extends ASTVisitor {

	private CompilationUnit unit = null;
	private Path filePath = null;

	public ASTVisitorImpl(final Path path) {
		this.filePath = path;
	}

	@Override
	public boolean visit(CompilationUnit node) {
		this.unit = node;
		FileManager.addClass(filePath, node);

		return super.visit(node);
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		// String str = getFullQualifiedName(node);
		ITypeBinding bind = node.resolveBinding();
		ClassManager.addClass(bind, node);
		
		ClassManager.addInjeritance(bind, bind.getSuperclass());
		
		ITypeBinding[] interfaces = bind.getInterfaces();
		for(int i=0; i<interfaces.length; i++){
			ClassManager.addInjeritance(bind, interfaces[i]);
		}
		
		return super.visit(node);
	}

	@Override
	public boolean visit(AnonymousClassDeclaration node) {
		// String str = getFullQualifiedName(node);
		ClassManager.addClass(node.resolveBinding(), node);
		return super.visit(node);
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		// String str = getFullQualifiedName(node);
		MethodManager.addMethod(node.resolveBinding(), node);
		return super.visit(node);
	}

	@Override
	public boolean visit(SingleVariableDeclaration node) {
		// String str = getFullQualifiedName(node);
		IBinding nameBinding = node.getName().resolveBinding();
		VariableManager.addVariableDec(nameBinding, node);

		// visit expression
		if (node.getInitializer() != null)
			node.getInitializer().accept(this);

		return false;
	}

	@Override
	public boolean visit(MethodInvocation node) {
		IMethodBinding binding = node.resolveMethodBinding();
		
		
		MethodManager.rel.AddRelation(node, binding);
		
		
		if (binding == null) {
			Output.cannotResolve(node.getName().getFullyQualifiedName());
		} else {
			MethodManager.addRelation(node.resolveMethodBinding(), binding);
		}
		return super.visit(node);
	}

	// /// Variables

	@Override
	public boolean visit(VariableDeclarationFragment node) {
		// String str = getFullQualifiedName(node);
		IBinding nameBinding = node.getName().resolveBinding();
		VariableManager.addVariableDec(nameBinding, node.getParent());

		// visit expression
		if (node.getInitializer() != null)
			node.getInitializer().accept(this);

		return false;
	}

	@Override
	public boolean visit(SimpleName node) {
		IBinding binding = node.resolveBinding();
		if (binding == null) {
			Output.cannotResolve(node.getFullyQualifiedName());
		} else {
			VariableManager.addVariableUse(node, binding);
		}
		return super.visit(node);
	}

	// ////////////////////////////////////////////
	private String getFullQualifiedName(final ASTNode __node) {

		ASTNode tmpNode = __node;

		// 内部クラスなどの問題を解決するため，一度リストに格納する
		final ArrayList<IBinding> bindList = new ArrayList<IBinding>();

		while (true) {

			if (tmpNode == null)
				break;

			switch (tmpNode.getNodeType()) {
			case ASTNode.ANONYMOUS_CLASS_DECLARATION: {
				final AnonymousClassDeclaration anon = (AnonymousClassDeclaration) tmpNode;
				final ITypeBinding binding = anon.resolveBinding();
				if (binding != null)
					bindList.add(binding);
				else
					Output.cannotResolve("Anonymous");

				break;
			}

			case ASTNode.METHOD_DECLARATION: {
				final MethodDeclaration md = (MethodDeclaration) tmpNode;
				final IMethodBinding binding = md.resolveBinding();
				if (binding != null)
					bindList.add(binding);
				else
					Output.cannotResolve(md.getName().getFullyQualifiedName());

				break;
			}

			case ASTNode.TYPE_DECLARATION: {
				final TypeDeclaration type = (TypeDeclaration) tmpNode;
				final ITypeBinding binding = type.resolveBinding();

				if (binding != null)
					bindList.add(binding);
				else
					Output.cannotResolve(type.getName().getFullyQualifiedName());

				break;
			}

			case ASTNode.VARIABLE_DECLARATION_FRAGMENT: {
				final VariableDeclarationFragment type = (VariableDeclarationFragment) tmpNode;
				final IVariableBinding binding = type.resolveBinding();

				if (binding != null)
					bindList.add(binding);
				else
					Output.cannotResolve(type.getName().getFullyQualifiedName());

				break;
			}

			default:
				// None
				break;
			}

			tmpNode = tmpNode.getParent();
		}

		return getNameFromBindingList(bindList);
	}

	private String getFullQualifiedName(final IBinding bind) {
		// 内部クラスなどの問題を解決するため，一度リストに格納する
		final ArrayList<IBinding> bindList = new ArrayList<IBinding>();
		recursiveName(bind, bindList);

		return getNameFromBindingList(bindList);
	}

	private void recursiveName(final IBinding bind,
			final ArrayList<IBinding> bindList) {
		if (bind == null)
			return;

		switch (bind.getKind()) {
		case IBinding.TYPE: {
			final ITypeBinding type = (ITypeBinding) bind;
			bindList.add(type);
			recursiveName(type.getDeclaringClass(), bindList);
			recursiveName(type.getDeclaringMethod(), bindList);
			break;
		}

		case IBinding.METHOD: {
			final IMethodBinding mbind = (IMethodBinding) bind;
			bindList.add(mbind);
			recursiveName(mbind.getDeclaringClass(), bindList);
			break;
		}

		case IBinding.VARIABLE: {
			final IVariableBinding vbind = (IVariableBinding) bind;

			bindList.add(vbind);
			recursiveName(vbind.getDeclaringClass(), bindList);
			recursiveName(vbind.getDeclaringMethod(), bindList);
			break;
		}

		default:
			// None
			break;
		}

	}

	private String getNameFromBindingList(ArrayList<IBinding> bindList) {
		String str = "";
		for (int i = bindList.size() - 1; 0 <= i; i--) {
			final IBinding bind = bindList.get(i);
			if (i == bindList.size() - 1) { // トップレベルの要素のみ，getQualifiedNameする
				if (bind.getKind() != IBinding.TYPE)
					Output.err("Invalid ASTNode order " + bind.getName());
				else {
					final ITypeBinding type = (ITypeBinding) bind;
					str += type.getQualifiedName();
				}
			} else {
				switch (bind.getKind()) {
				case IBinding.TYPE:
					str += ".";

					final ITypeBinding type = (ITypeBinding) bind;
					if (type.isAnonymous())
						str += AnonymousIDManager.getID(type);
					else
						str += bind.getName();
					break;
				case IBinding.METHOD:
					str += "#";

					final IMethodBinding method = (IMethodBinding) bind;
					str += getMethodNameWithParams(method);
					break;
				case IBinding.VARIABLE:
					str += "$";
					str += bind.getName();
					break;
				}
			}
		}
		return str;
	}

	private String getMethodNameWithParams(IMethodBinding binding) {
		StringBuilder buf = new StringBuilder();
		// メソッド名を追加
		buf.append(binding.getName());
		// メソッド名と引数一覧の区切り文字を追加 ("(")
		buf.append('(');
		// 引数の型一覧を追加
		ITypeBinding[] params = binding.getParameterTypes();
		if (params.length > 0) {
			buf.append(params[0].getErasure().getQualifiedName());
			for (int i = 1; i < params.length; i++) {
				buf.append(',');
				buf.append(params[i].getErasure().getQualifiedName());
			}
		}
		buf.append(')');

		return buf.toString();
	}

}
