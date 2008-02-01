package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import java.util.ArrayList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


public class IdentifierBuilder extends DataBuilderAdapter<String[]> {

    @Override
    public void entered(AstVisitEvent e) {
        if (isActive()) {
            AstToken token = e.getToken();
            if (token.isIdentifier()) {
                this.buildingIdentifiers.add(token.toString());
            } else if (token.isNameSeparator()) {
                this.separatorCount++;
            } else {
                String[] buitIdentifier = buildIdentifier();
                if (null != buitIdentifier) {
                    registBuiltData(buitIdentifier);
                }
            }
        }
    }

    @Override
    public void exited(AstVisitEvent e) {
        if (isActive()) {
            AstToken token = e.getToken();
            if (token.isIdentifier() || token.isNameSeparator()) {
                if (token.isNameSeparator()) {
                    this.separatorCount--;
                }

                if (0 == this.separatorCount) {
                    String[] buitIdentifier = buildIdentifier();
                    if (null != buitIdentifier) {
                        registBuiltData(buitIdentifier);
                    }
                } else if (0 > this.separatorCount) {
                    //activateされるタイミングによっては負値になる
                    this.separatorCount = 0;
                }
            } else {
                String[] buitIdentifier = buildIdentifier();
                if (null != buitIdentifier) {
                    registBuiltData(buitIdentifier);
                }
            }
        }
    }

    @Override
    public boolean hasBuiltData() {
        return super.hasBuiltData() || !this.buildingIdentifiers.isEmpty();
    }

    @Override
    public String[] getLastBuildData() {
        String[] result = super.getLastBuildData();
        return null != result ? result : this.buildIdentifier();
    }

    @Override
    public String[] popLastBuiltData() {
        String[] result = super.popLastBuiltData();
        return null != result ? result : buildIdentifier();
    }

    @Override
    public void reset() {
        super.reset();
        this.buildingIdentifiers.clear();
        this.separatorCount = 0;
    }

    private String[] buildIdentifier() {
        if (!this.buildingIdentifiers.isEmpty()) {
            String[] result = new String[this.buildingIdentifiers.size()];
            this.buildingIdentifiers.toArray(result);
            this.buildingIdentifiers.clear();
            return result;
        }
        return null;
    }

    private int separatorCount;

    private final List<String> buildingIdentifiers = new ArrayList<String>();
}
