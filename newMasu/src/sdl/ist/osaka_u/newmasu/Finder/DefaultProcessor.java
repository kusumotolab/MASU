package sdl.ist.osaka_u.newmasu.Finder;



import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;

public enum DefaultProcessor {

    IF(IfStatement.class){
        @Override
        public void process(ASTNode node){
           vis.visit((IfStatement)node);
        }
    },
    FOR(ForStatement.class){
        @Override
        public void process(ASTNode node){
            vis.visit((ForStatement)node);
        }
    },
    BLOCK(Block.class){
        @Override
        public void process(ASTNode node){
            vis.visit((Block)node);
        }
    },

    _DEF_(Object.class){
        @Override
        public void process(ASTNode node){
            vis.def(node);
        }
    }
    ;

    private static String indent = "  ";
    private static CFGSample vis = null;

    private final Class<?> clazz;
    private DefaultProcessor(Class<?> clazz) {
        this.clazz = clazz;
    }

    public abstract void process(ASTNode node);

    public static DefaultProcessor get(Object obj, CFGSample vi) {
        vis = vi;
        for (DefaultProcessor processor : values()) {
            if(processor.clazz.isAssignableFrom(obj.getClass())) {
                return processor;
            }
        }
//        System.out.println("default");
//        throw new RuntimeException();
        return _DEF_;
    }
}
