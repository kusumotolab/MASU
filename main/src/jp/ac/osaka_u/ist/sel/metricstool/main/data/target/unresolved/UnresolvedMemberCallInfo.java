package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決メンバ(メソッド，コンストラクタ)呼び出しを保存するためのクラス
 * 
 * @author t-miyake, higo
 *
 */
public abstract class UnresolvedMemberCallInfo implements UnresolvedEntityUsageInfo {

    public UnresolvedMemberCallInfo() {

        MetricsToolSecurityManager.getInstance().checkAccess();

        this.typeArguments = new LinkedList<UnresolvedClassTypeInfo>();
        this.parameterTypes = new LinkedList<UnresolvedEntityUsageInfo>();

        this.resolvedInfo = null;

    }

    /**
     * この未解決メソッド呼び出しがすでに解決されているかどうかを返す
     * 
     * @return 既に解決されている場合は true，そうでない場合は false
     */
    public final boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * 解決済みメソッド呼び出し情報を返す
     * 
     * @return 解決済みメソッド呼び出し情報
     * @throw 解決されていない場合にスローされる
     */
    public final EntityUsageInfo getResolvedEntityUsage() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }

    /**
     * 型パラメータ使用を追加する
     * 
     * @param typeParameterUsage 追加する型パラメータ使用
     */
    public final void addTypeArgument(final UnresolvedClassTypeInfo typeParameterUsage) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeParameterUsage) {
            throw new NullPointerException();
        }

        this.typeArguments.add(typeParameterUsage);
    }

    /**
     * 引数を追加
     * 
     * @param typeInfo
     */
    public final void addParameter(final UnresolvedEntityUsageInfo typeInfo) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeInfo) {
            throw new NullPointerException();
        }

        this.parameterTypes.add(typeInfo);
    }

    /**
     * 引数の List を返す
     * 
     * @return 引数の List
     */
    public final List<UnresolvedEntityUsageInfo> getParameters() {
        return Collections.unmodifiableList(this.parameterTypes);
    }

    /**
     * 型パラメータ使用の List を返す
     * 
     * @return 型パラメータ使用の List
     */
    public final List<UnresolvedClassTypeInfo> getTypeArguments() {
        return Collections.unmodifiableList(this.typeArguments);
    }

    /**
     * 
     * @param unresolvedParameters
     * @return
     */
    protected final List<EntityUsageInfo> resolveParameters(final TargetClassInfo usingClass,
            final TargetMethodInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        //　解決済み実引数を格納するための変数
        final List<EntityUsageInfo> parameters = new LinkedList<EntityUsageInfo>();

        for (final UnresolvedEntityUsageInfo unresolvedParameter : this.getParameters()) {

            EntityUsageInfo parameter = unresolvedParameter.resolveEntityUsage(usingClass,
                    usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

            assert parameter != null : "resolveEntityUsage returned null!";

            if (parameter instanceof UnknownEntityUsageInfo) {

                // クラス参照だった場合
                if (unresolvedParameter instanceof UnresolvedClassReferenceInfo) {

                    // TODO 型パラメータの情報を格納する                    
                    final ExternalClassInfo externalClassInfo = NameResolver
                            .createExternalClassInfo((UnresolvedClassReferenceInfo) unresolvedParameter);
                    classInfoManager.add(externalClassInfo);
                    final ClassTypeInfo referenceType = new ClassTypeInfo(externalClassInfo);
                    parameter = new ClassReferenceInfo(referenceType);

                } else {
                    assert false : "Here shouldn't be reached!";
                }
            }
            parameters.add(parameter);
        }

        return parameters;
    }



    /**
     * 型パラメータ使用を保存するための変数
     */
    protected List<UnresolvedClassTypeInfo> typeArguments;

    /**
     * 引数を保存するための変数
     */
    protected List<UnresolvedEntityUsageInfo> parameterTypes;

    /**
     * 解決済みメソッド呼び出し情報を保存するための変数
     */
    protected EntityUsageInfo resolvedInfo;

    /**
     * エラーメッセージ出力用のプリンタ
     */
    protected static final MessagePrinter err = new DefaultMessagePrinter(new MessageSource() {
        public String getMessageSourceName() {
            return "UnresolvedMethodCall";
        }
    }, MESSAGE_TYPE.ERROR);
}
