package jp.ac.osaka_u.ist.sel.metricstool.main.plugin;


import java.security.AccessControlException;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.ConcurrentHashSet;


/**
 * @author kou-tngt
 *
 * プラグインインスタンスを管理するクラス．
 * 
 */
public class PluginManager {

    /**
     * シングルトンインスタンスを取得する
     * @return シングルトンインスタンス
     */
    public static PluginManager getInstance() {
        return SINGLETON;
    }

    /**
     * プラグインを登録する
     * このメソッドを呼び出すには特別権限が必要である
     * @param plugin 登録するプラグイン
     * @throws AccessControlException 特別権限スレッドでない場合
     * @throws NullPointerException pluginがnullの場合
     */
    public void addPlugin(final AbstractPlugin plugin) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == plugin) {
            throw new NullPointerException("plugin is null.");
        }

        this.plugins.add(plugin);
    }

    /**
     * プラグインを登録する
     * このメソッドを呼び出すには特別権限が必要である
     * @param collection 登録するプラグイン群をもつコレクション
     * @throws AccessControlException 特別権限スレッドでない場合
     * @throws NullPointerException collectionがnullの場合
     */
    public void addPlugins(final Collection<AbstractPlugin> collection) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == collection) {
            throw new NullPointerException("collection is null.");
        }

        for (final AbstractPlugin plugin : collection) {
            this.addPlugin(plugin);
        }
    }

    /**
     * プラグインの編集不可なSetを返す
     * プラグインスレッドからは呼び出せない
     * @return プラグインのSet
     * @throws AccessControlException プラグインスレッドからの呼び出しの場合
     */
    public Set<AbstractPlugin> getPlugins() {
        MetricsToolSecurityManager.getInstance().checkPlugin();
        return Collections.unmodifiableSet(this.plugins);
    }

    /**
     * プラグインを削除する
     * 特別権限スレッドのみから呼び出せる.
     * @param plugin 削除するプラグイン
     * @throws AccessControlException 特別権限を持っていない場合
     */
    public void removePlugin(final AbstractPlugin plugin) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null != plugin) {
            this.plugins.remove(plugin);
        }
    }

    /**
     * シングルトン用，空のprivateコンストラクタ
     */
    private PluginManager() {
    };

    /**
     * プラグインのSet
     */
    private final Set<AbstractPlugin> plugins = new ConcurrentHashSet<AbstractPlugin>();

    /**
     * シングルトンインスタンス
     */
    private static final PluginManager SINGLETON = new PluginManager();
}
