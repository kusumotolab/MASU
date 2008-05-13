package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.UsageElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.SwitchBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.SwitchBlockStateManager.SWITCH_BLOCK_STATE_CHANGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CastUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SwitchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedCaseEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedDefaultEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedSwitchBlockInfo;


public class SwitchBlockBuilder extends
        ConditionalBlockBuilder<SwitchBlockInfo, UnresolvedSwitchBlockInfo> {

    public SwitchBlockBuilder(final BuildDataManager targetDataManager, final ExpressionElementManager expressionManager) {
        super(targetDataManager, new SwitchBlockStateManager());
        
        if(null == expressionManager) {
            throw new IllegalArgumentException("expressionManager is null");
        }
        
        this.expressionManager = expressionManager;
    }

    @Override
    public void stateChangend(final StateChangeEvent<AstVisitEvent> event) {
        super.stateChangend(event);
        final StateChangeEventType type = event.getType();

        if (this.getCurrentSpace() instanceof UnresolvedSwitchBlockInfo) {
            final UnresolvedSwitchBlockInfo currentSwitch = (UnresolvedSwitchBlockInfo) this.getCurrentSpace();
            if (type.equals(SWITCH_BLOCK_STATE_CHANGE.ENTER_CASE_ENTRY)) {

            } else if (type.equals(SWITCH_BLOCK_STATE_CHANGE.EXIT_CASE_ENTYR)) {
                
                final UnresolvedCaseEntryInfo caseEntry = new UnresolvedCaseEntryInfo(currentSwitch, this.expressionManager.getLastPoppedExpressionElement().getUsage());
                currentSwitch.addStatement(caseEntry);
            } else if (type.equals(SWITCH_BLOCK_STATE_CHANGE.ENTER_DEFAULT_ENTRY)) {
                final UnresolvedDefaultEntryInfo defaultEntry = new UnresolvedDefaultEntryInfo(currentSwitch);
                currentSwitch.addStatement(defaultEntry);
            } else if (type.equals(SWITCH_BLOCK_STATE_CHANGE.EXIT_DEFAULT_ENTRY)) {

            }
        }
    }

    @Override
    protected UnresolvedSwitchBlockInfo createUnresolvedBlockInfo(
            final UnresolvedLocalSpaceInfo<?> outerSpace) {
        return new UnresolvedSwitchBlockInfo(outerSpace);
    }

    private final ExpressionElementManager expressionManager;
}
