package jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph;


/**
 * {@link Edge} ‚ÌŽí—Þ.
 * 
 * @author rniitani
 */
public enum EDGE_TYPE {
    CALL {
        @Override
        public String getLabel() {
            return "call";
        }
    },
    SUPERCLASS {
        @Override
        public String getLabel() {
            return "superclass";
        }
    },
    INNERCLASS {
        @Override
        public String getLabel() {
            return "innerclass";
        }
    },
    ;
    /**
     * ƒ‰ƒxƒ‹‚ÉŽg—p‚·‚é•¶Žš—ñ.
     */
    public abstract String getLabel();
}