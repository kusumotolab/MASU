package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;


import java.util.LinkedHashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BlockScopeBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ClassBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ConstructorBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DataBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.FieldBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.InheritanceBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.LocalParameterBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.LocalVariableBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.MethodBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.MethodParameterBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ModifiersBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.NameBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.NameSpaceBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionDescriptionBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.InstanceElementBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.MethodCallBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.OperatorExpressionBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.SingleIdentifierBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock.CaseEntryBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock.CatchBlockBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock.DefaultEntryBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock.DoBlockBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock.ElseBlockBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock.FinallyBlockBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock.ForBlockBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock.IfBlockBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock.SimpleBlockBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock.SwitchBlockBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock.SynchronizedBlockBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock.TryBlockBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock.WhileBlockBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.ElseBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitor;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitorManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.PositionManager;


/**
 * Java言語専用のデータ登録クラス群を適切にビジターに登録し，ビジターに対する操作を行うメソッドを提供する．
 * 
 * @author kou-tngt
 *
 * @param <T> 管理するVisitorが訪問するASTのノードの型
 */
public class JavaAstVisitorManager<T> implements AstVisitorManager<T> {
    
    /**
     * Java用のビジターを構築する
     * 
     * @param visitor
     */
    public JavaAstVisitorManager(final AstVisitor<T> visitor) {

        final JavaModifiersInterpriter modifiersInterpriter = new JavaModifiersInterpriter();

        this.builders.add(new BlockScopeBuilder(this.buildDataManager));
        this.builders.add(new NameSpaceBuilder(this.buildDataManager));
        this.builders.add(new JavaImportBuilder(this.buildDataManager));
        this.builders.add(new ClassBuilder(this.buildDataManager, modifiersInterpriter));
        this.builders.add(new InheritanceBuilder(this.buildDataManager, new JavaTypeBuilder(
                this.buildDataManager)));
        this.builders.add(new JavaAnonymousClassBuilder(this.buildDataManager));
        this.builders.add(new JavaEnumElementBuilder(this.buildDataManager));
        this.builders.add(new JavaIntefaceMarker(this.buildDataManager));
        this.builders.add(new JavaTypeParameterBuilder(this.buildDataManager));

        this.builders.add(new ConstructorBuilder(this.buildDataManager, modifiersInterpriter,
                new ModifiersBuilder(), new JavaTypeBuilder(this.buildDataManager),
                new NameBuilder(), new MethodParameterBuilder(this.buildDataManager,
                        new ModifiersBuilder(), new JavaTypeBuilder(this.buildDataManager),
                        new NameBuilder(), modifiersInterpriter)));
        
        this.builders.add(new MethodBuilder(this.buildDataManager, modifiersInterpriter,
                new ModifiersBuilder(), new JavaTypeBuilder(this.buildDataManager),
                new NameBuilder(), new MethodParameterBuilder(this.buildDataManager,
                        new ModifiersBuilder(), new JavaTypeBuilder(this.buildDataManager),
                        new NameBuilder(), modifiersInterpriter)));
        //this.builders.add(new Initializerbuilder());
        //this.builders.add(new StaticInitializerBuilder());

        this.builders
                .add(new FieldBuilder(this.buildDataManager, new ModifiersBuilder(),
                        new JavaTypeBuilder(this.buildDataManager), new NameBuilder(),
                        modifiersInterpriter));
        this.builders
                .add(new LocalVariableBuilder(this.buildDataManager, new ModifiersBuilder(),
                        new JavaTypeBuilder(this.buildDataManager), new NameBuilder(),
                        modifiersInterpriter));
        this.builders
                .add(new LocalParameterBuilder(this.buildDataManager, new ModifiersBuilder(),
                        new JavaTypeBuilder(this.buildDataManager), new NameBuilder(),
                        modifiersInterpriter));

        // for expressions
        this.builders.add(new ExpressionDescriptionBuilder(this.expressionManager,
                this.buildDataManager));
        this.builders
                .add(new SingleIdentifierBuilder(this.expressionManager, this.buildDataManager));
        this.builders.add(new JavaCompoundIdentifierBuilder(this.expressionManager,
                this.buildDataManager));
        this.builders.add(new JavaTypeElementBuilder(this.expressionManager, this.buildDataManager));
        this.builders.add(new MethodCallBuilder(this.expressionManager, this.buildDataManager));
        this.builders.add(new InstanceElementBuilder(this.expressionManager));
        this.builders.add(new OperatorExpressionBuilder(this.expressionManager,
                this.buildDataManager));
        this.builders.add(new JavaConstructorCallBuilder(this.expressionManager,
                this.buildDataManager));
        this.builders.add(new JavaArrayInstantiationBuilder(this.expressionManager));

        this.builders.add(new JavaExpressionElementBuilder(this.expressionManager,
                this.buildDataManager));

        this.addInnerBlockBuilder();
        
        for (final DataBuilder builder : this.builders) {
            visitor.addVisitListener(builder);
        }

        this.visitor = visitor;
    }
    
    private void addInnerBlockBuilder() {
        this.builders.add(new CaseEntryBuilder(this.buildDataManager));
        this.builders.add(new CatchBlockBuilder(this.buildDataManager));
        this.builders.add(new DefaultEntryBuilder(this.buildDataManager));
        this.builders.add(new DoBlockBuilder(this.buildDataManager));
        this.builders.add(new ElseBlockBuilder(this.buildDataManager));
        this.builders.add(new FinallyBlockBuilder(this.buildDataManager));
        this.builders.add(new ForBlockBuilder(this.buildDataManager));
        this.builders.add(new IfBlockBuilder(this.buildDataManager));
        this.builders.add(new SimpleBlockBuilder(this.buildDataManager));
        this.builders.add(new SwitchBlockBuilder(this.buildDataManager));
        this.builders.add(new SynchronizedBlockBuilder(this.buildDataManager));
        this.builders.add(new TryBlockBuilder(this.buildDataManager));
        this.builders.add(new WhileBlockBuilder(this.buildDataManager));
    }
    

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.java.AstVisitorManager#visitStart(antlr.collections.AST)
     */
    public void visitStart(final T node) {
        this.reset();

        this.visitor.startVisiting(node);
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.java.AstVisitorManager#setLineColumnManager(jp.ac.osaka_u.ist.sel.metricstool.main.ast.LineColumnPositionManager)
     */
    public void setPositionManager(final PositionManager lineColumn) {
        this.visitor.setPositionManager(lineColumn);
    }

    /**
     * ビジターの状態と構築中のデータをリセットする．
     */
    private void reset() {
        for (final DataBuilder builder : this.builders) {
            builder.reset();
        }
        this.expressionManager.reset();
        this.buildDataManager.reset();
    }

    /**
     * 管理するビジター
     */
    private final AstVisitor<T> visitor;

    /**
     * 構築中のデータの管理者
     */
    private final JavaBuildManager buildDataManager = new JavaBuildManager();

    /**
     * 解析中の式データの管理者
     */
    private final ExpressionElementManager expressionManager = new ExpressionElementManager();

    /**
     * ビジターにセットしたビルダー群のセット
     */
    private final Set<DataBuilder> builders = new LinkedHashSet<DataBuilder>();
}