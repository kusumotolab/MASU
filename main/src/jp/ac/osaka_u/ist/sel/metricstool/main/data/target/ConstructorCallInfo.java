package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * コンストラクタ呼び出しを保存する変数
 * 
 * @author higo
 *
 */
public final class ConstructorCallInfo extends CallInfo {

    /**
     * 呼び出されるコンストラクタを与えてオブジェクトを初期化
     * 
     * @param callee 呼び出されるコンストラクタ
     */
    public ConstructorCallInfo(final ReferenceTypeInfo referenceType, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        if (null == referenceType) {
            throw new NullPointerException();
        }

        this.referenceType = referenceType;

    }

    /**
     * コンストラクタ呼び出しの型を返す
     */
    @Override
    public TypeInfo getType() {
        return this.referenceType;
    }

    private final ReferenceTypeInfo referenceType;

}
