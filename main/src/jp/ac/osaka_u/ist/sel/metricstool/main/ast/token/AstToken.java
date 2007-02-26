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
     * トークンがvoid型を表すかどうかを返す.
     * @return void型を表すトークンならtrue
     */
    public boolean isVoidType();
}
