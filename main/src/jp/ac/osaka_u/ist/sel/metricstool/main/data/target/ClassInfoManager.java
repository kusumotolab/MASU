package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;


/**
 * クラス情報を管理するクラス．
 * 
 * @author higo
 * 
 */
public final class ClassInfoManager {

    /**
     * 対象クラスを追加する
     * 
     * @param classInfo 追加するクラス情報
     * @return 引数クラスを追加した場合は true,しなかった場合はfalse
     */
    public boolean add(final ClassInfo classInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfo) {
            throw new NullPointerException();
        }

        // 二重登録チェック
        if (this.targetClassInfos.contains(classInfo)) {
            err.println(classInfo.getFullQualifiedName(".") + " is already registered!");
            return false;
        } else if (this.externalClassInfos.contains(classInfo)) {
            // 外部クラスと重複している場合はエラー出力しない
            return false;
        }

        // クラス一覧のセットに登録
        if (classInfo instanceof TargetClassInfo) {
            this.targetClassInfos.add((TargetClassInfo) classInfo);
        } else if (classInfo instanceof ExternalClassInfo) {
            this.externalClassInfos.add((ExternalClassInfo) classInfo);
        } else {
            assert false : "Here shouldn't be reached!";
        }

        // クラス名からクラスオブジェクトを得るためのマップに追加
        {
            final String name = classInfo.getClassName();
            SortedSet<ClassInfo> classInfos = this.classNameMap.get(name);
            if (null == classInfos) {
                classInfos = new TreeSet<ClassInfo>();
                this.classNameMap.put(name, classInfos);
            }
            classInfos.add(classInfo);
        }

        //　名前空間からクラスオブジェクトを得るためのマップに追加
        {
            final NamespaceInfo namespace = classInfo.getNamespace();
            SortedSet<ClassInfo> classInfos = this.namespaceMap.get(namespace);
            if (null == classInfos) {
                classInfos = new TreeSet<ClassInfo>();
                this.namespaceMap.put(namespace, classInfos);
            }
            classInfos.add(classInfo);
        }

        return true;
    }

    /**
     * 対象クラスのSortedSetを返す
     * 
     * @return 対象クラスのSortedSet
     */
    public SortedSet<TargetClassInfo> getTargetClassInfos() {
        return Collections.unmodifiableSortedSet(this.targetClassInfos);
    }

    /**
     * 外部クラスのSortedSetを返す
     * 
     * @return 外部クラスのSortedSet
     */
    public SortedSet<ExternalClassInfo> getExternalClassInfos() {
        return Collections.unmodifiableSortedSet(this.externalClassInfos);
    }

    /**
     * 対象クラスの数を返す
     * 
     * @return 対象クラスの数
     */
    public int getTargetClassCount() {
        return this.targetClassInfos.size();
    }

    /**
     * 外部クラスの数を返す
     * 
     * @return 外部クラスの数
     */
    public int getExternalClassCount() {
        return this.targetClassInfos.size();
    }

    /**
     * 引数で指定した完全限定名を持つクラス情報を返す
     * 
     * @param fullQualifiedName 完全限定名
     * @return クラス情報
     */
    public ClassInfo getClassInfo(final String[] fullQualifiedName) {

        if (null == fullQualifiedName) {
            throw new NullPointerException();
        }

        final int namespaceLength = fullQualifiedName.length - 1;
        final String[] namespace = Arrays.<String> copyOf(fullQualifiedName,
                fullQualifiedName.length - 1);
        final String className = fullQualifiedName[namespaceLength];

        // 同じクラス名を持つクラス一覧を取得
        final SortedSet<ClassInfo> classInfos = this.classNameMap.get(className);
        if (null != classInfos) {
            // 名前空間が等しいクラスを返す
            for (final ClassInfo classInfo : classInfos) {
                if (classInfo.getNamespace().equals(namespace)) {
                    return classInfo;
                }
            }

            // ここに来るのは登録されていないクラスの完全限定名が指定されたとき
            // 外部クラスとしてオブジェクトを生成し，登録する
            final ExternalClassInfo classInfo = new ExternalClassInfo(fullQualifiedName);
            this.add(classInfo);
            return classInfo;

        } else {

            // ここに来るのは登録されていないクラスの完全限定名が指定されたとき
            // 外部クラスとしてオブジェクトを生成し，登録する
            final ExternalClassInfo classInfo = new ExternalClassInfo(fullQualifiedName);
            this.add(classInfo);
            return classInfo;
        }
    }

    /**
     * 引数で指定した名前空間を持つクラス情報の Collection を返す
     * 
     * @param namespace 名前空間
     * @return 引数で指定した名前空間を持つクラス情報の Collection
     */
    public Collection<ClassInfo> getClassInfos(final String[] namespace) {

        if (null == namespace) {
            throw new IllegalArgumentException();
        }

        final SortedSet<ClassInfo> classInfos = this.namespaceMap.get(new NamespaceInfo(namespace));
        return null != classInfos ? Collections.unmodifiableSortedSet(classInfos) : Collections
                .unmodifiableSortedSet(new TreeSet<ClassInfo>());
    }

    /**
     * 引数で指定したクラス名を持つクラス情報の Collection を返す
     * 
     * @param className クラス名
     * @return 引数で指定したクラス名を持つクラス情報の Collection
     */
    public Collection<ClassInfo> getClassInfos(final String className) {

        if (null == className) {
            throw new IllegalArgumentException();
        }

        final SortedSet<ClassInfo> classInfos = this.classNameMap.get(className);
        return null != classInfos ? Collections.unmodifiableSortedSet(classInfos) : Collections
                .unmodifiableSortedSet(new TreeSet<ClassInfo>());
    }

    /**
     * エラーメッセージ出力用のプリンタ
     */
    private static final MessagePrinter err = new DefaultMessagePrinter(new MessageSource() {
        public String getMessageSourceName() {
            return "main";
        }
    }, MESSAGE_TYPE.ERROR);

    /**
     * 
     * コンストラクタ． 
     */
    public ClassInfoManager() {

        this.classNameMap = new HashMap<String, SortedSet<ClassInfo>>();
        this.namespaceMap = new HashMap<NamespaceInfo, SortedSet<ClassInfo>>();

        this.targetClassInfos = new TreeSet<TargetClassInfo>();
        this.externalClassInfos = new TreeSet<ExternalClassInfo>();

        // java言語の場合は，暗黙にインポートされるクラスを追加しておく
        if (Settings.getLanguage().equals(LANGUAGE.JAVA15)
                || Settings.getLanguage().equals(LANGUAGE.JAVA14)
                || Settings.getLanguage().equals(LANGUAGE.JAVA13)) {
            for (int i = 0; i < ExternalClassInfo.JAVA_PREIMPORTED_CLASSES.length; i++) {
                this.add(ExternalClassInfo.JAVA_PREIMPORTED_CLASSES[i]);
            }
        }
    }

    /**
     * クラス名から，クラスオブジェクトを得るためのマップ
     */
    private final Map<String, SortedSet<ClassInfo>> classNameMap;

    /**
     * 名前空間名から，クラスオブジェクトを得るためのマップ
     */
    private final Map<NamespaceInfo, SortedSet<ClassInfo>> namespaceMap;

    /**
     * 対象クラス一覧を保存するためのセット
     */
    private final SortedSet<TargetClassInfo> targetClassInfos;

    /**
     * 外部クラス一覧を保存するためのセット
     */
    private final SortedSet<ExternalClassInfo> externalClassInfos;
}
