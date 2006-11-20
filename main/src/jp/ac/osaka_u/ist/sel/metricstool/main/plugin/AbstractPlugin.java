package jp.ac.osaka_u.ist.sel.metricstool.main.plugin;


import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRICS_TYPE;


/**
 * @author kou-tngt
 * 
 * 仮実装(2006/11/17） 他のコード群で型を利用するため，コンパイル用に登録．
 * 
 * <p>
 * メトリクス計測プラグイン実装用の抽象クラス
 * <p>
 * 各プラグインはこのクラスを継承したクラスを1つもたなければならない． また，そのクラス名をplugin.xmlファイルに指定の形式で記述しなければならない．
 * <p>
 * mainモジュールは各プラグインディレクトリからplugin.xmlファイルを探索し、 そこに記述されている，このクラスを継承したクラスをインスタンス化し、
 * 各メソッドを通じて情報を取得した後、executeメソッドを呼び出してメトリクス値を計測する
 */
public abstract class AbstractPlugin {

    /**
     * プラグインの情報を保存する内部不変クラス． AbstractPluginからのみインスタンス化できる．
     * <p>
     * プラグインの情報を動的に変更されると困るので、この内部クラスのインスタンスを用いて 情報をやりとすることでプラグイン情報の不変性を実現する．
     * 各プラグインの情報を保存するPluginInfoインスタンスの取得には {@link AbstractPlugin#getPluginInfo()}を用いる．
     * 
     * @author kou-tngt
     * 
     */
    public class PluginInfo {

        /**
         * デフォルトのコンストラクタ
         */
        private PluginInfo() {
            final LANGUAGE[] languages = AbstractPlugin.this.getMeasurableLanguages();
            this.measurableLanguages = new LANGUAGE[languages.length];
            System.arraycopy(languages, 0, this.measurableLanguages, 0, languages.length);
            this.metricsName = AbstractPlugin.this.getMetricsName();
            this.metricsType = AbstractPlugin.this.getMetricsType();
            this.useClassInfo = AbstractPlugin.this.useClassInfo();
            this.useMethodInfo = AbstractPlugin.this.useMethodInfo();
            this.useFileInfo = AbstractPlugin.this.useFileInfo();
            this.useMethodLocalInfo = AbstractPlugin.this.useMethodLocalInfo();
        }

        /**
         * このプラグインがメトリクスを計測できる言語を返す．
         * 
         * @return 計測可能な言語を全て含む配列．
         */
        public LANGUAGE[] getMeasurableLanguages() {
            return measurableLanguages;
        }

        /**
         * このプラグインが引数で指定された言語で利用可能であるかを返す．
         * 
         * @param language 利用可能であるかを調べたい言語
         * @return 利用可能である場合は true，利用できない場合は false．
         */
        public boolean isMeasurable(LANGUAGE language) {
            LANGUAGE[] measurableLanguages = this.getMeasurableLanguages();
            for (int i = 0; i < measurableLanguages.length; i++) {
                if (language.equals(measurableLanguages[i])) {
                    return true;
                }
            }
            return false;
        }

        /**
         * このプラグインが計測するメトリクスの名前を返す．
         * 
         * @return メトリクス名
         */
        public String getMetricsName() {
            return metricsName;
        }

        /**
         * このプラグインが計測するメトリクスのタイプを返す．
         * 
         * @return メトリクスタイプ
         * @see jp.ac.osaka_u.ist.sel.metricstool.main.util.METRICS_TYPE
         */
        public METRICS_TYPE getMetricsType() {
            return metricsType;
        }

        /**
         * このプラグインがクラスに関する情報を利用するかどうかを返す．
         * 
         * @return クラスに関する情報を利用する場合はtrue．
         */
        public boolean isUseClassInfo() {
            return useClassInfo;
        }

        /**
         * このプラグインがファイルに関する情報を利用するかどうかを返す．
         * 
         * @return ファイルに関する情報を利用する場合はtrue．
         */
        public boolean isUseFileInfo() {
            return useFileInfo;
        }

        /**
         * このプラグインがメソッドに関する情報を利用するかどうかを返す．
         * 
         * @return メソッドに関する情報を利用する場合はtrue．
         */
        public boolean isUseMethodInfo() {
            return useMethodInfo;
        }

        /**
         * このプラグインがメソッド内部に関する情報を利用するかどうかを返す．
         * 
         * @return メソッド内部に関する情報を利用する場合はtrue．
         */
        public boolean isUseMethodLocalInfo() {
            return useMethodLocalInfo;
        }

        private final LANGUAGE[] measurableLanguages;

        private final String metricsName;

        private final METRICS_TYPE metricsType;

        private final boolean useClassInfo;

        private final boolean useFileInfo;

        private final boolean useMethodInfo;

        private final boolean useMethodLocalInfo;
    }

    /**
     * プラグイン情報を保存している{@link PluginInfo}クラスのインスタンスを返す．
     * 同一のAbstractPluginインスタンスに対するこのメソッドは必ず同一のインスタンスを返し， その内部に保存されている情報は不変である．
     * 
     * @return プラグイン情報を保存している{@link PluginInfo}クラスのインスタンス
     */
    public final PluginInfo getPluginInfo() {
        if (null == this.pluginInfo) {
            synchronized (this) {
                if (null == this.pluginInfo) {
                    this.pluginInfo = new PluginInfo();
                }
            }
        }
        return this.pluginInfo;
    }

    /**
     * このプラグインがメトリクスを計測できる言語を返す 利用できる言語に制限のあるプラグインは、このメソッドをオーバーライドする必要がある．
     * 
     * @return 計測可能な言語を全て含む配列
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE
     */
    protected LANGUAGE[] getMeasurableLanguages() {
        return LANGUAGE.values();
    }

    /**
     * このプラグインが計測するメトリクスの名前を返す抽象メソッド．
     * 
     * @return メトリクス名
     */
    protected abstract String getMetricsName();

    /**
     * このプラグインが計測するメトリクスのタイプを返す抽象メソッド．
     * 
     * @return メトリクスタイプ
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.util.METRICS_TYPE
     */
    protected abstract METRICS_TYPE getMetricsType();

    /**
     * このプラグインがクラスに関する情報を利用するかどうかを返すメソッド． デフォルト実装ではfalseを返す．
     * クラスに関する情報を利用するプラグインはこのメソッドをオーバーラードしてtrueを返さなければ成らない．
     * 
     * @return クラスに関する情報を利用する場合はtrue．
     */
    protected boolean useClassInfo() {
        return false;
    }

    /**
     * このプラグインがファイルに関する情報を利用するかどうかを返すメソッド． デフォルト実装ではfalseを返す．
     * ファイルに関する情報を利用するプラグインはこのメソッドをオーバーラードしてtrueを返さなければ成らない．
     * 
     * @return ファイルに関する情報を利用する場合はtrue．
     */
    protected boolean useFileInfo() {
        return false;
    }

    /**
     * このプラグインがメソッドに関する情報を利用するかどうかを返すメソッド． デフォルト実装ではfalseを返す．
     * メソッドに関する情報を利用するプラグインはこのメソッドをオーバーラードしてtrueを返さなければ成らない．
     * 
     * @return メソッドに関する情報を利用する場合はtrue．
     */
    protected boolean useMethodInfo() {
        return false;
    }

    /**
     * このプラグインがメソッド内部に関する情報を利用するかどうかを返すメソッド． デフォルト実装ではfalseを返す．
     * メソッド内部に関する情報を利用するプラグインはこのメソッドをオーバーラードしてtrueを返さなければ成らない．
     * 
     * @return メソッド内部に関する情報を利用する場合はtrue．
     */
    protected boolean useMethodLocalInfo() {
        return false;
    }

    /**
     * メトリクス解析をスタートする抽象メソッド．
     */
    protected abstract void execute();

    /**
     * プラグインの情報を保存する{@link PluginInfo}クラスのインスタンス getPluginInfoメソッドの初回の呼び出しによって作成され．
     * それ以降、このフィールドは常に同じインスタンスを参照する．
     */
    private PluginInfo pluginInfo;
}
