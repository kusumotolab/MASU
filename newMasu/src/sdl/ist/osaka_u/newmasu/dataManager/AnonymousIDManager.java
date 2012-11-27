package sdl.ist.osaka_u.newmasu.dataManager;

import java.util.HashSet;

import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;

import sdl.ist.osaka_u.newmasu.util.Pair;

public class AnonymousIDManager {
	
	private static Integer id = 0; 
	private static String getUniqueID() {
		id++;
		return id.toString();
	}

	final private static HashSet<Pair<ITypeBinding, String>> relations = new HashSet<Pair<ITypeBinding, String>>();
	public final static HashSet<Pair<ITypeBinding, String>> getRelations() {
		return relations;
	}

	public final static void addRelation(ITypeBinding annoClass) {
		String name = getUniqueID();
		relations.add(new Pair<ITypeBinding, String>(annoClass, name));
	}
	
	/**
	 * 未登録だったら追加も行う
	 * @param fullQualifiedPath
	 * @return
	 */
	public static String getID(ITypeBinding annoClass)
	{
		for( Pair<ITypeBinding, String> p : relations ){
			if( p.getFirst().equals(annoClass) )
				return p.getSecond();
		}
		
		String newID = getUniqueID();
		relations.add(new Pair<ITypeBinding, String>(annoClass, newID));
		return newID;
	}

	// インスタンスの生成を防ぐ
	private AnonymousIDManager(){}

}
