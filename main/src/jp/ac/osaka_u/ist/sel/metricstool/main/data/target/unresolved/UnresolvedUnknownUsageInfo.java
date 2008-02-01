package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.List;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Members;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetInnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;


/**
 * 未解決エンティティ使用を保存するためのクラス． 未解決エンティティ使用とは，パッケージ名やクラス名の参照 を表す．
 * 
 * @author higo
 * 
 */
public final class UnresolvedUnknownUsageInfo implements UnresolvedEntityUsageInfo {

    /**
     * 未解決エンティティ使用オブジェクトを作成する．
     * 
     * @param availableNamespaces 利用可能な名前空間
     * @param name 未解決エンティティ使用名
     */
    public UnresolvedUnknownUsageInfo(final AvailableNamespaceInfoSet availableNamespaces,
            final String[] name) {

        this.availableNamespaces = availableNamespaces;
        this.name = name;

        this.resolvedIndo = null;
    }

    /**
     * 未解決エンティティ使用が解決されているかどうかを返す
     * 
     * @return 解決されている場合は true，そうでない場合は false
     */
    public boolean alreadyResolved() {
        return null != this.resolvedIndo;
    }

    /**
     * 解決済みエンティティ使用を返す
     * 
     * @return 解決済みエンティティ使用
     * @throws 解決されていない場合にスローされる
     */
    public EntityUsageInfo getResolvedEntityUsage() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedIndo;
    }

    public EntityUsageInfo resolveEntityUsage(final TargetClassInfo usingClass,
            final TargetMethodInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolvedEntityUsage();
        }

        // エンティティ参照名を取得
        final String[] name = this.getName();

        // 利用可能なインスタンスフィールド名からエンティティ名を検索
        {
            // このクラスで利用可能なインスタンスフィールド一覧を取得
            final List<TargetFieldInfo> availableFieldsOfThisClass = Members
                    .<TargetFieldInfo> getInstanceMembers(NameResolver
                            .getAvailableFields(usingClass));

            for (final TargetFieldInfo availableFieldOfThisClass : availableFieldsOfThisClass) {

                // 一致するフィールド名が見つかった場合
                if (name[0].equals(availableFieldOfThisClass.getName())) {
                    //usingMethod.addReferencee(availableFieldOfThisClass);
                    //availableFieldOfThisClass.addReferencer(usingMethod);

                    // availableField.getType() から次のword(name[i])を名前解決
                    EntityUsageInfo entityUsage = new FieldUsageInfo(availableFieldOfThisClass,
                            true);
                    for (int i = 1; i < name.length; i++) {

                        // 親が UnknownTypeInfo だったら，どうしようもない
                        if (entityUsage.getType() instanceof UnknownTypeInfo) {

                            this.resolvedIndo = UnknownEntityUsageInfo.getInstance();
                            return this.resolvedIndo;

                            //　親がクラス型の場合
                        } else if (entityUsage.getType() instanceof ClassTypeInfo) {

                            final ClassInfo ownerClass = ((ClassTypeInfo) entityUsage.getType())
                                    .getReferencedClass();

                            // 親が対象クラス(TargetClassInfo)の場合
                            if (ownerClass instanceof TargetClassInfo) {

                                // まずは利用可能なフィールド一覧を取得
                                boolean found = false;
                                {
                                    // 利用可能なインスタンスフィールド一覧を取得
                                    final List<TargetFieldInfo> availableFields = Members
                                            .getInstanceMembers(NameResolver.getAvailableFields(
                                                    (TargetClassInfo) entityUsage.getType(),
                                                    usingClass));

                                    for (final TargetFieldInfo availableField : availableFields) {

                                        // 一致するフィールド名が見つかった場合
                                        if (name[i].equals(availableField.getName())) {
                                            //usingMethod.addReferencee(availableField);
                                            //availableField.addReferencer(usingMethod);

                                            entityUsage = new FieldUsageInfo(availableField, true);
                                            found = true;
                                            break;
                                        }
                                    }
                                }

                                // 利用可能なフィールドが見つからなかった場合は，外部クラスである親クラスがあるはず．
                                // そのクラスのフィールドを使用しているとみなす
                                {
                                    if (!found) {

                                        final ClassInfo referencedClass = ((ClassTypeInfo) entityUsage
                                                .getType()).getReferencedClass();
                                        final ExternalClassInfo externalSuperClass = NameResolver
                                                .getExternalSuperClass((TargetClassInfo) referencedClass);
                                        if (!(referencedClass instanceof TargetInnerClassInfo)
                                                && (null != externalSuperClass)) {

                                            final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                    name[i], externalSuperClass);

                                            //usingMethod.addReferencee(fieldInfo);
                                            //fieldInfo.addReferencer(usingMethod);
                                            fieldInfoManager.add(fieldInfo);

                                            entityUsage = new FieldUsageInfo(fieldInfo, true);

                                        } else {
                                            assert false : "Can't resolve entity usage1 : "
                                                    + this.toString();
                                        }
                                    }
                                }

                                // 親が外部クラス(ExternalClassInfo)の場合
                            } else if (ownerClass instanceof ExternalClassInfo) {

                                final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(name[i],
                                        ownerClass);

                                //usingMethod.addReferencee(fieldInfo);
                                //fieldInfo.addReferencer(usingMethod);
                                fieldInfoManager.add(fieldInfo);

                                entityUsage = new FieldUsageInfo(fieldInfo, true);
                            }

                        } else {
                            assert false : "Here shouldn't be reached!";
                        }
                    }

                    this.resolvedIndo = entityUsage;
                    return this.resolvedIndo;
                }
            }
        }

        // 利用可能なスタティックフィールド名からエンティティ名を検索
        {
            // このクラスで利用可能なスタティックフィールド一覧を取得
            final List<TargetFieldInfo> availableFieldsOfThisClass = Members
                    .<TargetFieldInfo> getStaticMembers(NameResolver.getAvailableFields(usingClass));

            for (final TargetFieldInfo availableFieldOfThisClass : availableFieldsOfThisClass) {

                // 一致するフィールド名が見つかった場合
                if (name[0].equals(availableFieldOfThisClass.getName())) {
                    //usingMethod.addReferencee(availableFieldOfThisClass);
                    //availableFieldOfThisClass.addReferencer(usingMethod);

                    // availableField.getType() から次のword(name[i])を名前解決
                    EntityUsageInfo entityUsage = new FieldUsageInfo(availableFieldOfThisClass,
                            true);
                    for (int i = 1; i < name.length; i++) {

                        // 親が UnknownTypeInfo だったら，どうしようもない
                        if (entityUsage.getType() instanceof UnknownTypeInfo) {

                            this.resolvedIndo = UnknownEntityUsageInfo.getInstance();
                            return this.resolvedIndo;

                            // 親がクラス型の場合
                        } else if (entityUsage.getType() instanceof ClassTypeInfo) {

                            final ClassInfo ownerClass = ((ClassTypeInfo) entityUsage.getType())
                                    .getReferencedClass();

                            // 親が対象クラス(TargetClassInfo)の場合
                            if (ownerClass instanceof TargetClassInfo) {

                                // まずは利用可能なフィールド一覧を取得
                                boolean found = false;
                                {
                                    // 利用可能なスタティックフィールド一覧を取得
                                    final List<TargetFieldInfo> availableFields = Members
                                            .getStaticMembers(NameResolver.getAvailableFields(
                                                    (TargetClassInfo) entityUsage.getType(),
                                                    usingClass));

                                    for (final TargetFieldInfo availableField : availableFields) {

                                        // 一致するフィールド名が見つかった場合
                                        if (name[i].equals(availableField.getName())) {
                                            //usingMethod.addReferencee(availableField);
                                            //availableField.addReferencer(usingMethod);

                                            entityUsage = new FieldUsageInfo(availableField, true);
                                            found = true;
                                            break;
                                        }
                                    }
                                }

                                // スタティックフィールドで見つからなかった場合は，インナークラスから探す
                                {
                                    if (!found) {
                                        // インナークラス一覧を取得
                                        final SortedSet<TargetInnerClassInfo> innerClasses = NameResolver
                                                .getAvailableDirectInnerClasses((TargetClassInfo) entityUsage
                                                        .getType());
                                        for (final TargetInnerClassInfo innerClass : innerClasses) {

                                            // 一致するクラス名が見つかった場合
                                            if (name[i].equals(innerClass.getClassName())) {
                                                // TODO 利用関係を構築するコードが必要？

                                                final ClassTypeInfo referenceType = new ClassTypeInfo(
                                                        innerClass);
                                                entityUsage = new ClassReferenceInfo(referenceType);
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

                                        final ClassInfo referencedClass = ((ClassTypeInfo) entityUsage
                                                .getType()).getReferencedClass();
                                        final ExternalClassInfo externalSuperClass = NameResolver
                                                .getExternalSuperClass((TargetClassInfo) referencedClass);
                                        if (!(referencedClass instanceof TargetInnerClassInfo)
                                                && (null != externalSuperClass)) {

                                            final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                    name[i], externalSuperClass);

                                            //usingMethod.addReferencee(fieldInfo);
                                            //fieldInfo.addReferencer(usingMethod);
                                            fieldInfoManager.add(fieldInfo);

                                            entityUsage = new FieldUsageInfo(fieldInfo, true);

                                        } else {
                                            assert false : "Can't resolve entity usage2 : "
                                                    + this.toString();
                                        }
                                    }
                                }

                                // 親が外部クラス(ExternalClassInfo)の場合
                            } else if (ownerClass instanceof ExternalClassInfo) {

                                final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(name[i],
                                        ownerClass);

                                //usingMethod.addReferencee(fieldInfo);
                                //fieldInfo.addReferencer(usingMethod);
                                fieldInfoManager.add(fieldInfo);

                                entityUsage = new FieldUsageInfo(fieldInfo, true);
                            }

                        } else {
                            assert false : "Here shouldn't be reached!";
                        }
                    }

                    this.resolvedIndo = entityUsage;
                    return this.resolvedIndo;
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

                    EntityUsageInfo entityUsage = new ClassReferenceInfo(new ClassTypeInfo(
                            searchingClass));
                    for (int i = length; i < name.length; i++) {

                        // 親が UnknownTypeInfo だったら，どうしようもない
                        if (entityUsage.getType() instanceof UnknownTypeInfo) {

                            this.resolvedIndo = UnknownEntityUsageInfo.getInstance();
                            return this.resolvedIndo;

                            // 親がクラス型の場合
                        } else if (entityUsage.getType() instanceof ClassTypeInfo) {

                            final ClassInfo ownerClass = ((ClassTypeInfo) entityUsage.getType())
                                    .getReferencedClass();

                            // 親が対象クラス(TargetClassInfo)の場合
                            if (ownerClass instanceof TargetClassInfo) {

                                // まずは利用可能なフィールド一覧を取得
                                boolean found = false;
                                {
                                    // 利用可能なフィールド一覧を取得
                                    final List<TargetFieldInfo> availableFields = Members
                                            .getStaticMembers(NameResolver.getAvailableFields(
                                                    (TargetClassInfo) entityUsage.getType(),
                                                    usingClass));

                                    for (final TargetFieldInfo availableField : availableFields) {

                                        // 一致するフィールド名が見つかった場合
                                        if (name[i].equals(availableField.getName())) {
                                            //usingMethod.addReferencee(availableField);
                                            //availableField.addReferencer(usingMethod);

                                            entityUsage = new FieldUsageInfo(availableField, true);
                                            found = true;
                                            break;
                                        }
                                    }
                                }

                                // スタティックフィールドで見つからなかった場合は，インナークラスから探す
                                {
                                    if (!found) {
                                        // インナークラス一覧を取得
                                        final SortedSet<TargetInnerClassInfo> innerClasses = NameResolver
                                                .getAvailableDirectInnerClasses((TargetClassInfo) entityUsage
                                                        .getType());
                                        for (final TargetInnerClassInfo innerClass : innerClasses) {

                                            // 一致するクラス名が見つかった場合
                                            if (name[i].equals(innerClass.getClassName())) {
                                                // TODO 利用関係を構築するコードが必要？

                                                final ClassTypeInfo referenceType = new ClassTypeInfo(
                                                        innerClass);
                                                entityUsage = new ClassReferenceInfo(referenceType);
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

                                        final ClassInfo referencedClass = ((ClassTypeInfo) entityUsage
                                                .getType()).getReferencedClass();
                                        final ExternalClassInfo externalSuperClass = NameResolver
                                                .getExternalSuperClass((TargetClassInfo) referencedClass);
                                        if (!(referencedClass instanceof TargetInnerClassInfo)
                                                && (null != externalSuperClass)) {

                                            final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                    name[i], externalSuperClass);

                                            //usingMethod.addReferencee(fieldInfo);
                                            //fieldInfo.addReferencer(usingMethod);
                                            fieldInfoManager.add(fieldInfo);

                                            entityUsage = new FieldUsageInfo(fieldInfo, true);

                                        } else {
                                            assert false : "Can't resolve entity usage3 : "
                                                    + this.toString();
                                        }
                                    }
                                }

                                // 親が外部クラス(ExternalClassInfo)の場合
                            } else if (ownerClass instanceof ExternalClassInfo) {

                                final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(name[i],
                                        ownerClass);

                                //usingMethod.addReferencee(fieldInfo);
                                //fieldInfo.addReferencer(usingMethod);
                                fieldInfoManager.add(fieldInfo);

                                entityUsage = new FieldUsageInfo(fieldInfo, true);
                            }

                        } else {
                            assert false : "Here shouldn't be reached!";
                        }
                    }

                    this.resolvedIndo = entityUsage;
                    return this.resolvedIndo;
                }
            }
        }

        // 利用可能なクラス名からエンティティ名を検索
        {

            // 内部クラス名から検索
            {
                final TargetClassInfo outestClass;
                if (usingClass instanceof TargetInnerClassInfo) {
                    outestClass = NameResolver.getOuterstClass((TargetInnerClassInfo) usingClass);
                } else {
                    outestClass = usingClass;
                }

                for (final TargetInnerClassInfo innerClassInfo : NameResolver
                        .getAvailableInnerClasses(outestClass)) {

                    // クラス名と参照名の先頭が等しい場合は，そのクラス名が参照先であると決定する
                    final String innerClassName = innerClassInfo.getClassName();
                    if (innerClassName.equals(name[0])) {

                        EntityUsageInfo entityUsage = new ClassReferenceInfo(new ClassTypeInfo(
                                innerClassInfo));
                        for (int i = 1; i < name.length; i++) {

                            // 親が UnknownTypeInfo だったら，どうしようもない
                            if (entityUsage.getType() instanceof UnknownTypeInfo) {

                                this.resolvedIndo = UnknownEntityUsageInfo.getInstance();
                                return this.resolvedIndo;

                                // 親がクラス型の場合
                            } else if (entityUsage.getType() instanceof ClassTypeInfo) {

                                final ClassInfo ownerClass = ((ClassTypeInfo) entityUsage.getType())
                                        .getReferencedClass();

                                // 親が対象クラス(TargetClassInfo)の場合
                                if (ownerClass instanceof TargetClassInfo) {

                                    // まずは利用可能なフィールド一覧を取得
                                    boolean found = false;
                                    {
                                        // 利用可能なフィールド一覧を取得
                                        final List<TargetFieldInfo> availableFields = NameResolver
                                                .getAvailableFields((TargetClassInfo) entityUsage
                                                        .getType(), usingClass);

                                        for (final TargetFieldInfo availableField : availableFields) {

                                            // 一致するフィールド名が見つかった場合
                                            if (name[i].equals(availableField.getName())) {
                                                //usingMethod.addReferencee(availableField);
                                                //availableField.addReferencer(usingMethod);

                                                entityUsage = new FieldUsageInfo(availableField,
                                                        true);
                                                found = true;
                                                break;
                                            }
                                        }
                                    }

                                    // スタティックフィールドで見つからなかった場合は，インナークラスから探す
                                    {
                                        if (!found) {
                                            // インナークラス一覧を取得
                                            final SortedSet<TargetInnerClassInfo> innerClasses = NameResolver
                                                    .getAvailableDirectInnerClasses((TargetClassInfo) entityUsage
                                                            .getType());
                                            for (final TargetInnerClassInfo innerClass : innerClasses) {

                                                // 一致するクラス名が見つかった場合
                                                if (name[i].equals(innerClass.getClassName())) {
                                                    // TODO 利用関係を構築するコードが必要？

                                                    final ClassTypeInfo referenceType = new ClassTypeInfo(
                                                            innerClassInfo);
                                                    entityUsage = new ClassReferenceInfo(
                                                            referenceType);
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

                                            final ClassInfo referencedClass = ((ClassTypeInfo) entityUsage
                                                    .getType()).getReferencedClass();
                                            final ExternalClassInfo externalSuperClass = NameResolver
                                                    .getExternalSuperClass((TargetClassInfo) referencedClass);
                                            if (!(referencedClass instanceof TargetInnerClassInfo)
                                                    && (null != externalSuperClass)) {

                                                final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                        name[i], externalSuperClass);

                                                //usingMethod.addReferencee(fieldInfo);
                                                //fieldInfo.addReferencer(usingMethod);
                                                fieldInfoManager.add(fieldInfo);

                                                entityUsage = new FieldUsageInfo(fieldInfo, true);

                                            } else {
                                                assert false : "Can't resolve entity usage3.5 : "
                                                        + this.toString();
                                            }
                                        }
                                    }

                                    // 親が外部クラス(ExternalClassInfo)の場合
                                } else if (ownerClass instanceof ExternalClassInfo) {

                                    final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                            name[i], ownerClass);

                                    //usingMethod.addReferencee(fieldInfo);
                                    //fieldInfo.addReferencer(usingMethod);
                                    fieldInfoManager.add(fieldInfo);

                                    entityUsage = new FieldUsageInfo(fieldInfo, true);
                                }

                            } else {
                                assert false : "Here shouldn't be reached!";
                            }
                        }

                        this.resolvedIndo = entityUsage;
                        return this.resolvedIndo;
                    }
                }
            }

            // 利用可能な名前空間から検索
            {
                for (final AvailableNamespaceInfo availableNamespace : this
                        .getAvailableNamespaces()) {

                    // 名前空間名.* となっている場合
                    if (availableNamespace.isAllClasses()) {
                        final String[] namespace = availableNamespace.getNamespace();

                        // 名前空間の下にある各クラスに対して
                        for (final ClassInfo classInfo : classInfoManager.getClassInfos(namespace)) {
                            final String className = classInfo.getClassName();

                            // クラス名と参照名の先頭が等しい場合は，そのクラス名が参照先であると決定する
                            if (className.equals(name[0])) {

                                EntityUsageInfo entityUsage = new ClassReferenceInfo(
                                        new ClassTypeInfo(classInfo));
                                for (int i = 1; i < name.length; i++) {

                                    // 親が UnknownTypeInfo だったら，どうしようもない
                                    if (entityUsage.getType() instanceof UnknownTypeInfo) {

                                        this.resolvedIndo = UnknownEntityUsageInfo.getInstance();
                                        return this.resolvedIndo;

                                        // 親がクラス型の場合
                                    } else if (entityUsage.getType() instanceof ClassTypeInfo) {

                                        final ClassInfo ownerClass = ((ClassTypeInfo) entityUsage
                                                .getType()).getReferencedClass();

                                        // 親が対象クラス(TargetClassInfo)の場合
                                        if (ownerClass instanceof TargetClassInfo) {

                                            // まずは利用可能なフィールド一覧を取得
                                            boolean found = false;
                                            {
                                                // 利用可能なフィールド一覧を取得
                                                final List<TargetFieldInfo> availableFields = NameResolver
                                                        .getAvailableFields(
                                                                (TargetClassInfo) entityUsage
                                                                        .getType(), usingClass);

                                                for (TargetFieldInfo availableField : availableFields) {

                                                    // 一致するフィールド名が見つかった場合
                                                    if (name[i].equals(availableField.getName())) {
                                                        //usingMethod.addReferencee(availableField);
                                                        //availableField.addReferencer(usingMethod);

                                                        entityUsage = new FieldUsageInfo(
                                                                availableField, true);
                                                        found = true;
                                                        break;
                                                    }
                                                }
                                            }

                                            // スタティックフィールドで見つからなかった場合は，インナークラスから探す
                                            {
                                                if (!found) {
                                                    // インナークラス一覧を取得
                                                    final SortedSet<TargetInnerClassInfo> innerClasses = NameResolver
                                                            .getAvailableDirectInnerClasses((TargetClassInfo) entityUsage
                                                                    .getType());
                                                    for (final TargetInnerClassInfo innerClass : innerClasses) {

                                                        // 一致するクラス名が見つかった場合
                                                        if (name[i].equals(innerClass
                                                                .getClassName())) {
                                                            // TODO 利用関係を構築するコードが必要？

                                                            final ClassTypeInfo referenceType = new ClassTypeInfo(
                                                                    innerClass);
                                                            entityUsage = new ClassReferenceInfo(
                                                                    referenceType);
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

                                                    final ClassInfo referencedClass = ((ClassTypeInfo) entityUsage
                                                            .getType()).getReferencedClass();
                                                    final ExternalClassInfo externalSuperClass = NameResolver
                                                            .getExternalSuperClass((TargetClassInfo) referencedClass);
                                                    if (!(referencedClass instanceof TargetInnerClassInfo)
                                                            && (null != externalSuperClass)) {

                                                        final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                                name[i], externalSuperClass);

                                                        //usingMethod.addReferencee(fieldInfo);
                                                        //fieldInfo.addReferencer(usingMethod);
                                                        fieldInfoManager.add(fieldInfo);

                                                        entityUsage = new FieldUsageInfo(fieldInfo,
                                                                true);

                                                    } else {
                                                        assert false : "Can't resolve entity usage4 : "
                                                                + this.toString();
                                                    }
                                                }
                                            }

                                            // 親が外部クラス(ExternalClassInfo)の場合
                                        } else if (ownerClass instanceof ExternalClassInfo) {

                                            final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                    name[i], ownerClass);

                                            //usingMethod.addReferencee(fieldInfo);
                                            //fieldInfo.addReferencer(usingMethod);
                                            fieldInfoManager.add(fieldInfo);

                                            entityUsage = new FieldUsageInfo(fieldInfo, true);
                                        }

                                    } else {
                                        assert false : "Here shouldn't be reached!";
                                    }
                                }

                                this.resolvedIndo = entityUsage;
                                return this.resolvedIndo;
                            }
                        }

                        // 名前空間.クラス名 となっている場合
                    } else {

                        final String[] importName = availableNamespace.getImportName();

                        // クラス名と参照名の先頭が等しい場合は，そのクラス名が参照先であると決定する
                        if (importName[importName.length - 1].equals(name[0])) {

                            ClassInfo specifiedClassInfo = classInfoManager
                                    .getClassInfo(importName);
                            if (null == specifiedClassInfo) {
                                specifiedClassInfo = new ExternalClassInfo(importName);
                                classInfoManager.add((ExternalClassInfo) specifiedClassInfo);
                            }

                            EntityUsageInfo entityUsage = new ClassReferenceInfo(new ClassTypeInfo(
                                    specifiedClassInfo));
                            for (int i = 1; i < name.length; i++) {

                                // 親が UnknownTypeInfo だったら，どうしようもない
                                if (entityUsage.getType() instanceof UnknownTypeInfo) {

                                    this.resolvedIndo = UnknownEntityUsageInfo.getInstance();
                                    return this.resolvedIndo;

                                    // 親がクラス型の場合
                                } else if (entityUsage.getType() instanceof ClassTypeInfo) {

                                    final ClassInfo ownerClass = ((ClassTypeInfo) entityUsage
                                            .getType()).getReferencedClass();

                                    // 親が対象クラス(TargetClassInfo)の場合
                                    if (ownerClass instanceof TargetClassInfo) {

                                        // まずは利用可能なフィールド一覧を取得
                                        boolean found = false;
                                        {
                                            // 利用可能なフィールド一覧を取得
                                            final List<TargetFieldInfo> availableFields = NameResolver
                                                    .getAvailableFields(
                                                            (TargetClassInfo) entityUsage.getType(),
                                                            usingClass);

                                            for (final TargetFieldInfo availableField : availableFields) {

                                                // 一致するフィールド名が見つかった場合
                                                if (name[i].equals(availableField.getName())) {
                                                    //usingMethod.addReferencee(availableField);
                                                    //availableField.addReferencer(usingMethod);

                                                    entityUsage = new FieldUsageInfo(
                                                            availableField, true);
                                                    found = true;
                                                    break;
                                                }
                                            }
                                        }

                                        // スタティックフィールドで見つからなかった場合は，インナークラスから探す
                                        {
                                            if (!found) {
                                                // インナークラス一覧を取得
                                                final SortedSet<TargetInnerClassInfo> innerClasses = NameResolver
                                                        .getAvailableDirectInnerClasses((TargetClassInfo) entityUsage
                                                                .getType());
                                                for (final TargetInnerClassInfo innerClass : innerClasses) {

                                                    // 一致するクラス名が見つかった場合
                                                    if (name[i].equals(innerClass.getClassName())) {
                                                        // TODO 利用関係を構築するコードが必要？

                                                        final ClassTypeInfo referenceType = new ClassTypeInfo(
                                                                innerClass);
                                                        entityUsage = new ClassReferenceInfo(
                                                                referenceType);
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

                                                final ClassInfo referencedClass = ((ClassTypeInfo) entityUsage
                                                        .getType()).getReferencedClass();
                                                final ExternalClassInfo externalSuperClass = NameResolver
                                                        .getExternalSuperClass((TargetClassInfo) referencedClass);
                                                if (!(referencedClass instanceof TargetInnerClassInfo)
                                                        && (null != externalSuperClass)) {

                                                    final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                            name[i], externalSuperClass);

                                                    //usingMethod.addReferencee(fieldInfo);
                                                    //fieldInfo.addReferencer(usingMethod);
                                                    fieldInfoManager.add(fieldInfo);

                                                    entityUsage = new FieldUsageInfo(fieldInfo,
                                                            true);

                                                } else {
                                                    assert false : "Can't resolve entity usage5 : "
                                                            + this.toString();
                                                }
                                            }
                                        }

                                        // 親が外部クラス(ExternalClassInfo)の場合
                                    } else if (ownerClass instanceof ExternalClassInfo) {

                                        final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                name[i], ownerClass);

                                        //usingMethod.addReferencee(fieldInfo);
                                        //fieldInfo.addReferencer(usingMethod);
                                        fieldInfoManager.add(fieldInfo);

                                        entityUsage = new FieldUsageInfo(fieldInfo, true);
                                    }

                                } else {
                                    assert false : "Here shouldn't be reached!";
                                }
                            }

                            this.resolvedIndo = entityUsage;
                            return this.resolvedIndo;
                        }
                    }
                }
            }
        }

        err.println("Remain unresolved \"" + this.toString() + "\"" + " on \""
                + usingClass.getFullQualifiedName(LANGUAGE.JAVA.getNamespaceDelimiter()) + "#"
                + usingMethod.getMethodName() + "\".");

        // 見つからなかった処理を行う
        usingMethod.addUnresolvedUsage(this);

        this.resolvedIndo = UnknownEntityUsageInfo.getInstance();
        return this.resolvedIndo;
    }

    /**
     * 未解決エンティティ使用名を返す．
     * 
     * @return 未解決エンティティ使用名
     */
    public String[] getName() {
        return this.name;
    }

    /**
     * この未解決エンティティ使用が利用することのできる名前空間を返す．
     * 
     * @return この未解決エンティティ使用が利用することのできる名前空間
     */
    public AvailableNamespaceInfoSet getAvailableNamespaces() {
        return this.availableNamespaces;
    }

    /**
     * この未解決エンティティ使用が利用することのできる名前空間を保存するための変数
     */
    private final AvailableNamespaceInfoSet availableNamespaces;

    /**
     * この未解決エンティティ使用名を保存するための変数
     */
    private final String[] name;

    /**
     * 解決済みエンティティ使用を保存するための変数
     */
    private EntityUsageInfo resolvedIndo;

    /**
     * エラーメッセージ出力用のプリンタ
     */
    private static final MessagePrinter err = new DefaultMessagePrinter(new MessageSource() {
        public String getMessageSourceName() {
            return "UnresolvedUnknownEntityUsage";
        }
    }, MESSAGE_TYPE.ERROR);
}
