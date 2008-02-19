package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalClauseInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ElseBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.IfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決ifブロックを表すクラス
 * 
 * @author higo
 * 
 */
public final class UnresolvedIfBlockInfo extends UnresolvedConditionalBlockInfo<IfBlockInfo> {

    /**
     * if ブロック情報を初期化
     */
    public UnresolvedIfBlockInfo(final UnresolvedLocalSpaceInfo<?> ownerSpace) {
        super(ownerSpace);

        this.sequentElseBlock = null;
    }

    /**
     * この未解決 if ブロックを解決する
     * 
     * @param usingClass 所属クラス
     * @param usingMethod 所属メソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManger 用いるメソッドマネージャ
     */
    @Override
    public IfBlockInfo resolveUnit(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolvedUnit();
        }

        // この if文 の条件節を解決する
        final UnresolvedConditionalClauseInfo unresolvedConditionalClause = this
                .getConditionalClause();
        final ConditionalClauseInfo conditionalClause = unresolvedConditionalClause.resolveUnit(
                usingClass, usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        // この if文の位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        this.resolvedInfo = new IfBlockInfo(usingClass, usingMethod, conditionalClause, fromLine,
                fromColumn, toLine, toColumn);

        // もしelseブロックがある場合は解決する
        if (this.hasElseBlock()) {
            final UnresolvedElseBlockInfo unresolvedElseBlockInfo = this.getSequentElseBlock();
            final ElseBlockInfo sequentBlockInfo = unresolvedElseBlockInfo.resolveUnit(usingClass,
                    usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.setSequentElseBlock(sequentBlockInfo);
        }

        //　内部ブロック情報を解決し，解決済みifブロックオブジェクトに追加
        for (final UnresolvedBlockInfo<?> unresolvedInnerBlock : this.getInnerBlocks()) {
            final BlockInfo innerBlock = unresolvedInnerBlock.resolveUnit(usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addInnerBlock(innerBlock);
        }

        // ローカル変数情報を解決し，解決済みifブロックオブジェクトに追加
        for (final UnresolvedLocalVariableInfo unresolvedVariable : this.getLocalVariables()) {
            final LocalVariableInfo variable = unresolvedVariable.resolveUnit(usingClass,
                    usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addLocalVariable(variable);
        }

        return this.resolvedInfo;
    }

    /**
     * 対応するelseブロックを返す
     * @return 対応するelseブロック．対応するelseブロックが存在しない場合はnull
     */
    public UnresolvedElseBlockInfo getSequentElseBlock() {
        return this.sequentElseBlock;
    }

    /**
     * 対応するelseブロックをセットする
     * @param elseBlock 対応するelseブロック
     */
    public void setSequentElseBlock(UnresolvedElseBlockInfo elseBlock) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == elseBlock) {
            throw new IllegalArgumentException("elseBlock is null");
        }

        this.sequentElseBlock = elseBlock;
    }

    /**
     * 対応するelseブロックが存在するかどうか表す
     * @return 対応するelseブロックが存在するならtrue
     */
    public boolean hasElseBlock() {
        return null != this.sequentElseBlock;
    }

    /**
     * 対応するelseブロックを保存する変数
     */
    private UnresolvedElseBlockInfo sequentElseBlock;

}
