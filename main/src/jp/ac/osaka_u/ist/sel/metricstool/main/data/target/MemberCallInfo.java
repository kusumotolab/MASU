package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * メソッド呼び出し，コンストラクタ呼び出しの共通の親クラス
 * 
 * @author higo
 *
 */
public abstract class MemberCallInfo extends EntityUsageInfo {

    /**
     * 呼び出されているメソッドまたはコンストラクタを与えて初期化
     * 
     * @param callee
     */
    MemberCallInfo(final MethodInfo callee, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        if (null == callee) {
            throw new NullPointerException();
        }

        this.callee = callee;
    }

    public final MethodInfo getCallee() {
        return this.callee;
    }

    private final MethodInfo callee;
}
