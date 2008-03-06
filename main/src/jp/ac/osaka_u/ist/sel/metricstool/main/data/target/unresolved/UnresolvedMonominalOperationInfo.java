package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MonominalOperationInfo;
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
        UnresolvedEntityUsageInfo<MonominalOperationInfo> {

    /**
     * €‚Æˆê€‰‰Z‚ÌŒ‹‰Ê‚ÌŒ^‚ğ—^‚¦‚Ä‰Šú‰»
     * 
     * @param term €
     * @param type ˆê€‰‰Z‚ÌŒ‹‰Ê‚ÌŒ^
     */
    public UnresolvedMonominalOperationInfo(final UnresolvedEntityUsageInfo<?> term,
            final PrimitiveTypeInfo type) {

        if (null == term || null == type) {
            throw new IllegalArgumentException("term or type is null");
        }

        this.term = term;
        this.type = type;
    }

    @Override
    public boolean alreadyResolved() {
        return null != this.resolved;
    }

    @Override
    public MonominalOperationInfo getResolved() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolved;
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

        final UnresolvedEntityUsageInfo<?> unresolvedTerm = this.getTerm();
        final EntityUsageInfo term = unresolvedTerm.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        final PrimitiveTypeInfo type = this.getResultType();

        this.resolved = new MonominalOperationInfo(term, type, fromLine, fromColumn, toLine,
                toColumn);
        return this.resolved;
    }

    /**
     * ˆê€‰‰Z‚Ì€‚ğ•Ô‚·
     * 
     * @return ˆê€‰‰Z‚Ì€
     */
    public UnresolvedEntityUsageInfo<?> getTerm() {
        return this.term;
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
    private final UnresolvedEntityUsageInfo<?> term;

    /**
     * ˆê€‰‰Z‚ÌŒ‹‰Ê‚ÌŒ^
     */
    private final PrimitiveTypeInfo type;

    private MonominalOperationInfo resolved;
}
