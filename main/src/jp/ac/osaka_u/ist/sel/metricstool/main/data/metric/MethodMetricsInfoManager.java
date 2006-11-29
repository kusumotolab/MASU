package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * メソッドメトリクスを管理するクラス．
 * 
 * @author y-higo
 * 
 */
public final class MethodMetricsInfoManager implements Iterable<MethodMetricsInfo>, MessageSource {

    /**
     * このクラスのインスタンスを返す．シングルトンパターンを用いている．
     * 
     * @return このクラスのインスタンス
     */
    public static MethodMetricsInfoManager getInstance() {
        return SINGLETON;
    }

    /**
     * メトリクス情報一覧のイテレータを返す．
     * 
     * @return メトリクス情報のイテレータ
     */
    public Iterator<MethodMetricsInfo> iterator() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        Collection<MethodMetricsInfo> unmodifiableMethodMetricsInfoCollection = Collections
                .unmodifiableCollection(this.methodMetricsInfos.values());
        return unmodifiableMethodMetricsInfoCollection.iterator();
    }

    /**
     * 引数で指定されたメソッドのメトリクス情報を返す． 引数で指定されたメソッドのメトリクス情報が存在しない場合は， null を返す．
     * 
     * @param methodInfo ほしいメトリクス情報のメソッド
     * @return メトリクス情報
     */
    public MethodMetricsInfo get(final MethodInfo methodInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == methodInfo) {
            throw new NullPointerException();
        }

        return this.methodMetricsInfos.get(methodInfo);
    }

    /**
     * メトリクスを登録する
     * 
     * @param methodInfo メトリクス計測対象のメソッドオブジェクト
     * @param plugin メトリクスのプラグイン
     * @param value メトリクス値
     * @throws MetricAlreadyRegisteredException 登録しようとしているメトリクスが既に登録されている
     */
    public void putMetric(final MethodInfo methodInfo, final AbstractPlugin plugin, final int value)
            throws MetricAlreadyRegisteredException {

        MethodMetricsInfo methodMetricsInfo = this.methodMetricsInfos.get(methodInfo);

        // 対象メソッドの methodMetricsInfo が無い場合は，new して Map に登録する
        if (null == methodMetricsInfo) {
            methodMetricsInfo = new MethodMetricsInfo(methodInfo);
            this.methodMetricsInfos.put(methodInfo, methodMetricsInfo);
        }

        methodMetricsInfo.putMetric(plugin, value);
    }

    /**
     * メソッドメトリクスに登録漏れがないかをチェックする
     * 
     * @throws MetricNotRegisteredException 登録漏れがあった場合にスローされる
     */
    public void checkMetrics() throws MetricNotRegisteredException {

        MetricsToolSecurityManager.getInstance().checkAccess();

        for (MethodInfo methodInfo : MethodInfoManager.getInstance()) {

            MethodMetricsInfo methodMetricsInfo = this.get(methodInfo);
            if (null == methodMetricsInfo) {
                String methodName = methodInfo.getName();
                ClassInfo ownerClassInfo = methodInfo.getOwnerClass();
                String ownerClassName = ownerClassInfo.getName();
                String message = "Metrics of " + ownerClassName + "::" + methodName
                        + " are not registered!";
                MessagePrinter printer = new DefaultMessagePrinter(this,
                        MessagePrinter.MESSAGE_TYPE.ERROR);
                printer.println(message);
                throw new MetricNotRegisteredException(message);
            }
            methodMetricsInfo.checkMetrics();
        }
    }

    /**
     * メッセージ送信者名を返す
     * 
     * @return メッセージ送信者
     */
    public String getMessageSourceName() {
        return this.getClass().getName();
    }
    
    /**
     * メソッドメトリクスマネージャのオブジェクトを生成する． シングルトンパターンを用いているため，private がついている．
     * 
     */
    private MethodMetricsInfoManager() {
        this.methodMetricsInfos = Collections
                .synchronizedSortedMap(new TreeMap<MethodInfo, MethodMetricsInfo>());
    }

    /**
     * このクラスのオブジェクトを管理している定数．シングルトンパターンを用いている．
     */
    private static final MethodMetricsInfoManager SINGLETON = new MethodMetricsInfoManager();

    /**
     * メソッドメトリクスのマップを保存するための変数
     */
    private final SortedMap<MethodInfo, MethodMetricsInfo> methodMetricsInfos;
}
