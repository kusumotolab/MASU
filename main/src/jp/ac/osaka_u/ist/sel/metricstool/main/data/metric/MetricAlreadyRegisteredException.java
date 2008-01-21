package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


/**
 * 既に登録しているメトリクス情報を再度登録しようとした場合に，スローされる例外
 * 
 * @author higo 
 */
public class MetricAlreadyRegisteredException extends Exception {

    public MetricAlreadyRegisteredException() {
        super();
    }

    public MetricAlreadyRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }

    public MetricAlreadyRegisteredException(String message) {
        super(message);
    }

    public MetricAlreadyRegisteredException(Throwable cause) {
        super(cause);
    }

}
