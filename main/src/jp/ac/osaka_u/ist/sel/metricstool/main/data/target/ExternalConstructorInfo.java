package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;


/**
 * 外部クラスに定義されているコンストラクタ情報を保存するためのクラス
 * 
 * @author higo
 */
public final class ExternalConstructorInfo extends ConstructorInfo {

    /**
     * 外部クラスに定義されているコンストラクタオブジェクトを初期化する
     * 
     * @param ownerClass このメソッドを定義しているクラス
     */
    public ExternalConstructorInfo(final ClassInfo ownerClass) {
        super(new HashSet<ModifierInfo>(), ownerClass, false, true, true, true, 0, 0, 0, 0);
    }

    /**
     * ExternalConstructorInfoでは利用できない
     */
    @Override
    public SortedSet<LocalVariableInfo> getLocalVariables() {
        throw new CannotUseException();
    }

    /**
     * ExternalConstructorInfoでは利用できない
     */
    @Override
    public Set<FieldUsageInfo> getFieldUsages() {
        throw new CannotUseException();
    }

    /**
     * ExternalConstructorInfoでは利用できない
     */
    @Override
    public SortedSet<StatementInfo> getStatements() {
        throw new CannotUseException();
    }

    /**
     * ExternalConstructorInfo では利用できない
     */
    @Override
    public int getFromLine() {
        throw new CannotUseException();
    }

    /**
     * ExternalConstructorInfo では利用できない
     */
    @Override
    public int getFromColumn() {
        throw new CannotUseException();
    }

    /**
     * ExternalConstructorInfo では利用できない
     */
    @Override
    public int getToLine() {
        throw new CannotUseException();
    }

    /**
     * ExternalConstructorInfo では利用できない
     */
    @Override
    public int getToColumn() {
        throw new CannotUseException();
    }

    /**
     * ExternalConstructorInfo では利用できない
     */
    @Override
    public final boolean isInheritanceVisible() {
        throw new CannotUseException();
    }

    /**
     * ExternalConstructorInfo では利用できない
     */
    @Override
    public final boolean isNamespaceVisible() {
        throw new CannotUseException();
    }

    /**
     * ExternalConstructorInfo では利用できない
     */
    @Override
    public final boolean isPrivateVisible() {
        throw new CannotUseException();
    }

    /**
     * ExternalConstructorInfo では利用できない
     */
    @Override
    public final boolean isPublicVisible() {
        throw new CannotUseException();
    }

    /**
     * ExternalConstructorInfo では利用できない
     */
    @Override
    public Set<ModifierInfo> getModifiers() {
        throw new CannotUseException();
    }

    /**
     * ExternalClassInfo では利用できない
     */
    @Override
    public TypeParameterInfo getTypeParameter(int index) {
        throw new CannotUseException();
    }

    /**
     * ExternalClassInfo では利用できない
     */
    @Override
    public List<TypeParameterInfo> getTypeParameters() {
        throw new CannotUseException();
    }

    /**
     * ExternalClassInfo では利用できない
     */
    @Override
    public void addTypeParameter(TypeParameterInfo typeParameter) {
        throw new CannotUseException();
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
}
