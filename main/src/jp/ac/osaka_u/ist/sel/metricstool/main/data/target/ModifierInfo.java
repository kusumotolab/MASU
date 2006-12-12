package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * クラス，メソッド，フィールドなどの修飾子を表すクラス．現在以下の，修飾子情報を持つ
 * <ul>
 * <li>public</li>
 * <li>private</li>
 * <li>virtual(abstract)
 * <li>
 * </ul>
 * 
 * @author y-higo
 * 
 */
public class ModifierInfo {

    public ModifierInfo() {
        this.publicInfo = false;
        this.privateInfo = false;
        this.virtualInfo = false;
    }

    /**
     * private かどうかを返す
     * 
     * @return private な場合は true，そうでない場合は false
     */
    public boolean isPrivateInfo() {
        return privateInfo;
    }

    /**
     * private かどうかをセットする
     * 
     * @param privateInfo private かどうか
     */
    public void setPrivateInfo(boolean privateInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        this.privateInfo = privateInfo;
    }

    /**
     * public かどうかを返す
     * 
     * @return public な場合は true，そうでない場合は false
     */
    public boolean isPublicInfo() {
        return this.publicInfo;
    }

    /**
     * public かどうかをセットする
     * 
     * @param publicInfo public かどうか
     */
    public void setPublicInfo(boolean publicInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        this.publicInfo = publicInfo;
    }

    /**
     * virtual (abstract) かどうかを返す
     * 
     * @return virtual (abstract) な場合は true，そうでない場合は false
     */
    public boolean isVirtualInfo() {
        return this.virtualInfo;
    }

    /**
     * abstract (virtual) かどうかを返す
     * 
     * @return abstract (virtual) な場合は true，そうでない場合は false
     */
    public boolean isAbstractInfo() {
        return this.virtualInfo;
    }

    /**
     * virtual (abstract) かどうかをセットする
     * 
     * @param virtualInfo virtual (abstract) かどうか
     */
    public void setVirtualInfo(boolean virtualInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        this.virtualInfo = virtualInfo;
    }

    /**
     * abstract (virtual) かどうかをセットする
     * 
     * @param virtualInfo abstract (virtual) かどうか
     */
    public void setAbstractInfo(boolean virtualInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        this.virtualInfo = virtualInfo;
    }

    /**
     * public かどうかを表す変数
     */
    private boolean publicInfo;

    /**
     * private かどうかを表す変数
     */
    private boolean privateInfo;

    /**
     * virtual(abstract) かどうかを表す変数
     */
    private boolean virtualInfo;
}
