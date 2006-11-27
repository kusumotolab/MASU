package jp.ac.osaka_u.ist.sel.metricstool.main.plugin;


import java.io.File;

import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.ProgressSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRICS_TYPE;


/**
 * メトリクス計測プラグイン実装用の抽象クラス
 * <p>
 * 各プラグインはこのクラスを継承したクラスを1つもたなければならない． また，そのクラス名をplugin.xmlファイルに指定の形式で記述しなければならない．
 * <p>
 * mainモジュールは各プラグインディレクトリからplugin.xmlファイルを探索し、 そこに記述されている，このクラスを継承したクラスをインスタンス化し、
 * 各メソッドを通じて情報を取得した後、executeメソッドを呼び出してメトリクス値を計測する
 * 
 * @author kou-tngt
 */
public abstract class AbstractPlugin implements MessageSource, ProgressSource {

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
            this.description = AbstractPlugin.this.getDescription();
            this.detailDescription = AbstractPlugin.this.getDetailDescription();
        }

        /**
         * このプラグインの簡易説明を１行で返す（できれば英語で）.
         * デフォルトの実装では "Measure メトリクス名 metrics." と返す
         * 各プラグインはこのメソッドを任意にオーバーライドする.
         * @return 簡易説明文字列
         */
        public String getDescription() {
            return this.description;
        }

        /**
         * このプラグインの詳細説明を返す（できれば英語で）.
         * デフォルトの実装では空文字列を返す
         * 各プラグインはこのメソッドを任意にオーバーライドする.
         * @return 詳細説明文字列
         */
        public String getDetailDescription() {
            return this.detailDescription;
        }

        /**
         * このプラグインがメトリクスを計測できる言語を返す．
         * 
         * @return 計測可能な言語を全て含む配列．
         */
        public LANGUAGE[] getMeasurableLanguages() {
            return this.measurableLanguages;
        }

        /**
         * このプラグインが引数で指定された言語で利用可能であるかを返す．
         * 
         * @param language 利用可能であるかを調べたい言語
         * @return 利用可能である場合は true，利用できない場合は false．
         */
        public boolean isMeasurable(final LANGUAGE language) {
            final LANGUAGE[] measurableLanguages = this.getMeasurableLanguages();
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
            return this.metricsName;
        }

        /**
         * このプラグインが計測するメトリクスのタイプを返す．
         * 
         * @return メトリクスタイプ
         * @see jp.ac.osaka_u.ist.sel.metricstool.main.util.METRICS_TYPE
         */
        public METRICS_TYPE getMetricsType() {
            return this.metricsType;
        }

        /**
         * このプラグインがクラスに関する情報を利用するかどうかを返す．
         * 
         * @return クラスに関する情報を利用する場合はtrue．
         */
        public boolean isUseClassInfo() {
            return this.useClassInfo;
        }

        /**
         * このプラグインがファイルに関する情報を利用するかどうかを返す．
         * 
         * @return ファイルに関する情報を利用する場合はtrue．
         */
        public boolean isUseFileInfo() {
            return this.useFileInfo;
        }

        /**
         * このプラグインがメソッドに関する情報を利用するかどうかを返す．
         * 
         * @return メソッドに関する情報を利用する場合はtrue．
         */
        public boolean isUseMethodInfo() {
            return this.useMethodInfo;
        }

        /**
         * このプラグインがメソッド内部に関する情報を利用するかどうかを返す．
         * 
         * @return メソッド内部に関する情報を利用する場合はtrue．
         */
        public boolean isUseMethodLocalInfo() {
            return this.useMethodLocalInfo;
        }

        private final LANGUAGE[] measurableLanguages;

        private final String metricsName;

        private final METRICS_TYPE metricsType;

        private final String description;

        private final String detailDescription;

        private final boolean useClassInfo;

        private final boolean useFileInfo;

        private final boolean useMethodInfo;

        private final boolean useMethodLocalInfo;
    }

    /**
     * プラグインのルートディレクトリをセットする
     * 一度セットされた値を変更することは出来ない.
     * @param rootDir ルートディレクトリ
     * @throws NullPointerException rootDirがnullの場合
     * @throws IllegalStateException rootDirが既にセットされている場合
     */
    public final synchronized void setPluginRootdir(final File rootDir) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == rootDir) {
            throw new NullPointerException("rootdir is null.");
        }
        if (null != this.pluginRootDir) {
            throw new IllegalStateException("rootdir was already set.");
        }

        this.pluginRootDir = rootDir;
    }

    /**
     * メッセージ送信者としての名前を返す
     * @return 送信者としての名前
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.plugin.connection.MessageSource#getMessageSourceName()
     */
    public String getMessageSourceName() {
        return sourceName;
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
                    this.sourceName = "Plugin(" + pluginInfo.getMetricsName() + ")";
                }
            }
        }
        return this.pluginInfo;
    }

    /**
     * プラグインのルートディレクトリを返す
     * @return プラグインのルートディレクトリ
     */
    public final File getPluginRootDir() {
        return this.pluginRootDir;
    }

    /**
     * 進捗情報送信者としての名前を返す
     * @return 進捗情報送信者としての名前
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.plugin.connection.ProgressSource#getProgressSourceName()
     */
    public String getProgressSourceName() {
        return sourceName;
    }

    /**
     * このプラグインの簡易説明を１行で返す（できれば英語で）
     * デフォルトの実装では "Measure メトリクス名 metrics." と返す
     * 各プラグインはこのメソッドを任意にオーバーライドする.
     * @return 簡易説明文字列
     */
    protected String getDescription() {
        return "Measure " + this.getMetricsName() + " metrics.";
    }

    /**
     * このプラグインの詳細説明を返す（できれば英語で）
     * デフォルト実装では空文字列を返す.
     * 各プラグインはこのメソッドを任意にオーバーライドする.
     * @return
     */
    protected String getDetailDescription() {
        return "";
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

    /**
     * プラグインのルートディレクトリ
     */
    private File pluginRootDir;

    /**
     * {@link MessageSource}と {@link ProgressSource}用の名前
     */
    private String sourceName = "";
}
