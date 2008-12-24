package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.List;
import java.util.Map;


/**
 * 型パラメータが定義可能であることを表すインターフェース
 */
public interface TypeParameterizable {

    /**
     * 型パラメータ定義を追加する
     * 
     * @param typeParameter 追加する型パラメータ定義
     */
    void addTypeParameter(TypeParameterInfo typeParameter);

    /**
     * 引数で与えられたインデックスの型パラメータを返す
     * 
     * @param index 型パラメータを指定するためのインデックス
     * @return 引数で与えられたインデックスの型パラメータ
     */
    TypeParameterInfo getTypeParameter(int index);

    /**
     * 型パラメータのリストを返す
     * 
     * @return 型パラメータのリスト
     */
    List<TypeParameterInfo> getTypeParameters();

    /**
     * 型パラメータと実際に使用されている型のペアを追加する
     * 
     * @param typeParameterInfo 型パラメータ
     * @param usedType 実際に使用されている型
     */
    void addTypeParameterUsage(TypeParameterInfo typeParameterInfo, TypeInfo usedType);

    /**
     * 型パラメータと実際に使用されている型のマップを返す
     * 
     * @return 型パラメータと実際に使用されている型のマップ
     */
    Map<TypeParameterInfo, TypeInfo> getTypeParameterUsages();
}
