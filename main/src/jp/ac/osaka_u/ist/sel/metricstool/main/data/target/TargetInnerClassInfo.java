package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;


/**
 * インナークラスを表すクラス．
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public class TargetInnerClassInfo extends TargetClassInfo implements InnerClassInfo {

    /**
     * インナークラスオブジェクトを初期化する
     * 
     * @param modifiers 修飾子名の Set
     * @param namespace 名前空間
     * @param className クラス名
     * @param outerUnit 外側のユニット，TargetClassInfo　もしくは TargetMethodInfo でなければならない
     * @param privateVisible クラス内からのみ参照可能
     * @param namespaceVisible 同じ名前空間から参照可能
     * @param inheritanceVisible 子クラスから参照可能
     * @param publicVisible どこからでも参照可能
     * @param instance インスタンスメンバーかどうか
     * @param isInterface インタフェースかどうか
     * @param fileInfo このクラスを宣言しているファイル情報
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public TargetInnerClassInfo(final Set<ModifierInfo> modifiers, final NamespaceInfo namespace,
            final String className, final UnitInfo outerUnit, final boolean privateVisible,
            final boolean namespaceVisible, final boolean inheritanceVisible,
            final boolean publicVisible, final boolean instance, final boolean isInterface,
            final FileInfo fileInfo, final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {

        super(modifiers, namespace, className, privateVisible, namespaceVisible,
                inheritanceVisible, publicVisible, instance, isInterface, fileInfo, fromLine,
                fromColumn, toLine, toColumn);

        if (null == outerUnit) {
            throw new NullPointerException();
        }

        if (!(outerUnit instanceof TargetClassInfo) && !(outerUnit instanceof TargetMethodInfo)) {
            throw new IllegalArgumentException();
        }

        this.outerUnit = outerUnit;
    }

    /**
     * インナークラスオブジェクトを初期化する
     * 
     * @param modifiers 修飾子名の Set
     * @param fullQualifiedName 完全限定名
     * @param outerUnit 外側のユニット，TargetClassInfo　もしくは TargetMethodInfo でなければならない
     * @param privateVisible クラス内からのみ参照可能
     * @param namespaceVisible 同じ名前空間から参照可能
     * @param inheritanceVisible 子クラスから参照可能
     * @param publicVisible どこからでも参照可能
     * @param instance インスタンスメンバーかどうか
     * @param isInterface インタフェースかどうか
     * @param fileInfo このクラスを宣言しているファイル情報
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public TargetInnerClassInfo(final Set<ModifierInfo> modifiers,
            final String[] fullQualifiedName, final UnitInfo outerUnit,
            final boolean privateVisible, final boolean namespaceVisible,
            final boolean inheritanceVisible, final boolean publicVisible, final boolean instance,
            final boolean isInterface, final FileInfo fileInfo, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(modifiers, fullQualifiedName, privateVisible, namespaceVisible, inheritanceVisible,
                publicVisible, instance, isInterface, fileInfo, fromLine, fromColumn, toLine,
                toColumn);

        if (null == outerUnit) {
            throw new NullPointerException();
        }

        if (!(outerUnit instanceof TargetClassInfo) && !(outerUnit instanceof TargetMethodInfo)
                && !(outerUnit instanceof TargetConstructorInfo)
                && !(outerUnit instanceof InitializerInfo)) {
            throw new IllegalArgumentException();
        }

        this.outerUnit = outerUnit;
    }

    /**
     * 外側のユニットを返す
     * 
     * @return 外側のユニット
     */
    @Override
    public final UnitInfo getOuterUnit() {
        return this.outerUnit;
    }

    /**
     * 外側のクラスを返す.
     * つまり，getOuterUnit の返り値がTargetClassInfoである場合は，そのオブジェクトを返し，
     * 返り値が，TargetMethodInfoである場合は，そのオブジェクトの ownerClass を返す．
     * 
     * @return　外側のクラス
     */
    @Override
    public final ClassInfo getOuterClass() {

        final UnitInfo unitInfo = this.getOuterUnit();

        // 外側のユニットがクラスであればそのまま返す
        if (unitInfo instanceof TargetClassInfo) {
            return (TargetClassInfo) unitInfo;

            // 外側のユニットがメソッドであれば，その所有クラスを返す
        } else if (unitInfo instanceof TargetMethodInfo) {

            final ClassInfo ownerClass = ((TargetMethodInfo) unitInfo).getOwnerClass();
            return (TargetClassInfo) ownerClass;
        }

        assert false : "here shouldn't be reached!";
        return null;
    }

    /**
     * 外側のユニットのオブジェクトを保存する変数
     */
    private final UnitInfo outerUnit;
}
