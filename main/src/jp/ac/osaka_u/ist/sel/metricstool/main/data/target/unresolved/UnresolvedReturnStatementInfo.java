package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReturnStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決return文情報を表すクラス
 * 
 * @author t-miyake
 *
 */
public class UnresolvedReturnStatementInfo extends
        UnresolvedSingleStatementInfo<ReturnStatementInfo> {

    /**
     * return文の戻り値を表す式の未解決情報を与えて初期化
     * 
     * @param returnedExpression
     */
    public UnresolvedReturnStatementInfo(
            UnresolvedExpressionInfo<? extends ExpressionInfo> returnedExpression) {
        super();
        
        if(null == returnedExpression) {
            throw new IllegalArgumentException("returnedExpression is null");
        }
        
        this.returnedExpression = returnedExpression;
    }

    @Override
    public ReturnStatementInfo resolve(TargetClassInfo usingClass, CallableUnitInfo usingMethod,
            ClassInfoManager classInfoManager, FieldInfoManager fieldInfoManager,
            MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == methodInfoManager)) {
            throw new IllegalArgumentException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final ExpressionInfo returnedExpression = this.returnedExpression.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        this.resolvedInfo = new ReturnStatementInfo(returnedExpression, fromLine, fromColumn,
                toLine, toColumn);

        return this.resolvedInfo;
    }

    /**
     * return文の戻り値を表す式の未解決情報を保存する変数
     */
    private final UnresolvedExpressionInfo<? extends ExpressionInfo> returnedExpression;

}
