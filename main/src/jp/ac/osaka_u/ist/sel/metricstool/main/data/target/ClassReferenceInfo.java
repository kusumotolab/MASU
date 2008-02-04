package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * クラスの参照を表すクラス．
 * ReferenceTypeInfo　は「参照型」を表すのに対して，
 * このクラスはクラスの参照に関する情報（参照位置など）を表す
 * 
 * @author higo
 *
 */
public final class ClassReferenceInfo extends EntityUsageInfo {

    /**
     * 参照型を与えてオブジェクトを初期化
     * 
     * @param referenceType　このクラス参照の参照型
     */
    public ClassReferenceInfo(final ClassTypeInfo referenceType, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        if (null == referenceType) {
            throw new NullPointerException();
        }

        this.referenceType = referenceType;
    }

    /**
     * このクラス参照の参照型を返す
     * 
     * @return このクラス参照の参照型
     */
    @Override
    public TypeInfo getType() {
        return this.referenceType;
    }

    /**
     * このクラス参照で参照されているクラスを返す
     * 
     * @return このクラス参照で参照されているクラス
     */
    public ClassInfo getReferencedClass() {
        return this.referenceType.getReferencedClass();
    }

    /**
     * このクラス参照の参照型を保存する変数
     */
    private final ClassTypeInfo referenceType;
}
