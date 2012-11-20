package sdl.ist.osaka_u.newmasu;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.*;

import org.eclipse.jdt.core.dom.*;

public class TestAstVisitor extends ASTVisitor {

	private TargetClassInfo classInfo = null;
	private CompilationUnit unit = null;
	private FileInfo fileInfo = null;

	public TestAstVisitor(FileInfo fileInfo) {
		this.fileInfo = fileInfo;
	}

	@Override
	public boolean visit(CompilationUnit node) {
		this.unit = node;
		return super.visit(node);
	}

	@Override
	public boolean visit(ImportDeclaration node) {

		return super.visit(node);
	}
	
	@Override
	public boolean visit(AnonymousClassDeclaration node) {

		ITypeBinding bind = node.resolveBinding();
		System.out.println("-- " + bind.getQualifiedName());
		return super.visit(node);
	}
	

	/**
	 * Class or Interface declaration
	 */
	@Override
	public boolean visit(TypeDeclaration node) {
		final ITypeBinding bind = node.resolveBinding();
		if (bind.isNested()) {
			// TODO: Visitorを再帰
			// TODO: isNested()は再帰したときもtrueを返すので、innner classでは無限ループになる
			return false;
		} else {

			classInfo = new TargetClassInfo(getModifiers(node.modifiers()),
					new NamespaceInfo(bind.getQualifiedName().split("\\.")),
					node.getName().getIdentifier(), node.isInterface(),
					false, // setEnum
					fileInfo, unit.getLineNumber(node.getStartPosition()),
					unit.getColumnNumber(node.getStartPosition()) + 1,
					unit.getLineNumber(node.getStartPosition()
							+ node.getLength()), unit.getColumnNumber(node
							.getStartPosition() + node.getLength()) + 1);

			return super.visit(node);
		}
	}

	@Override
	public boolean visit(FieldDeclaration node) {

		final Type type = node.getType();
		// final TypeInfo typeInfo = new TypeInfo();
		createTypeInfo(type);

		final List<VariableDeclarationFragment> fragments = node.fragments();
		for (VariableDeclarationFragment v : fragments) {
			final Set<ModifierInfo> modifiers = getModifiers(node.modifiers());
			final TargetFieldInfo field = new TargetFieldInfo(modifiers, v
					.getName().getIdentifier(), classInfo,
					ModifierInfo.isInstanceMember(modifiers),
					unit.getLineNumber(node.getStartPosition()),
					unit.getColumnNumber(node.getStartPosition()) + 1,
					unit.getLineNumber(node.getStartPosition()
							+ node.getLength()), unit.getColumnNumber(node
							.getStartPosition() + node.getLength()) + 1);

			// field.setInitializer(initializer);
			// field.setType(type);

			classInfo.addDefinedField(field);
		}

		return super.visit(node);

	}

	// @Override
	// public boolean visit(MethodInvocation node) {
	//
	// IMethodBinding binding = node.resolveMethodBinding();
	// if (binding == null) {
	// System.out.println("binding(MD) = null");
	// } else {
	// System.out.println(binding.getMethodDeclaration().toString());
	// }
	//
	// return super.visit(node);
	// }
	//
	// @Override
	// public boolean visit(MethodDeclaration node) {
	//
	// IMethodBinding binding = node.resolveBinding();
	// System.out.println("MethodDeclaration");
	// if (binding == null) {
	// System.out.println("binding(MD) = null");
	// } else {
	// System.out.println(binding.getKey());
	// }
	// System.out.println();
	//
	// return super.visit(node);
	// }

	private Set<ModifierInfo> getModifiers(List<IExtendedModifier> modifiers) {
		final Set<ModifierInfo> setModifiers = new HashSet<ModifierInfo>();
		for (Object mo : modifiers)
			setModifiers.add(JavaPredefinedModifierInfo
					.getModifierInfo(((IExtendedModifier) mo).toString()));
		return setModifiers;
	}

	private TypeInfo createTypeInfo(Type type) {

		if (type.isArrayType()) {
			final ArrayType array = (ArrayType) type;
			return ArrayTypeInfo.getType(
					createTypeInfo(array.getComponentType()),
					array.getDimensions());
		} else if (type.isParameterizedType()) {

		} else if (type.isPrimitiveType()) {
			final PrimitiveType prim = (PrimitiveType) type;
			final String code = prim.toString();

			System.out.println("--- " + code);

			// MASUではvoidだけ別の型なので除外
			if (code.equals("void"))
				return VoidTypeInfo.getInstance();
			else
				return PrimitiveTypeInfo.getType(PrimitiveTypeInfo.TYPE
						.valueOf(code.toUpperCase()));
		} else if (type.isQualifiedType()) {

			//TODO: 何がここくるの？
//			final QualifiedType qual = (QualifiedType) type;
//			System.out.println("--- " + qual.getName());
			System.err.println("Qualified kita---" + type);

		} else if (type.isSimpleType()) {
			final SimpleType sim = (SimpleType) type;
			
			
			

		} else if (type.isUnionType()) {

		} else if (type.isWildcardType()) {

		} else {
			System.err.println("Undefined type" + type);
			return UnknownTypeInfo.getInstance();
		}
		
		return UnknownTypeInfo.getInstance();

	}
	
	

	public final TargetClassInfo getClassInfo() {
		return classInfo;
	}

	public final void setClassInfo(TargetClassInfo classInfo) {
		this.classInfo = classInfo;
	}

}
