package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ASTパースで取得したクラス情報を一時的に格納するためのクラス． 以下の情報を持つ
 * 
 * <ul>
 * <li>修飾子</li>
 * <li>未解決名前空間</li>
 * <li>型パラメータ名一覧</li>
 * <li>クラス名</li>
 * <li>行数</li>
 * <li>未解決親クラス名一覧</li>
 * <li>未解決子クラス名一覧</li>
 * <li>未解決インナークラス一覧</li>
 * <li>未解決定義メソッド一覧</li>
 * <li>未解決定義フィールド一覧</li>
 * <li>ファイル内位置</li>
 * </ul>
 * 
 * @author y-higo
 * 
 */
public final class UnresolvedClassInfo implements UnresolvedTypeInfo, VisualizableSetting,
        MemberSetting, PositionSetting, NameResolvable<TargetClassInfo> {

    /**
     * 引数なしコンストラクタ
     */
    public UnresolvedClassInfo() {

        MetricsToolSecurityManager.getInstance().checkAccess();

        this.namespace = null;
        this.className = null;
        this.loc = 0;

        this.modifiers = new HashSet<ModifierInfo>();
        this.typeParameters = new LinkedList<UnresolvedTypeParameterInfo>();
        this.superClasses = new LinkedHashSet<UnresolvedTypeInfo>();
        this.innerClasses = new HashSet<UnresolvedClassInfo>();
        this.definedMethods = new HashSet<UnresolvedMethodInfo>();
        this.definedFields = new HashSet<UnresolvedFieldInfo>();

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
     * 未解決型パラメータを追加する
     * 
     * @param typeParameter 追加する未解決型パラメータ名
     */
    public void addTypeParameter(final UnresolvedTypeParameterInfo typeParameter){
        
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeParameter){
            throw new NullPointerException();
        }
        
        this.typeParameters.add(typeParameter);
    }

    /**
     * このクラスと対象クラスが等しいかどうかを判定する
     * 
     * @param o 比較対象クラス
     */
    @Override
    public boolean equals(final Object o) {

        if (null == o) {
            throw new NullPointerException();
        }

        if (!(o instanceof UnresolvedClassInfo)) {
            return false;
        }

        final String[] fullQualifiedName = this.getFullQualifiedName();
        final String[] correspondFullQualifiedName = ((UnresolvedClassInfo) o)
                .getFullQualifiedName();

        if (fullQualifiedName.length != correspondFullQualifiedName.length) {
            return false;
        }

        for (int i = 0; i < fullQualifiedName.length; i++) {
            if (!fullQualifiedName[i].equals(correspondFullQualifiedName[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * このクラスのハッシュコードを返す
     * 
     * @return このクラスのハッシュコード
     */
    @Override
    public int hashCode() {

        final StringBuffer buffer = new StringBuffer();
        final String[] fullQualifiedName = this.getFullQualifiedName();
        for (int i = 0; i < fullQualifiedName.length; i++) {
            buffer.append(fullQualifiedName[i]);
        }

        return buffer.toString().hashCode();
    }

    /**
     * 名前空間名を返す
     * 
     * @return 名前空間名
     */
    public String[] getNamespace() {
        return this.namespace;
    }

    /**
     * クラス名を取得する
     * 
     * @return クラス名
     */
    public String getClassName() {
        return this.className;
    }

    /**
     * このクラスの完全修飾名を返す
     * 
     * @return このクラスの完全修飾名
     */
    public String[] getFullQualifiedName() {

        final String[] namespace = this.getNamespace();
        final String[] fullQualifiedName = new String[namespace.length + 1];

        for (int i = 0; i < namespace.length; i++) {
            fullQualifiedName[i] = namespace[i];
        }
        fullQualifiedName[fullQualifiedName.length - 1] = this.getClassName();

        return fullQualifiedName;
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
     * 未解決型パラメータの List を返す
     * @return 未解決型パラメータの List
     */
    public List<UnresolvedTypeParameterInfo> getTypeParameters(){
        return Collections.unmodifiableList(this.typeParameters);
    }
    
    /**
     * 名前空間名を保存する.名前空間名がない場合は長さ0の配列を与えること．
     * 
     * @param namespace 名前空間名
     */
    public void setNamespace(final String[] namespace) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == namespace) {
            throw new NullPointerException();
        }

        this.namespace = namespace;
    }

    /**
     * クラス名を保存する
     * 
     * @param className
     */
    public void setClassName(final String className) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == className) {
            throw new NullPointerException();
        }

        this.className = className;
    }

    /**
     * 行数を取得する
     * 
     * @return 行数
     */
    public int getLOC() {
        return this.loc;
    }

    /**
     * 行数を保存する
     * 
     * @param loc 行数
     */
    public void setLOC(final int loc) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (loc < 0) {
            throw new IllegalArgumentException("LOC must be o or more!");
        }

        this.loc = loc;
    }

    /**
     * 親クラスを追加する
     * 
     * @param superClass 親クラス名
     */
    public void addSuperClass(final UnresolvedReferenceTypeInfo superClass) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == superClass) {
            throw new NullPointerException();
        }

        this.superClasses.add(superClass);
    }

    /**
     * インナークラスを追加する
     * 
     * @param innerClass インナークラス
     */
    public void addInnerClass(final UnresolvedClassInfo innerClass) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == innerClass) {
            throw new NullPointerException();
        }

        this.innerClasses.add(innerClass);
    }

    /**
     * 定義しているメソッドを追加する
     * 
     * @param definedMethod 定義しているメソッド
     */
    public void addDefinedMethod(final UnresolvedMethodInfo definedMethod) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == definedMethod) {
            throw new NullPointerException();
        }

        this.definedMethods.add(definedMethod);
    }

    /**
     * 定義しているフィールドを追加する
     * 
     * @param definedField 定義しているフィールド
     */
    public void addDefinedField(final UnresolvedFieldInfo definedField) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == definedField) {
            throw new NullPointerException();
        }

        this.definedFields.add(definedField);
    }

    /**
     * 親クラス名のセットを返す
     * 
     * @return 親クラス名のセット
     */
    public Set<UnresolvedTypeInfo> getSuperClasses() {
        return Collections.unmodifiableSet(this.superClasses);
    }

    /**
     * インナークラスのセットを返す
     * 
     * @return インナークラスのセット
     */
    public Set<UnresolvedClassInfo> getInnerClasses() {
        return Collections.unmodifiableSet(this.innerClasses);
    }

    /**
     * 外部クラスを返す
     * 
     * @return 外部クラス. 外部クラスがない場合はnull
     */
    public UnresolvedClassInfo getOuterClass() {
        return this.outerClass;
    }

    /**
     * 定義しているメソッドのセットを返す
     * 
     * @return 定義しているメソッドのセット
     */
    public Set<UnresolvedMethodInfo> getDefinedMethods() {
        return Collections.unmodifiableSet(this.definedMethods);
    }

    /**
     * 定義しているフィールドのセット
     * 
     * @return 定義しているフィールドのセット
     */
    public Set<UnresolvedFieldInfo> getDefinedFields() {
        return Collections.unmodifiableSet(this.definedFields);
    }

    /**
     * 型名を返す
     * 
     * @return 型名
     */
    public String getTypeName() {

        final String delimiter = Settings.getLanguage().getNamespaceDelimiter();
        final StringBuffer buffer = new StringBuffer();

        for (final String name : this.getFullQualifiedName()) {
            buffer.append(name);
            buffer.append(delimiter);
        }

        int delimiterLength = 0;
        if (null != delimiter) {
            delimiterLength = delimiter.length();
        }

        return buffer.substring(0, buffer.length() - delimiterLength);
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
     * 外部クラスをセットする
     * 
     * @param outerClass 外部クラス
     */
    public void setOuterClass(final UnresolvedClassInfo outerClass) {
        this.outerClass = outerClass;
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
    public TargetClassInfo getResolvedInfo() {
        return this.resolvedInfo;
    }

    /**
     * 名前解決された情報をセットする
     * 
     * @param resolvedInfo 名前解決された情報
     */
    public void setResolvedInfo(final TargetClassInfo resolvedInfo) {

        if (null == resolvedInfo) {
            throw new NullPointerException();
        }

        this.resolvedInfo = resolvedInfo;
    }
    
    /**
     * 既に名前解決されたかどうかを返す
     * 
     * @return 名前解決されている場合は true，そうでない場合は false
     */
    public final boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * 名前空間名を保存するための変数
     */
    private String[] namespace;

    /**
     * クラス名を保存するための変数
     */
    private String className;

    /**
     * 行数を保存するための変数
     */
    private int loc;

    /**
     * 修飾子を保存するための変数
     */
    private final Set<ModifierInfo> modifiers;

    /**
     * 型パラメータを保存するための変数
     */
    private final List<UnresolvedTypeParameterInfo> typeParameters;
    
    /**
     * 親クラスを保存するためのセット
     */
    private final Set<UnresolvedTypeInfo> superClasses;

    /**
     * インナークラスを保存するためのセット
     */
    private final Set<UnresolvedClassInfo> innerClasses;

    /**
     * 外側のクラスを保持する変数
     */
    private UnresolvedClassInfo outerClass;

    /**
     * 定義しているメソッドを保存するためのセット
     */
    private final Set<UnresolvedMethodInfo> definedMethods;

    /**
     * 定義しているフィールドを保存するためのセット
     */
    private final Set<UnresolvedFieldInfo> definedFields;

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
    private TargetClassInfo resolvedInfo;
}
