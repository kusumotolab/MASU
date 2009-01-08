package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;


/**
 * メソッド呼び出しを表すクラス
 * 
 * @author higo
 *
 */
public final class MethodCallInfo extends CallInfo<MethodInfo> {

    /**
     * 呼び出されるメソッドを与えてオブジェクトを初期化
     *
     * @param qualifierType メソッド呼び出しの親の型
     * @param qualifierExpression メソッド呼び出しの親エンティティ
     * @param callee 呼び出されているメソッド
     * @param ownerMethod オーナーメソッド
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public MethodCallInfo(final TypeInfo qualifierType, final ExpressionInfo qualifierExpression,
            final MethodInfo callee, final CallableUnitInfo ownerMethod, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(callee, ownerMethod, fromLine, fromColumn, toLine, toColumn);

        if ((null == qualifierType) || (null == callee) || (null == qualifierExpression)) {
            throw new NullPointerException();
        }

        this.qualifierType = qualifierType;
        this.qualifierExpression = qualifierExpression;
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

        //　型パラメータの場合
        final ClassTypeInfo callOwnerType = (ClassTypeInfo) this.getQualifierType();
        final List<TypeInfo> typeArguments = callOwnerType.getTypeArguments();

        // 型引数がある場合は，その型を返す
        if (0 < typeArguments.size()) {
            final int typeParameterIndex = ((TypeParameterInfo) definitionType).getIndex();
            final TypeInfo typeArgument = typeArguments.get(typeParameterIndex);
            return typeArgument;

            // 型引数がない場合は，特殊な型を返す
        } else {

            // Java　の場合 (型パラメータは1.5から導入された)
            if (Settings.getInstance().getLanguage().equals(LANGUAGE.JAVA15)) {
                final ClassInfo referencedClass = DataManager.getInstance().getClassInfoManager()
                        .getClassInfo(new String[] { "java", "lang", "Object" });
                final TypeInfo classType = new ClassTypeInfo(referencedClass);
                return classType;
            }
        }

        assert false : "Here shouldn't be reached!";
        return null;
    }

    @Override
    public void setOwnerExecutableElement(ExecutableElementInfo ownerExecutableElement) {
        super.setOwnerExecutableElement(ownerExecutableElement);
        this.qualifierExpression.setOwnerExecutableElement(ownerExecutableElement);
    }

    /**
     * このメソッド呼び出しがくっついている型を返す
     * 
     * @return このメソッド呼び出しがくっついている型
     */
    public TypeInfo getQualifierType() {
        return this.qualifierType;
    }

    /**
     * この式（メソッド呼び出し）における変数利用の一覧を返すクラス
     * 
     * @return 変数利用のSet
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {

        final SortedSet<VariableUsageInfo<?>> variableUsages = new TreeSet<VariableUsageInfo<?>>();
        variableUsages.addAll(super.getVariableUsages());

        final ExpressionInfo ownerExpression = this.getQualifierExpression();
        variableUsages.addAll(ownerExpression.getVariableUsages());

        return Collections.unmodifiableSortedSet(variableUsages);
    }

    /**
     * このメソッド呼び出しのテキスト表現（型）を返す
     * 
     * @return このメソッド呼び出しのテキスト表現（型）を返す
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        final ExpressionInfo ownerExpression = this.getQualifierExpression();
        sb.append(ownerExpression.getText());

        sb.append(".");

        final MethodInfo method = this.getCallee();
        sb.append(method.getMethodName());

        sb.append("(");

        for (final ExpressionInfo argument : this.getArguments()) {
            sb.append(argument.getText());
            sb.append(",");
        }

        sb.append(")");

        return sb.toString();
    }

    /**
     * このメソッド呼び出しの親，つまりこのメソッド呼び出しがくっついている要素を返す
     * 
     * @return このメソッド呼び出しの親
     */
    public final ExpressionInfo getQualifierExpression() {
        return this.qualifierExpression;
    }

    private final TypeInfo qualifierType;

    private final ExpressionInfo qualifierExpression;
}
