package jp.ac.osaka_u.ist.sel.metricstool.main.io;


/**
 * プラグインとの接続で発生する例外群の親クラス
 * 
 * @author kou-tngt
 *
 */
public class ConnectionException extends Exception {

    public ConnectionException() {
        super();
    }

    public ConnectionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ConnectionException(final String message) {
        super(message);
    }

    public ConnectionException(final Throwable cause) {
        super(cause);
    }

}
