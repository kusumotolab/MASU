package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;


/**
 * if文やwhile文などのメソッド内の構造（ブロック）を表すためのクラス
 * 
 * @author higo
 * 
 */
public abstract class UnresolvedBlockInfo<T extends BlockInfo> extends UnresolvedLocalSpaceInfo<T> {

    /**
     * ブロック構造を表すオブジェクトを初期化する
     * 
     */
    public UnresolvedBlockInfo() {
        super();
    }

    /**
     * このブロックが既に解決されているかどうかをかえす
     * 
     * @return 既に解決されている場合は true, そうでない場合は false
     */
    @Override
    public final boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * このブロックの解決済みオブジェクトを返す
     */
    @Override
    public final T getResolvedUnit() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }

    /**
     * 解決済みブロック情報を保存するための変数
     */
    protected T resolvedInfo;
}
