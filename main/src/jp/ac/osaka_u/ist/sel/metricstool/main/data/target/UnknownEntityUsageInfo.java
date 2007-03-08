package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


public class UnknownEntityUsageInfo extends EntityUsageInfo {

    /**
     * このクラスの単一オブジェクトを返す
     * 
     * @return このクラスの単一オブジェクト
     */
    public static UnknownEntityUsageInfo getInstance() {
        return SINGLETON;
    }

    @Override
    public TypeInfo getType() {
        return UnknownTypeInfo.getInstance();
    }

    private UnknownEntityUsageInfo() {
    }

    /**
     * このクラスの単一オブジェクトを保存するための定数
     */
    private static final UnknownEntityUsageInfo SINGLETON = new UnknownEntityUsageInfo();
}
