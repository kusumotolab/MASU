package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;


/**
 * import文を表すクラス
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
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

        // インポートされているクラスがExternalクラスである場合はそれに対応するターゲットクラスがあるかを調べる
        final Set<ClassInfo> refreshedImportedClasses = new HashSet<ClassInfo>();
        for (final ClassInfo importedClass : this.getImportedUnits()) {
            if (importedClass instanceof ExternalClassInfo) {
                final String[] importedFQName = importedClass.getFullQualifiedName();
                final ClassInfo refreshedImportedClass = DataManager.getInstance()
                        .getClassInfoManager().getClassInfo(importedFQName);
                refreshedImportedClasses.add(refreshedImportedClass);
            } else {
                refreshedImportedClasses.add(importedClass);
            }
        }
        return refreshedImportedClasses;
    }
}
