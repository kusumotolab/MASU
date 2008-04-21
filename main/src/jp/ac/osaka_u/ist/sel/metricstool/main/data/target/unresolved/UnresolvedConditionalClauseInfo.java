package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalClauseInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決条件節を表すクラス
 * 
 * @author higo
 *
 */
public final class UnresolvedConditionalClauseInfo extends
        UnresolvedLocalSpaceInfo<ConditionalClauseInfo> {

    UnresolvedConditionalClauseInfo(
            final UnresolvedConditionalBlockInfo<? extends ConditionalBlockInfo> ownerBlock) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == ownerBlock) {
            throw new IllegalArgumentException("ownerBlock is null");
        }

        this.ownerBlock = ownerBlock;
        this.resolvedInfo = null;
    }

    @Override
    public ConditionalClauseInfo resolve(final TargetClassInfo usingClass,
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
            return this.getResolved();
        }

        // この for エントリの位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        this.resolvedInfo = new ConditionalClauseInfo(usingClass, fromLine, fromColumn, toLine,
                toColumn);
        
        // 条件節に対応したブロック文の情報を解決
        final ConditionalBlockInfo ownerBlock = this.ownerBlock.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        this.resolvedInfo.setOwnerBlock(ownerBlock);


        // ローカル変数情報を解決し，解決済み条件節オブジェクトに追加
        for (final UnresolvedLocalVariableInfo unresolvedVariable : this.getLocalVariables()) {
            final LocalVariableInfo variable = unresolvedVariable.resolve(usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addLocalVariable(variable);
        }

        this.resolveVariableUsages(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                methodInfoManager);

        return this.resolvedInfo;
    }

    private final UnresolvedConditionalBlockInfo<? extends ConditionalBlockInfo> ownerBlock;
}
