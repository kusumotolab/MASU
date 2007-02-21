package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import java.util.Iterator;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;


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
    public Iterator<TargetMethodInfo> iterator() {
        MethodInfoManager methodInfoManager = MethodInfoManager.getInstance();
        SortedSet<TargetMethodInfo> methodInfos = methodInfoManager.getTargetMethodInfos();
        return methodInfos.iterator();
    }

    /**
     * 対象メソッドの数を返すメソッド.
     * 
     * @return 対象メソッドの数
     */
    public int getMethodCount() {
        return MethodInfoManager.getInstance().getTargetMethodCount();
    }

}
