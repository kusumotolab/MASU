package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;


/**
 * メソッドメトリクスを登録するためのデータクラス
 * 
 * @author higo
 * 
 */
public final class MethodMetricsInfo extends MetricsInfo<MethodInfo> {

    /**
     * 計測対象オブジェクトを与えて初期化
     * 
     * @param 計測対象メソッド
     */
    public MethodMetricsInfo(final MethodInfo method) {
        super(method);
    }

    /**
     * メッセージの送信者名を返す
     * 
     * @return メッセージの送信者名
     */
    public String getMessageSourceName() {
        return this.getClass().getName();
    }
}
