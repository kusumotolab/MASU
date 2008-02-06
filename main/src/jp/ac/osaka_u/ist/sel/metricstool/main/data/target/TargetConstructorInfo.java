package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public final class TargetConstructorInfo extends ConstructorInfo {

    public TargetConstructorInfo(final Set<ModifierInfo> modifiers, final ClassInfo ownerClass,
            final boolean privateVisible, final boolean namespaceVisible,
            final boolean inheritanceVisible, final boolean publicVisible, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(ownerClass, privateVisible, namespaceVisible, inheritanceVisible, publicVisible,
                fromLine, fromColumn, toLine, toColumn);

        if (null == modifiers) {
            throw new NullPointerException();
        }

        this.modifiers = new HashSet<ModifierInfo>();
        this.modifiers.addAll(modifiers);
    }

    /**
     * Cüq‚Ì Set ‚ğ•Ô‚·
     * 
     * @return Cüq‚Ì Set
     */
    public Set<ModifierInfo> getModifiers() {
        return Collections.unmodifiableSet(this.modifiers);
    }

    /**
     * Cüq‚ğ•Û‘¶‚·‚é‚½‚ß‚Ì•Ï”
     */
    private final Set<ModifierInfo> modifiers;
}
