package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.CaseEntryStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedCaseEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedSwitchBlockInfo;

public class CaseEntryBuilder extends InnerBlockBuilder {

    public CaseEntryBuilder(BuildDataManager targetDataManager) {
        super(targetDataManager);

        this.blockStateManager = new CaseEntryStateManager();
    }

    @Override
    protected UnresolvedCaseEntryInfo createUnresolvedBlockInfo() {
        UnresolvedBlockInfo<?> preBlock = buildManager.getPreBlock();
        if(preBlock instanceof UnresolvedSwitchBlockInfo) {
            return new UnresolvedCaseEntryInfo((UnresolvedSwitchBlockInfo) preBlock);
        } else {
            return null;
        }
    }

}
