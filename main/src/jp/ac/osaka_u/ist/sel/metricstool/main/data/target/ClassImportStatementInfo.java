package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;


/**
 * import文を表すクラス
 * 
 * @author higo
 *
 */
public class ClassImportStatementInfo extends ImportStatementInfo<ClassInfo> {

    /**
     * オブジェクトを初期化
     * @param importedClasses
     * @param fromLine 
     * @param fromColumn
     * @param toLine
     * @param toColumn
     */
    public ClassImportStatementInfo(final Set<ClassInfo> importedClasses, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(importedClasses, fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * インポートされたクラスのSortedSetを返す
     * 
     * @return　インポートされたクラスのSortedSet
     */
    public Set<ClassInfo> getImportedClasses() {
        return this.getImportedUnits();
    }
}
