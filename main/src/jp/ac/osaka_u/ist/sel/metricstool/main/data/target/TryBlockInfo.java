package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * try ブロックを表すクラス
 * 
 * @author higo
 * 
 */
public final class TryBlockInfo extends BlockInfo {

    /**
     * 位置情報を与えて try ブロックを初期化
     * 
     * @param ownerClass 所有クラス
     * @param ownerMethod 所有メソッド
     * @param outerSpace 外側のブロック
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public TryBlockInfo(final TargetClassInfo ownerClass, final CallableUnitInfo ownerMethod,
            final LocalSpaceInfo outerSpace, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(ownerClass, ownerMethod, outerSpace, fromLine, fromColumn, toLine, toColumn);

        this.sequentFinallyBlock = null;
        this.sequentCatchBlocks = new HashSet<CatchBlockInfo>();
    }

    /**
     * 対応する finally 文をセットする
     * 
     * @param sequentFinallyBlock 対応する finally 文
     */
    public void setSequentFinallyBlock(final FinallyBlockInfo sequentFinallyBlock) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == sequentFinallyBlock) {
            throw new NullPointerException();
        }

        this.sequentFinallyBlock = sequentFinallyBlock;
    }

    /**
     * 対応する finally 文を返す
     * 
     * @return 対応する finally 文
     */
    public FinallyBlockInfo getSequentFinallyBlock() {
        return this.sequentFinallyBlock;
    }

    /**
     * 対応するcatchブロックを追加する
     * @param catchBlock 対応するcatchブロック
     */
    public void addSequentCatchBlock(final CatchBlockInfo catchBlock) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == catchBlock) {
            throw new IllegalArgumentException("catchBlock is null");
        }

        this.sequentCatchBlocks.add(catchBlock);
    }

    /**
     * 対応するcatchブロックのSetを返す
     * @return 対応するcatchブロックのSet
     */
    public Set<CatchBlockInfo> getSequentCatchBlocks() {
        return this.sequentCatchBlocks;
    }

    /**
     * 対応するfinallyブロックが存在するかどうか返す
     * @return 対応するfinallyブロックが存在するならtrue
     */
    public boolean hasFinallyBlock() {
        return null != this.sequentFinallyBlock;
    }

    /**
     * このtry文のテキスト表現（型）を返す
     * 
     * @return このtry文のテキスト表現（型）
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("try {");
        sb.append(System.getProperty("line.separator"));

        final SortedSet<StatementInfo> statements = this.getStatements();
        for (final StatementInfo statement : statements) {
            sb.append(statement.getText());
            sb.append(System.getProperty("line.separator"));
        }

        sb.append("}");

        return sb.toString();
    }

    /**
     * 対応するcatchブロックを保存する変数
     */
    private final Set<CatchBlockInfo> sequentCatchBlocks;

    private FinallyBlockInfo sequentFinallyBlock;
}
