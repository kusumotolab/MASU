package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * メソッド呼び出しを表すクラス
 * 
 * @author higo
 *
 */
public final class MethodCallInfo extends CallInfo {

    /**
     * 呼び出されるメソッドを与えてオブジェクトを初期化
     * 
     * @param callee 呼び出されるメソッド
     */
    public MethodCallInfo(final MethodInfo callee, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        if (null == callee) {
            throw new NullPointerException();
        }

        this.callee = callee;
    }

    /**
     * このメソッド呼び出しの型を返す
     */
    @Override
    public TypeInfo getType() {
        final MethodInfo callee = this.getCallee();
        return callee.getReturnType();
    }

    /**
     * このメソッド呼び出しで呼び出されているメソッドを返す
     * @return
     */
    public MethodInfo getCallee() {
        return this.callee;
    }

    private final MethodInfo callee;
}
