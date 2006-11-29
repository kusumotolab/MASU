package jp.ac.osaka_u.ist.sel.metricstool.main.plugin;


import java.security.AccessControlException;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.ProgressConnector;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.WeakHashSet;


/**
 * 実際にプラグインを実行するクラス.
 * インスタンス化には特別権限が必要.
 * @author kou-tngt
 *
 */
public class PluginExecutor implements Runnable {
    /**
     * 引数のプラグインを実行するインスタンスを生成する.
     * 特別権限を持つクラスからのみ呼び出すことができる.
     * @param plugin 実行するプラグイン
     * @throws AccessControlException 特別権限を持たないスレッドから呼ばれた場合
     */
    public PluginExecutor(final AbstractPlugin plugin) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        this.plugin = plugin;
    }

    /**
     * 実行が終了した時に，実行スレッドから呼び出されるリスナを登録するメソッド
     * @param listener 登録するリスナ
     * @throws NullPointerException listenerがnullの場合
     */
    public void addExecutionEndListener(final ExecutionEndListener listener) {
        if (null == listener) {
            throw new NullPointerException("listener is null.");
        }

        this.listeners.add(listener);
    }

    /**
     * 実行メソッド
     */
    public void execute() {
        try {
            //プラグインディレクトリへのファイルアクセスを要求してからプラグインのexecuteを呼び出す
            //MetricsToolSecurityManagerがシステムのセキュリティマネージャーではない場合，
            //ファイルアクセス権限が与えられるかどうかは
            //システムのセキュリティマネージャー次第
            MetricsToolSecurityManager.getInstance().requestPluginDirAccessPermission(this.plugin);
            this.plugin.execute();
        } catch (final Exception e) {
            (new DefaultMessagePrinter(this.plugin, MessagePrinter.MESSAGE_TYPE.ERROR)).println(e
                    .getMessage());
        }

        ProgressConnector.getConnector(this.plugin).progressEnd();
        MetricsToolSecurityManager.getInstance().removePluginDirAccessPermission(this.plugin);
        
        fireExecutionEnd();
    }

    /**
     * プラグインを取得する
     * @return プラグインを取得する
     */
    public AbstractPlugin getPlugin() {
        return this.plugin;
    }

    /**
     * 別スレッドとして起動される場合，のエントリメソッド.
     * {@link #execute()} を呼び出すのみである.
     * @see java.lang.Runnable#run()
     */
    public void run() {
        this.execute();
    }

    /**
     * 実行が終了した時に，実行スレッドから呼び出されるリスナを削除するメソッド
     * @param listener 削除したいリスナ
     */
    public void removeExectionEndListener(final ExecutionEndListener listener) {
        if (null != listener) {
            this.listeners.remove(listener);
        }
    }

    /**
     * 実行終了をリスナに通知する.
     */
    private void fireExecutionEnd() {
        for (final ExecutionEndListener listener : this.listeners) {
            listener.executionEnd(this.plugin);
        }
    }

    /**
     * 実行するプラグイン
     */
    private final AbstractPlugin plugin;

    /**
     * リスナのSet
     */
    private final Set<ExecutionEndListener> listeners = new WeakHashSet<ExecutionEndListener>();

}
