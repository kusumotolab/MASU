package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * 外部クラス情報を表すクラス
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public class ExternalClassInfo
        extends
        ClassInfo<ExternalFieldInfo, ExternalMethodInfo, ExternalConstructorInfo, ExternalInnerClassInfo> {

    /**
     * 名前空間名とクラス名を与えて，オブジェクトを初期化
     * 
     * @param namespace 名前空間名
     * @param className クラス名
     */
    public ExternalClassInfo(final NamespaceInfo namespace, final String className) {

        super(new HashSet<ModifierInfo>(), namespace, className, false, true, true, true, true,
                false, 0, 0, 0, 0);
    }

    /**
     * 完全限定名とアクセス制御子を与えて，クラス情報オブジェクトを初期化
     * 
     * @param fullQualifiedName 完全限定名
     */
    public ExternalClassInfo(final String[] fullQualifiedName, final boolean privateVisible,
            final boolean namespaceVisible, final boolean inheritanceVisible,
            final boolean publicVisible, final boolean instance, final boolean isInterface) {

        super(new HashSet<ModifierInfo>(), fullQualifiedName, privateVisible, namespaceVisible,
                inheritanceVisible, publicVisible, instance, isInterface, 0, 0, 0, 0);
    }

    /**
     * 完全限定名を与えて，クラス情報オブジェクトを初期化
     * 
     * @param fullQualifiedName 完全限定名
     */
    public ExternalClassInfo(final String[] fullQualifiedName) {

        super(new HashSet<ModifierInfo>(), fullQualifiedName, false, true, true, true, true, false,
                0, 0, 0, 0);
    }

    /**
     * 名前空間が不明な外部クラスのオブジェクトを初期化
     * 
     * @param className クラス名
     */
    public ExternalClassInfo(final String className) {

        super(new HashSet<ModifierInfo>(), NamespaceInfo.UNKNOWN, className, false, true, true,
                true, true, false, 0, 0, 0, 0);
    }

    /**
     * ExternalClassInfo では利用できない
     */
    @Override
    public void addTypeParameterUsage(TypeParameterInfo typeParameterInfo, TypeInfo usedType) {
        throw new CannotUseException();
    }

    /**
     * ExternalClassInfo では利用できない
     */
    @Override
    public Map<TypeParameterInfo, TypeInfo> getTypeParameterUsages() {
        throw new CannotUseException();
    }

    /**
     * ExternalClassInfo では利用できない
     */
    @Override
    public final Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        throw new CannotUseException();
    }

    /**
     * ExternalClassInfo では利用できない
     */
    @Override
    public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        throw new CannotUseException();
    }

    /**
     * ExternalClassInfo では利用できない
     */
    @Override
    public Set<CallInfo<? extends CallableUnitInfo>> getCalls() {
        throw new CannotUseException();
    }

    /**
     * 不明な外部クラスを表すための定数
     */
    public static final ExternalClassInfo UNKNOWN = new ExternalClassInfo("UNKNOWN");
}
