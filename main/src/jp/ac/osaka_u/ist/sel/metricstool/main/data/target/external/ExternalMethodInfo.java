package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;


public class ExternalMethodInfo extends MethodInfo {

    public ExternalMethodInfo(final String methodName, final ExternalClassInfo ownerClass,
            final boolean constructor) {

        super(methodName, UnknownTypeInfo.getInstance(), ownerClass, constructor);
    }

}
