package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;

/**
 * 未解決配列型参照を表すクラス
 * 
 * @author t-miyake
 *
 */
public class UnresolvedArrayTypeReferenceInfo extends UnresolvedEntityUsageInfo {

    /**
     * 参照されている未解決配列型を与えて初期化
     * 
     * @param referencedType 参照されている未解決配列型
     */
    public UnresolvedArrayTypeReferenceInfo(final UnresolvedArrayTypeInfo referencedType) {
        this.referencedType = referencedType;
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
     * 参照されている未解決配列型を返す
     * @return 参照されている未解決配列型
     */
    public UnresolvedArrayTypeInfo getType() {
        return this.referencedType;
    }
    
    /**
     * 参照されている未解決配列型を保存するための変数
     */
    private final UnresolvedArrayTypeInfo referencedType;
}
