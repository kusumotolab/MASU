package jp.ac.osaka_u.ist.sdl.scdetector;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayElementUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.AssertStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BinominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CastUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LiteralUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MonominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.NullUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.OPERATOR;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReturnStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TernaryOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ThrowStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


public class Conversion {

    public static String getNormalizedString(final Object o) {

        if (o instanceof SingleStatementInfo) {
            return getNormalizedString((SingleStatementInfo) o);

        } else if (o instanceof ConditionalBlockInfo) {
            return getNormalizedString((ConditionalBlockInfo) o);

        } else if (o instanceof ExpressionInfo) {
            return getNormalizedString((ExpressionInfo) o);

        } else if (o instanceof ConditionInfo) {
            return getNormalizedString((ConditionInfo) o);

        } else if (o instanceof VariableInfo) {
            return getNormalizedString((VariableInfo<?>) o);
        }

        assert false : "Here shouldn't be reached!";
        return null;
    }

    public static String getNormalizedString(final SingleStatementInfo statement) {

        final StringBuilder sb = new StringBuilder();

        if (statement instanceof AssertStatementInfo) {

            sb.append("assert");

            final ExpressionInfo expression = ((AssertStatementInfo) statement)
                    .getAssertedExpression();
            final String expressionString = Conversion.getNormalizedString(expression);
            sb.append(expressionString);

        } else if (statement instanceof ExpressionStatementInfo) {

            final ExpressionInfo expression = ((ExpressionStatementInfo) statement).getExpression();
            final String expressionString = Conversion.getNormalizedString(expression);
            sb.append(expressionString);

        } else if (statement instanceof ReturnStatementInfo) {

            sb.append("return");

            final ExpressionInfo expression = ((ReturnStatementInfo) statement)
                    .getReturnedExpression();
            final String expressionString = Conversion.getNormalizedString(expression);
            sb.append(expressionString);

        } else if (statement instanceof ThrowStatementInfo) {

            sb.append("throw");

            final ExpressionInfo expression = ((ThrowStatementInfo) statement)
                    .getThrownExpression();
            final String expressionString = Conversion.getNormalizedString(expression);
            sb.append(expressionString);

        } else if (statement instanceof VariableDeclarationStatementInfo) {

            final LocalVariableInfo variable = ((VariableDeclarationStatementInfo) statement)
                    .getDeclaredLocalVariable();
            final TypeInfo variableType = variable.getType();

            switch (Configuration.INSTANCE.getPV()) {
            case 0: // 正規化レベル0，変数名をそのまま使う
                sb.append(variable.getType().getTypeName());
                sb.append(" ");
                sb.append(variable.getName());
                break;

            case 1: // 正規化レベル1，変数は型名に正規化する．変数名が異なっていても，型が同じであれば，クローンとして検出する
                sb.append(variableType.getTypeName());
                break;

            case 2: // 正規化レベル2，全ての変数を同一字句に正規化する．
                sb.append("TOKEN");
                break;

            default:
                assert false : "Here shouldn't be reached!";
            }

            if (((VariableDeclarationStatementInfo) statement).isInitialized()) {

                sb.append("=");
                final ExpressionInfo expression = ((VariableDeclarationStatementInfo) statement)
                        .getInitializationExpression();
                final String expressionString = Conversion.getNormalizedString(expression);
                sb.append(expressionString);
            }
        }

        sb.append(";");

        return sb.toString();
    }

    public static String getNormalizedString(final ConditionalBlockInfo block) {
        return getNormalizedString(block.getConditionalClause().getCondition());
    }

    /*
    public static String getNormalizedString(final BlockInfo block) {

        final StringBuilder sb = new StringBuilder();

        if (block instanceof CatchBlockInfo) {

            sb.append("CATCH");

        } else if (block instanceof ConditionalBlockInfo) {

            sb.append("CONDITION");

        } else if (block instanceof ElseBlockInfo) {

            sb.append("ELSE");

        } else if (block instanceof FinallyBlockInfo) {

            sb.append("FINALLY");

        } else if (block instanceof SimpleBlockInfo) {

            sb.append("SIMPLE");

        } else if (block instanceof SynchronizedBlockInfo) {

            sb.append("CYNCHRONIZED");

        } else if (block instanceof TryBlockInfo) {

            sb.append("TRY");
        }

        return sb.toString();
    }
    */

    public static String getNormalizedString(final ConditionInfo condition) {

        final StringBuilder sb = new StringBuilder();

        if (condition instanceof VariableDeclarationStatementInfo) {

            sb.append((StatementInfo) condition);

        } else if (condition instanceof ExpressionInfo) {

            final ExpressionInfo expression = (ExpressionInfo) condition;
            final String expressionString = Conversion.getNormalizedString(expression);
            sb.append(expressionString);

        }

        return sb.toString();
    }

    public static String getNormalizedString(final ExpressionInfo expression) {

        final StringBuilder sb = new StringBuilder();

        if (expression instanceof ArrayElementUsageInfo) {

            final ExpressionInfo ownerExpression = ((ArrayElementUsageInfo) expression)
                    .getQualifierExpression();
            final String ownerExpressionString = Conversion.getNormalizedString(ownerExpression);
            sb.append(ownerExpressionString);

            sb.append("[");
            final ExpressionInfo indexExpression = ((ArrayElementUsageInfo) expression)
                    .getIndexExpression();
            final String indexExpressionString = Conversion.getNormalizedString(indexExpression);
            sb.append(indexExpressionString);
            sb.append("]");

        } else if (expression instanceof ArrayTypeReferenceInfo) {

            sb.append("TYPE[]");

        } else if (expression instanceof BinominalOperationInfo) {

            final BinominalOperationInfo operation = (BinominalOperationInfo) expression;

            switch (Configuration.INSTANCE.getPO()) {

            case 0: // 演算をそのまま用いる
                final ExpressionInfo firstOperand = operation.getFirstOperand();
                final String firstOperandString = Conversion.getNormalizedString(firstOperand);
                sb.append(firstOperandString);

                final OPERATOR operator = operation.getOperator();
                final String operatorString = operator.getToken();
                sb.append(operatorString);

                final ExpressionInfo secondOperand = operation.getSecondOperand();
                final String secondOperandString = Conversion.getNormalizedString(secondOperand);
                sb.append(secondOperandString);
                break;

            case 1: // 演算をその型に正規化する
                sb.append(operation.getType().getTypeName());
                break;

            case 2: // 全ての演算を同一の字句に正規化する
                sb.append("TOKEN");
                break;
            }

        } else if (expression instanceof MethodCallInfo) {

            final MethodCallInfo methodCall = (MethodCallInfo) expression;
            final MethodInfo method = methodCall.getCallee();

            switch (Configuration.INSTANCE.getPI()) {

            case 0: // 正規化レベル0，メソッド名はそのまま，引数情報も用いる
                sb.append(method.getMethodName());
                sb.append("(");
                for (final ExpressionInfo argument : methodCall.getArguments()) {
                    final String argumentString = Conversion.getNormalizedString(argument);
                    sb.append(argumentString);
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                sb.append(")");
                break;
            case 1: // 正規化レベル1，メソッド名を返り値の型名に正規化する，引数情報も用いる
                sb.append(method.getReturnType().getTypeName());
                sb.append("(");
                for (final ExpressionInfo argument : methodCall.getArguments()) {
                    final String argumentString = Conversion.getNormalizedString(argument);
                    sb.append(argumentString);
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                sb.append(")");
                break;
            case 2: // 正規化レベル1，メソッド名を返り値の型名に正規化する，引数情報は用いない
                sb.append(method.getReturnType().getTypeName());
                break;
            case 3: // 正規化レベル1，全てのメソッドを同一字句に正規化する．引数情報は用いない
                sb.append("TOKEN");
                break;
            default:
                assert false : "Here shouldn't be reached!";
            }

            sb.append("");

        } else if (expression instanceof ConstructorCallInfo) {

            final ConstructorCallInfo<?> constructorCall = ((ConstructorCallInfo<?>) expression);

            switch (Configuration.INSTANCE.getPI()) {

            case 0: // 正規化レベル0，コンストラクタ名はそのまま，引数情報も用いる
                sb.append("new ");
                sb.append(constructorCall.getType().getTypeName());
                sb.append("(");

                for (final ExpressionInfo argument : constructorCall.getArguments()) {
                    final String argumentString = Conversion.getNormalizedString(argument);
                    sb.append(argumentString);
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                sb.append(")");
                break;
            case 1: // 正規化レベル1，コンストラクタ呼び出しを型に正規化する(new 演算子を取る)，引数情報は用いる
                sb.append(constructorCall.getType().getTypeName());
                sb.append("(");
                for (final ExpressionInfo argument : constructorCall.getArguments()) {
                    final String argumentString = Conversion.getNormalizedString(argument);
                    sb.append(argumentString);
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                sb.append(")");
                break;
            case 2: // 正規化レベル2，コンストラクタ呼び出しを型に正規化する（new 演算子を取る），引数情報は用いない
                sb.append(constructorCall.getType().getTypeName());
                break;
            case 3: // 正規化レベル3，全てのコンストラクタ呼び出しを同一字句に正規化する，引数情報は用いない
                sb.append("TOKEN");
                break;
            default:
                assert false : "Here shouldn't be reached!";
            }

        } else if (expression instanceof CastUsageInfo) {

            final CastUsageInfo cast = (CastUsageInfo) expression;

            switch (Configuration.INSTANCE.getPC()) {
            case 0:
                sb.append("(");

                sb.append(cast.getType().getTypeName());

                sb.append(")");

                final ExpressionInfo castedExpression = cast.getCastedUsage();
                final String castedExpressionString = Conversion
                        .getNormalizedString(castedExpression);
                sb.append(castedExpressionString);
                break;

            case 1:
                sb.append(cast.getType().getTypeName());
                break;

            case 2:
                sb.append("TOKEN");
                break;
            }

        } else if (expression instanceof ClassReferenceInfo) {

            if (Configuration.INSTANCE.getPR()) {
                final ClassReferenceInfo reference = (ClassReferenceInfo) expression;
                sb.append(reference.getType().getTypeName());
            } else {
                sb.append("TOKEN");
            }

        } else if (expression instanceof LiteralUsageInfo) {

            final LiteralUsageInfo literal = (LiteralUsageInfo) expression;

            switch (Configuration.INSTANCE.getPL()) {

            case 0: // リテラルをそのまま用いる
                sb.append(literal.getLiteral());
                break;
            case 1: // リテラルをその型の正規化する
                sb.append(literal.getType().getTypeName());
                break;
            case 2: // 全てのリテラルを同一の字句に正規化する 
                sb.append("TOKEN");
                break;
            }

        } else if (expression instanceof MonominalOperationInfo) {

            final MonominalOperationInfo operation = (MonominalOperationInfo) expression;

            switch (Configuration.INSTANCE.getPO()) {

            case 0: // 演算をそのまま用いる
                final OPERATOR operator = operation.getOperator();
                sb.append(operator.getToken());

                final ExpressionInfo operand = ((MonominalOperationInfo) expression).getOperand();
                final String operandString = Conversion.getNormalizedString(operand);
                sb.append(operandString);
                break;

            case 1: // 演算をその型に正規化する
                sb.append(operation.getType().getTypeName());
                break;

            case 2: // 全ての演算を同一の字句に正規化する
                sb.append("TOKEN");
                break;
            }

        } else if (expression instanceof NullUsageInfo) {

            sb.append("NULL");

        } else if (expression instanceof TernaryOperationInfo) {

            final TernaryOperationInfo operation = (TernaryOperationInfo) expression;

            switch (Configuration.INSTANCE.getPO()) {

            case 0: // 演算をそのまま用いる

                final ConditionInfo condition = operation.getCondition();
                final String conditionExpressionString = Conversion.getNormalizedString(condition);
                sb.append(conditionExpressionString);

                sb.append("?");

                final ExpressionInfo trueExpression = operation.getTrueExpression();
                String trueExpressionString = Conversion.getNormalizedString(trueExpression);
                sb.append(trueExpressionString);

                sb.append(":");

                final ExpressionInfo falseExpression = operation.getFalseExpression();
                String falseExpressionString = Conversion.getNormalizedString(falseExpression);
                sb.append(falseExpressionString);
                break;

            case 1: // 演算をその型に正規化する
                sb.append(operation.getType().getTypeName());
                break;

            case 2: // 全ての演算を同一の字句に正規化する
                sb.append("TOKEN");
                break;
            }

        } else if (expression instanceof TypeParameterUsageInfo) {

            sb.append("<");

            final ExpressionInfo typeParameterExpression = ((TypeParameterUsageInfo) expression)
                    .getExpression();
            final String typeParameterExpressionString = Conversion
                    .getNormalizedString(typeParameterExpression);
            sb.append(typeParameterExpressionString);

            sb.append(">");

        } else if (expression instanceof UnknownEntityUsageInfo) {

            sb.append("UNKNOWN");

        } else if (expression instanceof VariableUsageInfo) {

            final VariableInfo<?> usedVariable = ((VariableUsageInfo<?>) expression)
                    .getUsedVariable();
            final TypeInfo variableType = usedVariable.getType();

            switch (Configuration.INSTANCE.getPV()) {
            case 0: // 正規化レベル0，変数名をそのまま使う
                sb.append(usedVariable.getName());
                break;

            case 1: // 正規化レベル1，変数は型名に正規化する．変数名が異なっていても，型が同じであれば，クローンとして検出する
                sb.append(variableType.getTypeName());
                break;

            case 2: // 正規化レベル2，全ての変数を同一字句に正規化する．
                sb.append("TOKEN");
                break;

            default:
                assert false : "Here shouldn't be reached!";
            }
        }

        return sb.toString();
    }

    public static String getNormalizedString(final VariableInfo<?> variable) {

        final StringBuilder text = new StringBuilder();

        switch (Configuration.INSTANCE.getPV()) {
        case 0: // 正規化レベル0，変数名をそのまま使う
            text.append(variable.getName());
            break;

        case 1: // 正規化レベル1，変数は型名に正規化する．変数名が異なっていても，型が同じであれば，クローンとして検出する
            text.append(variable.getType().getTypeName());
            break;

        case 2: // 正規化レベル2，全ての変数を同一字句に正規化する．
            text.append("TOKEN");
            break;

        default:
            assert false : "Here shouldn't be reached!";
        }

        return text.toString();
    }
}
