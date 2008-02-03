package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;

/**
 * 内部ブロックの解析状態を管理するクラス
 * 
 * @author t-miyake
 *
 */
public class InnerBlockStateManager extends DeclaredBlockStateManager {

	public static enum INNER_BLOCK_STATE_CHANGE implements StateChangeEventType {
        ENTER_BLOCK_DEF, EXIT_BLOCK_DEF,

        ENTER_BLOCK_SCOPE, EXIT_BLOCK_SCOPE
    }
	
	@Override
	protected StateChangeEventType getBlockEnterEventType() {
		return INNER_BLOCK_STATE_CHANGE.ENTER_BLOCK_SCOPE;
	}

	@Override
	protected StateChangeEventType getBlockExitEventType() {
		return INNER_BLOCK_STATE_CHANGE.EXIT_BLOCK_SCOPE;
	}

	@Override
	protected StateChangeEventType getDefinitionEnterEventType() {
		return INNER_BLOCK_STATE_CHANGE.ENTER_BLOCK_DEF;
	}

	@Override
	protected StateChangeEventType getDefinitionExitEventType() {
		return INNER_BLOCK_STATE_CHANGE.EXIT_BLOCK_DEF;
	}

	@Override
	protected boolean isDefinitionToken(AstToken token) {
		return token.isSpecificBlock();
	}

}
