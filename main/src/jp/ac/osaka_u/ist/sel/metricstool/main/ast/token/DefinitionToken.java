package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


/**
 * 何かの要素の定義部を表すトークンクラス
 * 
 * @author kou-tngt
 *
 */
public class DefinitionToken extends AstTokenAdapter {

    /**
     * クラス定義部を表す定数インスタンス.
     */
    public static final DefinitionToken CLASS_DEFINITION = new DefinitionToken("CLASS_DEFINITION") {
        @Override
        public boolean isClassDefinition() {
            return true;
        }
        
        @Override
        public boolean isBlockDefinition() {
            return true;
        }
    };

    /**
     * コンストラクタ定義部を表す定数インスタンス.
     */
    public static final DefinitionToken CONSTRUCTOR_DEFINITION = new DefinitionToken(
            "CONSTRUCTOR_DEFINITION") {
        @Override
        public boolean isMethodDefinition() {
            return true;
        }

        @Override
        public boolean isConstructorDefinition() {
            return true;
        }
        
        @Override
        public boolean isBlockDefinition() {
            return true;
        }
    };

    /**
     * フィールド定義部を表す定数インスタンス.
     */
    public static final DefinitionToken FIELD_DEFINITION = new DefinitionToken("FIELD_DEFINITION") {
        @Override
        public boolean isFieldDefinition() {
            return true;
        }
    };

    /**
     * ローカルパラメータ（for文やcatch節の最初に宣言される変数）の定義部を表す定数インスタンス
     */
    public static final DefinitionToken LOCAL_PARAMETER_DEFINITION = new DefinitionToken(
            "LOCAL_PARAMETER_DEFINITION") {
        @Override
        public boolean isLocalParameterDefinition() {
            return true;
        }
    };

    /**
     * ローカル変数定義部を表す定数インスタンス.
     */
    public static final DefinitionToken LOCALVARIABLE_DEFINITION = new DefinitionToken(
            "LOCALVARIABLE_DEFINITION") {
        @Override
        public boolean isLocalVariableDefinition() {
            return true;
        }
    };

    /**
     * メソッド定義部を表す定数インスタンス.
     */
    public static final DefinitionToken METHOD_DEFINITION = new DefinitionToken("METHOD_DEFINITION") {
        @Override
        public boolean isMethodDefinition() {
            return true;
        }
        
        @Override
        public boolean isBlockDefinition() {
            return true;
        }
    };

    /**
     * メソッドパラメータ定義部を表す定数インスタンス.
     */
    public static final DefinitionToken METHOD_PARAMETER_DEFINITION = new DefinitionToken(
            "METHOD_PARAMETER_DEFINITION") {
        @Override
        public boolean isMethodParameterDefinition() {
            return true;
        }
    };

    /**
     * 名前空間の定義部を表す定数インスタンス.
     */
    public static final DefinitionToken NAMESPACE_DEFINITION = new DefinitionToken(
            "NAMESPACE_DEFINITION") {
        @Override
        public boolean isNameSpaceDefinition() {
            return true;
        }
    };
    
    /**
     * 型パラメータの定義部を表す定数インスタンス.
     */
    public static final DefinitionToken TYPE_PARAMETER_DEFINITION = new DefinitionToken("TYPE_PARAMETER_DEFINITION"){
        @Override
        public boolean isTypeParameterDefinition(){
            return true;
        }
    };

    /**
     * 指定された文字列で初期化するコンストラクタ.
     * @param text このトークンを表す文字列.
     */
    public DefinitionToken(final String text) {
        super(text);
    }
}
