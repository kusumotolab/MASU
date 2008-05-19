package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LiteralUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決リテラル使用を表すクラス
 * 
 * @author higo
 *
 */
public final class UnresolvedLiteralUsageInfo extends UnresolvedEntityUsageInfo<LiteralUsageInfo> {

    /**
     * リテラルの文字列表現とリテラルの型を与えてオブジェクトを初期化
     * 
     * @param literal リテラルの文字列表現
     * @param type リテラルの型
     */
    public UnresolvedLiteralUsageInfo(final String literal, final PrimitiveTypeInfo type) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == literal) || (null == type)) {
            throw new IllegalArgumentException();
        }

        this.literal = literal;
        this.type = type;
        this.resolvedInfo = null;
    }

    @Override
    public LiteralUsageInfo resolve(final TargetClassInfo usingClass,
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

        final String literal = this.getLiteral();
        final PrimitiveTypeInfo type = (PrimitiveTypeInfo) this.getType();

        this.resolvedInfo = new LiteralUsageInfo(literal, type, fromLine, fromColumn, toLine,
                toColumn);
        return this.resolvedInfo;
    }

    /**
     * このリテラル使用の文字列を返す
     * 
     * @return このリテラル使用の文字列
     */
    public final String getLiteral() {
        return this.literal;
    }

    /**
     * このリテラル使用の文字列を返す
     * 
     * @return　このリテラル使用の文字列を返す
     */
    public final UnresolvedTypeInfo getType() {
        return this.type;
    }

    private final String literal;

    private final PrimitiveTypeInfo type;

}
