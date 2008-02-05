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

public abstract class BlockNameToken extends AstTokenAdapter {

	public BlockNameToken(String text){
		super(text);
	}
	
	@Override
	public boolean isBlockName() {
		return true;
	}
	
	public abstract UnresolvedBlockInfo<? extends BlockInfo> createUnresolvedInfo();
	
	public abstract boolean isPostConditionalClause();
	
	public abstract boolean isPreConditionalClause();
	
	
	
	public static BlockNameToken IF_BLOCK = new BlockNameToken("if"){
		
		@Override
		public UnresolvedBlockInfo<IfBlockInfo> createUnresolvedInfo() {
			return new UnresolvedIfBlockInfo();
		}
		
		@Override
		public boolean isFor() {
		    return true;
		}
		
		@Override
		public boolean isPostConditionalClause() {
		    return false;
		}
		
		@Override
		public boolean isPreConditionalClause() {
		    return true;
		}
	};
	
	public static BlockNameToken ELSE_BLOCK = new BlockNameToken("else"){
		
		@Override
		public UnresolvedBlockInfo<ElseBlockInfo> createUnresolvedInfo(){
			return null;
		}
		
		@Override
		public boolean isElse() {
		    return true;
		}
		
		@Override
        public boolean isPostConditionalClause() {
            return false;
        }
        
        @Override
        public boolean isPreConditionalClause() {
            return false;
        }
	};
	
	public static BlockNameToken WHILE_BLOCK = new BlockNameToken("while"){
	
		@Override
		public UnresolvedBlockInfo<WhileBlockInfo> createUnresolvedInfo(){
			return new UnresolvedWhileBlockInfo();
		}
		
		@Override
		public boolean isWhile() {
		    return true;
		}
		
		@Override
        public boolean isPostConditionalClause() {
            return false;
        }
        
        @Override
        public boolean isPreConditionalClause() {
            return true;
        }
	};
	
	public static BlockNameToken DO_BLOCK = new BlockNameToken("do"){
		
		@Override
		public UnresolvedBlockInfo<DoBlockInfo> createUnresolvedInfo(){
			return new UnresolvedDoBlockInfo();
		}
		
		@Override
		public boolean isDo() {
		    return true;
		}
		
		@Override
        public boolean isPostConditionalClause() {
            return true;
        }
        
        @Override
        public boolean isPreConditionalClause() {
            return false;
        }
	};
	
	public static BlockNameToken FOR_BLOCK = new BlockNameToken("for"){
		
		@Override
		public UnresolvedBlockInfo<ForBlockInfo> createUnresolvedInfo(){
			return new UnresolvedForBlockInfo();
		}
		
		@Override
		public boolean isFor() {
		    return true;
		}
		
		@Override
        public boolean isPostConditionalClause() {
            return false;
        }
        
        @Override
        public boolean isPreConditionalClause() {
            return true;
        }
	};

	public static BlockNameToken TRY_BLOCK = new BlockNameToken("try"){
		
		@Override
		public UnresolvedBlockInfo<TryBlockInfo> createUnresolvedInfo(){
			return new UnresolvedTryBlockInfo();
		}
		
		@Override
		public boolean isTry() {
		    return true;
		}
		
		@Override
        public boolean isPostConditionalClause() {
            return false;
        }
        
        @Override
        public boolean isPreConditionalClause() {
            return false;
        }
	};
	
	public static BlockNameToken CATCH_BLOCK = new BlockNameToken("catch"){
		
		@Override
		public UnresolvedBlockInfo<CatchBlockInfo> createUnresolvedInfo(){
			return null;
		}
		
		@Override
		public boolean isCatch() {
		    return true;
		}
		
		@Override
        public boolean isPostConditionalClause() {
            return false;
        }
        
        @Override
        public boolean isPreConditionalClause() {
            return false;
        }
	};
	
	public static BlockNameToken FINALLY_BLOCK = new BlockNameToken("finally"){
		
		@Override
		public UnresolvedBlockInfo<FinallyBlockInfo> createUnresolvedInfo(){
			return null;
		}
		
		@Override
		public boolean isFinally() {
		    return true;
		}
		
		@Override
        public boolean isPostConditionalClause() {
            return false;
        }
        
        @Override
        public boolean isPreConditionalClause() {
            return false;
        }
	};
	
	public static BlockNameToken SWITCH_BLOCK = new BlockNameToken("switch"){
		
		@Override
		public UnresolvedBlockInfo<SwitchBlockInfo> createUnresolvedInfo(){
			return new UnresolvedSwitchBlockInfo();
		}
		
		@Override
		public boolean isSwitch() {
		    return true;
		}
		
		@Override
        public boolean isPostConditionalClause() {
            return false;
        }
        
        @Override
        public boolean isPreConditionalClause() {
            return true;
        }
	};
	
	public static BlockNameToken CASE_ENTRY = new BlockNameToken("case"){
		
		@Override
		public UnresolvedBlockInfo<CaseEntryInfo> createUnresolvedInfo(){
			return null;
		}
		
		@Override
		public boolean isCase() {
		    return true;
		}
		
		@Override
        public boolean isPostConditionalClause() {
            return false;
        }
        
        @Override
        public boolean isPreConditionalClause() {
            return true;
        }
	};
	
	public static BlockNameToken DEFAULT_ENTRY = new BlockNameToken("default"){
		
		@Override
		public UnresolvedBlockInfo<DefaultEntryInfo> createUnresolvedInfo(){
			return null;
		}
		
		@Override
		public boolean isDefault() {
		    return true;
		}
		
		@Override
        public boolean isPostConditionalClause() {
            return false;
        }
        
        @Override
        public boolean isPreConditionalClause() {
            return false;
        }
	};
	
}
