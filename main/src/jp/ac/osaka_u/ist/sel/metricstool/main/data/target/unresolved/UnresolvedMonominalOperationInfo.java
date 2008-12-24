package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MonominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.OPERATOR;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ˆê€‰‰Z‚Ì“à—e‚ğ•\‚·ƒNƒ‰ƒX
 * 
 * @author t-miyake, higo
 *
 */
public final class UnresolvedMonominalOperationInfo extends
        UnresolvedExpressionInfo<MonominalOperationInfo> {

    /**
     * €‚Æˆê€‰‰Z‚ÌŒ‹‰Ê‚ÌŒ^‚ğ—^‚¦‚Ä‰Šú‰»
     * 
     * @param operand €
     * @param operator ˆê€‰‰Z‚Ì‰‰Zq
     * @param type ˆê€‰‰Z‚ÌŒ‹‰Ê‚ÌŒ^
     */
    public UnresolvedMonominalOperationInfo(
            final UnresolvedExpressionInfo<? extends ExpressionInfo> operand,
            final OPERATOR operator, final PrimitiveTypeInfo type) {

        if (null == operand || null == operator || null == type) {
            throw new IllegalArgumentException("term or type is null");
        }

        this.operand = operand;
        this.operator = operator;
        this.type = type;
    }

    @Override
    public MonominalOperationInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // •s³‚ÈŒÄ‚Ño‚µ‚Å‚È‚¢‚©‚ğƒ`ƒFƒbƒN
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // Šù‚É‰ğŒˆÏ‚İ‚Å‚ ‚éê‡‚ÍCƒLƒƒƒbƒVƒ…‚ğ•Ô‚·
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // g—pˆÊ’u‚ğæ“¾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // —v‘fg—p‚ÌƒI[ƒi[—v‘f‚ğ•Ô‚·
        final UnresolvedExecutableElementInfo<?> unresolvedOwnerExecutableElement = this
                .getOwnerExecutableElement();
        final ExecutableElementInfo ownerExecutableElement = unresolvedOwnerExecutableElement
                .resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                        methodInfoManager);

        final UnresolvedExpressionInfo<?> unresolvedTerm = this.getOperand();
        final ExpressionInfo term = unresolvedTerm.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        final PrimitiveTypeInfo type = this.getResultType();
        final boolean isPreposed = fromColumn < term.getFromColumn() ? true : false;

        this.resolvedInfo = new MonominalOperationInfo(term, this.operator, isPreposed, type,
                fromLine, fromColumn, toLine, toColumn);
        this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);
        return this.resolvedInfo;
    }

    /**
     * ˆê€‰‰Z‚Ì€‚ğ•Ô‚·
     * 
     * @return ˆê€‰‰Z‚Ì€
     */
    public UnresolvedExpressionInfo<? extends ExpressionInfo> getOperand() {
        return this.operand;
    }

    /**
     * ˆê€‰‰Z‚ÌŒ‹‰Ê‚ÌŒ^‚ğ•Ô‚·
     * 
     * @return ˆê€‰‰Z‚ÌŒ‹‰Ê‚ÌŒ^
     */
    public PrimitiveTypeInfo getResultType() {
        return this.type;
    }

    /**
     * ˆê€‰‰Z‚Ì€
     */
    private final UnresolvedExpressionInfo<? extends ExpressionInfo> operand;

    /**
     * ˆê€‰‰Z‚Ì‰‰Zq
     */
    private final OPERATOR operator;

    /**
     * ˆê€‰‰Z‚ÌŒ‹‰Ê‚ÌŒ^
     */
    private final PrimitiveTypeInfo type;

}
