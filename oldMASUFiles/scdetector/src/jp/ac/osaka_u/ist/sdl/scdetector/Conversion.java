package jp.ac.osaka_u.ist.sdl.scdetector;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jp.ac.osaka_u.ist.sdl.scdetector.settings.CALL_NORMALIZATION;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.Configuration;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayElementUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.AssertStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BinominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BreakStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CaseEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CastUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ContinueStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.DefaultEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EmptyExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForeachConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LiteralUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MonominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.NullUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.OPERATOR;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParenthesesExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReturnStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TernaryOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ThrowStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGMethodEnterNode.PseudoConditionInfo;

/**
 * �v���O�����v�f�̕�����ϊ������邽�߂̃N���X
 * 
 * @author higo
 * 
 */
public class Conversion {

	/**
	 * �I�u�W�F�N�g�^��ϊ����邽�߂̃��\�b�h
	 * 
	 * @param o
	 * @return
	 */
	public static String getNormalizedElement(final Object o) {

		String converted = ORIGINAL_TO_CONVERTED_MAP.get(o);
		if (null != converted) {
			return converted;
		}

		if (o instanceof SingleStatementInfo) {
			converted = getNormalizedStatement((SingleStatementInfo) o);
			ORIGINAL_TO_CONVERTED_MAP.put(o, converted);
			return converted;

		} else if (o instanceof ExpressionInfo) {
			converted = getNormalizedExpression((ExpressionInfo) o);
			ORIGINAL_TO_CONVERTED_MAP.put(o, converted);
			return converted;

		} else if (o instanceof ConditionInfo) {
			converted = getNormalizedCondition((ConditionInfo) o);
			ORIGINAL_TO_CONVERTED_MAP.put(o, converted);
			return converted;

		} else if (o instanceof CaseEntryInfo) {

			final StringBuilder text = new StringBuilder();
			if (o instanceof DefaultEntryInfo) {
				text.append("default:");
			} else {
				text.append("case ");

				final ExpressionInfo label = ((CaseEntryInfo) o).getLabel();
				final String labelString = getNormalizedExpression(label);
				text.append(labelString);

				text.append(":");
			}

			converted = text.toString();
			ORIGINAL_TO_CONVERTED_MAP.put(o, converted);
			return converted;
		}

		assert false : "Here shouldn't be reached!";
		return null;
	}

	/**
	 * �P����ϊ����邽�߂̃��\�b�h
	 * 
	 * @param statement
	 * @return
	 */
	public static String getNormalizedStatement(
			final SingleStatementInfo statement) {

		String converted = ORIGINAL_TO_CONVERTED_MAP.get(statement);
		if (null != converted) {
			return converted;
		}

		final StringBuilder text = new StringBuilder();

		if (statement instanceof AssertStatementInfo) {

			text.append("assert ");

			final ExpressionInfo expression = ((AssertStatementInfo) statement)
					.getAssertedExpression();
			final String expressionString = Conversion
					.getNormalizedExpression(expression);
			text.append(expressionString);

			text.append(";");

		} else if (statement instanceof BreakStatementInfo) {

			text.append("break;");

		} else if (statement instanceof ContinueStatementInfo) {

			text.append("continue;");

		} else if (statement instanceof ExpressionStatementInfo) {

			final ExpressionInfo expression = ((ExpressionStatementInfo) statement)
					.getExpression();
			text.append(Conversion.getNormalizedExpression(expression));

			text.append(";");

		} else if (statement instanceof ReturnStatementInfo) {

			text.append("return ");

			final ExpressionInfo expression = ((ReturnStatementInfo) statement)
					.getReturnedExpression();
			final String expressionString = Conversion
					.getNormalizedExpression(expression);
			text.append(expressionString);

			text.append(";");

		} else if (statement instanceof ThrowStatementInfo) {

			text.append("throw ");

			final ExpressionInfo expression = ((ThrowStatementInfo) statement)
					.getThrownExpression();
			final String expressionString = Conversion
					.getNormalizedExpression(expression);
			text.append(expressionString);

			text.append(";");

		} else if (statement instanceof VariableDeclarationStatementInfo) {

			text.append(Conversion
					.getNormalizedCondition((ConditionInfo) statement));

			text.append(";");

		} else {
			assert false : "Here shouldn't be reached!";
		}

		converted = text.toString();
		ORIGINAL_TO_CONVERTED_MAP.put(statement, converted);
		return converted;
	}

	/**
	 * ��������ϊ����邽�߂̃��\�b�h
	 * 
	 * @param condition
	 * @return
	 */
	public static String getNormalizedCondition(final ConditionInfo condition) {

		String converted = ORIGINAL_TO_CONVERTED_MAP.get(condition);
		if (null != converted) {
			return converted;
		}

		if (condition instanceof VariableDeclarationStatementInfo) {

			final StringBuilder text = new StringBuilder();
			final VariableDeclarationStatementInfo declarationStatement = (VariableDeclarationStatementInfo) condition;
			final LocalVariableInfo variable = declarationStatement
					.getDeclaredLocalVariable();
			final TypeInfo variableType = variable.getType();

			switch (Configuration.INSTANCE.getPV()) {
			case NO: // ���K�����x��0�C�ϐ��������̂܂܎g��
				text.append(variable.getType().getTypeName());
				text.append(" ");
				text.append(variable.getName());
				break;

			case TYPE: // ���K�����x��1�C�ϐ��͌^���ɐ��K������D�ϐ������قȂ��Ă��Ă��C�^�������ł���΁C�N���[���Ƃ��Č��o����
				text.append(variableType.getTypeName());
				break;

			case ALL: // ���K�����x��2�C�S�Ă̕ϐ��𓯈ꎚ��ɐ��K������D
				text.append("TOKEN");
				break;

			default:
				assert false : "Here shouldn't be reached!";
			}

			if (declarationStatement.isInitialized()) {

				text.append("=");
				final ExpressionInfo expression = declarationStatement
						.getInitializationExpression();
				final String expressionString = Conversion
						.getNormalizedExpression(expression);
				text.append(expressionString);
			}

			converted = text.toString();
			ORIGINAL_TO_CONVERTED_MAP.put(condition, converted);
			return converted;

		} else if (condition instanceof ExpressionInfo) {

			converted = Conversion
					.getNormalizedExpression((ExpressionInfo) condition);
			ORIGINAL_TO_CONVERTED_MAP.put(condition, converted);
			return converted;

		} else if (condition instanceof PseudoConditionInfo) {

			converted = "PseudoConditionInfo";
			ORIGINAL_TO_CONVERTED_MAP.put(condition, converted);
			return converted;
		}

		assert false : "Here shouldn't be reached!";
		return null;
	}

	/**
	 * ����ϊ����邽�߂̃��\�b�h
	 * 
	 * @param expression
	 * @return
	 */
	public static String getNormalizedExpression(final ExpressionInfo expression) {

		String converted = ORIGINAL_TO_CONVERTED_MAP.get(expression);
		if (null != converted) {
			return converted;
		}

		final StringBuilder text = new StringBuilder();

		if (expression instanceof ArrayElementUsageInfo) {

			final ExpressionInfo ownerExpression = ((ArrayElementUsageInfo) expression)
					.getQualifierExpression();
			final String ownerExpressionString = Conversion
					.getNormalizedExpression(ownerExpression);
			text.append(ownerExpressionString);

			text.append("[");
			final ExpressionInfo indexExpression = ((ArrayElementUsageInfo) expression)
					.getIndexExpression();
			final String indexExpressionString = Conversion
					.getNormalizedExpression(indexExpression);
			text.append(indexExpressionString);
			text.append("]");

		} else if (expression instanceof ArrayInitializerInfo) {

			final ArrayInitializerInfo initializer = (ArrayInitializerInfo) expression;

			text.append("{");

			for (final ExpressionInfo element : initializer
					.getElementInitializers()) {
				text.append(Conversion.getNormalizedExpression(element));
				text.append(",");
			}

			text.deleteCharAt(text.length() - 1);
			text.append("}");

		} else if (expression instanceof ArrayTypeReferenceInfo) {

			final ArrayTypeReferenceInfo reference = (ArrayTypeReferenceInfo) expression;
			final ArrayTypeInfo arrayType = (ArrayTypeInfo) reference.getType();
			text.append(arrayType.getTypeName());

		} else if (expression instanceof BinominalOperationInfo) {

			final BinominalOperationInfo operation = (BinominalOperationInfo) expression;

			switch (Configuration.INSTANCE.getPO()) {

			case NO: // ���Z�����̂܂ܗp����
				final ExpressionInfo firstOperand = operation.getFirstOperand();
				final String firstOperandString = Conversion
						.getNormalizedExpression(firstOperand);
				text.append(firstOperandString);

				final OPERATOR operator = operation.getOperator();
				final String operatorString = operator.getToken();
				text.append(operatorString);

				final ExpressionInfo secondOperand = operation
						.getSecondOperand();
				final String secondOperandString = Conversion
						.getNormalizedExpression(secondOperand);
				text.append(secondOperandString);
				break;

			case TYPE: // ���Z�����̌^�ɐ��K������
				text.append(operation.getType().getTypeName());
				break;

			case ALL: // �S�Ẳ��Z�𓯈�̎���ɐ��K������
				text.append("TOKEN");
				break;
			}

		} else if (expression instanceof MethodCallInfo) {

			final MethodCallInfo methodCall = (MethodCallInfo) expression;
			final MethodInfo method = methodCall.getCallee();
			final ExpressionInfo qualifier = methodCall
					.getQualifierExpression();

			if (CALL_NORMALIZATION.FQN == Configuration.INSTANCE.getPI()) {
//				final ClassInfo ownerClass = methodCall.getOwnerMethod()
//						.getOwnerClass();
//				if ((qualifier instanceof ClassReferenceInfo)
//						&& (((ClassReferenceInfo) qualifier)
//								.getReferencedClass().equals(ownerClass))) {
//				} else {
					text.append(Conversion.getNormalizedExpression(qualifier));
					text.append(".");
//				}
			}			

			switch (Configuration.INSTANCE.getPI()) {

			case NO: // ���K�����x��0�C���\�b�h���͂��̂܂܁C���������p����
				text.append(method.getMethodName());
				text.append("(");
				for (final ExpressionInfo argument : methodCall.getArguments()) {
					final String argumentString = Conversion
							.getNormalizedExpression(argument);
					text.append(argumentString);
					text.append(",");
				}
				if (0 < methodCall.getArguments().size()) {
					text.deleteCharAt(text.length() - 1);
				}
				text.append(")");
				break;
			case TYPE_WITH_ARG: // ���K�����x��1�C���\�b�h����Ԃ�l�̌^���ɐ��K������C���������p����
				text.append(method.getReturnType().getTypeName());
				text.append("(");
				for (final ExpressionInfo argument : methodCall.getArguments()) {
					final String argumentString = Conversion
							.getNormalizedExpression(argument);
					text.append(argumentString);
					text.append(",");
				}
				if (0 < methodCall.getArguments().size()) {
					text.deleteCharAt(text.length() - 1);
				}
				text.append(")");
				break;
			case TYPE_WITHOUT_ARG: // ���K�����x��1�C���\�b�h����Ԃ�l�̌^���ɐ��K������C�������͗p���Ȃ�
				text.append(method.getReturnType().getTypeName());
				break;
			case ALL: // ���K�����x��1�C�S�Ẵ��\�b�h�𓯈ꎚ��ɐ��K������D�������͗p���Ȃ�
				text.append("TOKEN");
				break;
			default:
				assert false : "Here shouldn't be reached!";
			}

			text.append("");

		} else if (expression instanceof ConstructorCallInfo<?>) {

			final ConstructorCallInfo<?> constructorCall = ((ConstructorCallInfo<?>) expression);

			switch (Configuration.INSTANCE.getPI()) {

			case NO: // ���K�����x��0�C�R���X�g���N�^���͂��̂܂܁C���������p����
				text.append("new ");
				text.append(constructorCall.getType().getTypeName());
				text.append("(");

				for (final ExpressionInfo argument : constructorCall
						.getArguments()) {
					final String argumentString = Conversion
							.getNormalizedExpression(argument);
					text.append(argumentString);
					text.append(",");
				}
				if (0 < constructorCall.getArguments().size()) {
					text.deleteCharAt(text.length() - 1);
				}
				text.append(")");
				break;
			case TYPE_WITH_ARG: // ���K�����x��1�C�R���X�g���N�^�Ăяo�����^�ɐ��K������(new
				// ���Z�q�����)�C�������͗p����
				text.append(constructorCall.getType().getTypeName());
				text.append("(");
				for (final ExpressionInfo argument : constructorCall
						.getArguments()) {
					final String argumentString = Conversion
							.getNormalizedExpression(argument);
					text.append(argumentString);
					text.append(",");
				}
				if (0 < constructorCall.getArguments().size()) {
					text.deleteCharAt(text.length() - 1);
				}
				text.append(")");
				break;
			case TYPE_WITHOUT_ARG: // ���K�����x��2�C�R���X�g���N�^�Ăяo�����^�ɐ��K������inew
				// ���Z�q�����j�C�������͗p���Ȃ�
				text.append(constructorCall.getType().getTypeName());
				break;
			case ALL: // ���K�����x��3�C�S�ẴR���X�g���N�^�Ăяo���𓯈ꎚ��ɐ��K������C�������͗p���Ȃ�
				text.append("TOKEN");
				break;
			default:
				assert false : "Here shouldn't be reached!";
			}

		} else if (expression instanceof CastUsageInfo) {

			final CastUsageInfo cast = (CastUsageInfo) expression;

			switch (Configuration.INSTANCE.getPC()) {
			case NO:
				text.append("(");

				text.append(cast.getType().getTypeName());

				text.append(")");

				final ExpressionInfo castedExpression = cast.getCastedUsage();
				final String castedExpressionString = Conversion
						.getNormalizedExpression(castedExpression);
				text.append(castedExpressionString);
				break;

			case TYPE:
				text.append(cast.getType().getTypeName());
				break;

			case ALL:
				text.append("TOKEN");
				break;
			}

		} else if (expression instanceof ClassReferenceInfo) {

			switch (Configuration.INSTANCE.getPR()) {

			case NO: // �N���X�Q�Ƃ𐳋K�����Ȃ�
				final ClassReferenceInfo reference = (ClassReferenceInfo) expression;
				text.append(reference.getType().getTypeName());
				break;
			case ALL: // ���ׂē���g�[�N���ɐ��K��
				text.append("TOKEN");
				break;
			}

		} else if (expression instanceof EmptyExpressionInfo) {

			// ��������K�v���Ȃ�

		} else if (expression instanceof ForeachConditionInfo) {

			final ForeachConditionInfo foreach = (ForeachConditionInfo) expression;
			text.append(Conversion.getNormalizedCondition(foreach
					.getIteratorVariable()));
			text.append(":");
			text.append(Conversion.getNormalizedExpression(foreach
					.getIteratorExpression()));

		} else if (expression instanceof LiteralUsageInfo) {

			final LiteralUsageInfo literal = (LiteralUsageInfo) expression;

			switch (Configuration.INSTANCE.getPL()) {

			case NO: // ���e���������̂܂ܗp����
				text.append(literal.getLiteral());
				break;
			case TYPE: // ���e���������̌^�̐��K������
				text.append(literal.getType().getTypeName());
				break;
			case ALL: // �S�Ẵ��e�����𓯈�̎���ɐ��K������
				text.append("TOKEN");
				break;
			}

		} else if (expression instanceof MonominalOperationInfo) {

			final MonominalOperationInfo operation = (MonominalOperationInfo) expression;

			switch (Configuration.INSTANCE.getPO()) {

			case NO: // ���Z�����̂܂ܗp����
				final OPERATOR operator = operation.getOperator();
				text.append(operator.getToken());

				final ExpressionInfo operand = ((MonominalOperationInfo) expression)
						.getOperand();
				final String operandString = Conversion
						.getNormalizedExpression(operand);
				text.append(operandString);
				break;

			case TYPE: // ���Z�����̌^�ɐ��K������
				text.append(operation.getType().getTypeName());
				break;

			case ALL: // �S�Ẳ��Z�𓯈�̎���ɐ��K������
				text.append("TOKEN");
				break;
			}

		} else if (expression instanceof NullUsageInfo) {

			text.append("NULL");

		} else if (expression instanceof ParenthesesExpressionInfo) {

			text.append("(");

			final ParenthesesExpressionInfo parentheses = (ParenthesesExpressionInfo) expression;
			text.append(Conversion.getNormalizedExpression(parentheses
					.getParnentheticExpression()));

			text.append(")");

		} else if (expression instanceof TernaryOperationInfo) {

			final TernaryOperationInfo operation = (TernaryOperationInfo) expression;

			switch (Configuration.INSTANCE.getPO()) {

			case NO: // ���Z�����̂܂ܗp����

				final ConditionInfo condition = operation.getCondition();
				final String conditionExpressionString = Conversion
						.getNormalizedCondition(condition);
				text.append(conditionExpressionString);

				text.append("?");

				final ExpressionInfo trueExpression = operation
						.getTrueExpression();
				String trueExpressionString = Conversion
						.getNormalizedExpression(trueExpression);
				text.append(trueExpressionString);

				text.append(":");

				final ExpressionInfo falseExpression = operation
						.getFalseExpression();
				String falseExpressionString = Conversion
						.getNormalizedExpression(falseExpression);
				text.append(falseExpressionString);
				break;

			case TYPE: // ���Z�����̌^�ɐ��K������
				text.append(operation.getType().getTypeName());
				break;

			case ALL: // �S�Ẳ��Z�𓯈�̎���ɐ��K������
				text.append("TOKEN");
				break;
			}

		} else if (expression instanceof UnknownEntityUsageInfo) {

			text.append("UNKNOWN");

		} else if (expression instanceof VariableUsageInfo<?>) {

			final VariableInfo<?> usedVariable = ((VariableUsageInfo<?>) expression)
					.getUsedVariable();
			final TypeInfo variableType = usedVariable.getType();

			switch (Configuration.INSTANCE.getPV()) {
			case NO: // ���K�����x��0�C�ϐ��������̂܂܎g��
				text.append(usedVariable.getName());
				break;

			case TYPE: // ���K�����x��1�C�ϐ��͌^���ɐ��K������D�ϐ������قȂ��Ă��Ă��C�^�������ł���΁C�N���[���Ƃ��Č��o����
				text.append(variableType.getTypeName());
				break;

			case ALL: // ���K�����x��2�C�S�Ă̕ϐ��𓯈ꎚ��ɐ��K������D
				text.append("TOKEN");
				break;

			default:
				assert false : "Here shouldn't be reached!";
			}
		}

		else {
			assert false : "Here shouldn't be reached!";
		}

		converted = text.toString();
		ORIGINAL_TO_CONVERTED_MAP.put(expression, converted);
		return converted;
	}

	private static final ConcurrentMap<Object, String> ORIGINAL_TO_CONVERTED_MAP = new ConcurrentHashMap<Object, String>();
}
