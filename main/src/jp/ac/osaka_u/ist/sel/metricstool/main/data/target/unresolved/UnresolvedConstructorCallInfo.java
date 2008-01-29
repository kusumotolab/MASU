package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

import java.util.LinkedList;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;

/**
 * 未解決コンストラクタ呼び出しを保存するためのクラス
 * 
 * @author t-miyake
 *
 */
public final class UnresolvedConstructorCallInfo extends UnresolvedMemberCallInfo {


    /**
     * コンストラクタ呼び出しが実行される参照型と名前を与えてオブジェクトを初期化
     * 
     * @param type コンストラクタ呼び出しが実行される型
     * @param callName 呼び出されたときの名前
     */
    public UnresolvedConstructorCallInfo(final UnresolvedReferenceTypeInfo type) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == type) {
            throw new IllegalArgumentException();
        }
        this.type = type;
        this.memberName = type.getTypeName();
        this.typeArguments = new LinkedList<UnresolvedReferenceTypeInfo>();
        this.parameterTypes = new LinkedList<UnresolvedEntityUsageInfo>();

        this.resolvedInfo = null;
    }

	public EntityUsageInfo resolveEntityUsage(TargetClassInfo usingClass,
			TargetMethodInfo usingMethod, ClassInfoManager classInfoManager,
			FieldInfoManager fieldInfoManager,
			MethodInfoManager methodInfoManager) {
		// TODO 解決せんといかん
		return null;
	}
	
	public UnresolvedReferenceTypeInfo getType() {
		return this.type;
	}
	
	private final UnresolvedReferenceTypeInfo type;
    
    
}
