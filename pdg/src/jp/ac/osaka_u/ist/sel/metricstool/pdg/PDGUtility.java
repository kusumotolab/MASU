package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;


public class PDGUtility {

    static public ConditionalBlockInfo getOwnerConditionalBlock(final ConditionInfo condition) {
        final ExecutableElementInfo owner = condition.getOwnerExecutableElement();
        assert owner instanceof ConditionalBlockInfo : "owner must be an instance of ConditionalBlockInfo!";
        return (ConditionalBlockInfo) owner;
    }
}
