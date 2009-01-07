package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.HashMap;
import java.util.Map;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableLengthTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決可変長型を表すクラス
 * 
 * @author higo
 *
 */
public class UnresolvedVariableLengthTypeInfo extends UnresolvedArrayTypeInfo {

    /**
     * UnresolvedVariableLengthTypeInfo のインスタンスを返すためのファクトリメソッド．
     * 
     * @param type 未解決型を表す変数
     * @return 生成した UnresolvedVariableLengthTypeInfo オブジェクト
     */
    public static UnresolvedVariableLengthTypeInfo getType(
            final UnresolvedTypeInfo<? extends TypeInfo> type) {

        if (null == type) {
            throw new IllegalArgumentException();
        }

        UnresolvedVariableLengthTypeInfo variableLengthUsage = VARIABLE_LENGTH_TYPE_MAP.get(type);
        if (variableLengthUsage == null) {
            variableLengthUsage = new UnresolvedVariableLengthTypeInfo(type);
            VARIABLE_LENGTH_TYPE_MAP.put(type, variableLengthUsage);
        }

        return variableLengthUsage;
    }

    UnresolvedVariableLengthTypeInfo(final UnresolvedTypeInfo<? extends TypeInfo> type) {
        super(type, 1);
    }

    /**
     * 未解決配列型を解決し，解決済み配列型を返す．
     * 
     * @param usingClass 未解決配列型が存在するクラス
     * @param usingMethod 未解決配列型が存在するメソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     * @return 解決済み配列型
     */
    @Override
    public ArrayTypeInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == classInfoManager)) {
            throw new IllegalArgumentException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        final UnresolvedTypeInfo<?> unresolvedElementType = this.getElementType();
        final TypeInfo elementType = unresolvedElementType.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        assert null != elementType : "resolveEntityUsage returned null!";

        // 要素の型が不明のときは UnnownTypeInfo を返す
        if (elementType instanceof UnknownTypeInfo) {
            this.resolvedInfo = VariableLengthTypeInfo.getType(UnknownTypeInfo.getInstance());
            return this.resolvedInfo;

            // 要素の型が解決できた場合はその配列型を作成し返す
        } else {
            this.resolvedInfo = VariableLengthTypeInfo.getType(elementType);
            return this.resolvedInfo;
        }
    }

    /**
     * UnresolvedVariableLengthTypeInfo オブジェクトを一元管理するための Map．オブジェクトはファクトリメソッドで生成される．
     */
    private static final Map<UnresolvedTypeInfo<?>, UnresolvedVariableLengthTypeInfo> VARIABLE_LENGTH_TYPE_MAP = new HashMap<UnresolvedTypeInfo<?>, UnresolvedVariableLengthTypeInfo>();
}
