package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StaticInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決 static initializer を表すクラス
 * 
 * @author t-miyake, higo
 */
public class UnresolvedStaticInitializerInfo extends
        UnresolvedLocalSpaceInfo<StaticInitializerInfo> {

    /**
     * 所有クラスを与えて，オブジェクトを初期化
     * 
     * @param ownerClass 所有クラス
     */
    public UnresolvedStaticInitializerInfo(final UnresolvedClassInfo ownerClass) {
        super();

        if (null == ownerClass) {
            throw new IllegalArgumentException("ownerClass is null");
        }

        this.ownerClass = ownerClass;
    }

    /**
     * 名前解決を行う
     */
    @Override
    public StaticInitializerInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // 使用位置を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // 所有クラスを取得
        final UnresolvedClassInfo unresolvedOwnerClass = this.getOwnerClass();
        final ClassInfo ownerClass = unresolvedOwnerClass.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);

        this.resolvedInfo = new StaticInitializerInfo(ownerClass, fromLine, fromColumn, toLine,
                toColumn);
        return this.resolvedInfo;
    }

    /**
     * このスタティックイニシャライザーを定義しているクラスを返す
     * 
     * @return このスタティックイニシャライザーを定義しているクラス
     */
    public final UnresolvedClassInfo getOwnerClass() {
        return this.ownerClass;
    }

    private final UnresolvedClassInfo ownerClass;

}
