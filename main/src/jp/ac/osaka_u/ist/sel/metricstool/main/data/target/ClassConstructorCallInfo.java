package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


public class ClassConstructorCallInfo extends ConstructorCallInfo<ClassTypeInfo> {

    /**
     * 型を与えてコンストラクタ呼び出しを初期化
     * 
     * @param classType 呼び出しの型
     * @param ownerMethod オーナーメソッド 
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列 
     */
    public ClassConstructorCallInfo(final ClassTypeInfo classType, final ConstructorInfo callee,
            final CallableUnitInfo ownerMethod, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {
        super(classType, callee, ownerMethod, fromLine, fromColumn, toLine, toColumn);

    }
}
