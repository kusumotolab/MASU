package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ASTParseException;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.StateDrivenDataBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.ExpressionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedParenthesesExpressionInfo;


/**
 * 括弧式情報を構築するクラス 
 * <br>
 * 式を構成する要素ではあるが，式の構文木情報に影響を与えてはいけないため， 
 * {@link ExpressionBuilder}の子クラスにはしていない．
 * 
 * @author g-yamada
 * 
 */
public class ParenthesesExpressionBuilder extends
        StateDrivenDataBuilder<UnresolvedParenthesesExpressionInfo> {

    /**
     * オブジェクトを初期化する
     * 
     * @param expressionManager ExpressionElementManager
     * @param buildDataManager 
     */
    public ParenthesesExpressionBuilder(final ExpressionElementManager expressionManager,
            BuildDataManager buildDataManager) {
        if (null == buildDataManager || null == expressionManager) {
            throw new IllegalArgumentException();
        }

        this.buildDataManager = buildDataManager;
        this.expressionManager = expressionManager;

        this.addStateManager(this.expressionStateManger);
    }

    @Override
    public void entered(final AstVisitEvent e) {
        super.entered(e);
    }

    /**
     * exit したノードが括弧式なら，UnresolvedParenthesesExpressionInfoをつくる命令をする
     */
    @Override
    public void exited(AstVisitEvent e) throws ASTParseException {
        super.exited(e);
        final AstToken token = e.getToken();
        if (this.isActive() && this.expressionStateManger.inExpression()
                && token.isParenthesesExpression()) {
            this.buildParenthesesExpressionBuilder(e);
        }
    }

    /**
     * 命令されて実際にUnresolvedParenthesesExpressionInfoをつくる
     */
    protected void buildParenthesesExpressionBuilder(final AstVisitEvent e) {
        // ExpressionManagerのexpressionAnalyzeStackの頭の要素が，括弧式の直下にくるExpressionElement
        // ポップする必要があるかわからないのでスタックはいじらない
        final ExpressionElement parentheticElement = expressionManager.getPeekExpressionElement();
        final UnresolvedExpressionInfo<? extends ExpressionInfo> parentheticExpression = parentheticElement
                .getUsage();

        if (null != parentheticExpression) {
            // expressionAnalyzeStackの頭の要素をポップして，かわりに括弧式をプッシュする
            expressionManager.popExpressionElement();
            final UnresolvedParenthesesExpressionInfo paren = new UnresolvedParenthesesExpressionInfo(
                    parentheticExpression);
            paren.setFromLine(e.getStartLine());
            paren.setFromColumn(e.getStartColumn());
            paren.setToLine(e.getEndLine());
            paren.setToColumn(e.getEndColumn());
            expressionManager.pushExpressionElement(new UsageElement(paren));
        } else {
            // TODO (a)のような場合の括弧もとれるようにする
            /*
             * ここにくるのは(a)みたいに括弧式の直下に変数がきているとき．
             * expressionAnalyzeStackをいじらないため，結果として括弧式がなかったかのような
             * 振る舞いをする．
             * 
             * カッコ式直下に変数がきている場合，ASTの括弧式からexitする時点では
             * まだ内部の変数のUsageが存在しない．
             * 変数ElementのresolveAsVariableを呼び出すことでUsageを生成するのだが，
             * この場所では変数が被代入なのか代入なのかわからないため呼び出せない．
             */
        }
    }

    @Override
    public void stateChanged(StateChangeEvent<AstVisitEvent> event) {
        // nothing to do
    }

    protected final ExpressionElementManager expressionManager;

    protected final BuildDataManager buildDataManager;

    private final ExpressionStateManager expressionStateManger = new ExpressionStateManager();
}