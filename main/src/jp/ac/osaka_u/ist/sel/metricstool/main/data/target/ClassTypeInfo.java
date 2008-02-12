package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 参照型を表すクラス
 * 
 * @author higo
 * 
 */
public final class ClassTypeInfo implements ReferenceTypeInfo {

    /**
     * 参照型のListをクラスのListに変換する
     * 
     * @param references 参照型のList
     * @return クラスのList
     */
    public static List<ClassInfo> convert(final List<ClassTypeInfo> references) {

        final List<ClassInfo> classInfos = new LinkedList<ClassInfo>();
        for (final ClassTypeInfo reference : references) {
            classInfos.add(reference.getReferencedClass());
        }

        return Collections.unmodifiableList(classInfos);
    }

    /**
     * 参照型のSortedSetをクラスのSortedSetに変換する
     * 
     * @param references 参照型のSortedSet
     * @return クラスのSortedSet
     */
    public static SortedSet<ClassInfo> convert(final SortedSet<ClassTypeInfo> references) {

        final SortedSet<ClassInfo> classInfos = new TreeSet<ClassInfo>();
        for (final ClassTypeInfo reference : references) {
            classInfos.add(reference.getReferencedClass());
        }

        return Collections.unmodifiableSortedSet(classInfos);
    }

    /**
     * 参照されるクラスを与えて初期化
     * 
     * @param referencedClass 参照されるクラス
     */
    public ClassTypeInfo(final ClassInfo referencedClass) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == referencedClass) {
            throw new NullPointerException();
        }

        this.referencedClass = referencedClass;
        this.typeArguments = new ArrayList<TypeInfo>();
    }

    /**
     * 引数で与えられた型を等しいかどうかを比較．
     * 
     * @return 等しい場合はtrue，等しくない場合はfalse
     */
    @Override
    public boolean equals(TypeInfo typeInfo) {

        // 引数が null ならば，等しくない
        if (null == typeInfo) {
            return false;
        }

        // 引数が参照型でなければ，等しくない
        if (!(typeInfo instanceof ClassTypeInfo)) {
            return false;
        }

        // 引数が参照型の場合，
        // 参照されているクラスが等しくない場合は，参照型は等しくない
        final ClassTypeInfo targetReferenceType = (ClassTypeInfo) typeInfo;
        if (!this.referencedClass.equals(targetReferenceType)) {
            return false;
        }

        // 型パラメータの数が異なる場合は，等しくない
        final List<TypeInfo> thisTypeParameters = this.typeArguments;
        final List<TypeInfo> targetTypeParameters = targetReferenceType.getTypeArguments();
        if (thisTypeParameters.size() != targetTypeParameters.size()) {
            return false;
        }

        // 全ての型パラメータが等しくなければ，等しくない
        final Iterator<TypeInfo> thisTypeParameterIterator = thisTypeParameters.iterator();
        final Iterator<TypeInfo> targetTypeParameterIterator = targetTypeParameters.iterator();
        while (thisTypeParameterIterator.hasNext()) {
            final TypeInfo thisTypeParameter = thisTypeParameterIterator.next();
            final TypeInfo targetTypeParameter = targetTypeParameterIterator.next();
            if (!thisTypeParameter.equals(targetTypeParameter)) {
                return false;
            }
        }

        return true;
    }

    /**
     * この参照型を表す文字列を返す
     * 
     * @return この参照型を表す文字列
     */
    @Override
    public String getTypeName() {

        final StringBuilder sb = new StringBuilder();
        sb.append(this.referencedClass.getFullQualifiedName("."));

        if (0 <= this.typeArguments.size()) {
            sb.append("<");
            for (final TypeInfo typeParameter : this.typeArguments) {
                sb.append(typeParameter.getTypeName());
            }
            sb.append(">");
        }

        return sb.toString();
    }

    /**
     * 参照されているクラスを返す
     * 
     * @return 参照されているクラス
     */
    public ClassInfo getReferencedClass() {
        return this.referencedClass;
    }

    /**
     * この参照型に用いられている型引数のリストを返す
     * 
     * @return この参照型に用いられている型引数のリストを返す
     */
    public List<TypeInfo> getTypeArguments() {
        return Collections.unmodifiableList(this.typeArguments);
    }

    /**
     * この参照型のインデックスで指定された型引数を返す
     * 
     * @param index　型引数のインデックス
     * @return　この参照型のインデックスで指定された型引数
     */
    public TypeInfo getTypeArgument(final int index) {
        return this.typeArguments.get(index);
    }

    /**
     * この参照型に型引数を追加
     * 
     * @param argument 追加する型引数
     */
    public void addTypeArgument(final TypeInfo argument) {
        this.typeArguments.add(argument);
    }

    /**
     * この参照型が表すクラスを保存するための変数
     */
    private final ClassInfo referencedClass;

    /**
     * この参照型の型パラメータを保存するための変数
     */
    private final List<TypeInfo> typeArguments;

}
