package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BreakStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReturnStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;


/**
 * 制御依存グラフのノードを表すクラス
 * @author t-miyake
 *
 * @param <T> ノードの核となる情報の型
 */
public abstract class CFGNode<T extends StatementInfo> {

    /**
     * このノードのフォワードノードのセット
     */
    private final Set<CFGNode<? extends StatementInfo>> forwardNodes;

    /**
     * このノードのバックワードノードのセット
     */
    private final Set<CFGNode<? extends StatementInfo>> backwardNodes;

    protected String text;

    /**
     * このノードに対応する文
     */
    private final T statement;

    protected CFGNode(final T statement) {

        if (null == statement) {
            throw new IllegalArgumentException("statement is null");
        }
        this.statement = statement;
        this.forwardNodes = new HashSet<CFGNode<? extends StatementInfo>>();
        this.backwardNodes = new HashSet<CFGNode<? extends StatementInfo>>();
    }

    public void addForwardNode(final CFGNode<? extends StatementInfo> forwardNode) {

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
    public T getStatement() {
        return this.statement;
    }

    /**
     * このノードのフォワードノードのセットを取得
     * @return このノードのフォワードノードのセット
     */
    public Set<CFGNode<? extends StatementInfo>> getForwardNodes() {
        return Collections.unmodifiableSet(this.forwardNodes);
    }

    /**
     * このノードのバックワードノードのセットを取得
     * @return このノードのバックワードノードのセット
     */
    public Set<CFGNode<? extends StatementInfo>> getBackwardNodes() {
        return this.backwardNodes;
    }

    /**
     * このノードが引数で与えられたローカル空間の出口のノードであるか否か返す．
     * @param localSpace ローカル空間
     * @return 引数のローカル空間の出口の場合，true
     */
    public boolean isExitNode(final LocalSpaceInfo localSpace) {
        if (this.statement instanceof ReturnStatementInfo) {
            return true;
        } else if (this.statement instanceof BreakStatementInfo) {
            final BreakStatementInfo breakStatement = (BreakStatementInfo) this.statement;
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
}
