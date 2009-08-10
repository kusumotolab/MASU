package jp.ac.osaka_u.ist.sdl.scdetector;


import jp.ac.osaka_u.ist.sdl.scdetector.data.ClonePairInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;


class TwoClassHash {

    TwoClassHash(final ClonePairInfo clonePair) {
        this(clonePair.getCodeFragmentA().getElements().first().getOwnerMethod().getOwnerClass(),
                clonePair.getCodeFragmentB().getElements().first().getOwnerMethod().getOwnerClass());
    }

    TwoClassHash(final ClassInfo class1, final ClassInfo class2) {
        this.class1 = class1;
        this.class2 = class2;
    }

    @Override
    public int hashCode() {
        return this.class1.hashCode() + this.class2.hashCode();
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof TwoClassHash)) {
            return false;
        }

        final TwoClassHash target = (TwoClassHash) o;
        return this.getClass1().equals(target.getClass1())
                && this.getClass2().equals(target.getClass2())
                || this.getClass1().equals(target.getClass2())
                && this.getClass2().equals(target.getClass1());
    }

    public ClassInfo getClass1() {
        return this.class1;
    }

    public ClassInfo getClass2() {
        return this.class2;
    }

    final ClassInfo class1;

    final ClassInfo class2;
}
