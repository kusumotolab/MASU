package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external;


import java.util.HashSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;


/**
 * 外部メソッドの引数情報を保存するためのクラス
 * 
 * @author higo
 */
public final class ExternalParameterInfo extends ParameterInfo {

    /**
     * 引数の型を指定してオブジェクトを初期化．外部定義のメソッド名なので引数名は不明．
     * 
     * @param type 引数の型
     * @param definitionMethod 宣言しているメソッド
     */
    public ExternalParameterInfo(final TypeInfo type, final MethodInfo definitionMethod) {
        super(new HashSet<ModifierInfo>(), UNKNOWN_NAME, type, definitionMethod, 0, 0, 0, 0);
    }

    /**
     * ExternalParameterInfo では利用できない
     */
    @Override
    public int getFromLine() {
        throw new CannotUseException();
    }

    /**
     * ExternalParameterInfo では利用できない
     */
    @Override
    public int getFromColumn() {
        throw new CannotUseException();
    }

    /**
     * ExternalParameterInfo では利用できない
     */
    @Override
    public int getToLine() {
        throw new CannotUseException();
    }

    /**
     * ExternalParameterInfo では利用できない
     */
    @Override
    public int getToColumn() {
        throw new CannotUseException();
    }

    /**
     * 不明な引数名を表す定数
     */
    public final static String UNKNOWN_NAME = "unknown";
}
