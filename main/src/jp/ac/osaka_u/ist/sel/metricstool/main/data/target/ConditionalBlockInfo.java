package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * if　や while など，条件節を持ったブロック文を表すクラス
 * 
 * @author higo
 *
 */
public abstract class ConditionalBlockInfo extends BlockInfo {

    /**
     * 位置情報を与えて初期化
     * 
     * @param ownerClass このブロックを所有するクラス
     * @param ownerSpace このブロックを所有するブロック
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    ConditionalBlockInfo(final TargetClassInfo ownerClass, final LocalSpaceInfo outerSpace,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(ownerClass, outerSpace, fromLine, fromColumn, toLine, toColumn);

    }

    /**
     * 変数利用の一覧を返す．
     * 
     * @return 変数利用のSet
     */
    @Override
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        final Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> variableUsages = new HashSet<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>>();
        variableUsages.addAll(super.getVariableUsages());
        variableUsages.addAll(this.getConditionalClause().getVariableUsages());
        return Collections.unmodifiableSet(variableUsages);
    }

    /**
     * 定義された変数のSetを返す
     * 
     * @return 定義された変数のSet
     */
    @Override
    public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        final Set<VariableInfo<? extends UnitInfo>> definedVariables = new HashSet<VariableInfo<? extends UnitInfo>>();
        definedVariables.addAll(super.getDefinedVariables());
        definedVariables.addAll(this.getConditionalClause().getDefinedVariables());
        return Collections.unmodifiableSet(definedVariables);
    }

    /**
     * 呼び出し一覧を返す
     * 
     * @return 呼び出し一覧
     */
    @Override
    public Set<CallInfo<?>> getCalls() {
        final Set<CallInfo<?>> calls = new HashSet<CallInfo<?>>();
        calls.addAll(super.getCalls());

        final ConditionInfo condition = this.getConditionalClause().getCondition();
        calls.addAll(condition.getCalls());

        return Collections.unmodifiableSet(calls);
    }

    /**
     * この条件付ブロックの条件節を返す
     * 
     * @return　この条件付ブロックの条件節
     */
    public final ConditionalClauseInfo getConditionalClause() {
        return this.conditionalClause;
    }

    private ConditionalClauseInfo conditionalClause;
}
