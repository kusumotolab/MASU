package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldUsage;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableInfo;

/**
 * 
 * 
 * @author kou-tngt
 */
public class CompoundIdentifierBuilder extends ExpressionBuilder{

    /**
     * @param expressionManager
     */
    public CompoundIdentifierBuilder(ExpressionElementManager expressionManager, BuildDataManager buildManager) {
        super(expressionManager);
        this.buildDataManager = buildManager;
    }
    
    protected void afterExited(AstVisitEvent event){
        
        AstToken token = event.getToken();
        if (token.isNameSeparator()){
            buildCompoundIdentifierElement();
        }
    }
    
    protected void buildCompoundIdentifierElement(){
        ExpressionElement[] elements = getAvailableElements();
        
        if (elements.length == 2){
            ExpressionElement left = elements[0];
            ExpressionElement right = elements[1];
            
            if (right instanceof SingleIdentifierElement){
                //右側は単一の識別子のはず
                
                SingleIdentifierElement rightIdentifier = (SingleIdentifierElement)right;
                String rightName = rightIdentifier.getName();
                
                UnresolvedTypeInfo leftElementType = null;
                
                if (left instanceof FieldOrMethodElement){
                    IdentifierElement leftIdentifier = (IdentifierElement)left;
                    leftElementType = leftIdentifier.resolveAsReferencedVariable(buildDataManager);
                }
                else if (left instanceof SingleIdentifierElement){
                    //左側も単一の識別子なら、そいつは変数かもしれない
                    SingleIdentifierElement leftIdentifier = (SingleIdentifierElement)left;
                    String leftName = leftIdentifier.getName();
                    UnresolvedVariableInfo variable = buildDataManager.getCurrentScopeVariable(leftName);
                    
                    if (null != variable){
                        //スコープ内に変数がみつかった
                        if (variable instanceof UnresolvedFieldInfo){
                            //実はフィールドでした
                            leftElementType = new UnresolvedFieldUsage(buildDataManager.getAvailableNameSpaceSet(),
                                    buildDataManager.getCurrentClass(),leftName);
                        } else {
                            leftElementType = variable.getType();
                        }
                    }
                } else if (left.equals(InstanceSpecificElement.THIS)){
                    //左側がthisなら右側はこのクラスのフィールド名かメソッド名
                    if (null != buildDataManager){
                        leftElementType = buildDataManager.getCurrentClass();
                    }
                } else {
                    leftElementType = left.getType();
                }
                
                if (null != leftElementType){
                    //左側の型が決定できたので右側はフィールド名かメソッド名だろう
                    pushElement(new FieldOrMethodElement(leftElementType,rightName));
                } else if (left instanceof IdentifierElement){
                    //左側の型が分からないので全体をなんかよく分からん識別子として扱う
                    pushElement(new CompoundIdentifierElement((IdentifierElement)left,rightName));
                } else {
                    assert(false) : "Illegal state: unknown left element type.";
                }
            }
            else {
                assert(false) : "Illegal state: unexpected element type.";
            }
        } else {
            assert(false) : "Illegal state: two elements must be available.";
        }
    }

    @Override
    protected boolean isTriggerToken(AstToken token) {
        return token.isNameSeparator();
    }
    
    private final BuildDataManager buildDataManager;
}
