package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Resolved;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 一度目のASTパースで取得したメソッド情報を一時的に格納するためのクラス．
 * 
 * 
 * @author y-higo
 * 
 */
public class UnresolvedMethodInfo implements VisualizableSetting, MemberSetting, PositionSetting,
        Unresolved {

    /**
     * 未解決メソッド定義情報オブジェクトを初期化
     */
    public UnresolvedMethodInfo() {

        this.methodName = null;
        this.returnType = null;
        this.ownerClass = null;
        this.constructor = false;

        this.modifiers = new HashSet<ModifierInfo>();
        this.parameterInfos = new LinkedList<UnresolvedParameterInfo>();
        this.methodCalls = new HashSet<UnresolvedMethodCall>();
        this.fieldReferences = new HashSet<UnresolvedFieldUsage>();
        this.fieldAssignments = new HashSet<UnresolvedFieldUsage>();
        this.localVariables = new HashSet<UnresolvedLocalVariableInfo>();

        this.privateVisible = false;
        this.inheritanceVisible = false;
        this.namespaceVisible = false;
        this.publicVisible = false;

        this.instance = true;

        this.fromLine = 0;
        this.fromColumn = 0;
        this.toLine = 0;
        this.toColumn = 0;

        this.resolvedInfo = null;
    }

    /**
     * 未解決メソッド定義情報オブジェクトを初期化
     * 
     * @param methodName メソッド名
     * @param returnType 返り値の型
     * @param ownerClass このメソッドを定義しているクラス
     * @param constructor コンストラクタかどうか
     */
    public UnresolvedMethodInfo(final String methodName, final UnresolvedTypeInfo returnType,
            final UnresolvedClassInfo ownerClass, final boolean constructor) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == methodName) || (null == returnType) || (null == ownerClass)) {
            throw new NullPointerException();
        }

        this.methodName = methodName;
        this.returnType = returnType;
        this.ownerClass = ownerClass;
        this.constructor = constructor;

        this.modifiers = new HashSet<ModifierInfo>();
        this.parameterInfos = new LinkedList<UnresolvedParameterInfo>();
        this.methodCalls = new HashSet<UnresolvedMethodCall>();
        this.fieldReferences = new HashSet<UnresolvedFieldUsage>();
        this.fieldAssignments = new HashSet<UnresolvedFieldUsage>();
        this.localVariables = new HashSet<UnresolvedLocalVariableInfo>();

        this.privateVisible = false;
        this.inheritanceVisible = false;
        this.namespaceVisible = false;
        this.publicVisible = false;
    }

    /**
     * コンストラクタかどうかを返す
     * 
     * @return コンストラクタの場合は true，そうでない場合は false
     */
    public boolean isConstructor() {
        return this.constructor;
    }

    /**
     * コンストラクタかどうかをセットする
     * 
     * @param constructor コンストラクタかどうか
     */
    public void setConstructor(final boolean constructor) {
        this.constructor = constructor;
    }

    /**
     * 修飾子の Set を返す
     * 
     * @return 修飾子の Set
     */
    public Set<ModifierInfo> getModifiers() {
        return Collections.unmodifiableSet(this.modifiers);
    }

    /**
     * 修飾子を追加する
     * 
     * @param modifier 追加する修飾子
     */
    public void addModifier(final ModifierInfo modifier) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == modifier) {
            throw new NullPointerException();
        }

        this.modifiers.add(modifier);
    }

    /**
     * メソッド名を返す
     * 
     * @return メソッド名
     */
    public String getMethodName() {
        return this.methodName;
    }

    /**
     * メソッド名をセットする
     * 
     * @param methodName メソッド名
     */
    public void setMethodName(final String methodName) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == methodName) {
            throw new NullPointerException();
        }

        this.methodName = methodName;
    }

    /**
     * メソッドの返り値の型を返す
     * 
     * @return メソッドの返り値の型
     */
    public UnresolvedTypeInfo getReturnType() {
        return this.returnType;
    }

    /**
     * メソッドの返り値をセットする
     * 
     * @param returnType メソッドの返り値
     */
    public void setReturnType(final UnresolvedTypeInfo returnType) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == returnType) {
            throw new NullPointerException();
        }

        this.returnType = returnType;
    }

    /**
     * このメソッドを定義しているクラスを返す
     * 
     * @return このメソッドを定義しているクラス
     */
    public UnresolvedClassInfo getOwnerClass() {
        return this.ownerClass;
    }

    /**
     * メソッドを定義しているクラスをセットする
     * 
     * @param ownerClass メソッドを定義しているクラス
     */
    public void setOwnerClass(final UnresolvedClassInfo ownerClass) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == ownerClass) {
            throw new NullPointerException();
        }

        this.ownerClass = ownerClass;
    }

    /**
     * メソッドに引数を追加する
     * 
     * @param parameterInfo 追加する引数
     */
    public void adParameter(final UnresolvedParameterInfo parameterInfo) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == parameterInfo) {
            throw new NullPointerException();
        }

        this.parameterInfos.add(parameterInfo);
    }

    /**
     * メソッド呼び出しを追加する
     * 
     * @param methodCall メソッド呼び出し
     */
    public void addMethodCall(final UnresolvedMethodCall methodCall) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == methodCall) {
            throw new NullPointerException();
        }

        this.methodCalls.add(methodCall);
    }

    /**
     * フィールド参照を追加する
     * 
     * @param fieldUsage フィールド参照
     */
    public void addFieldReference(final UnresolvedFieldUsage fieldUsage) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fieldUsage) {
            throw new NullPointerException();
        }

        this.fieldReferences.add(fieldUsage);
    }

    /**
     * フィールド代入を追加する
     * 
     * @param fieldUsage フィールド代入
     */
    public void addFieldAssignment(final UnresolvedFieldUsage fieldUsage) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fieldUsage) {
            throw new NullPointerException();
        }

        this.fieldAssignments.add(fieldUsage);
    }

    /**
     * ローカル変数を追加する
     * 
     * @param localVariable ローカル変数
     */
    public void addLocalVariable(final UnresolvedLocalVariableInfo localVariable) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == localVariable) {
            throw new NullPointerException();
        }

        this.localVariables.add(localVariable);
    }

    /**
     * メソッドの引数のリストを返す
     * 
     * @return メソッドの引数のリスト
     */
    public List<UnresolvedParameterInfo> getParameterInfos() {
        return Collections.unmodifiableList(this.parameterInfos);
    }

    /**
     * メソッド呼び出しの Set を返す
     * 
     * @return メソッド呼び出しの Set
     */
    public Set<UnresolvedMethodCall> getMethodCalls() {
        return Collections.unmodifiableSet(this.methodCalls);
    }

    /**
     * フィールド参照の Set を返す
     * 
     * @return フィールド参照の Set
     */
    public Set<UnresolvedFieldUsage> getFieldReferences() {
        return Collections.unmodifiableSet(this.fieldReferences);
    }

    /**
     * フィールド代入の Set を返す
     * 
     * @return フィールド代入の Set
     */
    public Set<UnresolvedFieldUsage> getFieldAssignments() {
        return Collections.unmodifiableSet(this.fieldAssignments);
    }

    /**
     * 定義されているローカル変数の Set を返す
     * 
     * @return 定義されているローカル変数の Set
     */
    public Set<UnresolvedLocalVariableInfo> getLocalVariables() {
        return Collections.unmodifiableSet(this.localVariables);
    }

    /**
     * このメソッドの行数を返す
     * 
     * @return メソッドの行数
     */
    public int getLOC() {
        return this.loc;
    }

    /**
     * このメソッドの行数を保存する
     * 
     * @param loc このメソッドの行数
     */
    public void setLOC(final int loc) {
        this.loc = loc;
    }

    /**
     * 子クラスから参照可能かどうかを設定する
     * 
     * @param inheritanceVisible 子クラスから参照可能な場合は true，そうでない場合は false
     */
    public void setInheritanceVisible(final boolean inheritanceVisible) {
        this.inheritanceVisible = inheritanceVisible;
    }

    /**
     * 同じ名前空間内から参照可能かどうかを設定する
     * 
     * @param namespaceVisible 同じ名前空間から参照可能な場合は true，そうでない場合は false
     */
    public void setNamespaceVisible(final boolean namespaceVisible) {
        this.namespaceVisible = namespaceVisible;
    }

    /**
     * クラス内からのみ参照可能かどうかを設定する
     * 
     * @param privateVisible クラス内からのみ参照可能な場合は true，そうでない場合は false
     */
    public void setPrivateVibible(final boolean privateVisible) {
        this.privateVisible = privateVisible;
    }

    /**
     * どこからでも参照可能かどうかを設定する
     * 
     * @param publicVisible どこからでも参照可能な場合は true，そうでない場合は false
     */
    public void setPublicVisible(final boolean publicVisible) {
        this.publicVisible = publicVisible;
    }

    /**
     * 子クラスから参照可能かどうかを返す
     * 
     * @return 子クラスから参照可能な場合は true, そうでない場合は false
     */
    public boolean isInheritanceVisible() {
        return this.inheritanceVisible;
    }

    /**
     * 同じ名前空間から参照可能かどうかを返す
     * 
     * @return 同じ名前空間から参照可能な場合は true, そうでない場合は false
     */
    public boolean isNamespaceVisible() {
        return this.namespaceVisible;
    }

    /**
     * クラス内からのみ参照可能かどうかを返す
     * 
     * @return クラス内からのみ参照可能な場合は true, そうでない場合は false
     */
    public boolean isPrivateVisible() {
        return this.privateVisible;
    }

    /**
     * どこからでも参照可能かどうかを返す
     * 
     * @return どこからでも参照可能な場合は true, そうでない場合は false
     */
    public boolean isPublicVisible() {
        return this.publicVisible;
    }

    /**
     * インスタンスメンバーかどうかを返す
     * 
     * @return インスタンスメンバーの場合 true，そうでない場合 false
     */
    public boolean isInstanceMember() {
        return this.instance;
    }

    /**
     * スタティックメンバーかどうかを返す
     * 
     * @return スタティックメンバーの場合 true，そうでない場合 false
     */
    public boolean isStaticMember() {
        return !this.instance;
    }

    /**
     * インスタンスメンバーかどうかをセットする
     * 
     * @param instance インスタンスメンバーの場合は true， スタティックメンバーの場合は false
     */
    public void setInstanceMember(final boolean instance) {
        this.instance = instance;
    }

    /**
     * 開始行をセットする
     * 
     * @param fromLine 開始行
     */
    public void setFromLine(final int fromLine) {

        if (fromLine < 0) {
            throw new IllegalArgumentException();
        }

        this.fromLine = fromLine;
    }

    /**
     * 開始列をセットする
     * 
     * @param fromColumn 開始列
     */
    public void setFromColumn(final int fromColumn) {

        if (fromColumn < 0) {
            throw new IllegalArgumentException();
        }

        this.fromColumn = fromColumn;
    }

    /**
     * 終了行をセットする
     * 
     * @param toLine 終了行
     */
    public void setToLine(final int toLine) {

        if (toLine < 0) {
            throw new IllegalArgumentException();
        }

        this.toLine = toLine;
    }

    /**
     * 終了列をセットする
     * 
     * @param toColumn 終了列
     */
    public void setToColumn(final int toColumn) {

        if (toColumn < 0) {
            throw new IllegalArgumentException();
        }

        this.toColumn = toColumn;
    }

    /**
     * 開始行を返す
     * 
     * @return 開始行
     */
    public int getFromLine() {
        return this.fromLine;
    }

    /**
     * 開始列を返す
     * 
     * @return 開始列
     */
    public int getFromColumn() {
        return this.fromColumn;
    }

    /**
     * 終了行を返す
     * 
     * @return 終了行
     */
    public int getToLine() {
        return this.toLine;
    }

    /**
     * 終了列を返す
     * 
     * @return 終了列
     */
    public int getToColumn() {
        return this.toColumn;
    }

    /**
     * 名前解決された情報を返す
     * 
     * @return 名前解決された情報
     */
    public Resolved getResolvedInfo() {
        return this.resolvedInfo;
    }

    /**
     * 名前解決された情報をセットする
     * 
     * @param resolvedInfo 名前解決された情報
     */
    public void setResolvedInfo(final Resolved resolvedInfo) {

        if (null == resolvedInfo) {
            throw new NullPointerException();
        }

        if (!(resolvedInfo instanceof MethodInfo)) {
            throw new IllegalArgumentException();
        }

        this.resolvedInfo = resolvedInfo;
    }

    /**
     * 修飾子を保存する
     */
    private Set<ModifierInfo> modifiers;

    /**
     * メソッド名を保存するための変数
     */
    private String methodName;

    /**
     * メソッド引数を保存するための変数
     */
    private final List<UnresolvedParameterInfo> parameterInfos;

    /**
     * メソッドの返り値を保存するための変数
     */
    private UnresolvedTypeInfo returnType;

    /**
     * このメソッドを定義しているクラスを保存するための変数
     */
    private UnresolvedClassInfo ownerClass;

    /**
     * コンストラクタかどうかを表す変数
     */
    private boolean constructor;

    /**
     * メソッド呼び出しを保存する変数
     */
    private final Set<UnresolvedMethodCall> methodCalls;

    /**
     * フィールド参照を保存する変数
     */
    private final Set<UnresolvedFieldUsage> fieldReferences;

    /**
     * フィールド代入を保存する変数
     */
    private final Set<UnresolvedFieldUsage> fieldAssignments;

    /**
     * このメソッド内で定義されているローカル変数を保存する変数
     */
    private final Set<UnresolvedLocalVariableInfo> localVariables;

    /**
     * メソッドの行数を保存するための変数
     */
    private int loc;

    /**
     * クラス内からのみ参照可能かどうか保存するための変数
     */
    private boolean privateVisible;

    /**
     * 同じ名前空間から参照可能かどうか保存するための変数
     */
    private boolean namespaceVisible;

    /**
     * 子クラスから参照可能かどうか保存するための変数
     */
    private boolean inheritanceVisible;

    /**
     * どこからでも参照可能かどうか保存するための変数
     */
    private boolean publicVisible;

    /**
     * インスタンスメンバーかどうかを保存するための変数
     */
    private boolean instance;

    /**
     * 開始行を保存するための変数
     */
    private int fromLine;

    /**
     * 開始列を保存するための変数
     */
    private int fromColumn;

    /**
     * 終了行を保存するための変数
     */
    private int toLine;

    /**
     * 開始列を保存するための変数
     */
    private int toColumn;

    /**
     * 名前解決された情報を格納するための変数
     */
    private Resolved resolvedInfo;
}
