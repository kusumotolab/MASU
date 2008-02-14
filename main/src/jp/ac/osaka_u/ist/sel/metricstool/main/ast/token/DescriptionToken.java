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
     * 型下限記述部を表す定数インスタンス.
     */
    public static final DescriptionToken TYPE_LOWER_BOUNDS = new DescriptionToken(
            "TYPE_LOWER_BOUNDS") {
        @Override
        public boolean isTypeLowerBoundsDescription() {
            return true;
        }
    };

    /**
     * 型上限記述部を表す定数インスタンス.
     */
    public static final DescriptionToken TYPE_UPPER_BOUNDS = new DescriptionToken(
            "TYPE_UPPER_BOUNDS") {
        @Override
        public boolean isTypeUpperBoundsDescription() {
            return true;
        }
    };

    /**
     * 型引数記述部を表す定数インスタンス
     */
    public static final DescriptionToken TYPE_ARGUMENT = new DescriptionToken("TYPE_ARGUMENT") {
        @Override
        public boolean isTypeArgument() {
            return true;
        }
    };

    /**
     * 型引数記述部の列を表す定数インスタンス
     */
    public static final DescriptionToken TYPE_ARGUMENTS = new DescriptionToken("TYPE_ARGUMENTS") {
        @Override
        public boolean isTypeArguments() {
            return true;
        }
    };

    /**
     * ワイルドカード型引数を表す定数インスタンス
     */
    public static final DescriptionToken TYPE_WILDCARD = new DescriptionToken("TYPE_WILDCARD") {
        @Override
        public boolean isTypeWildcard() {
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

    public static final DescriptionToken SLIST = new DescriptionToken("SLIST") {
        @Override
        public boolean isSList() {
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
