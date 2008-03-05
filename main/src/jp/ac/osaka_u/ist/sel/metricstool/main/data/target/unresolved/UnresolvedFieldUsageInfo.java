package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.List;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetInnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決フィールド使用を保存するためのクラス
 * 
 * @author higo
 * 
 */
public final class UnresolvedFieldUsageInfo extends UnresolvedVariableUsageInfo<FieldUsageInfo> {

    /**
     * フィールド使用が実行される変数の型名と変数名，利用可能な名前空間を与えてオブジェクトを初期化
     * 
     * @param availableNamespaces 利用可能な名前空間
     * @param ownerClassType フィールド使用が実行される変数の型名
     * @param fieldName 変数名
     * @param reference フィールド使用が参照である場合は true，代入である場合は false を指定
     */
    public UnresolvedFieldUsageInfo(final Set<AvailableNamespaceInfo> availableNamespaces,
            final UnresolvedEntityUsageInfo<ClassReferenceInfo> ownerClassType,
            final String fieldName, final boolean reference, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(fieldName, reference, fromLine, fromColumn, toLine, toColumn);

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == availableNamespaces) || (null == ownerClassType) || (null == fieldName)) {
            throw new NullPointerException();
        }

        this.availableNamespaces = availableNamespaces;
        this.ownerClassType = ownerClassType;
        this.fieldName = fieldName;
        this.reference = reference;
    }

    /**
     * 未解決フィールド使用を解決し，その型を返す．
     * 
     * @param usingClass 未解決フィールド使用が行われているクラス
     * @param usingMethod 未解決フィールド使用が行われているメソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     * @return 解決済みフィールド使用
     */
    @Override
    public FieldUsageInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == fieldInfoManager) || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // フィールド名，参照・代入を取得
        final String fieldName = this.getFieldName();
        final boolean reference = this.isReference();

        // 使用位置を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // 親の型を解決
        final UnresolvedEntityUsageInfo<?> unresolvedOwnerUsage = this.getOwnerClassType();
        final EntityUsageInfo ownerUsage = unresolvedOwnerUsage.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        assert ownerUsage != null : "resolveEntityUsage returned null!";

        // -----ここから親の型に応じて処理を分岐
        TypeInfo ownerType = ownerUsage.getType();

        // 型パラメータの場合はその継承型を求める
        if (ownerType instanceof TypeParameterInfo) {
            final TypeInfo extendsType = ((TypeParameterInfo) ownerType).getExtendsType();
            if (null != extendsType) {
                ownerType = extendsType;
            } else {
                assert false : "Here should not be reached";

                final ExternalFieldInfo unknownField = new ExternalFieldInfo(fieldName);
                this.resolved = new FieldUsageInfo(UnknownTypeInfo.getInstance(), unknownField,
                        reference, fromLine, fromColumn, toLine, toColumn);
                return this.resolved;
            }
        }

        // -----ここから親の型 に応じて処理を分岐
        // 親が解決できなかった場合はどうしようもない
        if (ownerType instanceof UnknownTypeInfo) {

            final ExternalFieldInfo unknownField = new ExternalFieldInfo(fieldName);
            this.resolved = new FieldUsageInfo(UnknownTypeInfo.getInstance(), unknownField,
                    reference, fromLine, fromColumn, toLine, toColumn);
            return this.resolved;

            //親がクラス型の場合
        } else if (ownerType instanceof ClassTypeInfo) {

            final ClassInfo ownerClass = ((ClassTypeInfo) ownerUsage.getType())
                    .getReferencedClass();
            // 親が対象クラス(TargetClassInfo)だった場合
            if (ownerClass instanceof TargetClassInfo) {

                // まずは利用可能なフィールドから検索
                {
                    // 利用可能なフィールド一覧を取得
                    final List<TargetFieldInfo> availableFields = NameResolver.getAvailableFields(
                            (TargetClassInfo) ownerClass, usingClass);

                    // 利用可能なフィールドを，未解決フィールド名で検索
                    for (final TargetFieldInfo availableField : availableFields) {

                        // 一致するフィールド名が見つかった場合
                        if (fieldName.equals(availableField.getName())) {

                            this.resolved = new FieldUsageInfo(ownerUsage.getType(),
                                    availableField, reference, fromLine, fromColumn, toLine,
                                    toColumn);
                            return this.resolved;
                        }
                    }
                }

                // 利用可能なフィールドが見つからなかった場合は，外部クラスである親クラスがあるはず
                // そのクラスの変数を使用しているとみなす
                {
                    for (TargetClassInfo classInfo = (TargetClassInfo) ownerClass; true; classInfo = ((TargetInnerClassInfo) classInfo)
                            .getOuterClass()) {

                        final ExternalClassInfo externalSuperClass = NameResolver
                                .getExternalSuperClass(classInfo);
                        if (null != externalSuperClass) {

                            final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(fieldName,
                                    externalSuperClass);
                            fieldInfoManager.add(fieldInfo);

                            // 外部クラスに新規で外部変数(ExternalFieldInfo)を追加したので型は不明．
                            this.resolved = new FieldUsageInfo(ownerUsage.getType(), fieldInfo,
                                    reference, fromLine, fromColumn, toLine, toColumn);
                            return this.resolved;
                        }

                        if (!(classInfo instanceof TargetInnerClassInfo)) {
                            break;
                        }
                    }
                }

                // 見つからなかった処理を行う
                {
                    assert false : "Can't resolve field reference : " + this.getFieldName();

                    final ExternalFieldInfo unknownField = new ExternalFieldInfo(fieldName);
                    this.resolved = new FieldUsageInfo(UnknownTypeInfo.getInstance(), unknownField,
                            reference, fromLine, fromColumn, toLine, toColumn);
                    return this.resolved;
                }

                // 親が外部クラス（ExternalClassInfo）だった場合
            } else if (ownerClass instanceof ExternalClassInfo) {

                final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(fieldName, ownerClass);
                fieldInfoManager.add(fieldInfo);

                // 外部クラスに新規で外部変数(ExternalFieldInfo)を追加したので型は不明．
                this.resolved = new FieldUsageInfo(ownerUsage.getType(), fieldInfo, reference,
                        fromLine, fromColumn, toLine, toColumn);
                return this.resolved;
            }

        } else if (ownerType instanceof ArrayTypeInfo) {

            // TODO ここは言語依存にするしかないのか？ 配列.length など

            // Java 言語で フィールド名が length だった場合は int 型を返す
            // TODO　ちゃんとかきなおさないといけない
            // if (Settings.getLanguage().equals(LANGUAGE.JAVA) && fieldName.equals("length")) {
            //this.resolved = new ArrayLengthUsageInfo(ownerUsage, fromLine, fromColumn, toLine,
            //            toColumn);
            //    return this.resolved;
            //}
        }

        assert false : "Here shouldn't be reached!";
        final ExternalFieldInfo unknownField = new ExternalFieldInfo(fieldName);
        this.resolved = new FieldUsageInfo(UnknownTypeInfo.getInstance(), unknownField, reference,
                fromLine, fromColumn, toLine, toColumn);
        return this.resolved;
    }

    /**
     * 使用可能な名前空間を返す
     * 
     * @return 使用可能な名前空間を返す
     */
    public Set<AvailableNamespaceInfo> getAvailableNamespaces() {
        return this.availableNamespaces;
    }

    /**
     * フィールド使用が実行される変数の未解決型名を返す
     * 
     * @return フィールド使用が実行される変数の未解決型名
     */
    public UnresolvedEntityUsageInfo<ClassReferenceInfo> getOwnerClassType() {
        return this.ownerClassType;
    }

    /**
     * フィールド名を返す
     * 
     * @return フィールド名
     */
    public String getFieldName() {
        return this.fieldName;
    }

    /**
     * 使用可能な名前空間を保存するための変数
     */
    private final Set<AvailableNamespaceInfo> availableNamespaces;

    /**
     * フィールド使用が実行される変数の未解決型名を保存するための変数
     */
    private final UnresolvedEntityUsageInfo<ClassReferenceInfo> ownerClassType;

    /**
     * フィールド名を保存するための変数
     */
    private final String fieldName;
}
