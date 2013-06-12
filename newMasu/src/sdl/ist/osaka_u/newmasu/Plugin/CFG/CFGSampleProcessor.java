package sdl.ist.osaka_u.newmasu.Plugin.CFG;



import org.eclipse.jdt.core.dom.*;

public enum CFGSampleProcessor {

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
    METHOD_DECLARATION(MethodDeclaration.class){
        @Override
        public void process(ASTNode node){
            vis.visit((MethodDeclaration)node);
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
    private CFGSampleProcessor(Class<?> clazz) {
        this.clazz = clazz;
    }

    public abstract void process(ASTNode node);

    public static CFGSampleProcessor get(Object obj, CFGSample vi) {
        vis = vi;
        for (CFGSampleProcessor processor : values()) {
            if(processor.clazz.isAssignableFrom(obj.getClass())) {
                return processor;
            }
        }
//        System.out.println("default");
//        throw new RuntimeException();
        return _DEF_;
    }
}
