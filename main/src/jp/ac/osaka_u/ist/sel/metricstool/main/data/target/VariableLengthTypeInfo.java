package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.HashMap;
import java.util.Map;


/**
 * 可変長型を表すクラス
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class VariableLengthTypeInfo extends ArrayTypeInfo {

    /**
     * VariableLengthTypeInfo のインスタンスを返すためのファクトリメソッド．
     * 
     * @param element 型を表す変数
     * @return 生成した VariableLengthTypeInfo オブジェクト
     */
    public static VariableLengthTypeInfo getType(final TypeInfo element) {

        if (null == element) {
            throw new IllegalArgumentException();
        }

        VariableLengthTypeInfo variableLengthType = VARIABLE_LENGTH_TYPE_MAP.get(element);
        if (variableLengthType == null) {
            variableLengthType = new VariableLengthTypeInfo(element);
            VARIABLE_LENGTH_TYPE_MAP.put(element, variableLengthType);
        }

        return variableLengthType;
    }

    VariableLengthTypeInfo(final TypeInfo element) {
        super(element, 1);
    }

    /**
     * 型名を返す
     * 
     * @return 型名
     */
    @Override
    public String getTypeName() {

        final StringBuilder sb = new StringBuilder();

        final TypeInfo element = this.getElementType();
        sb.append(element.getTypeName());
        sb.append(" ...");

        return sb.toString();
    }

    /**
     * VariableLengthTypeInfo オブジェクトを一元管理するための Map．オブジェクトはファクトリメソッドで生成される．
     */
    private static final Map<TypeInfo, VariableLengthTypeInfo> VARIABLE_LENGTH_TYPE_MAP = new HashMap<TypeInfo, VariableLengthTypeInfo>();
}
