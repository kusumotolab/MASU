package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.SortedSet;


/**
 * finally ブロック情報を表すクラス
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public final class FinallyBlockInfo extends BlockInfo implements
        SubsequentialBlockInfo<TryBlockInfo> {

    /**
     * 対応する try ブロック情報を与えて finally ブロックを初期化
     * 
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     * @param ownerTryBlock この finally 節が属する try ブロック
     */
    public FinallyBlockInfo(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn, final TryBlockInfo ownerTryBlock) {

        super(fromLine, fromColumn, toLine, toColumn);

        if (null == ownerTryBlock) {
            throw new NullPointerException();
        }

        this.ownerTryBlock = ownerTryBlock;
    }

    /**
     * このfinally節のテキスト表現（String型）を返す
     * 
     * @return このfinally節のテキスト表現（String型）
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("finally {");
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
     * 対応する try ブロックを返す
     * このメソッドは将来廃止されるため，使用は推奨されない
     * {@link FinallyBlockInfo#getOwnerBlock()} を使用すべきである．
     * 
     * @return 対応する try ブロック
     * @deprecated
     */
    public TryBlockInfo getOwnerTryBlock() {
        return this.ownerTryBlock;
    }

    /**
     * 対応する try ブロックを返す
     * 
     * @return 対応する try ブロック
     */
    @Override
    public TryBlockInfo getOwnerBlock() {
        return this.ownerTryBlock;
    }

    private final TryBlockInfo ownerTryBlock;

}
