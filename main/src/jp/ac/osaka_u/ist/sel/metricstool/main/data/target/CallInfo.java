package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * メソッド呼び出し，コンストラクタ呼び出しの共通の親クラス
 * 
 * @author higo
 *
 */
public abstract class CallInfo extends EntityUsageInfo {

    /**
     * 位置情報を与えてオブジェクトを初期化
     */
    CallInfo(final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);
        this.arguments = new LinkedList<ExpressionInfo>();
        this.typeArguments = new LinkedList<ReferenceTypeInfo>();
    }

    /**
     * このメソッド呼び出しの実引数を追加．プラグインからは呼び出せない．
     * 
     * @param argument 追加する実引数
     */
    public final void addArgument(final EntityUsageInfo argument) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == argument) {
            throw new NullPointerException();
        }

        this.arguments.add(argument);
    }

    /**
     * この呼び出しの実引数を追加．プラグインからは呼び出せない．
     * 
     * @param arguments 追加する実引数
     */
    public final void addArguments(final List<ExpressionInfo> arguments) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == arguments) {
            throw new NullPointerException();
        }

        this.arguments.addAll(arguments);
    }

    /**
     * このメソッド呼び出しの型引数を追加．プラグインからは呼び出せない
     * 
     * @param typeArgument 追加する型引数
     */
    public final void addTypeArgument(final ReferenceTypeInfo typeArgument) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeArgument) {
            throw new NullPointerException();
        }

        this.typeArguments.add(typeArgument);
    }

    /**
     * この呼び出しの型引数を追加．プラグインからは呼び出せない．
     * 
     * @param typeArguments 追加する型引数
     */
    public final void addTypeArguments(final List<ReferenceTypeInfo> typeArguments) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeArguments) {
            throw new NullPointerException();
        }

        this.typeArguments.addAll(typeArguments);
    }

    /**
     * この呼び出しの実引数のListを返す．
     * 
     * @return　この呼び出しの実引数のList
     */
    public List<ExpressionInfo> getArguments() {
        return Collections.unmodifiableList(this.arguments);
    }

    /**
     * この呼び出しにおける変数使用群を返す
     * 
     * @return この呼び出しにおける変数使用群
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        final SortedSet<VariableUsageInfo<?>> variableUsages = new TreeSet<VariableUsageInfo<?>>();
        for (final ExpressionInfo parameter : this.getArguments()) {
            variableUsages.addAll(parameter.getVariableUsages());
        }
        return Collections.unmodifiableSortedSet(variableUsages);
    }

    private final List<ExpressionInfo> arguments;

    private final List<ReferenceTypeInfo> typeArguments;
}
