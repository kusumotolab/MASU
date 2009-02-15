package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Iterator;


/**
 * 
 * @author t-miyake
 *
 * break文を表すクラス
 */
@SuppressWarnings("serial")
public class BreakStatementInfo extends JumpStatementInfo {

    /**
     * オブジェクトを初期化
     * 
     * @param ownerSpace オーナーブロック
     * @param destinationLabel ラベル
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public BreakStatementInfo(LocalSpaceInfo ownerSpace, LabelInfo destinationLabel, int fromLine,
            int fromColumn, int toLine, int toColumn) {
        super(ownerSpace, destinationLabel, fromLine, fromColumn, toLine, toColumn);
    }

    @Override
    protected String getReservedKeyword() {
        return "break";
    }

    @Override
    public StatementInfo getFollowingStatement() {
        if (null != this.getDestinationLabel()) {
            return this.getFollowingStatement(this.getDestinationLabel().getLabeledStatement());
        } else {
            if (this.getOwnerSpace() instanceof BlockInfo
                    && ((BlockInfo) this.getOwnerSpace()).isLoopStatement()) {
                return this.getFollowingStatement((BlockInfo) this.getOwnerSpace());
            }
            assert false;
            return null;
        }
    }

    private StatementInfo getFollowingStatement(final StatementInfo statement) {
        final Iterator<StatementInfo> statements = statement.getOwnerSpace().getStatements()
                .iterator();
        while (statements.hasNext()) {
            if (statements.next().equals(statement)) {
                if (statements.hasNext()) {
                    return statements.next();
                } else {
                    if (statement.getOwnerSpace() instanceof BlockInfo) {
                        return this.getFollowingStatement((BlockInfo) statement.getOwnerSpace());
                    }
                }
            }
        }

        return null;
    }

}
