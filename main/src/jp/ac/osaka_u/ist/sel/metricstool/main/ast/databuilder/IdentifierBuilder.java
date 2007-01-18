package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;

import java.util.ArrayList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;

public class IdentifierBuilder extends DataBuilderAdapter<String[]>{
    
    public void entered(AstVisitEvent e) {
        if(isActive()){
            AstToken token = e.getToken();
            if (token.isIdentifier()){
                buildingIdentifiers.add(token.toString());
            } else if (token.isNameSeparator()){
                this.separatorCount++;
            }
        }
    }

    public void exited(AstVisitEvent e) {
        if(isActive()){
            AstToken token = e.getToken();
            if (token.isIdentifier() || token.isNameSeparator()){
                if (token.isNameSeparator()){
                    this.separatorCount--;
                }
                
                if (0 == this.separatorCount){
                    String[] buitIdentifier = buildIdentifier();
                    if (null != buildingIdentifiers){
                        registBuiltData(buitIdentifier);
                    }
                } else if (0 > this.separatorCount){
                    //activateされるタイミングによっては負値になる
                    this.separatorCount = 0;
                }
            }
        }
    }
    
    public void reset(){
        super.reset();
        buildingIdentifiers.clear();
        separatorCount = 0;
    }

    private String[] buildIdentifier(){
        if (!buildingIdentifiers.isEmpty()){
            String[] result = new String[buildingIdentifiers.size()];
            buildingIdentifiers.toArray(result);
            buildingIdentifiers.clear();
            return result;
        } else {
            return null;
        }
    }
    
    private int separatorCount;
    
    private final List<String> buildingIdentifiers = new ArrayList<String>();
}
