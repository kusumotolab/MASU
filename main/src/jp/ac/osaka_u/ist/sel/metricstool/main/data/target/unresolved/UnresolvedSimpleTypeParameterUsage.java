package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


public final class UnresolvedSimpleTypeParameterUsage extends UnresolvedTypeParameterUsage {

    /**
     * –¢‰ğŒˆŒ^‚ğ—^‚¦‚ÄƒIƒuƒWƒFƒNƒg‚ğ‰Šú‰»
     * 
     * @param type –¢‰ğŒˆŒ^
     */
    public UnresolvedSimpleTypeParameterUsage(final UnresolvedTypeInfo type) {

        if (null == type) {
            throw new NullPointerException();
        }

        this.type = type;
    }

    /**
     * Œ^ƒpƒ‰ƒ[ƒ^‚Ì–¢‰ğŒˆŒ^‚ğ•Ô‚·
     * 
     * @return –¢‰ğŒˆŒ^
     */
    public UnresolvedTypeInfo getType() {
        return this.type;
    }

    /**
     * –¢‰ğŒˆŒ^‚ğ•Û‘¶‚·‚é‚½‚ß‚Ì•Ï”
     */
    private final UnresolvedTypeInfo type;
}
