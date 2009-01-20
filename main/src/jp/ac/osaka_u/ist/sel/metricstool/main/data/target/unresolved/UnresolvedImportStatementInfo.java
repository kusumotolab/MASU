package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ImportStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ASTパースの際，参照型変数の利用可能な名前空間名，または完全限定名を表すクラス
 * 
 * @author higo
 * 
 */
public final class UnresolvedImportStatementInfo extends UnresolvedUnitInfo<ImportStatementInfo> {

    /**
     * 利用可能名前空間名とそれ以下のクラス全てのクラスが利用可能かどうかを表すbooleanを与えてオブジェクトを初期化.
     * <p>
     * import aaa.bbb.ccc.DDD； // new AvailableNamespace({"aaa","bbb","ccc","DDD"}, false); <br>
     * import aaa.bbb.ccc.*; // new AvailableNamespace({"aaa","bbb","ccc"},true); <br>
     * </p>
     * 
     * @param namespace 利用可能名前空間名
     * @param allClasses 全てのクラスが利用可能かどうか
     */
    public UnresolvedImportStatementInfo(final String[] namespace, final boolean allClasses) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == namespace) {
            throw new NullPointerException();
        }

        this.importName = namespace;
        this.allClasses = allClasses;
    }

    /**
     * 対象オブジェクトと等しいかどうかを返す
     * 
     * @param o 対象オブジェクト
     * @return 等しい場合 true，そうでない場合 false
     */
    @Override
    public boolean equals(Object o) {

        if (null == o) {
            throw new NullPointerException();
        }

        if (!(o instanceof UnresolvedImportStatementInfo)) {
            return false;
        }

        String[] importName = this.getImportName();
        String[] correspondImportName = ((UnresolvedImportStatementInfo) o).getImportName();
        if (importName.length != correspondImportName.length) {
            return false;
        }

        for (int i = 0; i < importName.length; i++) {
            if (!importName[i].equals(correspondImportName[i])) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ImportStatementInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final SortedSet<ClassInfo> accessibleClasses = new TreeSet<ClassInfo>();
        if (this.isAllClasses()) {
            final String[] namespace = this.getNamespace();
            final Collection<ClassInfo> specifiedClasses = classInfoManager
                    .getClassInfos(namespace);
            accessibleClasses.addAll(specifiedClasses);
        } else {
            final String[] importName = this.getImportName();
            ClassInfo specifiedClass = classInfoManager.getClassInfo(importName);
            if (null == specifiedClass) {
                specifiedClass = new ExternalClassInfo(importName);
                accessibleClasses.add(specifiedClass);
            }
        }

        this.resolvedInfo = new ImportStatementInfo(fromLine, fromColumn, toLine, toColumn,
                accessibleClasses);
        return this.resolvedInfo;
    }

    /**
     * 名前空間名を返す
     * 
     * @return 名前空間名
     */
    public String[] getImportName() {
        return this.importName;
    }

    /**
     * 名前空間名を返す．
     * 
     * @return 名前空間名
     */
    public String[] getNamespace() {

        final String[] importName = this.getImportName();
        if (this.isAllClasses()) {
            return importName;
        }

        final String[] namespace = new String[importName.length - 1];
        System.arraycopy(importName, 0, namespace, 0, importName.length - 1);
        return namespace;
    }

    /**
     * このオブジェクトのハッシュコードを返す
     * 
     * @return このオブジェクトのハッシュコード
     */
    @Override
    public int hashCode() {

        int hash = 0;
        String[] namespace = this.getNamespace();
        for (int i = 0; i < namespace.length; i++) {
            hash += namespace.hashCode();
        }

        return hash;
    }

    /**
     * 全てのクラスが利用可能かどうか
     * 
     * @return 利用可能である場合は true, そうでない場合は false
     */
    public boolean isAllClasses() {
        return this.allClasses;
    }

    /**
     * 名前空間名を表す変数
     */
    private final String[] importName;

    /**
     * 全てのクラスが利用可能かどうかを表す変数
     */
    private final boolean allClasses;
}
