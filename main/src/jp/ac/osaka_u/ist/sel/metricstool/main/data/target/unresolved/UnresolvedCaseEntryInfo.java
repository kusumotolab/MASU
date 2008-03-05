package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CaseEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SwitchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * switch 文の case エントリを表すクラス
 * 
 * @author higo
 */
public class UnresolvedCaseEntryInfo extends UnresolvedBlockInfo<CaseEntryInfo> {

    /**
     * 対応する switch ブロック情報を与えて case エントリを初期化
     * 
     * @param ownerSwitchBlock
     */
    public UnresolvedCaseEntryInfo(final UnresolvedSwitchBlockInfo ownerSwitchBlock,
            final UnresolvedLocalSpaceInfo<?> outerSpace) {
        super(outerSpace);
        if (null == ownerSwitchBlock) {
            throw new IllegalArgumentException("ownerSwitchBlock is null");
        }

        this.ownerSwitchBlock = ownerSwitchBlock;
        this.breakStatement = false;
    }

    /**
     * この未解決 case エントリを解決する
     * 
     * @param usingClass 所属クラス
     * @param usingMethod 所属メソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManger 用いるメソッドマネージャ
     */
    @Override
    public CaseEntryInfo resolve(final TargetClassInfo usingClass,
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

        // この case エントリが属する switch 文を取得
        final UnresolvedSwitchBlockInfo unresolvedOwnerSwitchBlock = this.getOwnerSwitchBlock();
        final SwitchBlockInfo ownerSwitchBlock = unresolvedOwnerSwitchBlock.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        // break 文の有無を取得
        final boolean breakStatement = this.hasBreakStatement();

        // この case エントリの位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        //　解決済み case エントリオブジェクトを作成
        this.resolvedInfo = new CaseEntryInfo(usingClass, usingMethod, ownerSwitchBlock,
                breakStatement, fromLine, fromColumn, toLine, toColumn);

        //　直内の未解決文情報を解決し，解決済みcaseエントリオブジェクトに追加
        for (final UnresolvedStatementInfo<?> unresolvedStatement : this.getStatements()) {
            final StatementInfo statement = unresolvedStatement.resolve(usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addStatement(statement);
        }

        // ローカル変数情報を解決し，解決済みcaseエントリオブジェクトに追加
        for (final UnresolvedLocalVariableInfo unresolvedVariable : this.getLocalVariables()) {
            final LocalVariableInfo variable = unresolvedVariable.resolve(usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addLocalVariable(variable);
        }

        return this.resolvedInfo;
    }

    /**
     * この case エントリが属する switch ブロックを返す
     * 
     * @return この case エントリが属する switch ブロック
     */
    public final UnresolvedSwitchBlockInfo getOwnerSwitchBlock() {
        return this.ownerSwitchBlock;
    }

    /**
     * この case エントリが break 文を持つかどうかを設定する
     * 
     * @param breakStatement break 文を持つ場合は true, 持たない場合は false
     */
    public final void setHasBreak(final boolean breakStatement) {
        this.breakStatement = breakStatement;
    }

    /**
     * この case エントリが break 文を持つかどうかを返す
     * 
     * @return break 文を持つ場合はtrue，持たない場合は false
     */
    public final boolean hasBreakStatement() {
        return this.breakStatement;
    }

    /**
     * この case エントリが属する switch ブロックを保存するための変数
     */
    private final UnresolvedSwitchBlockInfo ownerSwitchBlock;

    /**
     * この case エントリが break 文を持つかどうかを保存する変数
     */
    private boolean breakStatement;
}
