package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;


/**
 * if文やwhile文などのメソッド内の構造（ブロック）を表すためのクラス
 * 
 * @author higo
 * @param <T> 解決済みのブロックの型
 * 
 */
public abstract class UnresolvedBlockInfo<T extends BlockInfo> extends UnresolvedLocalSpaceInfo<T>
        implements UnresolvedStatementInfo<T> {

    /**
     * このブロックの外側に位置するブロックを与えて，オブジェクトを初期化
     * 
     * @param outerSpace このブロックの外側に位置するブロック
     * 
     */
    public UnresolvedBlockInfo(final UnresolvedLocalSpaceInfo<?> outerSpace) {
        super();

        if (null == outerSpace) {
            throw new IllegalArgumentException("outerSpace is null");
        }

        this.outerSpace = outerSpace;
    }
    
    @Override
    public final int compareTo(UnresolvedStatementInfo<T> o) {

        if (null == o) {
            throw new NullPointerException();
        }

        if (this.getFromLine() < o.getFromLine()) {
            return -1;
        } else if (this.getFromLine() > o.getFromLine()) {
            return 1;
        } else if (this.getFromColumn() < o.getFromColumn()) {
            return -1;
        } else if (this.getFromColumn() > o.getFromColumn()) {
            return 1;
        } else if (this.getToLine() < o.getToLine()) {
            return -1;
        } else if (this.getToLine() > o.getToLine()) {
            return 1;
        } else if (this.getToColumn() < o.getToColumn()) {
            return -1;
        } else if (this.getToColumn() > o.getToColumn()) {
            return 1;
        }

        return 0;
    }

    /**
     * このブロックが属する空間を返す
     * @return このブロックが属する空間
     */
    public UnresolvedLocalSpaceInfo<?> getOuterSpace() {
        return this.outerSpace;
    }

    /**
     * このブロックが属する空間を保存するための変数
     */
    private final UnresolvedLocalSpaceInfo<?> outerSpace;

}
