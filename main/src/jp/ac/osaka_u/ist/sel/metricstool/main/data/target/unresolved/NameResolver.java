package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
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

    public static List<ClassInfo> getAvailableClasses(final ClassInfo usingClass) {

         final List<ClassInfo> availableClasses = getAvailableClasses(usingClass, usingClass,
                new HashSet<ClassInfo>());
        final ClassInfo outestClass = usingClass instanceof InnerClassInfo ? TargetInnerClassInfo
                .getOutestClass((InnerClassInfo) usingClass) : usingClass;
        availableClasses.addAll(DataManager.getInstance().getClassInfoManager().getClassInfos(
                outestClass.getNamespace()));
        return availableClasses;
    }

    private static List<ClassInfo> getAvailableClasses(final ClassInfo usedClass,
            final ClassInfo usingClass, Set<ClassInfo> checkedClasses) {

        if (checkedClasses.contains(usedClass)) {
            return Collections.<ClassInfo> emptyList();
        }

        checkedClasses.add(usedClass);

        final List<ClassInfo> availableClasses = new ArrayList<ClassInfo>();
        if (isAccessible(usedClass, usingClass)) {
            availableClasses.add(usedClass);
        }

        for (final InnerClassInfo innerClass : usedClass.getInnerClasses()) {
            checkedClasses.addAll(getAvailableClasses((ClassInfo) innerClass, usingClass,
                    checkedClasses));
        }

        if (usedClass instanceof InnerClassInfo) {
            final ClassInfo outerUsedClass = ((InnerClassInfo) usedClass).getOuterClass();
            checkedClasses.addAll(getAvailableClasses(outerUsedClass, usingClass, checkedClasses));
        }

        for (final ClassTypeInfo superUsedType : usedClass.getSuperClasses()) {
            final ClassInfo superUsedClass = superUsedType.getReferencedClass();
            checkedClasses.addAll(getAvailableClasses(superUsedClass, usingClass, checkedClasses));
        }

        return availableClasses;
    }

    /**
     * usedClassがusingClassにおいてアクセス可能かを返す．
     * なお，usedClassがpublicである場合は考慮していない．
     * publicでアクセス可能かどうかは，インポート文も調べなければわからない
     * 
     * @param usedClass
     * @param usingClass
     * @return
     */
    public static boolean isAccessible(final ClassInfo usedClass, final ClassInfo usingClass) {

        // usedがインナークラスのとき
        if (usedClass instanceof InnerClassInfo) {

            //直のouterクラスからはアクセス可
            {
                final ClassInfo outerClass = ((InnerClassInfo) usedClass).getOuterClass();
                if (outerClass.equals(usingClass)) {
                    return true;
                }
            }

            // 直のouterクラスが同じクラスからはアクセス可
            if (usedClass.getNamespace().equals(usingClass.getNamespace())) {
                return true;
            }

            // 直のouterクラスがインナークラスでない場合
            if (!(((InnerClassInfo) usedClass).getOuterClass() instanceof InnerClassInfo)) {
                final ClassInfo outerUsedClass = ((InnerClassInfo) usedClass).getOuterClass();
                final ClassInfo outestUsingClass = usingClass instanceof InnerClassInfo ? TargetInnerClassInfo
                        .getOutestClass((InnerClassInfo) usingClass)
                        : usingClass;

                // 名前空間が同じ時
                if (outerUsedClass.getNamespace().equals(outestUsingClass.getNamespace())) {

                    ClassInfo outerUsingClass = usingClass;
                    while (true) {
                        if (outerUsingClass.isSubClass(outerUsedClass)) {
                            return true;
                        }

                        if (!(outerUsingClass instanceof InnerClassInfo)) {
                            break;
                        }

                        outerUsingClass = ((InnerClassInfo) outerUsingClass).getOuterClass();
                    }
                }

                // 名前空間が違う時
                else {
                    if (usedClass.isInheritanceVisible()) {

                        ClassInfo outerUsingClass = usingClass;
                        while (true) {
                            if (outerUsingClass.isSubClass(outerUsedClass)) {
                                return true;
                            }

                            if (!(outerUsingClass instanceof InnerClassInfo)) {
                                break;
                            }

                            outerUsingClass = ((InnerClassInfo) outerUsingClass).getOuterClass();
                        }
                    }
                }
            }
        }

        // usedがインナークラスでないとき
        else {

            //名前空間が同じであれば参照可
            {
                final ClassInfo tmpUsingClass = usingClass instanceof InnerClassInfo ? TargetInnerClassInfo
                        .getOutestClass((InnerClassInfo) usingClass)
                        : usingClass;
                if (tmpUsingClass.getNamespace().equals(usedClass.getNamespace())) {
                    return true;
                }
            }

            //usedが子クラスから参照可能であれば
            if (usedClass.isInheritanceVisible()) {
                ClassInfo outerClass = usingClass;
                while (true) {
                    if (outerClass.isSubClass(usedClass)) {
                        return true;
                    }

                    if (!(outerClass instanceof InnerClassInfo)) {
                        break;
                    }

                    outerClass = ((InnerClassInfo) outerClass).getOuterClass();
                }
            }
        }

        return false;
    }

    /**
     * 使用するクラスと使用されるクラスを与えることにより，利用可能なメソッドのListを返す
     * 
     * @param usedClass 使用されるクラス
     * @param usingClass 使用するクラス
     * @return
     */
    public static synchronized List<MethodInfo> getAvailableMethods(final ClassInfo usedClass,
            final ClassInfo usingClass) {

        final boolean hasCache = METHOD_CACHE.hasCash(usedClass, usingClass);
        if (hasCache) {
            return METHOD_CACHE.getCache(usedClass, usingClass);
        } else {
            final List<MethodInfo> methods = getAvailableMethods(usedClass, usingClass,
                    new HashSet<ClassInfo>());
            METHOD_CACHE.putCache(usedClass, usingClass, methods);
            return methods;
        }
    }

    /**
     * 使用するクラスと使用されるクラスを与えることにより，利用可能なフィールドのListを返す
     * 
     * @param usedClass 使用されるクラス
     * @param usingClass 使用するクラス
     * @return
     */
    public static synchronized List<FieldInfo> getAvailableFields(final ClassInfo usedClass,
            final ClassInfo usingClass) {

        final boolean hasCache = FIELD_CACHE.hasCash(usedClass, usingClass);
        if (hasCache) {
            return FIELD_CACHE.getCache(usedClass, usingClass);
        } else {
            final List<FieldInfo> fields = getAvailableFields(usedClass, usingClass,
                    new HashSet<ClassInfo>());
            FIELD_CACHE.putCache(usedClass, usingClass, fields);
            return fields;
        }
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

    private static List<FieldInfo> getAvailableFields(final ClassInfo usedClass,
            final ClassInfo usingClass, final Set<ClassInfo> checkedClasses) {

        // すでにチェックしているクラスであれば何もせずに抜ける
        if (checkedClasses.contains(usedClass)) {
            return Collections.<FieldInfo> emptyList();
        }

        // チェック済みクラスに追加
        checkedClasses.add(usedClass);

        // usedに定義されているメソッドのうち，利用可能なものを追加
        final List<FieldInfo> availableFields = new ArrayList<FieldInfo>();
        availableFields.addAll(extractAvailableFields(usedClass, usingClass));

        // usedの外クラスをチェック
        if (usedClass instanceof InnerClassInfo) {
            final ClassInfo outerClass = ((InnerClassInfo) usedClass).getOuterClass();
            availableFields.addAll(getAvailableFields(outerClass, usingClass, checkedClasses));
        }

        // 親クラスをチェック
        for (final ClassTypeInfo superClassType : usedClass.getSuperClasses()) {
            final ClassInfo superClass = superClassType.getReferencedClass();
            availableFields.addAll(getAvailableFields(superClass, usingClass, checkedClasses));
        }

        return availableFields;
    }

    private static List<MethodInfo> extractAvailableMethods(final ClassInfo usedClass,
            final ClassInfo usingClass) {

        final List<MethodInfo> availableMethods = new ArrayList<MethodInfo>();

        // usingとusedが等しい場合は，すべてのメソッドを使用可能
        {
            final ClassInfo tmpUsingClass = usingClass instanceof InnerClassInfo ? TargetInnerClassInfo
                    .getOutestClass((InnerClassInfo) usingClass)
                    : usingClass;
            final ClassInfo tmpUsedClass = usedClass instanceof InnerClassInfo ? TargetInnerClassInfo
                    .getOutestClass((InnerClassInfo) usedClass)
                    : usedClass;
            if (tmpUsingClass.getNamespace().equals(tmpUsedClass.getNamespace())) {
                availableMethods.addAll(usedClass.getDefinedMethods());
            }
        }

        // usingがusedと同じパッケージであれば，private 以外のメソッドが使用可能
        {
            final ClassInfo tmpUsingClass = usingClass instanceof InnerClassInfo ? TargetInnerClassInfo
                    .getOutestClass((InnerClassInfo) usingClass)
                    : usingClass;
            final ClassInfo tmpUsedClass = usedClass instanceof InnerClassInfo ? TargetInnerClassInfo
                    .getOutestClass((InnerClassInfo) usedClass)
                    : usedClass;
            if (tmpUsingClass.getNamespace().equals(tmpUsedClass.getNamespace())) {
                for (final MethodInfo method : usedClass.getDefinedMethods()) {
                    if (method.isNamespaceVisible()) {
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

    private static List<FieldInfo> extractAvailableFields(final ClassInfo usedClass,
            final ClassInfo usingClass) {

        final List<FieldInfo> availableFields = new ArrayList<FieldInfo>();

        // usingとusedが等しい場合は，すべてのフィールドを使用可能
        {
            final ClassInfo tmpUsingClass = usingClass instanceof InnerClassInfo ? TargetInnerClassInfo
                    .getOutestClass((InnerClassInfo) usingClass)
                    : usingClass;
            final ClassInfo tmpUsedClass = usedClass instanceof InnerClassInfo ? TargetInnerClassInfo
                    .getOutestClass((InnerClassInfo) usedClass)
                    : usedClass;
            if (tmpUsingClass.getNamespace().equals(tmpUsedClass.getNamespace())) {
                availableFields.addAll(usedClass.getDefinedFields());
            }
        }

        // usingがusedと同じパッケージであれば，private 以外のフィールドが使用可能
        {
            final ClassInfo tmpUsingClass = usingClass instanceof InnerClassInfo ? TargetInnerClassInfo
                    .getOutestClass((InnerClassInfo) usingClass)
                    : usingClass;
            final ClassInfo tmpUsedClass = usedClass instanceof InnerClassInfo ? TargetInnerClassInfo
                    .getOutestClass((InnerClassInfo) usedClass)
                    : usedClass;
            if (tmpUsingClass.getNamespace().equals(tmpUsedClass.getNamespace())) {
                for (final FieldInfo field : usedClass.getDefinedFields()) {
                    if (field.isNamespaceVisible()) {
                        availableFields.add(field);
                    }
                }
            }
        }

        // usingがusedのサブクラスであれば,protected以外のフィールドが使用可能
        if (usingClass.isSubClass(usedClass)) {
            for (final FieldInfo field : usedClass.getDefinedFields()) {
                if (field.isInheritanceVisible()) {
                    availableFields.add(field);
                }
            }
        }

        // usingがusedと関係のないクラスであれば，publicのフィールドが利用可能
        for (final FieldInfo field : usedClass.getDefinedFields()) {
            if (field.isPublicVisible()) {
                availableFields.add(field);
            }
        }

        return availableFields;
    }

    private static final Cache<MethodInfo> METHOD_CACHE = new Cache<MethodInfo>();

    private static final Cache<FieldInfo> FIELD_CACHE = new Cache<FieldInfo>();

    /**
     * 使用するクラスと使用されるクラスの関係から利用可能なメンバーのキャッシュを蓄えておくためのクラス
     * 
     * @author higo
     *
     * @param <T>
     */
    static class Cache<T> {

        private final ConcurrentMap<ClassInfo, ConcurrentMap<ClassInfo, List<T>>> firstCache;

        Cache() {
            this.firstCache = new ConcurrentHashMap<ClassInfo, ConcurrentMap<ClassInfo, List<T>>>();
        }

        boolean hasCash(final ClassInfo usedClass, final ClassInfo usingClass) {

            final boolean hasSecondCache = this.firstCache.containsKey(usedClass);
            if (!hasSecondCache) {
                return false;
            }

            final ConcurrentMap<ClassInfo, List<T>> secondCache = this.firstCache.get(usedClass);
            final boolean hasThirdCache = secondCache.containsKey(usingClass);
            return hasThirdCache;
        }

        List<T> getCache(final ClassInfo usedClass, final ClassInfo usingClass) {

            final ConcurrentMap<ClassInfo, List<T>> secondCache = this.firstCache.get(usedClass);
            if (null == secondCache) {
                return null;
            }

            return secondCache.get(usingClass);
        }

        void putCache(final ClassInfo usedClass, final ClassInfo usingClass, final List<T> cache) {

            ConcurrentMap<ClassInfo, List<T>> secondCache = this.firstCache.get(usedClass);
            if (null == secondCache) {
                secondCache = new ConcurrentHashMap<ClassInfo, List<T>>();
                this.firstCache.put(usedClass, secondCache);
            }

            secondCache.put(usingClass, cache);
        }
    }
}
