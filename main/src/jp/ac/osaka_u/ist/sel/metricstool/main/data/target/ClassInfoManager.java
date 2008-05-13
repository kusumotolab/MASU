package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
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
     * クラス情報を管理しているインスタンスを返す． シングルトンパターンを持ちている．
     * 
     * @return クラス情報を管理しているインスタンス
     */
    public static ClassInfoManager getInstance() {
        return SINGLETON;
    }

    /**
     * 対象クラスを追加する
     * 
     * @param classInfo 追加するクラス情報
     * @return 引数クラスを追加した場合は true,しなかった場合はfalse
     */
    public boolean add(final TargetClassInfo classInfo) {

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

        this.targetClassInfos.add(classInfo);
        this.packageInfo.add(classInfo);

        /* この処理はいらないような．．．
        // 内部クラスに対して再帰的に処理
        for (final TargetInnerClassInfo innerClassInfo : classInfo.getInnerClasses()) {
            this.add(innerClassInfo);
        }
        */

        return true;
    }

    /**
     * 外部クラスを追加する
     * 
     * @param classInfo 追加するクラス情報
     * @return 引数クラスを追加した場合は true,しなかった場合はfalse
     */
    public boolean add(final ExternalClassInfo classInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfo) {
            throw new NullPointerException();
        }

        // 二重登録チェック
        if (this.targetClassInfos.contains(classInfo)
                || (this.externalClassInfos.contains(classInfo))) {
            // 外部クラスの場合は二重登録エラーは出力しない
            //err.println(classInfo.getFullQualifiedtName(".") + " is already registered!");
            return false;
        }

        this.externalClassInfos.add(classInfo);
        this.packageInfo.add(classInfo);

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

        return this.packageInfo.getClassInfo(fullQualifiedName);
    }

    /**
     * 引数で指定した名前空間を持つクラス情報の Collection を返す
     * 
     * @param namespace 名前空間
     * @return 引数で指定した名前空間を持つクラス情報の Collection
     */
    public Collection<ClassInfo> getClassInfos(final String[] namespace) {

        if (null == namespace) {
            throw new NullPointerException();
        }

        return this.packageInfo.getClassInfos(namespace);
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
     * コンストラクタ． シングルトンパターンで実装しているために private がついている．
     */
    private ClassInfoManager() {
        this.targetClassInfos = new TreeSet<TargetClassInfo>();
        this.externalClassInfos = new TreeSet<ExternalClassInfo>();
        this.packageInfo = new PackageInfo("DEFAULT", 0);

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
     * 
     * シングルトンパターンを実装するための変数．
     */
    private static final ClassInfoManager SINGLETON = new ClassInfoManager();

    /**
     * 
     * 対象クラス情報(TargetClassInfo)を格納する変数．
     */
    private final SortedSet<TargetClassInfo> targetClassInfos;

    /**
     * 外部クラス情報（ExternalClassInfo）を格納する変数
     */
    private final SortedSet<ExternalClassInfo> externalClassInfos;

    /**
     * クラス情報を階層構造で保つための変数
     */
    private final PackageInfo packageInfo;

    /**
     * クラス一覧を階層的名前空間（パッケージ階層）で持つデータクラス
     * 
     * @author higo
     */
    class PackageInfo {

        /**
         * 名前空間を初期化．名前空間名と深さを与える．デフォルトパッケージは0．
         * 
         * @param packageName 名前空間名
         */
        PackageInfo(final String packageName, final int depth) {

            if (null == packageName) {
                throw new NullPointerException();
            }
            if (depth < 0) {
                throw new IllegalArgumentException("Depth must be 0 or more!");
            }

            this.packageName = packageName;
            this.depth = depth;
            this.classInfos = new TreeMap<String, ClassInfo>();
            this.subPackages = new TreeMap<String, PackageInfo>();
        }

        /**
         * 引数で指定されたクラス情報を追加する
         * 
         * @param classInfo 追加するクラス情報
         */
        void add(final ClassInfo classInfo) {

            if (null == classInfo) {
                throw new NullPointerException();
            }

            String[] packageNames = classInfo.getNamespace().getName();

            // 追加するクラス情報の名前空間階層が，この名前空間階層よりも深い場合は，該当するサブ名前空間を呼び出す
            if (this.getDepth() < packageNames.length) {
                PackageInfo subPackage = this.subPackages.get(packageNames[this.getDepth()]);
                if (null == subPackage) {
                    subPackage = new PackageInfo(packageNames[this.getDepth()], this.getDepth() + 1);
                    this.subPackages.put(subPackage.getPackageName(), subPackage);
                }
                subPackage.add(classInfo);

                // 追加するクラス情報の名前空間階層が，この名前空間階層と同じ場合は，この名前空間にクラス情報を追加する
            } else if (this.getDepth() == packageNames.length) {
                this.classInfos.put(classInfo.getClassName(), classInfo);

                final PackageInfo innerPackage = new PackageInfo(classInfo.getClassName(), this
                        .getDepth() + 1);
                this.subPackages.put(classInfo.getClassName(), innerPackage);

                // 追加するクラス情報の名前空間階層が，この名前空間階層よりも浅い場合は，エラー
            } else {
                throw new IllegalArgumentException("Illegal class Info: " + classInfo.toString());
            }
        }

        /**
         * この名前空間の深さを返す
         * 
         * @return この名前空間の深さ
         */
        int getDepth() {
            return this.depth;
        }

        /**
         * この名前空間名を返す
         * 
         * @return この名前空間名
         */
        String getPackageName() {
            return this.packageName;
        }

        /**
         * 引数で与えられた Full Qualified Name を持つクラスを返す
         * 
         * @param fullQualifiedName ほしいクラス情報の Full Qualified Name
         * @return クラス情報
         */
        ClassInfo getClassInfo(final String[] fullQualifiedName) {

            if (null == fullQualifiedName) {
                throw new NullPointerException();
            }

            int namespaceLength = fullQualifiedName.length - 1;

            // ほしいクラス情報の名前空間階層が，この名前空間階層よりも深い場合は，該当するサブ名前空間を呼び出す
            if (this.getDepth() < namespaceLength) {
                final PackageInfo subPackage = this.subPackages.get(fullQualifiedName[this
                        .getDepth()]);
                return null == subPackage ? null : subPackage.getClassInfo(fullQualifiedName);

                // ほしいクラス情報の名前空間層が，この名前空間階層と同じ場合は，クラス情報を返す
            } else if (this.getDepth() == namespaceLength) {
                final ClassInfo classInfo = this.classInfos.get(fullQualifiedName[namespaceLength]);
                return classInfo;

                // ほしいクラス情報の名前空間階層が，この名前空間階層よりも浅い場合は，エラー
            } else {
                throw new IllegalArgumentException("Illegal full qualified name: "
                        + fullQualifiedName.toString());
            }
        }

        /**
         * 引数で与えられた名前空間を持つクラスの Collection を返す
         * 
         * @param namespace 名前空間
         * @return 引数で与えられた名前空間を持つクラスの Collection
         */
        Collection<ClassInfo> getClassInfos(final String[] namespace) {

            if (null == namespace) {
                throw new NullPointerException();
            }

            // ほしいクラス情報の名前空間層が，この名前空間層よりも深い場合は，該当するサブ名前空間名を呼び出す
            if (this.getDepth() < namespace.length) {
                final PackageInfo subPackage = this.subPackages.get(namespace[this.getDepth()]);
                return null == subPackage ? Collections
                        .unmodifiableCollection(new HashSet<ClassInfo>()) : subPackage
                        .getClassInfos(namespace);

                // ほしいクラス情報の名前空間層が，この名前空間層と同じ場合は，クラス情報を返す
            } else if (this.getDepth() == namespace.length) {

                return Collections.unmodifiableCollection(this.classInfos.values());
                // ほしいクラス情報の名前空間階層が，この名前空間階層よりも浅い場合は，エラー
            } else {
                throw new IllegalArgumentException("Illegal namepace: " + namespace.toString());
            }
        }

        /**
         * このパッケージの名前を保存する
         */
        private final String packageName;

        /**
         * このパッケージの深さ． デフォルトパッケージは0である．
         */
        private final int depth;

        /**
         * このパッケージにあるクラス一覧を保存する
         */
        private final SortedMap<String, ClassInfo> classInfos;

        /**
         * このパッケージのサブパッケージ一覧を保存する
         */
        private final SortedMap<String, PackageInfo> subPackages;
    }
}
