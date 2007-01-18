package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;

import java.util.LinkedHashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;

public abstract class CompoundDataBuilder<T> extends StateDrivenDataBuilder<T>{
    
    public final void addInnerBuilder(DataBuilder builder){
        if (null != builder){
            this.builders.add(builder);
        }
    }
    
    public void entered(AstVisitEvent e) {
        super.entered(e);
        
        if (isActive()){
            for(DataBuilder builder: builders){
                builder.entered(e);
            }
        }
    }

    public void exited(AstVisitEvent e) {
        if (isActive()){
            for(DataBuilder builder: builders){
                builder.exited(e);
            }
        }
        
        super.exited(e);
    }

    public final void removeInnerBuilder(DataBuilder builder){
        this.builders.remove(builder);
    }
    
    public void reset(){
        super.reset();
        for(DataBuilder builder : builders){
            builder.reset();
        }
    }
    
    private final Set<DataBuilder> builders = new LinkedHashSet<DataBuilder>();
}
