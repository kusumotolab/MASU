package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LabelInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決ラベル定義を表すクラス
 * 
 * @author higo
 *
 */
public final class UnresolvedLabelInfo extends UnresolvedUnitInfo<LabelInfo> implements
        UnresolvedStatementInfo<LabelInfo> {

    /**
     * ラベル名を与えて，オブジェクトを初期化
     * 
     * @param name ラベル名
     */
    public UnresolvedLabelInfo(final String name) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == name) {
            throw new IllegalArgumentException();
        }

        this.name = name;
        this.labeledStatement = null;
        this.resolvedInfo = null;
    }
    
    @Override
    public final int compareTo(UnresolvedStatementInfo<LabelInfo> o) {

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
     * 名前解決を行うメソッド
     */
    @Override
    public LabelInfo resolve(final TargetClassInfo usingClass, final CallableUnitInfo usingMethod,
            final ClassInfoManager classInfoManager, final FieldInfoManager fieldInfoManager,
            final MethodInfoManager methodInfoManager) {

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

        // このラベルの位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // このラベルの名前を取得
        final String name = this.getName();

        // このラベルが付いた文を取得
        final UnresolvedStatementInfo<?> unresolvedLabeledStatement = this.getLabeledStatement();
        final StatementInfo labeledStatement = unresolvedLabeledStatement.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        this.resolvedInfo = new LabelInfo(name, labeledStatement, fromLine, fromColumn, toLine,
                toColumn);
        return this.resolvedInfo;
    }

    /**
     * このラベルの名前を返す
     * 
     * @return このラベルの名前
     */
    public String getName() {
        return this.name;
    }

    /**
     * このラベルが付いた文をセットする
     * 
     * @param labeledStatement このラベルが付いた文
     */
    public void setLabeledStatement(final UnresolvedStatementInfo<?> labeledStatement) {
        this.labeledStatement = labeledStatement;
    }

    /**
     * このラベルが付いた文を返す
     * 
     * @return このラベルが付いた文
     */
    public UnresolvedStatementInfo<?> getLabeledStatement() {
        return this.labeledStatement;
    }

    private String name;

    private UnresolvedStatementInfo<?> labeledStatement;
}
