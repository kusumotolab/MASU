package jp.ac.osaka_u.ist.sel.metricstool.main.plugin;


import java.security.AccessControlException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.ClosableLinkedBlockingQueue;


/**
 * プラグインを実行するランチャー
 * ほとんどのパブリックメソッドの実行に特別権限を必要とする.
 * @author kou-tngt
 *
 */
public final class DefaultPluginLauncher implements PluginLauncher {

    /**
     * プラグインの実行をキャンセルするメソッド.
     * 特別権限を持つスレッドからしか実行できない.
     * @param plugin キャンセルするプラグイン
     * @return キャンセルできた場合はtrueできなかったり，すでに終了していた場合はfalse
     * @throws NullPointerException pluginがnullの場合
     * @throws AccessControlException 特別権限を持たない場合
     */
    public boolean cancel(final AbstractPlugin plugin) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == plugin) {
            throw new NullPointerException("plugin is null.");
        }

        if (this.futureMap.containsKey(plugin)) {
            final Future<Boolean> future = this.futureMap.get(plugin);
            this.futureMap.remove(plugin);
            return future.cancel(true);
        }
        return false;
    }

    /**
     * 実行をまとめてキャンセルするメソッド.
     * @param plugins キャンセルするプラグイン群を含むコレクション
     * @throws NullPointerException pluginsがnullの場合
     */
    public void cancelAll(final Collection<AbstractPlugin> plugins) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == plugins) {
            throw new NullPointerException("plugins is null.");
        }

        for (final AbstractPlugin plugin : plugins) {
            this.cancel(plugin);
        }
    }

    /**
     * 実行中，実行待ちのタスクを全てキャンセルする.
     */
    public void cancelAll() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        for (final AbstractPlugin plugin : this.futureMap.keySet()) {
            this.cancel(plugin);
        }
    }

    /**
     * 現在実行中のプラグインの数を返すメソッド.
     * @return 実行中のプラグインの数.
     */
    public int getCurrentLaunchingNum() {
        return this.threadPool.getActiveCount();
    }

    /**
     * 現在の同時実行最大数を返すメソッド
     * @return 同時実行最大数
     */
    public int getMaximumLaunchingNum() {
        return this.threadPool.getMaximumPoolSize();
    }

    /**
     * プラグインを実行するメソッド.
     * 特別権限を持つスレッドからしか実行できない.
     * @param plugin 実行するプラグイン
     * @throws AccessControlException 特別権限を持たないスレッドから呼び出された場合
     * @throws NullPointerException pluginがnullの場合
     */
    public void launch(final AbstractPlugin plugin) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (this.stoped) {
            throw new IllegalStateException("launcher was already stoped.");
        }
        if (null == plugin) {
            throw new NullPointerException("plugin is null.");
        }

        final RunnablePlugin runnablePlugin = new RunnablePlugin(plugin);
        final Future future = this.threadPool.submit(runnablePlugin);
        this.futureMap.put(runnablePlugin.getPlugin(), future);
    }

    /**
     * プラグインをまとめて実行するメソッド.
     * 特別権限を持つスレッドからしか実行できない.
     * @param plugins 実行するプラグイン群を含むコレクション
     * @throws NullPointerException pluginsがnullの場合
     * @throws AccessControlException 特別権限を持たないスレッドから呼び出された場合
     */
    public void launchAll(final Collection<AbstractPlugin> plugins) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == plugins) {
            throw new NullPointerException("plugins is null.");
        }

        for (final AbstractPlugin plugin : plugins) {
            this.launch(plugin);
        }
    }

    /**
     * 同時実行最大数を設定するメソッド
     * @param size 同時実行最大数
     * @throws IllegalArgumentException sizeが0以下だった場合
     */
    public void setMaximumLaunchingNum(final int size) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (1 > size) {
            throw new IllegalArgumentException("parameter size must be natural number.");
        }
        this.threadPool.setCorePoolSize(size);
    }

    /**
     *  ランチャーを終了する.
     *  実行中のタスクは終わるまで待つ.
     */
    public void stopLaunching() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        this.stoped = true;
        this.workQueue.close();
        this.threadPool.setCorePoolSize(0);
    }

    /**
     * ランチャーを終了する.
     * 実行中のタスクも全てキャンセルする.
     */
    public void stopLaunchingNow() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        this.stopLaunching();
        this.cancelAll();
    }

    /**
     * プラグイン実行用スレッドのファクトリクラス
     * @author kou-tngt
     *
     */
    private class PluginThreadFactory implements ThreadFactory {
        /**
         * プラグイン実行用のスレッドを作成するメソッド.
         * プラグインスレッドとして登録もする.
         * @see ThreadFactory#newThread(Runnable)
         */
        public Thread newThread(final Runnable r) {
            final Thread thread = new Thread(this.PLUGIN_THREAD_GROUP, r, "plugin_"
                    + ++this.threadNameCount);
            MetricsToolSecurityManager.getInstance().addPluginThread(thread);
            return thread;
        }

        /**
         * プラグインスレッド用のスレッドグループ
         */
        private final ThreadGroup PLUGIN_THREAD_GROUP = new ThreadGroup("PluginThreads");

        /**
         * スレッドのナンバリング用変数
         */
        private int threadNameCount = 0;
    }

    /**
     * プラグインをスレッド単位で実行するための {@link Runnable} なクラス
     * @author kou-tngt
     *
     */
    private class RunnablePlugin implements Runnable {
        public void run() {
            try {
                //プラグインディレクトリへのファイルアクセスを要求してからプラグインのexecuteを呼び出す
                //MetricsToolSecurityManagerがシステムのセキュリティマネージャーではない場合，
                //ファイルアクセス権限が与えられるかどうかは
                //システムのセキュリティマネージャー次第
                MetricsToolSecurityManager.getInstance().requestPluginDirAccessPermission(
                        this.plugin);
                this.plugin.execute();
            } catch (final Exception e) {
                (new DefaultMessagePrinter(this.plugin, MessagePrinter.MESSAGE_TYPE.ERROR))
                        .println(e.getMessage());
            }
            DefaultPluginLauncher.this.futureMap.remove(this.plugin);
        }

        /**
         * コンストラクタ
         * @param plugin このクラスで実行するプラグイン
         */
        private RunnablePlugin(final AbstractPlugin plugin) {
            this.plugin = plugin;
        }

        /**
         * このインスタンスで実行するプラグインを返す
         * @return このインスタンスで実行するプラグイン
         */
        private AbstractPlugin getPlugin() {
            return this.plugin;
        }

        /**
         * このインスタンスで実行するプラグイン
         */
        private final AbstractPlugin plugin;

    }

    /**
     * 各 {@link RunnablePlugin} のFutureを保存するマップ
     */
    private final Map<AbstractPlugin, Future> futureMap = new ConcurrentHashMap<AbstractPlugin, Future>();

    /**
     * ランチャーを停止されたかどうかを表す変数
     */
    private boolean stoped = false;

    /**
     * スレッドプールに使用させるキュー
     */
    private final ClosableLinkedBlockingQueue<Runnable> workQueue = new ClosableLinkedBlockingQueue<Runnable>();

    /**
     * 内部的に実際にスレッドを実行するスレッドプール
     */
    private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(Integer.MAX_VALUE,
            Integer.MAX_VALUE, 0, TimeUnit.SECONDS, this.workQueue, new PluginThreadFactory());
}
