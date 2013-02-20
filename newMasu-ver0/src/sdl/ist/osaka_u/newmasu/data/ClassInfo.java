package sdl.ist.osaka_u.newmasu.data;

import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ClassInfo {

	private final TypeDeclaration typeDec;
	private final EnumDeclaration enumDec;
	private final AnonymousClassDeclaration anonymousDec;

	public ClassInfo(TypeDeclaration typeDec){
		if(typeDec != null)
			throw new IllegalArgumentException();
		this.typeDec =typeDec ;
		this.enumDec = null;
		this.anonymousDec = null;
	}

	public ClassInfo(EnumDeclaration enumDec){
		if(enumDec != null)
			throw new IllegalArgumentException();
		this.typeDec =null ;
		this.enumDec = enumDec;
		this.anonymousDec = null;
	}

	public ClassInfo(AnonymousClassDeclaration anonymousDec){
		if(anonymousDec != null)
			throw new IllegalArgumentException();
		this.typeDec = null;
		this.enumDec = null;
		this.anonymousDec = anonymousDec;
	}

	public VariableInfo getFields(){

		return null;
	}

	public MethodInfo getMethods(){

		return null;
	}

	public ClassInfo getInnerClass(){

		return null;
	}

	public ClassInfo getSuperClass(){

		return null;
	}

	public ClassInfo getSubClass(){

		return null;
	}

	public ClassInfo getUsedClass(){

		return null;
	}

	public ClassInfo getUsingClass(){

		return null;
	}

	public ITypeBinding resolveBinding(){

		if(typeDec != null){
			return typeDec.resolveBinding();
		}
		if(enumDec != null){
			return enumDec.resolveBinding();
		}
		if(anonymousDec != null)
			return anonymousDec.resolveBinding();

		System.err.println("instance error");
		return null;
	}

	public boolean isClass(){

		if(typeDec != null){
			return true;
		}

		return false;
	}

	public boolean isInterface(){

		if(typeDec != null){
			return typeDec.isInterface();
		}

		return false;
	}

	public boolean isEnum(){

		if(enumDec != null){
			return true;
		}

		return false;
	}

	public boolean isAnonymousClass(){

		if(anonymousDec != null){
			return true;
		}

		return false;
	}

}
