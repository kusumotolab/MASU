package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;


/**
 * 外部クラスに定義されているコンストラクタ情報を保存するためのクラス
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public final class ExternalConstructorInfo extends ConstructorInfo {

    /**
     * 外部クラスに定義されているコンストラクタオブジェクトを初期化する
     * 
     * @param ownerClass このメソッドを定義しているクラス
     */
    public ExternalConstructorInfo(final Set<ModifierInfo> modifiers,
            final ExternalClassInfo ownerClass, final boolean privateVisible,
            final boolean namespaceVisible, final boolean inheritanceVisible,
            final boolean publicVisible) {
        super(modifiers, ownerClass, privateVisible, namespaceVisible, inheritanceVisible,
                publicVisible, 0, 0, 0, 0);
    }

    /**
     * 外部クラスに定義されているコンストラクタオブジェクトを初期化する
     * 
     * @param ownerClass このメソッドを定義しているクラス
     */
    public ExternalConstructorInfo(final ExternalClassInfo ownerClass) {
        super(new HashSet<ModifierInfo>(), ownerClass, false, true, true, true, 0, 0, 0, 0);
    }

    /**
     * ExternalConstructorInfoでは利用できない
     */
    @Override
    public SortedSet<StatementInfo> getStatements() {
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
