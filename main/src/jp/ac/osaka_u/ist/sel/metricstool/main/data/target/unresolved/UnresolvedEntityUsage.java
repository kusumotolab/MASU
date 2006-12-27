package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;


/**
 * 未解決エンティティ使用を保存するためのクラス． 未解決エンティティ使用とは，パッケージ名やクラス名の参照 を表す．
 * 
 * @author y-higo
 * 
 */
public final class UnresolvedEntityUsage implements UnresolvedTypeInfo {

    /**
     * 未解決エンティティ使用オブジェクトを作成する．
     * 
     * @param availableNamespaces 利用可能な名前空間 
     * @param name 未解決エンティティ使用名
     */
    public UnresolvedEntityUsage(final AvailableNamespaceInfoSet availableNamespaces,
            final String[] name) {

        this.availableNamespaces = availableNamespaces;
        this.name = name;
    }

    /**
     * 型名を返す. このクラスは UnresolvedTypeInfo を実装しているので便宜上存在しているだけ．
     */
    public String getTypeName() {

        final String delimiter = Settings.getLanguage().getNamespaceDelimiter();
        final StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < (this.name.length - 1); i++) {
            buffer.append(this.name[i]);
            buffer.append(delimiter);
        }
        buffer.append(this.name[this.name.length - 1]);
        return buffer.toString();
    }

    /**
     * 未解決エンティティ使用名を返す．
     * 
     * @return 未解決エンティティ使用名
     */
    public String[] getName() {
        return this.name;
    }

    /**
     * この未解決エンティティ使用が利用することのできる名前空間を返す．
     * 
     * @return この未解決エンティティ使用が利用することのできる名前空間
     */
    public AvailableNamespaceInfoSet getAvailableNamespaces() {
        return this.availableNamespaces;
    }

    /**
     * この未解決エンティティ使用が利用することのできる名前空間を保存するための変数
     */
    private final AvailableNamespaceInfoSet availableNamespaces;

    /**
     * この未解決エンティティ使用名を保存するための変数
     */
    private final String[] name;
}
