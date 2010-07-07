package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * 外側のユニットが存在することを表すインターフェース
 * 
 * @author higo
 *
 */
public interface HavingOuterUnit {

    /**
     * 外側のユニットを返す
     * 
     * @return 外側のユニット
     */
    UnitInfo getOuterUnit();
    
    /**
     * 外側のユニットを設定する
     * 
     * @param outerUnit 外側のユニット
     */
    void setOuterUnit(UnitInfo outerUnit);
}
