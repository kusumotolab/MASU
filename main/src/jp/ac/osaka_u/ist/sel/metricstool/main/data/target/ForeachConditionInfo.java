package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * Foreach ���̎���\���N���X
 * 
 * @author higo
 *
 */
public final class ForeachConditionInfo extends ExpressionInfo {

    public ForeachConditionInfo(final CallableUnitInfo ownerMethod, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn,
            final LocalVariableInfo iteratorVariable, final ExpressionInfo iteratorExpression) {

        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);

        this.iteratorVariable = iteratorVariable;
        this.iteratorExpression = iteratorExpression;

        this.iteratorExpression.setOwnerExecutableElement(this);
    }

    public LocalVariableInfo getIteratorVariable() {
        return this.iteratorVariable;
    }

    public ExpressionInfo getIteratorExpression() {
        return this.iteratorExpression;
    }

    @Override
    public TypeInfo getType() {
        return this.getIteratorVariable().getType();
    }

    @Override
    public Set<CallInfo<?>> getCalls() {
        return this.getIteratorExpression().getCalls();
    }

    @Override
    public String getText() {

        final StringBuilder text = new StringBuilder();

        text.append(this.getIteratorVariable().getType().getTypeName());
        text.append(" ");
        text.append(this.getIteratorVariable().getName());
        text.append(" : ");
        text.append(this.getIteratorExpression().getText());

        return text.toString();
    }

    @Override
    public Set<ClassTypeInfo> getThrownExceptions() {
        return this.getIteratorExpression().getThrownExceptions();
    }

    @Override
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        final Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> variableUsages = new HashSet<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>>();
        variableUsages.addAll(this.getIteratorExpression().getVariableUsages());
        variableUsages.add(LocalVariableUsageInfo.getInstance(this.getIteratorVariable(), false,
                true, this.getOwnerMethod(), this.getFromLine(), this.getFromColumn(), this
                        .getToLine(), this.getToColumn()));
        return Collections.unmodifiableSet(variableUsages);
    }

    private final LocalVariableInfo iteratorVariable;

    private final ExpressionInfo iteratorExpression;
}