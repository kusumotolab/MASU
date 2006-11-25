package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricAlreadyRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;


/**
 * このインターフェースは，クラスメトリクスを登録するためのメソッド群を提供する．
 * 
 * @author y-higo
 * 
 */
public interface ClassMetricsRegister {

    /**
     * 第一引数のクラスのメトリクス値（第二引数）を登録する
     * 
     * @param classInfo メトリクスの計測対象クラス
     * @param value メトリクス値
     * @throws 登録しようとしているメトリクスが既に登録されている場合にスローされる
     */
    void registMetric(ClassInfo classInfo, int value) throws MetricAlreadyRegisteredException;
}
