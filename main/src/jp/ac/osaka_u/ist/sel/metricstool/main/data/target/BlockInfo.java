package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
     * @param ownerSpace このブロックを所有するブロック
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    BlockInfo(final TargetClassInfo ownerClass, final LocalSpaceInfo outerSpace,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(ownerClass, fromLine, fromColumn, toLine, toColumn);

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == ownerClass) || (null == outerSpace)) {
            throw new IllegalArgumentException();
        }

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
     * このブロックを所有するを返す
     * 
     * @return このブロックを所有するメソッド
     */
    @Override
    public final CallableUnitInfo getOwnerMethod() {

        final LocalSpaceInfo outerSpace = this.getOwnerSpace();
        if (outerSpace instanceof CallableUnitInfo) {
            return (CallableUnitInfo) outerSpace;
        }

        if (outerSpace instanceof BlockInfo) {
            return ((BlockInfo) outerSpace).getOwnerMethod();
        }

        throw new IllegalStateException();
    }

    /**
     * このブロックが繰り返し文であるかどうか返す
     * @return 繰り返し文であるならtrue
     */
    public boolean isLoopStatement() {
        return false;
    }

    /**
     * このブロックを直接所有するローカル空間を返す
     * 
     * @return このブロックを直接所有するローカル空間
     */
    @Override
    public final LocalSpaceInfo getOwnerSpace() {
        return this.outerSpace;
    }

    /**
     * この式で投げられる可能性がある例外のSetを返す
     * 
     * @return　この式で投げられる可能性がある例外のSet
     */
    @Override
    public Set<ClassTypeInfo> getThrownExceptions() {
        final Set<ClassTypeInfo> thrownExpressions = new HashSet<ClassTypeInfo>();
        for (final StatementInfo innerStatement : this.getStatements()) {
            thrownExpressions.addAll(innerStatement.getThrownExceptions());
        }
        return Collections.unmodifiableSet(thrownExpressions);
    }

    /**
     * このブロックを直接所有するローカル空間を保存するための変数
     */
    private final LocalSpaceInfo outerSpace;
}
