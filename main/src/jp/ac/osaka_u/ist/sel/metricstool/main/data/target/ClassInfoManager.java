package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * クラス情報を管理するクラス．
 * 
 * @author y-higo
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
     */
    public void add(final TargetClassInfo classInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfo) {
            throw new NullPointerException();
        }

        this.targetClassInfos.add(classInfo);
        this.packageInfo.add(classInfo);
    }

    /**
     * 外部クラスを追加する
     * 
     * @param classInfo 追加するクラス情報
     */
    public void add(final ExternalClassInfo classInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfo) {
            throw new NullPointerException();
        }

        this.externalClassInfos.add(classInfo);
        this.packageInfo.add(classInfo);
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
        
        if (null == fullQualifiedName){
            throw new NullPointerException();
        }
        
        return this.packageInfo.getClassInfo(fullQualifiedName);
    }

    public Collection<ClassInfo> getClassInfos(final String[] namespace) {
        
        if (null == namespace){
            throw new NullPointerException();
        }
        
        return this.packageInfo.getClassInfos(namespace);
    }
    /**
     * 
     * コンストラクタ． シングルトンパターンで実装しているために private がついている．
     */
    private ClassInfoManager() {
        this.targetClassInfos = new TreeSet<TargetClassInfo>();
        this.externalClassInfos = new TreeSet<ExternalClassInfo>();
        this.packageInfo = new PackageInfo("DEFAULT", 0);
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
     * @author y-higo
     */
    class PackageInfo {

        /**
         * 名前空間を初期化．名前空間名と深さを与える．デフォルトパッケージは0．
         * 
         * @param packageName 名前空間名
         */
        PackageInfo(final String packageName, final int depth) {
            
            if(null == packageName){
                throw new NullPointerException();
            }
            if(depth < 0){
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
                this.classInfos.put(classInfo.getName(), classInfo);

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

            if (null == fullQualifiedName){
                throw new NullPointerException();
            }
            
            int namespaceLength = fullQualifiedName.length - 1;

            // ほしいクラス情報の名前空間階層が，この名前空間階層よりも深い場合は，該当するサブ名前空間を呼び出す
            if (this.getDepth() < namespaceLength) {
                PackageInfo subPackage = this.subPackages.get(fullQualifiedName[this.getDepth()]);
                if (null == subPackage) {
                    return null;
                } else {
                    return subPackage.getClassInfo(fullQualifiedName);
                }

                // ほしいクラス情報の名前空間層が，この名前空間階層と同じ場合は，クラス情報を返す
            } else if (this.getDepth() == namespaceLength) {
                ClassInfo classInfo = this.classInfos.get(fullQualifiedName[namespaceLength]);
                return classInfo;

                // ほしいクラス情報の名前空間階層が，この名前空間階層よりも浅い場合は，エラー
            } else {
                throw new IllegalArgumentException("Illegal full qualified name: "
                        + fullQualifiedName.toString());
            }
        }
        
        Collection<ClassInfo> getClassInfos(final String[] namespace){
            
            if (null == namespace){
                throw new NullPointerException();
            }

            //ほしいクラス情報の名前空間層が，この名前空間層よりも深い場合は，該当するサブ名前空間名を呼び出す
            if (this.getDepth() < namespace.length){
                PackageInfo subPackage = this.subPackages.get(namespace[this.getDepth()]);
                if (null == subPackage) {
                    return Collections.unmodifiableCollection(new HashSet<ClassInfo>());
                }else{
                    return subPackage.getClassInfos(namespace);
                }
            
                // ほしいクラス情報の名前空間層が，この名前空間層と同じ場合は，クラス情報を返す
            }else if (this.getDepth() == namespace.length){
                
                return Collections.unmodifiableCollection(this.classInfos.values());
                //ほしいクラス情報の名前空間階層が，この名前空間階層よりも浅い場合は，エラー
            }else{
                throw new IllegalArgumentException("Illegal namepace: "
                        + namespace.toString());
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
