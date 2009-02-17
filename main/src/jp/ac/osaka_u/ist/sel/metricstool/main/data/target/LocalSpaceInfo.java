package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ローカル領域(メソッドやメソッド内ブロック)を表すクラス
 * 
 * @author higo
 *
 */
public abstract class LocalSpaceInfo extends UnitInfo {

    /**
     * 必要な情報を与えてオブジェクトを居幾何
     * 
     * @param ownerClass このローカル領域を定義しているクラス
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn終了列
     */
    LocalSpaceInfo(final ClassInfo ownerClass, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        this.ownerClass = ownerClass;
        this.statements = new TreeSet<StatementInfo>();
    }

    /**
     * このローカルスペース内で定義された変数のSetを返す
     * 
     * @return このローカルスペース内で定義された変数のSet
     */
    @Override
    public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        final Set<VariableInfo<? extends UnitInfo>> definedVariables = new HashSet<VariableInfo<? extends UnitInfo>>();
        for (final StatementInfo statement : this.getStatements()) {
            definedVariables.addAll(statement.getDefinedVariables());
        }
        return Collections.unmodifiableSet(definedVariables);
    }

    /**
     * このローカル領域に文を追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param statement 追加する文
     */
    public void addStatement(final StatementInfo statement) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == statement) {
            throw new NullPointerException();
        }

        this.statements.add(statement);
    }

    /**
     * メソッドおよびコンストラクタ呼び出し一覧を返す
     * 
     * @return メソッドおよびコンストラクタ呼び出し
     */
    @Override
    public Set<CallInfo<?>> getCalls() {
        final Set<CallInfo<?>> calls = new HashSet<CallInfo<?>>();
        for (final StatementInfo statement : this.getStatements()) {
            calls.addAll(statement.getCalls());
        }
        return Collections.unmodifiableSet(calls);
    }

    /**
     * このローカル領域の変数利用のSetを返す
     * 
     * @return このローカル領域の変数利用のSet
     */
    @Override
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        final Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> variableUsages = new HashSet<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>>();
        for (final StatementInfo statement : this.getStatements()) {
            variableUsages.addAll(statement.getVariableUsages());
        }
        return Collections.unmodifiableSet(variableUsages);
    }

    /**
     * このローカルスペースの直内の文情報の SortedSet を返す．
     * 
     * @return このローカルスペースの直内の文情報の SortedSet
     */
    public SortedSet<StatementInfo> getStatements() {
        return Collections.unmodifiableSortedSet(this.statements);
    }

    /**
     * 所属しているクラスを返す
     * 
     * @return 所属しているクラス
     */
    public final ClassInfo getOwnerClass() {
        return this.ownerClass;
    }

    public static SortedSet<StatementInfo> getAllStatements(final LocalSpaceInfo localSpace) {
        if (null == localSpace) {
            new NullPointerException("localSpace is null");
        }

        if (localSpace instanceof ExternalMethodInfo
                || localSpace instanceof ExternalConstructorInfo) {
            new IllegalArgumentException("localSpace is an external local space.");
        }
        
        final SortedSet<StatementInfo> allStatements = new TreeSet<StatementInfo>();
        for (final StatementInfo innerStatement : localSpace.getStatements()) {
            allStatements.add(innerStatement);
            if (innerStatement instanceof BlockInfo) {
                allStatements.addAll(LocalSpaceInfo.getAllStatements((BlockInfo) innerStatement));
            }
        }
        return allStatements;
    }

    /**
     * このローカルスコープの直内の文情報一覧を保存するための変数
     */
    private final SortedSet<StatementInfo> statements;

    /**
     * 所属しているクラスを保存するための変数
     */
    private final ClassInfo ownerClass;
}
