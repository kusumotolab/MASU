package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.AnonymousClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.InnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetInnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterizable;


/**
 * 未解決型情報を解決するためのユーティリティクラス
 * 
 * @author higo
 * 
 */
public final class NameResolver {

    /**
     * 引数で与えられたクラスの親クラスであり，かつ外部クラス(ExternalClassInfo)であるものを返す． クラス階層的に最も下位に位置する外部クラスを返す．
     * 該当するクラスが存在しない場合は， null を返す．
     * 
     * @param classInfo 対象クラス
     * @return 引数で与えられたクラスの親クラスであり，かつクラス階層的に最も下位に位置する外部クラス
     */
    public static ExternalClassInfo getExternalSuperClass(final ClassInfo classInfo) {

        if (null == classInfo) {
            throw new IllegalArgumentException();
        }

        for (final ClassInfo superClassInfo : ClassTypeInfo.convert(classInfo.getSuperClasses())) {

            if (superClassInfo instanceof ExternalClassInfo) {
                return (ExternalClassInfo) superClassInfo;
            }

            final ExternalClassInfo superSuperClassInfo = NameResolver
                    .getExternalSuperClass(superClassInfo);
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
    public static ClassInfo getOuterstClass(final InnerClassInfo innerClass) {

        if (null == innerClass) {
            throw new IllegalArgumentException();
        }

        final ClassInfo outerClass = innerClass.getOuterClass();
        return outerClass instanceof InnerClassInfo ? NameResolver
                .getOuterstClass((InnerClassInfo) outerClass) : outerClass;
    }

    /**
     * 引数で与えられたクラス内の利用可能な内部クラスの SortedSet を返す
     * 
     * @param classInfo クラス
     * @return 引数で与えられたクラス内の利用可能な内部クラスの SortedSet
     */
    public static SortedSet<InnerClassInfo> getAvailableInnerClasses(final ClassInfo classInfo) {

        if (null == classInfo) {
            throw new NullPointerException();
        }

        final SortedSet<InnerClassInfo> innerClasses = new TreeSet<InnerClassInfo>();
        for (final InnerClassInfo innerClass : classInfo.getInnerClasses()) {

            innerClasses.add(innerClass);
            final SortedSet<InnerClassInfo> innerClassesInInnerClass = NameResolver
                    .getAvailableInnerClasses((ClassInfo) innerClass);
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
    public static List<ClassInfo> getAvailableClasses(final ClassInfo classInfo) {

        if (null == classInfo) {
            throw new IllegalArgumentException();
        }

        // 利用可能な変数を代入するためのリスト
        final List<ClassInfo> availableClasses = new LinkedList<ClassInfo>();

        // 最も外側のクラスを取得
        final ClassInfo outestClass;
        if (classInfo instanceof InnerClassInfo) {

            outestClass = NameResolver.getOuterstClass((InnerClassInfo) classInfo);

            // 外部および外部クラスの親クラスを追加
            for (ClassInfo outerClass = classInfo; !outerClass.equals(outestClass); outerClass = ((InnerClassInfo) outerClass)
                    .getOuterClass()) {

                availableClasses.add(outerClass);
                NameResolver.getAvailableSuperClasses(classInfo, outerClass, availableClasses);
            }

        } else {
            outestClass = classInfo;
        }

        //　最も外側およびもっとも外側のクラスの親クラスを追加
        availableClasses.add(outestClass);
        for (final ClassInfo superClass : ClassTypeInfo.convert(outestClass.getSuperClasses())) {
            NameResolver.getAvailableSuperClasses(outestClass, superClass, availableClasses);
            availableClasses.add(superClass);
        }
        NameResolver.getAvailableSuperClasses(classInfo, outestClass, availableClasses);

        // 内部クラスを追加
        for (final InnerClassInfo innerClass : classInfo.getInnerClasses()) {
            NameResolver.getAvailableInnerClasses((ClassInfo) innerClass, availableClasses);
        }

        return Collections.unmodifiableList(availableClasses);
    }

    public static void getAvailableSuperClasses(final ClassInfo subClass,
            final ClassInfo superClass, final List<ClassInfo> availableClasses) {

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
                for (final InnerClassInfo innerClass : superClass.getInnerClasses()) {
                    NameResolver.getAvailableInnerClasses((ClassInfo) innerClass, availableClasses);
                }
            }

            //子クラスと親クラスの名前空間が違う場合は，継承可視があればよい
        } else {

            if (superClass.isInheritanceVisible()) {
                availableClasses.add(superClass);
                for (final InnerClassInfo innerClass : superClass.getInnerClasses()) {
                    NameResolver.getAvailableInnerClasses((ClassInfo) innerClass, availableClasses);
                }
            }
        }

        // 親クラスを追加
        for (final ClassInfo superSuperClass : ClassTypeInfo.convert(superClass.getSuperClasses())) {
            NameResolver.getAvailableSuperClasses(subClass, superSuperClass, availableClasses);
        }
    }

    public static void getAvailableInnerClasses(final ClassInfo classInfo,
            final List<ClassInfo> availableClasses) {

        if ((null == classInfo) || (null == availableClasses)) {
            throw new NullPointerException();
        }

        // 既にチェックしたクラスである場合は何もせずに終了する
        if (availableClasses.contains(classInfo)) {
            return;
        }

        // 無名インナークラスの場合は追加せずに終了する
        if (classInfo instanceof AnonymousClassInfo) {
            return;
        }

        availableClasses.add(classInfo);

        // 内部クラスを追加
        for (final InnerClassInfo innerClass : classInfo.getInnerClasses()) {
            NameResolver.getAvailableInnerClasses((ClassInfo) innerClass, availableClasses);
        }

        return;
    }

    /**
     * 「現在のクラス」で利用可能なフィールド一覧を返す．
     * ここで，「利用可能なフィールド」とは，「現在のクラス」で定義されているフィールド，「現在のクラス」のインナークラスで定義されているフィールド，
     * 及びその親クラスで定義されているフィールドのうち子クラスからアクセスが可能なフィールドである． 利用可能なフィールドは List に格納されている．
     * リストの先頭から優先順位の高いフィールド（つまり， クラス階層において下位のクラスに定義されているフィールド）が格納されている．
     * 
     * TODO スタティックインポートからを追加しないといけない
     * 
     * @param currentClass 現在のクラス
     * @return 利用可能なフィールド一覧
     */
    public static List<FieldInfo> getAvailableFields(final ClassInfo currentClass) {

        if (null == currentClass) {
            throw new IllegalArgumentException();
        }

        // チェックしたクラスを入れるためのキャッシュ，キャッシュにあるクラスは二度目はフィールド取得しない（ループ構造対策）
        final Set<ClassInfo> checkedClasses = new HashSet<ClassInfo>();

        // 利用可能な変数を代入するためのリスト
        final List<FieldInfo> availableFields = new LinkedList<FieldInfo>();

        // 最も外側のクラスを取得
        final ClassInfo outestClass;
        if (currentClass instanceof InnerClassInfo) {

            outestClass = NameResolver.getOuterstClass((InnerClassInfo) currentClass);

            for (ClassInfo outerClass = currentClass; !outerClass.equals(outestClass); outerClass = ((InnerClassInfo) outerClass)
                    .getOuterClass()) {

                // 自クラスおよび，外部クラスで定義されたメソッドを追加
                availableFields.addAll(outerClass.getDefinedFields());
                checkedClasses.add(outerClass);
            }

            // 内部クラスで定義されたフィールドを追加
            for (final InnerClassInfo innerClass : currentClass.getInnerClasses()) {
                final List<FieldInfo> availableFieldsDefinedInInnerClasses = NameResolver
                        .getAvailableFieldsDefinedInInnerClasses((ClassInfo) innerClass,
                                checkedClasses);
                availableFields.addAll(availableFieldsDefinedInInnerClasses);
            }

            // 親クラスで定義されたフィールドを追加
            for (final ClassInfo superClass : ClassTypeInfo.convert(currentClass.getSuperClasses())) {
                final List<FieldInfo> availableFieldsDefinedInSuperClasses = NameResolver
                        .getAvailableFieldsDefinedInSuperClasses(currentClass, superClass,
                                checkedClasses);
                availableFields.addAll(availableFieldsDefinedInSuperClasses);
            }

        } else {
            outestClass = currentClass;
        }

        // 最も外側のクラスで定義されたフィールドを追加
        availableFields.addAll(outestClass.getDefinedFields());
        checkedClasses.add(outestClass);

        // 内部クラスで定義されたフィールドを追加
        for (final InnerClassInfo innerClass : outestClass.getInnerClasses()) {
            final List<FieldInfo> availableFieldsDefinedInInnerClasses = NameResolver
                    .getAvailableFieldsDefinedInInnerClasses((ClassInfo) innerClass, checkedClasses);
            availableFields.addAll(availableFieldsDefinedInInnerClasses);
        }

        // 親クラスで定義されたフィールドを追加
        for (final ClassInfo superClass : ClassTypeInfo.convert(outestClass.getSuperClasses())) {
            final List<FieldInfo> availableFieldsDefinedInSuperClasses = NameResolver
                    .getAvailableFieldsDefinedInSuperClasses(outestClass, superClass,
                            checkedClasses);
            availableFields.addAll(availableFieldsDefinedInSuperClasses);
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
    public static List<FieldInfo> getAvailableFieldsDefinedInInnerClasses(
            final ClassInfo classInfo, final Set<ClassInfo> checkedClasses) {

        if ((null == classInfo) || (null == checkedClasses)) {
            throw new NullPointerException();
        }

        // 既にチェックしたクラスである場合は何もせずに終了する
        if (checkedClasses.contains(classInfo)) {
            return new LinkedList<FieldInfo>();
        }

        // 無名クラスであれば何もせずに終了する
        if (classInfo instanceof AnonymousClassInfo) {
            return new LinkedList<FieldInfo>();
        }

        final List<FieldInfo> availableFields = new LinkedList<FieldInfo>();

        // 自クラスで定義されており，名前空間可視性を持つフィールドを追加
        // for (final TargetFieldInfo definedField : classInfo.getDefinedFields()) {
        // if (definedField.isNamespaceVisible()) {
        // availableFields.add(definedField);
        // }
        // }
        availableFields.addAll(classInfo.getDefinedFields());
        checkedClasses.add(classInfo);

        // 内部クラスで定義されたフィールドを追加
        for (final InnerClassInfo innerClass : classInfo.getInnerClasses()) {
            final List<FieldInfo> availableFieldsDefinedInInnerClasses = NameResolver
                    .getAvailableFieldsDefinedInInnerClasses((ClassInfo) innerClass, checkedClasses);
            availableFields.addAll(availableFieldsDefinedInInnerClasses);
        }

        // 親クラスで定義されたフィールドを追加
        for (final ClassInfo superClass : ClassTypeInfo.convert(classInfo.getSuperClasses())) {
            final List<FieldInfo> availableFieldsDefinedInSuperClasses = NameResolver
                    .getAvailableFieldsDefinedInSuperClasses(classInfo, superClass, checkedClasses);
            availableFields.addAll(availableFieldsDefinedInSuperClasses);
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
    private static List<FieldInfo> getAvailableFieldsDefinedInSuperClasses(
            final ClassInfo subClass, final ClassInfo superClass,
            final Set<ClassInfo> checkedClasses) {

        if ((null == subClass) || (null == superClass) || (null == checkedClasses)) {
            throw new NullPointerException();
        }

        // 既にチェックしたクラスである場合は何もせずに終了する
        if (checkedClasses.contains(superClass)) {
            return new LinkedList<FieldInfo>();
        }

        final List<FieldInfo> availableFields = new LinkedList<FieldInfo>();

        // 自クラスで定義されており，クラス階層可視性を持つフィールドを追加
        for (final FieldInfo definedField : superClass.getDefinedFields()) {

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
        for (final InnerClassInfo innerClass : superClass.getInnerClasses()) {
            final List<FieldInfo> availableFieldsDefinedInInnerClasses = NameResolver
                    .getAvailableFieldsDefinedInInnerClasses((ClassInfo) innerClass, checkedClasses);
            for (final FieldInfo field : availableFieldsDefinedInInnerClasses) {

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
            final List<FieldInfo> availableFieldsDefinedInSuperClasses = NameResolver
                    .getAvailableFieldsDefinedInSuperClasses(subClass, superSuperClass,
                            checkedClasses);
            availableFields.addAll(availableFieldsDefinedInSuperClasses);
        }

        return Collections.unmodifiableList(availableFields);
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
    public static List<FieldInfo> getAvailableFields(final ClassInfo usedClass,
            final ClassInfo usingClass) {

        if ((null == usedClass) || (null == usingClass)) {
            throw new NullPointerException();
        }

        // 使用されるクラスの最も外側のクラスを取得
        final ClassInfo usedOutestClass;
        if (usedClass instanceof InnerClassInfo) {
            usedOutestClass = NameResolver.getOuterstClass((InnerClassInfo) usedClass);
        } else {
            usedOutestClass = usedClass;
        }

        // 使用するクラスの最も外側のクラスを取得
        final ClassInfo usingOutestClass;
        if (usingClass instanceof InnerClassInfo) {
            usingOutestClass = NameResolver.getOuterstClass((InnerClassInfo) usingClass);
        } else {
            usingOutestClass = usingClass;
        }

        // このクラスで定義されているフィールドのうち，使用するクラスで利用可能なフィールドを取得する
        // 2つのクラスが同じ場合，全てのフィールドが利用可能
        if (usedOutestClass.equals(usingOutestClass)) {

            return NameResolver.getAvailableFields(usedClass);

            // 2つのクラスが同じ名前空間を持っている場合
        } else if (usedOutestClass.getNamespace().equals(usingOutestClass.getNamespace())) {

            final List<FieldInfo> availableFields = new LinkedList<FieldInfo>();

            // 名前空間可視性を持ったフィールドのみが利用可能
            for (final FieldInfo field : NameResolver.getAvailableFields(usedClass)) {
                if (field.isNamespaceVisible()) {
                    availableFields.add(field);
                }
            }

            return Collections.unmodifiableList(availableFields);

            // 違う名前空間を持っている場合
        } else {

            final List<FieldInfo> availableFields = new LinkedList<FieldInfo>();

            // 全可視性を持つフィールドのみが利用可能
            for (final FieldInfo field : NameResolver.getAvailableFields(usedClass)) {
                if (field.isPublicVisible()) {
                    availableFields.add(field);
                }
            }

            return Collections.unmodifiableList(availableFields);
        }
    }

    /**
     * 引数で与えられたクラス型で呼び出し可能なコンストラクタのListを返す
     * 
     * @param classType
     * @return
     */
    public static final List<ConstructorInfo> getAvailableConstructors(final ClassTypeInfo classType) {

        final List<ConstructorInfo> constructors = new LinkedList<ConstructorInfo>();
        final ClassInfo classInfo = classType.getReferencedClass();

        constructors.addAll(classInfo.getDefinedConstructors());

        for (final ClassTypeInfo superClassType : classInfo.getSuperClasses()) {
            final List<ConstructorInfo> superConstructors = NameResolver
                    .getAvailableConstructors(superClassType);
            constructors.addAll(superConstructors);
        }

        return constructors;
    }

    /**
     * 引数で与えられたクラスの直接のインナークラスを返す．親クラスで定義されたインナークラスも含まれる．
     * 
     * @param classInfo クラス
     * @return 引数で与えられたクラスの直接のインナークラス，親クラスで定義されたインナークラスも含まれる．
     */
    public static final SortedSet<InnerClassInfo> getAvailableDirectInnerClasses(
            final ClassInfo classInfo) {

        if (null == classInfo) {
            throw new IllegalArgumentException();
        }

        final SortedSet<InnerClassInfo> availableDirectInnerClasses = new TreeSet<InnerClassInfo>();

        // 引数で与えられたクラスの直接のインナークラスを追加
        availableDirectInnerClasses.addAll(classInfo.getInnerClasses());

        // 親クラスに対して再帰的に処理
        for (final ClassInfo superClassInfo : ClassTypeInfo.convert(classInfo.getSuperClasses())) {

            final SortedSet<InnerClassInfo> availableDirectInnerClassesInSuperClass = NameResolver
                    .getAvailableDirectInnerClasses((ClassInfo) superClassInfo);
            availableDirectInnerClasses.addAll(availableDirectInnerClassesInSuperClass);
        }

        return Collections.unmodifiableSortedSet(availableDirectInnerClasses);
    }

    public static final List<TypeParameterInfo> getAvailableTypeParameters(
            final TypeParameterizable unit) {

        if (null == unit) {
            throw new IllegalArgumentException();
        }

        final List<TypeParameterInfo> typeParameters = new LinkedList<TypeParameterInfo>();

        typeParameters.addAll(unit.getTypeParameters());
        final TypeParameterizable outerUnit = unit.getOuterTypeParameterizableUnit();
        if (null != outerUnit) {
            typeParameters.addAll(getAvailableTypeParameters(outerUnit));
        }

        return Collections.unmodifiableList(typeParameters);
    }

    public static List<MethodInfo> getAvailableMethods(final ClassInfo usedClass,
            final ClassInfo usingClass) {
        return Collections.unmodifiableList(getAvailableMethods(usedClass, usingClass,
                new HashSet<ClassInfo>()));
    }

    private static List<MethodInfo> getAvailableMethods(final ClassInfo usedClass,
            final ClassInfo usingClass, final Set<ClassInfo> checkedClasses) {

        // すでにチェックしているクラスであれば何もせずに抜ける
        if (checkedClasses.contains(usedClass)) {
            return Collections.<MethodInfo> emptyList();
        }

        // チェック済みクラスに追加
        checkedClasses.add(usedClass);

        // usedに定義されているメソッドのうち，利用可能なものを追加
        final List<MethodInfo> availableMethods = new ArrayList<MethodInfo>();
        availableMethods.addAll(extractAvailableMethods(usedClass, usingClass));

        // usedの外クラスをチェック
        if (usedClass instanceof InnerClassInfo) {
            final ClassInfo outerClass = ((InnerClassInfo) usedClass).getOuterClass();
            availableMethods.addAll(getAvailableMethods(outerClass, usingClass, checkedClasses));
        }

        // 親クラスをチェック
        for (final ClassTypeInfo superClassType : usedClass.getSuperClasses()) {
            final ClassInfo superClass = superClassType.getReferencedClass();
            availableMethods.addAll(getAvailableMethods(superClass, usingClass, checkedClasses));
        }

        return availableMethods;
    }

    private static List<MethodInfo> extractAvailableMethods(final ClassInfo usedClass,
            final ClassInfo usingClass) {

        final List<MethodInfo> availableMethods = new ArrayList<MethodInfo>();

        // usingとusedが等しい場合は，すべてのメソッドを使用可能
        if (usingClass.equals(usedClass)) {
            availableMethods.addAll(usedClass.getDefinedMethods());
        }

        // usingがusedのインナークラスであればすべてのメソッドを使用可能
        if (usingClass.isInnerClass(usedClass)) {
            availableMethods.addAll(usedClass.getDefinedMethods());
        }

        // usedがusingのインナークラスの場合は，すべてのメソッドを利用可能
        if (usedClass.isInnerClass(usingClass)) {
            availableMethods.addAll(usedClass.getDefinedMethods());
        }

        // usingがusedと同じパッケージであれば，private 以外のメソッドが使用可能
        if (usingClass.getNamespace().equals(usedClass.getNamespace())) {
            for (final MethodInfo method : usedClass.getDefinedMethods()) {
                if (method.isNamespaceVisible()) {
                    availableMethods.add(method);
                }
            }
        }

        // usingがインナークラスであれば，その最外クラスと，usedクラスが同じパッケージであれば使用可能
        if (usingClass instanceof InnerClassInfo) {
            final ClassInfo outestClass = TargetInnerClassInfo
                    .getOutestClass((InnerClassInfo) usingClass);
            if (outestClass.getNamespace().equals(usedClass.getNamespace())) {
                for (final MethodInfo method : usedClass.getDefinedMethods()) {
                    if (method.isNamespaceVisible()) {
                        availableMethods.add(method);
                    }
                }
            }
        }

        // usedがインナークラスであれば，その最外クラスと，usingくらすが同じパッケージであれば使用可能
        if (usedClass instanceof InnerClassInfo) {
            final ClassInfo outestClass = TargetInnerClassInfo
                    .getOutestClass((InnerClassInfo) usedClass);
            if (usingClass.getNamespace().equals(outestClass.getNamespace())) {
                for(final MethodInfo method : usedClass.getDefinedMethods()){
                    if(method.isNamespaceVisible()){
                        availableMethods.add(method);
                    }
                }
            }
        }

        // usingがusedのサブクラスであれば,protected以外のメソッドが使用可能
        if (usingClass.isSubClass(usedClass)) {
            for (final MethodInfo method : usedClass.getDefinedMethods()) {
                if (method.isInheritanceVisible()) {
                    availableMethods.add(method);
                }
            }
        }

        // usingがusedと関係のないクラスであれば，publicのメソッドが利用可能
        for (final MethodInfo method : usedClass.getDefinedMethods()) {
            if (method.isPublicVisible()) {
                availableMethods.add(method);
            }
        }

        return availableMethods;
    }
}
