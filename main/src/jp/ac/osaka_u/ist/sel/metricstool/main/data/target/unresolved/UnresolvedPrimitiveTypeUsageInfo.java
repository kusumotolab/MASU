package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決プリミティブ型使用を表すクラス
 * 
 * @author higo
 *
 */
public final class UnresolvedPrimitiveTypeUsageInfo extends UnresolvedEntityUsageInfo {

    /**
     * 型とリテラルを与えて初期化
     * 
     * @param type　使用されている型
     * @param literal　使用されているリテラル
     */
    public UnresolvedPrimitiveTypeUsageInfo(final PrimitiveTypeInfo type, final String literal) {
        this.type = type;
        this.literal = type.getTypeName();
    }

    /**
     * 使用されているリテラルを返す
     * 
     * @return　使用されているリテラル
     */
    public String getLiteral() {
        return this.literal;
    }

    /**
     * 未解決エンティティ使用が解決されているかどうかを返す
     * 
     * @return 解決されている場合は true，そうでない場合は false
     */
    @Override
    public boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * 解決済みエンティティ使用を返す
     * 
     * @return 解決済みエンティティ使用
     * @throws 解決されていない場合にスローされる
     */
    @Override
    public EntityUsageInfo getResolvedEntityUsage() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }

    /**
     * この未解決プリミティブ型使用を解決
     */
    @Override
    public EntityUsageInfo resolveEntityUsage(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolvedEntityUsage();
        }

        //　位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        this.resolvedInfo = new PrimitiveTypeUsageInfo(this.type, this.literal, fromLine,
                fromColumn, toLine, toColumn);
        return this.resolvedInfo;
    }

    private PrimitiveTypeUsageInfo resolvedInfo;

    private PrimitiveTypeInfo type;

    private String literal;
}
