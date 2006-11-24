package jp.ac.osaka_u.ist.sel.metricstool.main.plugin.connection;


/**
 * プラグインとの接続で発生する例外群の親クラス
 * 
 * @author kou-tngt
 *
 */
public class PluginConnectionException extends Exception {

    public PluginConnectionException() {
        super();
    }

    public PluginConnectionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public PluginConnectionException(final String message) {
        super(message);
    }

    public PluginConnectionException(final Throwable cause) {
        super(cause);
    }

}
