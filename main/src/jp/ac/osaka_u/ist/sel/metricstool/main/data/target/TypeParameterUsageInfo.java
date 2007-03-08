package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


public final class TypeParameterUsageInfo extends EntityUsageInfo {

    public TypeParameterUsageInfo(final EntityUsageInfo entityUsage) {

        super();

        if (null == entityUsage) {
            throw new NullPointerException();
        }

        this.entityUsage = entityUsage;
    }

    @Override
    public TypeInfo getType(){
       return this.entityUsage.getType();
    }
    
    public EntityUsageInfo getEntityUsage() {
        return this.entityUsage;
    }

    private final EntityUsageInfo entityUsage;
}
