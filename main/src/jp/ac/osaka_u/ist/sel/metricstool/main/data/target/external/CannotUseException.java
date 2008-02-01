package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external;

/**
 * Externalなユニットでは利用できないメソッドを呼び出したときに投げられる例外
 * 
 * @author higo
 *
 */
public final class CannotUseException extends RuntimeException {

    public CannotUseException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public CannotUseException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public CannotUseException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public CannotUseException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
}
