package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.HashSet;
import java.util.Set;



/**
 * 外部クラスに定義されているフィールドの情報を保存するためのクラス．
 * 
 * @author higo
 */
public final class ExternalFieldInfo extends FieldInfo {

    /**
     * 名前と定義しているクラス情報を与えて初期化． 型は不明．
     * 
     * @param name フィールド名
     * @param definitionClass フィールドを定義しているクラス
     */
    public ExternalFieldInfo(final String name, final ClassInfo definitionClass) {
        super(new HashSet<ModifierInfo>(), name, UnknownTypeInfo.getInstance(), definitionClass, 0,
                0, 0, 0);
    }

    /**
     * 名前を与えて初期化．定義しているクラスが不明な場合に用いる．
     * 
     * @param name フィールド名
     */
    public ExternalFieldInfo(final String name) {
        super(new HashSet<ModifierInfo>(), name, UnknownTypeInfo.getInstance(),
                ExternalClassInfo.UNKNOWN, 0, 0, 0, 0);
    }

    /**
     * ExternalFieldInfo では利用できない
     */
    @Override
    public int getFromLine() {
        throw new CannotUseException();
    }

    /**
     * ExternalFieldInfo では利用できない
     */
    @Override
    public int getFromColumn() {
        throw new CannotUseException();
    }

    /**
     * ExternalFieldInfo では利用できない
     */
    @Override
    public int getToLine() {
        throw new CannotUseException();
    }

    /**
     * ExternalFieldInfo では利用できない
     */
    @Override
    public int getToColumn() {
        throw new CannotUseException();
    }

    /**
     * ExternalFieldInfo では利用できない
     */
    @Override
    public Set<ModifierInfo> getModifiers() {
        throw new CannotUseException();
    }
}
