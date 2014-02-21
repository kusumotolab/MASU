package sdl.ist.osaka_u.newmasu.util;import org.eclipse.jdt.core.dom.ASTNode;import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;import org.eclipse.jdt.core.dom.CompilationUnit;import org.eclipse.jdt.core.dom.EnumDeclaration;import org.eclipse.jdt.core.dom.FieldDeclaration;import org.eclipse.jdt.core.dom.Initializer;import org.eclipse.jdt.core.dom.MethodDeclaration;import org.eclipse.jdt.core.dom.TypeDeclaration;import org.eclipse.jdt.core.dom.VariableDeclaration;import sdl.ist.osaka_u.newmasu.data.AnonymousTypeDeclarationInfo;import sdl.ist.osaka_u.newmasu.data.ClassInfo;import sdl.ist.osaka_u.newmasu.data.EnumDeclarationInfo;import sdl.ist.osaka_u.newmasu.data.FieldDeclarationInfo;import sdl.ist.osaka_u.newmasu.data.InitializerInfo;import sdl.ist.osaka_u.newmasu.data.MethodDeclarationInfo;import sdl.ist.osaka_u.newmasu.data.MethodInfo;import sdl.ist.osaka_u.newmasu.data.TypeDeclarationInfo;import sdl.ist.osaka_u.newmasu.data.UnitInfo;public class NodeFinder {	private NodeFinder() {	}	public static final ASTNode getDeclaringNode(ASTNode node){		if(node == null){			return null;		}		ASTNode ast = node;		while (true) {			switch (ast.getNodeType()) {			case ASTNode.ASSERT_STATEMENT:			case ASTNode.BREAK_STATEMENT:			case ASTNode.CONSTRUCTOR_INVOCATION:			case ASTNode.CONTINUE_STATEMENT:			case ASTNode.DO_STATEMENT:			case ASTNode.EMPTY_STATEMENT:			case ASTNode.ENHANCED_FOR_STATEMENT:			case ASTNode.EXPRESSION_STATEMENT:			case ASTNode.FOR_STATEMENT:			case ASTNode.IF_STATEMENT:			case ASTNode.LABELED_STATEMENT:			case ASTNode.RETURN_STATEMENT:			case ASTNode.SUPER_CONSTRUCTOR_INVOCATION:			case ASTNode.SWITCH_CASE:			case ASTNode.SWITCH_STATEMENT:			case ASTNode.SYNCHRONIZED_STATEMENT:			case ASTNode.THROW_STATEMENT:			case ASTNode.TRY_STATEMENT:			case ASTNode.TYPE_DECLARATION_STATEMENT:			case ASTNode.VARIABLE_DECLARATION_STATEMENT:			case ASTNode.WHILE_STATEMENT:			case ASTNode.METHOD_DECLARATION:			case ASTNode.FIELD_DECLARATION:			case ASTNode.INITIALIZER:			case ASTNode.ENUM_CONSTANT_DECLARATION:			case ASTNode.ENUM_DECLARATION:			case ASTNode.TYPE_DECLARATION:				return ast;			case ASTNode.PACKAGE_DECLARATION:			case ASTNode.IMPORT_DECLARATION:				return null;			}			if (ast.getParent() != null) {				ast = ast.getParent();			} else {				System.err.println("Error Error Error  can't find declared node");				System.err.println(node);				return null;			}		}	}	public static final AbstractTypeDeclaration getTypeNode(ASTNode node) {		ASTNode ast = node;		while (true) {			switch (ast.getNodeType()) {			case ASTNode.TYPE_DECLARATION:                return (TypeDeclaration) ast;			case ASTNode.ENUM_DECLARATION:                return (EnumDeclaration) ast;            }			if (ast.getParent() != null) {				ast = ast.getParent();			} else {				System.err.println("can't resolve node");				return null;			}		}	}	/**	 * 親となるメソッドノード探す	 * @param node	 * @return	 */	public static final ASTNode getMethodNode(ASTNode node) {		ASTNode ast = node;		while (true) {			switch (ast.getNodeType()) {			case ASTNode.METHOD_DECLARATION:			case ASTNode.FIELD_DECLARATION:			case ASTNode.INITIALIZER:				return (ASTNode) ast;			}			if (ast.getParent() != null) {				ast = ast.getParent();			} else {				System.err.println("can't resolve node");				return null;			}		}	}    /**     * 特定の親ノードを見つける．見つからなかったらnull     * 可変長引数を利用して，ジェネリクス型の     */    public static final <T extends ASTNode> T getOneParentNode(ASTNode node, T... t){        Class<T> type = (Class<T>) t.getClass().getComponentType();        System.out.println("node:: " + type.getName());        int nodetype = 0;        try {            nodetype = type.newInstance().getNodeType();        } catch (InstantiationException e) {            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.        } catch (IllegalAccessException e) {            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.        }        ASTNode n = node;        while(n!=null){            if(n.getNodeType() == nodetype)                return (T)n;            n = n.getParent();        }        return null;    }//	public static ASTNode getVariableNode(ASTNode node) {////		ASTNode ast = node;//		while (true) {//			switch (ast.getNodeType()) {//			case ASTNode.ASSERT_STATEMENT://			case ASTNode.BREAK_STATEMENT://			case ASTNode.CONSTRUCTOR_INVOCATION://			case ASTNode.CONTINUE_STATEMENT://			case ASTNode.DO_STATEMENT://			case ASTNode.EMPTY_STATEMENT://			case ASTNode.ENHANCED_FOR_STATEMENT://			case ASTNode.EXPRESSION_STATEMENT://			case ASTNode.FOR_STATEMENT://			case ASTNode.IF_STATEMENT://			case ASTNode.LABELED_STATEMENT://			case ASTNode.RETURN_STATEMENT://			case ASTNode.SUPER_CONSTRUCTOR_INVOCATION://			case ASTNode.SWITCH_CASE://			case ASTNode.SWITCH_STATEMENT://			case ASTNode.SYNCHRONIZED_STATEMENT://			case ASTNode.THROW_STATEMENT://			case ASTNode.TRY_STATEMENT://			case ASTNode.TYPE_DECLARATION_STATEMENT://			case ASTNode.VARIABLE_DECLARATION_STATEMENT://			case ASTNode.WHILE_STATEMENT://			case ASTNode.METHOD_DECLARATION://			case ASTNode.FIELD_DECLARATION://			case ASTNode.INITIALIZER://			case ASTNode.ENUM_DECLARATION://				return ast;//			}////			if (ast.getParent() != null) {//				ast = ast.getParent();//			} else {//				System.err.println("can't resolve node");//				return null;//			}//		}//	}	public static final CompilationUnit getCompilationUnitNode(ASTNode node) {		ASTNode ast = node;		while (true) {			switch (ast.getNodeType()) {			case ASTNode.COMPILATION_UNIT:				return (CompilationUnit) ast;			}			if (ast.getParent() != null) {				ast = ast.getParent();			} else {				System.err.println("can't resolve node");				return null;			}		}	}	public static final VariableDeclaration getDeclaration(ASTNode node){		if(node == null){			return null;		}		ASTNode ast = node;		while (true) {			switch (ast.getNodeType()) {			case ASTNode.SINGLE_VARIABLE_DECLARATION:			case ASTNode.VARIABLE_DECLARATION_FRAGMENT:				return (VariableDeclaration) ast;			}			if (ast.getParent() != null) {				ast = ast.getParent();			} else {				System.err.println("can't resolve node");				System.err.println(node);				return null;			}		}	}	///////////////////////////////////////////////////////////////////////////	///////////////////////////////////////////////////////////////////////////	public static final UnitInfo getDeclaredNodeInfo(ASTNode node){		ASTNode ast = node;		while (true) {			switch (ast.getNodeType()) {			case ASTNode.TYPE_DECLARATION:				return new TypeDeclarationInfo((TypeDeclaration)ast);			case ASTNode.ENUM_DECLARATION:				return new EnumDeclarationInfo((EnumDeclaration)ast);			case ASTNode.ANONYMOUS_CLASS_DECLARATION:				return new AnonymousTypeDeclarationInfo((AnonymousClassDeclaration)ast);			case ASTNode.METHOD_DECLARATION:				return new MethodDeclarationInfo((MethodDeclaration)ast);			case ASTNode.FIELD_DECLARATION:				return new FieldDeclarationInfo((FieldDeclaration)ast);			case ASTNode.INITIALIZER:				return new InitializerInfo((Initializer)ast);			}			if (ast.getParent() != null) {				ast = ast.getParent();			} else {				System.err.println("can't resolve node");				return null;			}		}	}	public static final MethodInfo getMethodInfo(ASTNode node){		ASTNode ast = node;		while (true) {			switch (ast.getNodeType()) {			case ASTNode.METHOD_DECLARATION:				return new MethodDeclarationInfo((MethodDeclaration)ast);			case ASTNode.FIELD_DECLARATION:				return new FieldDeclarationInfo((FieldDeclaration)ast);			case ASTNode.INITIALIZER:				return new InitializerInfo((Initializer)ast);			}			if (ast.getParent() != null) {				ast = ast.getParent();			} else {				System.err.println("can't resolve node");				return null;			}		}	}	public static final ClassInfo getClassInfo(ASTNode node){		ASTNode ast = node;		while (true) {			switch (ast.getNodeType()) {			case ASTNode.TYPE_DECLARATION:				return new TypeDeclarationInfo((TypeDeclaration)ast);			case ASTNode.ENUM_DECLARATION:				return new EnumDeclarationInfo((EnumDeclaration)ast);			case ASTNode.ANONYMOUS_CLASS_DECLARATION:				return new AnonymousTypeDeclarationInfo((AnonymousClassDeclaration)ast);			}			if (ast.getParent() != null) {				ast = ast.getParent();			} else {				System.err.println("can't resolve node");				return null;			}		}	}	/////////////////////////////////////////////////////////////////////////////////////////	public static final ASTNode getDeclaredModule(ASTNode node){		if(node == null){			return null;		}		ASTNode ast = node;		while (true) {			switch (ast.getNodeType()) {			case ASTNode.METHOD_DECLARATION:			case ASTNode.FIELD_DECLARATION:			case ASTNode.INITIALIZER:			case ASTNode.ENUM_CONSTANT_DECLARATION:			case ASTNode.ENUM_DECLARATION:			case ASTNode.TYPE_DECLARATION:			case ASTNode.ANONYMOUS_CLASS_DECLARATION:				return ast;			case ASTNode.PACKAGE_DECLARATION:			case ASTNode.IMPORT_DECLARATION:				return null;			}			if (ast.getParent() != null) {				ast = ast.getParent();			} else {				System.err.println("Error Error Error  can't find declared node");				System.err.println(node);				return null;			}		}	}}