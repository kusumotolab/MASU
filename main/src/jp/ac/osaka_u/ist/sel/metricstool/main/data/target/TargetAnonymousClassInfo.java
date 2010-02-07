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
     * @param outerUnit 外側のユニット
     * @param fileInfo このクラスを宣言しているファイル情報
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public TargetAnonymousClassInfo(final NamespaceInfo namespace, final String className,
            final UnitInfo outerUnit, final FileInfo fileInfo, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(new HashSet<ModifierInfo>(), namespace, className, outerUnit, false, false, false,
                false, true, false, fileInfo, fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * 無名インナークラスオブジェクトを初期化する
     * 
     * @param fullQualifiedName 完全限定名
     * @param outerUnit 外側のユニット
     * @param fileInfo このクラスを宣言しているファイル情報
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public TargetAnonymousClassInfo(final String[] fullQualifiedName, final UnitInfo outerUnit,
            final FileInfo fileInfo, final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {

        super(new HashSet<ModifierInfo>(), fullQualifiedName, outerUnit, false, false, false,
                false, true, false, fileInfo, fromLine, fromColumn, toLine, toColumn);
    }
}
