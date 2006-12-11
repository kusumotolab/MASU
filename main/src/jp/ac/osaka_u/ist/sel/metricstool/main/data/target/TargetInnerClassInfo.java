package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.TreeSet;


/**
 * インナークラスを表すクラス．
 * 
 * @author y-higo
 */
public final class TargetInnerClassInfo extends TargetClassInfo {

    /**
     * インナークラスオブジェクトを初期化する
     * 
     * @param namespace 名前空間
     * @param className クラス名
     * @param outerClass 外側のクラス
     * @param loc 行数
     */
    public TargetInnerClassInfo(final NamespaceInfo namespace, final String className,
            final TargetClassInfo outerClass, final int loc) {

        super(namespace, className, loc);

        if (null == outerClass) {
            throw new NullPointerException();
        }

        this.outerClass = outerClass;
    }

    /**
     * インナークラスオブジェクトを初期化する
     * 
     * @param fullQualifiedName 完全限定名
     * @param outerClass 外側のクラス
     * @param loc 行数
     */
    public TargetInnerClassInfo(final String[] fullQualifiedName, final TargetClassInfo outerClass,
            final int loc) {

        super(fullQualifiedName, loc);

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
