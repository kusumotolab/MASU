package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Iterator;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VoidTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * Unresolved * Info から * Info を得るための名前解決ユーティリティクラス
 * 
 * @author y-higo
 * 
 */
public final class NameResolver {

    /**
     * UnresolvedTypeInfoな情報から TypeInfoを生成する．参照されているTypeInfoがclassInfoManagerに含まれていない場合は追加する．
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

        // プリミティブ型の場合
        if (unresolvedTypeInfo instanceof PrimitiveTypeInfo) {
            return (PrimitiveTypeInfo) unresolvedTypeInfo;

            // void型の場合
        } else if (unresolvedTypeInfo instanceof VoidTypeInfo) {
            return (VoidTypeInfo) unresolvedTypeInfo;

            // 参照型の場合
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
                        String className = classInfo.getClassName();

                        // クラス名と参照名の先頭が等しい場合は，そのクラス名が参照先であると決定する
                        if (className.equals(referenceName[0])) {
                            String[] fullQualifiedName = new String[namespace.length
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
            ExternalClassInfo classInfo = new ExternalClassInfo(referenceName[referenceName.length - 1]);
            classInfoManager.add(classInfo);
            return classInfo;

            // それ以外の型の場合はエラー
        } else {
            throw new IllegalArgumentException(unresolvedTypeInfo.toString()
                    + " is a wrong object!");
        }
    }

    /**
     * フィールド使用情報から，該当する FieldInfo を返す．該当する FieldInfo がない場合は生成する．
     * 
     * @param fieldUsage フィールド使用情報
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @return フィールド使用情報に対応する FieldInfo
     */
    public static FieldInfo resolveFieldUsage(final UnresolvedFieldUsage fieldUsage,
            final ClassInfoManager classInfoManager, final FieldInfoManager fieldInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == fieldUsage) || (null == classInfoManager) || (null == fieldInfoManager)) {
            throw new NullPointerException();
        }

        // フィールド名を取得
        String fieldName = fieldUsage.getFieldName();

        // フィールドが定義されているクラスの型を特定
        UnresolvedTypeInfo unresolvedOwnerClassType = fieldUsage.getOwnerClassType();
        TypeInfo ownerClassType = NameResolver.resolveTypeInfo(unresolvedOwnerClassType,
                classInfoManager);

        // 外部クラスの場合は，呼び出すフィールド情報を生成し，フィールドマネージャに追加
        if (ownerClassType instanceof ExternalClassInfo) {

            ExternalFieldInfo fieldInfo = new ExternalFieldInfo(fieldName,
                    (ClassInfo) ownerClassType);
            fieldInfoManager.add(fieldInfo);
            return fieldInfo;

            // 対象クラスの場合は，そのクラスから該当フィールドの探す，
            // 見つからなければ，生成し登録する
        } else if (ownerClassType instanceof TargetClassInfo) {

            for (TargetFieldInfo fieldInfo : ((TargetClassInfo) ownerClassType).getDefinedFields()) {

                if (fieldName.equals(fieldInfo.getName())) {
                    return fieldInfo;
                }
            }

            // 使用されているフィールドがClassInfoManagerに登録されていないので，追加する．
            // 対象クラスに定義されていないフィールドなので ExternalFieldInfo になる.
            // おそらく対象クラスの（外部の）親クラスに定義されているフィールドの使用．
            ExternalFieldInfo fieldInfo = new ExternalFieldInfo(fieldName,
                    (ClassInfo) ownerClassType);
            fieldInfoManager.add(fieldInfo);
            return fieldInfo;
        }

        throw new IllegalArgumentException(fieldUsage.toString() + " is wrong!");
    }

    /**
     * メソッド呼び出し情報から，該当する MethodInfo を返す． 該当する MethodInfo がない場合は生成する．
     * 
     * @param methodCall メソッド呼び出し情報
     * @param classInfoManager 用いるクラスマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     * @return メソッド呼び出し情報に対応する MethodInfo
     */
    public static MethodInfo resolveMethodCall(final UnresolvedMethodCall methodCall,
            final ClassInfoManager classInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == methodCall) || (null == classInfoManager) || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // メソッド名，引数を取得
        String methodName = methodCall.getMethodName();
        List<UnresolvedTypeInfo> unresolvedTypeInfos = methodCall.getParameterTypes();

        // クラスを特定
        String[] ownerClassName = methodCall.getOwnerClassName();
        ClassInfo ownerClass = classInfoManager.getClassInfo(ownerClassName);
        if (null == ownerClass) {
            ownerClass = new ExternalClassInfo(ownerClassName);
            classInfoManager.add((ExternalClassInfo) ownerClass);
        }

        // メソッド呼び出しが実行されているのが，外部の型（クラス）の場合
        if (ownerClass instanceof ExternalClassInfo) {

            // 呼び出し先メソッドのオブジェクトを生成
            ExternalMethodInfo methodInfo = new ExternalMethodInfo(methodName, ownerClass,
                    methodCall.isConstructor());
            for (UnresolvedTypeInfo unresolvedTypeInfo : methodCall.getParameterTypes()) {
                TypeInfo parameterType = NameResolver.resolveTypeInfo(unresolvedTypeInfo,
                        classInfoManager);
                ExternalParameterInfo parameterInfo = new ExternalParameterInfo(parameterType);
                methodInfo.addParameter(parameterInfo);
            }
            methodInfoManager.add(methodInfo);
            return methodInfo;

            // メソッド呼び出しが実行されているのが，対象の型（クラス）の場合
        } else if (ownerClass instanceof TargetClassInfo) {

            for (TargetMethodInfo methodInfo : ((TargetClassInfo) ownerClass).getDefinedMethods()) {

                // メソッド名が違う場合は，該当メソッドではない
                if (!methodName.equals(methodInfo.getMethodName())) {
                    continue;
                }

                // 引数の数が違う場合は，該当メソッドではない
                List<ParameterInfo> typeInfos = methodInfo.getParameters();
                if (unresolvedTypeInfos.size() != typeInfos.size()) {
                    continue;
                }

                // 全ての引数の型をチェック，1つでも異なる場合は，該当メソッドではない
                Iterator<UnresolvedTypeInfo> unresolvedTypeInfoIterator = unresolvedTypeInfos
                        .iterator();
                Iterator<ParameterInfo> parameterInfoIterator = typeInfos.iterator();
                boolean same = true;
                while (unresolvedTypeInfoIterator.hasNext() && parameterInfoIterator.hasNext()) {
                    UnresolvedTypeInfo unresolvedTypeInfo = unresolvedTypeInfoIterator.next();
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
        }

        // 呼び出されているメソッドがClassInfoManagerに登録されていないので，追加する．
        // 対象クラスに定義されていないメソッドなので ExternalMethodInfo になる.
        // おそらく対象クラスの（外部の）親クラスに定義されているメソッドの呼び出し．
        ExternalMethodInfo methodInfo = new ExternalMethodInfo(methodName, ownerClass, methodCall
                .isConstructor());
        for (UnresolvedTypeInfo unresolvedTypeInfo : methodCall.getParameterTypes()) {
            TypeInfo parameterType = NameResolver.resolveTypeInfo(unresolvedTypeInfo,
                    classInfoManager);
            ExternalParameterInfo parameterInfo = new ExternalParameterInfo(parameterType);
            methodInfo.addParameter(parameterInfo);
        }
        methodInfoManager.add(methodInfo);
        return methodInfo;
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
}
