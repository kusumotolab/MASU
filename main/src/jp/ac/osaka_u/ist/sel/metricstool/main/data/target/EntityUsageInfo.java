package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public abstract class EntityUsageInfo {

    EntityUsageInfo() {
        this.contexts = new LinkedList<EntityUsageInfo>();
    }

    /**
     * エンティティ使用の型を返す．
     * 
     * @return エンティティ使用の型
     */
    public abstract TypeInfo getType();

    public final List<EntityUsageInfo> getContexts() {
        return Collections.unmodifiableList(this.contexts);
    }

    private final List<EntityUsageInfo> contexts;
}
