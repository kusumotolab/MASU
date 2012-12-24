package sdl.ist.osaka_u.newmasu.util;

import java.util.HashMap;

/**
 * 1:1の関係を双方向から高速に引き出すためのマップ
 * 
 * @author s-kimura
 */
public class DualHashMap<T_CALLER, T_CALLEE> {

	private final HashMap<T_CALLER, T_CALLEE> callerMap = new HashMap<T_CALLER, T_CALLEE>();

	public HashMap<T_CALLER, T_CALLEE> getCallerMap() {
		return callerMap;
	}

	private final HashMap<T_CALLEE, T_CALLER> calleeMap = new HashMap<T_CALLEE, T_CALLER>();

	public HashMap<T_CALLEE, T_CALLER> getCalleeMap() {
		return calleeMap;
	}

	public void AddRelation(final T_CALLER caller, final T_CALLEE callee) {
		callerMap.put(caller, callee);
		calleeMap.put(callee, caller);
	}
}
