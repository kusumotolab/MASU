package sdl.ist.osaka_u.newmasu.accessor;public class VariableInfoAccessor extends UnitInfoAccessor{//	/**//	 * 対象となる変数が定義されている文のノードを取得する//	 *//	 * @param node//	 *            対象となる変数//	 * @return 定義されている文//	 *///	public static ASTNode getDeclaringStatement(SimpleName node){////		IBinding binding = node.resolveBinding();//		if(binding != null){//			if(binding.getKind() == IBinding.VARIABLE){//				IVariableBinding vb = (IVariableBinding) binding;//				ASTNode ast = BindingManager.getDec().get(vb);////				return NodeFinder.getDeclaringNode(ast);//			}//		}else{//			System.err.println("binding error in getDeclaringStatement");//		}////		return null;//	}//////	protected VariableInfoAccessor(){////	}}