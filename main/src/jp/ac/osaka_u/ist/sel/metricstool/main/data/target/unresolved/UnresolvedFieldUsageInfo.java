package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayLengthUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetInnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;


/**
 * 未解決フィールド使用を保存するためのクラス
 * 
 * @author y-higo
 * 
 */
public final class UnresolvedFieldUsageInfo implements UnresolvedEntityUsageInfo {

    /**
     * フィールド使用が実行される変数の型名と変数名，利用可能な名前空間を与えてオブジェクトを初期化
     * 
     * @param availableNamespaces 利用可能な名前空間
     * @param ownerClassType フィールド使用が実行される変数の型名
     * @param fieldName 変数名
     * @param reference フィールド使用が参照である場合は true，代入である場合は false を指定
     */
    public UnresolvedFieldUsageInfo(final AvailableNamespaceInfoSet availableNamespaces,
            final UnresolvedEntityUsageInfo ownerClassType, final String fieldName,
            final boolean reference) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == availableNamespaces) || (null == ownerClassType) || (null == fieldName)) {
            throw new NullPointerException();
        }

        this.availableNamespaces = availableNamespaces;
        this.ownerClassType = ownerClassType;
        this.fieldName = fieldName;
        this.reference = reference;

        this.resolvedInfo = null;
    }

    /**
     * この未解決フィールド使用が既に解決されているかどうかを返す
     * 
     * @return 既に解決されている場合は true, そうでない場合は false
     */
    public boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * 解決済みフィールド使用を返す
     */
    public EntityUsageInfo getResolvedEntityUsage() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
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
    public EntityUsageInfo resolveEntityUsage(final TargetClassInfo usingClass,
            final TargetMethodInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == fieldInfoManager) || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolvedEntityUsage();
        }

        // フィールド名，参照・代入を取得
        final String fieldName = this.getFieldName();
        final boolean reference = this.isReference();

        // 親の型を解決
        final UnresolvedEntityUsageInfo unresolvedOwnerUsage = this.getOwnerClassType();
        final EntityUsageInfo ownerUsage = unresolvedOwnerUsage.resolveEntityUsage(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        assert ownerUsage != null : "resolveEntityUsage returned null!";

        // -----ここから親の型 に応じて処理を分岐
        // 親が解決できなかった場合はどうしようもない
        if (ownerUsage.getType() instanceof UnknownTypeInfo) {

            // 見つからなかった処理を行う
            usingMethod.addUnresolvedUsage(this);

            this.resolvedInfo = UnknownEntityUsageInfo.getInstance();
            return this.resolvedInfo;

            // 親が対象クラス(TargetClassInfo)だった場合
        } else if (ownerUsage.getType() instanceof TargetClassInfo) {

            // まずは利用可能なフィールドから検索
            {
                // 利用可能なフィールド一覧を取得
                final List<TargetFieldInfo> availableFields = NameResolver.getAvailableFields(
                        (TargetClassInfo) ownerUsage.getType(), usingClass);

                // 利用可能なフィールドを，未解決フィールド名で検索
                for (final TargetFieldInfo availableField : availableFields) {

                    // 一致するフィールド名が見つかった場合
                    if (fieldName.equals(availableField.getName())) {

                        if (reference) {
                            usingMethod.addReferencee(availableField);
                            availableField.addReferencer(usingMethod);
                        } else {
                            usingMethod.addAssignmentee(availableField);
                            availableField.addAssignmenter(usingMethod);
                        }

                        this.resolvedInfo = new FieldUsageInfo(availableField, reference);
                        return this.resolvedInfo;
                    }
                }
            }

            // 利用可能なフィールドが見つからなかった場合は，外部クラスである親クラスがあるはず
            // そのクラスの変数を使用しているとみなす
            {
                for (TargetClassInfo classInfo = (TargetClassInfo) ownerUsage.getType(); true; classInfo = ((TargetInnerClassInfo) classInfo)
                        .getOuterClass()) {

                    final ExternalClassInfo externalSuperClass = NameResolver
                            .getExternalSuperClass(classInfo);
                    if (null != externalSuperClass) {

                        final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(fieldName,
                                externalSuperClass);
                        if (reference) {
                            usingMethod.addReferencee(fieldInfo);
                            fieldInfo.addReferencer(usingMethod);
                        } else {
                            usingMethod.addAssignmentee(fieldInfo);
                            fieldInfo.addAssignmenter(usingMethod);
                        }
                        fieldInfoManager.add(fieldInfo);

                        // 外部クラスに新規で外部変数(ExternalFieldInfo)を追加したので型は不明．
                        this.resolvedInfo = new FieldUsageInfo(fieldInfo, reference);
                        return this.resolvedInfo;
                    }

                    if (!(classInfo instanceof TargetInnerClassInfo)) {
                        break;
                    }
                }
            }

            // 見つからなかった処理を行う
            {
                assert false : "Can't resolve field reference : " + this.getFieldName();

                usingMethod.addUnresolvedUsage(this);

                this.resolvedInfo = UnknownEntityUsageInfo.getInstance();
                return this.resolvedInfo;
            }

            // 親が外部クラス（ExternalClassInfo）だった場合
        } else if (ownerUsage.getType() instanceof ExternalClassInfo) {

            final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(fieldName,
                    (ExternalClassInfo) ownerUsage.getType());
            if (reference) {
                usingMethod.addReferencee(fieldInfo);
                fieldInfo.addReferencer(usingMethod);
            } else {
                usingMethod.addAssignmentee(fieldInfo);
                fieldInfo.addAssignmenter(usingMethod);
            }
            fieldInfoManager.add(fieldInfo);

            // 外部クラスに新規で外部変数(ExternalFieldInfo)を追加したので型は不明．
            this.resolvedInfo = new FieldUsageInfo(fieldInfo, reference);
            return this.resolvedInfo;

        } else if (ownerUsage.getType() instanceof ArrayTypeInfo) {

            // TODO ここは言語依存にするしかないのか？ 配列.length など

            // Java 言語で フィールド名が length だった場合は int 型を返す
            if (Settings.getLanguage().equals(LANGUAGE.JAVA) && fieldName.equals("length")) {
                this.resolvedInfo = new ArrayLengthUsageInfo(ownerUsage);
                return this.resolvedInfo;
            }
        }

        assert false : "Here shouldn't be reached!";
        this.resolvedInfo = UnknownEntityUsageInfo.getInstance();
        return this.resolvedInfo;
    }

    /**
     * この未解決フィールド使用の型名を返す
     * 
     * @return この未解決フィールド使用の型名
     */
    public String getTypeName() {
        return this.getFieldName();
    }

    /**
     * 使用可能な名前空間を返す
     * 
     * @return 使用可能な名前空間を返す
     */
    public AvailableNamespaceInfoSet getAvailableNamespaces() {
        return this.availableNamespaces;
    }

    /**
     * フィールド使用が実行される変数の未解決型名を返す
     * 
     * @return フィールド使用が実行される変数の未解決型名
     */
    public UnresolvedEntityUsageInfo getOwnerClassType() {
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
     * このフィールド使用が参照であるかどうかを返す
     * 
     * @return 参照である場合は true，代入である場合は false
     */
    public boolean isReference() {
        return this.reference;
    }

    /**
     * このフィールド使用が代入であるかどうかを返す
     * 
     * @return 代入である場合は true，参照である場合は false
     */
    public boolean isAssignment() {
        return !this.reference;
    }

    /**
     * 使用可能な名前空間を保存するための変数
     */
    private final AvailableNamespaceInfoSet availableNamespaces;

    /**
     * フィールド使用が実行される変数の未解決型名を保存するための変数
     */
    private final UnresolvedEntityUsageInfo ownerClassType;

    /**
     * フィールド名を保存するための変数
     */
    private final String fieldName;

    /**
     * フィールド使用の参照・代入を表す変数
     */
    private final boolean reference;

    /**
     * 解決済みフィールド使用を保存するための変数
     */
    private EntityUsageInfo resolvedInfo;
}
