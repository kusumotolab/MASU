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
        this.typeParameterUsages = new LinkedList<UnresolvedTypeParameterUsage>();
    }

    /**
     * 型パラメータ使用を追加する
     * 
     * @param typeParameterUsage 追加する型パラメータ使用
     */
    public final void addTypeParameterUsage(final UnresolvedTypeParameterUsage typeParameterUsage) {

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
    public final List<UnresolvedTypeParameterUsage> getTypeParameterUsages() {
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

    /**
     * この参照型の参照名を返す
     * 
     * @return この参照型の参照名を返す
     */
    public final String[] getReferenceName() {
        return this.referenceName;
    }

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

    /**
     * 利用可能な名前空間名を保存するための変数，名前解決処理の際に用いる
     */
    private final AvailableNamespaceInfoSet availableNamespaceSet;

    /**
     * 参照名を保存する変数
     */
    private final String[] referenceName;

    /**
     * 未解決型パラメータ使用を保存するための変数
     */
    private final List<UnresolvedTypeParameterUsage> typeParameterUsages;
}
