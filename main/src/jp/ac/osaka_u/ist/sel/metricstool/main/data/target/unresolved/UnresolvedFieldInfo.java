package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;


/**
 * ASTパースで取得したフィールド情報を一時的に格納するためのクラス．
 * 
 * 
 * @author y-higo
 * 
 */
public final class UnresolvedFieldInfo extends UnresolvedVariableInfo {

    /**
     * Unresolvedフィールドオブジェクトを初期化する． フィールド名と型，定義しているクラスが与えられなければならない．
     * 
     * @param modifier 修飾子
     * @param name フィールド名
     * @param type フィールドの型
     * @param ownerClass フィールドを定義しているクラス
     */
    public UnresolvedFieldInfo(final ModifierInfo modifier, final String name,
            final UnresolvedTypeInfo type, final UnresolvedClassInfo ownerClass) {

        super(name, type);

        if ((null == modifier) || (null == ownerClass)) {
            throw new NullPointerException();
        }

        this.modifier = modifier;
        this.ownerClass = ownerClass;
    }

    /**
     * 修飾子を返す
     * 
     * @return 修飾子
     */
    public ModifierInfo getModifier(){
        return this.modifier;
    }

    /**
     * 修飾子をセットする
     * 
     * @param modifier 修飾子
     */
    public void setModifiar(final ModifierInfo modifier) {

        if (null == modifier) {
            throw new NullPointerException();
        }

        this.modifier = modifier;
    }

    /**
     * このフィールドを定義している未解決クラス情報を返す
     * 
     * @return このフィールドを定義している未解決クラス情報
     */
    public UnresolvedClassInfo getOwnerClass() {
        return this.ownerClass;
    }

    /**
     * このフィールドを定義している未解決クラス情報をセットする
     * 
     * @param ownerClass このフィールドを定義している未解決クラス情報
     */
    public void setOwnerClass(final UnresolvedClassInfo ownerClass) {

        if (null == ownerClass) {
            throw new NullPointerException();
        }

        this.ownerClass = ownerClass;
    }

    /**
     * このフィールドの修飾子を保存するための変数
     */
    private ModifierInfo modifier;

    /**
     * このフィールドを定義しているクラスを保存するための変数
     */
    private UnresolvedClassInfo ownerClass;
}
