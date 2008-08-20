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
        this.parameters = new LinkedList<EntityUsageInfo>();
        this.typeArguments = new LinkedList<ReferenceTypeInfo>();
    }

    /**
     * このメソッド呼び出しの実引数を追加．プラグインからは呼び出せない．
     * 
     * @param parameter 追加する実引数
     */
    public final void addParameter(final EntityUsageInfo parameter) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == parameter) {
            throw new NullPointerException();
        }

        this.parameters.add(parameter);
    }

    /**
     * この呼び出しの実引数を追加．プラグインからは呼び出せない．
     * 
     * @param parameters 追加する実引数
     */
    public final void addParameters(final List<EntityUsageInfo> parameters) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == parameters) {
            throw new NullPointerException();
        }

        this.parameters.addAll(parameters);
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
     * @return
     */
    public List<EntityUsageInfo> getParameters() {
        return Collections.unmodifiableList(this.parameters);
    }

    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        final SortedSet<VariableUsageInfo<?>> variableUsages = new TreeSet<VariableUsageInfo<?>>();
        for (EntityUsageInfo parameter : this.getParameters()) {
            variableUsages.addAll(parameter.getVariableUsages());
        }
        return Collections.unmodifiableSortedSet(variableUsages);
    }

    private final List<EntityUsageInfo> parameters;

    private final List<ReferenceTypeInfo> typeArguments;
}
