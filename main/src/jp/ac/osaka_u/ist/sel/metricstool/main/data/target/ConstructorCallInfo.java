package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * コンストラクタ呼び出しを保存する変数
 * 
 * @author higo
 *
 */
public final class ConstructorCallInfo extends CallInfo {

    /**
     * 型を与えてコンストラクタ呼び出しを初期化
     * 
     * @param ownerExecutableElement オーナーエレメント
     * @param referenceType 呼び出しの型
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列 
     */
    public ConstructorCallInfo(final ExecutableElementInfo ownerExecutableElement,
            final ReferenceTypeInfo referenceType, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(ownerExecutableElement, fromLine, fromColumn, toLine, toColumn);

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

    /**
     * このコンストラクタ呼び出しのテキスト表現（型）を返す
     * 
     * @return このコンストラクタ呼び出しのテキスト表現（型）を返す
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("new ");

        final TypeInfo type = this.getType();
        sb.append(type.getTypeName());

        sb.append("(");

        for (final ExpressionInfo argument : this.getArguments()) {
            sb.append(argument.getText());
            sb.append(",");
        }

        sb.append(")");

        return sb.toString();
    }

    private final ReferenceTypeInfo referenceType;

}
