package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;




/**
 * インナークラスを表すクラス．
 * 
 * @author y-higo
 */
public final class TargetInnerClassInfo extends TargetClassInfo {

    /**
     * インナークラスオブジェクトを初期化する
     * 
     * @param modifier 修飾子
     * @param namespace 名前空間
     * @param className クラス名
     * @param outerClass 外側のクラス
     * @param loc 行数
     */
    public TargetInnerClassInfo(final ModifierInfo modifier, final NamespaceInfo namespace,
            final String className, final TargetClassInfo outerClass, final int loc) {

        super(modifier, namespace, className, loc);

        if (null == outerClass) {
            throw new NullPointerException();
        }

        this.outerClass = outerClass;
    }

    /**
     * インナークラスオブジェクトを初期化する
     * 
     * @param modifier 修飾子
     * @param fullQualifiedName 完全限定名
     * @param outerClass 外側のクラス
     * @param loc 行数
     */
    public TargetInnerClassInfo(final ModifierInfo modifier, final String[] fullQualifiedName,
            final TargetClassInfo outerClass, final int loc) {

        super(modifier, fullQualifiedName, loc);

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
