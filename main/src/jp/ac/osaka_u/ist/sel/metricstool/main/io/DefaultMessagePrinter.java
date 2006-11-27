package jp.ac.osaka_u.ist.sel.metricstool.main.io;


/**
 * メッセージプリンタのデフォルト実装
 * 
 * @author kou-tngt
 *
 */
public class DefaultMessagePrinter implements MessagePrinter {

    /**
     * コンストラクタ
     * @param source メッセージ送信者
     * @param type メッセージタイプ
     */
    public DefaultMessagePrinter(final MessageSource source, final MESSAGE_TYPE type) {
        this.pool = MessagePool.getInstance(type);
        this.source = source;
    }

    /**
     * メッセージをそのまま出力する
     * @param message 出力するメッセージ
     */
    public void print(final String message) {
        this.pool.sendMessage(this.source, message);
    }

    /**
     * メッセージを出力して改行する
     * @param message 出力するメッセージ
     */
    public void println(final String message) {
        this.print(message + LINE_SEPARATOR);
    }

    /**
     * 複数行のメッセージの間に，他のメッセージの割り込みがないように出力する.
     * @param messages 出力するメッセージの配列
     */
    public void println(final String[] messages) {
        final StringBuilder builder = new StringBuilder();
        for (final String message : messages) {
            builder.append(message);
            builder.append(LINE_SEPARATOR);
        }
        this.print(builder.toString());
    }

    /**
     * メッセージ送信者
     */
    private final MessageSource source;

    /**
     * 対応するメッセージプール
     */
    private final MessagePool pool;

    /**
     * システム依存の改行記号
     */
    private final static String LINE_SEPARATOR = System.getProperty("line.separator");

}
