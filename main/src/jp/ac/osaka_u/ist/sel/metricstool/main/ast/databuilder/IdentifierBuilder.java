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
                    registBuiltData(buildIdentifier());
                } else if (0 > this.separatorCount){
                    //activateされるタイミングによっては負値になる
                    this.separatorCount = 0;
                }
            }
        }
    }

    private String[] buildIdentifier(){
        assert(!buildingIdentifiers.isEmpty()) : "Illegal state: identifier tokens were not found.";
        
        String[] result = new String[buildingIdentifiers.size()];
        buildingIdentifiers.toArray(result);
        buildingIdentifiers.clear();
        return result;
    }
    
    private int separatorCount;
    
    private final List<String> buildingIdentifiers = new ArrayList<String>();
}
