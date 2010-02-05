package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


public interface InnerClassInfo<T extends ClassInfo<?, ?, ?, ?>> {

    /**
     * 外側のユニットを返す
     * 
     * @return 外側のユニット
     */
    UnitInfo getOuterUnit();

    /**
     * 外側のクラスを返す.
     * つまり，getOuterUnit の返り値がClassInfoである場合は，そのオブジェクトを返し，
     * 返り値が，MethodInfoである場合は，そのオブジェクトの ownerClass を返す．
     * 
     * @return　外側のクラス
     */
    T getOuterClass();
}
