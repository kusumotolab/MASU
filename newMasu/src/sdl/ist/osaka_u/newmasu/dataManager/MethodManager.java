package sdl.ist.osaka_u.newmasu.dataManager;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IMethodBinding;

import sdl.ist.osaka_u.newmasu.util.DualMultiMap;

public class MethodManager {
	
	final private static DualMultiMap<ASTNode, IMethodBinding> rel = new DualMultiMap<>();
	
	
//	// 呼び出し元→呼び出し先
//	final private static MultiHashMap<ASTNode, IMethodBinding> calleeToCaller = new MultiHashMap<>();
//	public static final MultiHashMap<ASTNode, IMethodBinding> getCalleetocaller() {
//		return calleeToCaller;
//	}
//
//	// 呼び出し先→呼び出し元
//	final private static MultiHashMap<IMethodBinding, ASTNode> callerToCaller = new MultiHashMap<>();
//	public static final MultiHashMap<IMethodBinding, ASTNode> getCallertocallee() {
//		return callerToCaller;
//	}
//	
//	public static void addRelation(final MethodInvocation node, final IMethodBinding bind){
//		calleeToCaller.put(node, bind);
//		callerToCaller.put(bind, node);
//	}

	public static final DualMultiMap<ASTNode, IMethodBinding> getRel() {
		return rel;
	}


	// インスタンスの生成を防ぐ
	private MethodManager() {
	}

}
