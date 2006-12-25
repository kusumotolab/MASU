package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;

import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitListener;

public interface DataBuilder<T> extends AstVisitListener{
    
    public void activate();
    
    public void clearBuiltData();
    
    public void deactivate();
    
    public List<T> getBuiltDatas();
    
    public int getBuiltDataCount();
    
    public T getFirstBuiltData();
    
    public T getLastBuildData();
    
    public T popLastBuiltData();
    
    public boolean hasBuiltData();
    
    public boolean isActive();
    
    public void reset();
}
