package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CaseEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SwitchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * switch 文の case エントリを表すクラス
 * 
 * @author higo
 */
public class UnresolvedCaseEntryInfo extends UnresolvedUnitInfo<CaseEntryInfo> implements
        UnresolvedStatementInfo<CaseEntryInfo> {

    /**
     * 対応する switch ブロック情報を与えて case エントリを初期化
     * 
     * @param ownerSwitchBlock 対応する switch ブロック
     * @param name ラベルの名前
     * 
     */
    public UnresolvedCaseEntryInfo(final UnresolvedSwitchBlockInfo ownerSwitchBlock,
            final UnresolvedEntityUsageInfo<?> label) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == ownerSwitchBlock) {
            throw new IllegalArgumentException("ownerSwitchBlock is null");
        }
        if ((null == label) || !((label instanceof UnresolvedLiteralUsageInfo)
                || (label instanceof UnresolvedClassReferenceInfo))) {
            throw new IllegalArgumentException("label is incorrect");
        }

        this.ownerSwitchBlock = ownerSwitchBlock;
        this.label = label;
    }

    /**
     * 対応する switch ブロック情報を与えて case エントリを初期化．
     * なお，このコンストラクタは default エントリ用のものである．
     * 
     * @param ownerSwitchBlock 対応する switch ブロック
     */
    protected UnresolvedCaseEntryInfo(final UnresolvedSwitchBlockInfo ownerSwitchBlock) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == ownerSwitchBlock) {
            throw new IllegalArgumentException("ownerSwitchBlock is null");
        }
        this.ownerSwitchBlock = ownerSwitchBlock;
        this.label = null;
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

        // この case エントリのラベルを取得
        final UnresolvedEntityUsageInfo<?> unresolvedLabel = this.getLabel();
        final EntityUsageInfo label = unresolvedLabel.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);

        // この case エントリの位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        //　解決済み case エントリオブジェクトを作成
        this.resolvedInfo = new CaseEntryInfo(ownerSwitchBlock, label, fromLine, fromColumn,
                toLine, toColumn);
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
     * この case エントリのラベルを返す
     * 
     * @return この case エントリのラベル
     */
    public final UnresolvedEntityUsageInfo<?> getLabel() {
        return this.label;
    }

    /**
     * この case エントリが属する switch ブロックを保存するための変数
     */
    private final UnresolvedSwitchBlockInfo ownerSwitchBlock;

    /**
     * この case エントリのラベルを保存する変数
     */
    private final UnresolvedEntityUsageInfo<?> label;
}
