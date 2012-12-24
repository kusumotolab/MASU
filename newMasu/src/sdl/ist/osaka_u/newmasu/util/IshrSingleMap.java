package sdl.ist.osaka_u.newmasu.util;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections15.multimap.MultiHashMap;

public abstract class IshrSingleMap<T_CALLER, T_CALLEE, T_NODE> {

	private final MultiHashMap<T_CALLER, T_CALLEE> calleeMap = new MultiHashMap<T_CALLER, T_CALLEE>();

	public MultiHashMap<T_CALLER, T_CALLEE> getCalleeMap() {
		return calleeMap;
	}
	public Collection<T_CALLEE> getRelation(final T_CALLER bind) {
		final Collection<T_CALLEE> callElem = calleeMap.get(bind);
		return callElem;
	}

	public void AddRelation(final T_NODE passValue, final T_CALLER callee) {

		T_CALLEE md = getCalleeType(passValue);
		T_CALLER bind = getCallerType(md);

		if (calleeMap.containsKey(bind) && isRemoveNull(bind)){
			final Collection<T_CALLEE> callElem = calleeMap.get(bind);
			if(callElem.size() != 1)		// if calleeMap already have null value
				calleeMap.remove(bind, null);
		}
		else
			calleeMap.put(bind, null);
			
		calleeMap.put(callee, md);
		if (isRemoveNull(callee))
			calleeMap.remove(callee, null);
	}

	private boolean isRemoveNull(T_CALLER key) {
		final Collection<T_CALLEE> callElem = calleeMap.get(key);
		final Iterator<T_CALLEE> iter = callElem.iterator();
		for (; iter.hasNext();) {
			if (iter.next() == null) {
				return true;
			}
		}
		return false;
	}


	protected abstract T_CALLEE getCalleeType(final T_NODE node);
	/*
	 * { while(node.getNodeType() != ASTNode.METHOD_DECLARATION &&
	 * node.getNodeType() != ASTNode.TYPE_DECLARATION) node = node.getParent();
	 * return node; }
	 */

	protected abstract T_CALLER getCallerType(final T_CALLEE md);
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
