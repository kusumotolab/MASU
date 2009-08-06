package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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
        UnresolvedImportStatementInfo<MemberImportStatementInfo> {

    public static List<UnresolvedMemberImportStatementInfo> getMemberImportStatements(
            final Collection<UnresolvedImportStatementInfo<?>> importStatements) {

        final List<UnresolvedMemberImportStatementInfo> memberImportStatements = new LinkedList<UnresolvedMemberImportStatementInfo>();
        for (final UnresolvedImportStatementInfo<?> importStatement : importStatements) {
            if (importStatement instanceof UnresolvedMemberImportStatementInfo) {
                memberImportStatements.add((UnresolvedMemberImportStatementInfo) importStatement);
            }
        }
        return Collections.unmodifiableList(memberImportStatements);
    }
    
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
        super(namespace, allMembers);
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

        if (this.isAll()) {

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
}
