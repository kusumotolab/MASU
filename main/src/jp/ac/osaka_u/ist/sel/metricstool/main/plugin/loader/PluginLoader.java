package jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader;


import java.io.File;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;


/**
 * @author kou-tngt
 * 
 * このインタフェースはプラグインをロードするためのメソッド群を提供する．
 * loadPlugin，またはloadPluginsメソッド群を用いて，任意のディレクトリ以下のプラグインをロードすることができる．
 * 単にデフォルトのpluginsディレクトリから全てのプラグインをロードする場合はloadPlugins()メソッドを使う．
 *
 */
public interface PluginLoader {
    /**
     * デフォルトのpluginsディレクトリから、pluginDirNameで指定されたディレクトリ名を持つプラグインをロードする
     * @param pluginDirName プラグインディレクトリ名
     * @return ロードしたプラグインクラスのインスタンス
     * @throws PluginLoadException プラグインのロードに失敗した場合に投げられる．但し，下記の例外のいずれかにケースに該当した時はそちらが優先される．
     * @throws IllegalPluginXmlFormatException ロードするプラグインの設定情報を記述したXMLファイルの形式が正しくない場合．
     * @throws IlleagalPluginDirectoryStructureException ロードするプラグインのディレクトリ構成が正しくない場合．
     * @throws PluginClassLoadException プラグインのクラスロードに失敗した場合．
     */
    public AbstractPlugin loadPlugin(final String pluginDirName) throws PluginLoadException,
            IllegalPluginXmlFormatException, IlleagalPluginDirectoryStructureException,
            PluginClassLoadException;

    /**
     * pluginsDirで指定されてディレクトリ以下から，pluginNameで指定されたディレクトリ名を持つプラグインをロードする
     * @param pluginsDir プラグインが配置されるディレクトリ
     * @param pluginDirName プラグインのルートディレクトリ
     * @return ロードしたプラグインクラスのインスタンス
     * @throws PluginLoadException プラグインのロードに失敗した場合に投げられる．但し，下記の例外のいずれかにケースに該当した時はそちらが優先される．
     * @throws IllegalPluginXmlFormatException ロードするプラグインの設定情報を記述したXMLファイルの形式が正しくない場合．
     * @throws IlleagalPluginDirectoryStructureException ロードするプラグインのディレクトリ構成が正しくない場合．
     * @throws PluginClassLoadException プラグインのクラスロードに失敗した場合．
     */
    public AbstractPlugin loadPlugin(final File pluginsDir, final String pluginDirName)
            throws PluginLoadException, IllegalPluginXmlFormatException,
            IlleagalPluginDirectoryStructureException, PluginClassLoadException;

    /**
     * プラグイン自体のディレクトリを直接pluginRootDirで指定してロードするメソッド．
     * @param pluginRootDir プラグインのルートディレクトリ
     * @return ロードしたプラグインクラスのインスタンス
     * @throws PluginLoadException プラグインのロードに失敗した場合に投げられる．但し，下記の例外のいずれかにケースに該当した時はそちらが優先される．
     * @throws IllegalPluginXmlFormatException ロードするプラグインの設定情報を記述したXMLファイルの形式が正しくない場合．
     * @throws IlleagalPluginDirectoryStructureException ロードするプラグインのディレクトリ構成が正しくない場合．
     * @throws PluginClassLoadException プラグインのクラスロードに失敗した場合．
     */
    public AbstractPlugin loadPlugin(final File pluginRootDir) throws PluginLoadException,
            IllegalPluginXmlFormatException, IlleagalPluginDirectoryStructureException,
            PluginClassLoadException;

    /**
     * デフォルトのpluginsディレクトリから全てのプラグインをロードするメソッド
     * 個別のプラグインのロード失敗によって発生した例外は返さない．
     * @return ロードできた各プラグインのプラグインクラスを格納するメソッド
     * @throws PluginLoadException デフォルトのpluginsディレクトリの検出に失敗した場合．
     */
    public List<AbstractPlugin> loadPlugins() throws PluginLoadException;

    /**
     * 指定したディレクトリ以下にある全てのプラグインをロードするメソッド．
     * 個別のプラグインのロード失敗によって発生した例外は返さない．
     * @param pluginsDir プラグインが配置されているディレクトリ
     * @return　ロードできた各プラグインのプラグインクラスを格納するメソッド
     */
    public List<AbstractPlugin> loadPlugins(final File pluginsDir);
}
