package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstTokenAdapter;


/**
 * Java特有の要素を表すトークンを定義するクラス
 * 
 * @author kou-tngt
 *
 */
public class JavaAstToken extends AstTokenAdapter {

    /**
     * import記述部を表す定数インスタンス
     */
    public static final JavaAstToken IMPORT = new JavaAstToken("import");

    /**
     * enum要素の記述部を表す定数インスタンス
     */
    public static final JavaAstToken ENUM_CONSTANT = new JavaAstToken("ENUM_CONST");

    /**
     * super記述しを表す定数インスタンス
     */
    public static final JavaAstToken SUPER = new JavaAstToken("SUPER");

    /**
     * 配列初期化部を表す定数インスタンス
     */
    public static final JavaAstToken ARRAY_INIT = new JavaAstToken("ARRAY_INIT");
    
    /**
     * new文による配列型指定（[]）を表す定数インスタンス
     */
    public static final JavaAstToken ARRAY_INSTANTIATION = new JavaAstToken("ARRAY_INSTANTIATION");

    /**
     * this()のような自コンストラクタの呼び出しを表す定数インスタンス
     */
    public static final JavaAstToken CONSTRUCTOR_CALL = new JavaAstToken("CONSTRUCTOR_CALL");

    /**
     * super()のような親コンストラクタの呼び出しを表す定数インスタンス
     */
    public static final JavaAstToken SUPER_CONSTRUCTOR_CALL = new JavaAstToken(
            "SUPER_CONSTRUCTOR_CALL");

    /**
     * .classを記述を表す定数インスタンス
     */
    public static final JavaAstToken CLASS = new JavaAstToken("CLASS");

    /**
     * インタフェース定義部を表す定数インスタンス
     */
    public static final JavaAstToken INTERFACE_DEFINITION = new JavaAstToken("INTERFACE_DEFINITION") {
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
     * 指定された文字列を表すトークンを作成する.
     * @param text トークンの文字列
     */
    public JavaAstToken(final String text) {
        super(text);
    }

}
