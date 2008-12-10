package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.AssertStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * assert文の未解決情報を表すクラス
 * 
 * @author t-miyake
 *
 */
public class UnresolvedAssertStatementInfo extends
        UnresolvedSingleStatementInfo<AssertStatementInfo> {

    public UnresolvedAssertStatementInfo(
            final UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> ownerSpace) {
        super(ownerSpace);
    }

    @Override
    public AssertStatementInfo resolve(TargetClassInfo usingClass, CallableUnitInfo usingMethod,
            ClassInfoManager classInfoManager, FieldInfoManager fieldInfoManager,
            MethodInfoManager methodInfoManager) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 検証の結果がfalseであったときに出力されるメッセージを表す式の未解決情報を設定する
     * @param messageExpression
     */
    public final void setMessageExpression(
            final UnresolvedExpressionInfo<? extends ExpressionInfo> messageExpression) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        this.messsageExpression = messageExpression;
    }

    /**
     * 検証式の未解決情報を設定する
     * @param assertedExpression 検証式の未解決情報
     */
    public final void setAsserttedExpression(
            final UnresolvedExpressionInfo<? extends ExpressionInfo> assertedExpression) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        this.assertedExpression = assertedExpression;
    }

    /**
     * 検証式の未解決情報を保存する変数
     */
    private UnresolvedExpressionInfo<? extends ExpressionInfo> assertedExpression;

    /**
     * 検証式がfalseを返すときに出力されるメッセージを表す式の未解決情報を保存するための変数
     */
    private UnresolvedExpressionInfo<? extends ExpressionInfo> messsageExpression;

}
