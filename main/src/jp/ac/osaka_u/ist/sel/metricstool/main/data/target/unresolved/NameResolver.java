package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VoidTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * Unresolved * Info から * Info を得るための名前解決ユーティリティクラス
 * 
 * @author y-higo
 * 
 */
public final class NameResolver {

    /**
     * UnresolvedTypeInfoな情報から TypeInfoを生成する． 参照されているTypeInfoがclassInfoManagerに含まれていない場合は追加する．
     * 
     * @param unresolvedTypeInfo 名前解決したい型情報
     * @param classInfoManager 参照型の解決に用いるデータベース
     * @return 名前解決された型情報
     */
    public static TypeInfo resolveTypeInfo(final UnresolvedTypeInfo unresolvedTypeInfo,
            final ClassInfoManager classInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == unresolvedTypeInfo) || (null == classInfoManager)) {
            throw new NullPointerException();
        }

        // 未解決プリミティブ型の場合
        if (unresolvedTypeInfo instanceof PrimitiveTypeInfo) {
            return (PrimitiveTypeInfo) unresolvedTypeInfo;

            // 未解決void型の場合
        } else if (unresolvedTypeInfo instanceof VoidTypeInfo) {
            return (VoidTypeInfo) unresolvedTypeInfo;

            // 未解決参照型の場合
        } else if (unresolvedTypeInfo instanceof UnresolvedReferenceTypeInfo) {

            // 利用可能な名前空間から，型名を探す
            String[] referenceName = ((UnresolvedReferenceTypeInfo) unresolvedTypeInfo)
                    .getReferenceName();
            for (AvailableNamespaceInfo availableNamespace : ((UnresolvedReferenceTypeInfo) unresolvedTypeInfo)
                    .getAvailableNamespaces()) {

                // 名前空間名.* となっている場合
                if (availableNamespace.isAllClasses()) {
                    String[] namespace = availableNamespace.getNamespace();

                    // 名前空間の下にある各クラスに対して
                    for (ClassInfo classInfo : classInfoManager.getClassInfos(namespace)) {
                        final String className = classInfo.getClassName();

                        // クラス名と参照名の先頭が等しい場合は，そのクラス名が参照先であると決定する
                        if (className.equals(referenceName[0])) {
                            return classInfo;
                        }
                    }

                    // 名前空間.クラス名 となっている場合
                } else {

                    final String[] importName = availableNamespace.getImportName();

                    // クラス名と参照名の先頭が等しい場合は，そのクラス名が参照先であると決定する
                    if (importName[importName.length - 1].equals(referenceName[0])) {

                        final String[] namespace = availableNamespace.getNamespace();
                        final String[] fullQualifiedName = new String[namespace.length
                                + referenceName.length];
                        System.arraycopy(namespace, 0, fullQualifiedName, 0, namespace.length);
                        System.arraycopy(referenceName, 0, fullQualifiedName, namespace.length,
                                referenceName.length);
                        ClassInfo specifiedClassInfo = classInfoManager
                                .getClassInfo(fullQualifiedName);
                        if (null == specifiedClassInfo) {
                            specifiedClassInfo = new ExternalClassInfo(fullQualifiedName);
                            classInfoManager.add((ExternalClassInfo) specifiedClassInfo);
                        }
                        return specifiedClassInfo;
                    }
                }
            }

            // 見つからなかった場合は名前空間名がUNKNOWNなクラスを登録する
            ExternalClassInfo classInfo = new ExternalClassInfo(
                    referenceName[referenceName.length - 1]);
            classInfoManager.add(classInfo);
            return classInfo;

            // 未解決エンティティ使用の場合
        } else if (unresolvedTypeInfo instanceof UnresolvedEntityUsage) {

            final String specifiedName = ((UnresolvedEntityUsage) unresolvedTypeInfo)
                    .getFieldName();
            final AvailableNamespaceInfoSet availableNamespaces = ((UnresolvedEntityUsage) unresolvedTypeInfo)
                    .getAvailableNamespaces();
            for (AvailableNamespaceInfo availableNamespace : availableNamespaces) {

                // 名前空間名.* となっている場合
                if (availableNamespace.isAllClasses()) {

                    // 名前空間名を取得
                    final String[] namespace = availableNamespace.getNamespace();

                    // 名前空間の下にある各クラスに対して
                    for (ClassInfo classInfo : classInfoManager.getClassInfos(namespace)) {

                        // クラス名と参照名が等しい場合は，そのクラス名が参照先であると決定する．
                        if (specifiedName.equals(classInfo.getClassName())) {
                            return classInfo;
                        }
                    }

                    // 名前空間名.クラス名となっている場合
                } else {

                    final String[] importName = availableNamespace.getImportName();

                    // クラス名と参照名が等しい場合は，そのクラス名が参照先であると決定する．
                    if (specifiedName.equals(importName[importName.length - 1])) {
                        ClassInfo specifiedClassInfo = classInfoManager.getClassInfo(importName);
                        if (null == specifiedClassInfo) {
                            specifiedClassInfo = new ExternalClassInfo(importName);
                            classInfoManager.add((ExternalClassInfo) specifiedClassInfo);
                        }
                        return specifiedClassInfo;
                    }
                }
            }

            return null;

            // それ以外の型の場合はエラー
        } else {
            throw new IllegalArgumentException(unresolvedTypeInfo.toString()
                    + " is a wrong object!");
        }
    }

    /**
     * 未解決エンティティ使用情報を解決し，エンティティ使用処理が行われているメソッドに登録する．また，エンティティの型を返す．
     * 
     * @param entityUsage 未解決エンティティ使用情報
     * @param usingClass エンティティ使用処理が行われているクラス
     * @param usingMethod エンティティ使用処理が行われているメソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     * @param resolvedCache 解決済みUnresolvedTypeInfoのキャッシュ
     * @return 解決済みエンティティの型
     */
    public static TypeInfo resolveEntityReference(final UnresolvedEntityUsage entityUsage,
            final TargetClassInfo usingClass, final TargetMethodInfo usingMethod,
            final ClassInfoManager classInfoManager, final FieldInfoManager fieldInfoManager,
            final MethodInfoManager methodInfoManager,
            final Map<UnresolvedTypeInfo, TypeInfo> resolvedCache) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == entityUsage) || (null == usingClass) || (null == usingMethod)
                || (null == classInfoManager) || (null == fieldInfoManager)
                || (null == methodInfoManager) || (null == resolvedCache)) {
            throw new NullPointerException();
        }

        // 既に解決済みであれば，そこから型を取得
        if (resolvedCache.containsKey(entityUsage)) {
            final TypeInfo type = resolvedCache.get(entityUsage);
            return type;
        }

        // エンティティ名，及びエンティティ使用がくっついている未定義型を取得
        final String fieldName = entityUsage.getFieldName();
        final UnresolvedTypeInfo unresolvedFieldOwnerClassType = entityUsage.getOwnerClassType();

        // エンティティ使用(a)がエンティティ使用(b)にくっついている場合 (b.a)
        if (unresolvedFieldOwnerClassType instanceof UnresolvedEntityUsage) {

            // (b)のクラス定義を取得
            final TypeInfo fieldOwnerClassType = NameResolver.resolveEntityReference(
                    (UnresolvedEntityUsage) unresolvedFieldOwnerClassType, usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager, resolvedCache);

            // 利用可能なフィールド一覧を取得
            final List<TargetFieldInfo> availableFields = NameResolver.getAvailableFields(
                    (TargetClassInfo) fieldOwnerClassType, usingClass);

            // 利用可能なフィールドを，未解決エンティティ名で検索
            for (TargetFieldInfo availableField : availableFields) {

                // 一致するフィールド名が見つかった場合
                if (fieldName.equals(availableField.getName())) {
                    usingMethod.addReferencee(availableField);
                    availableField.addReferencer(usingMethod);

                    // 解決済みキャッシュにに登録
                    resolvedCache.put(entityUsage, availableField.getType());

                    return availableField.getType();
                }
            }

            err.println("resolveEntityReference : TODO1");
            // TODO 見つからなかった場合の処理が必要

            // エンティティ使用(a)がメソッド呼び出し(c())にくっついている場合(c().a)
        } else if (unresolvedFieldOwnerClassType instanceof UnresolvedMethodCall) {

            // (c)のクラス定義を取得
            final TypeInfo fieldOwnerClassType = NameResolver.resolveMethodCall(
                    (UnresolvedMethodCall) unresolvedFieldOwnerClassType, usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager, resolvedCache);

            // 利用可能なフィールド一覧を取得
            final List<TargetFieldInfo> availableFields = NameResolver.getAvailableFields(
                    (TargetClassInfo) fieldOwnerClassType, usingClass);

            // 利用可能なフィールド一覧を，未解決フィールド名で検索
            for (TargetFieldInfo availableField : availableFields) {

                // フィールド名が見つかった場合
                if (fieldName.equals(availableField.getName())) {
                    usingMethod.addReferencee(availableField);
                    availableField.addReferencer(usingMethod);

                    // 解決済みキャッシュにに登録
                    resolvedCache.put(entityUsage, availableField.getType());

                    return availableField.getType();
                }
            }

            err.println("resolveEntityReference : TODO2");
            // TODO 見つからなかった場合の処理が必要

            // フィールド使用(a)が自オブジェクトにくっついている場合(a or this.a or super.a )
        } else if (unresolvedFieldOwnerClassType instanceof UnresolvedReferenceTypeInfo) {

            // 使用可能なフィールド名から，未解決エンティティ使用を解決
            {
                // 利用可能なフィールド一覧を取得
                final List<TargetFieldInfo> availableFields = NameResolver
                        .getAvailableFields(usingClass);

                // 利用可能なフィールド一覧を，未解決フィールド名で検索
                for (TargetFieldInfo availableField : availableFields) {

                    // 一致するフィールド名が見つかった場合
                    if (fieldName.equals(availableField.getName())) {
                        usingMethod.addReferencee(availableField);
                        availableField.addReferencer(usingMethod);

                        // 解決済みキャッシュにに登録
                        resolvedCache.put(entityUsage, availableField.getType());

                        return availableField.getType();
                    }
                }
            }

            // 利用可能なクラス名から未解決エンティティ使用を解決
            {
                final TypeInfo classInfo = NameResolver.resolveTypeInfo(
                        unresolvedFieldOwnerClassType, classInfoManager);

                // 一致するクラス名が見つかった場合
                if (null != classInfo) {

                    // 解決済みキャッシュにに登録
                    resolvedCache.put(entityUsage, classInfo);
                    return classInfo;
                }
            }

            err.println("resolveEntityReference : TODO3");
            // TODO 見つからなかった場合の処理が必要
            // クラス名を見ている場合や，
            // ExternalClassInfo を継承していて，そこで定義されている変数を使用している場合など

        }

        throw new IllegalArgumentException(entityUsage.toString() + " is wrong!");
    }
    
    /**
     * 未解決エンティティ代入情報を解決し，エンティティ代入処理が行われているメソッドに登録する．また，エンティティの型を返す．
     * 
     * @param entityUsage 未解決エンティティ使用情報
     * @param usingClass エンティティ使用処理が行われているクラス
     * @param usingMethod エンティティ使用処理が行われているメソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     * @param resolvedCache 解決済みUnresolvedTypeInfoのキャッシュ
     * @return 解決済みエンティティの型
     */
    public static TypeInfo resolveEntityAssignment(final UnresolvedEntityUsage entityUsage,
            final TargetClassInfo usingClass, final TargetMethodInfo usingMethod,
            final ClassInfoManager classInfoManager, final FieldInfoManager fieldInfoManager,
            final MethodInfoManager methodInfoManager,
            final Map<UnresolvedTypeInfo, TypeInfo> resolvedCache) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == entityUsage) || (null == usingClass) || (null == usingMethod)
                || (null == classInfoManager) || (null == fieldInfoManager)
                || (null == methodInfoManager) || (null == resolvedCache)) {
            throw new NullPointerException();
        }

        // 既に解決済みであれば，そこから型を取得
        if (resolvedCache.containsKey(entityUsage)) {
            final TypeInfo type = resolvedCache.get(entityUsage);
            return type;
        }

        // エンティティ名，及びエンティティ使用がくっついている未定義型を取得
        final String fieldName = entityUsage.getFieldName();
        final UnresolvedTypeInfo unresolvedFieldOwnerClassType = entityUsage.getOwnerClassType();

        // エンティティ使用(a)がエンティティ使用(b)にくっついている場合 (b.a)
        if (unresolvedFieldOwnerClassType instanceof UnresolvedEntityUsage) {

            // (b)のクラス定義を取得
            final TypeInfo fieldOwnerClassType = NameResolver.resolveEntityReference(
                    (UnresolvedEntityUsage) unresolvedFieldOwnerClassType, usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager, resolvedCache);

            // 利用可能なフィールド一覧を取得
            final List<TargetFieldInfo> availableFields = NameResolver.getAvailableFields(
                    (TargetClassInfo) fieldOwnerClassType, usingClass);

            // 利用可能なフィールドを，未解決エンティティ名で検索
            for (TargetFieldInfo availableField : availableFields) {

                // 一致するフィールド名が見つかった場合
                if (fieldName.equals(availableField.getName())) {
                    usingMethod.addAssignmentee(availableField);
                    availableField.addAssignmenter(usingMethod);

                    // 解決済みキャッシュにに登録
                    resolvedCache.put(entityUsage, availableField.getType());

                    return availableField.getType();
                }
            }

            err.println("resolveEntityAssignment : TODO1");
            // TODO 見つからなかった場合の処理が必要

            // エンティティ使用(a)がメソッド呼び出し(c())にくっついている場合(c().a)
        } else if (unresolvedFieldOwnerClassType instanceof UnresolvedMethodCall) {

            // (c)のクラス定義を取得
            final TypeInfo fieldOwnerClassType = NameResolver.resolveMethodCall(
                    (UnresolvedMethodCall) unresolvedFieldOwnerClassType, usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager, resolvedCache);

            // 利用可能なフィールド一覧を取得
            final List<TargetFieldInfo> availableFields = NameResolver.getAvailableFields(
                    (TargetClassInfo) fieldOwnerClassType, usingClass);

            // 利用可能なフィールド一覧を，未解決フィールド名で検索
            for (TargetFieldInfo availableField : availableFields) {

                // フィールド名が見つかった場合
                if (fieldName.equals(availableField.getName())) {
                    usingMethod.addAssignmentee(availableField);
                    availableField.addAssignmenter(usingMethod);

                    // 解決済みキャッシュにに登録
                    resolvedCache.put(entityUsage, availableField.getType());

                    return availableField.getType();
                }
            }

            err.println("resolveEntityAssignment : TODO2");
            // TODO 見つからなかった場合の処理が必要

            // フィールド使用(a)が自オブジェクトにくっついている場合(a or this.a or super.a )
        } else if (unresolvedFieldOwnerClassType instanceof UnresolvedReferenceTypeInfo) {

            // 使用可能なフィールド名から，未解決エンティティ使用を解決
            {
                // 利用可能なフィールド一覧を取得
                final List<TargetFieldInfo> availableFields = NameResolver
                        .getAvailableFields(usingClass);

                // 利用可能なフィールド一覧を，未解決フィールド名で検索
                for (TargetFieldInfo availableField : availableFields) {

                    // 一致するフィールド名が見つかった場合
                    if (fieldName.equals(availableField.getName())) {
                        usingMethod.addAssignmentee(availableField);
                        availableField.addAssignmenter(usingMethod);

                        // 解決済みキャッシュにに登録
                        resolvedCache.put(entityUsage, availableField.getType());

                        return availableField.getType();
                    }
                }
            }

            // 利用可能なクラス名から未解決エンティティ使用を解決
            {
                final TypeInfo classInfo = NameResolver.resolveTypeInfo(
                        unresolvedFieldOwnerClassType, classInfoManager);

                // 一致するクラス名が見つかった場合
                if (null != classInfo) {

                    // 解決済みキャッシュにに登録
                    resolvedCache.put(entityUsage, classInfo);
                    return classInfo;
                }
            }

            err.println("resolveEntityAssignment : TODO3");
            // TODO 見つからなかった場合の処理が必要
            // クラス名を見ている場合や，
            // ExternalClassInfo を継承していて，そこで定義されている変数を使用している場合など

        }

        throw new IllegalArgumentException(entityUsage.toString() + " is wrong!");
    }

    /**
     * 未解決メソッド呼び出し情報を解決し，メソッド呼び出し処理処理が行われているメソッドに登録する．また，メソッドの返り値の型を返す．
     * 
     * @param methodCall メソッド呼び出し情報
     * @param usingClass メソッド呼び出しが行われているクラス
     * @param usingMethod メソッド呼び出しが行われているメソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     * @param resolvedCache 解決済みUnresolvedTypeInfoのキャッシュ
     * @return メソッド呼び出し情報に対応する MethodInfo
     */
    public static TypeInfo resolveMethodCall(final UnresolvedMethodCall methodCall,
            final TargetClassInfo usingClass, final TargetMethodInfo usingMethod,
            final ClassInfoManager classInfoManager, final FieldInfoManager fieldInfoManager,
            final MethodInfoManager methodInfoManager,
            final Map<UnresolvedTypeInfo, TypeInfo> resolvedCache) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == methodCall) || (null == usingClass) || (null == usingMethod)
                || (null == classInfoManager) || (null == methodInfoManager)
                || (null == resolvedCache)) {
            throw new NullPointerException();
        }

        // 既に解決済みであれば，そこから型を取得
        if (resolvedCache.containsKey(methodCall)) {
            final TypeInfo type = resolvedCache.get(methodCall);
            return type;
        }

        // メソッドのシグネチャ，及びメソッド使用がくっついている未定義参照型を取得
        final String methodName = methodCall.getMethodName();
        final List<UnresolvedTypeInfo> unresolvedParameterTypes = methodCall.getParameterTypes();
        final UnresolvedTypeInfo unresolvedMethodOwnerClassType = methodCall.getOwnerClassType();

        // メソッドの未解決引数を解決
        final List<TypeInfo> parameterTypes = new LinkedList<TypeInfo>();
        for (UnresolvedTypeInfo unresolvedParameterType : unresolvedParameterTypes) {

            if (unresolvedParameterType instanceof UnresolvedEntityUsage) {
                final TypeInfo parameterType = NameResolver.resolveEntityReference(
                        (UnresolvedEntityUsage) unresolvedParameterType, usingClass, usingMethod,
                        classInfoManager, fieldInfoManager, methodInfoManager, resolvedCache);
                parameterTypes.add(parameterType);
            }
        }

        // メソッド呼び出し(a())がフィールド使用(b)にくっついている場合 (b.a())
        if (unresolvedMethodOwnerClassType instanceof UnresolvedEntityUsage) {

            // (b)のクラス定義を取得
            final TypeInfo methodOwnerClassType = NameResolver.resolveEntityReference(
                    (UnresolvedEntityUsage) unresolvedMethodOwnerClassType, usingClass,
                    usingMethod, classInfoManager, fieldInfoManager, methodInfoManager,
                    resolvedCache);

            // 利用可能なメソッド一覧を取得
            final List<TargetMethodInfo> availableMethods = NameResolver.getAvailableMethods(
                    (TargetClassInfo) methodOwnerClassType, usingClass);

            // 利用可能なメソッドから，未解決メソッドと一致するものを検索
            // メソッド名，引数の型のリストを用いて，このメソッドの呼び出しであるかどうかを判定
            for (TargetMethodInfo availableMethod : availableMethods) {

                // 呼び出し可能なメソッドが見つかった場合
                if (availableMethod.canCalledWith(methodName, parameterTypes)) {
                    usingMethod.addCallee(availableMethod);
                    availableMethod.addCaller(usingMethod);
                    
                    // 解決済みキャッシュにに登録
                    resolvedCache.put(methodCall, availableMethod.getReturnType());
                    
                    return availableMethod.getReturnType();
                }
            }

            // TODO 見つからなかった場合の処理が必要
            err.println("resolveMethodCall : TODO1");
            
            
            // メソッド呼び出し(a())がメソッド呼び出し(c())にくっついている場合(c().a())
        } else if (unresolvedMethodOwnerClassType instanceof UnresolvedMethodCall) {

            // (c)のクラス定義を取得
            final TypeInfo methodOwnerClassType = NameResolver.resolveMethodCall(
                    (UnresolvedMethodCall) unresolvedMethodOwnerClassType, usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager, resolvedCache);

            // 利用可能なメソッド一覧を取得
            final List<TargetMethodInfo> availableMethods = NameResolver.getAvailableMethods(
                    (TargetClassInfo) methodOwnerClassType, usingClass);

            // 利用可能なメソッドから，未解決メソッドと一致するものを検索
            // メソッド名，引数の型のリストを用いて，このメソッドの呼び出しであるかどうかを判定
            for (TargetMethodInfo availableMethod : availableMethods) {

                // 呼び出し可能なメソッドが見つかった場合
                if (availableMethod.canCalledWith(methodName, parameterTypes)) {
                    usingMethod.addCallee(availableMethod);
                    availableMethod.addCaller(usingMethod);
                    
                    // 解決済みキャッシュにに登録
                    resolvedCache.put(methodCall, availableMethod.getReturnType());
                    
                    return availableMethod.getReturnType();
                }
            }

            // TODO 見つからなかった場合の処理が必要
            err.println("resolveMethodCall : TODO2");
            
            // メソッド呼び出し(a())が自オブジェクトにくっついている場合(a or this.a or super.a )
        } else if (unresolvedMethodOwnerClassType instanceof UnresolvedReferenceTypeInfo) {

            // 利用可能なメソッド一覧を取得
            final List<TargetMethodInfo> availableMethods = NameResolver
                    .getAvailableMethods(usingClass);

            // 利用可能なメソッドから，未解決メソッドと一致するものを検索
            // メソッド名，引数の型のリストを用いて，このメソッドの呼び出しであるかどうかを判定
            for (TargetMethodInfo availableMethod : availableMethods) {

                // 呼び出し可能なメソッドが見つかった場合
                if (availableMethod.canCalledWith(methodName, parameterTypes)) {
                    usingMethod.addCallee(availableMethod);
                    availableMethod.addCaller(usingMethod);
                    
                    // 解決済みキャッシュにに登録
                    resolvedCache.put(methodCall, availableMethod.getReturnType());
                    
                    return availableMethod.getReturnType();
                }
            }

            err.println("resolveMethodCall : TODO3");
            // TODO 見つからなかった場合の処理が必要
            // ExternalClassInfo を継承していて，そこで定義されているメソッドを使用している場合など
        }

        throw new IllegalArgumentException(methodCall.toString() + " is wrong!");
    }

    /**
     * 未解決フィールド情報から，対応するFieldInfo を返す．該当するメソッドがない場合は， IllegalArgumentException が投げられる
     * 
     * @param unresolvedFieldInfo 未解決フィールド情報
     * @param classInfoManager 用いるクラスマネージャ
     * @return 対応する FieldInfo
     */
    public static TargetFieldInfo resolveFieldInfo(final UnresolvedFieldInfo unresolvedFieldInfo,
            final ClassInfoManager classInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == unresolvedFieldInfo) || (null == classInfoManager)) {
            throw new NullPointerException();
        }

        // 所有クラスを取得，取得したクラスが外部クラスである場合は，引数の情報がおかしい
        UnresolvedClassInfo unresolvedOwnerClass = unresolvedFieldInfo.getOwnerClass();
        ClassInfo ownerClass = NameResolver
                .resolveClassInfo(unresolvedOwnerClass, classInfoManager);
        if (!(ownerClass instanceof TargetClassInfo)) {
            throw new IllegalArgumentException(unresolvedFieldInfo.toString() + " is wrong!");
        }

        // UnresolvedFieldInfo からフィールド名，型名を取得
        String fieldName = unresolvedFieldInfo.getName();
        UnresolvedTypeInfo unresolvedFieldType = unresolvedFieldInfo.getType();
        TypeInfo fieldType = NameResolver.resolveTypeInfo(unresolvedFieldType, classInfoManager);

        for (TargetFieldInfo fieldInfo : ((TargetClassInfo) ownerClass).getDefinedFields()) {

            // フィールド名が違う場合は，該当フィールドではない
            if (!fieldName.equals(fieldInfo.getName())) {
                continue;
            }

            // フィールドの型が違う場合は，該当フィールドではない
            if (!fieldType.equals(fieldInfo.getType())) {
                continue;
            }

            return fieldInfo;
        }

        throw new IllegalArgumentException(unresolvedFieldInfo.toString() + " is wrong!");
    }

    /**
     * 未解決メソッド情報から，対応するMethodInfo を返す．該当するメソッドがない場合は IllegalArgumentException が投げられる
     * 
     * @param unresolvedMethodInfo 未解決メソッド情報
     * @param classInfoManager 用いるクラスマネージャ
     * @return 対応する MethodInfo
     */
    public static TargetMethodInfo resolveMethodInfo(
            final UnresolvedMethodInfo unresolvedMethodInfo, final ClassInfoManager classInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == unresolvedMethodInfo) || (null == classInfoManager)) {
            throw new NullPointerException();
        }

        // UnresolvedMethodInfo から所有クラスを取得，取得したクラスが外部クラスである場合は，引数の情報がおかしい
        UnresolvedClassInfo unresolvedOwnerClass = unresolvedMethodInfo.getOwnerClass();
        ClassInfo ownerClass = NameResolver
                .resolveClassInfo(unresolvedOwnerClass, classInfoManager);
        if (!(ownerClass instanceof TargetClassInfo)) {
            throw new IllegalArgumentException(unresolvedMethodInfo.toString() + " is wrong!");
        }

        // Unresolved メソッド名，引数を取得
        String methodName = unresolvedMethodInfo.getMethodName();
        List<UnresolvedParameterInfo> unresolvedParameterInfos = unresolvedMethodInfo
                .getParameterInfos();

        for (TargetMethodInfo methodInfo : ((TargetClassInfo) ownerClass).getDefinedMethods()) {

            // メソッド名が違う場合は，該当メソッドではない
            if (!methodName.equals(methodInfo.getMethodName())) {
                continue;
            }

            // 引数の数が違う場合は，該当メソッドではない
            List<ParameterInfo> typeInfos = methodInfo.getParameters();
            if (unresolvedParameterInfos.size() != typeInfos.size()) {
                continue;
            }

            // 全ての引数の型をチェック，1つでも異なる場合は，該当メソッドではない
            Iterator<UnresolvedParameterInfo> unresolvedParameterIterator = unresolvedParameterInfos
                    .iterator();
            Iterator<ParameterInfo> parameterInfoIterator = typeInfos.iterator();
            boolean same = true;
            while (unresolvedParameterIterator.hasNext() && parameterInfoIterator.hasNext()) {
                UnresolvedParameterInfo unresolvedParameterInfo = unresolvedParameterIterator
                        .next();
                UnresolvedTypeInfo unresolvedTypeInfo = unresolvedParameterInfo.getType();
                TypeInfo typeInfo = NameResolver.resolveTypeInfo(unresolvedTypeInfo,
                        classInfoManager);
                ParameterInfo parameterInfo = parameterInfoIterator.next();
                if (!typeInfo.equals(parameterInfo.getType())) {
                    same = false;
                    break;
                }
            }
            if (same) {
                return methodInfo;
            }
        }

        throw new IllegalArgumentException(unresolvedMethodInfo.toString() + " is wrong!");
    }

    /**
     * 未解決クラス情報から，該当する ClassInfo を返す．該当するクラスがない場合は IllegalArgumentException が投げられる
     * 
     * @param unresolvedClassInfo 未解決クラス情報
     * @param classInfoManager 用いるクラスマネージャ
     * @return 該当する ClassInfo
     */
    public static TargetClassInfo resolveClassInfo(final UnresolvedClassInfo unresolvedClassInfo,
            final ClassInfoManager classInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == unresolvedClassInfo) || (null == classInfoManager)) {
            throw new NullPointerException();
        }

        // 完全限定名を取得し，ClassInfo を取得
        String[] fullQualifiedName = unresolvedClassInfo.getFullQualifiedName();
        ClassInfo classInfo = classInfoManager.getClassInfo(fullQualifiedName);

        // UnresolvedClassInfo のオブジェクトは registClassInfo メソッドにより，全て登録済みのはずなので，
        // null が返ってきた場合は，不正
        if (null == classInfo) {
            throw new IllegalArgumentException(unresolvedClassInfo.toString() + " is wrong!");
        }
        // registClassInfoにより登録されたクラス情報は TargetClassInfo であるべき
        if (!(classInfo instanceof TargetClassInfo)) {
            throw new IllegalArgumentException(unresolvedClassInfo.toString() + " is wrong!");
        }

        return (TargetClassInfo) classInfo;
    }

    /**
     * 「現在のクラス」で利用可能なフィールド一覧を返す．
     * ここで，「利用可能なフィールド」とは，「現在のクラス」で定義されているフィールド，及びその親クラスで定義されているフィールドのうち子クラスからアクセスが可能なフィールドである．
     * 利用可能なフィールドは List に格納されている． リストの先頭から優先順位の高いフィールド（つまり，クラス階層において下位のクラスに定義されているフィールド）が格納されている．
     * 
     * @param thisClass 現在のクラス
     * @return 利用可能なフィールド一覧
     */
    public static List<TargetFieldInfo> getAvailableFields(final TargetClassInfo thisClass) {

        if (null == thisClass) {
            throw new NullPointerException();
        }

        final List<TargetFieldInfo> availableFields = new LinkedList<TargetFieldInfo>();

        // このクラスで定義されているフィールド一覧を取得する
        availableFields.addAll(thisClass.getDefinedFields());

        // 親クラスで定義されており，このクラスからアクセスが可能なフィールドを取得
        for (ClassInfo superClass : thisClass.getSuperClasses()) {

            if (superClass instanceof TargetClassInfo) {
                final List<TargetFieldInfo> availableFieldsDefinedInSuperClasses = NameResolver
                        .getAvailableFieldsInSubClasses((TargetClassInfo) superClass);
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
    public static List<TargetMethodInfo> getAvailableMethods(final TargetClassInfo thisClass) {

        if (null == thisClass) {
            throw new NullPointerException();
        }

        final List<TargetMethodInfo> availableMethods = new LinkedList<TargetMethodInfo>();

        // このクラスで定義されているメソッド一覧を取得する
        availableMethods.addAll(thisClass.getDefinedMethods());

        // 親クラスで定義されており，このクラスからアクセスが可能なメソッドを取得
        for (ClassInfo superClass : thisClass.getSuperClasses()) {

            if (superClass instanceof TargetClassInfo) {
                final List<TargetMethodInfo> availableMethodsDefinedInSuperClasses = NameResolver
                        .getAvailableMethodsInSubClasses((TargetClassInfo) superClass);
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

        final List<TargetFieldInfo> availableFields = new LinkedList<TargetFieldInfo>();

        // このクラスで定義されているフィールドのうち，使用するクラスで利用可能なフィールドを取得する
        // 2つのクラスが同じ名前空間を持っている場合
        if (usedClass.getNamespace().equals(usingClass.getNamespace())) {

            for (TargetFieldInfo field : usedClass.getDefinedFields()) {
                if (field.isNamespaceVisible()) {
                    availableFields.add(field);
                }
            }

            // 違う名前空間を持っている場合
        } else {
            for (TargetFieldInfo field : usedClass.getDefinedFields()) {
                if (field.isPublicVisible()) {
                    availableFields.add(field);
                }
            }
        }

        // 親クラスで定義されており，子クラスからアクセスが可能なフィールドを取得
        // List に入れるので，親クラスのフィールドの後に add しなければならない
        for (ClassInfo superClassInfo : usedClass.getSuperClasses()) {

            if (superClassInfo instanceof TargetClassInfo) {
                final List<TargetFieldInfo> availableFieldsDefinedInSuperClasses = NameResolver
                        .getAvailableFieldsInSubClasses((TargetClassInfo) superClassInfo);
                availableFields.addAll(availableFieldsDefinedInSuperClasses);
            }
        }

        return Collections.unmodifiableList(availableFields);
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

        final List<TargetMethodInfo> availableMethods = new LinkedList<TargetMethodInfo>();

        // このクラスで定義されているメソッドのうち，使用するクラスで利用可能なメソッドを取得する
        // 2つのクラスが同じ名前空間を持っている場合
        if (usedClass.getNamespace().equals(usingClass.getNamespace())) {

            for (TargetMethodInfo method : usedClass.getDefinedMethods()) {
                if (method.isNamespaceVisible()) {
                    availableMethods.add(method);
                }
            }

            // 違う名前空間を持っている場合
        } else {
            for (TargetMethodInfo method : usedClass.getDefinedMethods()) {
                if (method.isPublicVisible()) {
                    availableMethods.add(method);
                }
            }
        }

        // 親クラスで定義されており，子クラスからアクセスが可能なメソッドを取得
        // List に入れるので，親クラスのメソッドの後に add しなければならない
        for (ClassInfo superClassInfo : usedClass.getSuperClasses()) {

            if (superClassInfo instanceof TargetClassInfo) {
                final List<TargetMethodInfo> availableMethodsDefinedInSuperClasses = NameResolver
                        .getAvailableMethodsInSubClasses((TargetClassInfo) superClassInfo);
                availableMethods.addAll(availableMethodsDefinedInSuperClasses);
            }
        }

        return Collections.unmodifiableList(availableMethods);
    }

    /**
     * 「使用されるクラス」の子クラス使用される場合に，利用可能なフィールド一覧を返す．
     * ここで，「利用可能なフィールド」とは，「使用されるクラス」もしくはその親クラスで定義されているフィールドのうち，子クラスからアクセスが可能なフィールドである．
     * 子クラスで利用可能なフィールド一覧は List に格納されている．
     * リストの先頭から優先順位の高いフィールド（つまり，クラス階層において下位のクラスに定義されているフィールド）が格納されている．
     * 
     * @param usedClass 使用されるクラス
     * @return 利用可能なフィールド一覧
     */
    public static List<TargetFieldInfo> getAvailableFieldsInSubClasses(
            final TargetClassInfo usedClass) {

        if (null == usedClass) {
            throw new NullPointerException();
        }

        final List<TargetFieldInfo> availableFields = new LinkedList<TargetFieldInfo>();

        // このクラスで定義されており，子クラスからアクセス可能なフィールドを取得
        for (TargetFieldInfo field : usedClass.getDefinedFields()) {
            if (field.isInheritanceVisible()) {
                availableFields.add(field);
            }
        }

        // 親クラスで定義されており，子クラスからアクセスが可能なフィールドを取得
        // List に入れるので，親クラスのフィールドの後に add しなければならない
        for (ClassInfo superClassInfo : usedClass.getSuperClasses()) {

            if (superClassInfo instanceof TargetClassInfo) {
                final List<TargetFieldInfo> availableFieldsDefinedInSuperClasses = NameResolver
                        .getAvailableFieldsInSubClasses((TargetClassInfo) superClassInfo);
                availableFields.addAll(availableFieldsDefinedInSuperClasses);
            }
        }

        return Collections.unmodifiableList(availableFields);
    }

    /**
     * 「使用されるクラス」の子クラス使用される場合に，利用可能なメソッド一覧を返す．
     * ここで，「利用可能なメソッド」とは，「使用されるメソッド」もしくはその親クラスで定義されているメソッドのうち，子クラスからアクセスが可能なメソッドである．
     * 子クラスで利用可能なメソッド一覧は List に格納されている．
     * リストの先頭から優先順位の高いメソッド（つまり，クラス階層において下位のクラスに定義されているメソッド）が格納されている．
     * 
     * @param usedClass 使用されるクラス
     * @return 利用可能なメソッド一覧
     */
    public static List<TargetMethodInfo> getAvailableMethodsInSubClasses(
            final TargetClassInfo usedClass) {

        if (null == usedClass) {
            throw new NullPointerException();
        }

        final List<TargetMethodInfo> availableMethods = new LinkedList<TargetMethodInfo>();

        // このクラスで定義されており，子クラスからアクセス可能なメソッドを取得
        for (TargetMethodInfo method : usedClass.getDefinedMethods()) {
            if (method.isInheritanceVisible()) {
                availableMethods.add(method);
            }
        }

        // 親クラスで定義されており，子クラスからアクセスが可能なメソッドを取得
        // List に入れるので，親クラスのメソッドの後に add しなければならない
        for (ClassInfo superClassInfo : usedClass.getSuperClasses()) {

            if (superClassInfo instanceof TargetClassInfo) {
                final List<TargetMethodInfo> availableMethodsDefinedInSuperClasses = NameResolver
                        .getAvailableMethodsInSubClasses((TargetClassInfo) superClassInfo);
                availableMethods.addAll(availableMethodsDefinedInSuperClasses);
            }
        }

        return Collections.unmodifiableList(availableMethods);
    }

    /**
     * 出力メッセージ出力用のプリンタ
     */
    private static final MessagePrinter out = new DefaultMessagePrinter(new MessageSource() {
        public String getMessageSourceName() {
            return "NameResolver";
        }
    }, MESSAGE_TYPE.OUT);

    /**
     * エラーメッセージ出力用のプリンタ
     */
    private static final MessagePrinter err = new DefaultMessagePrinter(new MessageSource() {
        public String getMessageSourceName() {
            return "NameResolver";
        }
    }, MESSAGE_TYPE.ERROR);
}
