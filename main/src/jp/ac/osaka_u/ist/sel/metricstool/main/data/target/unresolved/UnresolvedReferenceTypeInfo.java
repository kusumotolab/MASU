package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決参照型を表すクラス
 * 
 * @author y-higo
 * 
 */
public class UnresolvedReferenceTypeInfo implements UnresolvedTypeInfo {

    /**
     * 利用可能な名前空間名，参照名を与えて初期化
     * 
     * @param availableNamespaces 名前空間名
     * @param referenceName 参照名
     */
    public UnresolvedReferenceTypeInfo(final AvailableNamespaceInfoSet availableNamespaces,
            final String[] referenceName) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == availableNamespaces) || (null == referenceName)) {
            throw new NullPointerException();
        }

        this.availableNamespaceSet = availableNamespaces;
        this.referenceName = referenceName;
        //this.fullReferenceName = referenceName;
        //this.ownerType = null;
        this.typeParameterUsages = new LinkedList<UnresolvedReferenceTypeInfo>();
    }
    
    /**
     * 利用可能な名前空間，型の完全修飾名を与えて初期化
     * @param referenceName 型の完全修飾名
     */
    public UnresolvedReferenceTypeInfo(final String[] referenceName) {
    	this(new AvailableNamespaceInfoSet(), referenceName);
    }

    ///**
    // * 利用可能な名前空間名，参照名を与えて初期化
    // * 
    // * @param availableNamespaces 名前空間名
    // * @param referenceName 参照名
    // */
    /*
    public UnresolvedReferenceTypeInfo(final AvailableNamespaceInfoSet availableNamespaces,
            final String[] referenceName, final UnresolvedReferenceTypeInfo ownerType) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == availableNamespaces) || (null == referenceName) || (null == ownerType)) {
            throw new NullPointerException();
        }

        this.availableNamespaceSet = availableNamespaces;
        String[] ownerReferenceName = ownerType.getFullReferenceName();
        String[] fullReferenceName = new String[referenceName.length+ownerReferenceName.length];
        System.arraycopy(ownerReferenceName, 0, fullReferenceName, 0, ownerReferenceName.length);
        System.arraycopy(referenceName, 0, fullReferenceName, ownerReferenceName.length, referenceName.length);
        this.fullReferenceName = fullReferenceName;
        this.referenceName = referenceName;
        this.ownerType = ownerType;
        this.typeParameterUsages = new LinkedList<UnresolvedReferenceTypeInfo>();
    }
*/
    /**
     * 型パラメータ使用を追加する
     * 
     * @param typeParameterUsage 追加する型パラメータ使用
     */
    public final void addTypeArgument(final UnresolvedReferenceTypeInfo typeParameterUsage) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeParameterUsage) {
            throw new NullPointerException();
        }

        this.typeParameterUsages.add(typeParameterUsage);
    }

    /**
     * このクラス参照で使用されている型パラメータの List を返す
     * 
     * @return このクラス参照で使用されている型パラメータの List
     */
    public final List<UnresolvedReferenceTypeInfo> getTypeArguments() {
        return Collections.unmodifiableList(this.typeParameterUsages);
    }

    /**
     * この参照型の名前を返す
     * 
     * @return この参照型の名前を返す
     */
    public final String getTypeName() {
        final String[] referenceName = this.getReferenceName();
        return referenceName[referenceName.length - 1];
    }

    ///**
    // * この参照型のownerも含めた参照名を返す
    // * 
    // * @return この参照型のownerも含めた参照名を返す
    // */
    /*public final String[] getFullReferenceName() {
        return this.fullReferenceName;
    }*/
    
    /**
     * この参照型の参照名を返す
     * 
     * @return この参照型の参照名を返す
     */
    public final String[] getReferenceName() {
        return this.referenceName;
    }

    ///**
    // * この参照型がくっついている未解決参照型を返す
    // * 
    // * @return この参照型がくっついている未解決参照型
    // */
    /*public final UnresolvedReferenceTypeInfo getOwnerType() {
        return this.ownerType;
    }*/

    ///**
    // * この参照型が，他の参照型にくっついているかどうかを返す
    // * 
    // * @return くっついている場合は true，くっついていない場合は false
    // */
    /*public final boolean hasOwnerReference() {
        return null != this.ownerType;
    }*/

    /**
     * この参照型の参照名を引数で与えられた文字で結合して返す
     * 
     * @param delimiter 結合に用いる文字
     * @return この参照型の参照名を引数で与えられた文字で結合した文字列
     */
    public final String getReferenceName(final String delimiter) {

        if (null == delimiter) {
            throw new NullPointerException();
        }

        final StringBuilder sb = new StringBuilder(this.referenceName[0]);
        for (int i = 1; i < this.referenceName.length; i++) {
            sb.append(delimiter);
            sb.append(this.referenceName[i]);
        }

        return sb.toString();
    }

    /**
     * この参照型の完全限定名として可能性のある名前空間名の一覧を返す
     * 
     * @return この参照型の完全限定名として可能性のある名前空間名の一覧
     */
    public final AvailableNamespaceInfoSet getAvailableNamespaces() {
        return this.availableNamespaceSet;
    }
    
    public final static UnresolvedReferenceTypeInfo getInstance(UnresolvedClassInfo referencedClass) {
        return new UnresolvedReferenceTypeInfo(referencedClass.getFullQualifiedName());
    }

    /**
     * 利用可能な名前空間名を保存するための変数，名前解決処理の際に用いる
     */
    private final AvailableNamespaceInfoSet availableNamespaceSet;

    /**
     * 参照名を保存する変数
     */
    private final String[] referenceName;
    
    ///**
    // * ownerも含めた参照名を保存する変数
    // */
    //private final String[] fullReferenceName;

    ///**
    // * この参照がくっついている未解決参照型を保存する変数
    // */
    //private final UnresolvedReferenceTypeInfo ownerType;

    /**
     * 型引数参照を保存するための変数
     */
    private final List<UnresolvedReferenceTypeInfo> typeParameterUsages;
    
}
