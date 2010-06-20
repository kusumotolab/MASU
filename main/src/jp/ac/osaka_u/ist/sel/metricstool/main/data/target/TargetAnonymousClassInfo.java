package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.HashSet;


/**
 * 無名インナークラスを現すクラス
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class TargetAnonymousClassInfo extends TargetInnerClassInfo implements
        AnonymousClassInfo {

    /**
     * 無名インナークラスオブジェクトを初期化する
     * 
     * @param namespace 名前空間
     * @param className クラス名
     * @param outerClass 外側のクラス
     * @param fileInfo このクラスを宣言しているファイル情報
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public TargetAnonymousClassInfo(final NamespaceInfo namespace, final String className,
            final TargetClassInfo outerClass, final FileInfo fileInfo, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(new HashSet<ModifierInfo>(), namespace, className, outerClass, false, false, false,
                false, true, false, fileInfo, fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * 無名インナークラスオブジェクトを初期化する
     * 
     * @param fullQualifiedName 完全限定名
     * @param outerClass 外側のクラス
     * @param fileInfo このクラスを宣言しているファイル情報
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public TargetAnonymousClassInfo(final String[] fullQualifiedName,
            final TargetClassInfo outerClass, final FileInfo fileInfo, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(new HashSet<ModifierInfo>(), fullQualifiedName, outerClass, false, false, false,
                false, true, false, fileInfo, fromLine, fromColumn, toLine, toColumn);
    }

    public void setOuterCallableUnit(final CallableUnitInfo outerCallableUnit) {
        if (null == outerCallableUnit) {
            throw new IllegalArgumentException();
        }
        this.outerCallableUnit = outerCallableUnit;
    }

    public CallableUnitInfo getOuterCallableUnit() {
        return this.outerCallableUnit;
    }

    /**
     * 外側のユニットを返す
     * 
     * @return 外側のユニット
     */
    @Override
    public UnitInfo getOuterUnit() {
        return this.getOuterCallableUnit();
    }

    private CallableUnitInfo outerCallableUnit;
}
