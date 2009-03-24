package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ASTParseException;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedParenthesesExpressionInfo;


/**
 * 括弧式情報を構築するクラス
 * 
 * @author g-yamada
 * 
 */
public class ParenthesesExpressionBuilder extends ExpressionBuilder {

    /**
     * オブジェクトを初期化する
     * 
     * @param expressionManager ExpressionElementManager
     * @param buildDataManager 
     */
    public ParenthesesExpressionBuilder(final ExpressionElementManager expressionManager,
            BuildDataManager buildDataManager) {
        super(expressionManager, buildDataManager);
    }

    /**
     * exit したノードが括弧式なら，UnresolvedParenthesesExpressionInfoをつくる命令をする
     */
    @Override
    protected void afterExited(AstVisitEvent event) throws ASTParseException {
        final AstToken token = event.getToken();
        if (isTriggerToken(token)) {
            this.buildParenthesesExpressionBuilder();
        }
    }

    /**
     * 命令されて実際にUnresolvedParenthesesExpressionInfoをつくる
     */
    protected void buildParenthesesExpressionBuilder() {
        final ExpressionElement[] elements = this.getAvailableElements();
        if (elements.length > 0) {
            final ExpressionElement from = elements[0];
            final ExpressionElement to = elements[elements.length - 1];
            // expressionManager から最後に pop された ExpressionElement が
            // 括弧内の式(UnresolvedExpressionInfo)を持つ
            final UnresolvedExpressionInfo<? extends ExpressionInfo> parentheticExpression = expressionManager
                    .getLastPoppedExpressionElement().getUsage();

            if (null != parentheticExpression) {
                final UnresolvedParenthesesExpressionInfo paren = new UnresolvedParenthesesExpressionInfo(
                        parentheticExpression);
                paren.setFromLine(from.fromLine);
                paren.setFromColumn(from.fromColumn);
                paren.setToLine(to.toLine);
                paren.setToColumn(to.toColumn);
                expressionManager.pushExpressionElement(new UsageElement(paren));
            }
        }
    }

    /**
     * token が括弧式を表すのかどうかを返す
     */
    @Override
    protected boolean isTriggerToken(AstToken token) {
        return token.isParenthesesExpression();
    }

}
