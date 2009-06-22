package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StaticOrInstance;


/**
 * ソフトウェアの単位がインスタンスなのかスタティックなのかを定義するインターフェース
 * 
 * @author higo
 */
public interface StaticOrInstanceSetting extends StaticOrInstance {

    /**
     * インスタンスメンバーかどうかをセットする
     * 
     * @param instance インスタンスメンバーの場合は true， スタティックメンバーの場合は false
     */
    void setInstanceMember(boolean instance);
}
