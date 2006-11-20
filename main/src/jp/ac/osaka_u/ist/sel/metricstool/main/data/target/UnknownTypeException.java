package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

/**
 * 利用可能でない型が検出された時に throw される例外
 * 
 * @author y-higo
 *
 */
public class UnknownTypeException extends Exception {
    public UnknownTypeException() {
        super();
    }

    public UnknownTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownTypeException(String message) {
        super(message);
    }

    public UnknownTypeException(Throwable cause) {
        super(cause);
    }
}
