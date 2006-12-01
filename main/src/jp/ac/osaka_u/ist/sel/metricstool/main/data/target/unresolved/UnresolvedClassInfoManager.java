package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * UnresolvedClassInfoManager を操作するクラス
 * 
 * @author y-higo
 * 
 */
public class UnresolvedClassInfoManager {

    /**
     * 単一オブジェクトを返す
     */
    public static UnresolvedClassInfoManager getInstance() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        return SINGLETON;
    }

    /**
     * クラス情報を追加する
     * 
     * @param classInfo クラス情報
     */
    public void addClass(final UnresolvedClassInfo classInfo) {

        if (null == classInfo) {
            throw new NullPointerException();
        }

        ClassKey classKey = new ClassKey(classInfo.getFullQualifiedName());
        this.classInfos.put(classKey, classInfo);
    }

    /**
     * クラス情報のセットを返す
     * 
     * @return クラス情報のセット
     */
    public Collection<UnresolvedClassInfo> getUnresolvedClassInfos() {
        return Collections.unmodifiableCollection(this.classInfos.values());
    }

    /**
     * クラス情報を Map に保存するためのキー
     * 
     * @author y-higo
     * 
     */
    class ClassKey implements Comparable<ClassKey> {

        /**
         * コンストラクタ．クラスの完全修飾名を与える
         * 
         * @param fullQualifiedName クラスの完全修飾名
         */
        ClassKey(final String[] fullQualifiedName) {
            this.fullQualifiedName = fullQualifiedName;
        }

        /**
         * キーを返す
         * 
         * @return キー． クラスの完全修飾名
         */
        public String[] getFullQualifiedName() {
            return this.fullQualifiedName;
        }

        /**
         * キーの順序を定義する
         */
        public int compareTo(final ClassKey classKey) {
            String[] fullQualifiedName = this.getFullQualifiedName();
            String[] correspondFullQualifiedName = classKey.getFullQualifiedName();

            if (fullQualifiedName.length > correspondFullQualifiedName.length) {
                return 1;
            } else if (fullQualifiedName.length < correspondFullQualifiedName.length) {
                return -1;
            } else {
                for (int i = 0; i < fullQualifiedName.length; i++) {
                    int order = fullQualifiedName[i].compareTo(correspondFullQualifiedName[i]);
                    if (order != 0) {
                        return order;
                    }
                }

                return 0;
            }
        }

        /**
         * このクラスと対象クラスが等しいかどうかを判定する
         */
        public boolean equals(Object o) {

            String[] fullQualifiedName = this.getFullQualifiedName();
            String[] correspondFullQualifiedName = ((UnresolvedClassInfo) o).getFullQualifiedName();

            if (fullQualifiedName.length != correspondFullQualifiedName.length) {
                return false;
            }

            for (int i = 0; i < fullQualifiedName.length; i++) {
                if (!fullQualifiedName[i].equals(correspondFullQualifiedName[i])) {
                    return false;
                }
            }

            return true;
        }

        /**
         * このクラスのハッシュコードを返す
         */
        public int hashCode() {

            StringBuffer buffer = new StringBuffer();
            String[] fullQualifiedName = this.getFullQualifiedName();
            for (int i = 0; i < fullQualifiedName.length; i++) {
                buffer.append(fullQualifiedName[i]);
            }

            return buffer.toString().hashCode();
        }

        private final String[] fullQualifiedName;
    }

    /**
     * 引数なしコンストラクタ
     * 
     */
    private UnresolvedClassInfoManager() {
        this.classInfos = new HashMap<ClassKey, UnresolvedClassInfo>();
    }

    /**
     * 単一オブジェクトを保存するための変数
     */
    private final static UnresolvedClassInfoManager SINGLETON = new UnresolvedClassInfoManager();

    /**
     * UnresolvedClassInfo を保存するためのセット
     */
    private final Map<ClassKey, UnresolvedClassInfo> classInfos;
}
