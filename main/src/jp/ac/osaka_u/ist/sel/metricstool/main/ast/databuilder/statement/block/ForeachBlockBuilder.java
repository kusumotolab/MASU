package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.statement.block;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.LocalVariableBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.statement.LocalVariableDeclarationStatementBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.ForBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.ForeachBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.InnerBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForeachBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedForeachBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.ForeachBlockStateManager.FOREACH_BLOCK_STATE_CHANGE;;

/* **
 * For-Eachブロックのビルダー
 * @author a-saitoh
 *
 */
public class ForeachBlockBuilder extends InnerBlockBuilder<ForeachBlockInfo, UnresolvedForeachBlockInfo> {

    
    public ForeachBlockBuilder(final BuildDataManager targetDataManager,
            final LocalVariableBuilder localVariableBuilder, final ExpressionElementManager expressionManager, final LocalVariableDeclarationStatementBuilder variableBuilder) {
        super(targetDataManager, new ForeachBlockStateManager());
        this.declarationStatementBuilder = variableBuilder;
        this.expressionElementManager = expressionManager;

    }
    
    @Override
    protected UnresolvedForeachBlockInfo createUnresolvedBlockInfo(UnresolvedLocalSpaceInfo outerSpace) {
        

        return new UnresolvedForeachBlockInfo(outerSpace);
    }
    
    @Override
    public void stateChangend(final StateChangeEvent<AstVisitEvent> event){
        super.stateChangend(event);
        final StateChangeEventType type = event.getType();
        
        if(type.equals(FOREACH_BLOCK_STATE_CHANGE.ENTER_FOR_EACH_CLAUSE)){

        }
        else if(type.equals(FOREACH_BLOCK_STATE_CHANGE.EXIT_FOR_EACH_CLAUSE)){

            final UnresolvedForeachBlockInfo buildingBlock = this.getBuildingBlock();
            buildingBlock.setIteratorExpression(expressionElementManager.getPeekExpressionElement().getUsage());

        }else if(type.equals(FOREACH_BLOCK_STATE_CHANGE.ENTER_LOCAL_PARAMETER)){
            
        }else if(type.equals(FOREACH_BLOCK_STATE_CHANGE.EXIT_LOCAL_PARAMETER)){
            if(null != this.declarationStatementBuilder && null != this.declarationStatementBuilder.getLastBuildData()){

                final UnresolvedForeachBlockInfo buildingBlock = this.getBuildingBlock();
                buildingBlock.setIteratorVariableDeclaration(this.declarationStatementBuilder.getLastBuildData());
                declarationStatementBuilder.deactivate();
                
            }
        }
       
    }
    
    private final ExpressionElementManager expressionElementManager;

    private final LocalVariableDeclarationStatementBuilder declarationStatementBuilder;

}
