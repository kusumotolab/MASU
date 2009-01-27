package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;


public class InstanceInitializerInfo extends CallableUnitInfo {

    public InstanceInitializerInfo(ClassInfo ownerClass, int fromLine, int fromColumn, int toLine,
            int toColumn) {
        super(Collections.EMPTY_SET, ownerClass, true, false, false, false, fromLine, fromColumn,
                toLine, toColumn);
    }

}
