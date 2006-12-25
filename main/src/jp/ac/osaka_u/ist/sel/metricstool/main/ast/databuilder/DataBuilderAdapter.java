package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;

/**
 * @author kou-tngt
 *
 */
public class DataBuilderAdapter<T> implements DataBuilder<T>{
    
    public final void activate() {
        this.active = true;
    }

    public final void clearBuiltData() {
        builtDataStack.clear();
    }

    public final void deactivate() {
        this.active = false;
    }
    
    public void entered(AstVisitEvent e) {
        // adapted method.
    }
    
    public void exited(AstVisitEvent e) {
        // adapted method.
    }

    public final List<T> getBuiltDatas() {
        return Collections.unmodifiableList(builtDataStack);
    }
    
    public final int getBuiltDataCount(){
        return builtDataStack.size();
    }
    
    public T getFirstBuiltData() {
        if (!builtDataStack.isEmpty()){
            return builtDataStack.firstElement();
        } else {
            return null;
        }
    }

    public T getLastBuildData() {
        if (!builtDataStack.isEmpty()){
            return builtDataStack.peek();
        } else {
            return null;
        }
    }
    
    public void init(){
        this.clearBuiltData();
    }

    public final T popLastBuiltData() {
        if (!builtDataStack.isEmpty()){
            return builtDataStack.pop();
        } else {
            return null;
        }
    }
    
    public boolean hasBuiltData(){
        return !this.builtDataStack.isEmpty();
    }

    public boolean isActive() {
        return this.active;
    }
    
    public void reset() {
        this.builtDataStack.clear();
    }
    
    public final void visited(AstVisitEvent e) {
        // Žg‚í‚È‚¢‚±‚Æ‚É‚·‚é
    }
    
    protected final void registBuiltData(T data){
        builtDataStack.add(data);
    }

    private boolean active = true;
    private final Stack<T> builtDataStack = new Stack<T>();
}
