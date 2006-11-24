package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import java.util.Iterator;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;


/**
 * プラグインが ClassInfo にアクセスするために用いるインターフェース
 * 
 * @author y-higo
 *
 */
public final class DefaultClassInfoAccessor implements ClassInfoAccessor {

    /**
     * ClassInfo のイテレータを返す． このイテレータは参照専用であり変更処理を行うことはできない．
     * 
     * @return ClassInfo のイテレータ
     */
    public Iterator<ClassInfo> classInfoIterator() {
        ClassInfoManager classInfoManager = ClassInfoManager.getInstance();
        return classInfoManager.iterator();
    }

}
