package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;


/**
 * Java言語用の型変換ユーティリティ
 * 
 * @author higo
 */
final public class JavaTypeConverter extends TypeConverter {

    static final JavaTypeConverter SINGLETON = new JavaTypeConverter();

    /**
     * プリミティブ型のラッパークラスを返す
     * 
     * @param primitiveType ラッパークラスを取得したいプリミティブ型
     * @return 指定したプリミティブ型のラッパークラス
     */
    @Override
    public ExternalClassInfo getWrapperClass(final PrimitiveTypeInfo primitiveType) {

        if (null == primitiveType) {
            throw new NullPointerException();
        }

        final ClassInfoManager classInfoManager = DataManager.getInstance().getClassInfoManager();

        switch (primitiveType.getPrimitiveType()) {
        case BOOLEAN:
            final ExternalClassInfo booleanClass = (ExternalClassInfo) classInfoManager
                    .getClassInfo(new String[] { "java", "lang", "Boolean" });
            return booleanClass;
        case BYTE:
            final ExternalClassInfo byteClass = (ExternalClassInfo) classInfoManager
                    .getClassInfo(new String[] { "java", "lang", "Byte" });
            return byteClass;
        case CHAR:
            final ExternalClassInfo charClass = (ExternalClassInfo) classInfoManager
                    .getClassInfo(new String[] { "java", "lang", "Character" });
            return charClass;
        case DOUBLE:
            final ExternalClassInfo doubleClass = (ExternalClassInfo) classInfoManager
                    .getClassInfo(new String[] { "java", "lang", "Double" });
            return doubleClass;
        case FLOAT:
            final ExternalClassInfo floatClass = (ExternalClassInfo) classInfoManager
                    .getClassInfo(new String[] { "java", "lang", "Float" });
            return floatClass;
        case INT:
            final ExternalClassInfo intClass = (ExternalClassInfo) classInfoManager
                    .getClassInfo(new String[] { "java", "lang", "Integer" });
            return intClass;
        case LONG:
            final ExternalClassInfo longClass = (ExternalClassInfo) classInfoManager
                    .getClassInfo(new String[] { "java", "lang", "Long" });
            return longClass;
        case SHORT:
            final ExternalClassInfo shortClass = (ExternalClassInfo) classInfoManager
                    .getClassInfo(new String[] { "java", "lang", "Short" });
            return shortClass;
        case STRING:
            final ExternalClassInfo stringClass = (ExternalClassInfo) classInfoManager
                    .getClassInfo(new String[] { "java", "lang", "String" });
            return stringClass;
        default:
            throw new IllegalArgumentException();
        }

    }

}
