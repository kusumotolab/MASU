package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


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
            throw new IllegalArgumentException();
        }

        // 二重登録チェック
        if (this.targetClassInfos.contains(classInfo)) {
            err.println(classInfo.getFullQualifiedName(".") + " is already registered!");
            return false;
        } 
        
        // 外部クラスと重複している場合は，外部クラスの方を取り除く
        // 例えば，java.lang.*を解析対象として含めた場合は，java.lang.*のExternalClassInfoはいらない
        else if (this.externalClassInfos.contains(classInfo)) {
            this.externalClassInfos.remove(classInfo);
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
        return this.externalClassInfos.size();
    }

    /**
     * 引数で指定した完全限定名を持つクラス情報を返す.
     * 指定された完全限定名をもつクラスが存在しないときはnullを返す
     * 
     * @param fullQualifiedName 完全限定名
     * @return クラス情報
     */
    public ClassInfo getClassInfo(final String[] fullQualifiedName) {

        if ((null == fullQualifiedName) || (0 == fullQualifiedName.length)) {
            throw new IllegalArgumentException();
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
        }
        return null;
    }

    /**
     * 引数で指定した完全限定名を持つクラスがあるか判定する
     * 
     * @param fullQualifiedName 調査したいクラスの完全限定名
     * @return クラスがある場合はtrue, ない場合はfalse
     */
    public boolean hasClassInfo(final String[] fullQualifiedName) {

        if ((null == fullQualifiedName) || (0 == fullQualifiedName.length)) {
            throw new IllegalArgumentException();
        }

        final int namespaceLength = fullQualifiedName.length - 1;
        final String[] namespace = Arrays.<String> copyOf(fullQualifiedName,
                fullQualifiedName.length - 1);
        final String className = fullQualifiedName[namespaceLength];

        //同じクラス名を持つクラス一覧を取得
        final SortedSet<ClassInfo> classInfos = this.classNameMap.get(className);
        if (null != classInfos) {

            // 名前空間が等しいクラスがあれば，trueを返す
            for (final ClassInfo classInfo : classInfos) {
                if (classInfo.getNamespace().equals(namespace)) {
                    return true;
                }
            }
        }

        return false;
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

        return this.getClassInfos(new NamespaceInfo(namespace));
    }

    /**
     * 引数で指定した名前空間を持つクラス情報の Collection を返す
     * 
     * @param namespace 名前空間
     * @return 引数で指定した名前空間を持つクラス情報の Collection
     */
    public Collection<ClassInfo> getClassInfos(final NamespaceInfo namespace) {

        if (null == namespace) {
            throw new IllegalArgumentException();
        }

        final SortedSet<ClassInfo> classInfos = this.namespaceMap.get(namespace);
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
