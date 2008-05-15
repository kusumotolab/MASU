package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external;


import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
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
     */
    public ExternalMethodInfo(final String methodName, final ClassInfo ownerClass) {

        super(new HashSet<ModifierInfo>(), methodName, ownerClass, false, true, true, true, 0, 0,
                0, 0);

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
                true, true, 0, 0, 0, 0);
        this.setReturnType(UnknownTypeInfo.getInstance());
    }

    /**
     * ExternalMethodInfoでは利用できない
     */
    @Override
    public Set<CallInfo> getCalls() {
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
    public SortedSet<StatementInfo> getStatements() {
        throw new CannotUseException();
    }

    /**
     * ExternalMethodInfo では利用できない
     */
    @Override
    public int getFromLine() {
        throw new CannotUseException();
    }

    /**
     * ExternalMethodInfo では利用できない
     */
    @Override
    public int getFromColumn() {
        throw new CannotUseException();
    }

    /**
     * ExternalMethodInfo では利用できない
     */
    @Override
    public int getToLine() {
        throw new CannotUseException();
    }

    /**
     * ExternalMethodInfo では利用できない
     */
    @Override
    public int getToColumn() {
        throw new CannotUseException();
    }

    /**
     * ExternalMethodInfo では利用できない
     */
    @Override
    public final boolean isInheritanceVisible() {
        throw new CannotUseException();
    }

    /**
     * ExternalMethodInfo では利用できない
     */
    @Override
    public final boolean isNamespaceVisible() {
        throw new CannotUseException();
    }

    /**
     * ExternalMethodInfo では利用できない
     */
    @Override
    public final boolean isPrivateVisible() {
        throw new CannotUseException();
    }

    /**
     * ExternalMethodInfo では利用できない
     */
    @Override
    public final boolean isPublicVisible() {
        throw new CannotUseException();
    }

    /**
     * ExternalMethodInfo では利用できない
     */
    @Override
    public Set<ModifierInfo> getModifiers() {
        throw new CannotUseException();
    }
}
