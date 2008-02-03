package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CaseEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CatchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.DefaultEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.DoBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ElseBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FinallyBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.IfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SwitchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TryBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.WhileBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedDoBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedForBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedIfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedSwitchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTryBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedWhileBlockInfo;

public abstract class SpecificBlockToken extends AstTokenAdapter {

	public SpecificBlockToken(String text){
		super(text);
	}
	
	@Override
	public boolean isSpecificBlock() {
		return true;
	}
	
	public abstract UnresolvedBlockInfo<? extends BlockInfo> createUnresolvedInfo();
	
	
	public static SpecificBlockToken IF_BLOCK = new SpecificBlockToken("if"){
		
		@Override
		public UnresolvedBlockInfo<IfBlockInfo> createUnresolvedInfo() {
			return new UnresolvedIfBlockInfo();
		}
	};
	
	public static SpecificBlockToken ELSE_BLOCK = new SpecificBlockToken("else"){
		
		@Override
		public UnresolvedBlockInfo<ElseBlockInfo> createUnresolvedInfo(){
			return null;
		}
	};
	
	public static SpecificBlockToken WHILE_BLOCK = new SpecificBlockToken("while"){
	
		@Override
		public UnresolvedBlockInfo<WhileBlockInfo> createUnresolvedInfo(){
			return new UnresolvedWhileBlockInfo();
		}
	};
	
	public static SpecificBlockToken DO_BLOCK = new SpecificBlockToken("do"){
		
		@Override
		public UnresolvedBlockInfo<DoBlockInfo> createUnresolvedInfo(){
			return new UnresolvedDoBlockInfo();
		}
	};
	
	public static SpecificBlockToken FOR_BLOCK = new SpecificBlockToken("for"){
		
		@Override
		public UnresolvedBlockInfo<ForBlockInfo> createUnresolvedInfo(){
			return new UnresolvedForBlockInfo();
		}
	};

	public static SpecificBlockToken TRY_BLOCK = new SpecificBlockToken("try"){
		
		@Override
		public UnresolvedBlockInfo<TryBlockInfo> createUnresolvedInfo(){
			return new UnresolvedTryBlockInfo();
		}
	};
	
	public static SpecificBlockToken CATCH_BLOCK = new SpecificBlockToken("catch"){
		
		@Override
		public UnresolvedBlockInfo<CatchBlockInfo> createUnresolvedInfo(){
			return null;
		}
	};
	
	public static SpecificBlockToken FINALLY_BLOCK = new SpecificBlockToken("finally"){
		
		@Override
		public UnresolvedBlockInfo<FinallyBlockInfo> createUnresolvedInfo(){
			return null;
		}
	};
	
	public static SpecificBlockToken SWITCH_BLOCK = new SpecificBlockToken("switch"){
		
		@Override
		public UnresolvedBlockInfo<SwitchBlockInfo> createUnresolvedInfo(){
			return new UnresolvedSwitchBlockInfo();
		}
	};
	
	public static SpecificBlockToken CASE_ENTRY = new SpecificBlockToken("case"){
		
		@Override
		public UnresolvedBlockInfo<CaseEntryInfo> createUnresolvedInfo(){
			return null;
		}
	};
	
	public static SpecificBlockToken DEFAULT_ENTRY = new SpecificBlockToken("default"){
		
		@Override
		public UnresolvedBlockInfo<DefaultEntryInfo> createUnresolvedInfo(){
			return null;
		}
	};
	
}
