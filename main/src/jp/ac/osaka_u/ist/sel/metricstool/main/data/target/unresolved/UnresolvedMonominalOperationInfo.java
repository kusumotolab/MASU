package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;

/**
 * 一項演算の内容を表すクラス
 * 
 * @author t-miyake
 *
 */
public class UnresolvedMonominalOperationInfo extends UnresolvedEntityUsageInfo {

    /**
     * 項と一項演算の結果の型を与えて初期化
     * 
     * @param term 項
     * @param type 一項演算の結果の型
     */
    public UnresolvedMonominalOperationInfo(final UnresolvedEntityUsageInfo term, final PrimitiveTypeInfo type) {
        this.term = term;
        this.type = type;
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
     * 一項演算の項を返す
     * 
     * @return 一項演算の項
     */
    public UnresolvedEntityUsageInfo getTerm() {
        return term;
    }

    /**
     * 一項演算の結果の型を返す
     * 
     * @return 一項演算の結果の型
     */
    public PrimitiveTypeInfo getResultType() {
        return type;
    }
    
    /**
     * 一項演算の項
     */
    private final UnresolvedEntityUsageInfo term;
    
    /**
     * 一項演算の結果の型
     */
    private final PrimitiveTypeInfo type;

    

}
