package jp.ac.osaka_u.ist.sel.metricstool.main.plugin.connection;


import java.security.AccessControlException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.WeakHashSet;


/**
 * 進捗状況を報告する {@link ProgressReporter} とそれを受け取る {@link ProgressListener}の橋渡しをするクラス.
 * プラグイン１つ対してこのクラスのインスタンスが１つ作られる.
 * １つのインスタンスに対して，複数のリスナーが登録できる.
 * <p>
 * {@link ProgressReporter}を実装するクラスは， {@link #getConnection(AbstractPlugin)}メソッドに
 * 報告するプラグインインスタンスを渡すことで，このクラスのインスタンスを取得する.
 * 次に， {@link #connect(ProgressReporter)}メソッドに自分自身を渡すことで，
 * コネクションを確立する.
 * <p>
 *  {@link ProgressListener}を実装するクラスは，{@link #getConnection(AbstractPlugin)}メソッドに
 *  報告を受け取りたいプラグインインスタンスを渡すことで，このクラスのインスタンスを取得し，
 *  次に， {@link #addProgressListener(ProgressListener)}メソッドに自身を渡すことで，
 *  コネクションを確立する.
 * <p>
 * 特別権限を持つスレッドは，このクラスのインスタンスに対して， {@link #disconnect()}メソッドを呼び出すことで，
 * コネクションを強制的に解除させることができる.
 * コネクションが解除されたことはリスナー側には即座に通知され，プラグイン側には次回以降の進捗報告時に {@link ProgressConnectionException}がスローされる
 * 
 * @author kou-tngt
 *
 */
public final class ProgressConnector {

    /**
     * ファクトリメソッド.
     * 引数にプラグインインスタンスを与えることで，そのプラグインからの進捗報告を橋渡しするコネクタを作成
     * @param plugin プラグインインスタンス
     * @return pluginインスタンスからの進捗報告を橋渡しするコネクタ
     */
    public static synchronized ProgressConnector getConnection(final AbstractPlugin plugin) {
        if (connectionsMap.containsKey(plugin)) {
            //マップにインスタンスが登録されていたので，そのまま返す
            return connectionsMap.get(plugin);
        } else {
            //なかったので新しく作って登録して返す.
            final ProgressConnector connection = new ProgressConnector(plugin);
            connectionsMap.put(plugin, connection);
            return connection;
        }
    }

    /**
     * 進捗報告を受け取るリスナーを登録する
     * @param listener 進捗報告を受け取るリスナー
     * @throws NullPointerException　listnerがnullの場合
     */
    public final synchronized void addProgressListener(final ProgressListener listener) {
        if (null == listener) {
            throw new NullPointerException("listener is null.");
        }
        this.listeners.add(listener);
    }

    /**
     * コネクションを強制的に解除するメソッド
     * @throws AccessControlException このメソッドを呼び出したスレッドが特別権限を持たない場合
     */
    public final synchronized void disconnect() {
        //アクセス権チェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        //解除済みフラグを立てて，プラグインからのレポーターをnullにする
        this.connectionState = STATE.DISCONNECTED;
        this.reporter = null;
        
        //リスナーに通知してから全削除
        for(ProgressListener listener: listeners){
            listener.disconnected();
        }
        this.listeners.clear();
    }

    /**
     * リスナーを削除する
     * @param listener　削除するリスナー
     */
    public final synchronized void removeProgressListener(final ProgressListener listener) {
        if (null != listener) {
            this.listeners.remove(listener);
        }
    }

    /**
     * 引数に与えられたプラグイン側のレポーターとの接続を確立する
     * @param reporter 接続するレポーター
     * @throws AlreadyConnectedException 別のreporterとの接続が確立されている時に，同じプラグインからの別のリポーターが接続してきた場合
     * @throws NullPointerException　reporterがnullの場合
     */
    synchronized void connect(final ProgressReporter reporter) throws AlreadyConnectedException {
        if (null == reporter) {
            throw new NullPointerException("reporter is null.");
        }

        if (null != this.reporter) {
            //他のリポーターとの接続が確立中
            throw new AlreadyConnectedException("New progress connection was refused.");
        }

        this.reporter = reporter;
        this.connectionState = STATE.CONNECTED;
    }

    /**
     * 進捗情報を報告する
     * 
     * このパッケージ以外からは呼び出せない.
     * このメソッドを呼び出す時は，引数の正しさは事前にチェックしておかなければならない.
     * 
     * @param percentage 進捗情報（%）
     * @throws DisconnectedException コネクションが切断されている場合
     * @throws PluginConnectionException コネクションが確立されていない場合
     */
    void reportProgress(final int percentage) throws DisconnectedException,PluginConnectionException{
        if (STATE.INIT == this.connectionState) {
            throw new PluginConnectionException("No Connection was created.");
        } else if (STATE.DISCONNECTED == this.connectionState) {
            throw new DisconnectedException("Already disconnected.");
        }

        //コネクション管理の本質とは外れるので，例外ではなくアサーションで引数の正しさをチェック
        //呼び出し元で引数チェック＆例外投げをしておくべき
        assert (0 <= percentage && 100 >= percentage) : "Illegal parameter : percentage was "
                + percentage;

        if (STATE.CONNECTED == this.connectionState) {
            //接続中なのでイベントを作ってリスナに投げる
            this.fireProgress(new ProgressEvent(this.plugin, percentage));
        }
    }

    /**
     * リスナに進捗情報を通知するメソッド
     * @param event　通知するイベント
     */
    private void fireProgress(final ProgressEvent event) {
        if (null == event) {
            throw new NullPointerException("event is null.");
        }

        synchronized (this) {
            for (final ProgressListener listener : this.listeners) {
                listener.updataProgress(event);
            }
        }
    }

    /**
     * private コンストラクタ.
     * 引数にプラグインインスタンスを取る.
     * @param plugin プラグインインスタンス
     */
    private ProgressConnector(final AbstractPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * 接続状態を表す
     * 状態遷移は INIT -> CONNECTED -> DISCONNECTED -> CONNECTED -> ...
     * @author kou-tngt
     */
    private static enum STATE {
        INIT, CONNECTED, DISCONNECTED
    };

    /**
     * このクラスのインスタンスを管理するMap
     */
    private static final Map<AbstractPlugin, ProgressConnector> connectionsMap = new HashMap<AbstractPlugin, ProgressConnector>();

    /**
     * リスナーを管理するSet.
     * ここにだけ参照があっても意味が無いので，弱参照で持つ.
     */
    private final Set<ProgressListener> listeners = new WeakHashSet<ProgressListener>();

    /**
     * このインスタンスの接続状態
     */
    private STATE connectionState = STATE.INIT;

    /**
     * このインスタンスが接続するプラグイン
     */
    private final AbstractPlugin plugin;

    /**
     * このインスタンスに直接進捗報告をしてくるリポーター
     */
    private ProgressReporter reporter;

}
