package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * 外部クラスに定義されているメソッド情報を保存するためのクラス
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public final class ExternalMethodInfo extends MethodInfo<ExternalClassInfo> {

    /**
     * 外部クラスに定義されているメソッドオブジェクトを初期化する
     * アクセス制御子まで分かっている場合
     *  
     * @param methodName メソッド名
     * @param ownerClass このメソッドを定義しているクラス
     */
    public ExternalMethodInfo(final Set<ModifierInfo> modifiers, final String methodName,
            final ExternalClassInfo ownerClass, final boolean privateVisible,
            final boolean namespaceVisible, final boolean inheritanceVisible,
            final boolean publicVisible, final boolean instance) {

        super(modifiers, methodName, ownerClass, privateVisible, namespaceVisible,
                inheritanceVisible, publicVisible, instance, new Random().nextInt(), new Random()
                        .nextInt(), new Random().nextInt(), new Random().nextInt());

        this.setReturnType(UnknownTypeInfo.getInstance());
    }

    /**
     * 外部クラスに定義されているメソッドオブジェクトを初期化する
     * 
     * @param methodName メソッド名
     * @param ownerClass このメソッドを定義しているクラス
     */
    public ExternalMethodInfo(final String methodName, final ExternalClassInfo ownerClass) {

        super(new HashSet<ModifierInfo>(), methodName, ownerClass, false, true, true, true, true,
                new Random().nextInt(), new Random().nextInt(), new Random().nextInt(),
                new Random().nextInt());

        this.setReturnType(UnknownTypeInfo.getInstance());
    }

    /**
     * 外部クラスに定義されているメソッドオブジェクトを初期化する．
     * 定義しているクラスが不明な場合に用いるコンストラクタ
     * 
     * @param methodName メソッド名
     */
    public ExternalMethodInfo(final String methodName) {

        super(new HashSet<ModifierInfo>(), methodName, ExternalClassInfo.UNKNOWN, false, true,
                true, true, true, new Random().nextInt(), new Random().nextInt(), new Random()
                        .nextInt(), new Random().nextInt());
        this.setReturnType(UnknownTypeInfo.getInstance());
    }

    /**
     * ExternalMethodInfoでは中身はない
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
