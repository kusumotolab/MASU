package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;


@SuppressWarnings("serial")
public class MemberImportStatementInfo extends ImportStatementInfo<Member> {

    /**
     * オブジェクトを初期化
     * @param importedMembers
     * @param fromLine 
     * @param fromColumn
     * @param toLine
     * @param toColumn
     */
    public MemberImportStatementInfo(final Set<Member> importedMembers, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(importedMembers, fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * インポートされたクラスのSortedSetを返す
     * 
     * @return　インポートされたクラスのSortedSet
     */
    public Set<Member> getImportedMembers() {
        return this.getImportedUnits();
    }
}
