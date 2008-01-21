package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


/**
 * 登録されていないメトリクス情報にアクセスした場合にスローされる例外
 * 
 * @author higo
 *
 */
public class MetricNotRegisteredException extends Exception {

    public MetricNotRegisteredException() {
        super();
    }

    public MetricNotRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }

    public MetricNotRegisteredException(String message) {
        super(message);
    }

    public MetricNotRegisteredException(Throwable cause) {
        super(cause);
    }

}
