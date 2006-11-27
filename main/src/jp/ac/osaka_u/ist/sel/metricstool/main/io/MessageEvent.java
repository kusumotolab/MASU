package jp.ac.osaka_u.ist.sel.metricstool.main.io;


import java.util.EventObject;


/**
 * メッセージイベントクラス
 * 
 * @author kou-tngt
 *
 */
public class MessageEvent extends EventObject {

    /**
     * コンストラクタ
     * @param source メッセージ送信者
     * @param message メッセージ
     */
    public MessageEvent(final MessageSource source, final String message) {
        super(source);
        this.source = source;
        this.message = message;
    }

    /**
     * メッセージを取得するメソッド
     * @return メッセージ
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * メッセージ送信者を取得するメソッド
     * @return メッセージ送信者
     * @see java.util.EventObject#getSource()
     */
    @Override
    public MessageSource getSource() {
        return this.source;
    }

    /**
     * メッセージ送信者
     */
    private final MessageSource source;

    /**
     * メッセージ
     */
    private final String message;

}
