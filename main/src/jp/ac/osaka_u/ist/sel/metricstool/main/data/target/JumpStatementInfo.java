package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Set;


public abstract class JumpStatementInfo extends SingleStatementInfo {

    public JumpStatementInfo(final LocalSpaceInfo ownerSpace, final LabelInfo destinationLabel,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(ownerSpace, fromLine, fromColumn, toLine, toColumn);

        this.destinationLabel = destinationLabel;
    }

    @Override
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        return Collections.EMPTY_SET;
    }
    
    @Override
    public String getText() {
        final StringBuilder text = new StringBuilder(this.getReservedKeyword());
        if(null != this.getDestinationLabel()) {
            text.append(" ").append(this.getDestinationLabel().getText());
        }
        return text.toString();
    }
    
    protected abstract String getReservedKeyword();
    

    public abstract StatementInfo getFollowingStatement();

    public LabelInfo getDestinationLabel() {
        return this.destinationLabel;
    }

    private final LabelInfo destinationLabel;


}
