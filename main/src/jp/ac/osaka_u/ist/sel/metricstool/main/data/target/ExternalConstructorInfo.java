package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * 外部クラスに定義されているコンストラクタ情報を保存するためのクラス
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public final class ExternalConstructorInfo extends ConstructorInfo {

    /**
     * 外部クラスに定義されているコンストラクタオブジェクトを初期化する
     */
    public ExternalConstructorInfo(final Set<ModifierInfo> modifiers, final boolean privateVisible,
            final boolean namespaceVisible, final boolean inheritanceVisible,
            final boolean publicVisible) {
        super(modifiers, privateVisible, namespaceVisible, inheritanceVisible, publicVisible,
                getDummyPosition(), getDummyPosition(), getDummyPosition(), getDummyPosition());
    }

    /**
     * 外部クラスに定義されているコンストラクタオブジェクトを初期化する
     */
    public ExternalConstructorInfo() {
        super(new HashSet<ModifierInfo>(), false, true, true, true, getDummyPosition(),
                getDummyPosition(), getDummyPosition(), getDummyPosition());
    }

    /**
     * 空のSortedSetを返す
     */
    @Override
    public SortedSet<StatementInfo> getStatements() {
        return Collections.unmodifiableSortedSet(new TreeSet<StatementInfo>());
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
