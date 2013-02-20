package sdl.ist.osaka_u.newmasu.data;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class MethodInfo {

	private final MethodDeclaration methodDec;
	private final FieldDeclaration fieldDec;
	private final Initializer initializer;
	private final EnumConstantDeclaration enumConstDec;

	public MethodInfo(MethodDeclaration methodDec){
		if(methodDec != null)
			throw new IllegalArgumentException();

		this.methodDec = methodDec;
		this.fieldDec = null;
		this.initializer = null;
		this.enumConstDec = null;
	}

	public MethodInfo(FieldDeclaration fieldDec){
		if(fieldDec != null)
			throw new IllegalArgumentException();

		this.methodDec = null;
		this.fieldDec = fieldDec;
		this.initializer = null;
		this.enumConstDec = null;
	}

	public MethodInfo(Initializer initializer){
		if(initializer != null)
			throw new IllegalArgumentException();

		this.methodDec = null;
		this.fieldDec = null;
		this.initializer = initializer;
		this.enumConstDec = null;
	}

	public MethodInfo(EnumConstantDeclaration enumConstDec){
		if(enumConstDec != null)
			throw new IllegalArgumentException();

		this.methodDec = null;
		this.fieldDec = null;
		this.initializer = null;
		this.enumConstDec = enumConstDec;
	}

	public boolean isMethod(){
		if(methodDec != null){
			return true;
		}

		return false;
	}

	public boolean isConstructor(){
		if(methodDec != null){
			return methodDec.isConstructor();
		}

		return false;
	}

	public boolean isInitializer(){
		if(initializer != null){
			return true;
		}

		return false;
	}

	public boolean isFieldDeclaration(){
		if(fieldDec != null){
			return true;
		}

		return false;
	}

	public boolean isEnumConst(){
		if(enumConstDec != null){
			return true;
		}

		return false;
	}

	public Set<VariableInfo> getLocalVariables(){

		Set<VariableInfo> set = new HashSet<VariableInfo>();

		return set;
	}

	public Set<MethodInfo> getCallers(){

		Set<MethodInfo> set = new HashSet<MethodInfo>();

		return set;
	}

	public Set<MethodInfo> getCallees(){

		Set<MethodInfo> set = new HashSet<MethodInfo>();

		return set;
	}

	public Set<MethodInfo> getOverriders(){

		Set<MethodInfo> set = new HashSet<MethodInfo>();

		return set;
	}

	public Set<MethodInfo> getOverridees(){

		Set<MethodInfo> set = new HashSet<MethodInfo>();

		return set;
	}

	public Set<VariableInfo> getUsedField(){

		Set<VariableInfo> set = new HashSet<VariableInfo>();

		return set;
	}

	public Set<VariableInfo> getAssignedField(){

		Set<VariableInfo> set = new HashSet<VariableInfo>();

		return set;
	}

	public Set<VariableInfo> getDecalaringClass(){

		return null;
	}

	public IMethodBinding resolveMethodBinding(){

		if(isMethod()){
			return methodDec.resolveBinding();
		}

		return null;
	}

	public IVariableBinding resolveEnumConstBinding(){

		if(isEnumConst()){
			return enumConstDec.resolveVariable();
		}

		return null;
	}

}
