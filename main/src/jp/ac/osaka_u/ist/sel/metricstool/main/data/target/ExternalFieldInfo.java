package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.HashSet;
import java.util.Random;
import java.util.Set;


/**
 * 外部クラスに定義されているフィールドの情報を保存するためのクラス．
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public final class ExternalFieldInfo extends FieldInfo {

    /**
     * 名前，型，定義しているクラス情報を与えて初期化． 
     * 
     * @param name フィールド名
     * @param type 型
     * @param definitionClass フィールドを定義しているクラス
     */
    public ExternalFieldInfo(final Set<ModifierInfo> modifiers, final String name,
            final TypeInfo type, final ExternalClassInfo definitionClass,
            final boolean privateVisible, final boolean namespaceVisible,
            final boolean inheritanceVisible, final boolean publicVisible, final boolean instance) {
        super(modifiers, name, type, definitionClass, privateVisible, namespaceVisible,
                inheritanceVisible, publicVisible, instance, new Random().nextInt(), new Random()
                        .nextInt(), new Random().nextInt(), new Random().nextInt());
    }

    /**
     * 名前と定義しているクラス情報を与えて初期化． 型は不明．
     * 
     * @param name フィールド名
     * @param definitionClass フィールドを定義しているクラス
     */
    public ExternalFieldInfo(final String name, final ExternalClassInfo definitionClass) {
        super(new HashSet<ModifierInfo>(), name, UnknownTypeInfo.getInstance(), definitionClass,
                true, true, true, true, true, new Random().nextInt(), new Random().nextInt(),
                new Random().nextInt(), new Random().nextInt());
    }

    /**
     * 名前を与えて初期化．定義しているクラスが不明な場合に用いる．
     * 
     * @param name フィールド名
     */
    public ExternalFieldInfo(final String name) {
        super(new HashSet<ModifierInfo>(), name, UnknownTypeInfo.getInstance(),
                ExternalClassInfo.UNKNOWN, true, true, true, true, true, new Random().nextInt(),
                new Random().nextInt(), new Random().nextInt(), new Random().nextInt());
    }
}
