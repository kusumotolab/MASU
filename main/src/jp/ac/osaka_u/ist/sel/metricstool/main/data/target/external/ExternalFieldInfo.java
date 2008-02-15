package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external;


import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;


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
     * @param ownerClass フィールドを定義しているクラス
     */
    public ExternalFieldInfo(final String name, final ClassInfo ownerClass) {
        super(new HashSet<ModifierInfo>(), name, UnknownTypeInfo.getInstance(), ownerClass, 0, 0,
                0, 0);
    }

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
