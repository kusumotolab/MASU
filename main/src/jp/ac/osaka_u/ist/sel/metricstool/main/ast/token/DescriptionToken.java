package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


/**
 * 何からの要素が記述されている箇所を表すトークンクラス
 * 
 * @author kou-tngt
 *
 */
public class DescriptionToken extends AstTokenAdapter {

    /**
     * 式記述部を表す定数インスタンス.
     */
    public static final DescriptionToken EXPRESSION = new DescriptionToken("EXPRESSION") {
        @Override
        public boolean isExpression() {
            return true;
        }
    };

    /**
     * 親クラス記述部を表す定数インスタンス.
     */
    public static final DescriptionToken INHERITANCE = new DescriptionToken("INHERITANCE") {
        @Override
        public boolean isInheritanceDescription() {
            return true;
        }
    };

    /**
     * 修飾子記述部を表す定数インスタンス.
     */
    public static final DescriptionToken MODIFIER = new DescriptionToken("MODIFIER") {
        @Override
        public boolean isModifiersDefinition() {
            return true;
        }
    };

    /**
     * 名前記述部を表す定数インスタンス.
     */
    public static final DescriptionToken NAME = new DescriptionToken("NAME") {
        @Override
        public boolean isNameDescription() {
            return true;
        }
    };

    /**
     * 型記述部を表す定数インスタンス.
     */
    public static final DescriptionToken TYPE = new DescriptionToken("TYPE") {
        @Override
        public boolean isTypeDescription() {
            return true;
        }
    };

    /**
     * 名前空間利用宣言記述部を表す定数インスタンス.
     */
    public static final DescriptionToken USING = new DescriptionToken("USING") {
        @Override
        public boolean isNameUsingDefinition() {
            return true;
        }
    };

    /**
     * 指定された文字列で初期化するコンストラクタ.
     * @param text このトークンを表す文字列.
     */
    public DescriptionToken(final String text) {
        super(text);
    }
}
