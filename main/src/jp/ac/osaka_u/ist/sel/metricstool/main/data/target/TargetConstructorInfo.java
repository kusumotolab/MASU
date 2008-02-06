package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;


public final class TargetConstructorInfo extends ConstructorInfo {

    public TargetConstructorInfo(final Set<ModifierInfo> modifiers, final ClassInfo ownerClass,
            final boolean privateVisible, final boolean namespaceVisible,
            final boolean inheritanceVisible, final boolean publicVisible, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(modifiers, ownerClass, privateVisible, namespaceVisible, inheritanceVisible,
                publicVisible, fromLine, fromColumn, toLine, toColumn);
    }
}
