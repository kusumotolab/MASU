package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFG;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGUtility;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.SimpleCFG;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGControlEdge;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGNormalEdge;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayElementUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BinominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CastUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalClauseInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ElseBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EmptyExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForeachConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.IfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LiteralUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MonominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.NullUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParenthesesExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TernaryOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


public class CFGVariableDeclarationStatementNode extends
        CFGStatementNode<VariableDeclarationStatementInfo> {

    CFGVariableDeclarationStatementNode(final VariableDeclarationStatementInfo statement) {
        super(statement);
    }

    @Override
    public CFG dissolve(final ICFGNodeFactory nodeFactory) {

        final VariableDeclarationStatementInfo statement = this.getCore();
        final ExpressionInfo expression = statement.getInitializationExpression();
        // 初期化式がない場合は何もしないで抜ける
        if (null == expression) {
            return null;
        }

        if (expression instanceof ArrayElementUsageInfo) {

            return this.dissolveArrayElementUsage((ArrayElementUsageInfo) expression, nodeFactory);

        } else if (expression instanceof ArrayInitializerInfo) {

            return this.dissolveArrayInitializer((ArrayInitializerInfo) expression, nodeFactory);

        } else if (expression instanceof ArrayTypeReferenceInfo) {

            return null;

        } else if (expression instanceof BinominalOperationInfo) {

            return this.dissolveBinominalOperationInfo((BinominalOperationInfo) expression,
                    nodeFactory);

        } else if (expression instanceof CallInfo<?>) {

            return this.dissolveCall((CallInfo<?>) expression, nodeFactory);

        } else if (expression instanceof CastUsageInfo) {

            return this.dissolveCastUsage((CastUsageInfo) expression, nodeFactory);

        } else if (expression instanceof ClassReferenceInfo) {

            return null;

        } else if (expression instanceof EmptyExpressionInfo) {

            return null;

        } else if (expression instanceof ForeachConditionInfo) {

            // foreach文の条件式は代入文の右側に来ないので処理する必要がない
            return null;

        } else if (expression instanceof LiteralUsageInfo) {

            return null;

        } else if (expression instanceof MonominalOperationInfo) {

            // 単項演算子のオペランドは変数使用しかこないはずなので，処理する必要が合ない
            return null;

        } else if (expression instanceof NullUsageInfo) {

            return null;

        } else if (expression instanceof ParenthesesExpressionInfo) {

            return this.dissolveParenthesesExpression((ParenthesesExpressionInfo) expression,
                    nodeFactory);

        } else if (expression instanceof TernaryOperationInfo) {

            return this.dissolveTernaryOperation((TernaryOperationInfo) expression, nodeFactory);

        } else if (expression instanceof UnknownEntityUsageInfo) {

            return null;

        } else if (expression instanceof VariableUsageInfo<?>) {

            return null;

        } else {
            throw new IllegalStateException("unknown expression type.");
        }
    }

    /**
     * 右辺がArrayElementUsageである代入文を分解するためのメソッド
     * 
     * @param arrayElementUsage
     * @param nodeFactory
     * @return
     */
    private CFG dissolveArrayElementUsage(final ArrayElementUsageInfo arrayElementUsage,
            final ICFGNodeFactory nodeFactory) {

        final VariableDeclarationStatementInfo statement = this.getCore();
        final ExpressionInfo indexExpression = arrayElementUsage.getIndexExpression();
        final ExpressionInfo qualifiedExpression = arrayElementUsage.getQualifierExpression();

        final boolean indexExpressionIsDissolved = CFGUtility.isDissolved(indexExpression);
        final boolean qualifiedExpressionIsDissolved = CFGUtility.isDissolved(qualifiedExpression);

        // indexExpressionとqualifiedExpressionが分解されないときは何もせずに抜ける
        if (!indexExpressionIsDissolved && !qualifiedExpressionIsDissolved) {
            return null;
        }

        // 分解前の文から必要な情報を取得
        final LocalVariableUsageInfo variableDeclaration = statement.getDeclaration();
        final LocalSpaceInfo ownerSpace = statement.getOwnerSpace();
        final int fromLine = statement.getFromLine();
        final int toLine = statement.getToLine();
        final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
                : ownerSpace.getOuterCallableUnit();

        final List<CFGNode<?>> dissolvedNodeList = new ArrayList<CFGNode<?>>();
        final List<LocalVariableUsageInfo> dissolvedVariableUsageList = new ArrayList<LocalVariableUsageInfo>();

        // TODO 本来はindexEpxression -> qualifiedExpression の順番でなければならない
        if (qualifiedExpressionIsDissolved) {
            this.makeDissolvedNode(qualifiedExpression, nodeFactory, dissolvedNodeList,
                    dissolvedVariableUsageList);
        }

        if (indexExpressionIsDissolved) {
            this.makeDissolvedNode(indexExpression, nodeFactory, dissolvedNodeList,
                    dissolvedVariableUsageList);
        }

        // 古いノードを削除
        nodeFactory.removeNode(statement);
        this.remove();

        // ダミー変数を利用したArrayElementUsageInfo，およびそれを用いた代入文を作成
        int index = 0;
        final ArrayElementUsageInfo newArrayElementUsage = new ArrayElementUsageInfo(
                qualifiedExpressionIsDissolved ? dissolvedVariableUsageList.get(index++)
                        : qualifiedExpression,
                indexExpressionIsDissolved ? dissolvedVariableUsageList.get(index++)
                        : indexExpression, outerCallableUnit, fromLine, CFGUtility
                        .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        final VariableDeclarationStatementInfo newStatement = new VariableDeclarationStatementInfo(
                variableDeclaration, newArrayElementUsage, fromLine, CFGUtility
                        .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        final CFGNode<?> newNode = nodeFactory.makeNormalNode(newStatement);
        dissolvedNodeList.add(newNode);

        this.makeEdges(dissolvedNodeList);

        // ownerSpaceとの調整
        ownerSpace.removeStatement(statement);
        for (final CFGNode<? extends ExecutableElementInfo> node : dissolvedNodeList) {
            final ExecutableElementInfo core = node.getCore();
            ownerSpace.addStatement((StatementInfo) core);
        }
        ownerSpace.addStatement(newStatement);

        // 分解したノード群からCFGを構築
        final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

        return newCFG;
    }

    /**
    * 右辺がArrayInitializerInfoである代入文を分解するためのメソッド
    * 
    * @param arrayInitialier
    * @param nodeFactory
    * @return
    */
    private CFG dissolveArrayInitializer(final ArrayInitializerInfo arrayInitializer,
            final ICFGNodeFactory nodeFactory) {

        final VariableDeclarationStatementInfo statement = this.getCore();
        final List<ExpressionInfo> initializers = arrayInitializer.getElementInitializers();

        // 分解前の文から必要な情報を取得
        final LocalVariableUsageInfo variableDeclaration = statement.getDeclaration();
        final LocalSpaceInfo ownerSpace = statement.getOwnerSpace();
        final int fromLine = statement.getFromLine();
        final int toLine = statement.getToLine();
        final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
                : ownerSpace.getOuterCallableUnit();

        final List<CFGNode<?>> dissolvedNodeList = new ArrayList<CFGNode<?>>();
        final List<LocalVariableUsageInfo> dissolvedVariableUsageList = new ArrayList<LocalVariableUsageInfo>();

        //各イニシャライザを分解すべきかチェックし，分解し，分解した文から新規ノードを作成
        final List<ExpressionInfo> newInitializers = new ArrayList<ExpressionInfo>();
        for (final ExpressionInfo initializer : initializers) {

            if (CFGUtility.isDissolved(initializer)) {

                this.makeDissolvedNode(initializer, nodeFactory, dissolvedNodeList,
                        dissolvedVariableUsageList);

                newInitializers.add(dissolvedVariableUsageList.get(dissolvedVariableUsageList
                        .size() - 1));
            }

            else {
                newInitializers.add(initializer);
            }
        }

        // 分解されたイニシャライザがなければ何もせずに抜ける
        if (dissolvedNodeList.isEmpty()) {
            return null;
        }

        // 古いノードを削除
        nodeFactory.removeNode(statement);
        this.remove();

        final ArrayInitializerInfo newArrayInitializer = new ArrayInitializerInfo(newInitializers,
                outerCallableUnit, fromLine, CFGUtility.getRandomNaturalValue(), toLine, CFGUtility
                        .getRandomNaturalValue());
        final VariableDeclarationStatementInfo newStatement = new VariableDeclarationStatementInfo(
                variableDeclaration, newArrayInitializer, fromLine, CFGUtility
                        .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        final CFGNode<?> newNode = nodeFactory.makeNormalNode(newStatement);
        dissolvedNodeList.add(newNode);

        this.makeEdges(dissolvedNodeList);

        // ownerSpaceとの調整
        ownerSpace.removeStatement(statement);
        for (final CFGNode<? extends ExecutableElementInfo> node : dissolvedNodeList) {
            final ExecutableElementInfo core = node.getCore();
            ownerSpace.addStatement((StatementInfo) core);
        }
        ownerSpace.addStatement(newStatement);

        // 分解したノード群からCFGを構築
        final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

        return newCFG;
    }

    /**
     * 右辺が BinominalOperationである代入文を分解するためのメソッド
     * 
     * @param binominalOperation
     * @param nodeFactory
     * @return
     */
    private CFG dissolveBinominalOperationInfo(final BinominalOperationInfo binominalOperation,
            final ICFGNodeFactory nodeFactory) {

        final VariableDeclarationStatementInfo statement = this.getCore();
        final ExpressionInfo firstOperand = binominalOperation.getFirstOperand();
        final ExpressionInfo secondOperand = binominalOperation.getFirstOperand();

        final boolean firstOperandIsDissolved = CFGUtility.isDissolved(firstOperand);
        final boolean secondOperandIsDissolved = CFGUtility.isDissolved(secondOperand);

        // 分解の必要のない場合は抜ける
        if (!firstOperandIsDissolved && !secondOperandIsDissolved) {
            return null;
        }

        // 分解前の文から必要な情報を取得
        final LocalVariableUsageInfo variableDeclaration = statement.getDeclaration();
        final LocalSpaceInfo ownerSpace = statement.getOwnerSpace();
        final int fromLine = statement.getFromLine();
        final int toLine = statement.getToLine();
        final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
                : ownerSpace.getOuterCallableUnit();

        final List<CFGNode<?>> dissolvedNodeList = new ArrayList<CFGNode<?>>();
        final List<LocalVariableUsageInfo> dissolvedVariableUsageList = new ArrayList<LocalVariableUsageInfo>();

        // firstOperandが必要であれば分解
        if (firstOperandIsDissolved) {
            this.makeDissolvedNode(firstOperand, nodeFactory, dissolvedNodeList,
                    dissolvedVariableUsageList);
        }

        // secondOperandが必要であれば分解
        if (secondOperandIsDissolved) {
            this.makeDissolvedNode(secondOperand, nodeFactory, dissolvedNodeList,
                    dissolvedVariableUsageList);
        }

        // 古いノードを削除
        nodeFactory.removeNode(statement);
        this.remove();

        // 新しい二項演算オブジェクトおよびそれを右辺として持つ代入文を作成
        int index = 0;
        final BinominalOperationInfo newBinominalOperation = new BinominalOperationInfo(
                binominalOperation.getOperator(),
                firstOperandIsDissolved ? dissolvedVariableUsageList.get(index++) : firstOperand,
                secondOperandIsDissolved ? dissolvedVariableUsageList.get(index++) : secondOperand,
                outerCallableUnit, fromLine, CFGUtility.getRandomNaturalValue(), toLine, CFGUtility
                        .getRandomNaturalValue());
        final VariableDeclarationStatementInfo newStatement = new VariableDeclarationStatementInfo(
                variableDeclaration, newBinominalOperation, fromLine, CFGUtility
                        .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        final CFGNode<?> newNode = nodeFactory.makeNormalNode(newStatement);
        dissolvedNodeList.add(newNode);

        this.makeEdges(dissolvedNodeList);

        // ownerSpaceとの調整
        ownerSpace.removeStatement(statement);
        for (final CFGNode<? extends ExecutableElementInfo> node : dissolvedNodeList) {
            final ExecutableElementInfo core = node.getCore();
            ownerSpace.addStatement((StatementInfo) core);
        }
        ownerSpace.addStatement(newStatement);

        // 分解したノード群からCFGを構築
        final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

        return newCFG;
    }

    /**
     * 右辺が CallInfo<?>である代入文を分解するためのメソッド
     * 
     * @param call
     * @param nodeFactory
     * @return
     */
    private CFG dissolveCall(final CallInfo<? extends CallableUnitInfo> call,
            final ICFGNodeFactory nodeFactory) {

        final List<CFGNode<?>> dissolvedNodeList = new ArrayList<CFGNode<?>>();
        final List<LocalVariableUsageInfo> dissolvedVariableUsageList = new ArrayList<LocalVariableUsageInfo>();

        //　引数を分解
        final List<ExpressionInfo> newArguments = new ArrayList<ExpressionInfo>();
        for (final ExpressionInfo argument : call.getArguments()) {
            if (CFGUtility.isDissolved(argument)) {
                this.makeDissolvedNode(argument, nodeFactory, dissolvedNodeList,
                        dissolvedVariableUsageList);
                newArguments.add(dissolvedVariableUsageList
                        .get(dissolvedVariableUsageList.size() - 1));
            } else {
                newArguments.add(argument);
            }
        }

        // メソッド呼び出しであれば，qualifiedExpressionを分解
        final ExpressionInfo newQualifiedExpression;
        if (call instanceof MethodCallInfo) {

            final MethodCallInfo methodCall = (MethodCallInfo) call;
            final ExpressionInfo qualifiedExpression = methodCall.getQualifierExpression();
            if (CFGUtility.isDissolved(qualifiedExpression)) {
                this.makeDissolvedNode(qualifiedExpression, nodeFactory, dissolvedNodeList,
                        dissolvedVariableUsageList);
                newQualifiedExpression = dissolvedVariableUsageList.get(dissolvedVariableUsageList
                        .size() - 1);
            } else {
                newQualifiedExpression = qualifiedExpression;
            }
        } else {
            newQualifiedExpression = null;
        }

        // 配列コンストラクタであれば，indexExpressionを分解
        final SortedMap<Integer, ExpressionInfo> newIndexExpressions = new TreeMap<Integer, ExpressionInfo>();
        if (call instanceof ArrayConstructorCallInfo) {

            final ArrayConstructorCallInfo arrayConstructorCall = (ArrayConstructorCallInfo) call;
            for (final Entry<Integer, ExpressionInfo> entry : arrayConstructorCall
                    .getIndexExpressions().entrySet()) {

                final Integer dimension = entry.getKey();
                final ExpressionInfo indexExpression = entry.getValue();

                if (CFGUtility.isDissolved(indexExpression)) {
                    this.makeDissolvedNode(indexExpression, nodeFactory, dissolvedNodeList,
                            dissolvedVariableUsageList);
                    newIndexExpressions.put(dimension, dissolvedVariableUsageList
                            .get(dissolvedVariableUsageList.size() - 1));
                } else {
                    newIndexExpressions.put(dimension, indexExpression);
                }
            }
        }

        // 分解が行われなかった場合は何もせずに抜ける
        if (dissolvedNodeList.isEmpty()) {
            return null;
        }

        // 分解前の文から必要な情報を取得
        final VariableDeclarationStatementInfo statement = this.getCore();
        final LocalVariableUsageInfo variableDeclaration = statement.getDeclaration();
        final LocalSpaceInfo ownerSpace = statement.getOwnerSpace();
        final int fromLine = statement.getFromLine();
        final int toLine = statement.getToLine();
        final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
                : ownerSpace.getOuterCallableUnit();

        // 古いノードを削除
        nodeFactory.removeNode(statement);
        this.remove();

        final CallInfo<? extends CallableUnitInfo> newCall;
        if (call instanceof MethodCallInfo) {
            final MethodCallInfo methodCall = (MethodCallInfo) call;
            newCall = new MethodCallInfo(newQualifiedExpression.getType(), newQualifiedExpression,
                    methodCall.getCallee(), outerCallableUnit, fromLine, CFGUtility
                            .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        } else if (call instanceof ClassConstructorCallInfo) {
            final ClassConstructorCallInfo classConstructorCall = (ClassConstructorCallInfo) call;
            newCall = new ClassConstructorCallInfo(classConstructorCall.getType(),
                    classConstructorCall.getCallee(), outerCallableUnit, fromLine, CFGUtility
                            .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        } else if (call instanceof ArrayConstructorCallInfo) {
            final ArrayConstructorCallInfo arrayConstructorCall = (ArrayConstructorCallInfo) call;
            newCall = new ArrayConstructorCallInfo(arrayConstructorCall.getType(),
                    outerCallableUnit, fromLine, CFGUtility.getRandomNaturalValue(), toLine,
                    CFGUtility.getRandomNaturalValue());

            for (final Entry<Integer, ExpressionInfo> entry : newIndexExpressions.entrySet()) {
                final Integer dimension = entry.getKey();
                final ExpressionInfo newIndexExpression = entry.getValue();
                ((ArrayConstructorCallInfo) newCall).addIndexExpression(dimension,
                        newIndexExpression);
            }

        } else {
            throw new IllegalStateException();
        }

        // 引数を追加
        for (final ExpressionInfo newArgument : newArguments) {
            newCall.addArgument(newArgument);
        }

        final VariableDeclarationStatementInfo newStatement = new VariableDeclarationStatementInfo(
                variableDeclaration, newCall, fromLine, CFGUtility.getRandomNaturalValue(), toLine,
                CFGUtility.getRandomNaturalValue());
        final CFGNode<?> newNode = nodeFactory.makeNormalNode(newStatement);
        dissolvedNodeList.add(newNode);

        this.makeEdges(dissolvedNodeList);

        // ownerSpaceとの調整
        ownerSpace.removeStatement(statement);
        for (final CFGNode<? extends ExecutableElementInfo> node : dissolvedNodeList) {
            final ExecutableElementInfo core = node.getCore();
            ownerSpace.addStatement((StatementInfo) core);
        }
        ownerSpace.addStatement(newStatement);

        // 分解したノード群からCFGを構築
        final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

        return newCFG;
    }

    /**
     * 右辺が CastUsageInfoである代入文を分解するためのメソッド
     * 
     * @param castUsage
     * @param nodeFactory
     * @return
     */
    private CFG dissolveCastUsage(final CastUsageInfo castUsage, final ICFGNodeFactory nodeFactory) {

        final List<CFGNode<?>> dissolvedNodeList = new ArrayList<CFGNode<?>>();
        final List<LocalVariableUsageInfo> dissolvedVariableUsageList = new ArrayList<LocalVariableUsageInfo>();

        final ExpressionInfo castedUsage = castUsage.getCastedUsage();
        if (CFGUtility.isDissolved(castedUsage)) {
            this.makeDissolvedNode(castedUsage, nodeFactory, dissolvedNodeList,
                    dissolvedVariableUsageList);
        }

        if (dissolvedNodeList.isEmpty()) {
            return null;
        }

        // 分解前の文から必要な情報を取得
        final VariableDeclarationStatementInfo statement = this.getCore();
        final LocalVariableUsageInfo variableDeclaration = statement.getDeclaration();
        final LocalSpaceInfo ownerSpace = statement.getOwnerSpace();
        final int fromLine = statement.getFromLine();
        final int toLine = statement.getToLine();
        final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
                : ownerSpace.getOuterCallableUnit();

        // 古いノードを削除
        nodeFactory.removeNode(statement);
        this.remove();

        // 新しい二項演算オブジェクトおよびそれを右辺として持つ代入文を作成
        final CastUsageInfo newCastUsage = new CastUsageInfo(castUsage.getType(),
                dissolvedVariableUsageList.get(0), outerCallableUnit, fromLine, CFGUtility
                        .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        final VariableDeclarationStatementInfo newStatement = new VariableDeclarationStatementInfo(
                variableDeclaration, newCastUsage, fromLine, CFGUtility.getRandomNaturalValue(),
                toLine, CFGUtility.getRandomNaturalValue());
        final CFGNode<?> newNode = nodeFactory.makeNormalNode(newStatement);
        dissolvedNodeList.add(newNode);

        this.makeEdges(dissolvedNodeList);

        // ownerSpaceとの調整
        ownerSpace.removeStatement(statement);
        for (final CFGNode<? extends ExecutableElementInfo> node : dissolvedNodeList) {
            final ExecutableElementInfo core = node.getCore();
            ownerSpace.addStatement((StatementInfo) core);
        }
        ownerSpace.addStatement(newStatement);

        // 分解したノード群からCFGを構築
        final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

        return newCFG;
    }

    /**
     * 右辺がParenthesesExpressionInfoである代入文を分解するためのメソッド
     * 
     * @param parenthesExpression
     * @param nodeFactory
     * @return
     */
    private CFG dissolveParenthesesExpression(
            final ParenthesesExpressionInfo parenthesesExpression, final ICFGNodeFactory nodeFactory) {

        final List<CFGNode<?>> dissolvedNodeList = new ArrayList<CFGNode<?>>();
        final List<LocalVariableUsageInfo> dissolvedVariableUsageList = new ArrayList<LocalVariableUsageInfo>();

        final ExpressionInfo innerExpression = parenthesesExpression.getParnentheticExpression();
        if (CFGUtility.isDissolved(innerExpression)) {
            this.makeDissolvedNode(innerExpression, nodeFactory, dissolvedNodeList,
                    dissolvedVariableUsageList);
        }

        if (dissolvedNodeList.isEmpty()) {
            return null;
        }

        // 分解前の文から必要な情報を取得
        final VariableDeclarationStatementInfo statement = this.getCore();
        final LocalVariableUsageInfo variableDeclaration = statement.getDeclaration();
        final LocalSpaceInfo ownerSpace = statement.getOwnerSpace();
        final int fromLine = statement.getFromLine();
        final int toLine = statement.getToLine();
        final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
                : ownerSpace.getOuterCallableUnit();

        // 古いノードを削除
        nodeFactory.removeNode(statement);
        this.remove();

        // 新しい二項演算オブジェクトおよびそれを右辺として持つ代入文を作成
        final ParenthesesExpressionInfo newInnerExpression = new ParenthesesExpressionInfo(
                dissolvedVariableUsageList.get(0), outerCallableUnit, fromLine, CFGUtility
                        .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        final VariableDeclarationStatementInfo newStatement = new VariableDeclarationStatementInfo(
                variableDeclaration, newInnerExpression, fromLine, CFGUtility
                        .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        final CFGNode<?> newNode = nodeFactory.makeNormalNode(newStatement);
        dissolvedNodeList.add(newNode);

        this.makeEdges(dissolvedNodeList);

        // ownerSpaceとの調整
        ownerSpace.removeStatement(statement);
        for (final CFGNode<? extends ExecutableElementInfo> node : dissolvedNodeList) {
            final ExecutableElementInfo core = node.getCore();
            ownerSpace.addStatement((StatementInfo) core);
        }
        ownerSpace.addStatement(newStatement);

        // 分解したノード群からCFGを構築
        final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

        return newCFG;
    }

    /**
     * 右辺がTernaryOperationInfoである代入文を分解するためのメソッド
     * 
     * @param ternaryOperation
     * @param nodeFactory
     * @return
     */
    private CFG dissolveTernaryOperation(final TernaryOperationInfo ternaryOperation,
            final ICFGNodeFactory nodeFactory) {

        final VariableDeclarationStatementInfo statement = this.getCore();
        final LocalSpaceInfo ownerSpace = statement.getOwnerSpace();
        final LocalVariableUsageInfo variableDeclaration = statement.getDeclaration();
        final int fromLine = statement.getFromLine();
        final int toLine = statement.getToLine();

        final ConditionInfo condition = ternaryOperation.getCondition();
        final ExpressionInfo trueExpression = ternaryOperation.getTrueExpression();
        final ExpressionInfo falseExpression = ternaryOperation.getFalseExpression();

        // trueExpressionを再構築
        final VariableDeclarationStatementInfo trueStatement = new VariableDeclarationStatementInfo(
                variableDeclaration, trueExpression, fromLine, CFGUtility.getRandomNaturalValue(),
                toLine, CFGUtility.getRandomNaturalValue());

        // falseExpressionを再構築
        final VariableDeclarationStatementInfo falseStatement = new VariableDeclarationStatementInfo(
                variableDeclaration, falseExpression, fromLine, CFGUtility.getRandomNaturalValue(),
                toLine, CFGUtility.getRandomNaturalValue());

        // conditionを再構築
        final IfBlockInfo newIfBlock = new IfBlockInfo(fromLine,
                CFGUtility.getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        newIfBlock.setOuterUnit(statement.getOwnerSpace());
        final ConditionalClauseInfo newConditionalClause = new ConditionalClauseInfo(newIfBlock,
                condition, fromLine, CFGUtility.getRandomNaturalValue(), toLine, CFGUtility
                        .getRandomNaturalValue());
        newIfBlock.setConditionalClause(newConditionalClause);
        final ElseBlockInfo newElseBlock = new ElseBlockInfo(fromLine, CFGUtility
                .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue(), newIfBlock);
        newElseBlock.setOuterUnit(statement.getOwnerSpace());
        newIfBlock.setSequentElseBlock(newElseBlock);
        newIfBlock.addStatement(trueStatement);
        newElseBlock.addStatement(falseStatement);

        // 古いノードを削除
        nodeFactory.removeNode(statement);
        this.remove();

        // ノードを作成し，つなぐ
        final CFGControlNode conditionNode = nodeFactory.makeControlNode(condition);
        final CFGNode<?> trueNode = nodeFactory.makeNormalNode(trueStatement);
        final CFGNode<?> falseNode = nodeFactory.makeNormalNode(falseStatement);
        final CFGControlEdge trueEdge = new CFGControlEdge(conditionNode, trueNode, true);
        final CFGControlEdge falseEdge = new CFGControlEdge(conditionNode, falseNode, false);
        conditionNode.addForwardEdge(trueEdge);
        conditionNode.addForwardEdge(falseEdge);
        trueNode.addBackwardEdge(trueEdge);
        falseNode.addBackwardEdge(falseEdge);

        for (final CFGEdge backwardEdge : this.getBackwardEdges()) {
            final CFGNode<?> backwardNode = backwardEdge.getFromNode();
            final CFGEdge newBackwardEdge = backwardEdge.replaceToNode(conditionNode);
            backwardNode.addForwardEdge(newBackwardEdge);
            conditionNode.addBackwardEdge(newBackwardEdge);
        }
        for (final CFGEdge forwardEdge : this.getForwardEdges()) {
            final CFGNode<?> forwardNode = forwardEdge.getToNode();
            final CFGEdge newTrueForwardEdge = forwardEdge.replaceFromNode(trueNode);
            final CFGEdge newFalseForwardEdge = forwardEdge.replaceFromNode(falseNode);
            forwardNode.addBackwardEdge(newTrueForwardEdge);
            forwardNode.addBackwardEdge(newFalseForwardEdge);
            trueNode.addForwardEdge(newTrueForwardEdge);
            falseNode.addForwardEdge(newFalseForwardEdge);
        }

        conditionNode.dissolve(nodeFactory);
        trueNode.dissolve(nodeFactory);
        falseNode.dissolve(nodeFactory);

        // 分解したノード群からCFGを構築
        final SimpleCFG newCFG = new SimpleCFG(nodeFactory);
        newCFG.addNode(conditionNode);
        newCFG.setEnterNode(conditionNode);
        newCFG.addNode(trueNode);
        newCFG.addExitNode(trueNode);
        newCFG.addNode(falseNode);
        newCFG.addExitNode(falseNode);

        final CFG conditionCFG = conditionNode.dissolve(nodeFactory);
        final CFG trueCFG = trueNode.dissolve(nodeFactory);
        final CFG falseCFG = falseNode.dissolve(nodeFactory);

        // ownerSpaceとの調整
        ownerSpace.removeStatement(statement);
        ownerSpace.addStatement(newIfBlock);
        ownerSpace.addStatement(newElseBlock);

        if (null != conditionCFG) {
            newCFG.removeNode(conditionNode);
            newCFG.addNodes(conditionCFG.getAllNodes());
            newCFG.setEnterNode(conditionCFG.getEnterNode());
        }

        if (null != trueCFG) {
            newCFG.removeNode(trueNode);
            newCFG.addNodes(trueCFG.getAllNodes());
            newCFG.addExitNodes(trueCFG.getExitNodes());
        }

        if (null != falseCFG) {
            newCFG.removeNode(falseNode);
            newCFG.addNodes(falseCFG.getAllNodes());
            newCFG.addExitNodes(falseCFG.getExitNodes());
        }

        return newCFG;
    }

    /**
     * 引数で与えられたoriginalExpressionが右辺となる代入文を作成する．
     * 作成した代入文のCFGノードはdissolvedNodeListの最後に追加され，
     * 代入文の左辺の変数使用オブジェクトはdissolvedVariableUsageListの最後に追加される．
     * 
     * @param originalExpression
     * @param nodeFactory
     * @param dissolvedNodeList
     * @param dissolvedVariableUsageList
     */
    private void makeDissolvedNode(final ExpressionInfo originalExpression,
            final ICFGNodeFactory nodeFactory, final List<CFGNode<?>> dissolvedNodeList,
            final List<LocalVariableUsageInfo> dissolvedVariableUsageList) {

        final LocalSpaceInfo ownerSpace = originalExpression.getOwnerSpace();
        assert null != ownerSpace : "ownerSpace is null!";
        final CallableUnitInfo outerCallableUnit = originalExpression.getOwnerMethod();
        assert null != outerCallableUnit : "outerCallableUnit is null!";
        final int fromLine = originalExpression.getFromLine();
        final int toLine = originalExpression.getToLine();
        final TypeInfo type = originalExpression.getType();

        final LocalVariableInfo dummyVariable = new LocalVariableInfo(Collections
                .<ModifierInfo> emptySet(), getDummyVariableName(), type, ownerSpace, fromLine,
                CFGUtility.getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        final VariableDeclarationStatementInfo dummyVariableDeclarationStatement = new VariableDeclarationStatementInfo(
                LocalVariableUsageInfo.getInstance(dummyVariable, false, true, outerCallableUnit,
                        fromLine, CFGUtility.getRandomNaturalValue(), toLine, CFGUtility
                                .getRandomNaturalValue()), originalExpression, fromLine, CFGUtility
                        .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        final LocalVariableUsageInfo dummyVariableUsage = LocalVariableUsageInfo.getInstance(
                dummyVariable, true, false, outerCallableUnit, fromLine, CFGUtility
                        .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());

        final CFGNode<?> newNode = nodeFactory.makeNormalNode(dummyVariableDeclarationStatement);
        dissolvedNodeList.add(newNode);
        dissolvedVariableUsageList.add(dummyVariableUsage);
    }

    /**
     * 分解したノードつなぎ，元の場所に入れる
     * 
     * @param dissolvedNodeList
     */
    private void makeEdges(final List<CFGNode<?>> dissolvedNodeList) {

        // 分解したノードをつなぐ
        for (int i = 1; i < dissolvedNodeList.size(); i++) {
            final CFGNode<?> fromNode = dissolvedNodeList.get(i - 1);
            final CFGNode<?> toNode = dissolvedNodeList.get(i);
            final CFGEdge edge = new CFGNormalEdge(fromNode, toNode);
            fromNode.addForwardEdge(edge);
            toNode.addBackwardEdge(edge);
        }

        // 元の場所に入れる
        {
            final CFGNode<?> firstNode = dissolvedNodeList.get(0);
            final CFGNode<?> lastNode = dissolvedNodeList.get(dissolvedNodeList.size() - 1);
            for (final CFGEdge backwardEdge : this.getBackwardEdges()) {
                final CFGNode<?> backwardNode = backwardEdge.getFromNode();
                final CFGEdge newBackwardEdge = backwardEdge.replaceToNode(firstNode);
                backwardNode.addForwardEdge(newBackwardEdge);
                firstNode.addBackwardEdge(newBackwardEdge);
            }
            for (final CFGEdge forwardEdge : this.getForwardEdges()) {
                final CFGNode<?> forwardNode = forwardEdge.getToNode();
                final CFGEdge newForwardEdge = forwardEdge.replaceFromNode(lastNode);
                forwardNode.addBackwardEdge(newForwardEdge);
                lastNode.addForwardEdge(newForwardEdge);
            }
        }
    }

    /**
     * 引数で与えられたノード群からCFGを構築して返す．
     * 
     * @param nodeFactory
     * @param dissolvedNodeList
     * @return
     */
    private CFG makeCFG(final ICFGNodeFactory nodeFactory, List<CFGNode<?>> dissolvedNodeList) {

        final SimpleCFG cfg = new SimpleCFG(nodeFactory);

        // enterNodeを設定
        {
            final CFGNode<?> firstNode = dissolvedNodeList.get(0);
            final CFG dissolvedCFG = firstNode.dissolve(nodeFactory);
            if (null != dissolvedCFG) {
                cfg.setEnterNode(dissolvedCFG.getEnterNode());
                cfg.addNodes(dissolvedCFG.getAllNodes());
            } else {
                cfg.setEnterNode(firstNode);
            }
        }

        // exitNodeを設定
        {
            final CFGNode<?> lastNode = dissolvedNodeList.get(dissolvedNodeList.size() - 1);
            final CFG dissolvedCFG = lastNode.dissolve(nodeFactory);
            if (null != dissolvedCFG) {
                cfg.addExitNodes(dissolvedCFG.getExitNodes());
                cfg.addNodes(dissolvedCFG.getAllNodes());
            } else {
                cfg.addExitNode(lastNode);
            }
        }

        // nodesを設定
        for (int index = 1; index < dissolvedNodeList.size() - 1; index++) {
            final CFGNode<?> node = dissolvedNodeList.get(index);
            final CFG dissolvedCFG = node.dissolve(nodeFactory);
            if (null != dissolvedCFG) {
                cfg.addNodes(dissolvedCFG.getAllNodes());
            } else {
                cfg.addNode(node);
            }
        }

        return cfg;
    }
}
