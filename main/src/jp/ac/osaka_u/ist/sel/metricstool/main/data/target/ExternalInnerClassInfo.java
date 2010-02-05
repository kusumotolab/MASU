package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


@SuppressWarnings("serial")
public class ExternalInnerClassInfo extends ExternalClassInfo implements
        InnerClassInfo<ExternalClassInfo> {

    public ExternalInnerClassInfo(final String[] fullQualifiedName, final UnitInfo outerUnit) {
        super(fullQualifiedName);

        if (null == outerUnit) {
            throw new IllegalArgumentException();
        }
        this.outerUnit = outerUnit;
    }

    /**
     * 外側のユニットを返す
     * 
     * @return 外側のユニット
     */
    @Override
    public final UnitInfo getOuterUnit() {
        return this.outerUnit;
    }

    /**
     * 外側のクラスを返す.
     * つまり，getOuterUnit の返り値がTargetClassInfoである場合は，そのオブジェクトを返し，
     * 返り値が，TargetMethodInfoである場合は，そのオブジェクトの ownerClass を返す．
     * 
     * @return　外側のクラス
     */
    @Override
    public final ExternalClassInfo getOuterClass() {

        final UnitInfo unitInfo = this.getOuterUnit();

        // 外側のユニットがクラスであればそのまま返す
        if (unitInfo instanceof ExternalClassInfo) {
            return (ExternalClassInfo) unitInfo;

            // 外側のユニットがメソッドであれば，その所有クラスを返す
        } else if (unitInfo instanceof ExternalMethodInfo) {

            final ClassInfo<?, ?, ?, ?> ownerClass = ((TargetMethodInfo) unitInfo).getOwnerClass();
            return (ExternalClassInfo) ownerClass;
        }

        assert false : "here shouldn't be reached!";
        return null;
    }

    /**
     * 外側のユニットのオブジェクトを保存する変数
     */
    private final UnitInfo outerUnit;

}
