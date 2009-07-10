package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StaticInitializerStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedStaticInitializerInfo;


// TODO åãã«InstanceInitializerÇ∆ìØÇ∂Ç…Ç»ÇÈÇ©ÇÁÇ§Ç‹Ç¢Ç±Ç∆Ç‹Ç∆ÇﬂÇÍÇÈÇÕÇ∏ÅDÇƒÇ¢Ç§Ç©CallableUnitBuilderÇ≈Ç¢Ç¢ÇÃ?
public class StaticInitializerBuilder extends CallableUnitBuilder<UnresolvedStaticInitializerInfo> {

    public StaticInitializerBuilder(BuildDataManager buildDataManager,
            ModifiersInterpriter interpriter) {
        super(buildDataManager, new StaticInitializerStateManager(), interpriter);
    }

    @Override
    protected UnresolvedStaticInitializerInfo startUnitDefinition(final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        UnresolvedStaticInitializerInfo initializer = super.startUnitDefinition(fromLine, fromColumn, toLine, toColumn);
        initializer.getOwnerClass().addStaticInitializer(initializer);
        return initializer;
    }

    @Override
    protected UnresolvedStaticInitializerInfo createUnresolvedCallableUnitInfo(int fromLine,
            int fromColumn, int toLine, int toColumn) {
        return new UnresolvedStaticInitializerInfo(this.buildManager.getCurrentClass(), fromLine,
                fromColumn, toLine, toColumn);
    }

    @Override
    protected void registName() {
        // i have no name
    }

    @Override
    protected void registType() {
        // and no type
    }

}
