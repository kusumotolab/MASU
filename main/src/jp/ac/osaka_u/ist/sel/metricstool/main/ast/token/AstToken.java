package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


/**
 * ASTノードの種類を表現するクラス.
 * MASUのAST解析部で汎用的に利用される.
 * 
 * @author kou-tngt
 *
 */
public interface AstToken {

    /**
     * トークンがアクセス修飾子を表すかどうかを返す.
     * @return アクセス修飾子を表すトークンならtrue
     */
    public boolean isAccessModifier();

    /**
     * トークンが代入演算子を表すかどうかを返す.
     * @return 代入演算子を表すトークンならtrue
     */
    public boolean isAssignmentOperator();

    /**
     * 配列記述子（[]）を表すかどうかを返す.
     * @return 配列記述子を表すトークンならtrue
     */
    public boolean isArrayDeclarator();

    /**
     * トークンがブロックを表すかどうかを返す.
     * @return ブロックを表すトークンならtrue
     */
    public boolean isBlock();
    
    /**
     * トークンがブロックの宣言を表すかどうかを返す．
     * @return ブロックの宣言を表すトークンならtrue
     */
    public boolean isBlockDefinition();
    
    /**
     * トークンが特殊なブロックを表すかどうか返す．
     * @return 特殊なブロックを表すトークンならtrue
     */
    public boolean isBlockName();
    
    /**
     * トークンが組み込み型であるかどうかを返す．
     * @return 組み込み型ならtrue
     */
    public boolean isBuiltinType();

    /**
     * トークンがクラス定義部を表すかどうかを返す.
     * @return クラス定義部を表すトークンならtrue
     */
    public boolean isClassDefinition();

    /**
     * トークンがクラスブロックを表すかどうかを返す.
     * クラスブロックは通常のブロックとは区別されなければならない.
     * @return クラスブロックを表すトークンならtrue
     */
    public boolean isClassBlock();
    
    /**
     * トークンが定数を表すかどうかを返す．
     * @return 定数を表すトークンならtrue
     */
    public boolean isConstant();
    
    /**
     * トークンがコンストラクタ定義部を表すかどうかを返す.
     * @return コンストラクタ定義部を表すトークンならtrue
     */
    public boolean isConstructorDefinition();

    /**
     * トークンが継承記述部を表すかどうかを返す.
     * @return 継承記述部を表すトークンならtrue
     */
    public boolean isInheritanceDescription();

    /**
     * トークンが式を表すかどうかを返す.
     * @return 式を表すトークンならtrue
     */
    public boolean isExpression();
    
    /**
     * トークンが式文を表すかどうかを返す．
     * @return 式文を表すトークンならtrue
     */
    public boolean isExpressionStatement();
    
    /**
     * トークンが文のリストを表すかどうか返す
     * @return 文のリストを表すトークンならtrue
     */
    public boolean isSList();
    
    /**
     * トークンがフィールド定義部を表すかどうかを返す.
     * @return フィールド定義部を表すトークンならtrue
     */
    public boolean isFieldDefinition();

    /**
     * トークンが識別子を表すかどうかを返す.
     * @return フィールド識別子を表すトークンならtrue
     */
    public boolean isIdentifier();

    /**
     * トークンがnew文を表すかどうかを返す.
     * @return new文を表すトークンならtrue
     */
    public boolean isInstantiation();

    /**
     * トークンがfor文やcatch節などの冒頭で定義される変数の定義部を表すかどうかを返す.
     * @return for文やcatch節などの冒頭で定義される変数の定義部であればtrue
     */
    public boolean isLocalParameterDefinition();

    /**
     * トークンがローカル変数定義部を表すかどうかを返す.
     * @return ローカル変数定義部であればtrue
     */
    public boolean isLocalVariableDefinition();

    /**
     * トークンがメソッド定義部を表すかどうかを返す.
     * @return メソッド定義部であればtrue
     */
    public boolean isMethodDefinition();

    /**
     * トークンが static initializer の定義部を表すかどうかを返す．
     * @return static initializer の定義部であればtrue
     */
    public boolean isStaticInitializerDefinition();
    
    /**
     * トークンがメソッド呼び出しを表すかどうかを返す.
     * @return メソッド呼び出しを表すトークンならtrue
     */
    public boolean isMethodCall();

    /**
     * トークンがメソッドパラメータ定義を表すかどうかを返す.
     * @return メソッドパラメータの定義を表すトークンならtrue
     */
    public boolean isMethodParameterDefinition();

    /**
     * トークンが修飾子を表すかどうかを返す.
     * @return 修飾子を表すトークンならtrue
     */
    public boolean isModifier();

    /**
     * トークンが修飾子定義部を表すかどうかを返す.
     * @return 修飾子定義部を表すトークンならtrue
     */
    public boolean isModifiersDefinition();

    /**
     * トークンが何らかの定義部内の名前記述部を表すかどうかを返す.
     * @return 名前記述部を表すトークンならtrue
     */
    public boolean isNameDescription();

    /**
     * トークンが名前空間定義部を表すかどうかを返す.
     * @return 名前空間定義部を表すトークンならtrue
     */
    public boolean isNameSpaceDefinition();

    /**
     * トークンが名前空間の使用宣言部を表すかどうかを返す.
     * @return 名前空間の使用宣言部を表すトークンならtrue
     */
    public boolean isNameUsingDefinition();

    /**
     * トークンが識別子の区切りに使われるものかどうかを返す.
     * @return 識別子の区切りに使われるトークンならtrue
     */
    public boolean isNameSeparator();

    /**
     * トークンが演算子を表すかどうかを返す.
     * @return 演算子を表すトークンならtrue
     */
    public boolean isOperator();

    /**
     * トークンが基本型を表すかどうかを返す.
     * @return 基本型を表すトークンならtrue
     */
    public boolean isPrimitiveType();

    /**
     * トークンが型引数記述部を表すかどうかを返す．
     * @return 型引数記述部を表すならtrue
     */
    public boolean isTypeArgument();
    
    /**
     * トークンが型引数記述部の列を表すかどうかを返す．
     * @return 型引数記述部の列を表すならtrue
     */
    public boolean isTypeArguments();
    
    /**
     * トークンが型記述部を表すかどうかを返す.
     * @return 型記述部を表すトークンならtrue
     */
    public boolean isTypeDescription();
    
    /**
     * トークンが型上限の制約記述部であるかどうかを返す．
     * @return 型上限の制約記述部であればtrue
     */
    public boolean isTypeUpperBoundsDescription();
    
    /**
     * トークンが型パラメータを表すかどうかを返す．
     * @return 型パラメータを表すトークンならtrue
     */
    public boolean isTypeParameterDefinition();

    /**
     * トークンが型下限の制約記述部であるかどうかを返す．
     * @return 型下限の制約記述部であればtrue
     */
    public boolean isTypeLowerBoundsDescription();
    
    /**
     * トークンがワイルドカード型引数を表すかどうかを返す．
     * @return ワイルドカード型引数を表すならtrue
     */
    public boolean isTypeWildcard();
    
    /**
     * トークンがvoid型を表すかどうかを返す.
     * @return void型を表すトークンならtrue
     */
    public boolean isVoidType();
    
    /**
     * トークンがifを表すかどうかを返す
     * @return ifを表すトークンならtrue
     */
    public boolean isIf();
    
    /**
     * トークンがelseを表すかどうかを返す
     * @return elseを表すトークンならtrue
     */
    public boolean isElse();
    
    /**
     * トークンがwhileを表すかどうかを返す
     * @return whileを表すトークンならtrue
     */
    public boolean isWhile();
    
    /**
     * トークンがdoを表すかどうかを返す
     * @return doを表すトークンならtrue
     */
    public boolean isDo();
    
    /**
     * トークンがforを表すかどうかを返す
     * @return forを表すトークンならtrue
     */
    public boolean isFor();
    
    /**
     * トークンがtryを表すかどうかを返す
     * @return tryを表すトークンならtrue
     */
    public boolean isTry();
    
    /**
     * トークンがcatchを表すかどうかを返す
     * @return catchを表すトークンならtrue
     */
    public boolean isCatch();
    
    /**
     * トークンがfinallyを表すかどうかを返す
     * @return finallyを表すトークンならtrue
     */
    public boolean isFinally();
    
    /**
     * トークンがsynchronizedを表すかどうかを返す
     * @return synchronizedを表すトークンならtrue
     */
    public boolean isSynchronized();
    
    /**
     * トークンがswitchを表すかどうかを返す
     * @return switchを表すトークンならtrue
     */
    public boolean isSwitch();
    
    /**
     * このトークンがcaseグループ(caseやdefaultエントリ)の定義を表すかどうか返す
     * @return caseグループの定義を表すならtrue
     */
    public boolean isCaseGroupDefinition();
    
    /**
     * トークンがSwitch文のエントリの宣言を表すかどうか返す．
     * @return Switch文のエントリの宣言をならtrue
     */
    public boolean isEntryDefinition();
    
    /**
     * トークンがcaseを表すかどうかを返す
     * @return caseを表すトークンならtrue
     */
    public boolean isCase();
    
    /**
     * トークンがdefaultを表すかどうかを返す
     * @return defaultを表すトークンならtrue
     */
    public boolean isDefault();
    
    /**
     * トークンがbreak文をあらわすかどうか返す
     * @return break文を表すトークンならtrue
     */
    public boolean isBreak();

    /**
     * トークンがreturn文を表すかどうか返す
     * @return return文を表すトークンならtrue
     */
    public boolean isReturn();
    
    /**
     * トークンがthrow文を表すかどうか返す
     * @return throw文を表すトークンならtrue
     */
    public boolean isThrow();
}
