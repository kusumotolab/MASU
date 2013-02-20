package sdl.ist.osaka_u.newmasu.data;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.SimpleName;

public class VariableInfo {

	private final SimpleName variable;

	public VariableInfo(SimpleName variable){
		if(variable == null){
			throw new IllegalArgumentException();
		}

		IBinding binding = variable.resolveBinding();

		if(binding == null){
			System.err.println("can`t resolve binding");
		}else{
			if(binding.getKind() == IBinding.VARIABLE){
				throw new IllegalArgumentException();
			}
		}

		this.variable = variable;
	}

	public boolean isDeclaration(){
		return variable.isDeclaration();
	}

	public boolean isField(){
		IVariableBinding vBinding = (IVariableBinding) variable.resolveBinding();
		if(vBinding != null)
			return vBinding.isField();

		return false;
	}

	public boolean isParameter(){
		IVariableBinding vBinding = (IVariableBinding) variable.resolveBinding();
		if(vBinding != null)
			return vBinding.isParameter();

		return false;
	}

	public IVariableBinding resolveBinding(){

		return (IVariableBinding) variable.resolveBinding();
	}
}
