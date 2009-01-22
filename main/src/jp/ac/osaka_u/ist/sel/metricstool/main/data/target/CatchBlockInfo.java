package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * catch ブロック情報を表すクラス
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public final class CatchBlockInfo extends BlockInfo {

    /**
     * 対応する try ブロック情報を与えて catch ブロックを初期化
     * 
     * @param ownerClass 所有クラス
     * @param outerSpace 外側のブロック
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     * @param ownerTryBlock このcatch節が属するtryブロック
     * @param caughtException このcatch節がキャッチする例外
     */
    public CatchBlockInfo(final TargetClassInfo ownerClass, final LocalSpaceInfo outerSpace,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn,
            final TryBlockInfo ownerTryBlock) {

        super(ownerClass, outerSpace, fromLine, fromColumn, toLine, toColumn);

        if (null == ownerTryBlock) {
            throw new NullPointerException();
        }

        this.ownerTryBlock = ownerTryBlock;
    }

    /**
     * 対応する try ブロックを返す
     * 
     * @return 対応する try ブロック
     */
    public final TryBlockInfo getOwnerTryBlock() {
        return this.ownerTryBlock;
    }
    
    /**
     * catchする例外を表す変数の情報を返す
     * @return catchする例外を表す変数の情報
     */
    public final LocalVariableInfo getCaughtException() {
        return caughtException;
    }
    
    public void setCaughtException(LocalVariableInfo caughtException) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == caughtException) {
            throw new NullPointerException();
        }
        
        if(null != this.caughtException) {
            throw new IllegalStateException();
        }

        this.caughtException = caughtException;
    }

    /**
     * このcatch　文のテキスト表現（String型）を返す
     * 
     * @return このcatch　文のテキスト表現（String型）
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("catch (UNDER IMPLEMENTATION) {");
        sb.append(System.getProperty("line.separator"));

        final SortedSet<StatementInfo> statements = this.getStatements();
        for (final StatementInfo statement : statements) {
            sb.append(statement.getText());
            sb.append(System.getProperty("line.separator"));
        }

        sb.append("}");

        return sb.toString();
    }

    private final TryBlockInfo ownerTryBlock;

    private LocalVariableInfo caughtException;
}
