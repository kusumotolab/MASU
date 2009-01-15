package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * スタティックイニシャライザを表すクラス
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class StaticInitializerInfo extends LocalSpaceInfo {

    /**
     * 必要な情報を与えて，オブジェクトを初期化
     * 
     * @param ownerClass 所有クラス
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public StaticInitializerInfo(final ClassInfo ownerClass, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(ownerClass, fromLine, fromColumn, toLine, toColumn);
    }
}
