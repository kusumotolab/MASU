package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

/**
 * ASTパースで取得したフィールド情報を一時的に格納するためのクラス．
 * 
 * 
 * @author y-higo
 * 
 */
public class UnresolvedFieldInfo extends UnresolvedVariableInfo{

    /**
     * Unresolvedフィールドオブジェクトを初期化する． フィールド名と型，定義しているクラスが与えられなければならない．
     * 
     * @param name フィールド名
     * @param type フィールドの型
     * @param ownerClass フィールドを定義しているクラス
     */
    public UnresolvedFieldInfo(final String name, final UnresolvedTypeInfo type, final UnresolvedClassInfo ownerClass){
        
        super(name, type);
        
        if (null == ownerClass){
            throw new NullPointerException();
        }
        
        this.ownerClass = ownerClass;
    }

    /**
     * このフィールドを定義しているUnresolved クラス情報を返す
     *
     * @return このフィールドを定義しているUnresolved クラス情報
     */
    public UnresolvedClassInfo getOwnerClass(){
        return this.ownerClass;
    }
    /**
     * このフィールドを定義しているクラスを保存するための変数
     */
    private final UnresolvedClassInfo ownerClass; 
}
