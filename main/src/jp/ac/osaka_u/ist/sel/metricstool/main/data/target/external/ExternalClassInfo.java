package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external;


import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.NamespaceInfo;


/**
 * 外部クラス情報を表すクラス
 * 
 * @author higo
 * 
 */
public final class ExternalClassInfo extends ClassInfo {

    /**
     * 名前空間名とクラス名を与えて，オブジェクトを初期化
     * 
     * @param namespace 名前空間名
     * @param className クラス名
     */
    public ExternalClassInfo(final NamespaceInfo namespace, final String className) {

        super(new HashSet<ModifierInfo>(), namespace, className, 0, 0, 0, 0);
    }

    /**
     * 完全限定名を与えて，クラス情報オブジェクトを初期化
     * 
     * @param fullQualifiedName 完全限定名
     */
    public ExternalClassInfo(final String[] fullQualifiedName) {
        
        super(new HashSet<ModifierInfo>(), fullQualifiedName, 0, 0, 0, 0);
    }

    /**
     * 名前空間が不明な外部クラスのオブジェクトを初期化
     * 
     * @param className クラス名
     */
    public ExternalClassInfo(final String className) {

        super(new HashSet<ModifierInfo>(), NamespaceInfo.UNKNOWN, className, 0, 0, 0, 0);
    }

    /**
     * ExternalClassInfo では利用できない
     */
    @Override
    public int getFromLine() {
        throw new CannotUseException();
    }

    /**
     * ExternalClassInfo では利用できない
     */
    @Override
    public int getFromColumn() {
        throw new CannotUseException();
    }

    /**
     * ExternalClassInfo では利用できない
     */
    @Override
    public int getToLine() {
        throw new CannotUseException();
    }

    /**
     * ExternalClassInfo では利用できない
     */
    @Override
    public int getToColumn() {
        throw new CannotUseException();
    }

    /**
     * ExternalClassInfo では利用できない
     */
    @Override
    public Set<ModifierInfo> getModifiers() {
        throw new CannotUseException();
    }

    /**
     * Java言語で，暗黙にインポートされるクラス一覧
     */
    public static final ExternalClassInfo[] JAVA_PREIMPORTED_CLASSES = {
            new ExternalClassInfo(new String[] { "java", "lang", "Appendable" }),
            new ExternalClassInfo(new String[] { "java", "lang", "CharSequence" }),
            new ExternalClassInfo(new String[] { "java", "lang", "Cloneable" }),
            new ExternalClassInfo(new String[] { "java", "lang", "Comparable" }),
            new ExternalClassInfo(new String[] { "java", "lang", "Iterable" }),
            new ExternalClassInfo(new String[] { "java", "lang", "Readable" }),
            new ExternalClassInfo(new String[] { "java", "lang", "Runnable" }),
            new ExternalClassInfo(new String[] { "java", "lang", "Thread",
                    "UncaughtExceptionHandler" }),
            new ExternalClassInfo(new String[] { "java", "lang", "Boolean" }),
            new ExternalClassInfo(new String[] { "java", "lang", "Byte" }),
            new ExternalClassInfo(new String[] { "java", "lang", "Character" }),
            new ExternalClassInfo(new String[] { "java", "lang", "Character", "Subset" }),
            new ExternalClassInfo(new String[] { "java", "lang", "Character", "UnicodeBlock" }),
            new ExternalClassInfo(new String[] { "java", "lang", "Class" }),
            new ExternalClassInfo(new String[] { "java", "lang", "ClassLoader" }),
            new ExternalClassInfo(new String[] { "java", "lang", "Compiler" }),
            new ExternalClassInfo(new String[] { "java", "lang", "Double" }),
            new ExternalClassInfo(new String[] { "java", "lang", "Enum" }),
            new ExternalClassInfo(new String[] { "java", "lang", "Float" }),
            new ExternalClassInfo(new String[] { "java", "lang", "InheritableThreadLocal" }),
            new ExternalClassInfo(new String[] { "java", "lang", "Integer" }),
            new ExternalClassInfo(new String[] { "java", "lang", "Long" }),
            new ExternalClassInfo(new String[] { "java", "lang", "Math" }),
            new ExternalClassInfo(new String[] { "java", "lang", "Number" }),
            new ExternalClassInfo(new String[] { "java", "lang", "Object" }),
            new ExternalClassInfo(new String[] { "java", "lang", "Package" }),
            new ExternalClassInfo(new String[] { "java", "lang", "Process" }),
            new ExternalClassInfo(new String[] { "java", "lang", "ProcessBuilder" }),
            new ExternalClassInfo(new String[] { "java", "lang", "Runtime" }),
            new ExternalClassInfo(new String[] { "java", "lang", "RuntimePermission" }),
            new ExternalClassInfo(new String[] { "java", "lang", "SecurityManager" }),
            new ExternalClassInfo(new String[] { "java", "lang", "Short" }),
            new ExternalClassInfo(new String[] { "java", "lang", "StackTraceElement" }),
            new ExternalClassInfo(new String[] { "java", "lang", "StrictMath" }),
            new ExternalClassInfo(new String[] { "java", "lang", "String" }),
            new ExternalClassInfo(new String[] { "java", "lang", "StringBuffer" }),
            new ExternalClassInfo(new String[] { "java", "lang", "StringBuilder" }),
            new ExternalClassInfo(new String[] { "java", "lang", "System" }),
            new ExternalClassInfo(new String[] { "java", "lang", "Thread" }),
            new ExternalClassInfo(new String[] { "java", "lang", "ThreadGroup" }),
            new ExternalClassInfo(new String[] { "java", "lang", "ThreadLocal" }),
            new ExternalClassInfo(new String[] { "java", "lang", "Throwable" }),
            new ExternalClassInfo(new String[] { "java", "lang", "Void" }),
            new ExternalClassInfo(new String[] { "java", "lang", "Thread.State" }),
            new ExternalClassInfo(new String[] { "java", "lang", "ArithmeticException" }),
            new ExternalClassInfo(new String[] { "java", "lang", "ArrayIndexOutOfBoundsException" }),
            new ExternalClassInfo(new String[] { "java", "lang", "ArrayStoreException" }),
            new ExternalClassInfo(new String[] { "java", "lang", "ClassCastException" }),
            new ExternalClassInfo(new String[] { "java", "lang", "ClassNotFoundException" }),
            new ExternalClassInfo(new String[] { "java", "lang", "CloneNotSupportedException" }),
            new ExternalClassInfo(
                    new String[] { "java", "lang", "EnumConstantNotPresentException" }),
            new ExternalClassInfo(new String[] { "java", "lang", "Exception" }),
            new ExternalClassInfo(new String[] { "java", "lang", "IllegalAccessExceptio" }),
            new ExternalClassInfo(new String[] { "java", "lang", "IllegalArgumentException" }),
            new ExternalClassInfo(new String[] { "java", "lang", "IllegalMonitorStateException" }),
            new ExternalClassInfo(new String[] { "java", "lang", "IllegalStateException" }),
            new ExternalClassInfo(new String[] { "java", "lang", "IllegalThreadStateException" }),
            new ExternalClassInfo(new String[] { "java", "lang", "IndexOutOfBoundsException" }),
            new ExternalClassInfo(new String[] { "java", "lang", "InstantiationException" }),
            new ExternalClassInfo(new String[] { "java", "lang", "InterruptedException" }),
            new ExternalClassInfo(new String[] { "java", "lang", "NegativeArraySizeException" }),
            new ExternalClassInfo(new String[] { "java", "lang", "NoSuchFieldException" }),
            new ExternalClassInfo(new String[] { "java", "lang", "NoSuchMethodException" }),
            new ExternalClassInfo(new String[] { "java", "lang", "NullPointerException" }),
            new ExternalClassInfo(new String[] { "java", "lang", "NumberFormatException" }),
            new ExternalClassInfo(new String[] { "java", "lang", "RuntimeException" }),
            new ExternalClassInfo(new String[] { "java", "lang", "SecurityException" }),
            new ExternalClassInfo(
                    new String[] { "java", "lang", "StringIndexOutOfBoundsException" }),
            new ExternalClassInfo(new String[] { "java", "lang", "TypeNotPresentException" }),
            new ExternalClassInfo(new String[] { "java", "lang", "UnsupportedOperationException" }),
            new ExternalClassInfo(new String[] { "java", "lang", "AbstractMethodError" }),
            new ExternalClassInfo(new String[] { "java", "lang", "AssertionError" }),
            new ExternalClassInfo(new String[] { "java", "lang", "ClassCircularityError" }),
            new ExternalClassInfo(new String[] { "java", "lang", "ClassFormatError" }),
            new ExternalClassInfo(new String[] { "java", "lang", "Error" }),
            new ExternalClassInfo(new String[] { "java", "lang", "ExceptionInInitializerError" }),
            new ExternalClassInfo(new String[] { "java", "lang", "IllegalAccessError" }),
            new ExternalClassInfo(new String[] { "java", "lang", "IncompatibleClassChangeError" }),
            new ExternalClassInfo(new String[] { "java", "lang", "InstantiationError" }),
            new ExternalClassInfo(new String[] { "java", "lang", "InternalError" }),
            new ExternalClassInfo(new String[] { "java", "lang", "LinkageError" }),
            new ExternalClassInfo(new String[] { "java", "lang", "NoClassDefFoundError" }),
            new ExternalClassInfo(new String[] { "java", "lang", "NoSuchFieldError" }),
            new ExternalClassInfo(new String[] { "java", "lang", "NoSuchMethodError" }),
            new ExternalClassInfo(new String[] { "java", "lang", "OutOfMemoryError" }),
            new ExternalClassInfo(new String[] { "java", "lang", "StackOverflowError" }),
            new ExternalClassInfo(new String[] { "java", "lang", "ThreadDeath" }),
            new ExternalClassInfo(new String[] { "java", "lang", "UnknownError" }),
            new ExternalClassInfo(new String[] { "java", "lang", "UnsatisfiedLinkError" }),
            new ExternalClassInfo(new String[] { "java", "lang", "UnsupportedClassVersionError" }),
            new ExternalClassInfo(new String[] { "java", "lang", "VerifyError" }),
            new ExternalClassInfo(new String[] { "java", "lang", "VirtualMachineError" }),
            new ExternalClassInfo(new String[] { "java", "lang", "Deprecated" }),
            new ExternalClassInfo(new String[] { "java", "lang", "Override" }),
            new ExternalClassInfo(new String[] { "java", "lang", "SuppressWarnings" }) };

    /**
     * 不明な外部クラスを表すための定数
     */
    public static final ExternalClassInfo UNKNOWN = new ExternalClassInfo("UNKNOWN");
}
