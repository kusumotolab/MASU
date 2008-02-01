package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * Unresolvedな変数の共通な親クラス.
 * <ul>
 * <li>変数名</li>
 * <li>型</li>
 * <li>修飾子</li>
 * <li>位置情報</li>
 * </ul>
 * 
 * @author higo
 * 
 */
public abstract class UnresolvedVariableInfo<T extends VariableInfo> extends UnresolvedUnitInfo<T> {

    /**
     * 変数名を返す
     * 
     * @return 変数名
     */
    public final String getName() {
        return this.name;
    }

    /**
     * 変数名をセットする
     * 
     * @param name 変数名
     */
    public final void setName(final String name) {

        if (null == name) {
            throw new NullPointerException();
        }

        this.name = name;
    }

    /**
     * 変数の型を返す
     * 
     * @return 変数の型
     */
    public final UnresolvedTypeInfo getType() {
        return this.type;
    }

    /**
     * 変数の型をセットする
     * 
     * @param type 変数の型
     */
    public final void setType(final UnresolvedTypeInfo type) {

        if (null == type) {
            throw new NullPointerException();
        }

        this.type = type;
    }

    /**
     * 修飾子の Set を返す
     * 
     * @return 修飾子の Set
     */
    public final Set<ModifierInfo> getModifiers() {
        return Collections.unmodifiableSet(this.modifiers);
    }

    /**
     * 修飾子を追加する
     * 
     * @param modifier 追加する修飾子
     */
    public final void addModifiar(final ModifierInfo modifier) {

        if (null == modifier) {
            throw new NullPointerException();
        }

        this.modifiers.add(modifier);
    }

    /**
     * 名前解決された情報を返す
     * 
     * @return 名前解決された情報
     */
    @Override
    public final T getResolvedUnit() {
        return this.resolvedInfo;
    }

    /**
     * 既に名前解決されたかどうかを返す
     * 
     * @return 名前解決されている場合は true，そうでない場合は false
     */
    @Override
    public final boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * 変数オブジェクトを初期化する．
     * 
     * @param name 変数名
     * @param type 変数の型
     */
    UnresolvedVariableInfo(final String name, final UnresolvedTypeInfo type) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == name) || (null == type)) {
            throw new NullPointerException();
        }

        this.name = name;
        this.type = type;
        this.modifiers = new HashSet<ModifierInfo>();
    }

    /**
     * 変数オブジェクトを初期化する．
     */
    UnresolvedVariableInfo() {

        MetricsToolSecurityManager.getInstance().checkAccess();
        this.name = null;
        this.type = null;
        this.modifiers = new HashSet<ModifierInfo>();

        this.resolvedInfo = null;
    }

    /**
     * 変数名を表す変数
     */
    private String name;

    /**
     * 変数の型を表す変数
     */
    private UnresolvedTypeInfo type;

    /**
     * このフィールドの修飾子を保存するための変数
     */
    private Set<ModifierInfo> modifiers;

    /**
     * 名前解決された情報を格納するための変数
     */
    protected T resolvedInfo;
}
