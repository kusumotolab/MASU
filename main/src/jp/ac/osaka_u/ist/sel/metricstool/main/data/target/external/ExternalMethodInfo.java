package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external;


import java.util.Set;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MemberCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;


/**
 * 外部クラスに定義されているメソッド情報を保存するためのクラス
 * 
 * @author higo
 */
public final class ExternalMethodInfo extends MethodInfo {

    /**
     * 外部クラスに定義されているメソッドオブジェクトを初期化する
     * 
     * @param methodName メソッド名
     * @param ownerClass このメソッドを定義しているクラス
     * @param constructor コンストラクタかどうか
     */
    public ExternalMethodInfo(final String methodName, final ClassInfo ownerClass,
            final boolean constructor) {

        super(methodName, ownerClass, constructor);

        this.setReturnType(UnknownTypeInfo.getInstance());
    }

    /**
     * ExternalMethodInfoでは利用できない
     */
    @Override
    public Set<MemberCallInfo> getMemberCalls() {
        throw new CannotUseException();
    }

    /**
     * ExternalMethodInfoでは利用できない
     */
    @Override
    public final SortedSet<MethodInfo> getCallees() {
        throw new CannotUseException();
    }

    /**
     * ExternalMethodInfoでは利用できない
     */
    @Override
    public SortedSet<LocalVariableInfo> getLocalVariables() {
        throw new CannotUseException();
    }

    /**
     * ExternalMethodInfoでは利用できない
     */
    @Override
    public Set<FieldUsageInfo> getFieldUsages() {
        throw new CannotUseException();
    }

    /**
     * ExternalMethodInfoでは利用できない
     */
    @Override
    public SortedSet<BlockInfo> getInnerBlocks() {
        throw new CannotUseException();
    }
}
