package sdl.ist.osaka_u.newmasu.dataManager;

import java.util.Collection;

import org.apache.commons.collections15.multimap.MultiHashMap;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

import sdl.ist.osaka_u.newmasu.util.DualMultiMap;

public class ClassManager {
	
	final private static DualMultiMap<ITypeBinding, ITypeBinding> rel = new DualMultiMap<>();
	
	public static final DualMultiMap<ITypeBinding, ITypeBinding> getRel() {
		return rel;
	}

//	// 継承元→継承先
//	final private static MultiHashMap<ITypeBinding, ITypeBinding> fromTo = new MultiHashMap<>();
//
//	public static final MultiHashMap<ITypeBinding, ITypeBinding> getFromTo() {
//		return fromTo;
//	}
//
//	// 継承先→継承元
//	final private static MultiHashMap<ITypeBinding, ITypeBinding> toFrom = new MultiHashMap<>();
//
//	public static final MultiHashMap<ITypeBinding, ITypeBinding> getToFrom() {
//		return toFrom;
//	}
//
//	public static void addRelation(final ITypeBinding from,
//			final ITypeBinding to) {
//		fromTo.put(from, to);
//		toFrom.put(to, from);
//	}

	public static IMethodBinding getOverrideMethod(IMethodBinding bind){
		ITypeBinding classBind = bind.getDeclaringClass();
		return rec(bind, classBind);
	}
	
	private static IMethodBinding rec(final IMethodBinding bind, final ITypeBinding classBind){
		if(classBind==null)
			return null;
		final IMethodBinding[] declaringMethods = classBind.getDeclaredMethods();
		for(IMethodBinding mb : declaringMethods)
		{				
			if(bind.overrides(mb))
				return mb;
		}
		
		final Collection<ITypeBinding> inh = rel.getCallerMap().getCollection(classBind);
		if(inh==null)
			return null;
		for(ITypeBinding t : inh){
			final IMethodBinding b = rec(bind,t);
			if(b != null)
				return b;
		}
		return null;
	}

	// インスタンスの生成を防ぐ
	private ClassManager() {
	}
}
