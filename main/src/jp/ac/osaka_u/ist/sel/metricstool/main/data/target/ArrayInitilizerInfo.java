package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class ArrayInitilizerInfo extends ExpressionInfo {

    public ArrayInitilizerInfo(List<ExpressionInfo> elements,
            final CallableUnitInfo ownerMethod, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {
        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);

        if (null == elements) {
            throw new IllegalArgumentException("elements is null");
        }
        this.elements = Collections.unmodifiableList(elements);
        
        for(final ExpressionInfo element : this.elements) {
            element.setOwnerExecutableElement(this);
        }
    }

    public List<ExpressionInfo> getElements() {
        return elements;
    }
    
    public final int getArrayLength() {
        return this.elements.size();
    }
    
    @Override
    public String getText() {
        final StringBuilder text = new StringBuilder();
        text.append("{");
        
        final Iterator<ExpressionInfo> elements = this.elements.iterator();
        if(elements.hasNext()) {
            text.append(elements.next().getText());
        }
        while(elements.hasNext()) {
            text.append(", ");
            text.append(elements.next().getText());
        }
        
        text.append("}");
        return text.toString();
    }

    @Override
    public TypeInfo getType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> usages = new TreeSet<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>>();
        for(final ExpressionInfo element: this.elements) {
            usages.addAll(element.getVariableUsages());
        }
        return Collections.unmodifiableSet(usages);
    }

    private final List<ExpressionInfo> elements;

}
