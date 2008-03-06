package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.HashSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ArrayTypeClassInfo;


public final class ArrayLengthInfo extends FieldInfo {

    public ArrayLengthInfo(final ArrayTypeInfo ownerArray) {

        super(new HashSet<ModifierInfo>(), "length", PrimitiveTypeInfo.INT, new ArrayTypeClassInfo(
                ownerArray), 0, 0, 0, 0);
    }
}
