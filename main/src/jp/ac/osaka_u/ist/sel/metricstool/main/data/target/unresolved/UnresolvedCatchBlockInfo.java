package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CatchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TryBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決 catch ブロック情報を表すクラス
 * 
 * @author y-higo
 */
public final class UnresolvedCatchBlockInfo extends UnresolvedBlockInfo<CatchBlockInfo> {

    /**
     * 対応するtry文情報を与えて catch ブロックを初期化
     * 
     * @param ownerTryBlock 対応するtry文
     */
    public UnresolvedCatchBlockInfo(final UnresolvedTryBlockInfo ownerTryBlock) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == ownerTryBlock) {
            throw new NullPointerException();
        }

        this.ownerTryBlock = ownerTryBlock;
    }

    /**
     * この未解決 catch 節を解決する
     * 
     * @param usingClass 所属クラス
     * @param usingMethod 所属メソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManger 用いるメソッドマネージャ
     */
    public CatchBlockInfo resolveUnit(final TargetClassInfo usingClass,
            final TargetMethodInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == fieldInfoManager) || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolvedUnit();
        }

        // この catch 節が属する try 文を取得
        final UnresolvedTryBlockInfo unresolvedOwnerTryBlock = this.getOwnerTryBlock();
        final TryBlockInfo ownerTryBlock = unresolvedOwnerTryBlock.resolveUnit(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        // この case エントリの位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        this.resolvedInfo = new CatchBlockInfo(usingClass, usingMethod, fromLine, fromColumn,
                toLine, toColumn, ownerTryBlock);
        return this.resolvedInfo;
    }

    /**
     * 対応する try ブロックを返す
     * 
     * @return 対応する try ブロック
     */
    public UnresolvedTryBlockInfo getOwnerTryBlock() {
        return this.ownerTryBlock;
    }

    private final UnresolvedTryBlockInfo ownerTryBlock;
}
