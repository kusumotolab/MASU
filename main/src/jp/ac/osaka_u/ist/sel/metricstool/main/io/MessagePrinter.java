package jp.ac.osaka_u.ist.sel.metricstool.main.io;


/**
 * メッセージを出力部に送るためのインタフェース
 * 
 * @author kou-tngt
 *
 */
public interface MessagePrinter {
    /**
     * メッセージの種類
     * @author kou-tngt
     */
    public static enum MESSAGE_TYPE {
        OUT, INFO, WARNING, ERROR
    };

    /**
     * メッセージをそのまま出力する
     * @param message 出力するメッセージ
     */
    public void print(Object o);

    /**
     * 改行する
     */
    public void println();

    /**
     * メッセージを出力して改行する
     * @param message 出力するメッセージ
     */
    public void println(Object o);

    /**
     * 複数行のメッセージの間に，他のメッセージの割り込みがないように出力する.
     * @param messages 出力するメッセージの配列
     */
    public void println(Object[] o);
}
