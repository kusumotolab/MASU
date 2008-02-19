package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 一度目のASTパースで取得したメソッド情報を一時的に格納するためのクラス．
 * 
 * 
 * @author higo
 * 
 */
public final class UnresolvedMethodInfo extends UnresolvedCallableUnitInfo<TargetMethodInfo>
        implements MemberSetting {

    /**
     * 未解決メソッド定義情報オブジェクトを初期化
     * @param ownerClass このメソッドを宣言しているクラス
     */
    public UnresolvedMethodInfo(final UnresolvedClassInfo ownerClass) {
        super(ownerClass);

        this.methodName = null;
        this.returnType = null;
        this.instance = true;
        this.resolvedInfo = null;
    }

    /**
     * この未解決メソッド情報が解決されているかどうかを返す
     * 
     * @return 解決済みの場合は true，そうでない場合は false
     */
    @Override
    public boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * 解決済みメソッド情報を返す
     * 
     * @return 解決済みメソッド情報
     * @throws まだ解決されていない場合にスローされる
     */
    @Override
    public TargetMethodInfo getResolvedUnit() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }

    /**
     * 未解決メソッド情報を解決し，解決済み参照を返す．
     * 
     * @param usingClass 未解決メソッド情報の定義があるクラス
     * @param usingMethod 未解決メソッド情報の定義があるメソッド（このメソッドが呼ばれる場合は通常 null がセットされているはず）
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     * @return 解決済みメソッド情報
     */
    @Override
    public TargetMethodInfo resolveUnit(final TargetClassInfo usingClass,
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

        // 修飾子，名前，返り値，行数，コンストラクタかどうか，可視性，インスタンスメンバーかどうかを取得
        final Set<ModifierInfo> methodModifiers = this.getModifiers();
        final String methodName = this.getMethodName();

        final boolean privateVisible = this.isPrivateVisible();
        final boolean namespaceVisible = this.isNamespaceVisible();
        final boolean inheritanceVisible = this.isInheritanceVisible();
        final boolean publicVisible = this.isPublicVisible();
        final boolean instance = this.isInstanceMember();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // MethodInfo オブジェクトを生成する．
        this.resolvedInfo = new TargetMethodInfo(methodModifiers, methodName, usingClass,
                privateVisible, namespaceVisible, inheritanceVisible, publicVisible, instance,
                fromLine, fromColumn, toLine, toColumn);

        // 型パラメータを解決し，解決済みメソッド情報に追加する
        for (final UnresolvedTypeParameterInfo unresolvedTypeParameter : this.getTypeParameters()) {

            final TypeParameterInfo typeParameter = (TypeParameterInfo) unresolvedTypeParameter
                    .resolveType(usingClass, this.resolvedInfo, classInfoManager, fieldInfoManager,
                            methodInfoManager);
            this.resolvedInfo.addTypeParameter(typeParameter);
        }

        // 返り値をセットする
        final UnresolvedTypeInfo unresolvedMethodReturnType = this.getReturnType();
        TypeInfo methodReturnType = unresolvedMethodReturnType.resolveType(usingClass, null,
                classInfoManager, fieldInfoManager, methodInfoManager);
        assert methodReturnType != null : "resolveTypeInfo returned null!";
        if (methodReturnType instanceof UnknownTypeInfo) {
            if (unresolvedMethodReturnType instanceof UnresolvedClassReferenceInfo) {

                final ExternalClassInfo classInfo = NameResolver
                        .createExternalClassInfo((UnresolvedClassReferenceInfo) unresolvedMethodReturnType);
                methodReturnType = new ClassTypeInfo(classInfo);
                for (final UnresolvedTypeInfo unresolvedTypeArgument : ((UnresolvedClassReferenceInfo) unresolvedMethodReturnType)
                        .getTypeArguments()) {
                    final TypeInfo typeArgument = unresolvedTypeArgument.resolveType(usingClass,
                            usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                    ((ClassTypeInfo) methodReturnType).addTypeArgument(typeArgument);
                }
                classInfoManager.add(classInfo);

            } else if (unresolvedMethodReturnType instanceof UnresolvedArrayTypeInfo) {

                // TODO 型パラメータの情報を格納する
                final UnresolvedTypeInfo unresolvedElementType = ((UnresolvedArrayTypeInfo) unresolvedMethodReturnType)
                        .getElementType();
                final int dimension = ((UnresolvedArrayTypeInfo) unresolvedMethodReturnType)
                        .getDimension();
                final TypeInfo elementType = unresolvedElementType.resolveType(usingClass,
                        usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                methodReturnType = ArrayTypeInfo.getType(elementType, dimension);

            } else {
                assert false : "Can't resolve method return type : "
                        + unresolvedMethodReturnType.toString();
            }
        }
        this.resolvedInfo.setReturnType(methodReturnType);

        // 引数を追加する
        for (final UnresolvedParameterInfo unresolvedParameterInfo : this.getParameters()) {

            final TargetParameterInfo parameterInfo = unresolvedParameterInfo.resolveUnit(
                    usingClass, this.resolvedInfo, classInfoManager, fieldInfoManager,
                    methodInfoManager);
            this.resolvedInfo.addParameter(parameterInfo);
        }

        //　内部ブロック情報を解決し，解決済みcaseエントリオブジェクトに追加
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

    /**
     * メソッド名を返す
     * 
     * @return メソッド名
     */
    public String getMethodName() {
        return this.methodName;
    }

    /**
     * メソッド名をセットする
     * 
     * @param methodName メソッド名
     */
    public void setMethodName(final String methodName) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == methodName) {
            throw new NullPointerException();
        }

        this.methodName = methodName;
    }

    /**
     * メソッドの返り値の型を返す
     * 
     * @return メソッドの返り値の型
     */
    public UnresolvedTypeInfo getReturnType() {
        return this.returnType;
    }

    /**
     * メソッドの返り値をセットする
     * 
     * @param returnType メソッドの返り値
     */
    public void setReturnType(final UnresolvedTypeInfo returnType) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == returnType) {
            throw new NullPointerException();
        }

        this.returnType = returnType;
    }

    /**
     * インスタンスメンバーかどうかを返す
     * 
     * @return インスタンスメンバーの場合 true，そうでない場合 false
     */
    public boolean isInstanceMember() {
        return this.instance;
    }

    /**
     * スタティックメンバーかどうかを返す
     * 
     * @return スタティックメンバーの場合 true，そうでない場合 false
     */
    public boolean isStaticMember() {
        return !this.instance;
    }

    /**
     * インスタンスメンバーかどうかをセットする
     * 
     * @param instance インスタンスメンバーの場合は true， スタティックメンバーの場合は false
     */
    public void setInstanceMember(final boolean instance) {
        this.instance = instance;
    }

    /**
     * メソッド名を保存するための変数
     */
    private String methodName;

    /**
     * メソッドの返り値を保存するための変数
     */
    private UnresolvedTypeInfo returnType;

    /**
     * インスタンスメンバーかどうかを保存するための変数
     */
    private boolean instance;

    /**
     * 名前解決された情報を格納するための変数
     */
    private TargetMethodInfo resolvedInfo;
}
