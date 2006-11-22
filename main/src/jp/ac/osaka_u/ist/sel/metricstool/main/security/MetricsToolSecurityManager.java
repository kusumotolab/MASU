package jp.ac.osaka_u.ist.sel.metricstool.main.security;


import java.security.AccessControlException;
import java.security.Permission;
import java.security.Permissions;
import java.util.Collections;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.util.WeakHashSet;


/**
 * メトリクスツールのアクセス制御をスレッド単位で動的に行うセキュリティマネージャ
 * <p>
 * 最初に {@link #getInstance()} を呼んだスレッドに全てのパーミッションを許可する特権を与える．
 * その後，特権を持っているスレッドが {@link #addPrivilegeThread(Thread)} メソッドを通じて登録したスレッドにも同様の特権を与える．
 * 特権を与えられたスレッドの特権は削除されない．
 * <p>
 * このクラスの利用者は特権を持たないスレッドからのアクセスを排除したい場合は {@link #checkAccess()} メソッドを呼ぶ．
 * 呼び出したスレッドが特権を持たない場合は， {@link AccessControlException}　例外がスローされる．
 * 特権を持つスレッドであった場合は，何もせずに処理を返す．
 * <p>
 * また，特権とは別のアクセスコントロールとしてグローバルパーミッションという概念を扱う．
 * グローバルパーミッションとはプラグインやGUIを含むVM上の全てのクラスに許されるパーミッションのことで，
 * {@link #addGlobalPermission(Permission)}によって登録されたパーミッションは，
 * 全てのスレッド，全てのコンテキスト，全てのコードソースに許可される．
 * ただし，グローバルパーミッションの追加は特権スレッドのみから可能である．
 * <p>
 * 特権を持たないスレッドで，グローバルパーミッションに登録されていないものについては，
 * 通常の {@link SecurityManager} と同等の機構を適用する．
 * <p>
 * シングルトンクラスであるため，コンストラクタは private であり，このクラスを継承することはできないが，
 * それを明示的に宣言するために final 修飾子をつけている．
 * 
 * @author kou-tngt
 *
 */
public final class MetricsToolSecurityManager extends SecurityManager {

    /**
     * シングルトンインスタンスを取得する
     * @return シングルトンインスタンス
     */
    public static MetricsToolSecurityManager getInstance() {
        if (null == SINGLETON) {
            synchronized (MetricsToolSecurityManager.class) {
                if (null == SINGLETON) {
                    SINGLETON = new MetricsToolSecurityManager();
                }
            }
        }
        return SINGLETON;
    }

    /**
     * メトリクスツールを実行しているVM上の全体で許可するパーミッションを追加する．
     * ロギングとかやりたい場合は，これを使って登録する．
     * 登録するには呼び出しスレッドに特権が必要
     * @param permission 許可したいパーミッションインスタンス
     * @throws AccessControlException スレッドに特別権限がない場合
     * @throws NullPointerException permissionがnullの場合
     */
    public final void addGlobalPermission(final Permission permission) {
        this.checkAccess();
        if (null == permission){
            throw new NullPointerException("permission is null.");
        }
        
        this.globalPermissions.add(permission);
    }

    /**
     * 引数 thread で与えられたスレッドに特権を付与する
     * @param thread 特権を付与したいスレッド
     * @throws AccessControlException 呼び出し側のスレッドが特権を持っていなかった場合
     * @throws NullPointerException threadがnullだった場合
     */
    public final void addPrivilegeThread(final Thread thread) {
        this.checkAccess();
        if (null == thread) {
            throw new NullPointerException("Added thread is null.");
        }
        
        this.privilegeThreads.add(thread);
    }

    /**
     * 特権スレッドからの呼び出しかどうかを判定するメソッド．
     * 特権スレッド以外から呼び出されると， {@link AccessControlException}　をスローする．
     * @throws AccessControlException 特権スレッド以外から呼び出された場合
     */
    public final void checkAccess() {
        //カレントスレッドを取得
        final Thread currentThread = Thread.currentThread();
        if (!this.isPrivilegeThread(currentThread)) {
            //登録されていなかった

            //エラー表示用にスタックとレースの取得
            final StackTraceElement[] traces = currentThread.getStackTrace();

            //このメソッドの呼び出し元のメソッドを取得
            assert (null != traces && 3 < traces.length) : "Illegal state: empty stack trace.";
            final StackTraceElement callerMethod = traces[3];

            throw new AccessControlException(
                    "Permission denide: current thread can not invoke the method "
                            + callerMethod.getClassName() + "#" + callerMethod.getMethodName()
                            + ".");
        }
    }

    /**
     * {@link SecurityManager#checkPermission(Permission)} メソッドをオーバーライドし，
     * 特権スレッドからの呼び出し，あるいはグローバルパーミッションとして登録済みであれば，
     * パーミッションチェックをせずに終了する．
     * そうでないなら，親クラスのメソッドを呼び，パーミッションのチェックを行う．
     * @param perm チェックするパーミッション
     * @throws NullPointerException 引数permがnullの場合
     * @throws SecurityException 特権スレッドでもグローバルパーミッションでもない場合に，パーミッションが許可されていない場合
     * @see java.lang.SecurityManager#checkPermission(java.security.Permission)
     */
    @Override
    public final void checkPermission(final Permission perm) {
        if (null == perm) {
            throw new NullPointerException("Permission is null.");
        }
        if (this.isPrivilegeThread()) {
            return;
        } else if (this.globalPermissions.implies(perm)) {
            return;
        } else {
            super.checkPermission(perm);
        }
    }

    /**
     * {@link SecurityManager#checkPermission(Permission, Object)} メソッドをオーバーライドし，
     * グローバルパーミッションとして登録済みであればパーミッションチェックをせずに終了する．
     * そうでないなら，親クラスのメソッドを呼び，パーミッションのチェックを行う．
     * @param perm チェックするパーミッション
     * @throws NullPointerException 引数permがnullの場合
     * @throws SecurityException permがグローバルパーミッションでない場合に，パーミッションが許可されていない場合
     * @see java.lang.SecurityManager#checkPermission(Permission, Object)
     */
    @Override
    public void checkPermission(final Permission perm, final Object context) {
        if (null == perm) {
            throw new NullPointerException("Permission is null.");
        }
        if (this.globalPermissions.implies(perm)) {
            return;
        } else {
            super.checkPermission(perm, context);
        }
    }

    /**
     * カレントスレッドが特権を持っているかを返す
     * @return 特権を持っていればtrue
     */
    public final boolean isPrivilegeThread() {
        return this.isPrivilegeThread(Thread.currentThread());
    }

    /**
     * 引数 thread で与えられたスレッドが特権を持っているかを返す
     * @param thread 特権を持っているかを調べたいスレッド
     * @return 引数 thread で与えられたスレッドが特権を持っていればtrue
     */
    public final boolean isPrivilegeThread(final Thread thread) {
        return this.privilegeThreads.contains(thread);
    }

    /**
     * シングルトン用privateコンストラクタ．
     * ここを呼び出したスレッドを初期特権クラスとして登録する．
     */
    private MetricsToolSecurityManager() {
        final Thread thread = Thread.currentThread();
        assert (null != thread) : "Illegal state : current thread is null.";
        this.privilegeThreads.add(thread);
    }

    /**
     * 特権スレッドのセット．
     * 他の全ての参照が切れたら特権スレッドを持っていても意味がないので，弱参照にするために {@link WeakHashSet} を用いる．
     * また，マルチスレッド環境で適切に動作させるために {@link Collections#synchronizedSet(Set)} を使って同期させる．
     */
    private final Set<Thread> privilegeThreads = Collections
            .synchronizedSet((new WeakHashSet<Thread>()));

    /**
     * シングルトンインスタンス．
     */
    private static MetricsToolSecurityManager SINGLETON;

    /**
     * VM全体で許可するパーミッションのコレクション
     */
    private final Permissions globalPermissions = new Permissions();
}
