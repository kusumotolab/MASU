package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


public interface InnerClassInfo extends HavingOuterUnit {

    /**
     * 外側のクラスを返す.
     * 
     * @return　外側のクラス
     */
    ClassInfo getOuterClass();

    /**
     * 外側の呼び出し可能なユニット（メソッド，コンストラクタ等）を返す
     * 
     * @return 外側の呼び出し可能なユニット（メソッド，コンストラクタ等）
     */
    CallableUnitInfo getOuterCallableUnit();
}
