package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;


public class ImportStatementInfo extends UnitInfo {

    public ImportStatementInfo(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn, final SortedSet<ClassInfo> importedClasses) {

        super(fromLine, fromColumn, toLine, toColumn);

        this.importedClasses = new TreeSet<ClassInfo>();
        this.importedClasses.addAll(importedClasses);
    }

    @Override
    public Set<CallInfo<? extends CallableUnitInfo>> getCalls() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        // TODO Auto-generated method stub
        return null;
    }

    private final SortedSet<ClassInfo> importedClasses;
}
