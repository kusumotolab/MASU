package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public abstract class ImportStatementInfo<T> extends UnitInfo {

    /**
     * オブジェクトを初期化
     * @param importedUnits
     * @param fromLine 
     * @param fromColumn
     * @param toLine
     * @param toColumn
     */
    ImportStatementInfo(final Set<T> importedUnits, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        this.importedUnits = new HashSet<T>();
        this.importedUnits.addAll(importedUnits);
    }

    /**
     * インポートされたクラスのSortedSetを返す
     * 
     * @return　インポートされたクラスのSortedSet
     */
    Set<T> getImportedUnits() {
        return Collections.unmodifiableSet(this.importedUnits);
    }

    @Override
    final public Set<CallInfo<? extends CallableUnitInfo>> getCalls() {
        return Collections.unmodifiableSet(new HashSet<CallInfo<? extends CallableUnitInfo>>());
    }

    @Override
    final public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        return Collections.unmodifiableSet(new HashSet<VariableInfo<? extends UnitInfo>>());
    }

    @Override
    final public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        return Collections
                .unmodifiableSet(new HashSet<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>>());
    }

    private final Set<T> importedUnits;
}
