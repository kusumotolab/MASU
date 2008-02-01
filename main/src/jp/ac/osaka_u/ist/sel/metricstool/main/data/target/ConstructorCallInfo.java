package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * コンストラクタ呼び出しを保存する変数
 * 
 * @author higo
 *
 */
public final class ConstructorCallInfo extends MemberCallInfo {

    /**
     * 呼び出されるコンストラクタを与えてオブジェクトを初期化
     * 
     * @param callee 呼び出されるコンストラクタ
     */
    public ConstructorCallInfo(final MethodInfo callee) {
        super(callee);
    }

    /**
     * コンストラクタ呼び出しの型を返す
     */
    @Override
    public TypeInfo getType() {

        final ClassInfo ownerClass = this.getCallee().getOwnerClass();
        final ReferenceTypeInfo reference = new ReferenceTypeInfo(ownerClass);
        return reference;
    }

}
