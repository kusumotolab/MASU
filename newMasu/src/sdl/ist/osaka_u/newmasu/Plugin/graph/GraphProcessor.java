package sdl.ist.osaka_u.newmasu.Plugin.graph;



import org.eclipse.jdt.core.dom.*;

public enum GraphProcessor {

    IF(IfStatement.class){
        @Override
        public void process(ASTNode node){
           vis.visit((IfStatement)node);
        }
    },
    SWITCH(SwitchStatement.class){
        @Override
        public void process(ASTNode node){
            vis.visit((SwitchStatement)node);
        }
    },
    FOR(ForStatement.class){
        @Override
        public void process(ASTNode node){
            vis.visit((ForStatement)node);
        }
    },
    ENHANCEDFOR(EnhancedForStatement.class){
        @Override
        public void process(ASTNode node){
            vis.visit((EnhancedForStatement)node);
        }
    },
    WHILE(WhileStatement.class){
        @Override
        public void process(ASTNode node){
            vis.visit((WhileStatement)node);
        }
    },
    DO(DoStatement.class){
        @Override
        public void process(ASTNode node){
            vis.visit((DoStatement)node);
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
    BREAK(BreakStatement.class){
        @Override
        public void process(ASTNode node){
            vis.visit((BreakStatement)node);
        }
    },
    RETURN(ReturnStatement.class){
        @Override
        public void process(ASTNode node){
            vis.visit((ReturnStatement)node);
        }
    },
    CONTINUE(ContinueStatement.class){
        @Override
        public void process(ASTNode node){
            vis.visit((ContinueStatement)node);
        }
    },
/*
    VARDEC(VariableDeclaration.class){
        @Override
        public void process(ASTNode node){
            vis.varDec((VariableDeclaration)node);
        }
    },
*/
    _DEF_(Object.class){
        @Override
        public void process(ASTNode node){
            vis.def(node);
        }
    }
    ;

    private static String indent = "  ";
    private static GraphVisitor vis = null;

    private final Class<?> clazz;
    private GraphProcessor(Class<?> clazz) {
        this.clazz = clazz;
    }

    public abstract void process(ASTNode node);

    public static GraphProcessor get(Object obj, GraphVisitor vi) {
        vis = vi;
        for (GraphProcessor processor : values()) {
            if(processor.clazz.isAssignableFrom(obj.getClass())) {
                return processor;
            }
        }
//        System.out.println("default");
//        throw new RuntimeException();
        return _DEF_;
    }
}
