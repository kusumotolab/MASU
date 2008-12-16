package jp.ac.osaka_u.ist.sel.metricstool.main.data;


import java.lang.reflect.Field;
import java.util.Map;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.ClassMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.FieldMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.FileMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MethodMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFileManager;


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

        if (null == SINGLETON) {
            SINGLETON = new DataManager();
        }

        return SINGLETON;

    }

    /**
     * データマネージャーに登録されている情報をクリアする
     */
    public static void clear() {
        SINGLETON = null;

        try {
            final Class<?> fieldUsageInfo = FieldUsageInfo.class;
            final Field FIELD_USAGE_MAP = fieldUsageInfo.getDeclaredField("USAGE_MAP");
            FIELD_USAGE_MAP.setAccessible(true);
            final Map<?, ?> fieldMap = (Map<?, ?>) FIELD_USAGE_MAP.get(null);
            fieldMap.clear();

            final Class<?> parameterUsageInfo = ParameterUsageInfo.class;
            final Field PARAMETER_USAGE_MAP = parameterUsageInfo.getDeclaredField("USAGE_MAP");
            PARAMETER_USAGE_MAP.setAccessible(true);
            final Map<?, ?> parameterMap = (Map<?, ?>) PARAMETER_USAGE_MAP.get(null);
            parameterMap.clear();

            final Class<?> localVariableUsageInfo = LocalVariableUsageInfo.class;
            final Field LOCALVARIABLE_USAGE_MAP = localVariableUsageInfo
                    .getDeclaredField("USAGE_MAP");
            LOCALVARIABLE_USAGE_MAP.setAccessible(true);
            final Map<?, ?> localVariableMap = (Map<?, ?>) LOCALVARIABLE_USAGE_MAP.get(null);
            localVariableMap.clear();

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * TargetFileManager　を返す
     * 
     * @return TargetFileManager
     */
    public TargetFileManager getTargetFileManager() {
        return this.targetFileManager;
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

    /**
     * ClassInfoManager　を返す
     * 
     * @return ClassInfoManager
     */
    public ClassInfoManager getClassInfoManager() {
        return this.classInfoManager;
    }

    /**
     * FieldInfoManager　を返す
     * 
     * @return FieldInfoManager
     */
    public FieldInfoManager getFieldInfoManager() {
        return this.fieldInfoManager;
    }

    /**
     * FileInfoManager　を返す
     * 
     * @return FileInfoManager
     */
    public FileInfoManager getFileInfoManager() {
        return this.fileInfoManager;
    }

    /**
     * MethodInfoManager　を返す
     * 
     * @return MethodInfoManager
     */
    public MethodInfoManager getMethodInfoManager() {
        return this.methodInfoManager;
    }

    private DataManager() {
        this.targetFileManager = new TargetFileManager();

        this.classMetricsInfoManager = new ClassMetricsInfoManager();
        this.fieldMetricsInfoManager = new FieldMetricsInfoManager();
        this.fileMetricsInfoManager = new FileMetricsInfoManager();
        this.methodMetricsInfoManager = new MethodMetricsInfoManager();

        this.classInfoManager = new ClassInfoManager();
        this.fieldInfoManager = new FieldInfoManager();
        this.fileInfoManager = new FileInfoManager();
        this.methodInfoManager = new MethodInfoManager();
    }

    private static DataManager SINGLETON;

    final private TargetFileManager targetFileManager;

    final private ClassMetricsInfoManager classMetricsInfoManager;

    final private FieldMetricsInfoManager fieldMetricsInfoManager;

    final private FileMetricsInfoManager fileMetricsInfoManager;

    final private MethodMetricsInfoManager methodMetricsInfoManager;

    final private ClassInfoManager classInfoManager;

    final private FieldInfoManager fieldInfoManager;

    final private FileInfoManager fileInfoManager;

    final private MethodInfoManager methodInfoManager;
}
