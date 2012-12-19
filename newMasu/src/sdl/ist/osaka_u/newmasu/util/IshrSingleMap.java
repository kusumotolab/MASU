package sdl.ist.osaka_u.newmasu.util;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections15.multimap.MultiHashMap;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IBinding;

public abstract class IshrSingleMap<T extends IBinding, E extends ASTNode, F extends ASTNode> {

	public final MultiHashMap<T, E> calleeMap = new MultiHashMap<T, E>();

	public Collection<E> getRelation(final T bind) {
		final Collection<E> callElem = calleeMap.get(bind);
		return callElem;
	}

	public void AddRelation(final F caller, final T callee) {

		E md = getNode(caller);
		T bind = getBind(md);

		if (calleeMap.containsKey(bind) && isRemoveNull(bind)){
			final Collection<E> callElem = calleeMap.get(bind);
			if(callElem.size() != 1)		// if calleeMap already have null value
				calleeMap.remove(bind, null);
		}
		else
			calleeMap.put(bind, null);
			
		calleeMap.put(callee, md);
		if (isRemoveNull(callee))
			calleeMap.remove(callee, null);
	}

	private boolean isRemoveNull(T key) {
		final Collection<E> callElem = calleeMap.get(key);
		final Iterator<E> iter = callElem.iterator();
		for (; iter.hasNext();) {
			if (iter.next() == null) {
				return true;
			}
		}
		return false;
	}


	protected abstract E getNode(final F node);
	/*
	 * { while(node.getNodeType() != ASTNode.METHOD_DECLARATION &&
	 * node.getNodeType() != ASTNode.TYPE_DECLARATION) node = node.getParent();
	 * return node; }
	 */

	protected abstract T getBind(final E md);
	/*
	 * {
	 * 
	 * IBinding bind = null; if( md.getNodeType() == ASTNode.METHOD_DECLARATION
	 * ) bind = ((MethodDeclaration)md).resolveBinding(); else if(
	 * md.getNodeType() == ASTNode.TYPE_DECLARATION ) bind =
	 * ((TypeDeclaration)md).resolveBinding(); else
	 * Output.err("Undefined Node Type in IDM"); return bind; }
	 */

}
