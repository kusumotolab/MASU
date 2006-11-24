package jp.ac.osaka_u.ist.sel.metricstool.main.plugin.connection;


import java.util.EventObject;

import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;


/**
 * 進捗情報イベント
 * 
 * @author kou-tngt
 *
 */
public class ProgressEvent extends EventObject {

    /**
     * コンストラクタ
     * 
     * @param source 進捗状況を送ったプラグイン
     * @param value 進捗状況を表す値(%)
     */
    public ProgressEvent(final AbstractPlugin source, final int value) {
        super(source);
        this.value = value;
        this.plugin = source;
    }

    /**
     * 進捗状況を取り出す
     * 
     * @return このイベントが表す進捗状況値
     */
    public int getProgressValue() {
        return this.value;
    }
    
    /**
     * このイベントを発行したプラグインを返す
     * @return このイベントを発行したプラグイン
     * 
     * @see java.util.EventObject#getSource()
     */
    public AbstractPlugin getSource(){
        return this.plugin;
    }

    
    private final AbstractPlugin plugin;
    /**
     * 進捗状況を表す値（%）
     */
    private final int value;

}
