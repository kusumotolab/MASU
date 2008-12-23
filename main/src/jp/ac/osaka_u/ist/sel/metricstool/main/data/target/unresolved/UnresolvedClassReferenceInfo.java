package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetInnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決クラス参照を表すクラス
 * 
 * @author higo
 * 
 */
public class UnresolvedClassReferenceInfo extends UnresolvedEntityUsageInfo<EntityUsageInfo> {

    /**
     * 利用可能な名前空間名，参照名を与えて初期化
     * 
     * @param availableNamespaces 名前空間名
     * @param referenceName 参照名
     */
    public UnresolvedClassReferenceInfo(final List<AvailableNamespaceInfo> availableNamespaces,
            final String[] referenceName) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == availableNamespaces) || (null == referenceName)) {
            throw new NullPointerException();
        }

        this.availableNamespaces = availableNamespaces;
        this.referenceName = referenceName;
        this.fullReferenceName = referenceName;
        this.qualifierUsage = null;
        this.typeArguments = new LinkedList<UnresolvedReferenceTypeInfo<?>>();
    }

    /**
     * 利用可能な名前空間名，参照名を与えて初期化
     * 
     * @param availableNamespaces 名前空間名
     * @param referenceName 参照名
     * @param ownerUsage 親参照
     */
    public UnresolvedClassReferenceInfo(final List<AvailableNamespaceInfo> availableNamespaces,
            final String[] referenceName, final UnresolvedClassReferenceInfo ownerUsage) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == availableNamespaces) || (null == referenceName) || (null == ownerUsage)) {
            throw new NullPointerException();
        }

        this.availableNamespaces = availableNamespaces;
        String[] ownerReferenceName = ownerUsage.getFullReferenceName();
        String[] fullReferenceName = new String[referenceName.length + ownerReferenceName.length];
        System.arraycopy(ownerReferenceName, 0, fullReferenceName, 0, ownerReferenceName.length);
        System.arraycopy(referenceName, 0, fullReferenceName, ownerReferenceName.length,
                referenceName.length);
        this.fullReferenceName = fullReferenceName;
        this.referenceName = referenceName;
        this.qualifierUsage = ownerUsage;
        this.typeArguments = new LinkedList<UnresolvedReferenceTypeInfo<?>>();
    }

    @Override
    public EntityUsageInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == classInfoManager)) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        //　位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // 要素使用のオーナー要素を返す
        final UnresolvedExecutableElementInfo<?> unresolvedOwnerExecutableElement = this
                .getOwnerExecutableElement();
        final ExecutableElementInfo ownerExecutableElement = unresolvedOwnerExecutableElement
                .resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                        methodInfoManager);

        final String[] referenceName = this.getReferenceName();

        if (this.hasOwnerReference()) {

            final UnresolvedClassReferenceInfo unresolvedClassReference = this.getQualifierUsage();
            EntityUsageInfo classReference = unresolvedClassReference.resolve(usingClass,
                    usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
            assert null != classReference : "null is returned!";

            NEXT_NAME: for (int i = 0; i < referenceName.length; i++) {

                // 親が UnknownTypeInfo だったら，どうしようもない
                if (classReference.getType() instanceof UnknownTypeInfo) {

                    this.resolvedInfo = new UnknownEntityUsageInfo(ownerExecutableElement,
                            fromLine, fromColumn, toLine, toColumn);
                    return this.resolvedInfo;

                    // 親が対象クラス(TargetClassInfo)の場合
                } else if (classReference.getType() instanceof ClassTypeInfo) {

                    final ClassInfo ownerClass = ((ClassTypeInfo) classReference.getType())
                            .getReferencedClass();
                    if (ownerClass instanceof TargetClassInfo) {

                        // インナークラスから探すので一覧を取得
                        final SortedSet<TargetInnerClassInfo> innerClasses = NameResolver
                                .getAvailableDirectInnerClasses((TargetClassInfo) ((ClassTypeInfo) classReference
                                        .getType()).getReferencedClass());
                        for (final TargetInnerClassInfo innerClass : innerClasses) {

                            // 一致するクラス名が見つかった場合
                            if (referenceName[i].equals(innerClass.getClassName())) {
                                // TODO 利用関係を構築するコードが必要？

                                // TODO 型パラメータ情報を追記する処理が必要
                                final ClassTypeInfo reference = new ClassTypeInfo(innerClass);
                                classReference = new ClassReferenceInfo(ownerExecutableElement,
                                        reference, fromLine, fromColumn, toLine, toColumn);
                                continue NEXT_NAME;
                            }
                        }

                        assert false : "Here shouldn't be reached!";

                        // 親が外部クラス(ExternalClassInfo)の場合
                    } else if (ownerClass instanceof ExternalClassInfo) {

                        classReference = new UnknownEntityUsageInfo(ownerExecutableElement,
                                fromLine, fromColumn, toLine, toColumn);
                        continue NEXT_NAME;
                    }
                }

                assert false : "Here shouldn't be reached!";
            }

            this.resolvedInfo = classReference;
            return this.resolvedInfo;

        } else {

            // 未解決参照型が UnresolvedFullQualifiedNameReferenceTypeInfo ならば，完全限定名参照であると判断できる
            if (this instanceof UnresolvedFullQualifiedNameClassReferenceInfo) {

                ClassInfo classInfo = classInfoManager.getClassInfo(referenceName);
                if (null == classInfo) {
                    classInfo = new ExternalClassInfo(referenceName);
                    classInfoManager.add((ExternalClassInfo) classInfo);
                }

                // TODO 型パラメータ情報を追記する処理が必要
                final ClassTypeInfo reference = new ClassTypeInfo(classInfo);
                this.resolvedInfo = new ClassReferenceInfo(ownerExecutableElement, reference,
                        fromLine, fromColumn, toLine, toColumn);
                return this.resolvedInfo;
            }

            // 参照名が完全限定名であるとして検索
            {
                final ClassInfo classInfo = classInfoManager.getClassInfo(referenceName);
                if (null != classInfo) {

                    // TODO　型パラメータ情報を追記する処理が必要
                    final ClassTypeInfo reference = new ClassTypeInfo(classInfo);
                    this.resolvedInfo = new ClassReferenceInfo(ownerExecutableElement, reference,
                            fromLine, fromColumn, toLine, toColumn);
                    return this.resolvedInfo;
                }
            }

            // 利用可能なインナークラス名から探す
            {
                final TargetClassInfo outestClass;
                if (usingClass instanceof TargetInnerClassInfo) {
                    outestClass = NameResolver.getOuterstClass((TargetInnerClassInfo) usingClass);
                } else {
                    outestClass = usingClass;
                }

                for (final TargetInnerClassInfo innerClassInfo : NameResolver
                        .getAvailableInnerClasses(outestClass)) {

                    if (innerClassInfo.getClassName().equals(referenceName[0])) {

                        // availableField.getType() から次のword(name[i])を名前解決
                        // TODO 型パラメータ情報を格納する処理が必要
                        ClassTypeInfo reference = new ClassTypeInfo(innerClassInfo);
                        EntityUsageInfo classReference = new ClassReferenceInfo(
                                ownerExecutableElement, reference, fromLine, fromColumn, toLine,
                                toColumn);
                        NEXT_NAME: for (int i = 1; i < referenceName.length; i++) {

                            // 親が UnknownTypeInfo だったら，どうしようもない
                            if (classReference.getType() instanceof UnknownTypeInfo) {

                                this.resolvedInfo = new UnknownEntityUsageInfo(
                                        ownerExecutableElement, fromLine, fromColumn, toLine,
                                        toColumn);
                                return this.resolvedInfo;

                                // 親がクラス型の場合
                            } else if (classReference.getType() instanceof ClassTypeInfo) {

                                final ClassInfo ownerClass = ((ClassTypeInfo) classReference
                                        .getType()).getReferencedClass();

                                // 親が対象クラス(TargetClassInfo)の場合
                                if (ownerClass instanceof TargetClassInfo) {

                                    // インナークラスから探すので一覧を取得
                                    final SortedSet<TargetInnerClassInfo> innerClasses = NameResolver
                                            .getAvailableDirectInnerClasses((TargetClassInfo) ((ClassTypeInfo) classReference
                                                    .getType()).getReferencedClass());
                                    for (final TargetInnerClassInfo innerClass : innerClasses) {

                                        // 一致するクラス名が見つかった場合
                                        if (referenceName[i].equals(innerClass.getClassName())) {
                                            // TODO 利用関係を構築するコードが必要？

                                            // TODO　型パラメータ情報を格納する処理が必要
                                            reference = new ClassTypeInfo(innerClass);
                                            classReference = new ClassReferenceInfo(
                                                    ownerExecutableElement, reference, fromLine,
                                                    fromColumn, toLine, toColumn);
                                            continue NEXT_NAME;
                                        }
                                    }

                                    assert false : "Here shouldn't be reached!";

                                    // 親が外部クラス(ExternalClassInfo)の場合
                                } else if (ownerClass instanceof ExternalClassInfo) {

                                    classReference = new UnknownEntityUsageInfo(
                                            ownerExecutableElement, fromLine, fromColumn, toLine,
                                            toColumn);
                                    continue NEXT_NAME;
                                }
                            }

                            assert false : "Here shouldn't be reached!";
                        }

                        this.resolvedInfo = classReference;
                        return this.resolvedInfo;
                    }
                }
            }

            // 利用可能な名前空間から型名を探す
            {
                for (final AvailableNamespaceInfo availableNamespace : this
                        .getAvailableNamespaces()) {

                    // 名前空間名.* となっている場合
                    if (availableNamespace.isAllClasses()) {
                        final String[] namespace = availableNamespace.getNamespace();

                        // 名前空間の下にある各クラスに対して
                        for (final ClassInfo classInfo : classInfoManager.getClassInfos(namespace)) {

                            // クラス名と参照名の先頭が等しい場合は，そのクラス名が参照先であると決定する
                            final String className = classInfo.getClassName();
                            if (className.equals(referenceName[0])) {

                                // availableField.getType() から次のword(name[i])を名前解決
                                // TODO 型パラメータ情報を格納する処理が必要
                                ClassTypeInfo reference = new ClassTypeInfo(classInfo);
                                EntityUsageInfo classReference = new ClassReferenceInfo(
                                        ownerExecutableElement, reference, fromLine, fromColumn,
                                        toLine, toColumn);
                                NEXT_NAME: for (int i = 1; i < referenceName.length; i++) {

                                    // 親が UnknownTypeInfo だったら，どうしようもない
                                    if (classReference.getType() instanceof UnknownTypeInfo) {

                                        this.resolvedInfo = new UnknownEntityUsageInfo(
                                                ownerExecutableElement, fromLine, fromColumn,
                                                toLine, toColumn);
                                        return this.resolvedInfo;

                                        // 親がクラス型の場合
                                    } else if (classReference.getType() instanceof ClassTypeInfo) {

                                        final ClassInfo ownerClass = ((ClassTypeInfo) classReference
                                                .getType()).getReferencedClass();

                                        // 親が対象クラス(TargetClassInfo)の場合                                         
                                        if (ownerClass instanceof TargetClassInfo) {

                                            // インナークラスから探すので一覧を取得
                                            final SortedSet<TargetInnerClassInfo> innerClasses = NameResolver
                                                    .getAvailableDirectInnerClasses((TargetClassInfo) ((ClassTypeInfo) classReference
                                                            .getType()).getReferencedClass());
                                            for (final TargetInnerClassInfo innerClass : innerClasses) {

                                                // 一致するクラス名が見つかった場合
                                                if (referenceName[i].equals(innerClass
                                                        .getClassName())) {
                                                    // TODO 利用関係を構築するコードが必要？

                                                    // TODO 型パラメータ情報を格納する処理が必要
                                                    reference = new ClassTypeInfo(innerClass);
                                                    classReference = new ClassReferenceInfo(
                                                            ownerExecutableElement, reference,
                                                            fromLine, fromColumn, toLine, toColumn);
                                                    continue NEXT_NAME;
                                                }
                                            }

                                            // 見つからなかったので null を返す．
                                            // 現在の想定では，この部分に到着しうるのは継承関係の名前解決が完全に終わっていない段階のみのはず．
                                            assert false : "Here shouldn't be reached!";
                                            return null;

                                            // 親が外部クラス(ExternalClassInfo)の場合
                                        } else if (ownerClass instanceof ExternalClassInfo) {

                                            classReference = new UnknownEntityUsageInfo(
                                                    ownerExecutableElement, fromLine, fromColumn,
                                                    toLine, toColumn);
                                            continue NEXT_NAME;
                                        }
                                    }

                                    assert false : "Here shouldn't be reached!";
                                }

                                this.resolvedInfo = classReference;
                                return this.resolvedInfo;
                            }
                        }

                        // 名前空間.クラス名 となっている場合
                    } else {

                        final String[] importName = availableNamespace.getImportName();

                        // クラス名と参照名の先頭が等しい場合は，そのクラス名が参照先であると決定する
                        if (importName[importName.length - 1].equals(referenceName[0])) {

                            ClassInfo specifiedClassInfo = classInfoManager
                                    .getClassInfo(importName);
                            if (null == specifiedClassInfo) {
                                specifiedClassInfo = new ExternalClassInfo(importName);
                                classInfoManager.add((ExternalClassInfo) specifiedClassInfo);
                            }

                            // TODO 型パラメータ情報を格納する処理が必要
                            ClassTypeInfo reference = new ClassTypeInfo(specifiedClassInfo);
                            EntityUsageInfo classReference = new ClassReferenceInfo(
                                    ownerExecutableElement, reference, fromLine, fromColumn,
                                    toLine, toColumn);
                            NEXT_NAME: for (int i = 1; i < referenceName.length; i++) {

                                // 親が UnknownTypeInfo だったら，どうしようもない
                                if (classReference.getType() instanceof UnknownTypeInfo) {

                                    this.resolvedInfo = new UnknownEntityUsageInfo(
                                            ownerExecutableElement, fromLine, fromColumn, toLine,
                                            toColumn);
                                    return this.resolvedInfo;

                                    // 親がクラス型の場合
                                } else if (classReference.getType() instanceof ClassTypeInfo) {

                                    final ClassInfo ownerClass = ((ClassTypeInfo) classReference
                                            .getType()).getReferencedClass();

                                    // 親が対象クラス(TargetClassInfo)の場合
                                    if (ownerClass instanceof TargetClassInfo) {

                                        // インナークラス一覧を取得
                                        final SortedSet<TargetInnerClassInfo> innerClasses = NameResolver
                                                .getAvailableDirectInnerClasses((TargetClassInfo) ((ClassTypeInfo) classReference
                                                        .getType()).getReferencedClass());
                                        for (final TargetInnerClassInfo innerClass : innerClasses) {

                                            // 一致するクラス名が見つかった場合
                                            if (referenceName[i].equals(innerClass.getClassName())) {
                                                // TODO 利用関係を構築するコードが必要？

                                                // TODO 型パラメータ情報を格納する処理が必要
                                                reference = new ClassTypeInfo(innerClass);
                                                classReference = new ClassReferenceInfo(
                                                        ownerExecutableElement, reference,
                                                        fromLine, fromColumn, toLine, toColumn);
                                                continue NEXT_NAME;
                                            }
                                        }

                                        // 親が外部クラス(ExternalClassInfo)の場合
                                    } else if (ownerClass instanceof ExternalClassInfo) {

                                        classReference = new UnknownEntityUsageInfo(
                                                ownerExecutableElement, fromLine, fromColumn,
                                                toLine, toColumn);
                                        continue NEXT_NAME;
                                    }
                                }

                                assert false : "Here shouldn't be reached!";
                            }

                            this.resolvedInfo = classReference;
                            return this.resolvedInfo;
                        }
                    }
                }
            }
        }

        /*
         * if (null == usingMethod) { err.println("Remain unresolved \"" +
         * reference.getReferenceName(Settings.getLanguage().getNamespaceDelimiter()) + "\"" + " on
         * \"" + usingClass.getFullQualifiedtName(LANGUAGE.JAVA.getNamespaceDelimiter())); } else {
         * err.println("Remain unresolved \"" +
         * reference.getReferenceName(Settings.getLanguage().getNamespaceDelimiter()) + "\"" + " on
         * \"" + usingClass.getFullQualifiedtName(LANGUAGE.JAVA.getNamespaceDelimiter()) + "#" +
         * usingMethod.getMethodName() + "\"."); }
         */

        // 見つからなかった場合は，UknownTypeInfo を返す
        this.resolvedInfo = new UnknownEntityUsageInfo(ownerExecutableElement, fromLine,
                fromColumn, toLine, toColumn);
        return this.resolvedInfo;
    }

    /**
     * 型パラメータ使用を追加する
     * 
     * @param typeArgument 追加する型パラメータ使用
     */
    public final void addTypeArgument(final UnresolvedReferenceTypeInfo<?> typeArgument) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeArgument) {
            throw new NullPointerException();
        }

        this.typeArguments.add(typeArgument);
    }

    /**
     * このクラス参照で使用されている型パラメータの List を返す
     * 
     * @return このクラス参照で使用されている型パラメータの List
     */
    public final List<UnresolvedReferenceTypeInfo<?>> getTypeArguments() {
        return Collections.unmodifiableList(this.typeArguments);
    }

    /**
     * この参照型のownerも含めた参照名を返す
     * 
     * @return この参照型のownerも含めた参照名を返す
     */
    public final String[] getFullReferenceName() {
        return this.fullReferenceName;
    }

    /**
     * この参照型の参照名を返す
     * 
     * @return この参照型の参照名を返す
     */
    public final String[] getReferenceName() {
        return this.referenceName;
    }

    /**
     * この参照型がくっついている未解決参照型を返す
     * 
     * @return この参照型がくっついている未解決参照型
     */
    public final UnresolvedClassReferenceInfo getQualifierUsage() {
        return this.qualifierUsage;
    }

    /**
     * この参照型が，他の参照型にくっついているかどうかを返す
     * 
     * @return くっついている場合は true，くっついていない場合は false
     */
    public final boolean hasOwnerReference() {
        return null != this.qualifierUsage;
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
    public final List<AvailableNamespaceInfo> getAvailableNamespaces() {
        return this.availableNamespaces;
    }

    /**
     * 未解決参照型を与えると，その未解決クラス参照を返す
     * 
     * @param referenceType 未解決参照型
     * @return 未解決クラス参照
     */
    public final static UnresolvedClassReferenceInfo createClassReference(
            final UnresolvedClassTypeInfo referenceType, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        final UnresolvedClassReferenceInfo reference = new UnresolvedClassReferenceInfo(
                referenceType.getAvailableNamespaces(), referenceType.getReferenceName());
        reference.setFromLine(fromLine);
        reference.setFromColumn(fromColumn);
        reference.setToLine(toLine);
        reference.setToColumn(toColumn);

        return reference;
    }

    /**
     * 利用可能な名前空間名を保存するための変数，名前解決処理の際に用いる
     */
    private final List<AvailableNamespaceInfo> availableNamespaces;

    /**
     * 参照名を保存する変数
     */
    private final String[] referenceName;

    /**
     * ownerも含めた参照名を保存する変数
     */
    private final String[] fullReferenceName;

    /**
     * この参照がくっついている未解決参照型を保存する変数
     */
    private final UnresolvedClassReferenceInfo qualifierUsage;

    /**
     * 未解決型パラメータ使用を保存するための変数
     */
    private final List<UnresolvedReferenceTypeInfo<?>> typeArguments;

}
