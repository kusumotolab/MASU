package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.InnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.InstanceInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StaticInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetAnonymousClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetInnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
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
 * </ul>
 * 
 * @author higo
 * 
 */
public final class UnresolvedClassInfo extends UnresolvedUnitInfo<TargetClassInfo> implements
        VisualizableSetting, StaticOrInstanceSetting, ModifierSetting {

    /**
     * このクラスが記述されているファイル情報を与えて初期化
     * 
     * @param fileInfo このクラスが記述されいてるファイル情報
     * @param outerUnit このクラスの外側のユニット
     */
    public UnresolvedClassInfo(final FileInfo fileInfo,
            final UnresolvedUnitInfo<? extends UnitInfo> outerUnit) {

        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == fileInfo) {
            throw new IllegalArgumentException("fileInfo is null");
        }

        this.outerUnit = outerUnit;

        this.fileInfo = fileInfo;
        this.namespace = null;
        this.className = null;

        this.modifiers = new HashSet<ModifierInfo>();
        this.typeParameters = new LinkedList<UnresolvedTypeParameterInfo>();
        this.superClasses = new ArrayList<UnresolvedClassTypeInfo>();
        this.innerClasses = new HashSet<UnresolvedClassInfo>();
        this.definedMethods = new HashSet<UnresolvedMethodInfo>();
        this.definedConstructors = new HashSet<UnresolvedConstructorInfo>();
        this.definedFields = new HashSet<UnresolvedFieldInfo>();
        this.implicitStaticInitializer = new UnresolvedStaticInitializerInfo(this);
        this.implicitInstanceInitializer = new UnresolvedInstanceInitializerInfo(this);
        this.instanceInitializers = new HashSet<UnresolvedInstanceInitializerInfo>();
        this.instanceInitializers.add(this.implicitInstanceInitializer);
        this.staticInitializers = new HashSet<UnresolvedStaticInitializerInfo>();
        this.staticInitializers.add(this.implicitStaticInitializer);
        this.importStatements = new LinkedList<UnresolvedImportStatementInfo<?>>();

        this.privateVisible = false;
        this.inheritanceVisible = false;
        this.namespaceVisible = false;
        this.publicVisible = false;
        this.isInterface = false;

        this.instance = true;

        this.anonymous = false;

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
     * @param type 追加する未解決型パラメータ
     */
    public void addTypeParameter(final UnresolvedTypeParameterInfo type) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == type) {
            throw new NullPointerException();
        }

        this.typeParameters.add(type);
    }

    /**
     * 未解決インスタンスイニシャライザを追加
     * 
     * @param instanceInitializer 未解決インスタンスイニシャライザ
     */
    public void addInstanceInitializer(final UnresolvedInstanceInitializerInfo instanceInitializer) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == instanceInitializer) {
            throw new NullPointerException("instanceInitializer is null");
        }
        this.instanceInitializers.add(instanceInitializer);
    }

    /**
     * 未解決スタティックイニシャライザを追加
     * 
     * @param staticInitialzer 未解決スタティックイニシャライザ
     */
    public void addStaticInitializer(final UnresolvedStaticInitializerInfo staticInitialzer) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == staticInitialzer) {
            throw new NullPointerException("staticInitializer is null");
        }
        this.staticInitializers.add(staticInitialzer);
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
     * このクラスが記述されているファイル情報を返す
     * 
     * @return このクラスが記述されているファイル情報
     */
    public FileInfo getFileInfo() {
        return this.fileInfo;
    }

    /**
     * 名前空間名を返す
     * 
     * @return 名前空間名
     */
    public String[] getNamespace() {
        return Arrays.<String> copyOf(this.namespace, this.namespace.length);
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
     * 
     * @return 未解決型パラメータの List
     */
    public List<UnresolvedTypeParameterInfo> getTypeParameters() {
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

        this.namespace = Arrays.<String> copyOf(namespace, namespace.length);
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
     * 親クラスを追加する
     * 
     * @param superClass 親クラス名
     */
    public void addSuperClass(final UnresolvedClassTypeInfo superClass) {

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
     * 定義しているコンストラクタを追加する
     * 
     * @param definedConstructor 定義しているコンストラクタメソッド
     */
    public void addDefinedConstructor(final UnresolvedConstructorInfo definedConstructor) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == definedConstructor) {
            throw new NullPointerException();
        }

        this.definedConstructors.add(definedConstructor);
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
     * このクラスにおいて利用可能な（インポートされている）クラスを追加する
     * 
     * @param importStatement このクラスにおいて利用可能な（インポートされている）クラス
     */
    public void addImportStatement(final UnresolvedClassImportStatementInfo importStatement) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == importStatement) {
            throw new IllegalArgumentException();
        }

        this.importStatements.add(importStatement);
    }

    /**
     * このクラスにおいて利用可能な（インポートされている）クラス群を追加する
     * 
     * @param importStatements このクラスにおいて利用可能な（インポートされている）クラス群
     */
    public void addImportStatements(final List<UnresolvedImportStatementInfo<?>> importStatements) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == importStatements) {
            throw new IllegalArgumentException();
        }

        this.importStatements.addAll(importStatements);
    }

    /**
     * 親クラス名のセットを返す
     * 
     * @return 親クラス名のセット
     */
    public List<UnresolvedClassTypeInfo> getSuperClasses() {
        return Collections.unmodifiableList(this.superClasses);
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
     * 外側のユニットを返す
     * 
     * @return 外側のユニット. 外側のユニットがない場合はnull
     */
    public UnresolvedUnitInfo<?> getOuterUnit() {
        return this.outerUnit;
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
     * 定義しているコンストラクタのセットを返す
     * 
     * @return 定義しているコンストラクタのセット
     */
    public Set<UnresolvedConstructorInfo> getDefinedConstructors() {
        return Collections.unmodifiableSet(this.definedConstructors);
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
     * インスタンスイニシャライザのセットを返す
     * @return インスタンスイニシャライザのセット
     */
    public final Set<UnresolvedInstanceInitializerInfo> getInstanceInitializers() {
        return Collections.unmodifiableSet(this.instanceInitializers);
    }

    /**
     * スタティックイニシャライザのセットを返す
     * @return スタティックイニシャライザのセット
     */
    public final Set<UnresolvedStaticInitializerInfo> getStaticInitializers() {
        return Collections.unmodifiableSet(this.staticInitializers);
    }

    /**
     * 利用可能なクラス（インポートされているクラス）とメンバ（インポートされているメンバ）のListを返す
     * 
     * @return　利用可能なクラス（インポートされているクラス）とメンバ（インポートされているメンバ）のListを返す
     */
    public List<UnresolvedImportStatementInfo<?>> getImportStatements() {
        return Collections.unmodifiableList(this.importStatements);
    }

    /**
     * インスタンスイニシャライザを返す
     * 
     * @return インスランスイニシャライザ
     */
    public UnresolvedInstanceInitializerInfo getImplicitInstanceInitializer() {
        return this.implicitInstanceInitializer;
    }

    /**
     * スタティックイニシャライザを返す
     * 
     * @return スタティックイニシャライザ
     */
    public UnresolvedStaticInitializerInfo getImplicitStaticInitializer() {
        return this.implicitStaticInitializer;
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
     * 外側のユニットをセットする
     * 
     * @param outerUnit 外側のユニット
     */
    public void setOuterUnit(final UnresolvedUnitInfo<?> outerUnit) {
        this.outerUnit = outerUnit;
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
     * インターフェースかどうかを返す
     * 
     * @return インターフェースの場合はtrue, そうでない場合はfalse
     */
    public final boolean isInterface() {
        return this.isInterface;
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
     * インターフェースかどうかをセットする．
     * @param isInterface インターフェースの場合は true，クラスの場合は false
     */
    public void setIsInterface(final boolean isInterface) {
        this.isInterface = isInterface;
    }

    /**
     * 無名クラスかどうかをセットする
     * 
     * @param anonymous 無名クラスの場合は true，そうでない場合は false
     */
    public void setAnonymous(final boolean anonymous) {
        this.anonymous = anonymous;
    }

    /**
     * 無名クラスかどうかを返す
     * 
     * @return 無名クラスである場合はtrue, そうでない場合はfalse
     */
    public boolean isAnonymous() {
        return this.anonymous;
    }

    /**
     * この未解決クラス情報を解決する
     * 
     * @param usingClass 所属クラス，このメソッド呼び出しの際は null さセットされていると思われる．
     * @param usingMethod 所属メソッド，このメソッド呼び出しの際は null さセットされていると思われる．
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     */
    @Override
    public TargetClassInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // 必要な引数がnullでないかをチェック
        if ((null == classInfoManager) || (null == fieldInfoManager) || (null == methodInfoManager)) {
            throw new IllegalArgumentException();
        }

        // 修飾子，完全限定名，行数，可視性，インスタンスメンバーかどうかを取得
        final Set<ModifierInfo> modifiers = this.getModifiers();
        final String[] fullQualifiedName = this.getFullQualifiedName();
        final boolean privateVisible = this.isPrivateVisible();
        final boolean namespaceVisible = this.isNamespaceVisible();
        final boolean inheritanceVisible = this.isInheritanceVisible();
        final boolean publicVisible = this.isPublicVisible();
        final boolean instance = this.isInstanceMember();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // ClassInfo オブジェクトを作成し，ClassInfoManagerに登録
        // 無名クラスの場合
        if (this.isAnonymous()) {
            final UnresolvedClassInfo unresolvedOuterClass = this.getOuterClass();
            final TargetClassInfo outerClass = unresolvedOuterClass.resolve(usingClass,
                    usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

            final UnresolvedUnitInfo<? extends UnitInfo> unresolvedOuterUnit = this.getOuterUnit();
            final UnitInfo outerUnit = unresolvedOuterUnit.resolve(usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager);

            this.resolvedInfo = new TargetAnonymousClassInfo(fullQualifiedName, outerClass, null,
                    this.fileInfo, fromLine, fromColumn, toLine, toColumn);
            // TODO outerUnitを登録する

            // 一番外側のクラスの場合
        } else if (null == this.outerUnit) {
            this.resolvedInfo = new TargetClassInfo(modifiers, fullQualifiedName, privateVisible,
                    namespaceVisible, inheritanceVisible, publicVisible, instance,
                    this.isInterface, this.fileInfo, fromLine, fromColumn, toLine, toColumn);

            // インナークラスの場合
        } else {

            final UnresolvedClassInfo unresolvedOuterClass = this.getOuterClass();
            final TargetClassInfo outerClass = unresolvedOuterClass.resolve(usingClass,
                    usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

            // outerUnitは後で解決する．ここでは登録しない
            this.resolvedInfo = new TargetInnerClassInfo(modifiers, fullQualifiedName, outerClass,
                    privateVisible, namespaceVisible, inheritanceVisible, publicVisible, instance,
                    this.isInterface, this.fileInfo, fromLine, fromColumn, toLine, toColumn);
        }

        // タイプパラメータがある場合は解決する．ただしここでは，exntends までは解決しない
        for (final UnresolvedTypeParameterInfo unresolvedTypeParameter : this.getTypeParameters()) {
            final TypeParameterInfo typeParameter = unresolvedTypeParameter.resolve(
                    this.resolvedInfo, usingMethod, classInfoManager, fieldInfoManager,
                    methodInfoManager);
            this.resolvedInfo.addTypeParameter(typeParameter);
        }

        //　このクラスで定義しているメソッドを解決
        for (final UnresolvedMethodInfo unresolvedMethod : this.getDefinedMethods()) {
            final TargetMethodInfo method = unresolvedMethod.resolve(this.resolvedInfo, null,
                    classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addDefinedMethod(method);
            methodInfoManager.add(method);
        }

        //　このクラスで定義しているコンストラクタを解決
        for (final UnresolvedConstructorInfo unresolvedConstructor : this.getDefinedConstructors()) {
            final TargetConstructorInfo constructor = unresolvedConstructor.resolve(
                    this.resolvedInfo, null, classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addDefinedConstructor(constructor);
            methodInfoManager.add(constructor);
        }

        //　このクラスで定義しているフィールドを解決
        for (final UnresolvedFieldInfo unresolvedConstructor : this.getDefinedFields()) {
            final TargetFieldInfo field = unresolvedConstructor.resolve(this.resolvedInfo, null,
                    classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addDefinedField(field);
            fieldInfoManager.add(field);
        }

        // このクラスで定義されているインスタンスイニシャライザを解決
        for (final UnresolvedInstanceInitializerInfo unresolvedInitializer : this
                .getInstanceInitializers()) {
            final InstanceInitializerInfo initializer = unresolvedInitializer.resolve(
                    this.resolvedInfo, null, classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addInstanceInitializer(initializer);
        }

        // このクラスで定義されているスタティックイニシャライザを解決
        for (final UnresolvedStaticInitializerInfo unresolvedInitializer : this
                .getStaticInitializers()) {
            final StaticInitializerInfo initializer = unresolvedInitializer.resolve(
                    this.resolvedInfo, null, classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addStaticInitializer(initializer);
        }

        return this.resolvedInfo;
    }

    /**
     * 未解決スーパークラス情報を解決する．
     * すでにresolveメソッドが呼び出された状態で用いなければならない
     * 
     * @param classInfoManager
     * @return
     */
    public TargetClassInfo resolveSuperClass(final ClassInfoManager classInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new IllegalArgumentException();
        }

        final TargetClassInfo resolved = this.getResolved();

        for (final UnresolvedClassTypeInfo unresolvedSuperType : this.getSuperClasses()) {

            // スーパークラスを設定
            final ReferenceTypeInfo superType = unresolvedSuperType.resolve(resolved, null,
                    classInfoManager, null, null);
            resolved.addSuperClass((ClassTypeInfo) superType);

            // サブクラスを設定
            final ClassInfo superClass = ((ClassTypeInfo) superType).getReferencedClass();
            superClass.addSubClass(resolved);
        }

        return resolved;
    }

    /**
     * 未解決型パラメータ情報を解決する．
     * すでにresolveメソッドが呼び出された状態で用いなければならない
     * 
     * @param classInfoManager
     * @return
     */
    public TargetClassInfo resolveTypeParameter(final ClassInfoManager classInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new IllegalArgumentException();
        }

        final TargetClassInfo resolved = this.getResolved();

        for (final UnresolvedTypeParameterInfo unresolvedTypeParameter : this.getTypeParameters()) {

            final TypeParameterInfo typeParameter = unresolvedTypeParameter.getResolved();
            for (final UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> unresolvedExtendsType : unresolvedTypeParameter
                    .getExtendsTypes()) {
                final ReferenceTypeInfo extendsType = unresolvedExtendsType.resolve(resolved, null,
                        classInfoManager, null, null);
                typeParameter.addExtendsType(extendsType);
            }
        }

        return resolved;
    }

    /**
     * 未解決内部クラス情報を解決する．
     * すでにresolveメソッドが呼び出された状態で用いなければならない
     * 
     * @param classInfoManager
     * @return
     */
    public TargetClassInfo resolveInnerClass(final ClassInfoManager classInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new IllegalArgumentException();
        }

        final TargetClassInfo resolved = this.getResolved();

        for (final UnresolvedClassInfo unresolvedInnerClass : this.getInnerClasses()) {
            final TargetClassInfo innerClass = unresolvedInnerClass.getResolved();
            resolved.addInnerClass((InnerClassInfo) innerClass);
        }

        return resolved;
    }

    /**
     * この未解決クラス定義情報の未解決参照型を返す
     * 
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列 
     * @return この未解決クラス定義情報の未解決参照型
     */
    public UnresolvedClassReferenceInfo getClassReference(final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {
        final UnresolvedClassReferenceInfo classReference = new UnresolvedFullQualifiedNameClassReferenceInfo(
                this);
        classReference.setFromLine(fromLine);
        classReference.setFromColumn(fromColumn);
        classReference.setToLine(toLine);
        classReference.setToColumn(toColumn);

        // 型パラメータの数だけ，型引数を追加
        for (final UnresolvedTypeParameterInfo typeParameter : this.typeParameters) {
            classReference.addTypeArgument(new UnresolvedClassTypeInfo(new String[] { "java",
                    "lang", "Object" }));
        }
        return classReference;
    }

    public UnresolvedClassTypeInfo getClassType() {
        if (null != this.classType) {
            return this.classType;
        }
        final List<UnresolvedClassImportStatementInfo> namespaces = new LinkedList<UnresolvedClassImportStatementInfo>();
        final UnresolvedClassImportStatementInfo namespace = new UnresolvedClassImportStatementInfo(
                this.getFullQualifiedName(), false);
        namespaces.add(namespace);
        this.classType = new UnresolvedClassTypeInfo(namespaces, this.getFullQualifiedName());
        return this.classType;
    }

    @Override
    public String toString() {
        return "class \"" + this.className + "\" in file \"" + this.fileInfo.getName() + "\"";
    }

    private UnresolvedClassInfo getOuterClass() {

        UnresolvedUnitInfo<?> outerUnit = this.getOuterUnit();
        if (null == outerUnit) {
            return null;
        }

        if (outerUnit instanceof UnresolvedClassInfo) {
            return (UnresolvedClassInfo) outerUnit;
        }

        if (outerUnit instanceof UnresolvedCallableUnitInfo<?>) {
            return ((UnresolvedCallableUnitInfo<?>) outerUnit).getOwnerClass();
        }

        assert false : "Here shouldn't be reached!";
        return null;
    }

    /**
     * クラスが記述されているファイル情報を保存するための変数
     */
    private final FileInfo fileInfo;

    /**
     * 名前空間名を保存するための変数
     */
    private String[] namespace;

    /**
     * クラス名を保存するための変数
     */
    private String className;

    /**
     * 修飾子を保存するための変数
     */
    private final Set<ModifierInfo> modifiers;

    /**
     * 型パラメータを保存するための変数
     */
    private final List<UnresolvedTypeParameterInfo> typeParameters;

    /**
     * 親クラスを保存するためのリスト
     */
    private final List<UnresolvedClassTypeInfo> superClasses;

    /**
     * インナークラスを保存するためのセット
     */
    private final Set<UnresolvedClassInfo> innerClasses;

    /**
     * 外側のユニットを保持する変数
     */
    private UnresolvedUnitInfo<?> outerUnit;

    /**
     * 定義しているメソッドを保存するためのセット
     */
    private final Set<UnresolvedMethodInfo> definedMethods;

    /**
     * 定義しているコンストラクタを保存するためのセット
     */
    private final Set<UnresolvedConstructorInfo> definedConstructors;

    /**
     * 定義しているフィールドを保存するためのセット
     */
    private final Set<UnresolvedFieldInfo> definedFields;

    /**
     * 暗黙のスタティックイニシャライザを保存するための変数
     */
    private final UnresolvedStaticInitializerInfo implicitStaticInitializer;

    /**
     * 暗黙のインスタンスイニシャライザを保存するための変数
     */
    private final UnresolvedInstanceInitializerInfo implicitInstanceInitializer;

    /**
     * スタティックイニシャライザ一覧を保存するための変数
     */
    private final Set<UnresolvedStaticInitializerInfo> staticInitializers;

    /**
     * インスタンスイニシャライザ一覧を保存するための変数
     */
    private final Set<UnresolvedInstanceInitializerInfo> instanceInitializers;

    /**
     * 利用可能な名前空間を保存するためのセット
     */
    private final List<UnresolvedImportStatementInfo<?>> importStatements;

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
     * インターフェースであるかどうかを保存するための変数
     */
    private boolean isInterface;

    /**
     * 無名クラスかどうかを表す変数
     */
    private boolean anonymous;

    private UnresolvedClassTypeInfo classType = null;

}
