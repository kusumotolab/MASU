package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.ForBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedForBlockInfo;

public class ForBlockBuilder extends ConditionalBlockBuilder {

    public ForBlockBuilder(BuildDataManager targetDataManager) {
        super(targetDataManager);

        this.blockStateManager = new ForBlockStateManager();
    }

    @Override
    protected UnresolvedForBlockInfo createUnresolvedBlockInfo() {
        return new UnresolvedForBlockInfo();
    }

}
