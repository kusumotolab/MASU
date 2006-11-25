package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;

import java.util.Iterator;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;

/**
 * プラグインが MethodInfo にアクセスするために用いるインターフェース
 * 
 * @author y-higo
 *
 */
public class DefaultMethodInfoAccessor implements MethodInfoAccessor {

    /**
     * MethodInfo のイテレータを返す． このイテレータは参照専用であり変更処理を行うことはできない．
     * 
     * @return MethodInfo のイテレータ
     */
    public Iterator<MethodInfo> methodInfoIterator() {
        MethodInfoManager methodInfoManager = MethodInfoManager.getInstance();
        return methodInfoManager.iterator();
    }
    
}
