package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * ラベル定義を表すクラス
 * 
 * @author higo
 *
 */
public final class LabelInfo extends UnitInfo {

    /**
     * 位置情報を与えてラベルオブジェクトを初期化
     * 
     * @param name ラベル名
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public LabelInfo(final String name, final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);

        this.name = name;

    }

    /**
     * このラベルの名前を返す
     * 
     * @return このラベルの名前
     */
    public String getName() {
        return this.name;
    }

    private final String name;
}
