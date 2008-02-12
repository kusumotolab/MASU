package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.ElseBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedElseBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedIfBlockInfo;

public class ElseBlockBuilder extends InnerBlockBuilder {

    public ElseBlockBuilder(BuildDataManager targetDataManager) {
        super(targetDataManager);

        this.blockStateManager = new ElseBlockStateManager();
    }

    @Override
    protected UnresolvedElseBlockInfo createUnresolvedBlockInfo() {
        final UnresolvedBlockInfo<?> preBlock = this.buildManager.getPreBlock();
        if(preBlock instanceof UnresolvedIfBlockInfo) {
            return new UnresolvedElseBlockInfo((UnresolvedIfBlockInfo) preBlock);
        } else {
            assert false : "Illegal state : incorrect block structure";
            return null;
        }
    }

}
