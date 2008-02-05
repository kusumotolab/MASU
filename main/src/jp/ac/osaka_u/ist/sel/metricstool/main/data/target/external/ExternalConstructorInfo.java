package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external;


import java.util.Set;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;


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
        super(ownerClass, 0, 0, 0, 0);
    }

    /**
     * ExternalConstructorInfoでは利用できない
     */
    @Override
    public Set<CallInfo> getCalls() {
        throw new CannotUseException();
    }

    /**
     * ExternalConstructorInfoでは利用できない
     */
    @Override
    public final SortedSet<MethodInfo> getCallees() {
        throw new CannotUseException();
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
    public SortedSet<BlockInfo> getInnerBlocks() {
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
}
