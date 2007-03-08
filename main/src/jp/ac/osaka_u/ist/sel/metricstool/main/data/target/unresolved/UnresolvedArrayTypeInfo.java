package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.HashMap;
import java.util.Map;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決配列型を表すためのクラス．以下の情報を持つ．
 * <ul>
 * <li>未解決型 (UnresolvedTypeInfo)</li>
 * <li>次元 (int)</li>
 * </ul>
 * 
 * @author y-higo
 * @see UnresolvedTypeInfo
 */
public final class UnresolvedArrayTypeInfo implements UnresolvedTypeInfo, UnresolvedEntityUsageInfo {

    // /**
    // * 等しいかどうかのチェックを行う
    // */
    // public boolean equals(final UnresolvedEntityUsage entityUsage) {
    //
    // if (null == entityUsage) {
    // throw new NullPointerException();
    // }
    //
    // if (!(entityUsage instanceof UnresolvedArrayUsage)) {
    // return false;
    // }
    //
    // final UnresolvedEntityUsage elementTypeInfo = this.getElementType();
    // final UnresolvedEntityUsage correspondElementTypeInfo = ((UnresolvedArrayUsage) entityUsage)
    // .getElementType();
    // if (!elementTypeInfo.equals(correspondElementTypeInfo)) {
    // return false;
    // }
    //
    // final int dimension = this.getDimension();
    // final int correspondDimension = ((UnresolvedArrayUsage) entityUsage).getDimension();
    // return dimension == correspondDimension;
    // }

    /**
     * この未解決配列使用が解決済みかどうか返す
     * 
     * @return 解決済みの場合は true, そうでない場合は false
     */
    public boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * 解決済み配列使用を返す
     * 
     * @return 解決済み配列使用
     * @throws NotResolvedException 未解決の場合にスローされる
     */
    public EntityUsageInfo getResolvedEntityUsage() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }

    /**
     * 未解決配列使用を解決し，解決済み参照を返す．
     * 
     * @param usingClass 未解決配列使用が行われているクラス
     * @param usingMethod 未解決配列使用が行われているメソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     * @return 解決済み配列使用
     */
    public EntityUsageInfo resolveEntityUsage(final TargetClassInfo usingClass,
            final TargetMethodInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == classInfoManager)) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolvedEntityUsage();
        }

        final UnresolvedEntityUsageInfo unresolvedElement = this.getElementType();
        final int dimension = this.getDimension();

        final EntityUsageInfo element = unresolvedElement.resolveEntityUsage(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        assert element != null : "resolveEntityUsage returned null!";

        // 要素の型が不明のときは UnnownTypeInfo を返す
        if (element instanceof UnknownEntityUsageInfo) {
            this.resolvedInfo = UnknownEntityUsageInfo.getInstance();
            return this.resolvedInfo;
        }

        // 要素の型が解決できた場合はその配列型を作成し返す
        this.resolvedInfo = ArrayTypeInfo.getType(element, dimension);
        return this.resolvedInfo;
    }

    /**
     * 配列の要素の未解決型を返す
     * 
     * @return 配列の要素の未解決型
     */
    public UnresolvedEntityUsageInfo getElementType() {
        return this.type;
    }

    /**
     * 配列の次元を返す
     * 
     * @return 配列の次元
     */
    public int getDimension() {
        return this.dimension;
    }

    /**
     * このインスタンスが表す配列の次元を1大きくした配列を表すインスタンスを返す．
     * 
     * @return このインスタンスが表す配列の次元を1大きくした配列
     */
    public UnresolvedArrayTypeInfo getDimensionInclementedArrayType() {
        return getType(getElementType(), getDimension() + 1);
    }

    /**
     * UnresolvedArrayTypeInfo のインスタンスを返すためのファクトリメソッド．
     * 
     * @param type 未解決型を表す変数
     * @param dimension 次元を表す変数
     * @return 生成した UnresolvedArrayTypeInfo オブジェクト
     */
    public static UnresolvedArrayTypeInfo getType(final UnresolvedEntityUsageInfo type,
            final int dimension) {

        if (null == type) {
            throw new NullPointerException();
        }
        if (dimension < 1) {
            throw new IllegalArgumentException("Array dimension must be 1 or more!");
        }

        final Key key = new Key(type, dimension);
        UnresolvedArrayTypeInfo arrayUsage = ARRAY_TYPE_MAP.get(key);
        if (arrayUsage == null) {
            arrayUsage = new UnresolvedArrayTypeInfo(type, dimension);
            ARRAY_TYPE_MAP.put(key, arrayUsage);
        }

        return arrayUsage;
    }

    /**
     * 未解決配列型オブジェクトの初期化を行う．配列の要素の未解決型と配列の次元が与えられなければならない
     * 
     * @param type 配列の要素の未解決型
     * @param dimension 配列の次元
     */
    private UnresolvedArrayTypeInfo(final UnresolvedEntityUsageInfo type, final int dimension) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == type) {
            throw new NullPointerException();
        }
        if (1 > dimension) {
            throw new IllegalArgumentException("Array dimension must be 1 or more!");
        }

        this.type = type;
        this.dimension = dimension;
        this.resolvedInfo = null;
    }

    /**
     * 配列の要素の型を保存する変数
     */
    private final UnresolvedEntityUsageInfo type;

    /**
     * 配列の次元を保存する変数
     */
    private final int dimension;

    /**
     * 解決済み配列使用を保存するための変数
     */
    private EntityUsageInfo resolvedInfo;

    /**
     * UnresolvedArrayTypeInfo オブジェクトを一元管理するための Map．オブジェクトはファクトリメソッドで生成される．
     */
    private static final Map<Key, UnresolvedArrayTypeInfo> ARRAY_TYPE_MAP = new HashMap<Key, UnresolvedArrayTypeInfo>();

    /**
     * 変数の型と次元を用いてキーとなるクラス．
     * 
     * @author y-higo
     */
    static class Key {

        /**
         * 第一キー
         */
        private final UnresolvedEntityUsageInfo type;

        /**
         * 第二キー
         */
        private final int dimension;

        /**
         * 第一，第二キーから，キーオブジェクトを生成する
         * 
         * @param type 第一キー
         * @param dimension 第二キー
         */
        Key(final UnresolvedEntityUsageInfo type, final int dimension) {

            if (null == type) {
                throw new NullPointerException();
            }
            if (1 > dimension) {
                throw new IllegalArgumentException("Array dimension must be 1 or more!");
            }

            this.type = type;
            this.dimension = dimension;
        }

        /**
         * このオブジェクトのハッシュコードを返す．
         */
        @Override
        public int hashCode() {
            return this.type.hashCode() + this.dimension;
        }

        /**
         * このキーオブジェクトの第一キーを返す．
         * 
         * @return 第一キー
         */
        public UnresolvedEntityUsageInfo getFirstKey() {
            return this.type;
        }

        /**
         * このキーオブジェクトの第二キーを返す．
         * 
         * @return 第二キー
         */
        public int getSecondKey() {
            return this.dimension;
        }

        /**
         * このオブジェクトと引数で指定されたオブジェクトが等しいかを返す．
         */
        @Override
        public boolean equals(Object o) {

            if (null == o) {
                throw new NullPointerException();
            }

            final UnresolvedEntityUsageInfo firstKey = this.getFirstKey();
            final UnresolvedEntityUsageInfo correspondFirstKey = ((Key) o).getFirstKey();
            if (!firstKey.equals(correspondFirstKey)) {
                return false;
            }

            final int secondKey = this.getSecondKey();
            final int correspondSecondKey = ((Key) o).getSecondKey();
            return secondKey == correspondSecondKey;
        }
    }
}
