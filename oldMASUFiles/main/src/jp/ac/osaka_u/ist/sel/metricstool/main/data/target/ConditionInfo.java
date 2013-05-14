package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * if文やfor文の条件を表すクラス
 * 
 * @author higo
 *
 */
public interface ConditionInfo extends ExecutableElementInfo {

    ConditionalBlockInfo getOwnerConditionalBlock();

    void setOwnerConditionalBlock(ConditionalBlockInfo ownerConditionalBlock);
}
