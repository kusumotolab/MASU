package jp.ac.osaka_u.ist.sel.metricstool.main.io;


/**
 * 接続が切断された場合に発生する
 * 
 * @author kou-tngt
 *
 */
public class DisconnectedException extends ConnectionException {

    /**
     * 
     */
    private static final long serialVersionUID = -1236997179711018009L;

    public DisconnectedException() {
        super();
    }

    public DisconnectedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DisconnectedException(String message) {
        super(message);
    }

    public DisconnectedException(Throwable cause) {
        super(cause);
    }

}
