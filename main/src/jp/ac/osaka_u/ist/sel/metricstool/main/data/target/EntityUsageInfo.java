package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * 変数の使用やメソッドの呼び出しなど，プログラム要素の使用を表すインターフェース
 * 
 * @author higo
 *
 */
public abstract class EntityUsageInfo {

    /**
     * オブジェクトを初期化 
     */
    EntityUsageInfo() {
    }

    /**
     * エンティティ使用の型を返す．
     * 
     * @return エンティティ使用の型
     */
    public abstract TypeInfo getType();
}
