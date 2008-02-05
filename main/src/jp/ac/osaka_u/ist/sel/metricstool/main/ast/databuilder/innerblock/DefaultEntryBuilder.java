package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.DefaultEntryStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedDefaultEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedSwitchBlockInfo;

public class DefaultEntryBuilder extends InnerBlockBuilder {

    public DefaultEntryBuilder(BuildDataManager targetDataManager) {
        super(targetDataManager);
        
        this.blockStateManager = new DefaultEntryStateManager();
    }

    @Override
    protected UnresolvedDefaultEntryInfo createUnresolvedBlockInfo() {
        UnresolvedBlockInfo<?> preBlock = this.buildManager.getPreBlock();
        if(preBlock instanceof UnresolvedSwitchBlockInfo) {
            return new UnresolvedDefaultEntryInfo((UnresolvedSwitchBlockInfo) preBlock);
        } else {
            return null;
        }
    }

}
