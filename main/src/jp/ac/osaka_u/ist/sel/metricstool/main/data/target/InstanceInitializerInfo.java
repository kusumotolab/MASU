package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;


/**
 * インスタンスイニシャライザーを表すクラス
 * 
 * @author t-miyake
 *
 */
public class InstanceInitializerInfo extends CallableUnitInfo {

    /**
     * 
     */
    private static final long serialVersionUID = 5833181372993442712L;

    /**
     * オブジェクトを初期化
     * 
     * @param ownerClass オーナークラス
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public InstanceInitializerInfo(final ClassInfo ownerClass, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(Collections.EMPTY_SET, ownerClass, true, false, false, false, fromLine, fromColumn,
                toLine, toColumn);
    }

    @Override
    public final String getSignatureText() {
        return "";
    }
}
