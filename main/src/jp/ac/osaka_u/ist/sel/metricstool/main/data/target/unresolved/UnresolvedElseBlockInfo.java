package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ElseBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.IfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決 else ブロックを表すクラス
 * 
 * @author higo
 */
public final class UnresolvedElseBlockInfo extends UnresolvedBlockInfo<ElseBlockInfo> {

    /**
     * 外側のブロックと対応する if ブロックを与えて，else ブロック情報を初期化
     * 
     * @param ownerIfBlock
     * @param outerSpace 外側のブロック
     */
    public UnresolvedElseBlockInfo(final UnresolvedIfBlockInfo ownerIfBlock,
            final UnresolvedLocalSpaceInfo<?> outerSpace) {
        super(outerSpace);

        if (null == ownerIfBlock) {
            throw new IllegalArgumentException("ownerIfBlock is null");
        }

        this.ownerIfBlock = ownerIfBlock;
    }

    /**
     * この未解決 else ブロックを解決する
     * 
     * @param usingClass 所属クラス
     * @param usingMethod 所属メソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManger 用いるメソッドマネージャ
     */
    @Override
    public ElseBlockInfo resolve(final TargetClassInfo usingClass,
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

        // この else ブロックが属する if ブロックを取得
        final UnresolvedIfBlockInfo unresolvedOwnerIfBlock = this.getOwnerIfBlock();
        final IfBlockInfo ownerIfBlock = unresolvedOwnerIfBlock.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);

        // この else ブロックの位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final LocalSpaceInfo outerSpace = this.getOuterSpace().getResolved();

        this.resolvedInfo = new ElseBlockInfo(usingClass, usingMethod, outerSpace, fromLine,
                fromColumn, toLine, toColumn, ownerIfBlock);

        // 未解決ブロック文情報を解決し，解決済みオブジェクトに追加
        this.resolveInnerBlock(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                methodInfoManager);

        // ローカル変数情報を解決し，解決済みcaseエントリオブジェクトに追加
        for (final UnresolvedLocalVariableInfo unresolvedVariable : this.getLocalVariables()) {
            final LocalVariableInfo variable = unresolvedVariable.resolve(usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addLocalVariable(variable);
        }

        this.resolveVariableUsages(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                methodInfoManager);

        return this.resolvedInfo;
    }

    /**
     * この else ブロックと対応する if ブロックを返す
     * 
     * @return この else ブロックと対応する if ブロック
     */
    public UnresolvedIfBlockInfo getOwnerIfBlock() {
        return this.ownerIfBlock;
    }

    /**
     * この else ブロックと対応する if ブロックを保存するための変数
     */
    private final UnresolvedIfBlockInfo ownerIfBlock;
}
