package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.WhileBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedWhileBlockInfo;

public class WhileBlockBuilder extends InnerBlockBuilder {

    public WhileBlockBuilder(BuildDataManager targetDataManager) {
        super(targetDataManager);

        this.blockStateManager = new WhileBlockStateManager();
    }

    @Override
    protected UnresolvedWhileBlockInfo createUnresolvedBlockInfo() {
        return new UnresolvedWhileBlockInfo();
    }

}
