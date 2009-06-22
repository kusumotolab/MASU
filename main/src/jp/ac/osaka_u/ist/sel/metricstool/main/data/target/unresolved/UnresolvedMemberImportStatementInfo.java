package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Arrays;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Member;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MemberImportStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StaticOrInstanceProcessing;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * staticインポートを表すクラス
 * 
 * @author higo
 *
 */
public class UnresolvedMemberImportStatementInfo extends
        UnresolvedUnitInfo<MemberImportStatementInfo> {

    /**
     * クラス名とそれ以下staticメンバー全てが利用可能かどうかを表すbooleanを与えてオブジェクトを初期化.
     * <p>
     * import aaa.bbb.CCC.DDD； // new UnresolvedMemberImportStatementInfo({"aaa","bbb","CCC","DDD"}, false); <br>
     * import aaa.bbb.CCC.*; // new AvailableNamespace({"aaa","bbb","CCC"},true); <br>
     * </p>
     * 
     * @param namespace 利用可能名前空間名
     * @param allMembers 全てのクラスが利用可能かどうか
     */
    public UnresolvedMemberImportStatementInfo(final String[] namespace, final boolean allMembers) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == namespace) {
            throw new NullPointerException();
        }

        this.importName = Arrays.<String> copyOf(namespace, namespace.length);
        this.allMembers = allMembers;
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

        if (!(o instanceof UnresolvedMemberImportStatementInfo)) {
            return false;
        }

        String[] importName = this.getImportName();
        String[] correspondImportName = ((UnresolvedMemberImportStatementInfo) o).getImportName();
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
    public MemberImportStatementInfo resolve(final TargetClassInfo usingClass,
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

        // 位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final String[] fullQualifiedName = this.getFullQualifiedName();
        ClassInfo classInfo = classInfoManager.getClassInfo(fullQualifiedName);
        final Set<Member> accessibleMembers = new TreeSet<Member>();
        if (null == classInfo) {
            classInfo = new ExternalClassInfo(fullQualifiedName);
            classInfoManager.add(classInfo);
        }

        if (this.isAllMembers()) {

            if (classInfo instanceof TargetClassInfo) {
                final SortedSet<TargetFieldInfo> fields = ((TargetClassInfo) classInfo)
                        .getDefinedFields();
                final SortedSet<TargetFieldInfo> staticFields = StaticOrInstanceProcessing
                        .getStaticMembers(fields);
                accessibleMembers.addAll(staticFields);
                final SortedSet<TargetMethodInfo> methods = ((TargetClassInfo) classInfo)
                        .getDefinedMethods();
                final SortedSet<TargetMethodInfo> staticMethods = StaticOrInstanceProcessing
                        .getStaticMembers(methods);
                accessibleMembers.addAll(staticMethods);
            }
        }

        else {

            final String[] importName = this.getImportName();
            final String memberName = importName[importName.length - 1];

            if (classInfo instanceof TargetClassInfo) {
                final SortedSet<TargetFieldInfo> fields = ((TargetClassInfo) classInfo)
                        .getDefinedFields();
                for (TargetFieldInfo field : fields) {
                    if (memberName.equals(field.getName())) {
                        accessibleMembers.add(field);
                    }
                }
                final SortedSet<TargetMethodInfo> methods = ((TargetClassInfo) classInfo)
                        .getDefinedMethods();
                for (TargetMethodInfo method : methods) {
                    if (memberName.equals(method.getMethodName())) {
                        accessibleMembers.add(method);
                    }
                }
            }

            // 外部メンバを追加する処理が必要
            else {

            }
        }

        this.resolvedInfo = new MemberImportStatementInfo(accessibleMembers, fromLine, fromColumn,
                toLine, toColumn);
        return this.resolvedInfo;
    }

    /**
     * 名前空間名を返す
     * 
     * @return 名前空間名
     */
    public String[] getImportName() {
        return Arrays.<String> copyOf(this.importName, this.importName.length);
    }

    /**
     * 名前空間名を返す．
     * 
     * @return 名前空間名
     */
    public String[] getFullQualifiedName() {

        final String[] importName = this.getImportName();
        if (this.isAllMembers()) {
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
        final String[] namespace = this.getFullQualifiedName();
        return Arrays.hashCode(namespace);
    }

    /**
     * 全てのクラスが利用可能かどうか
     * 
     * @return 利用可能である場合は true, そうでない場合は false
     */
    public boolean isAllMembers() {
        return this.allMembers;
    }

    /**
     * 名前空間名を表す変数
     */
    private final String[] importName;

    /**
     * 全てのクラスが利用可能かどうかを表す変数
     */
    private final boolean allMembers;
}
