package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.VariableDefinitionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.VariableDefinitionStateManager.VARIABLE_STATE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableInfo;


public abstract class InitializableVariableBuilder<TVar extends UnresolvedVariableInfo, TUnit extends UnresolvedUnitInfo<? extends UnitInfo>>
        extends VariableBuilder<TVar, TUnit> {

    public InitializableVariableBuilder(BuildDataManager buildDataManager,
            final ExpressionElementManager expressionManager,
            VariableDefinitionStateManager variableStateManager, ModifiersBuilder modifiersBuilder,
            TypeBuilder typeBuilder, NameBuilder nameBuilder) {
        super(buildDataManager, variableStateManager, modifiersBuilder, typeBuilder, nameBuilder);

        if (null == expressionManager) {
            throw new IllegalArgumentException("expressionManager is null");
        }

        this.expressionManager = expressionManager;
    }

    @Override
    public void stateChangend(final StateChangeEvent<AstVisitEvent> event) {
        super.stateChangend(event);

        final StateChangeEventType eventType = event.getType();
        if (eventType.equals(VARIABLE_STATE.ENTER_VARIABLE_DEF)) {

            // 変数の初期化式があるかどうかわからないのでとりあえずnullをいれる
            this.builtInitializerStack.push(null);

        } else if (eventType.equals(VARIABLE_STATE.ENTER_VARIABLE_INITIALIZER)) {

            // 初期化式が存在したので宣言部に入ったときにスタックに積んだnullを除去
            assert this.builtInitializerStack.peek() == null : "Illegal state: incorrect stack state";
            this.builtInitializerStack.pop();

        } else if (eventType.equals(VARIABLE_STATE.EXIT_VARIABLE_INITIALIZER)) {

            ExpressionElement lastExpression = expressionManager.getLastPoppedExpressionElement();

            assert (lastExpression.getUsage() instanceof UnresolvedExpressionInfo) : "Illegal state: variable initilizer was not a expression";

            this.builtInitializerStack
                    .push((UnresolvedExpressionInfo<? extends ExpressionInfo>) lastExpression
                            .getUsage());
        }
    }

    /**
     * 構築した初期化式を格納しておくスタック
     */
    protected final Stack<UnresolvedExpressionInfo<? extends ExpressionInfo>> builtInitializerStack = new Stack<UnresolvedExpressionInfo<? extends ExpressionInfo>>();

    protected final ExpressionElementManager expressionManager;
}
