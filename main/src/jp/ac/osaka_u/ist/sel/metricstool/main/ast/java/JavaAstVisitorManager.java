package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;


import java.util.LinkedHashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BlockScopeBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ClassBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DataBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.FieldBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.InheritanceBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.LocalVariableBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.MethodBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.NameSpaceBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionDescriptionBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.InstanceElementBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.MethodCallBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.OperatorExpressionBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.SingleIdentifierBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.TypeElementBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitor;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitorManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.PositionManager;

public class JavaAstVisitorManager<T> implements AstVisitorManager<T> {
    public JavaAstVisitorManager(AstVisitor<T> visitor) {

        JavaModifiersInterpriter modifiersInterpriter = new JavaModifiersInterpriter();

        this.builders.add(new BlockScopeBuilder(buildDataManager));
        this.builders.add(new NameSpaceBuilder(buildDataManager));
        this.builders.add(new JavaImportBuilder(buildDataManager));
        this.builders.add(new ClassBuilder(buildDataManager, modifiersInterpriter));
        this.builders.add(new InheritanceBuilder(buildDataManager));
        this.builders.add(new JavaAnonymousClassBuilder(buildDataManager));
        this.builders.add(new JavaEnumElementBuilder(buildDataManager));
        //this.builders.add(new JavaGenericsBuilder());

        this.builders.add(new MethodBuilder(buildDataManager, modifiersInterpriter));
        //this.builders.add(new Initializerbuilder());
        //this.builders.add(new StaticInitializerBuilder());

        this.builders.add(new FieldBuilder(buildDataManager, modifiersInterpriter));
        this.builders.add(new LocalVariableBuilder(buildDataManager, modifiersInterpriter));

        // for expressions
        this.builders.add(new ExpressionDescriptionBuilder(expressionManager));
        this.builders.add(new SingleIdentifierBuilder(expressionManager, buildDataManager));
        this.builders.add(new JavaCompoundIdentifierBuilder(expressionManager, buildDataManager));
        this.builders.add(new TypeElementBuilder(expressionManager, buildDataManager));
        this.builders.add(new MethodCallBuilder(expressionManager, buildDataManager));
        this.builders.add(new InstanceElementBuilder(expressionManager));
        this.builders.add(new OperatorExpressionBuilder(expressionManager, buildDataManager));
        this.builders.add(new JavaConstructorCallBuilder(expressionManager,
                        buildDataManager));
        this.builders.add(new JavaArrayInstantiationBuilder(expressionManager));
        
        this.builders.add(new JavaExpressionElementBuilder(expressionManager,
                buildDataManager));
        
        for(DataBuilder builder :builders){
            visitor.addVisitListener(builder);
        }

        this.visitor = visitor;
    }
    
    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.java.AstVisitorManager#visitStart(antlr.collections.AST)
     */
    public void visitStart(T node){
        this.reset();
        
        visitor.visit(node);
    }
    
    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.java.AstVisitorManager#setLineColumnManager(jp.ac.osaka_u.ist.sel.metricstool.main.ast.LineColumnPositionManager)
     */
    public void setPositionManager(PositionManager lineColumn){
        visitor.setPositionManager(lineColumn);
    }
    
    private void reset(){
        for(DataBuilder builder: builders){
            builder.reset();
        }
        expressionManager.reset();
        buildDataManager.reset();
    }
    
    private final AstVisitor<T> visitor;
    
    private final BuildDataManager buildDataManager = new JavaBuildManager();
    private final ExpressionElementManager expressionManager = new ExpressionElementManager();
    private final Set<DataBuilder> builders = new LinkedHashSet<DataBuilder>();
}