package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.NamespaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決クラス型を表すクラス
 * 
 * @author higo
 * 
 */
public class UnresolvedClassTypeInfo implements UnresolvedReferenceTypeInfo<ClassTypeInfo> {

    /**
     * 利用可能な名前空間名，参照名を与えて初期化
     * 
     * @param availableNamespaces 名前空間名
     * @param referenceName 参照名
     */
    public UnresolvedClassTypeInfo(
            final List<UnresolvedClassImportStatementInfo> availableNamespaces,
            final String[] referenceName) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == availableNamespaces) || (null == referenceName)) {
            throw new NullPointerException();
        }

        this.availableNamespaces = availableNamespaces;
        this.referenceName = Arrays.<String> copyOf(referenceName, referenceName.length);
        this.typeArguments = new LinkedList<UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo>>();
    }

    /**
     * この未解決クラス型がすでに解決済みかどうかを返す．
     * 
     * @return 解決済みの場合は true，解決されていない場合は false
     */
    public boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * この未解決クラス型の解決済みの型を返す
     */
    @Override
    public ClassTypeInfo getResolved() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }

    @Override
    public ClassTypeInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // import 文で指定されているクラスが登録されていないなら，外部クラスとして登録する
        for (final UnresolvedClassImportStatementInfo availableNamespace : this
                .getAvailableNamespaces()) {

            if (!availableNamespace.isAll()) {
                final String[] fullQualifiedName = availableNamespace.getImportName();
                if (!classInfoManager.hasClassInfo(fullQualifiedName)) {
                    final ExternalClassInfo externalClassInfo = new ExternalClassInfo(
                            fullQualifiedName);
                    classInfoManager.add(externalClassInfo);
                }
            }
        }

        final String[] referenceName = this.getReferenceName();
        final Collection<ClassInfo> classInfos = classInfoManager
                .getClassInfos(referenceName[referenceName.length - 1]);
        for (final ClassInfo classInfo : classInfos) {

            final String className = classInfo.getClassName();
            final NamespaceInfo namespace = classInfo.getNamespace();

            //　複数項参照の場合は，完全限定名かどうかを調べる
            if (!this.isMoniminalReference()) {

                final String[] referenceNamespace = Arrays.copyOf(referenceName,
                        referenceName.length - 1);
                if (classInfo.getNamespace().equals(referenceNamespace)) {
                    final ClassTypeInfo classType = new ClassTypeInfo(classInfo);
                    for (final UnresolvedTypeInfo<? extends ReferenceTypeInfo> unresolvedTypeArgument : this
                            .getTypeArguments()) {
                        final TypeInfo typeArgument = unresolvedTypeArgument.resolve(usingClass,
                                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                        classType.addTypeArgument(typeArgument);
                    }
                    this.resolvedInfo = classType;
                    return this.resolvedInfo;
                }
            }

            // 単項参照の場合は，デフォルトパッケージからクラスを検索
            if (this.isMoniminalReference()) {

                for (final ClassInfo defaultClassInfo : classInfoManager
                        .getClassInfos(new String[0])) {

                    // 参照されているクラスが見つかった
                    if (referenceName[0].equals(defaultClassInfo.getClassName())) {
                        final ClassTypeInfo classType = new ClassTypeInfo(defaultClassInfo);
                        for (final UnresolvedTypeInfo<? extends ReferenceTypeInfo> unresolvedTypeArgument : this
                                .getTypeArguments()) {
                            final TypeInfo typeArgument = unresolvedTypeArgument.resolve(
                                    usingClass, usingMethod, classInfoManager, fieldInfoManager,
                                    methodInfoManager);
                            classType.addTypeArgument(typeArgument);
                        }
                        this.resolvedInfo = classType;
                        return this.resolvedInfo;
                    }
                }
            }

            // 単項演算の場合は，インポートされているクラスから検索
            if (this.isMoniminalReference()) {

                for (final UnresolvedClassImportStatementInfo availableNamespace : this
                        .getAvailableNamespaces()) {

                    final String[] importedNamespace = availableNamespace.getNamespace();
                    if (namespace.equals(importedNamespace)) {

                        // import aaa.bbb.*の場合 (クラス名の部分が*)
                        if (availableNamespace.isAll()) {

                            Collection<ClassInfo> importedClassInfos = classInfoManager
                                    .getClassInfos(importedNamespace);
                            for (final ClassInfo importedClassInfo : importedClassInfos) {

                                //クラスが見つかった
                                if (className.equals(importedClassInfo.getClassName())) {
                                    final ClassTypeInfo classType = new ClassTypeInfo(
                                            importedClassInfo);
                                    for (final UnresolvedTypeInfo<? extends ReferenceTypeInfo> unresolvedTypeArgument : this
                                            .getTypeArguments()) {
                                        final TypeInfo typeArgument = unresolvedTypeArgument
                                                .resolve(usingClass, usingMethod, classInfoManager,
                                                        fieldInfoManager, methodInfoManager);
                                        classType.addTypeArgument(typeArgument);
                                    }
                                    this.resolvedInfo = classType;
                                    return this.resolvedInfo;
                                }
                            }

                            // import aaa.bbb.Ccc の場合 (クラス名まで明示的に記述されている)
                        } else {

                            ClassInfo importedClassInfo = classInfoManager
                                    .getClassInfo(availableNamespace.getImportName());
                            if (null == importedClassInfo) {
                                importedClassInfo = new ExternalClassInfo(referenceName);
                                classInfoManager.add((ExternalClassInfo) importedClassInfo);
                            }

                            //クラスが見つかった
                            if (className.equals(importedClassInfo.getClassName())) {
                                final ClassTypeInfo classType = new ClassTypeInfo(importedClassInfo);
                                for (final UnresolvedTypeInfo<? extends ReferenceTypeInfo> unresolvedTypeArgument : this
                                        .getTypeArguments()) {
                                    final TypeInfo typeArgument = unresolvedTypeArgument.resolve(
                                            usingClass, usingMethod, classInfoManager,
                                            fieldInfoManager, methodInfoManager);
                                    classType.addTypeArgument(typeArgument);
                                }
                                this.resolvedInfo = classType;
                                return this.resolvedInfo;
                            }
                        }
                    }
                }
            }

            // 複数項参照の場合は，importされているクラスのインナークラスを調べる
            if (!this.isMoniminalReference()) {

                for (final UnresolvedClassImportStatementInfo availableNamespace : this
                        .getAvailableNamespaces()) {

                    final String[] importedNamespace = availableNamespace.getNamespace();
                    if (namespace.equals(importedNamespace)) {

                        // import aaa.bbb.*の場合 (クラス名の部分が*)
                        if (availableNamespace.isAll()) {

                            final Collection<ClassInfo> importedClassInfos = classInfoManager
                                    .getClassInfos(importedNamespace);
                            for (final ClassInfo importedClassInfo : importedClassInfos) {

                                if (importedClassInfo instanceof TargetClassInfo) {
                                    final SortedSet<ClassInfo> importedInnerClassInfos = TargetClassInfo
                                            .getAccessibleInnerClasses(importedClassInfo);

                                    for (final ClassInfo importedInnerClassInfo : importedInnerClassInfos) {

                                        if (importedInnerClassInfo.equals(classInfo)) {
                                            final ClassTypeInfo classType = new ClassTypeInfo(
                                                    classInfo);
                                            for (final UnresolvedTypeInfo<? extends ReferenceTypeInfo> unresolvedTypeArgument : this
                                                    .getTypeArguments()) {
                                                final TypeInfo typeArgument = unresolvedTypeArgument
                                                        .resolve(usingClass, usingMethod,
                                                                classInfoManager, fieldInfoManager,
                                                                methodInfoManager);
                                                classType.addTypeArgument(typeArgument);
                                            }
                                            this.resolvedInfo = classType;
                                            return this.resolvedInfo;
                                        }
                                    }
                                }
                            }

                            // import aaa.bbb.Ccc の場合
                        } else {

                            final String[] importedFullQualifiedName = availableNamespace
                                    .getImportName();
                            final ClassInfo importedClassInfo = classInfoManager
                                    .getClassInfo(importedFullQualifiedName);
                            if (importedClassInfo instanceof TargetClassInfo) {
                                final SortedSet<ClassInfo> importedInnerClassInfos = TargetClassInfo
                                        .getAllInnerClasses(importedClassInfo);

                                for (final ClassInfo importedInnerClassInfo : importedInnerClassInfos) {

                                    if (importedInnerClassInfo.equals(classInfo)) {
                                        final ClassTypeInfo classType = new ClassTypeInfo(classInfo);
                                        for (final UnresolvedTypeInfo<? extends ReferenceTypeInfo> unresolvedTypeArgument : this
                                                .getTypeArguments()) {
                                            final TypeInfo typeArgument = unresolvedTypeArgument
                                                    .resolve(usingClass, usingMethod,
                                                            classInfoManager, fieldInfoManager,
                                                            methodInfoManager);
                                            classType.addTypeArgument(typeArgument);
                                        }
                                        this.resolvedInfo = classType;
                                        return this.resolvedInfo;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //ここにくるのは，クラスが見つからなかったとき
        if (this.isMoniminalReference()) {

            /*
            { // 単項参照の場合は，型パラメータの型かを調査
                final List<TypeParameterInfo> availableTypeParameters = null == usingMethod ? NameResolver
                        .getAvailableTypeParameters(usingClass)
                        : NameResolver.getAvailableTypeParameters(usingMethod);
                for (final TypeParameterInfo typeParameter : availableTypeParameters) {
                    if (referenceName[0].equals(typeParameter.getName())) {
                        this.resolvedInfo = new TypeParameterTypeInfo(typeParameter);
                        return this.resolvedInfo;
                    }
                }
            }*/

            final ExternalClassInfo externalClassInfo = new ExternalClassInfo(referenceName[0]);
            final ClassTypeInfo classType = new ClassTypeInfo(externalClassInfo);
            for (final UnresolvedTypeInfo<? extends ReferenceTypeInfo> unresolvedTypeArgument : this
                    .getTypeArguments()) {
                final TypeInfo typeArgument = unresolvedTypeArgument.resolve(usingClass,
                        usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                classType.addTypeArgument(typeArgument);
            }
            this.resolvedInfo = classType;

        } else {

            final ExternalClassInfo externalClassInfo = new ExternalClassInfo(referenceName);
            final ClassTypeInfo classType = new ClassTypeInfo(externalClassInfo);
            for (final UnresolvedTypeInfo<? extends ReferenceTypeInfo> unresolvedTypeArgument : this
                    .getTypeArguments()) {
                final TypeInfo typeArgument = unresolvedTypeArgument.resolve(usingClass,
                        usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                classType.addTypeArgument(typeArgument);
            }
            this.resolvedInfo = classType;
        }

        return this.resolvedInfo;

        /* ここからふるい実装
        //　単項参照の場合
        if (this.isMoniminalReference()) {

            //　インポートされているパッケージ内のクラスから検索
            for (final AvailableNamespaceInfo availableNamespace : this.getAvailableNamespaces()) {

                // import aaa.bbb.*の場合 (クラス名の部分が*)
                if (availableNamespace.isAllClasses()) {

                    //　利用可能なクラス一覧を取得し，そこから検索
                    final String[] namespace = availableNamespace.getNamespace();
                    for (final ClassInfo availableClass : classInfoManager.getClassInfos(namespace)) {

                        //　参照されているクラスが見つかった
                        if (this.referenceName[0].equals(availableClass.getClassName())) {
                            this.resolvedInfo = new ClassTypeInfo(availableClass);
                            for (final UnresolvedTypeInfo<? extends ReferenceTypeInfo> unresolvedTypeArgument : this
                                    .getTypeArguments()) {
                                final TypeInfo typeArgument = unresolvedTypeArgument.resolve(
                                        usingClass, usingMethod, classInfoManager,
                                        fieldInfoManager, methodInfoManager);
                                this.resolvedInfo.addTypeArgument(typeArgument);
                            }
                            return this.resolvedInfo;
                        }
                    }

                    // import aaa.bbb.CCCの場合　(クラス名まで記述されている)
                } else {

                    ClassInfo importedClass = classInfoManager.getClassInfo(availableNamespace
                            .getImportName());

                    // null の場合は外部クラスの参照とみなす
                    if (null == importedClass) {
                        importedClass = new ExternalClassInfo(availableNamespace.getImportName());
                        classInfoManager.add((ExternalClassInfo) importedClass);
                    }

                    // import のクラス名とこの参照されているクラス名が一致する場合は，そのクラスの参照とみなす
                    final String importedClassName = importedClass.getClassName();
                    if (this.referenceName[0].equals(importedClassName)) {
                        this.resolvedInfo = new ClassTypeInfo(importedClass);
                        for (final UnresolvedTypeInfo<? extends ReferenceTypeInfo> unresolvedTypeArgument : this
                                .getTypeArguments()) {
                            final TypeInfo typeArgument = unresolvedTypeArgument.resolve(
                                    usingClass, usingMethod, classInfoManager, fieldInfoManager,
                                    methodInfoManager);
                            this.resolvedInfo.addTypeArgument(typeArgument);
                        }
                        return this.resolvedInfo;
                    }
                }
            }

            // デフォルトパッケージからクラスを検索
            for (final ClassInfo availableClass : classInfoManager.getClassInfos(new String[0])) {

                // 参照されているクラスが見つかった
                if (this.referenceName[0].equals(availableClass.getClassName())) {
                    this.resolvedInfo = new ClassTypeInfo(availableClass);
                    for (final UnresolvedTypeInfo<? extends ReferenceTypeInfo> unresolvedTypeArgument : this
                            .getTypeArguments()) {
                        final TypeInfo typeArgument = unresolvedTypeArgument.resolve(usingClass,
                                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                        this.resolvedInfo.addTypeArgument(typeArgument);
                    }
                    return this.resolvedInfo;
                }
            }

            // 不明なクラス型である
            final ExternalClassInfo unknownReferencedClass = new ExternalClassInfo(
                    this.referenceName[0]);
            this.resolvedInfo = new ClassTypeInfo(unknownReferencedClass);
            return this.resolvedInfo;

            // 複数項参照の場合
        } else {

            //　インポートされているクラスの子クラスから検索
            AVAILABLENAMESPACE: for (final AvailableNamespaceInfo availableNamespace : this
                    .getAvailableNamespaces()) {

                // import aaa.bbb.*の場合 (クラス名の部分が*)
                if (availableNamespace.isAllClasses()) {

                    // 利用可能なクラス一覧を取得し，そこから検索
                    final String[] namespace = availableNamespace.getNamespace();
                    for (final ClassInfo availableClass : classInfoManager.getClassInfos(namespace)) {

                        //　参照されているクラスが見つかった
                        if (this.referenceName[0].equals(availableClass.getClassName())) {

                            // 対象クラスでない場合は内部クラス情報はわからないのでスキップ
                            if (!(availableClass instanceof TargetClassInfo)) {
                                continue AVAILABLENAMESPACE;
                            }

                            // 対象クラスの場合は，順に内部クラスをたどって行く
                            TargetClassInfo currentClass = (TargetClassInfo) availableClass;
                            INDEX: for (int index = 1; index < this.referenceName.length; index++) {
                                final SortedSet<TargetInnerClassInfo> innerClasses = currentClass
                                        .getInnerClasses();
                                for (final TargetInnerClassInfo innerClass : innerClasses) {

                                    if (this.referenceName[index].equals(innerClass.getClassName())) {
                                        currentClass = innerClass;
                                        continue INDEX;
                                    }

                                    // ここに到達するのは，クラスが見つからなかった場合
                                    final ExternalClassInfo unknownReferencedClass = new ExternalClassInfo(
                                            this.referenceName[this.referenceName.length - 1]);
                                    this.resolvedInfo = new ClassTypeInfo(unknownReferencedClass);
                                    for (final UnresolvedTypeInfo<? extends ReferenceTypeInfo> unresolvedTypeArgument : this
                                            .getTypeArguments()) {
                                        final TypeInfo typeArgument = unresolvedTypeArgument
                                                .resolve(usingClass, usingMethod, classInfoManager,
                                                        fieldInfoManager, methodInfoManager);
                                        this.resolvedInfo.addTypeArgument(typeArgument);
                                    }
                                    return this.resolvedInfo;
                                }
                            }

                            //　ここに到達するのは，クラスが見つかった場合
                            this.resolvedInfo = new ClassTypeInfo(currentClass);
                            for (final UnresolvedTypeInfo<? extends ReferenceTypeInfo> unresolvedTypeArgument : this
                                    .getTypeArguments()) {
                                final TypeInfo typeArgument = unresolvedTypeArgument.resolve(
                                        usingClass, usingMethod, classInfoManager,
                                        fieldInfoManager, methodInfoManager);
                                this.resolvedInfo.addTypeArgument(typeArgument);
                            }
                            return this.resolvedInfo;
                        }
                    }

                    // import aaa.bbb.CCCの場合 (クラス名まで記述されている)
                } else {

                    // 参照名を完全限定名とするクラスがあるかをチェック
                    {
                        final ClassInfo referencedClass = classInfoManager
                                .getClassInfo(this.referenceName);
                        if (null != referencedClass) {
                            this.resolvedInfo = new ClassTypeInfo(referencedClass);
                            for (final UnresolvedTypeInfo<? extends ReferenceTypeInfo> unresolvedTypeArgument : this
                                    .getTypeArguments()) {
                                final TypeInfo typeArgument = unresolvedTypeArgument.resolve(
                                        usingClass, usingMethod, classInfoManager,
                                        fieldInfoManager, methodInfoManager);
                                this.resolvedInfo.addTypeArgument(typeArgument);
                            }
                            return this.resolvedInfo;
                        }
                    }

                    ClassInfo importClass = classInfoManager.getClassInfo(availableNamespace
                            .getImportName());

                    //　null の場合はその(外部)クラスを表すオブジェクトを作成 
                    if (null == importClass) {
                        importClass = new ExternalClassInfo(availableNamespace.getImportName());
                        classInfoManager.add((ExternalClassInfo) importClass);
                    }

                    // importClassが対象クラスでない場合は内部クラス情報がわからないのでスキップ
                    if (!(importClass instanceof TargetClassInfo)) {
                        continue AVAILABLENAMESPACE;
                    }

                    //　対象クラスの場合は，参照名と一致しているかをチェック
                    // 参照名がインポート名よりも短い場合は該当しない
                    final String[] importFullQualifiedName = importClass.getFullQualifiedName();
                    if (this.referenceName.length < importFullQualifiedName.length) {
                        continue AVAILABLENAMESPACE;
                    }

                    // 参照名がインポート名と同じ長さ，もしくはより長い場合は詳しく調べる
                    int index = 0;
                    for (; index < importFullQualifiedName.length; index++) {
                        if (!importFullQualifiedName[index].equals(this.referenceName[index])) {
                            continue AVAILABLENAMESPACE;
                        }
                    }

                    // 参照名の方が長いので，インポートクラスの内部クラスをたどって一致するものがあるかを調べる
                    TargetClassInfo currentClass = (TargetClassInfo) importClass;
                    INDEX: for (; index < this.referenceName.length; index++) {

                        for (final TargetInnerClassInfo innerClass : currentClass.getInnerClasses()) {

                            if (this.referenceName[index].equals(innerClass.getClassName())) {
                                currentClass = innerClass;
                                continue INDEX;
                            }

                            // ここにくるのは，クラスが見つからなかった場合
                            continue AVAILABLENAMESPACE;
                        }

                    }

                    //　ここに到達するのは，クラスが見つかった場合
                    this.resolvedInfo = new ClassTypeInfo(currentClass);
                    for (final UnresolvedTypeInfo<? extends ReferenceTypeInfo> unresolvedTypeArgument : this
                            .getTypeArguments()) {
                        final TypeInfo typeArgument = unresolvedTypeArgument.resolve(usingClass,
                                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                        this.resolvedInfo.addTypeArgument(typeArgument);
                    }
                    return this.resolvedInfo;
                }
            }

            // 不明なクラス型である
            final ExternalClassInfo unknownReferencedClass = new ExternalClassInfo(
                    this.referenceName[this.referenceName.length - 1]);
            this.resolvedInfo = new ClassTypeInfo(unknownReferencedClass);
            for (final UnresolvedTypeInfo<? extends ReferenceTypeInfo> unresolvedTypeArgument : this
                    .getTypeArguments()) {
                final TypeInfo typeArgument = unresolvedTypeArgument.resolve(usingClass,
                        usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                this.resolvedInfo.addTypeArgument(typeArgument);
            }
            return this.resolvedInfo;
        }
        */
    }

    /**
     * 利用可能な名前空間，型の完全修飾名を与えて初期化
     * @param referenceName 型の完全修飾名
     */
    public UnresolvedClassTypeInfo(final String[] referenceName) {
        this(new LinkedList<UnresolvedClassImportStatementInfo>(), referenceName);
    }

    /**
     * 型パラメータ使用を追加する
     * 
     * @param typeParameterUsage 追加する型パラメータ使用
     */
    public final void addTypeArgument(
            final UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> typeParameterUsage) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeParameterUsage) {
            throw new NullPointerException();
        }

        this.typeArguments.add(typeParameterUsage);
    }

    /**
     * このクラス参照で使用されている型パラメータの List を返す
     * 
     * @return このクラス参照で使用されている型パラメータの List
     */
    public final List<UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo>> getTypeArguments() {
        return Collections.unmodifiableList(this.typeArguments);
    }

    /**
     * この参照型の名前を返す
     * 
     * @return この参照型の名前を返す
     */
    public final String getTypeName() {
        return this.referenceName[this.referenceName.length - 1];
    }

    /**
     * この参照型の参照名を返す
     * 
     * @return この参照型の参照名を返す
     */
    public final String[] getReferenceName() {
        return Arrays.<String> copyOf(this.referenceName, this.referenceName.length);
    }

    /**
     * この参照型の参照名を引数で与えられた文字で結合して返す
     * 
     * @param delimiter 結合に用いる文字
     * @return この参照型の参照名を引数で与えられた文字で結合した文字列
     */
    public final String getReferenceName(final String delimiter) {

        if (null == delimiter) {
            throw new NullPointerException();
        }

        final StringBuilder sb = new StringBuilder(this.referenceName[0]);
        for (int i = 1; i < this.referenceName.length; i++) {
            sb.append(delimiter);
            sb.append(this.referenceName[i]);
        }

        return sb.toString();
    }

    /**
     * この参照型の完全限定名として可能性のある名前空間名の一覧を返す
     * 
     * @return この参照型の完全限定名として可能性のある名前空間名の一覧
     */
    public final List<UnresolvedClassImportStatementInfo> getAvailableNamespaces() {
        return this.availableNamespaces;
    }

    /**
     * この参照が単項かどうかを返す
     * 
     * @return　単項である場合はtrue，そうでない場合はfalse
     */
    public final boolean isMoniminalReference() {
        return 1 == this.referenceName.length;
    }

    /**
     * 未解決クラスを与えると，その未解決参照型を返す
     * 
     * @param referencedClass 未解決クラス
     * @return 与えられた未解決クラスの未解決参照型
     */
    public final static UnresolvedClassTypeInfo getInstance(UnresolvedClassInfo referencedClass) {
        return new UnresolvedClassTypeInfo(referencedClass.getFullQualifiedName());
    }

    /**
     * この未解決参照型が表す未解決クラス参照を返す
     * 
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     * @return この未解決参照型が表す未解決クラス参照
     */
    public final UnresolvedClassReferenceInfo getUsage(final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        UnresolvedClassReferenceInfo usage = new UnresolvedClassReferenceInfo(
                this.availableNamespaces, this.referenceName);
        usage.setFromLine(fromLine);
        usage.setFromColumn(fromColumn);
        usage.setToLine(toLine);
        usage.setToColumn(toColumn);

        for (UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> typeArgument : this.typeArguments) {
            usage.addTypeArgument(typeArgument);
        }
        return usage;
    }

    /**
     * 利用可能な名前空間名を保存するための変数，名前解決処理の際に用いる
     */
    private final List<UnresolvedClassImportStatementInfo> availableNamespaces;

    /**
     * 参照名を保存する変数
     */
    private final String[] referenceName;

    /**
     * 型引数を保存するための変数
     */
    private final List<UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo>> typeArguments;

    private ClassTypeInfo resolvedInfo;

}
