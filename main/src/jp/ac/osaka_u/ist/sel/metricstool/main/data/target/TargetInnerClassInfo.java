package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Arrays;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * インナークラスを表すクラス．
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public class TargetInnerClassInfo extends TargetClassInfo implements InnerClassInfo {

    public static ClassInfo getOutestClass(final InnerClassInfo innerClass) {

        if (null == innerClass) {
            throw new IllegalArgumentException();
        }

        final String[] fqName = ((ClassInfo) innerClass).getFullQualifiedName();
        final String[] outerFQName = Arrays.copyOf(fqName, fqName.length - 1);

        final ClassInfo outerClass = DataManager.getInstance().getClassInfoManager().getClassInfo(
                outerFQName);
        return outerClass instanceof InnerClassInfo ? getOutestClass((InnerClassInfo) outerClass)
                : outerClass;
    }

    /**
     * インナークラスオブジェクトを初期化する
     * 
     * @param modifiers 修飾子名の Set
     * @param namespace 名前空間
     * @param className クラス名
     * @param privateVisible クラス内からのみ参照可能
     * @param namespaceVisible 同じ名前空間から参照可能
     * @param inheritanceVisible 子クラスから参照可能
     * @param publicVisible どこからでも参照可能
     * @param instance インスタンスメンバーかどうか
     * @param isInterface インタフェースかどうか
     * @param fileInfo このクラスを宣言しているファイル情報
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public TargetInnerClassInfo(final Set<ModifierInfo> modifiers, final NamespaceInfo namespace,
            final String className, final boolean privateVisible, final boolean namespaceVisible,
            final boolean inheritanceVisible, final boolean publicVisible, final boolean instance,
            final boolean isInterface, final FileInfo fileInfo, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(modifiers, namespace, className, privateVisible, namespaceVisible,
                inheritanceVisible, publicVisible, instance, isInterface, fileInfo, fromLine,
                fromColumn, toLine, toColumn);

        this.outerUnit = null;
    }

    /**
     * インナークラスオブジェクトを初期化する
     * 
     * @param modifiers 修飾子名の Set
     * @param fullQualifiedName 完全限定名
     * @param privateVisible クラス内からのみ参照可能
     * @param namespaceVisible 同じ名前空間から参照可能
     * @param inheritanceVisible 子クラスから参照可能
     * @param publicVisible どこからでも参照可能
     * @param instance インスタンスメンバーかどうか
     * @param isInterface インタフェースかどうか
     * @param fileInfo このクラスを宣言しているファイル情報
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public TargetInnerClassInfo(final Set<ModifierInfo> modifiers,
            final String[] fullQualifiedName, final boolean privateVisible,
            final boolean namespaceVisible, final boolean inheritanceVisible,
            final boolean publicVisible, final boolean instance, final boolean isInterface,
            final FileInfo fileInfo, final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {

        super(modifiers, fullQualifiedName, privateVisible, namespaceVisible, inheritanceVisible,
                publicVisible, instance, isInterface, fileInfo, fromLine, fromColumn, toLine,
                toColumn);

        this.outerUnit = null;
    }

    /**
     * 外側のユニットを返す
     * 
     * @return 外側のユニット
     */
    @Override
    public UnitInfo getOuterUnit() {
        assert null != this.outerUnit : "outerUnit is null!";
        return this.outerUnit;
    }

    /**
     * 外側のユニットを設定する
     * 
     * @param 外側のユニット
     */
    @Override
    public void setOuterUnit(final UnitInfo outerUnit) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == outerUnit) {
            throw new IllegalArgumentException();
        }

        this.outerUnit = outerUnit;
    }

    /**
     * 外側のクラスを返す.
     * 
     * @return　外側のクラス
     */
    @Override
    public final ClassInfo getOuterClass() {

        UnitInfo outer = this.getOuterUnit();

        while (true) {

            // インナークラスなのでかならず外側のクラスがある
            if (null == outer) {
                throw new IllegalStateException();
            }

            if (outer instanceof ClassInfo) {
                return (ClassInfo) outer;
            }

            outer = ((HavingOuterUnit) outer).getOuterUnit();
        }
    }

    /**
     * 外側のメソッドを返す.
     * 
     * @return　外側のメソッド
     */
    @Override
    public final CallableUnitInfo getOuterCallableUnit() {

        UnitInfo outer = this.getOuterUnit();

        while (true) {

            if (null == outer) {
                return null;
            }

            if (outer instanceof CallableUnitInfo) {
                return (CallableUnitInfo) outer;
            }

            if (!(outer instanceof HavingOuterUnit)) {
                return null;
            }

            outer = ((HavingOuterUnit) outer).getOuterUnit();
        }
    }

    @Override
    public TypeParameterizable getOuterTypeParameterizableUnit() {
        return this.getOuterClass();
    }

    /**
     * 外側のユニットを保存する変数
     */
    private UnitInfo outerUnit;
}
