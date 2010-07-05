package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


@SuppressWarnings("serial")
public class ExternalInnerClassInfo extends ExternalClassInfo implements InnerClassInfo {

    public ExternalInnerClassInfo(final String[] fullQualifiedName, final UnitInfo outerUnit) {
        super(fullQualifiedName);
        this.outerUnit = outerUnit;
    }

    public ExternalInnerClassInfo(final Set<ModifierInfo> modifiers,
            final String[] fullQualifiedName, final boolean privateVisible,
            final boolean namespaceVisible, final boolean inheritanceVisible,
            final boolean publicVisible, final boolean instance, final boolean isInterface) {

        super(modifiers, fullQualifiedName, privateVisible, namespaceVisible, inheritanceVisible,
                publicVisible, instance, isInterface);
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
     * 外側のユニットを設定する
     * 
     * @param outerUnit 外側のユニット
     */
    public void setOuterUnit(final UnitInfo outerUnit) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == outerUnit) {
            throw new IllegalArgumentException();
        }

        this.outerUnit = outerUnit;
    }

    /**
     * 外側のクラスを返す.
     * つまり，getOuterUnit の返り値がTargetClassInfoである場合は，そのオブジェクトを返し，
     * 返り値が，TargetMethodInfoである場合は，そのオブジェクトの ownerClass を返す．
     * 
     * @return　外側のクラス
     */
    @Override
    public final ClassInfo getOuterClass() {

        final UnitInfo unitInfo = this.getOuterUnit();

        // 外側のユニットがクラスであればそのまま返す
        if (unitInfo instanceof ExternalClassInfo) {
            return (ExternalClassInfo) unitInfo;

            // 外側のユニットがメソッドであれば，その所有クラスを返す
        } else if (unitInfo instanceof ExternalMethodInfo) {

            final ClassInfo ownerClass = ((ExternalMethodInfo) unitInfo).getOwnerClass();
            return (ExternalClassInfo) ownerClass;
        }

        assert false : "here shouldn't be reached!";
        return null;
    }

    @Override
    public TypeParameterizable getOuterTypeParameterizableUnit() {
        return (TypeParameterizable) this.getOuterUnit();
    }

    /**
     * 外側のユニットのオブジェクトを保存する変数
     */
    private UnitInfo outerUnit;

}
