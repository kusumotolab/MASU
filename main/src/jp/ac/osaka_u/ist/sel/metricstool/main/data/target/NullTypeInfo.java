package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


/**
 * void 型を表すクラス．
 * 
 * @author higo, t-miyake
 * 
 */
public final class NullTypeInfo extends EntityUsageInfo implements UnresolvedEntityUsageInfo, TypeInfo, UnresolvedTypeInfo {

    /**
     * このクラスの単一オブジェクトを返す
     * 
     * @return このクラスの単一オブジェクト
     */
    public static NullTypeInfo getInstance() {
        return SINGLETON;
    }

    /**
     * void 型の名前を返す．
     */
    public String getTypeName() {
        return this.name;
    }

    /**
     * 等しいかどうかのチェックを行う
     */
    public boolean equals(final TypeInfo typeInfo) {

        if (null == typeInfo) {
            throw new NullPointerException();
        }

        return typeInfo instanceof NullTypeInfo;
    }

    /**
     * 名前解決されているかどうかを返す．
     * 
     * @return 常に true を返す
     */
    public boolean alreadyResolved() {
        return true;
    }

    /**
     * 名前解決された型情報を返す
     * 
     * @return 自分自身を返す
     */
    public TypeInfo getResolvedType() {
        return this;
    }

    /**
     * 名前解決された使用情報を返す
     * 
     * @return 自分自身を返す
     */
    @Override
	public EntityUsageInfo getResolvedEntityUsage() {
		return this;
	}

    /**
     * 使用情報の名前解決する
     * nullは既に解決済みなので自分自身をそのまま返す
     * 
     * @return 解決済みの使用情報（自分自身）
     */
	@Override
	public EntityUsageInfo resolveEntityUsage(TargetClassInfo usingClass,
			TargetMethodInfo usingMethod, ClassInfoManager classInfoManager,
			FieldInfoManager fieldInfoManager,
			MethodInfoManager methodInfoManager) {
		return this;
	}

	/**
	 * 名前解決された型を返す
	 * 
	 * @return 自分自身
	 */
	@Override
	public TypeInfo getType() {
		return this;
	}

	/**
     * 名前解決を行う
     * 
     * @param usingClass 名前解決を行うエンティティがあるクラス
     * @param usingMethod 名前解決を行うエンティティがあるメソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     * 
     * @return 解決済みの型（自分自身）
     */
    public TypeInfo resolveType(final TargetClassInfo usingClass,
            final TargetMethodInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {
        return this;
    }

    /**
     * null 型の型名を表す定数
     */
    public static final String NULL_STRING = new String("null");

    /**
     * 引数なしコンストラクタ
     */
    private NullTypeInfo() {
        this.name = NULL_STRING;
    }

    /**
     * このクラスの単一オブジェクトを保存するための定数
     */
    private static final NullTypeInfo SINGLETON = new NullTypeInfo();

    /**
     * この型の名前を保存するための変数
     */
    private final String name;
}
