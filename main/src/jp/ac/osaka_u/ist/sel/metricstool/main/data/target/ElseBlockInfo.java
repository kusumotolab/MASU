package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * else ブロックを表すクラス
 * 
 * @author y-higo
 */
public final class ElseBlockInfo extends BlockInfo {

    /**
     * 対応する if ブロックを与えて，else ブロック情報を初期化
     * 
     * @param correspondingIfBlock
     */
    ElseBlockInfo(final int fromLine, final int fromColumn, final int toLine, final int toColumn,
            final IfBlockInfo correspondingIfBlock) {

        super(fromLine, fromColumn, toLine, toColumn);

        if (null == correspondingIfBlock) {
            throw new NullPointerException();
        }

        this.correspondingIfBlock = correspondingIfBlock;
    }

    /**
     * この else ブロックと対応する if ブロックを返す
     * 
     * @return この else ブロックと対応する if ブロック
     */
    public IfBlockInfo getCorrespondingIfBlock() {
        return this.correspondingIfBlock;
    }

    /**
     * この else ブロックと対応する if ブロックを保存するための変数
     */
    private final IfBlockInfo correspondingIfBlock;
}
