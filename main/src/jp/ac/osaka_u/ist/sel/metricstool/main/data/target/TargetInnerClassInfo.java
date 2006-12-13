package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;


/**
 * インナークラスを表すクラス．
 * 
 * @author y-higo
 */
public final class TargetInnerClassInfo extends TargetClassInfo {

    /**
     * インナークラスオブジェクトを初期化する
     * 
     * @param modifiers 修飾子名の Set
     * @param namespace 名前空間
     * @param className クラス名
     * @param outerClass 外側のクラス
     * @param loc 行数
     * @param privateVisible クラス内からのみ参照可能
     * @param namespaceVisible 同じ名前空間から参照可能
     * @param inheritanceVisible 子クラスから参照可能
     * @param publicVisible どこからでも参照可能
     * @param instance インスタンスメンバーかどうか
     */
    public TargetInnerClassInfo(final Set<ModifierInfo> modifiers, final NamespaceInfo namespace,
            final String className, final TargetClassInfo outerClass, final int loc,
            final boolean privateVisible, final boolean namespaceVisible,
            final boolean inheritanceVisible, final boolean publicVisible, final boolean instance) {

        super(modifiers, namespace, className, loc, privateVisible, namespaceVisible,
                inheritanceVisible, publicVisible, instance);

        if (null == outerClass) {
            throw new NullPointerException();
        }

        this.outerClass = outerClass;
    }

    /**
     * インナークラスオブジェクトを初期化する
     * 
     * @param modifiers 修飾子名の Set
     * @param fullQualifiedName 完全限定名
     * @param outerClass 外側のクラス
     * @param loc 行数
     * @param privateVisible クラス内からのみ参照可能
     * @param namespaceVisible 同じ名前空間から参照可能
     * @param inheritanceVisible 子クラスから参照可能
     * @param publicVisible どこからでも参照可能
     * @param instance インスタンスメンバーかどうか
     */
    public TargetInnerClassInfo(final Set<ModifierInfo> modifiers,
            final String[] fullQualifiedName, final TargetClassInfo outerClass, final int loc,
            final boolean privateVisible, final boolean namespaceVisible,
            final boolean inheritanceVisible, final boolean publicVisible, final boolean instance) {

        super(modifiers, fullQualifiedName, loc, privateVisible, namespaceVisible,
                inheritanceVisible, publicVisible, instance);

        if (null == outerClass) {
            throw new NullPointerException();
        }

        this.outerClass = outerClass;
    }

    /**
     * 外側のクラスのオブジェクトを返す
     * 
     * @return
     */
    public TargetClassInfo getOuterClass() {
        return this.outerClass;
    }

    /**
     * 外側のクラスのオブジェクトを保存する変数
     */
    private final TargetClassInfo outerClass;
}
