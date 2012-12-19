//package sdl.ist.osaka_u.newmasu.util;
//
//import java.util.Collection;
//import java.util.Iterator;
//
//import org.apache.commons.collections15.multimap.MultiHashMap;
//import org.eclipse.jdt.core.dom.ASTNode;
//import org.eclipse.jdt.core.dom.IBinding;
//import org.eclipse.jdt.core.dom.MethodDeclaration;
//import org.eclipse.jdt.core.dom.MethodInvocation;
//import org.eclipse.jdt.core.dom.TypeDeclaration;
//
//public class IshrDualMap {
//
//	private final MultiHashMap<ASTNode, IBinding> callerMap = new MultiHashMap<ASTNode, IBinding>();
//	public final MultiHashMap<IBinding, ASTNode> calleeMap = new MultiHashMap<IBinding, ASTNode>();
//	
//	public Collection<ASTNode> getRelation(final IBinding bind){
//		final Collection<ASTNode> callElem = calleeMap.get(bind);
//		return callElem;
//	}
//	public Collection<IBinding> getRelation(final ASTNode bind){
//		final Collection<IBinding> callElem = callerMap.get(bind);
//		return callElem;
//	}
//	
//	public void AddRelation(final MethodInvocation caller, final IBinding callee){
//		
//		ASTNode md = getMD(caller);
//		IBinding bind = null;
//		if( md.getNodeType() == ASTNode.METHOD_DECLARATION )
//			bind = ((MethodDeclaration)md).resolveBinding();
//		else if( md.getNodeType() == ASTNode.TYPE_DECLARATION )
//			bind = ((TypeDeclaration)md).resolveBinding();
//		else
//			Output.err("Undefined Node Type in IDM");
//		
//		// put callee
//		if(!calleeMap.containsKey(bind)){
//			calleeMap.put(bind, null);
//		}
//		else{
//			final Collection<ASTNode> callElem = calleeMap.get(bind);
//			final Iterator<ASTNode> iter = callElem.iterator();
//			boolean isNull = false;
//			for(;iter.hasNext();){
//				ASTNode node = iter.next();
//				if(node==null){
//					isNull = true;
//					break;
//				}
//			}
//			if(isNull)
//				calleeMap.remove(bind, null);
//		}
//
//		calleeMap.put(callee, md);
//		
//		// put caller
//		
//	}
//	
//	private ASTNode getMD(ASTNode node){
//		while(node.getNodeType() != ASTNode.METHOD_DECLARATION && node.getNodeType() != ASTNode.TYPE_DECLARATION)
//			node = node.getParent();
//		return node;
//	}
//}
