package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BreakStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReturnStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


/**
 * 制御依存グラフのノードを表すクラス
 * @author t-miyake
 *
 * @param <T> ノードの核となる情報の型
 */
public abstract class CFGNode<T extends ExecutableElementInfo> {

    /**
     * このノードのフォワードノードのセット
     */
    private final Set<CFGNode<? extends ExecutableElementInfo>> forwardNodes;

    /**
     * このノードのバックワードノードのセット
     */
    private final Set<CFGNode<? extends ExecutableElementInfo>> backwardNodes;

    private final String text;

    /**
     * このノードに対応する文
     */
    private final T core;

    protected CFGNode(final T core) {

        if (null == core) {
            throw new IllegalArgumentException("core is null");
        }
        this.core = core;
        this.forwardNodes = new HashSet<CFGNode<? extends ExecutableElementInfo>>();
        this.backwardNodes = new HashSet<CFGNode<? extends ExecutableElementInfo>>();
        this.text = core.getText() + " <" + core.getFromLine() + ">";
    }

    void addForwardNode(final CFGNode<? extends ExecutableElementInfo> forwardNode) {

        if (null == forwardNode) {
            throw new IllegalArgumentException();
        }

        if (!(forwardNode instanceof CFGEmptyNode)) {
            this.forwardNodes.add(forwardNode);
        }

        forwardNode.backwardNodes.add(this);
    }

    /**
     * このノードに対応する文の情報を取得
     * @return このノードに対応する文
     */
    public T getCore() {
        return this.core;
    }

    /**
     * このノードのフォワードノードのセットを取得
     * @return このノードのフォワードノードのセット
     */
    public Set<CFGNode<? extends ExecutableElementInfo>> getForwardNodes() {
        return Collections.unmodifiableSet(this.forwardNodes);
    }

    /**
     * このノードのバックワードノードのセットを取得
     * @return このノードのバックワードノードのセット
     */
    public Set<CFGNode<? extends ExecutableElementInfo>> getBackwardNodes() {
        return this.backwardNodes;
    }

    /**
     * このノードが引数で与えられたローカル空間の出口のノードであるか否か返す．
     * @param localSpace ローカル空間
     * @return 引数のローカル空間の出口の場合，true
     */
    public boolean isExitNode(final LocalSpaceInfo localSpace) {
        if (this.core instanceof ReturnStatementInfo) {
            return true;
        } else if (this.core instanceof BreakStatementInfo) {
            final BreakStatementInfo breakStatement = (BreakStatementInfo) this.core;
            if (localSpace instanceof BlockInfo && ((BlockInfo) localSpace).isLoopStatement()) {
                if (null == breakStatement.getDestinationLabel()) {
                    return true;
                } else {

                }
            }
        }
        return false;
    }

    public final String getText() {
        return this.text;
    }

    /**
     * このノードで定義・変更されている変数のSetを返す
     * 
     * @return
     */
    public final Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        return VariableUsageInfo.getUsedVariables(VariableUsageInfo.getAssignments(this.getCore()
                .getVariableUsages()));
    }

    /**
     * このノードで利用（参照）されている変数のSetを返す
     * 
     * @return
     */
    public final Set<VariableInfo<? extends UnitInfo>> getUsedVariables() {
        return VariableUsageInfo.getUsedVariables(VariableUsageInfo.getReferencees(this.getCore()
                .getVariableUsages()));
    }
}
