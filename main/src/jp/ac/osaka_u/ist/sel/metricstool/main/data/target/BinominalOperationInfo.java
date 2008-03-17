package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.TypeConverter;


/**
 * ニ項演算使用を表すクラス
 * 
 * @author higo
 * 
 */
public class BinominalOperationInfo extends EntityUsageInfo {

    /**
     * ニ項演算の各オペランド，オペレータを与えてオブジェクトを初期化
     * 
     * @param operator
     *            オペレータ
     * @param firstOperand
     *            第一オペランド
     * @param secondOperand
     *            第二オペランド
     */
    public BinominalOperationInfo(final OPERATOR operator, final EntityUsageInfo firstOperand,
            final EntityUsageInfo secondOperand, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        if ((null == operator) || (null == firstOperand) || (null == secondOperand)) {
            throw new NullPointerException();
        }

        this.operator = operator;
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;

    }

    @Override
    public TypeInfo getType() {

        final ExternalClassInfo DOUBLE = TypeConverter.getTypeConverter(Settings.getLanguage())
                .getWrapperClass(PrimitiveTypeInfo.DOUBLE);
        final ExternalClassInfo FLOAT = TypeConverter.getTypeConverter(Settings.getLanguage())
                .getWrapperClass(PrimitiveTypeInfo.FLOAT);
        final ExternalClassInfo LONG = TypeConverter.getTypeConverter(Settings.getLanguage())
                .getWrapperClass(PrimitiveTypeInfo.LONG);
        final ExternalClassInfo INTEGER = TypeConverter.getTypeConverter(Settings.getLanguage())
                .getWrapperClass(PrimitiveTypeInfo.INT);
        final ExternalClassInfo SHORT = TypeConverter.getTypeConverter(Settings.getLanguage())
                .getWrapperClass(PrimitiveTypeInfo.SHORT);
        final ExternalClassInfo CHARACTER = TypeConverter.getTypeConverter(Settings.getLanguage())
                .getWrapperClass(PrimitiveTypeInfo.CHAR);
        final ExternalClassInfo BYTE = TypeConverter.getTypeConverter(Settings.getLanguage())
                .getWrapperClass(PrimitiveTypeInfo.BYTE);
        final ExternalClassInfo BOOLEAN = TypeConverter.getTypeConverter(Settings.getLanguage())
                .getWrapperClass(PrimitiveTypeInfo.BOOLEAN);

        final TypeInfo firstOperandType = this.getFirstOperand().getType();
        final TypeInfo secondOperandType = this.getSecondOperand().getType();

        switch (Settings.getLanguage()) {
        case JAVA:

            final TypeInfo STRING = new ClassTypeInfo(TypeConverter.getTypeConverter(
                    Settings.getLanguage()).getWrapperClass(PrimitiveTypeInfo.STRING));

            switch (this.getOperator()) {
            case ARITHMETIC:

                if (firstOperandType.equals(DOUBLE)
                        || firstOperandType.equals(PrimitiveTypeInfo.DOUBLE)
                        || secondOperandType.equals(DOUBLE)
                        || secondOperandType.equals(PrimitiveTypeInfo.DOUBLE)) {
                    return PrimitiveTypeInfo.DOUBLE;

                } else if (firstOperandType.equals(FLOAT)
                        || firstOperandType.equals(PrimitiveTypeInfo.FLOAT)
                        || secondOperandType.equals(FLOAT)
                        || secondOperandType.equals(PrimitiveTypeInfo.FLOAT)) {
                    return PrimitiveTypeInfo.FLOAT;

                } else if (firstOperandType.equals(LONG)
                        || firstOperandType.equals(PrimitiveTypeInfo.LONG)
                        || secondOperandType.equals(LONG)
                        || secondOperandType.equals(PrimitiveTypeInfo.LONG)) {
                    return PrimitiveTypeInfo.LONG;

                } else if (firstOperandType.equals(INTEGER)
                        || firstOperandType.equals(PrimitiveTypeInfo.INT)
                        || secondOperandType.equals(INTEGER)
                        || secondOperandType.equals(PrimitiveTypeInfo.INT)) {
                    return PrimitiveTypeInfo.INT;

                } else if (firstOperandType.equals(SHORT)
                        || firstOperandType.equals(PrimitiveTypeInfo.SHORT)
                        || secondOperandType.equals(SHORT)
                        || secondOperandType.equals(PrimitiveTypeInfo.SHORT)) {
                    return PrimitiveTypeInfo.SHORT;

                } else if (firstOperandType.equals(CHARACTER)
                        || firstOperandType.equals(PrimitiveTypeInfo.CHAR)
                        || secondOperandType.equals(CHARACTER)
                        || secondOperandType.equals(PrimitiveTypeInfo.CHAR)) {
                    return PrimitiveTypeInfo.CHAR;

                } else if (firstOperandType.equals(BYTE)
                        || firstOperandType.equals(PrimitiveTypeInfo.BYTE)
                        || secondOperandType.equals(BYTE)
                        || secondOperandType.equals(PrimitiveTypeInfo.BYTE)) {
                    return PrimitiveTypeInfo.BYTE;

                } else if ((firstOperandType instanceof UnknownTypeInfo)
                        || (secondOperandType instanceof UnknownTypeInfo)) {

                    return UnknownTypeInfo.getInstance();
                }

                //それ以外の時はjava.lang.String型になる
                //"+"を定義した後は，+かどうかをチェックする必要あり
                return STRING;

            case COMPARATIVE:
                return PrimitiveTypeInfo.BOOLEAN;
            case LOGICAL:
                return PrimitiveTypeInfo.BOOLEAN;
            case BITS:

                if (firstOperandType.equals(LONG)
                        || firstOperandType.equals(PrimitiveTypeInfo.LONG)
                        || secondOperandType.equals(LONG)
                        || secondOperandType.equals(PrimitiveTypeInfo.LONG)) {
                    return PrimitiveTypeInfo.LONG;

                } else if (firstOperandType.equals(INTEGER)
                        || firstOperandType.equals(PrimitiveTypeInfo.INT)
                        || secondOperandType.equals(INTEGER)
                        || secondOperandType.equals(PrimitiveTypeInfo.INT)) {
                    return PrimitiveTypeInfo.INT;

                } else if (firstOperandType.equals(SHORT)
                        || firstOperandType.equals(PrimitiveTypeInfo.SHORT)
                        || secondOperandType.equals(SHORT)
                        || secondOperandType.equals(PrimitiveTypeInfo.SHORT)) {
                    return PrimitiveTypeInfo.SHORT;
                 
                } else if (firstOperandType.equals(BYTE)
                        || firstOperandType.equals(PrimitiveTypeInfo.BYTE)
                        || secondOperandType.equals(BYTE)
                        || secondOperandType.equals(PrimitiveTypeInfo.BYTE)) {
                    return PrimitiveTypeInfo.BYTE;

                } else if (firstOperandType.equals(CHARACTER)
                        || firstOperandType.equals(PrimitiveTypeInfo.CHAR)
                        || secondOperandType.equals(CHARACTER)
                        || secondOperandType.equals(PrimitiveTypeInfo.CHAR)) {
                    return PrimitiveTypeInfo.CHAR;
                    
                } else if (firstOperandType.equals(BOOLEAN)
                        || firstOperandType.equals(PrimitiveTypeInfo.BOOLEAN)
                        || secondOperandType.equals(BOOLEAN)
                        || secondOperandType.equals(PrimitiveTypeInfo.BOOLEAN)) {
                    return PrimitiveTypeInfo.BOOLEAN;

                } else if ((firstOperandType instanceof UnknownTypeInfo)
                        || (secondOperandType instanceof UnknownTypeInfo)) {

                    return UnknownTypeInfo.getInstance();

                } else {
                    assert false : "Here shouldn't be reached!";
                }

            case SHIFT:
                return firstOperandType;
            case ASSIGNMENT:
                return firstOperandType;
            default:
                assert false : "Here shouldn't be reached";
            }

            break;

        default:
            assert false : "Here shouldn't be reached";
        }

        return UnknownTypeInfo.getInstance();
    }

    /**
     * 演算子を取得する
     * 
     * @return 演算子
     */
    public OPERATOR getOperator() {
        return this.operator;
    }

    /**
     * 第一オペランドを取得する
     * 
     * @return 第一オペランド
     */
    public EntityUsageInfo getFirstOperand() {
        return this.firstOperand;
    }

    /**
     * 第二オペランドを取得する
     * 
     * @return 第二オペランド
     */
    public EntityUsageInfo getSecondOperand() {
        return this.secondOperand;
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo#getVariableUsages()
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        final SortedSet<VariableUsageInfo<?>> variableUsages = new TreeSet<VariableUsageInfo<?>>();
        variableUsages.addAll(this.getFirstOperand().getVariableUsages());               
        variableUsages.addAll(this.getSecondOperand().getVariableUsages());
        return Collections.unmodifiableSortedSet(variableUsages);
    }
    
    private final EntityUsageInfo firstOperand;

    private final EntityUsageInfo secondOperand;

    private final OPERATOR operator;
}
