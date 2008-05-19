package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import java.util.Set;


/**
 * 型パラメータの使用を表すクラス
 * 
 * @author higo
 *
 */
public final class TypeParameterUsageInfo extends EntityUsageInfo {

    /**
     * 必要な情報を与えて，オブジェクトを初期化
     * 
     * @param entityUsage 利用されているエンティティ
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public TypeParameterUsageInfo(final EntityUsageInfo entityUsage, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        if (null == entityUsage) {
            throw new NullPointerException();
        }

        this.entityUsage = entityUsage;
    }

    @Override
    public TypeInfo getType() {
        return this.entityUsage.getType();
    }

    /**
     * エンティティを返す
     * 
     * @return エンティティ
     */
    public EntityUsageInfo getEntityUsage() {
        return this.entityUsage;
    }

    /**
     * 型パラメータの使用に変数使用が含まれることはないので空のセットを返す
     * 
     * @return 空のセット
     */
    @Override
    public final Set<VariableUsageInfo<?>> getVariableUsages() {
        return VariableUsageInfo.EmptySet;
    }
    
    private final EntityUsageInfo entityUsage;
}
