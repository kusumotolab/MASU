package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.DeclaredBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;

/**
 * 内部ブロックの解析状態を管理するクラス
 * 
 * @author t-miyake
 *
 */
public abstract class InnerBlockStateManager extends DeclaredBlockStateManager {

	public static enum INNER_BLOCK_STATE_CHANGE implements StateChangeEventType {
        ENTER_BLOCK_DEF, EXIT_BLOCK_DEF,
        
        ENTER_CLAUSE, EXIT_CLAUSE,

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

}
