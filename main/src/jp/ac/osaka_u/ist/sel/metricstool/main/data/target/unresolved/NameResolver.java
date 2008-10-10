package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetInnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalParameterInfo;


/**
 * 未解決型情報を解決するためのユーティリティクラス
 * 
 * @author higo
 * 
 */
public final class NameResolver {

    /**
     * 引数で与えられた未解決型情報を表す解決済み型情報クラスを生成する． ここで引数として与えられるのは，ソースコードがパースされていない型であるので，生成する解決済み型情報クラスは
     * ExternalClassInfo となる．
     * 
     * @param unresolvedReferenceType 未解決型情報
     * @return 解決済み型情報
     */
    public static ExternalClassInfo createExternalClassInfo(
            final UnresolvedClassReferenceInfo unresolvedReferenceType) {

        if (null == unresolvedReferenceType) {
            throw new NullPointerException();
        }

        // 未解決クラス情報の参照名を取得
        final String[] referenceName = unresolvedReferenceType.getReferenceName();

        // 利用可能な名前空間を検索し，未解決クラス情報の完全限定名を決定
        for (AvailableNamespaceInfo availableNamespace : unresolvedReferenceType
                .getAvailableNamespaces()) {

            // 名前空間名.* となっている場合は，見つけることができない
            if (availableNamespace.isAllClasses()) {
                continue;
            }

            // 名前空間.クラス名 となっている場合
            final String[] importName = availableNamespace.getImportName();

            // クラス名と参照名の先頭が等しい場合は，そのクラス名が参照先であると決定する
            if (importName[importName.length - 1].equals(referenceName[0])) {

                final String[] namespace = availableNamespace.getNamespace();
                final String[] fullQualifiedName = new String[namespace.length
                        + referenceName.length];
                System.arraycopy(namespace, 0, fullQualifiedName, 0, namespace.length);
                System.arraycopy(referenceName, 0, fullQualifiedName, namespace.length,
                        referenceName.length);

                final ExternalClassInfo classInfo = new ExternalClassInfo(fullQualifiedName);
                return classInfo;
            }
        }

        // 見つからない場合は，名前空間が UNKNOWN な 外部クラス情報を作成
        final ExternalClassInfo unknownClassInfo = new ExternalClassInfo(
                referenceName[referenceName.length - 1]);
        return unknownClassInfo;
    }

    /**
     * 引数で与えられたエンティティの List から，引数の型の List を作成し，返す
     * 
     * @param entities エンティティのList
     * @param ownerMethod 引数を宣言しているメソッド
     * @return 引数の型の List
     */
    public static List<ParameterInfo> createParameters(final List<EntityUsageInfo> entities,
            final ExternalMethodInfo ownerMethod) {

        if (null == entities || null == ownerMethod) {
            throw new NullPointerException();
        }

        final List<ParameterInfo> parameters = new LinkedList<ParameterInfo>();
        for (final EntityUsageInfo entity : entities) {
            final TypeInfo type = entity.getType();
            final ExternalParameterInfo parameter = new ExternalParameterInfo(type, ownerMethod);
            parameters.add(parameter);
        }

        return Collections.unmodifiableList(parameters);
    }

    /**
     * 引数で与えられたクラスの親クラスであり，かつ外部クラス(ExternalClassInfo)であるものを返す． クラス階層的に最も下位に位置する外部クラスを返す．
     * 該当するクラスが存在しない場合は， null を返す．
     * 
     * @param classInfo 対象クラス
     * @return 引数で与えられたクラスの親クラスであり，かつクラス階層的に最も下位に位置する外部クラス
     */
    public static ExternalClassInfo getExternalSuperClass(final TargetClassInfo classInfo) {

        if (null == classInfo) {
            throw new NullPointerException();
        }

        for (final ClassInfo superClassInfo : ClassTypeInfo.convert(classInfo.getSuperClasses())) {

            if (superClassInfo instanceof ExternalClassInfo) {
                return (ExternalClassInfo) superClassInfo;
            }

            final ExternalClassInfo superSuperClassInfo = NameResolver
                    .getExternalSuperClass((TargetClassInfo) superClassInfo);
            if (null != superSuperClassInfo) {
                return superSuperClassInfo;
            }
        }

        return null;
    }

    /**
     * 引数で与えられたクラスを内部クラスとして持つ，最も外側の（インナークラスでない）クラスを返す
     * 
     * @param innerClass インナークラス
     * @return 最も外側のクラス
     */
    public static TargetClassInfo getOuterstClass(final TargetInnerClassInfo innerClass) {

        if (null == innerClass) {
            throw new NullPointerException();
        }

        final TargetClassInfo outerClass = innerClass.getOuterClass();
        return outerClass instanceof TargetInnerClassInfo ? NameResolver
                .getOuterstClass((TargetInnerClassInfo) outerClass) : outerClass;
    }

    /**
     * 引数で与えられたクラス内の利用可能な内部クラスの SortedSet を返す
     * 
     * @param classInfo クラス
     * @return 引数で与えられたクラス内の利用可能な内部クラスの SortedSet
     */
    public static SortedSet<TargetInnerClassInfo> getAvailableInnerClasses(
            final TargetClassInfo classInfo) {

        if (null == classInfo) {
            throw new NullPointerException();
        }

        final SortedSet<TargetInnerClassInfo> innerClasses = new TreeSet<TargetInnerClassInfo>();
        for (final TargetInnerClassInfo innerClass : classInfo.getInnerClasses()) {

            innerClasses.add(innerClass);
            final SortedSet<TargetInnerClassInfo> innerClassesInInnerClass = NameResolver
                    .getAvailableInnerClasses(innerClass);
            innerClasses.addAll(innerClassesInInnerClass);
        }

        return Collections.unmodifiableSortedSet(innerClasses);
    }

    /**
     * 引数で与えられたクラスで利用可能なクラスの　List　を返す
     * 
     * @param classInfo クラス
     * @return　引数で与えられたクラスで利用可能なクラスの　List
     */
    public static List<ClassInfo> getAvailableClasses(final TargetClassInfo classInfo) {

        if (null == classInfo) {
            throw new NullPointerException();
        }

        // 利用可能な変数を代入するためのリスト
        final List<ClassInfo> availableClasses = new LinkedList<ClassInfo>();

        // 最も外側のクラスを取得
        final TargetClassInfo outestClass;
        if (classInfo instanceof TargetInnerClassInfo) {

            outestClass = NameResolver.getOuterstClass((TargetInnerClassInfo) classInfo);

            // 外部および外部クラスの親クラスを追加
            for (TargetClassInfo outerClass = classInfo; !outerClass.equals(outestClass); outerClass = ((TargetInnerClassInfo) outerClass)
                    .getOuterClass()) {

                availableClasses.add(outerClass);
                NameResolver.getAvailableSuperClasses(classInfo, outerClass, availableClasses);
            }

        } else {
            outestClass = classInfo;
        }

        //　最も外側およびもっとも外側のクラスの親クラスを追加
        availableClasses.add(outestClass);
        for(final ClassInfo superClass : ClassTypeInfo.convert(outestClass.getSuperClasses())){
            if(superClass instanceof TargetClassInfo){
                NameResolver.getAvailableSuperClasses(outestClass, (TargetClassInfo)superClass, availableClasses);
            }else if(superClass instanceof TargetClassInfo){
                availableClasses.add(superClass);
            }
        }
        NameResolver.getAvailableSuperClasses(classInfo, outestClass, availableClasses);

        // 内部クラスを追加
        for (final TargetInnerClassInfo innerClass : classInfo.getInnerClasses()) {
            NameResolver.getAvailableInnerClasses(innerClass, availableClasses);
        }

        return Collections.unmodifiableList(availableClasses);
    }

    public static void getAvailableSuperClasses(final TargetClassInfo subClass,
            final TargetClassInfo superClass, final List<ClassInfo> availableClasses) {

        if ((null == subClass) || (null == superClass) || (null == availableClasses)) {
            throw new NullPointerException();
        }

        // 既にチェックしたクラスである場合は何もせずに終了する
        if (availableClasses.contains(superClass)) {
            return;
        }

        // 自クラスを追加
        // 子クラスと親クラスの名前空間が同じ場合は，名前空間可視もしくは継承可視があればよい
        if (subClass.getNamespace().equals(superClass.getNamespace())) {

            if (superClass.isInheritanceVisible() || superClass.isNamespaceVisible()) {
                availableClasses.add(superClass);
                for(final TargetClassInfo innerClass : superClass.getInnerClasses()){
                    NameResolver.getAvailableInnerClasses(innerClass,availableClasses);
                }
            }

            //子クラスと親クラスの名前空間が違う場合は，継承可視があればよい
        } else {

            if (superClass.isInheritanceVisible()) {
                availableClasses.add(superClass);
                for(final TargetClassInfo innerClass : superClass.getInnerClasses()){
                    NameResolver.getAvailableInnerClasses(innerClass,availableClasses);
                }
            }
        }

        // 親クラスを追加
        for (final ClassInfo superSuperClass : ClassTypeInfo.convert(superClass.getSuperClasses())) {
            if (superSuperClass instanceof TargetClassInfo) {
                NameResolver.getAvailableSuperClasses(subClass, (TargetClassInfo) superSuperClass,
                        availableClasses);
            }
        }
    }

    public static void getAvailableInnerClasses(final TargetClassInfo classInfo,
            final List<ClassInfo> availableClasses) {

        if ((null == classInfo) || (null == availableClasses)) {
            throw new NullPointerException();
        }

        // 既にチェックしたクラスである場合は何もせずに終了する
        if (availableClasses.contains(classInfo)) {
            return;
        }

        availableClasses.add(classInfo);

        // 内部クラスを追加
        for (final TargetInnerClassInfo innerClass : classInfo.getInnerClasses()){
            NameResolver.getAvailableInnerClasses(innerClass, availableClasses);
        }

        return;
    }

    /**
     * 「現在のクラス」で利用可能なフィールド一覧を返す．
     * ここで，「利用可能なフィールド」とは，「現在のクラス」で定義されているフィールド，「現在のクラス」のインナークラスで定義されているフィールド，
     * 及びその親クラスで定義されているフィールドのうち子クラスからアクセスが可能なフィールドである． 利用可能なフィールドは List に格納されている．
     * リストの先頭から優先順位の高いフィールド（つまり， クラス階層において下位のクラスに定義されているフィールド）が格納されている．
     * 
     * @param currentClass 現在のクラス
     * @return 利用可能なフィールド一覧
     */
    public static List<TargetFieldInfo> getAvailableFields(final TargetClassInfo currentClass) {

        if (null == currentClass) {
            throw new NullPointerException();
        }

        // チェックしたクラスを入れるためのキャッシュ，キャッシュにあるクラスは二度目はフィールド取得しない（ループ構造対策）
        final Set<TargetClassInfo> checkedClasses = new HashSet<TargetClassInfo>();

        // 利用可能な変数を代入するためのリスト
        final List<TargetFieldInfo> availableFields = new LinkedList<TargetFieldInfo>();

        // 最も外側のクラスを取得
        final TargetClassInfo outestClass;
        if (currentClass instanceof TargetInnerClassInfo) {
            outestClass = NameResolver.getOuterstClass((TargetInnerClassInfo) currentClass);

            for (TargetClassInfo outerClass = currentClass; !outerClass.equals(outestClass); outerClass = ((TargetInnerClassInfo) outerClass)
                    .getOuterClass()) {

                // 自クラスおよび，外部クラスで定義されたメソッドを追加
                availableFields.addAll(outerClass.getDefinedFields());
                checkedClasses.add(outerClass);
            }

            // 内部クラスで定義されたフィールドを追加
            for (final TargetInnerClassInfo innerClass : currentClass.getInnerClasses()) {
                final List<TargetFieldInfo> availableFieldsDefinedInInnerClasses = NameResolver
                        .getAvailableFieldsDefinedInInnerClasses(innerClass, checkedClasses);
                availableFields.addAll(availableFieldsDefinedInInnerClasses);
            }

            // 親クラスで定義されたフィールドを追加
            for (final ClassInfo superClass : ClassTypeInfo.convert(currentClass.getSuperClasses())) {
                if (superClass instanceof TargetClassInfo) {
                    final List<TargetFieldInfo> availableFieldsDefinedInSuperClasses = NameResolver
                            .getAvailableFieldsDefinedInSuperClasses(currentClass,
                                    (TargetClassInfo) superClass, checkedClasses);
                    availableFields.addAll(availableFieldsDefinedInSuperClasses);
                }
            }

        } else {
            outestClass = currentClass;
        }

        // 最も外側のクラスで定義されたフィールドを追加
        availableFields.addAll(outestClass.getDefinedFields());
        checkedClasses.add(outestClass);

        // 内部クラスで定義されたフィールドを追加
        for (final TargetInnerClassInfo innerClass : outestClass.getInnerClasses()) {
            final List<TargetFieldInfo> availableFieldsDefinedInInnerClasses = NameResolver
                    .getAvailableFieldsDefinedInInnerClasses(innerClass, checkedClasses);
            availableFields.addAll(availableFieldsDefinedInInnerClasses);
        }

        // 親クラスで定義されたフィールドを追加
        for (final ClassInfo superClass : ClassTypeInfo.convert(outestClass.getSuperClasses())) {
            if (superClass instanceof TargetClassInfo) {
                final List<TargetFieldInfo> availableFieldsDefinedInSuperClasses = NameResolver
                        .getAvailableFieldsDefinedInSuperClasses(outestClass,
                                (TargetClassInfo) superClass, checkedClasses);
                availableFields.addAll(availableFieldsDefinedInSuperClasses);
            }
        }

        return Collections.unmodifiableList(availableFields);
    }

    /**
     * 引数で与えられたクラスとその内部クラスで定義されたフィールドのうち，外側のクラスで利用可能なフィールドの List を返す
     * 
     * @param classInfo クラス
     * @param checkedClasses 既にチェックしたクラスのキャッシュ
     * @return 外側のクラスで利用可能なフィールドの List
     */
    public static List<TargetFieldInfo> getAvailableFieldsDefinedInInnerClasses(
            final TargetInnerClassInfo classInfo, final Set<TargetClassInfo> checkedClasses) {

        if ((null == classInfo) || (null == checkedClasses)) {
            throw new NullPointerException();
        }

        // 既にチェックしたクラスである場合は何もせずに終了する
        if (checkedClasses.contains(classInfo)) {
            return new LinkedList<TargetFieldInfo>();
        }

        final List<TargetFieldInfo> availableFields = new LinkedList<TargetFieldInfo>();

        // 自クラスで定義されており，名前空間可視性を持つフィールドを追加
        // for (final TargetFieldInfo definedField : classInfo.getDefinedFields()) {
        // if (definedField.isNamespaceVisible()) {
        // availableFields.add(definedField);
        // }
        // }
        availableFields.addAll(classInfo.getDefinedFields());
        checkedClasses.add(classInfo);

        // 内部クラスで定義されたフィールドを追加
        for (final TargetInnerClassInfo innerClass : classInfo.getInnerClasses()) {
            final List<TargetFieldInfo> availableFieldsDefinedInInnerClasses = NameResolver
                    .getAvailableFieldsDefinedInInnerClasses(innerClass, checkedClasses);
            availableFields.addAll(availableFieldsDefinedInInnerClasses);
        }

        // 親クラスで定義されたフィールドを追加
        for (final ClassInfo superClass : ClassTypeInfo.convert(classInfo.getSuperClasses())) {
            if (superClass instanceof TargetClassInfo) {
                final List<TargetFieldInfo> availableFieldsDefinedInSuperClasses = NameResolver
                        .getAvailableFieldsDefinedInSuperClasses(classInfo,
                                (TargetClassInfo) superClass, checkedClasses);
                availableFields.addAll(availableFieldsDefinedInSuperClasses);
            }
        }

        return Collections.unmodifiableList(availableFields);
    }

    /**
     * 引数で与えられたクラスとその親クラスで定義されたフィールドのうち，子クラスで利用可能なフィールドの List を返す
     * 
     * @param classInfo クラス
     * @param checkedClasses 既にチェックしたクラスのキャッシュ
     * @return 子クラスで利用可能なフィールドの List
     */
    private static List<TargetFieldInfo> getAvailableFieldsDefinedInSuperClasses(
            final TargetClassInfo subClass, final TargetClassInfo superClass,
            final Set<TargetClassInfo> checkedClasses) {

        if ((null == subClass) || (null == superClass) || (null == checkedClasses)) {
            throw new NullPointerException();
        }

        // 既にチェックしたクラスである場合は何もせずに終了する
        if (checkedClasses.contains(superClass)) {
            return new LinkedList<TargetFieldInfo>();
        }

        final List<TargetFieldInfo> availableFields = new LinkedList<TargetFieldInfo>();

        // 自クラスで定義されており，クラス階層可視性を持つフィールドを追加
        for (final TargetFieldInfo definedField : superClass.getDefinedFields()) {

            // 子クラスと親クラスの名前空間が同じ場合は，名前空間可視もしくは継承可視があればよい
            if (subClass.getNamespace().equals(superClass.getNamespace())) {

                if (definedField.isInheritanceVisible() || definedField.isNamespaceVisible()) {
                    availableFields.add(definedField);
                }

                //子クラスと親クラスの名前空間が違う場合は，継承可視があればよい
            } else {
                if (definedField.isInheritanceVisible()) {
                    availableFields.add(definedField);
                }
            }
        }
        checkedClasses.add(superClass);

        // 内部クラスで定義されたフィールドを追加
        for (final TargetInnerClassInfo innerClass : superClass.getInnerClasses()) {
            final List<TargetFieldInfo> availableFieldsDefinedInInnerClasses = NameResolver
                    .getAvailableFieldsDefinedInInnerClasses(innerClass, checkedClasses);
            for (final TargetFieldInfo field : availableFieldsDefinedInInnerClasses) {

                // 子クラスと親クラスの名前空間が同じ場合は，名前空間可視もしくは継承可視があればよい
                if (subClass.getNamespace().equals(superClass.getNamespace())) {

                    if (field.isInheritanceVisible() || field.isNamespaceVisible()) {
                        availableFields.add(field);
                    }

                } else {

                    //子クラスと親クラスの名前空間が違う場合は，継承可視があればよい
                    if (field.isInheritanceVisible()) {
                        availableFields.add(field);
                    }
                }
            }
        }

        // 親クラスで定義されたフィールドを追加
        for (final ClassInfo superSuperClass : ClassTypeInfo.convert(superClass.getSuperClasses())) {
            if (superSuperClass instanceof TargetClassInfo) {
                final List<TargetFieldInfo> availableFieldsDefinedInSuperClasses = NameResolver
                        .getAvailableFieldsDefinedInSuperClasses(subClass,
                                (TargetClassInfo) superSuperClass, checkedClasses);
                availableFields.addAll(availableFieldsDefinedInSuperClasses);
            }
        }

        return Collections.unmodifiableList(availableFields);
    }

    /**
     * 「現在のクラス」で利用可能なメソッド一覧を返す．
     * ここで，「利用可能なメソッド」とは，「現在のクラス」で定義されているメソッド，及びその親クラスで定義されているメソッドのうち子クラスからアクセスが可能なメソッドである．
     * 利用可能なメソッドは List に格納されている． リストの先頭から優先順位の高いメソッド（つまり，クラス階層において下位のクラスに定義されているメソッド）が格納されている．
     * 
     * @param thisClass 現在のクラス
     * @return 利用可能なメソッド一覧
     */
    private static List<TargetMethodInfo> getAvailableMethods(final TargetClassInfo currentClass) {

        if (null == currentClass) {
            throw new NullPointerException();
        }

        // チェックしたクラスを入れるためのキャッシュ，キャッシュにあるクラスは二度目はフィールド取得しない（ループ構造対策）
        final Set<TargetClassInfo> checkedClasses = new HashSet<TargetClassInfo>();

        // 利用可能な変数を代入するためのリスト
        final List<TargetMethodInfo> availableMethods = new LinkedList<TargetMethodInfo>();

        // 最も外側のクラスを取得
        final TargetClassInfo outestClass;
        if (currentClass instanceof TargetInnerClassInfo) {
            outestClass = NameResolver.getOuterstClass((TargetInnerClassInfo) currentClass);

            // 自クラスで定義されたメソッドを追加
            availableMethods.addAll(currentClass.getDefinedMethods());
            checkedClasses.add(currentClass);

            // 内部クラスで定義されたメソッドを追加
            for (final TargetInnerClassInfo innerClass : currentClass.getInnerClasses()) {
                final List<TargetMethodInfo> availableMethodsDefinedInInnerClasses = NameResolver
                        .getAvailableMethodsDefinedInInnerClasses(innerClass, checkedClasses);
                availableMethods.addAll(availableMethodsDefinedInInnerClasses);
            }

            // 親クラスで定義されたメソッドを追加
            for (final ClassInfo superClass : ClassTypeInfo.convert(currentClass.getSuperClasses())) {
                if (superClass instanceof TargetClassInfo) {
                    final List<TargetMethodInfo> availableMethodsDefinedInSuperClasses = NameResolver
                            .getAvailableMethodsDefinedInSuperClasses(outestClass,
                                    (TargetClassInfo) superClass, checkedClasses);
                    availableMethods.addAll(availableMethodsDefinedInSuperClasses);
                }
            }

        } else {
            outestClass = currentClass;
        }

        // 最も外側のクラスで定義されたメソッドを追加
        availableMethods.addAll(outestClass.getDefinedMethods());
        checkedClasses.add(outestClass);

        // 内部クラスで定義されたメソッドを追加
        for (final TargetInnerClassInfo innerClass : outestClass.getInnerClasses()) {
            final List<TargetMethodInfo> availableMethodsDefinedInInnerClasses = NameResolver
                    .getAvailableMethodsDefinedInInnerClasses(innerClass, checkedClasses);
            availableMethods.addAll(availableMethodsDefinedInInnerClasses);
        }

        // 親クラスで定義されたメソッドを追加
        for (final ClassInfo superClass : ClassTypeInfo.convert(outestClass.getSuperClasses())) {
            if (superClass instanceof TargetClassInfo) {
                final List<TargetMethodInfo> availableMethodsDefinedInSuperClasses = NameResolver
                        .getAvailableMethodsDefinedInSuperClasses(outestClass,
                                (TargetClassInfo) superClass, checkedClasses);
                availableMethods.addAll(availableMethodsDefinedInSuperClasses);
            }
        }

        return Collections.unmodifiableList(availableMethods);
    }

    /**
     * 引数で与えられたクラスとその内部クラスで定義されたメソッドのうち，外側のクラスで利用可能なメソッドの List を返す
     * 
     * @param classInfo クラス
     * @param checkedClasses 既にチェックしたクラスのキャッシュ
     * @return 外側のクラスで利用可能なメソッドの List
     */
    private static List<TargetMethodInfo> getAvailableMethodsDefinedInInnerClasses(
            final TargetInnerClassInfo classInfo, final Set<TargetClassInfo> checkedClasses) {

        if ((null == classInfo) || (null == checkedClasses)) {
            throw new NullPointerException();
        }

        // 既にチェックしたクラスである場合は何もせずに終了する
        if (checkedClasses.contains(classInfo)) {
            return new LinkedList<TargetMethodInfo>();
        }

        final List<TargetMethodInfo> availableMethods = new LinkedList<TargetMethodInfo>();

        // 自クラスで定義されており，名前空間可視性を持つメソッドを追加
        // for (final TargetFieldInfo definedField : classInfo.getDefinedFields()) {
        // if (definedField.isNamespaceVisible()) {
        // availableFields.add(definedField);
        // }
        // }
        availableMethods.addAll(classInfo.getDefinedMethods());
        checkedClasses.add(classInfo);

        // 内部クラスで定義されたメソッドを追加
        for (final TargetInnerClassInfo innerClass : classInfo.getInnerClasses()) {
            final List<TargetMethodInfo> availableMethodsDefinedInInnerClasses = NameResolver
                    .getAvailableMethodsDefinedInInnerClasses(innerClass, checkedClasses);
            availableMethods.addAll(availableMethodsDefinedInInnerClasses);
        }

        // 親クラスで定義されたメソッドを追加
        for (final ClassInfo superClass : ClassTypeInfo.convert(classInfo.getSuperClasses())) {
            if (superClass instanceof TargetClassInfo) {
                final List<TargetMethodInfo> availableMethodsDefinedInSuperClasses = NameResolver
                        .getAvailableMethodsDefinedInSuperClasses(classInfo,
                                (TargetClassInfo) superClass, checkedClasses);
                availableMethods.addAll(availableMethodsDefinedInSuperClasses);
            }
        }

        return Collections.unmodifiableList(availableMethods);
    }

    /**
     * 引数で与えられたクラスとその親クラスで定義されたメソッドのうち，子クラスで利用可能なメソッドの List を返す
     * 
     * @param classInfo クラス
     * @param checkedClasses 既にチェックしたクラスのキャッシュ
     * @return 子クラスで利用可能なメソッドの List
     */
    private static List<TargetMethodInfo> getAvailableMethodsDefinedInSuperClasses(
            final TargetClassInfo subClass, final TargetClassInfo superClass,
            final Set<TargetClassInfo> checkedClasses) {

        if ((null == subClass) || (null == superClass) || (null == checkedClasses)) {
            throw new NullPointerException();
        }

        // 既にチェックしたクラスである場合は何もせずに終了する
        if (checkedClasses.contains(superClass)) {
            return new LinkedList<TargetMethodInfo>();
        }

        final List<TargetMethodInfo> availableMethods = new LinkedList<TargetMethodInfo>();

        // 自クラスで定義されており，クラス階層可視性を持つメソッドを追加
        for (final TargetMethodInfo definedMethod : superClass.getDefinedMethods()) {

            // 子クラスと親クラスの名前空間が同じ場合は，名前空間可視もしくは継承可視があればよい
            if (subClass.getNamespace().equals(superClass.getNamespace())) {

                if (definedMethod.isInheritanceVisible() || definedMethod.isNamespaceVisible()) {
                    availableMethods.add(definedMethod);
                }

                // 子クラスと親クラスの名前空間が異なる場合は，継承可視があればよい
            } else {

                if (definedMethod.isInheritanceVisible()) {
                    availableMethods.add(definedMethod);
                }
            }
        }
        checkedClasses.add(superClass);

        // 内部クラスで定義されたメソッドを追加
        for (final TargetInnerClassInfo innerClass : superClass.getInnerClasses()) {
            final List<TargetMethodInfo> availableMethodsDefinedInInnerClasses = NameResolver
                    .getAvailableMethodsDefinedInInnerClasses(innerClass, checkedClasses);
            for (final TargetMethodInfo method : availableMethodsDefinedInInnerClasses) {

                // 子クラスと親クラスの名前空間が同じ場合は，名前空間可視もしくは継承可視があればよい
                if (subClass.getNamespace().equals(superClass.getNamespace())) {

                    if (method.isInheritanceVisible() || method.isNamespaceVisible()) {
                        availableMethods.add(method);
                    }

                    // 子クラスと親クラスの名前空間が異なる場合は，継承可視があればよい
                } else {

                    if (method.isInheritanceVisible()) {
                        availableMethods.add(method);
                    }
                }
            }
        }

        // 親クラスで定義されたメソッドを追加
        for (final ClassInfo superSuperClass : ClassTypeInfo.convert(superClass.getSuperClasses())) {
            if (superSuperClass instanceof TargetClassInfo) {
                final List<TargetMethodInfo> availableMethodsDefinedInSuperClasses = NameResolver
                        .getAvailableMethodsDefinedInSuperClasses(subClass,
                                (TargetClassInfo) superSuperClass, checkedClasses);
                availableMethods.addAll(availableMethodsDefinedInSuperClasses);
            }
        }

        return Collections.unmodifiableList(availableMethods);
    }

    /**
     * 「使用されるクラス」が「使用するクラス」において使用される場合に，利用可能なフィールド一覧を返す．
     * ここで，「利用可能なフィールド」とは，「使用されるクラス」で定義されているフィールド，及びその親クラスで定義されているフィールドのうち子クラスからアクセスが可能なフィールドである．
     * また，「使用されるクラス」と「使用するクラス」の名前空間を比較し，より正確に利用可能なフィールドを取得する． 子クラスで利用可能なフィールド一覧は List に格納されている．
     * リストの先頭から優先順位の高いフィールド（つまり，クラス階層において下位のクラスに定義されているフィールド）が格納されている．
     * 
     * @param usedClass 使用されるクラス
     * @param usingClass 使用するクラス
     * @return 利用可能なフィールド一覧
     */
    public static List<TargetFieldInfo> getAvailableFields(final TargetClassInfo usedClass,
            final TargetClassInfo usingClass) {

        if ((null == usedClass) || (null == usingClass)) {
            throw new NullPointerException();
        }

        // 使用されるクラスの最も外側のクラスを取得
        final TargetClassInfo usedOutestClass;
        if (usedClass instanceof TargetInnerClassInfo) {
            usedOutestClass = NameResolver.getOuterstClass((TargetInnerClassInfo) usedClass);
        } else {
            usedOutestClass = usedClass;
        }

        // 使用するクラスの最も外側のクラスを取得
        final TargetClassInfo usingOutestClass;
        if (usingClass instanceof TargetInnerClassInfo) {
            usingOutestClass = NameResolver.getOuterstClass((TargetInnerClassInfo) usingClass);
        } else {
            usingOutestClass = usingClass;
        }

        // このクラスで定義されているフィールドのうち，使用するクラスで利用可能なフィールドを取得する
        // 2つのクラスが同じ場合，全てのフィールドが利用可能
        if (usedOutestClass.equals(usingOutestClass)) {

            return NameResolver.getAvailableFields(usedClass);

            // 2つのクラスが同じ名前空間を持っている場合
        } else if (usedOutestClass.getNamespace().equals(usingOutestClass.getNamespace())) {

            final List<TargetFieldInfo> availableFields = new LinkedList<TargetFieldInfo>();

            // 名前空間可視性を持ったフィールドのみが利用可能
            for (final TargetFieldInfo field : NameResolver.getAvailableFields(usedClass)) {
                if (field.isNamespaceVisible()) {
                    availableFields.add(field);
                }
            }

            return Collections.unmodifiableList(availableFields);

            // 違う名前空間を持っている場合
        } else {

            final List<TargetFieldInfo> availableFields = new LinkedList<TargetFieldInfo>();

            // 全可視性を持つフィールドのみが利用可能
            for (final TargetFieldInfo field : NameResolver.getAvailableFields(usedClass)) {
                if (field.isPublicVisible()) {
                    availableFields.add(field);
                }
            }

            return Collections.unmodifiableList(availableFields);
        }
    }

    /**
     * 「使用されるクラス」が「使用するクラス」において使用される場合に，利用可能なメソッド一覧を返す．
     * ここで，「利用可能なメソッド」とは，「使用されるクラス」で定義されているメソッド，及びその親クラスで定義されているメソッドのうち子クラスからアクセスが可能なメソッドである．
     * また，「使用されるクラス」と「使用するクラス」の名前空間を比較し，より正確に利用可能なメソッドを取得する． 子クラスで利用可能なメソッド一覧は List に格納されている．
     * リストの先頭から優先順位の高いメソッド（つまり，クラス階層において下位のクラスに定義されているメソッド）が格納されている．
     * 
     * @param usedClass 使用されるクラス
     * @param usingClass 使用するクラス
     * @return 利用可能なメソッド一覧
     */
    public static List<TargetMethodInfo> getAvailableMethods(final TargetClassInfo usedClass,
            final TargetClassInfo usingClass) {

        if ((null == usedClass) || (null == usingClass)) {
            throw new NullPointerException();
        }

        // 使用されるクラスの最も外側のクラスを取得
        final TargetClassInfo usedOutestClass;
        if (usedClass instanceof TargetInnerClassInfo) {
            usedOutestClass = NameResolver.getOuterstClass((TargetInnerClassInfo) usedClass);
        } else {
            usedOutestClass = usedClass;
        }

        // 使用するクラスの最も外側のクラスを取得
        final TargetClassInfo usingOutestClass;
        if (usingClass instanceof TargetInnerClassInfo) {
            usingOutestClass = NameResolver.getOuterstClass((TargetInnerClassInfo) usingClass);
        } else {
            usingOutestClass = usingClass;
        }

        // このクラスで定義されているメソッドのうち，使用するクラスで利用可能なメソッドを取得する
        // 2つのクラスが同じ場合，全てのメソッドが利用可能
        if (usedOutestClass.equals(usingOutestClass)) {

            return NameResolver.getAvailableMethods(usedClass);

            // 2つのクラスが同じ名前空間を持っている場合
        } else if (usedOutestClass.getNamespace().equals(usingOutestClass.getNamespace())) {

            final List<TargetMethodInfo> availableMethods = new LinkedList<TargetMethodInfo>();

            // 名前空間可視性を持ったメソッドのみが利用可能
            for (final TargetMethodInfo method : NameResolver.getAvailableMethods(usedClass)) {
                if (method.isNamespaceVisible()) {
                    availableMethods.add(method);
                }
            }

            return Collections.unmodifiableList(availableMethods);

            // 違う名前空間を持っている場合
        } else {

            final List<TargetMethodInfo> availableMethods = new LinkedList<TargetMethodInfo>();

            // 全可視性を持つメソッドのみが利用可能
            for (final TargetMethodInfo method : NameResolver.getAvailableMethods(usedClass)) {
                if (method.isPublicVisible()) {
                    availableMethods.add(method);
                }
            }

            return Collections.unmodifiableList(availableMethods);
        }
    }

    /**
     * 引数で与えられたクラスの直接のインナークラスを返す．親クラスで定義されたインナークラスも含まれる．
     * 
     * @param classInfo クラス
     * @return 引数で与えられたクラスの直接のインナークラス，親クラスで定義されたインナークラスも含まれる．
     */
    public static final SortedSet<TargetInnerClassInfo> getAvailableDirectInnerClasses(
            final TargetClassInfo classInfo) {

        if (null == classInfo) {
            throw new NullPointerException();
        }

        final SortedSet<TargetInnerClassInfo> availableDirectInnerClasses = new TreeSet<TargetInnerClassInfo>();

        // 引数で与えられたクラスの直接のインナークラスを追加
        availableDirectInnerClasses.addAll(classInfo.getInnerClasses());

        // 親クラスに対して再帰的に処理
        for (final ClassInfo superClassInfo : ClassTypeInfo.convert(classInfo.getSuperClasses())) {

            if (superClassInfo instanceof TargetClassInfo) {
                final SortedSet<TargetInnerClassInfo> availableDirectInnerClassesInSuperClass = NameResolver
                        .getAvailableDirectInnerClasses((TargetClassInfo) superClassInfo);
                availableDirectInnerClasses.addAll(availableDirectInnerClassesInSuperClass);
            }
        }

        return Collections.unmodifiableSortedSet(availableDirectInnerClasses);
    }

}
