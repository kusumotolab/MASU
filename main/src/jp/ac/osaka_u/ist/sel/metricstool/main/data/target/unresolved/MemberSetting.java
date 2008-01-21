package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Member;


/**
 * ソフトウェアの単位がインスタンスなのかスタティックなのかを定義するインターフェース
 * 
 * @author higo
 */
public interface MemberSetting extends Member {

    /**
     * インスタンスメンバーかどうかをセットする
     * 
     * @param instance インスタンスメンバーの場合は true， スタティックメンバーの場合は false
     */
    void setInstanceMember(boolean instance);
}
