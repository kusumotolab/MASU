package jp.ac.osaka_u.ist.sel.metricstool.main.data;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.ClassMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.FieldMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.FileMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MethodMetricsInfoManager;


/**
 * 全てのマネージャーを管理するマネージャー
 * 
 * @author higo
 *
 */
public class DataManager {

    /**
     * データマネージャーのインスタンスを取得する
     * 
     * @return　データマネージャーのインスタンス
     */
    public static DataManager getInstance() {

        if (null == singleton) {
            singleton = new DataManager();
        }

        return singleton;

    }

    /**
     * データマネージャーに登録されている情報をクリアする
     */
    public static void clear() {
        singleton = null;
    }

    /**
     * ClassMetricsInfoManager　を返す
     * 
     * @return ClassMetricsInfoManager
     */
    public ClassMetricsInfoManager getClassMetricsInfoManager() {
        return this.classMetricsInfoManager;
    }

    /**
     * FieldMetricsInfoManager　を返す
     * 
     * @return FieldMetricsInfoManager
     */
    public FieldMetricsInfoManager getFieldMetricsInfoManager() {
        return this.fieldMetricsInfoManager;
    }

    /**
     * FileMetricsInfoManager　を返す
     * 
     * @return FileMetricsInfoManager
     */
    public FileMetricsInfoManager getFileMetricsInfoManager() {
        return this.fileMetricsInfoManager;
    }

    /**
     * MethodMetricsInfoManager　を返す
     * 
     * @return MethodMetricsInfoManager
     */
    public MethodMetricsInfoManager getMethodMetricsInfoManager() {
        return this.methodMetricsInfoManager;
    }

    private DataManager() {
        this.classMetricsInfoManager = new ClassMetricsInfoManager();
        this.fieldMetricsInfoManager = new FieldMetricsInfoManager();
        this.fileMetricsInfoManager = new FileMetricsInfoManager();
        this.methodMetricsInfoManager = new MethodMetricsInfoManager();
    }

    private static DataManager singleton;

    final private ClassMetricsInfoManager classMetricsInfoManager;

    final private FieldMetricsInfoManager fieldMetricsInfoManager;

    final private FileMetricsInfoManager fileMetricsInfoManager;

    final private MethodMetricsInfoManager methodMetricsInfoManager;

}
