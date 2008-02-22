package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public final class UnresolvedConstructorInfo extends
        UnresolvedCallableUnitInfo<TargetConstructorInfo> {

    public UnresolvedConstructorInfo(final UnresolvedClassInfo ownerClass) {

        super(ownerClass);
    }

    @Override
    public boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    @Override
    public TargetConstructorInfo getResolvedUnit() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }

    @Override
    public TargetConstructorInfo resolveUnit(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == classInfoManager) || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolvedUnit();
        }

        // 修飾子，名前，返り値，行数，可視性を取得
        final Set<ModifierInfo> methodModifiers = this.getModifiers();
        final boolean privateVisible = this.isPrivateVisible();
        final boolean namespaceVisible = this.isNamespaceVisible();
        final boolean inheritanceVisible = this.isInheritanceVisible();
        final boolean publicVisible = this.isPublicVisible();

        final int constructorFromLine = this.getFromLine();
        final int constructorFromColumn = this.getFromColumn();
        final int constructorToLine = this.getToLine();
        final int constructorToColumn = this.getToColumn();

        // MethodInfo オブジェクトを生成する．
        this.resolvedInfo = new TargetConstructorInfo(methodModifiers, usingClass, privateVisible,
                namespaceVisible, inheritanceVisible, publicVisible, constructorFromLine,
                constructorFromColumn, constructorToLine, constructorToColumn);

        // 型パラメータを解決し，解決済みコンストラクタ情報に追加する
        for (final UnresolvedTypeParameterInfo unresolvedTypeParameter : this.getTypeParameters()) {

            final TypeParameterInfo typeParameter = (TypeParameterInfo) unresolvedTypeParameter
                    .resolveType(usingClass, this.resolvedInfo, classInfoManager, null, null);
            this.resolvedInfo.addTypeParameter(typeParameter);
        }

        // 引数を解決し，解決済みコンストラクタ情報に追加する
        for (final UnresolvedParameterInfo unresolvedParameterInfo : this.getParameters()) {

            final TargetParameterInfo parameterInfo = unresolvedParameterInfo.resolveUnit(
                    usingClass, this.resolvedInfo, classInfoManager, fieldInfoManager,
                    methodInfoManager);
            this.resolvedInfo.addParameter(parameterInfo);
        }

        //　内部ブロック情報を解決し，解決済みコンストラクタオブジェクトに追加
        for (final UnresolvedBlockInfo<?> unresolvedInnerBlock : this.getInnerBlocks()) {
            final BlockInfo innerBlock = unresolvedInnerBlock.resolveUnit(usingClass,
                    this.resolvedInfo, classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addInnerBlock(innerBlock);
        }

        // メソッド内で定義されている各未解決ローカル変数に対して
        for (final UnresolvedLocalVariableInfo unresolvedLocalVariable : this.getLocalVariables()) {

            final LocalVariableInfo localVariable = unresolvedLocalVariable.resolveUnit(usingClass,
                    this.resolvedInfo, classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addLocalVariable(localVariable);
        }

        return this.resolvedInfo;
    }

    public boolean isInstanceMember() {
        return true;
    }

    public boolean isStaticMember() {
        return false;
    }

    public void setInstanceMember(boolean instance) {
    }

    /**
     * 名前解決された情報を格納するための変数
     */
    private TargetConstructorInfo resolvedInfo;
}
