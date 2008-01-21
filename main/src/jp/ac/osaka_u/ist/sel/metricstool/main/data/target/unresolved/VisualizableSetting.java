package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Visualizable;


/**
 * クラスや，フィールド，メソッドの可視性を設定するインターフェース． 以下の可視性を設定する．
 * 
 * <ul>
 * <li>クラス内からのみ参照可能</li>
 * <li>子クラスから参照可能</li>
 * <li>同じ名前空間内から参照可能</li>
 * <li>どこからでも参照可能</li>
 * </ul>
 * 
 * @author higo
 * 
 */
public interface VisualizableSetting extends Visualizable {

    /**
     * クラス内からのみ参照可能かどうかを設定する
     * 
     * @param privateVisible クラス内からのみ参照可能な場合は true，そうでない場合は false
     */
    void setPrivateVibible(boolean privateVisible);

    /**
     * 同じ名前空間内から参照可能かどうかを設定する
     * 
     * @param namespaceVisible 同じ名前空間から参照可能な場合は true，そうでない場合は false
     */
    void setNamespaceVisible(boolean namespaceVisible);

    /**
     * 子クラスから参照可能かどうかを設定する
     * 
     * @param inheritanceVisible 子クラスから参照可能な場合は true，そうでない場合は false
     */
    void setInheritanceVisible(boolean inheritanceVisible);

    /**
     * どこからでも参照可能かどうかを設定する
     * 
     * @param publicVisible どこからでも参照可能な場合は true，そうでない場合は false
     */
    void setPublicVisible(boolean publicVisible);
}
