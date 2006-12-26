package jp.ac.osaka_u.ist.sel.metricstool.main.util;


/**
 * 
 * @author y-higo
 * 
 * 利用可能でないプログラミング言語が指定された場合に用いられる．
 * 
 */
public class UnavailableLanguageException extends RuntimeException {

    public UnavailableLanguageException() {
        super();
    }

    public UnavailableLanguageException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnavailableLanguageException(String message) {
        super(message);
    }

    public UnavailableLanguageException(Throwable cause) {
        super(cause);
    }
}
