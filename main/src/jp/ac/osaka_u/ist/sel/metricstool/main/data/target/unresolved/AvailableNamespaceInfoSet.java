package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 利用可能な名前空間名の集合を現すクラス．
 * AvailableNamespaceInfoを要素として持つ
 * @author y-higo
 *
 */
public final class AvailableNamespaceInfoSet implements Iterable<AvailableNamespaceInfo> {

    public AvailableNamespaceInfoSet() {
        
        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        this.availableNamespaces = new HashSet<AvailableNamespaceInfo>();
    }

    public void add(final AvailableNamespaceInfo availableNamespace) {
        
        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == availableNamespace){
            throw new NullPointerException();
        }
        
        this.availableNamespaces.add(availableNamespace);
    }

    public Iterator<AvailableNamespaceInfo> iterator() {
        Set<AvailableNamespaceInfo> unmodifiableAvailableNamespace = Collections
                .unmodifiableSet(this.availableNamespaces);
        return unmodifiableAvailableNamespace.iterator();
    }

    private final Set<AvailableNamespaceInfo> availableNamespaces;
}
