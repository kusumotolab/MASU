package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

import java.util.Collections;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;

/**
 * 未解決メンバ(メソッド，コンストラクタ)呼び出しを保存するためのクラス
 * 
 * @author t-miyake
 *
 */
public abstract class UnresolvedMemberCallInfo  implements UnresolvedEntityUsageInfo {

	/**
     * この未解決メソッド呼び出しがすでに解決されているかどうかを返す
     * 
     * @return 既に解決されている場合は true，そうでない場合は false
     */
    public boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * 解決済みメソッド呼び出し情報を返す
     * 
     * @return 解決済みメソッド呼び出し情報
     * @throw 解決されていない場合にスローされる
     */
    public EntityUsageInfo getResolvedEntityUsage() {

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
    public void addTypeArgument(final UnresolvedReferenceTypeInfo typeParameterUsage) {

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
    public void addParameter(final UnresolvedEntityUsageInfo typeInfo) {

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
    public List<UnresolvedEntityUsageInfo> getParameters() {
        return Collections.unmodifiableList(this.parameterTypes);
    }

    /**
     * 型パラメータ使用の List を返す
     * 
     * @return 型パラメータ使用の List
     */
    public List<UnresolvedReferenceTypeInfo> getTypeArguments() {
        return Collections.unmodifiableList(this.typeArguments);
    }
    
    /**
     * メソッド名を返す
     * 
     * @return メソッド名
     */
    public String getMemberName() {
        return this.memberName;
    }
    
    /**
     * メンバ名を保存するための変数
     */
    protected String memberName;

    /**
     * 型パラメータ使用を保存するための変数
     */
    protected List<UnresolvedReferenceTypeInfo> typeArguments;

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
