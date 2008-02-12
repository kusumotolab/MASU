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
    public MethodCallInfo(final TypeInfo ownerType, final MethodInfo callee, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        if ((null == ownerType) || (null == callee)) {
            throw new NullPointerException();
        }

        this.ownerType = ownerType;
        this.callee = callee;
    }

    /**
     * このメソッド呼び出しの型を返す
     */
    @Override
    public TypeInfo getType() {

        final MethodInfo callee = this.getCallee();
        final TypeInfo definitionType = callee.getReturnType();

        // 定義の返り値が型パラメータでなければそのまま返せる
        if (!(definitionType instanceof TypeParameterInfo)) {
            return definitionType;
        }

        //　型パラメータの場合は型引数を返す
        final int typeParameterIndex = ((TypeParameterInfo) definitionType).getIndex();
        final ClassTypeInfo callOwnerType = (ClassTypeInfo) this.getOwnerType();
        final TypeInfo typeArgument = callOwnerType.getTypeArgument(typeParameterIndex);
        return typeArgument;
    }

    /**
     * このメソッド呼び出しがくっついている型を返す
     * 
     * @return このメソッド呼び出しがくっついている型
     */
    public TypeInfo getOwnerType() {
        return this.ownerType;
    }

    /**
     * このメソッド呼び出しで呼び出されているメソッドを返す
     * @return
     */
    public MethodInfo getCallee() {
        return this.callee;
    }

    private final TypeInfo ownerType;

    private final MethodInfo callee;
}
