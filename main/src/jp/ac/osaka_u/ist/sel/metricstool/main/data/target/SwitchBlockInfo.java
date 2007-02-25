package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;

/**
 * switch ブロックを表すクラス
 * 
 * @author y-higo
 * 
 */
public class SwitchBlockInfo extends BlockInfo {

    /**
     * switch ブロック情報を初期化
     * 
     */
    public SwitchBlockInfo(final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * このswitch ブロックに case エントリを追加する
     * 
     * @param innerBlock 追加する case エントリ
     */
    @Override
    public final void addInnerBlock(final BlockInfo innerBlock) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == innerBlock) {
            throw new NullPointerException();
        }

        if (!(innerBlock instanceof CaseEntryInfo)) {
            throw new IllegalArgumentException(
                    "Inner block of switch statement must be case or default entry!");
        }

        super.addInnerBlock(innerBlock);
    }
}

