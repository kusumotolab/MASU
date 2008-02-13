package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.OperatorToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.OPERATOR;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedArrayElementUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedArrayTypeReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedBinominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedCastUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMonominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedNullUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


public class OperatorExpressionBuilder extends ExpressionBuilder {

    public OperatorExpressionBuilder(final ExpressionElementManager expressionManager,
            final BuildDataManager buildManager) {
        super(expressionManager);
        this.buildDataManager = buildManager;
    }

    @Override
    protected void afterExited(final AstVisitEvent event) {
        final AstToken token = event.getToken();
        if (isTriggerToken(token)) {
            this.buildOperatorElement((OperatorToken) token);
        }
    }

    protected void buildOperatorElement(final OperatorToken token) {
        //演算子が必要とする項の数
        final int term = token.getTermCount();
        //左辺値への代入があるかどうか
        final boolean assignmentLeft = token.isAssignmentOperator();
        //左辺値への参照があるかどうか
        final boolean referenceLeft = token.isLeftTermIsReferencee();
        //型決定に関わる項のインデックスの配列
        final int[] typeSpecifiedTermIndexes = token.getTypeSpecifiedTermIndexes();

        final ExpressionElement[] elements = this.getAvailableElements();

        assert (term > 0 && term == elements.length) : "Illegal state: unexpected element count.";
        
        if (term > 0 && term == elements.length){
            //各項の型を記録する配列
            final UnresolvedEntityUsageInfo[] termTypes = new UnresolvedEntityUsageInfo[elements.length];
    
            //最左辺値について
            if (elements[0] instanceof IdentifierElement) {
                //識別子の場合
                final IdentifierElement leftElement = (IdentifierElement) elements[0];
                if (referenceLeft) {
                    //参照なら被参照変数として解決して結果の型を取得する
                    termTypes[0] = leftElement.resolveAsReferencedVariable(this.buildDataManager);
                }
    
                if (assignmentLeft) {
                    //代入なら被代入変数として解決して結果の型を取得する
                    termTypes[0] = leftElement.resolveAsAssignmetedVariable(this.buildDataManager);
                }
            } else if (elements[0].equals(InstanceSpecificElement.THIS)){
                termTypes[0] = InstanceSpecificElement.getThisInstanceType(buildDataManager);
            } else if (elements[0].equals(InstanceSpecificElement.NULL)){
                termTypes[0] = new UnresolvedNullUsageInfo();
            } else if (elements[0] instanceof TypeElement) {
                TypeElement typeElement = (TypeElement) elements[0];
                if (typeElement.getType() instanceof UnresolvedClassTypeInfo) {
                    // キャストがあるとおそらくここに到達
                    // TODO UnresolvedReferenceTypeInfoにすべき
                    termTypes[0] = ((UnresolvedClassTypeInfo) typeElement.getType()).getUsage();
                } else if(typeElement.getType() instanceof UnresolvedArrayTypeInfo) {
                    UnresolvedArrayTypeInfo arrayType = (UnresolvedArrayTypeInfo) typeElement.getType();
                    termTypes[0] = new UnresolvedArrayTypeReferenceInfo(arrayType);
                } else {
                
                    termTypes[0] = elements[0].getUsage();
                }
            }  else {
                //それ以外の場合は直接型を取得する
                termTypes[0] = elements[0].getUsage();
            }
    
            //2項目以降について
            for (int i = 1; i < term; i++) {
                if (elements[i] instanceof IdentifierElement) {
                    //識別子なら勝手に参照として解決して方を取得する
                    termTypes[i] = ((IdentifierElement) elements[i])
                            .resolveAsReferencedVariable(this.buildDataManager);
                } else if (elements[i].equals(InstanceSpecificElement.THIS)){
                    termTypes[i] = InstanceSpecificElement.getThisInstanceType(buildDataManager);
                } else if (elements[i].equals(InstanceSpecificElement.NULL)){
                    termTypes[i] = new UnresolvedNullUsageInfo();
                } else if (elements[i] instanceof TypeElement) {
                    TypeElement typeElement = (TypeElement) elements[i];
                    if (typeElement.getType() instanceof UnresolvedClassTypeInfo) {
                        termTypes[i] = ((UnresolvedClassTypeInfo) typeElement.getType()).getUsage();
                    } else if(typeElement.getType() instanceof UnresolvedArrayTypeInfo) {
                        // ここに到達するのはinstanceof type[]とき
                        // TODO instanceofの使用を示すEntityUsageを生成したほうがいいかも
                        UnresolvedArrayTypeInfo arrayType = (UnresolvedArrayTypeInfo) typeElement.getType();
                        termTypes[i] = new UnresolvedArrayTypeReferenceInfo(arrayType);
                    } else {
                        termTypes[i] = elements[i].getUsage();
                    }
                } else {
                    //それ以外なら直接型を取得する
                    termTypes[i] = elements[i].getUsage();
                }
            }
            
            final OPERATOR operator = token.getOperator();
            
            if (2 == term && null != operator){
                //オペレーターインスタンスがセットされている2項演算子＝名前解決部に型決定処理を委譲する
                assert(null != termTypes[0]) : "Illega state: first term type was not decided.";
                assert(null != termTypes[1]) : "Illega state: second term type was not decided.";
                
                UnresolvedBinominalOperationInfo operation = new UnresolvedBinominalOperationInfo(operator,termTypes[0],termTypes[1]);
                pushElement(UsageElement.getInstance(operation));
                
            } else{
                //自分で型決定する
                UnresolvedEntityUsageInfo resultType = null;
                
                //オペレータによってすでに決定している戻り値の型，確定していなければnull
                final PrimitiveTypeInfo type = token.getSpecifiedResultType();
                
                if (null != type) {
                    //オペレータによってすでに結果の型が決定している
                    resultType = new UnresolvedMonominalOperationInfo(termTypes[0], type);
                } else if (token.equals(OperatorToken.ARRAY)) {
                    //配列記述子の場合は特別処理
                    UnresolvedEntityUsageInfo ownerType;
                    if (elements[0] instanceof IdentifierElement) {
                        ownerType = ((IdentifierElement) elements[0])
                                .resolveAsReferencedVariable(this.buildDataManager);
                    } else {
                        ownerType = elements[0].getUsage();
                    }
                    resultType = new UnresolvedArrayElementUsageInfo(ownerType);
                } else if(token.equals(OperatorToken.CAST) && elements[0] instanceof TypeElement) {
                    UnresolvedTypeInfo castedType = ((TypeElement) elements[0]).getType();
                    resultType = new UnresolvedCastUsageInfo(castedType);
                } else {
                    //型決定に関連する項を左から順番に漁っていって最初に決定できた奴に勝手に決める
                    for (int i = 0; i < typeSpecifiedTermIndexes.length; i++) {
                        resultType = termTypes[typeSpecifiedTermIndexes[i]];
                        if (null != resultType){
                            break;
                        }
                    }
                }

                assert (null != resultType) : "Illegal state: operation resultType was not decided.";
                
                this.pushElement(UsageElement.getInstance(resultType));
            }
        }
    }

    @Override
    protected boolean isTriggerToken(final AstToken token) {
        return token.isOperator();
    }

    private final BuildDataManager buildDataManager;
 
}
