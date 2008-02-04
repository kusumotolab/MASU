package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * メソッド呼び出しを表すクラス
 * 
 * @author higo
 *
 */
public final class MethodCallInfo extends MemberCallInfo {

    /**
     * 呼び出されるメソッドを与えてオブジェクトを初期化
     * 
     * @param callee 呼び出されるメソッド
     */
    public MethodCallInfo(final MethodInfo callee, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {
        super(callee, fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * このメソッド呼び出しの型を返す
     */
    @Override
    public TypeInfo getType() {
        final MethodInfo callee = super.getCallee();
        return callee.getReturnType();
    }
}
