package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.TypeElementBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;

/**
 * Javaの式中に登場する型要素を構築するクラス．
 * 
 * 親クラスの {@link TypeElementBuilder#getTypeUpperBounds()} メソッドをオーバーライドし，
 * nullが帰ってきた場合はjava.lang.Objectを返すように拡張する．
 * 
 * @author kou-tngt
 *
 */
public class JavaTypeElementBuilder extends TypeElementBuilder{

    public JavaTypeElementBuilder(ExpressionElementManager expressionManager, BuildDataManager buildManager) {
        super(expressionManager, buildManager);
    }

    /**
     * 型要素に型の上限が指定されている場合はその型を返す．
     * されていない場合は，java.lang.Objectを表す型を返す．
     * @return 型要素の型の上限
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.TypeElementBuilder#getTypeUpperBounds()
     */
    protected UnresolvedTypeInfo getTypeUpperBounds(){
        UnresolvedTypeInfo type = super.getTypeUpperBounds();
        if (null == type){
            return JavaTypeBuilder.JAVA_LANG_OBJECT;
        } else {
            return type;
        }
    }
}
