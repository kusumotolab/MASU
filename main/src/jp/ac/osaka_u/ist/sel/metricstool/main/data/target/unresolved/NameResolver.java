package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Members;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.NullTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetInnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VoidTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;


/**
 * 未解決型情報を解決するためのユーティリティクラス
 * 
 * @author y-higo
 * 
 */
public final class NameResolver {

    /**
     * 未解決型情報（UnresolvedTypeInfo）から解決済み型情報（TypeInfo）を返す． 対応する解決済み型情報がない場合は UnknownTypeInfo を返す．
     * 
     * @param unresolvedTypeInfo 名前解決したい型情報
     * @param usingClass この未解決型が存在しているクラス
     * @param usingMethod この未解決型が存在しているメソッド，メソッド外である場合は null を与える
     * @param classInfoManager 型解決に用いるクラス情報データベース
     * @param fieldInfoManager 型解決に用いるフィールド情報データベース
     * @param methodInfoManager 型解決に用いるメソッド情報データベース
     * @param resolvCache 解決済みUnresolvedTypeInfoのキャッシュ
     * @return 名前解決された型情報
     */
    public static TypeInfo resolveTypeInfo(final UnresolvedTypeInfo unresolvedTypeInfo,
            final TargetClassInfo usingClass, final TargetMethodInfo usingMethod,
            final ClassInfoManager classInfoManager, final FieldInfoManager fieldInfoManager,
            final MethodInfoManager methodInfoManager,
            final Map<UnresolvedTypeInfo, TypeInfo> resolvedCache) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == unresolvedTypeInfo) {
            throw new NullPointerException();
        }

        // 既に解決済みであれば，そこから型を取得
        if ((null != resolvedCache) && resolvedCache.containsKey(unresolvedTypeInfo)) {
            final TypeInfo type = resolvedCache.get(unresolvedTypeInfo);
            return type;
        }

        // 未解決プリミティブ型の場合
        if (unresolvedTypeInfo instanceof PrimitiveTypeInfo) {
            return (PrimitiveTypeInfo) unresolvedTypeInfo;

            // 未解決void型の場合
        } else if (unresolvedTypeInfo instanceof VoidTypeInfo) {
            return (VoidTypeInfo) unresolvedTypeInfo;

        } else if (unresolvedTypeInfo instanceof NullTypeInfo) {
            return (NullTypeInfo) unresolvedTypeInfo;

            // 未解決参照型の場合
        } else if (unresolvedTypeInfo instanceof UnresolvedReferenceTypeInfo) {

            final String[] referenceName = ((UnresolvedReferenceTypeInfo) unresolvedTypeInfo)
                    .getReferenceName();

            // 参照名が完全限定名であるとして検索
            {
                final ClassInfo classInfo = classInfoManager.getClassInfo(referenceName);
                if (null != classInfo) {
                    return classInfo;
                }
            }

            // 利用可能な名前空間から，型名を探す
            {
                for (AvailableNamespaceInfo availableNamespace : ((UnresolvedReferenceTypeInfo) unresolvedTypeInfo)
                        .getAvailableNamespaces()) {

                    // 名前空間名.* となっている場合
                    if (availableNamespace.isAllClasses()) {
                        final String[] namespace = availableNamespace.getNamespace();

                        // 名前空間の下にある各クラスに対して
                        for (ClassInfo classInfo : classInfoManager.getClassInfos(namespace)) {
                            final String className = classInfo.getClassName();

                            // クラス名と参照名の先頭が等しい場合は，そのクラス名が参照先であると決定する
                            if (className.equals(referenceName[0])) {

                                // キャッシュ用ハッシュテーブルがる場合はキャッシュを追加
                                if (null != resolvedCache) {
                                    resolvedCache.put(unresolvedTypeInfo, classInfo);
                                }

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
                            final ClassInfo classInfo = classInfoManager
                                    .getClassInfo(fullQualifiedName);

                            // クラスが見つかった場合は，それを返す
                            if (null != classInfo) {
                                return classInfo;
                            }
                        }
                    }
                }
            }

            // 見つからなかった場合は，UknownTypeInfo を返す
            return UnknownTypeInfo.getInstance();

            // 未解決配列型の場合
        } else if (unresolvedTypeInfo instanceof UnresolvedArrayTypeInfo) {

            final UnresolvedTypeInfo unresolvedElementType = ((UnresolvedArrayTypeInfo) unresolvedTypeInfo)
                    .getElementType();
            final int dimension = ((UnresolvedArrayTypeInfo) unresolvedTypeInfo).getDimension();

            final TypeInfo elementType = NameResolver.resolveTypeInfo(unresolvedElementType,
                    usingClass, usingMethod, classInfoManager, fieldInfoManager, methodInfoManager,
                    resolvedCache);
            assert elementType != null : "resolveTypeInfo returned null!";

            // 要素の型が不明のときは UnnownTypeInfo を返す
            if (elementType instanceof UnknownTypeInfo) {
                return UnknownTypeInfo.getInstance();

                // 要素の型が解決できた場合はその配列型を作成し返す
            } else {
                final ArrayTypeInfo arrayType = ArrayTypeInfo.getType(elementType, dimension);
                return arrayType;
            }

            // 未解決クラス情報の場合
        } else if (unresolvedTypeInfo instanceof UnresolvedClassInfo) {

            final TypeInfo classInfo = (ClassInfo) ((UnresolvedClassInfo) unresolvedTypeInfo)
                    .getResolvedInfo();
            return classInfo;

            // 未解決フィールド使用の場合
        } else if (unresolvedTypeInfo instanceof UnresolvedFieldUsage) {

            final TypeInfo classInfo = NameResolver.resolveFieldReference(
                    (UnresolvedFieldUsage) unresolvedTypeInfo, usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager, resolvedCache);
            return classInfo;

            // 未解決メソッド呼び出しの場合
        } else if (unresolvedTypeInfo instanceof UnresolvedMethodCall) {

            // (c)のクラス定義を取得
            final TypeInfo classInfo = NameResolver.resolveMethodCall(
                    (UnresolvedMethodCall) unresolvedTypeInfo, usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager, resolvedCache);
            return classInfo;

            // 未解決エンティティ使用の場合
        } else if (unresolvedTypeInfo instanceof UnresolvedEntityUsage) {

            // エンティティのクラス定義を取得
            final TypeInfo classInfo = NameResolver.resolveEntityUsage(
                    (UnresolvedEntityUsage) unresolvedTypeInfo, usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager, resolvedCache);
            return classInfo;

            // 未解決配列使用の場合
        } else if (unresolvedTypeInfo instanceof UnresolvedArrayElementUsage) {

            final TypeInfo classInfo = NameResolver.resolveArrayElementUsage(
                    (UnresolvedArrayElementUsage) unresolvedTypeInfo, usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager, resolvedCache);
            return classInfo;

            // それ以外の型の場合はエラー
        } else {
            throw new IllegalArgumentException(unresolvedTypeInfo.toString()
                    + " is a wrong object!");
        }
    }

    /**
     * 未解決フィールド参照を解決し，フィールド参照が行われているメソッドに登録する．また，フィールドの型を返す．
     * 
     * @param fieldReference 未解決フィールド参照
     * @param usingClass フィールド参照が行われているクラス
     * @param usingMethod フィールド参照が行われているメソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     * @param resolvedCache 解決済みUnresolvedTypeInfoのキャッシュ
     * @return 解決済みフィールド参照の型（つまり，フィールドの型）
     */
    public static TypeInfo resolveFieldReference(final UnresolvedFieldUsage fieldReference,
            final TargetClassInfo usingClass, final TargetMethodInfo usingMethod,
            final ClassInfoManager classInfoManager, final FieldInfoManager fieldInfoManager,
            final MethodInfoManager methodInfoManager,
            final Map<UnresolvedTypeInfo, TypeInfo> resolvedCache) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == fieldReference) || (null == usingClass) || (null == usingMethod)
                || (null == classInfoManager) || (null == fieldInfoManager)
                || (null == methodInfoManager) || (null == resolvedCache)) {
            throw new NullPointerException();
        }

        // 既に解決済みであれば，そこから型を取得
        if (resolvedCache.containsKey(fieldReference)) {
            final TypeInfo type = resolvedCache.get(fieldReference);
            return type;
        }

        // フィールド名を取得
        final String fieldName = fieldReference.getFieldName();

        // 親の型を解決
        final UnresolvedTypeInfo unresolvedFieldOwnerClassType = fieldReference.getOwnerClassType();
        final TypeInfo fieldOwnerClassType = NameResolver.resolveTypeInfo(
                unresolvedFieldOwnerClassType, usingClass, usingMethod, classInfoManager,
                fieldInfoManager, methodInfoManager, resolvedCache);
        assert fieldOwnerClassType != null : "resolveTypeInfo returned null!";

        // -----ここから親のTypeInfo に応じて処理を分岐
        // 親が解決できなかった場合はどうしようもない
        if (fieldOwnerClassType instanceof UnknownTypeInfo) {

            // 見つからなかった処理を行う
            usingMethod.addUnresolvedUsage(fieldReference);

            // 解決済みキャッシュに登録
            resolvedCache.put(fieldReference, UnknownTypeInfo.getInstance());

            return UnknownTypeInfo.getInstance();

            // 親が対象クラス(TargetClassInfo)だった場合
        } else if (fieldOwnerClassType instanceof TargetClassInfo) {

            // まずは利用可能なフィールドから検索
            {
                // 利用可能なフィールド一覧を取得
                final List<TargetFieldInfo> availableFields = NameResolver.getAvailableFields(
                        (TargetClassInfo) fieldOwnerClassType, usingClass);

                // 利用可能なフィールドを，未解決フィールド名で検索
                for (TargetFieldInfo availableField : availableFields) {

                    // 一致するフィールド名が見つかった場合
                    if (fieldName.equals(availableField.getName())) {
                        usingMethod.addReferencee(availableField);
                        availableField.addReferencer(usingMethod);

                        // 解決済みキャッシュに登録
                        resolvedCache.put(fieldReference, availableField.getType());

                        return availableField.getType();
                    }
                }
            }

            // 利用可能なフィールドが見つからなかった場合は，外部クラスである親クラスがあるはず
            // そのクラスの変数を使用しているとみなす
            {
                final ExternalClassInfo externalSuperClass = NameResolver
                        .getExternalSuperClass((TargetClassInfo) fieldOwnerClassType);
                if (!(fieldOwnerClassType instanceof TargetInnerClassInfo)
                        && (null != externalSuperClass)) {

                    final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(fieldName,
                            externalSuperClass);
                    usingMethod.addReferencee(fieldInfo);
                    fieldInfo.addReferencer(usingMethod);
                    fieldInfoManager.add(fieldInfo);

                    // 解決済みキャッシュに登録
                    resolvedCache.put(fieldReference, fieldInfo.getType());

                    // 外部クラスに新規で外部変数(ExternalFieldInfo)を追加したので型は不明．
                    return fieldInfo.getType();
                }
            }

            // 見つからなかった処理を行う
            {
                err.println("Can't resolve field reference : " + fieldReference.getFieldName());

                usingMethod.addUnresolvedUsage(fieldReference);

                // 解決済みキャッシュに登録
                resolvedCache.put(fieldReference, UnknownTypeInfo.getInstance());

                return UnknownTypeInfo.getInstance();
            }

            // 親が外部クラス（ExternalClassInfo）だった場合
        } else if (fieldOwnerClassType instanceof ExternalClassInfo) {

            final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(fieldName,
                    (ExternalClassInfo) fieldOwnerClassType);
            usingMethod.addReferencee(fieldInfo);
            fieldInfo.addReferencer(usingMethod);
            fieldInfoManager.add(fieldInfo);

            // 解決済みキャッシュに登録
            resolvedCache.put(fieldReference, fieldInfo.getType());

            // 外部クラスに新規で外部変数(ExternalFieldInfo)を追加したので型は不明．
            return fieldInfo.getType();

        } else if (fieldOwnerClassType instanceof ArrayTypeInfo) {

            // TODO ここは言語依存にするしかないのか？ 配列.length など

            // Java 言語で フィールド名が length だった場合は int 型を返す
            if (Settings.getLanguage().equals(LANGUAGE.JAVA) && fieldName.equals("length")) {

                resolvedCache.put(fieldReference, PrimitiveTypeInfo.INT);
                return PrimitiveTypeInfo.INT;
            }
        }

        assert false : "Here shouldn't be reached!";
        return UnknownTypeInfo.getInstance();
    }

    /**
     * 未解決フィールド代入を解決し，フィールド代入が行われているメソッドに登録する．また，フィールドの型を返す．
     * 
     * @param fieldAssignment 未解決フィールド代入
     * @param usingClass フィールド代入が行われているクラス
     * @param usingMethod フィールド代入が行われているメソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     * @param resolvedCache 解決済みUnresolvedTypeInfoのキャッシュ
     * @return 解決済みフィールド代入の型（つまり，フィールドの型）
     */
    public static TypeInfo resolveFieldAssignment(final UnresolvedFieldUsage fieldAssignment,
            final TargetClassInfo usingClass, final TargetMethodInfo usingMethod,
            final ClassInfoManager classInfoManager, final FieldInfoManager fieldInfoManager,
            final MethodInfoManager methodInfoManager,
            final Map<UnresolvedTypeInfo, TypeInfo> resolvedCache) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == fieldAssignment) || (null == usingClass) || (null == usingMethod)
                || (null == classInfoManager) || (null == fieldInfoManager)
                || (null == methodInfoManager) || (null == resolvedCache)) {
            throw new NullPointerException();
        }

        // 既に解決済みであれば，そこから型を取得
        if (resolvedCache.containsKey(fieldAssignment)) {
            final TypeInfo type = resolvedCache.get(fieldAssignment);
            return type;
        }

        // フィールド名を取得
        final String fieldName = fieldAssignment.getFieldName();

        // 親の型を解決
        final UnresolvedTypeInfo unresolvedFieldOwnerClassType = fieldAssignment
                .getOwnerClassType();
        final TypeInfo fieldOwnerClassType = NameResolver.resolveTypeInfo(
                unresolvedFieldOwnerClassType, usingClass, usingMethod, classInfoManager,
                fieldInfoManager, methodInfoManager, resolvedCache);
        assert fieldOwnerClassType != null : "resolveTypeInfo returned null!";

        // -----ここから親のTypeInfo に応じて処理を分岐
        // 親が解決できなかった場合はどうしようもない
        if (fieldOwnerClassType instanceof UnknownTypeInfo) {

            // 見つからなかった処理を行う
            usingMethod.addUnresolvedUsage(fieldAssignment);

            // 解決済みキャッシュに登録
            resolvedCache.put(fieldAssignment, UnknownTypeInfo.getInstance());

            return UnknownTypeInfo.getInstance();

            // 親が対象クラス(TargetClassInfo)だった場合
        } else if (fieldOwnerClassType instanceof TargetClassInfo) {

            // まずは利用可能なフィールドから検索
            {
                // 利用可能なフィールド一覧を取得
                final List<TargetFieldInfo> availableFields = NameResolver.getAvailableFields(
                        (TargetClassInfo) fieldOwnerClassType, usingClass);

                // 利用可能なフィールド一覧を，未解決フィールド名で検索
                for (TargetFieldInfo availableField : availableFields) {

                    // 一致するフィールド名が見つかった場合
                    if (fieldName.equals(availableField.getName())) {
                        usingMethod.addAssignmentee(availableField);
                        availableField.addAssignmenter(usingMethod);

                        // 解決済みキャッシュにに登録
                        resolvedCache.put(fieldAssignment, availableField.getType());

                        return availableField.getType();
                    }
                }
            }

            // 利用可能なフィールドが見つからなかった場合は，外部クラスである親クラスがあるはず．
            // そのクラスの変数を使用しているとみなす
            {
                final ExternalClassInfo externalSuperClass = NameResolver
                        .getExternalSuperClass((TargetClassInfo) fieldOwnerClassType);
                if (!(fieldOwnerClassType instanceof TargetInnerClassInfo)
                        && (null != externalSuperClass)) {

                    final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(fieldName,
                            externalSuperClass);
                    usingMethod.addAssignmentee(fieldInfo);
                    fieldInfo.addAssignmenter(usingMethod);
                    fieldInfoManager.add(fieldInfo);

                    // 解決済みキャッシュに登録
                    resolvedCache.put(fieldAssignment, fieldInfo.getType());

                    // 外部クラスに新規で外部変数（ExternalFieldInfo）を追加したので型は不明
                    return fieldInfo.getType();
                }
            }

            // 見つからなかった処理を行う
            {
                err.println("Can't resolve field assignment : " + fieldAssignment.getFieldName());

                usingMethod.addUnresolvedUsage(fieldAssignment);

                // 解決済みキャッシュに登録
                resolvedCache.put(fieldAssignment, UnknownTypeInfo.getInstance());

                return UnknownTypeInfo.getInstance();
            }

            // 親が外部クラス（ExternalClassInfo）だった場合
        } else if (fieldOwnerClassType instanceof ExternalClassInfo) {

            final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(fieldName,
                    (ExternalClassInfo) fieldOwnerClassType);
            usingMethod.addAssignmentee(fieldInfo);
            fieldInfo.addAssignmenter(usingMethod);
            fieldInfoManager.add(fieldInfo);

            // 解決済みキャッシュに登録
            resolvedCache.put(fieldAssignment, fieldInfo.getType());

            // 外部クラスに新規で外部変数(ExternalFieldInfo)を追加したので型は不明．
            return fieldInfo.getType();
        }

        assert false : "Here shouldn't be reached!";
        return UnknownTypeInfo.getInstance();
    }

    /**
     * 未解決メソッド呼び出し情報を解決し，メソッド呼び出し処理が行われているメソッドに登録する．また，メソッドの返り値の型を返す．
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

        // メソッドのシグネチャを取得
        final String methodName = methodCall.getMethodName();
        final boolean constructor = methodCall.isConstructor();
        final List<UnresolvedTypeInfo> unresolvedParameterTypes = methodCall.getParameterTypes();

        // メソッドの未解決引数を解決
        final List<TypeInfo> parameterTypes = new LinkedList<TypeInfo>();
        for (UnresolvedTypeInfo unresolvedParameterType : unresolvedParameterTypes) {
            TypeInfo parameterType = NameResolver.resolveTypeInfo(unresolvedParameterType,
                    usingClass, usingMethod, classInfoManager, fieldInfoManager, methodInfoManager,
                    resolvedCache);
            assert parameterType != null : "resolveTypeInfo returned null!";
            if (parameterType instanceof UnknownTypeInfo) {
                if (unresolvedParameterType instanceof UnresolvedReferenceTypeInfo) {
                    parameterType = NameResolver
                            .createExternalClassInfo((UnresolvedReferenceTypeInfo) unresolvedParameterType);
                    classInfoManager.add((ExternalClassInfo) parameterType);
                } else if (unresolvedParameterType instanceof UnresolvedArrayTypeInfo) {
                    final UnresolvedTypeInfo unresolvedElementType = ((UnresolvedArrayTypeInfo) unresolvedParameterType)
                            .getElementType();
                    final int dimension = ((UnresolvedArrayTypeInfo) unresolvedParameterType)
                            .getDimension();
                    final TypeInfo elementType = NameResolver
                            .createExternalClassInfo((UnresolvedReferenceTypeInfo) unresolvedElementType);
                    classInfoManager.add((ExternalClassInfo) elementType);
                    parameterType = ArrayTypeInfo.getType(elementType, dimension);
                }
            }
            parameterTypes.add(parameterType);
        }

        // 親の型を解決
        final UnresolvedTypeInfo unresolvedMethodOwnerClassType = methodCall.getOwnerClassType();
        final TypeInfo methodOwnerClassType = NameResolver.resolveTypeInfo(
                unresolvedMethodOwnerClassType, usingClass, usingMethod, classInfoManager,
                fieldInfoManager, methodInfoManager, resolvedCache);
        assert methodOwnerClassType != null : "resolveTypeInfo returned null!";

        // -----ここから親のTypeInfo に応じて処理を分岐
        // 親が解決できなかった場合はどうしようもない
        if (methodOwnerClassType instanceof UnknownTypeInfo) {

            // 見つからなかった処理を行う
            usingMethod.addUnresolvedUsage(methodCall);

            // 解決済みキャッシュに登録
            resolvedCache.put(methodCall, UnknownTypeInfo.getInstance());

            return UnknownTypeInfo.getInstance();

            // 親が対象クラス(TargetClassInfo)だった場合
        } else if (methodOwnerClassType instanceof TargetClassInfo) {

            // まずは利用可能なメソッドから検索
            {
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
            }

            // 利用可能なメソッドが見つからなかった場合は，外部クラスである親クラスがあるはず．
            // そのクラスのメソッドを使用しているとみなす
            {
                final ExternalClassInfo externalSuperClass = NameResolver
                        .getExternalSuperClass((TargetClassInfo) methodOwnerClassType);
                if (!(methodOwnerClassType instanceof TargetInnerClassInfo)
                        && (null != externalSuperClass)) {

                    final ExternalMethodInfo methodInfo = new ExternalMethodInfo(methodName,
                            externalSuperClass, constructor);
                    final List<ParameterInfo> parameters = NameResolver
                            .createParameters(parameterTypes);
                    methodInfo.addParameters(parameters);

                    usingMethod.addCallee(methodInfo);
                    methodInfo.addCaller(usingMethod);
                    methodInfoManager.add(methodInfo);

                    // 解決済みキャッシュに登録
                    resolvedCache.put(methodCall, methodInfo.getReturnType());

                    // 外部クラスに新規で外部変数（ExternalFieldInfo）を追加したので型は不明
                    return methodInfo.getReturnType();
                }
            }

            // 見つからなかった処理を行う
            {
                err.println("Can't resolve method Call : " + methodCall.getMethodName());

                usingMethod.addUnresolvedUsage(methodCall);

                // 解決済みキャッシュに登録
                resolvedCache.put(methodCall, UnknownTypeInfo.getInstance());

                return UnknownTypeInfo.getInstance();
            }

            // 親が外部クラス（ExternalClassInfo）だった場合
        } else if (methodOwnerClassType instanceof ExternalClassInfo) {

            final ExternalMethodInfo methodInfo = new ExternalMethodInfo(methodName,
                    (ExternalClassInfo) methodOwnerClassType, constructor);
            final List<ParameterInfo> parameters = NameResolver.createParameters(parameterTypes);
            methodInfo.addParameters(parameters);

            usingMethod.addCallee(methodInfo);
            methodInfo.addCaller(usingMethod);
            methodInfoManager.add(methodInfo);

            // 解決済みキャッシュに登録
            resolvedCache.put(methodCall, methodInfo.getReturnType());

            // 外部クラスに新規で外部メソッド(ExternalMethodInfo)を追加したので型は不明．
            return methodInfo.getReturnType();

            // 親が配列だった場合
        } else if (methodOwnerClassType instanceof ArrayTypeInfo) {

            // Java 言語であれば， java.lang.Object に対する呼び出し
            if (Settings.getLanguage().equals(LANGUAGE.JAVA)) {
                final ClassInfo ownerClass = classInfoManager.getClassInfo(new String[] { "java",
                        "lang", "Object" });
                final ExternalMethodInfo methodInfo = new ExternalMethodInfo(methodName,
                        (ExternalClassInfo) ownerClass, false);
                final List<ParameterInfo> parameters = NameResolver
                        .createParameters(parameterTypes);
                methodInfo.addParameters(parameters);

                usingMethod.addCallee(methodInfo);
                methodInfo.addCaller(usingMethod);
                methodInfoManager.add(methodInfo);

                // 解決済みキャッシュに登録
                resolvedCache.put(methodCall, methodInfo.getReturnType());

                // 外部クラスに新規で外部メソッドを追加したので型は不明
                return methodInfo.getReturnType();
            }
        }

        assert false : "Here shouldn't be reached!";
        return UnknownTypeInfo.getInstance();
    }

    /**
     * 未解決配列型フィールドの要素使用を解決し，配列型フィールドの要素使用が行われているメソッドに登録する．また，フィールドの型を返す．
     * 
     * @param arrayElement 未解決配列型フィールドの要素使用
     * @param usingClass フィールド代入が行われているクラス
     * @param usingMethod フィールド代入が行われているメソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     * @param resolvedCache 解決済みUnresolvedTypeInfoのキャッシュ
     * @return 解決済みフィールド代入の型（つまり，フィールドの型）
     */
    public static TypeInfo resolveArrayElementUsage(final UnresolvedArrayElementUsage arrayElement,
            final TargetClassInfo usingClass, final TargetMethodInfo usingMethod,
            final ClassInfoManager classInfoManager, final FieldInfoManager fieldInfoManager,
            final MethodInfoManager methodInfoManager,
            final Map<UnresolvedTypeInfo, TypeInfo> resolvedCache) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == arrayElement) || (null == usingClass) || (null == usingMethod)
                || (null == classInfoManager) || (null == fieldInfoManager)
                || (null == methodInfoManager) || (null == resolvedCache)) {
            throw new NullPointerException();
        }

        // 既に解決済みであれば，そこから型を取得
        if (resolvedCache.containsKey(arrayElement)) {
            final TypeInfo type = resolvedCache.get(arrayElement);
            return type;
        }

        // 要素使用がくっついている未定義型を取得
        final UnresolvedTypeInfo unresolvedOwnerType = arrayElement.getOwnerArrayType();
        TypeInfo ownerArrayType = NameResolver.resolveTypeInfo(unresolvedOwnerType, usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager, resolvedCache);
        assert ownerArrayType != null : "resolveTypeInfo returned null!";

        // 未定義型の名前解決ができなかった場合
        if (ownerArrayType instanceof UnknownTypeInfo) {
            if (unresolvedOwnerType instanceof UnresolvedArrayTypeInfo) {
                final UnresolvedTypeInfo unresolvedElementType = ((UnresolvedArrayTypeInfo) unresolvedOwnerType)
                        .getElementType();
                final int dimension = ((UnresolvedArrayTypeInfo) unresolvedOwnerType)
                        .getDimension();
                final TypeInfo elementType = NameResolver
                        .createExternalClassInfo((UnresolvedReferenceTypeInfo) unresolvedElementType);
                classInfoManager.add((ExternalClassInfo) elementType);
                ownerArrayType = ArrayTypeInfo.getType(elementType, dimension);
            } else {

                err.println("Can't resolve array type : " + arrayElement.getTypeName());

                usingMethod.addUnresolvedUsage(arrayElement);
                resolvedCache.put(arrayElement, UnknownTypeInfo.getInstance());
                return UnknownTypeInfo.getInstance();
            }
        }

        // 配列の次元に応じて型を生成
        final int ownerArrayDimension = ((ArrayTypeInfo) ownerArrayType).getDimension();
        final TypeInfo ownerElementType = ((ArrayTypeInfo) ownerArrayType).getElementType();

        // 配列が二次元以上の場合は，次元を一つ落とした配列を返す
        if (1 < ownerArrayDimension) {

            final TypeInfo type = ArrayTypeInfo.getType(ownerElementType, ownerArrayDimension - 1);
            resolvedCache.put(arrayElement, type);
            return type;

            // 配列が一次元の場合は，要素の型を返す
        } else {

            resolvedCache.put(arrayElement, ownerElementType);
            return ownerElementType;
        }
    }

    /**
     * 未解決エンティティ使用情報を解決し，エンティティ使用処理が行われているメソッドに登録する．また，エンティティの解決済み型を返す．
     * 
     * @param entityUsage 未解決エンティティ使用
     * @param usingClass メソッド呼び出しが行われているクラス
     * @param usingMethod メソッド呼び出しが行われているメソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     * @param resolvedCache 解決済みUnresolvedTypeInfoのキャッシュ
     * @return メソッド呼び出し情報に対応する MethodInfo
     */
    public static TypeInfo resolveEntityUsage(final UnresolvedEntityUsage entityUsage,
            final TargetClassInfo usingClass, final TargetMethodInfo usingMethod,
            final ClassInfoManager classInfoManager, final FieldInfoManager fieldInfoManager,
            final MethodInfoManager methodInfoManager,
            final Map<UnresolvedTypeInfo, TypeInfo> resolvedCache) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == entityUsage) || (null == usingClass) || (null == usingMethod)
                || (null == classInfoManager) || (null == methodInfoManager)
                || (null == resolvedCache)) {
            throw new NullPointerException();
        }

        // 既に解決済みであれば，そこから型を取得
        if (resolvedCache.containsKey(entityUsage)) {
            final TypeInfo type = resolvedCache.get(entityUsage);
            assert null != type : "resolveEntityUsage returned null!";
            return type;
        }

        // エンティティ参照名を取得
        final String[] name = entityUsage.getName();

        // 利用可能なインスタンスフィールド名からエンティティ名を検索
        {
            // このクラスで利用可能なインスタンスフィールド一覧を取得
            final List<TargetFieldInfo> availableFieldsOfThisClass = Members
                    .<TargetFieldInfo> getInstanceMembers(NameResolver
                            .getAvailableFields(usingClass));

            for (TargetFieldInfo availableFieldOfThisClass : availableFieldsOfThisClass) {

                // 一致するフィールド名が見つかった場合
                if (name[0].equals(availableFieldOfThisClass.getName())) {
                    usingMethod.addReferencee(availableFieldOfThisClass);
                    availableFieldOfThisClass.addReferencer(usingMethod);

                    // availableField.getType() から次のword(name[i])を名前解決
                    TypeInfo ownerTypeInfo = availableFieldOfThisClass.getType();
                    for (int i = 1; i < name.length; i++) {

                        // 親が UnknownTypeInfo だったら，どうしようもない
                        if (ownerTypeInfo instanceof UnknownTypeInfo) {

                            // 解決済みキャッシュに登録
                            resolvedCache.put(entityUsage, UnknownTypeInfo.getInstance());

                            return UnknownTypeInfo.getInstance();

                            // 親が対象クラス(TargetClassInfo)の場合
                        } else if (ownerTypeInfo instanceof TargetClassInfo) {

                            // まずは利用可能なフィールド一覧を取得
                            boolean found = false;
                            {
                                // 利用可能なインスタンスフィールド一覧を取得
                                final List<TargetFieldInfo> availableFields = Members
                                        .getInstanceMembers(NameResolver.getAvailableFields(
                                                (TargetClassInfo) ownerTypeInfo, usingClass));

                                for (TargetFieldInfo availableField : availableFields) {

                                    // 一致するフィールド名が見つかった場合
                                    if (name[i].equals(availableField.getName())) {
                                        usingMethod.addReferencee(availableField);
                                        availableField.addReferencer(usingMethod);

                                        ownerTypeInfo = availableField.getType();
                                        found = true;
                                        break;
                                    }
                                }
                            }

                            // 利用可能なフィールドが見つからなかった場合は，外部クラスである親クラスがあるはず．
                            // そのクラスのフィールドを使用しているとみなす
                            {
                                if (!found) {

                                    final ExternalClassInfo externalSuperClass = NameResolver
                                            .getExternalSuperClass((TargetClassInfo) ownerTypeInfo);
                                    if (!(ownerTypeInfo instanceof TargetInnerClassInfo)
                                            && (null != externalSuperClass)) {

                                        final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                name[i], externalSuperClass);

                                        usingMethod.addReferencee(fieldInfo);
                                        fieldInfo.addReferencer(usingMethod);
                                        fieldInfoManager.add(fieldInfo);

                                        ownerTypeInfo = fieldInfo.getType();

                                    } else {
                                        err.println("Can't resolve entity usage1 : "
                                                + entityUsage.getTypeName());
                                    }
                                }
                            }

                            // 親が外部クラス(ExternalClassInfo)の場合
                        } else if (ownerTypeInfo instanceof ExternalClassInfo) {

                            final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(name[i],
                                    (ExternalClassInfo) ownerTypeInfo);

                            usingMethod.addReferencee(fieldInfo);
                            fieldInfo.addReferencer(usingMethod);
                            fieldInfoManager.add(fieldInfo);

                            ownerTypeInfo = fieldInfo.getType();

                        } else {
                            err.println("here shouldn't be reached!");
                            assert false;
                        }
                    }

                    // 解決済みキャッシュに登録
                    resolvedCache.put(entityUsage, ownerTypeInfo);
                    assert null != ownerTypeInfo : "resolveEntityUsage returned null!";
                    return ownerTypeInfo;
                }
            }
        }

        // 利用可能なスタティックフィールド名からエンティティ名を検索
        {
            // このクラスで利用可能なスタティックフィールド一覧を取得
            final List<TargetFieldInfo> availableFieldsOfThisClass = Members
                    .<TargetFieldInfo> getStaticMembers(NameResolver.getAvailableFields(usingClass));

            for (TargetFieldInfo availableFieldOfThisClass : availableFieldsOfThisClass) {

                // 一致するフィールド名が見つかった場合
                if (name[0].equals(availableFieldOfThisClass.getName())) {
                    usingMethod.addReferencee(availableFieldOfThisClass);
                    availableFieldOfThisClass.addReferencer(usingMethod);

                    // availableField.getType() から次のword(name[i])を名前解決
                    TypeInfo ownerTypeInfo = availableFieldOfThisClass.getType();
                    for (int i = 1; i < name.length; i++) {

                        // 親が UnknownTypeInfo だったら，どうしようもない
                        if (ownerTypeInfo instanceof UnknownTypeInfo) {

                            // 解決済みキャッシュに登録
                            resolvedCache.put(entityUsage, UnknownTypeInfo.getInstance());

                            return UnknownTypeInfo.getInstance();

                            // 親が対象クラス(TargetClassInfo)の場合
                        } else if (ownerTypeInfo instanceof TargetClassInfo) {

                            // まずは利用可能なフィールド一覧を取得
                            boolean found = false;
                            {
                                // 利用可能なスタティックフィールド一覧を取得
                                final List<TargetFieldInfo> availableFields = Members
                                        .getStaticMembers(NameResolver.getAvailableFields(
                                                (TargetClassInfo) ownerTypeInfo, usingClass));

                                for (TargetFieldInfo availableField : availableFields) {

                                    // 一致するフィールド名が見つかった場合
                                    if (name[i].equals(availableField.getName())) {
                                        usingMethod.addReferencee(availableField);
                                        availableField.addReferencer(usingMethod);

                                        ownerTypeInfo = availableField.getType();
                                        found = true;
                                        break;
                                    }
                                }
                            }

                            // スタティックフィールドで見つからなかった場合は，インナークラスから探す
                            {
                                if (!found) {
                                    // インナークラス一覧を取得
                                    final SortedSet<TargetInnerClassInfo> innerClasses = ((TargetClassInfo) ownerTypeInfo)
                                            .getInnerClasses();

                                    for (TargetInnerClassInfo innerClass : innerClasses) {

                                        // 一致するクラス名が見つかった場合
                                        if (name[i].equals(innerClass.getClassName())) {
                                            // TODO 利用関係を構築するコードが必要？

                                            ownerTypeInfo = innerClass;
                                            found = true;
                                            break;
                                        }
                                    }
                                }
                            }

                            // 利用可能なフィールドが見つからなかった場合は，外部クラスである親クラスがあるはず．
                            // そのクラスのフィールドを使用しているとみなす
                            {
                                if (!found) {

                                    final ExternalClassInfo externalSuperClass = NameResolver
                                            .getExternalSuperClass((TargetClassInfo) ownerTypeInfo);
                                    if (!(ownerTypeInfo instanceof TargetInnerClassInfo)
                                            && (null != externalSuperClass)) {

                                        final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                name[i], externalSuperClass);

                                        usingMethod.addReferencee(fieldInfo);
                                        fieldInfo.addReferencer(usingMethod);
                                        fieldInfoManager.add(fieldInfo);

                                        ownerTypeInfo = fieldInfo.getType();

                                    } else {
                                        err.println("Can't resolve entity usage2 : "
                                                + entityUsage.getTypeName());
                                    }
                                }
                            }

                            // 親が外部クラス(ExternalClassInfo)の場合
                        } else if (ownerTypeInfo instanceof ExternalClassInfo) {

                            final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(name[i],
                                    (ExternalClassInfo) ownerTypeInfo);

                            usingMethod.addReferencee(fieldInfo);
                            fieldInfo.addReferencer(usingMethod);
                            fieldInfoManager.add(fieldInfo);

                            ownerTypeInfo = fieldInfo.getType();

                        } else {
                            assert false : "Here shouldn't be reached!";
                        }
                    }

                    // 解決済みキャッシュに登録
                    resolvedCache.put(entityUsage, ownerTypeInfo);
                    assert null != ownerTypeInfo : "resolveEntityUsage returned null!";
                    return ownerTypeInfo;
                }
            }
        }

        // エンティティ名が完全限定名である場合を検索
        {

            for (int length = 1; length <= name.length; length++) {

                // 検索する名前(String[])を作成
                final String[] searchingName = new String[length];
                System.arraycopy(name, 0, searchingName, 0, length);

                final ClassInfo searchingClass = classInfoManager.getClassInfo(searchingName);
                if (null != searchingClass) {

                    TypeInfo ownerTypeInfo = searchingClass;
                    for (int i = length; i < name.length; i++) {

                        // 親が UnknownTypeInfo だったら，どうしようもない
                        if (ownerTypeInfo instanceof UnknownTypeInfo) {

                            // 解決済みキャッシュに登録
                            resolvedCache.put(entityUsage, UnknownTypeInfo.getInstance());

                            return UnknownTypeInfo.getInstance();

                            // 親が対象クラス(TargetClassInfo)の場合
                        } else if (ownerTypeInfo instanceof TargetClassInfo) {

                            // まずは利用可能なフィールド一覧を取得
                            boolean found = false;
                            {
                                // 利用可能なフィールド一覧を取得
                                final List<TargetFieldInfo> availableFields = Members
                                        .getStaticMembers(NameResolver.getAvailableFields(
                                                (TargetClassInfo) ownerTypeInfo, usingClass));

                                for (TargetFieldInfo availableField : availableFields) {

                                    // 一致するフィールド名が見つかった場合
                                    if (name[i].equals(availableField.getName())) {
                                        usingMethod.addReferencee(availableField);
                                        availableField.addReferencer(usingMethod);

                                        ownerTypeInfo = availableField.getType();
                                        found = true;
                                        break;
                                    }
                                }
                            }

                            // スタティックフィールドで見つからなかった場合は，インナークラスから探す
                            {
                                if (!found) {
                                    // インナークラス一覧を取得
                                    final SortedSet<TargetInnerClassInfo> innerClasses = ((TargetClassInfo) ownerTypeInfo)
                                            .getInnerClasses();

                                    for (TargetInnerClassInfo innerClass : innerClasses) {

                                        // 一致するクラス名が見つかった場合
                                        if (name[i].equals(innerClass.getClassName())) {
                                            // TODO 利用関係を構築するコードが必要？

                                            ownerTypeInfo = innerClass;
                                            found = true;
                                            break;
                                        }
                                    }
                                }
                            }

                            // 利用可能なフィールドが見つからなかった場合は，外部クラスである親クラスがあるはず．
                            // そのクラスのフィールドを使用しているとみなす
                            {
                                if (!found) {

                                    final ExternalClassInfo externalSuperClass = NameResolver
                                            .getExternalSuperClass((TargetClassInfo) ownerTypeInfo);
                                    if (!(ownerTypeInfo instanceof TargetInnerClassInfo)
                                            && (null != externalSuperClass)) {

                                        final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                name[i], externalSuperClass);

                                        usingMethod.addReferencee(fieldInfo);
                                        fieldInfo.addReferencer(usingMethod);
                                        fieldInfoManager.add(fieldInfo);

                                        ownerTypeInfo = fieldInfo.getType();

                                    } else {
                                        err.println("Can't resolve entity usage3 : "
                                                + entityUsage.getTypeName());
                                    }
                                }
                            }

                            // 親が外部クラス(ExternalClassInfo)の場合
                        } else if (ownerTypeInfo instanceof ExternalClassInfo) {

                            final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(name[i],
                                    (ExternalClassInfo) ownerTypeInfo);

                            usingMethod.addReferencee(fieldInfo);
                            fieldInfo.addReferencer(usingMethod);
                            fieldInfoManager.add(fieldInfo);

                            ownerTypeInfo = fieldInfo.getType();

                        } else {
                            assert false : "Here shouldn't be reached!";
                        }
                    }

                    // 解決済みキャッシュに登録
                    resolvedCache.put(entityUsage, ownerTypeInfo);
                    assert null != ownerTypeInfo : "resolveEntityUsage returned null!";
                    return ownerTypeInfo;
                }
            }
        }

        // 利用可能なクラス名からエンティティ名を検索
        {
            for (AvailableNamespaceInfo availableNamespace : entityUsage.getAvailableNamespaces()) {

                // 名前空間名.* となっている場合
                if (availableNamespace.isAllClasses()) {
                    final String[] namespace = availableNamespace.getNamespace();

                    // 名前空間の下にある各クラスに対して
                    for (ClassInfo classInfo : classInfoManager.getClassInfos(namespace)) {
                        final String className = classInfo.getClassName();

                        // クラス名と参照名の先頭が等しい場合は，そのクラス名が参照先であると決定する
                        if (className.equals(name[0])) {

                            TypeInfo ownerTypeInfo = classInfo;
                            for (int i = 1; i < name.length; i++) {

                                // 親が UnknownTypeInfo だったら，どうしようもない
                                if (ownerTypeInfo instanceof UnknownTypeInfo) {

                                    // 解決済みキャッシュに登録
                                    resolvedCache.put(entityUsage, UnknownTypeInfo.getInstance());

                                    return UnknownTypeInfo.getInstance();

                                    // 親が対象クラス(TargetClassInfo)の場合
                                } else if (ownerTypeInfo instanceof TargetClassInfo) {

                                    // まずは利用可能なフィールド一覧を取得
                                    boolean found = false;
                                    {
                                        // 利用可能なフィールド一覧を取得
                                        final List<TargetFieldInfo> availableFields = NameResolver
                                                .getAvailableFields(
                                                        (TargetClassInfo) ownerTypeInfo, usingClass);

                                        for (TargetFieldInfo availableField : availableFields) {

                                            // 一致するフィールド名が見つかった場合
                                            if (name[i].equals(availableField.getName())) {
                                                usingMethod.addReferencee(availableField);
                                                availableField.addReferencer(usingMethod);

                                                ownerTypeInfo = availableField.getType();
                                                found = true;
                                                break;
                                            }
                                        }
                                    }

                                    // スタティックフィールドで見つからなかった場合は，インナークラスから探す
                                    {
                                        if (!found) {
                                            // インナークラス一覧を取得
                                            final SortedSet<TargetInnerClassInfo> innerClasses = ((TargetClassInfo) ownerTypeInfo)
                                                    .getInnerClasses();

                                            for (TargetInnerClassInfo innerClass : innerClasses) {

                                                // 一致するクラス名が見つかった場合
                                                if (name[i].equals(innerClass.getClassName())) {
                                                    // TODO 利用関係を構築するコードが必要？

                                                    ownerTypeInfo = innerClass;
                                                    found = true;
                                                    break;
                                                }
                                            }
                                        }
                                    }

                                    // 利用可能なフィールドが見つからなかった場合は，外部クラスである親クラスがあるはず．
                                    // そのクラスのフィールドを使用しているとみなす
                                    {
                                        if (!found) {

                                            final ExternalClassInfo externalSuperClass = NameResolver
                                                    .getExternalSuperClass((TargetClassInfo) ownerTypeInfo);
                                            if (!(ownerTypeInfo instanceof TargetInnerClassInfo)
                                                    && (null != externalSuperClass)) {

                                                final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                        name[i], externalSuperClass);

                                                usingMethod.addReferencee(fieldInfo);
                                                fieldInfo.addReferencer(usingMethod);
                                                fieldInfoManager.add(fieldInfo);

                                                ownerTypeInfo = fieldInfo.getType();

                                            } else {
                                                err.println("Can't resolve entity usage4 : "
                                                        + entityUsage.getTypeName());
                                            }
                                        }
                                    }

                                    // 親が外部クラス(ExternalClassInfo)の場合
                                } else if (ownerTypeInfo instanceof ExternalClassInfo) {

                                    final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                            name[i], (ExternalClassInfo) ownerTypeInfo);

                                    usingMethod.addReferencee(fieldInfo);
                                    fieldInfo.addReferencer(usingMethod);
                                    fieldInfoManager.add(fieldInfo);

                                    ownerTypeInfo = fieldInfo.getType();

                                } else {
                                    assert false : "Here should be reached!";
                                }
                            }

                            // 解決済みキャッシュに登録
                            resolvedCache.put(entityUsage, ownerTypeInfo);
                            assert null != ownerTypeInfo : "resolveEntityUsage returned null!";
                            return ownerTypeInfo;
                        }
                    }

                    // 名前空間.クラス名 となっている場合
                } else {

                    final String[] importName = availableNamespace.getImportName();

                    // クラス名と参照名の先頭が等しい場合は，そのクラス名が参照先であると決定する
                    if (importName[importName.length - 1].equals(name[0])) {

                        ClassInfo specifiedClassInfo = classInfoManager.getClassInfo(importName);
                        if (null == specifiedClassInfo) {
                            specifiedClassInfo = new ExternalClassInfo(importName);
                            classInfoManager.add((ExternalClassInfo) specifiedClassInfo);
                        }

                        TypeInfo ownerTypeInfo = specifiedClassInfo;
                        for (int i = 1; i < name.length; i++) {

                            // 親が UnknownTypeInfo だったら，どうしようもない
                            if (ownerTypeInfo instanceof UnknownTypeInfo) {

                                // 解決済みキャッシュに登録
                                resolvedCache.put(entityUsage, UnknownTypeInfo.getInstance());

                                return UnknownTypeInfo.getInstance();

                                // 親が対象クラス(TargetClassInfo)の場合
                            } else if (ownerTypeInfo instanceof TargetClassInfo) {

                                // まずは利用可能なフィールド一覧を取得
                                boolean found = false;
                                {
                                    // 利用可能なフィールド一覧を取得
                                    final List<TargetFieldInfo> availableFields = NameResolver
                                            .getAvailableFields((TargetClassInfo) ownerTypeInfo,
                                                    usingClass);

                                    for (TargetFieldInfo availableField : availableFields) {

                                        // 一致するフィールド名が見つかった場合
                                        if (name[i].equals(availableField.getName())) {
                                            usingMethod.addReferencee(availableField);
                                            availableField.addReferencer(usingMethod);

                                            ownerTypeInfo = availableField.getType();
                                            found = true;
                                            break;
                                        }
                                    }
                                }

                                // スタティックフィールドで見つからなかった場合は，インナークラスから探す
                                {
                                    if (!found) {
                                        // インナークラス一覧を取得
                                        final SortedSet<TargetInnerClassInfo> innerClasses = ((TargetClassInfo) ownerTypeInfo)
                                                .getInnerClasses();

                                        for (TargetInnerClassInfo innerClass : innerClasses) {

                                            // 一致するクラス名が見つかった場合
                                            if (name[i].equals(innerClass.getClassName())) {
                                                // TODO 利用関係を構築するコードが必要？

                                                ownerTypeInfo = innerClass;
                                                found = true;
                                                break;
                                            }
                                        }
                                    }
                                }

                                // 利用可能なフィールドが見つからなかった場合は，外部クラスである親クラスがあるはず．
                                // そのクラスのフィールドを使用しているとみなす
                                {
                                    if (!found) {

                                        final ExternalClassInfo externalSuperClass = NameResolver
                                                .getExternalSuperClass((TargetClassInfo) ownerTypeInfo);
                                        if (!(ownerTypeInfo instanceof TargetInnerClassInfo)
                                                && (null != externalSuperClass)) {

                                            final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                    name[i], externalSuperClass);

                                            usingMethod.addReferencee(fieldInfo);
                                            fieldInfo.addReferencer(usingMethod);
                                            fieldInfoManager.add(fieldInfo);

                                            ownerTypeInfo = fieldInfo.getType();

                                        } else {
                                            err.println("Can't resolve entity usage5 : "
                                                    + entityUsage.getTypeName());
                                        }
                                    }
                                }

                                // 親が外部クラス(ExternalClassInfo)の場合
                            } else if (ownerTypeInfo instanceof ExternalClassInfo) {

                                final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(name[i],
                                        (ExternalClassInfo) ownerTypeInfo);

                                usingMethod.addReferencee(fieldInfo);
                                fieldInfo.addReferencer(usingMethod);
                                fieldInfoManager.add(fieldInfo);

                                ownerTypeInfo = fieldInfo.getType();

                            }else {
                                assert false : "Here shouldn't be reached!";
                            }
                        }

                        // 解決済みキャッシュに登録
                        resolvedCache.put(entityUsage, ownerTypeInfo);
                        assert null != ownerTypeInfo : "resolveEntityUsage returned null!";
                        return ownerTypeInfo;
                    }
                }
            }
        }

        err.println("Can't resolve entity usage6 : " + entityUsage.getTypeName());

        // 見つからなかった処理を行う
        usingMethod.addUnresolvedUsage(entityUsage);

        // 解決済みキャッシュに登録
        resolvedCache.put(entityUsage, UnknownTypeInfo.getInstance());

        return UnknownTypeInfo.getInstance();
    }

    /**
     * 引数で与えられた未解決型情報を表す解決済み型情報クラスを生成する． ここで引数として与えられるのは，ソースコードがパースされていない型であるので，生成する解決済み型情報クラスは
     * ExternalClassInfo となる．
     * 
     * @param unresolvedReferenceType 未解決型情報
     * @return 解決済み型情報
     */
    public static ExternalClassInfo createExternalClassInfo(
            final UnresolvedReferenceTypeInfo unresolvedReferenceType) {

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

                    final ExternalClassInfo classInfo = new ExternalClassInfo(fullQualifiedName);
                    return classInfo;
                }
            }
        }

        // 見つからない場合は，名前空間が UNKNOWN な 外部クラス情報を作成
        final ExternalClassInfo unknownClassInfo = new ExternalClassInfo(
                referenceName[referenceName.length - 1]);
        return unknownClassInfo;
    }

    /**
     * 引数で与えられた型の List から外部パラメータの List を作成し，返す
     * 
     * @param types 型のList
     * @return 外部パラメータの List
     */
    public static List<ParameterInfo> createParameters(final List<TypeInfo> types) {

        if (null == types) {
            throw new NullPointerException();
        }

        final List<ParameterInfo> parameters = new LinkedList<ParameterInfo>();
        for (TypeInfo type : types) {
            final ExternalParameterInfo parameter = new ExternalParameterInfo(type);
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
    private static ExternalClassInfo getExternalSuperClass(final TargetClassInfo classInfo) {

        for (ClassInfo superClassInfo : classInfo.getSuperClasses()) {

            if (superClassInfo instanceof ExternalClassInfo) {
                return (ExternalClassInfo) superClassInfo;
            }

            NameResolver.getExternalSuperClass((TargetClassInfo) superClassInfo);
        }

        return null;
    }

    /**
     * 「現在のクラス」で利用可能なフィールド一覧を返す．
     * ここで，「利用可能なフィールド」とは，「現在のクラス」で定義されているフィールド，及びその親クラスで定義されているフィールドのうち子クラスからアクセスが可能なフィールドである．
     * 利用可能なフィールドは List に格納されている． リストの先頭から優先順位の高いフィールド（つまり，クラス階層において下位のクラスに定義されているフィールド）が格納されている．
     * 
     * @param thisClass 現在のクラス
     * @return 利用可能なフィールド一覧
     */
    private static List<TargetFieldInfo> getAvailableFields(final TargetClassInfo thisClass) {

        if (null == thisClass) {
            throw new NullPointerException();
        }

        return NameResolver.getAvailableFields(thisClass, thisClass);
    }

    /**
     * 「現在のクラス」で利用可能なメソッド一覧を返す．
     * ここで，「利用可能なメソッド」とは，「現在のクラス」で定義されているメソッド，及びその親クラスで定義されているメソッドのうち子クラスからアクセスが可能なメソッドである．
     * 利用可能なメソッドは List に格納されている． リストの先頭から優先順位の高いメソッド（つまり，クラス階層において下位のクラスに定義されているメソッド）が格納されている．
     * 
     * @param thisClass 現在のクラス
     * @return 利用可能なメソッド一覧
     */
    private static List<TargetMethodInfo> getAvailableMethods(final TargetClassInfo thisClass) {

        if (null == thisClass) {
            throw new NullPointerException();
        }

        return NameResolver.getAvailableMethods(thisClass, thisClass);
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
    private static List<TargetFieldInfo> getAvailableFields(final TargetClassInfo usedClass,
            final TargetClassInfo usingClass) {

        if ((null == usedClass) || (null == usingClass)) {
            throw new NullPointerException();
        }

        final List<TargetFieldInfo> availableFields = new LinkedList<TargetFieldInfo>();

        // このクラスで定義されているフィールドのうち，使用するクラスで利用可能なフィールドを取得する
        // 2つのクラスが同じ場合
        if (usedClass.equals(usingClass)) {

            availableFields.addAll(usedClass.getDefinedFields());

            // 2つのクラスが同じ名前空間を持っている場合
        } else if (usedClass.getNamespace().equals(usingClass.getNamespace())) {

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
    private static List<TargetMethodInfo> getAvailableMethods(final TargetClassInfo usedClass,
            final TargetClassInfo usingClass) {

        if ((null == usedClass) || (null == usingClass)) {
            throw new NullPointerException();
        }

        final List<TargetMethodInfo> availableMethods = new LinkedList<TargetMethodInfo>();

        // このクラスで定義されているメソッドのうち，使用するクラスで利用可能なメソッドを取得する
        // 2つのクラスが同じ場合
        if (usedClass.equals(usingClass)) {

            availableMethods.addAll(usedClass.getDefinedMethods());

            // 2つのクラスが同じ名前空間を持っている場合
        } else if (usedClass.getNamespace().equals(usingClass.getNamespace())) {

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
    private static List<TargetFieldInfo> getAvailableFieldsInSubClasses(
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
    private static List<TargetMethodInfo> getAvailableMethodsInSubClasses(
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
