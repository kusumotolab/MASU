package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;

/**
 * 未解決キャスト使用を表すクラス
 * 
 * @author t-miyake
 *
 */
public class UnresolvedCastUsageInfo extends UnresolvedEntityUsageInfo {

    /**
     * キャストされた方を与えて初期化
     * 
     * @param castedType キャストされた型
     */
    public UnresolvedCastUsageInfo(UnresolvedTypeInfo castedType) {
        this.castedType = castedType;
    }

    /**
     * キャストされた型を返す
     * @return キャストされた型
     */
    public UnresolvedTypeInfo getCastedType() {
        return castedType;
    }
    
    @Override
    boolean alreadyResolved() {
        // TODO 自動生成されたメソッド・スタブ
        return false;
    }

    @Override
    EntityUsageInfo getResolvedEntityUsage() {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public EntityUsageInfo resolveEntityUsage(TargetClassInfo usingClass,
            CallableUnitInfo usingMethod, ClassInfoManager classInfoManager,
            FieldInfoManager fieldInfoManager, MethodInfoManager methodInfoManager) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    /**
     * キャストされた型を保存する変数
     */
    private final UnresolvedTypeInfo castedType;


}
