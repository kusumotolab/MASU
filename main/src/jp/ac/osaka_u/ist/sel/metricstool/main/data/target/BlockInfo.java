package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * if ブロックや for ブロックなど メソッド内の構造的なまとまりの単位を表す抽象クラス
 * 
 * @author higo
 */
public abstract class BlockInfo extends LocalSpaceInfo implements StatementInfo {

    /**
     * 位置情報を与えて初期化
     * 
     * @param ownerClass このブロックを所有するクラス
     * @param ownerMethod このブロックを所有するメソッド
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    BlockInfo(final TargetClassInfo ownerClass, final CallableUnitInfo ownerMethod,
            final LocalSpaceInfo outerSpace, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(ownerClass, fromLine, fromColumn, toLine, toColumn);

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == ownerClass) || (null == ownerMethod) || (null == outerSpace)) {
            throw new NullPointerException();
        }

        this.ownerMethod = ownerMethod;
        this.outerSpace = outerSpace;
    }

    /**
     * このブロックオブジェクトを他のブロックオブジェクトと比較する
     */
    @Override
    public final boolean equals(Object o) {

        if (null == o) {
            return false;
        }

        if (!(o instanceof BlockInfo)) {
            return false;
        }

        return 0 == this.compareTo((BlockInfo) o);
    }

    /**
     * このブロックオブジェクトを他のブロックオブジェクトと比較する
     */
    @Override
    public final int compareTo(StatementInfo o) {

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
     * このブロックオブジェクトのハッシュ値を返す
     * 
     * @return このブロックオブジェクトのハッシュ値
     */
    @Override
    public final int hashCode() {
        return this.getFromLine() + this.getFromColumn() + this.getToLine() + this.getToColumn();
    }

    /**
     * このブロックを所有するを返す
     * 
     * @return このブロックを所有するメソッド
     */
    public final CallableUnitInfo getOwnerMethod() {
        return this.ownerMethod;
    }

    /**
     * このブロックを直接所有するローカル空間を返す
     * 
     * @return このブロックを直接所有するローカル空間
     */
    public final LocalSpaceInfo getOuterSpace() {
        return this.outerSpace;
    }

    /**
     * このブロックを所有するメソッドを保存するための変数
     */
    private final CallableUnitInfo ownerMethod;

    /**
     * このブロックを直接所有するローカル空間を保存するための変数
     */
    private final LocalSpaceInfo outerSpace;
}
