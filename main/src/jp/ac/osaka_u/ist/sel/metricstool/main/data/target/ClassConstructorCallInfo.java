package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * クラスのコンストラクタ呼び出しを表すクラス
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public class ClassConstructorCallInfo extends ConstructorCallInfo<ClassTypeInfo> {

    /**
     * 型を与えてコンストラクタ呼び出しを初期化
     * 
     * @param classType 呼び出しの型
     * @param callee 呼び出されているコンストラクタ
     * @param ownerMethod オーナーメソッド 
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public ClassConstructorCallInfo(final ClassTypeInfo classType, final ConstructorInfo callee,
            final CallableUnitInfo ownerMethod, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {
        super(classType, callee, ownerMethod, fromLine, fromColumn, toLine, toColumn);

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
        if (0 < this.getArguments().size()) {
            sb.deleteCharAt(sb.length() - 1);
        }

        sb.append(")");

        return sb.toString();
    }

    /**
     * この式で投げられる可能性がある例外のSetを返す
     * 
     * @return　この式で投げられる可能性がある例外のSet
     */
    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        final Set<ReferenceTypeInfo> thrownExceptions = new HashSet<ReferenceTypeInfo>();
        thrownExceptions.addAll(this.getCallee().getThrownExceptions());
        return Collections.unmodifiableSet(thrownExceptions);
    }
}
