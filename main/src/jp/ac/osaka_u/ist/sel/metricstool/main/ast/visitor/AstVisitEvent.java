package jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor;


import java.util.EventObject;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;



public class AstVisitEvent extends EventObject {

    private final AstVisitor source;

    private final AstToken token;
    
    private final int startLine;
    private final int startColumn;

    private final int endLine;
    private final int endColumn;

    public AstVisitEvent(final AstVisitor source, final AstToken token,final int enterLine, final int enterColumn, final int exitLine, final int exitColumn) {
        super(source);
        this.source = source;
        this.token = token;
        
        this.startLine = enterLine;
        this.endLine = exitLine;
        
        this.startColumn = enterColumn;
        this.endColumn = exitColumn;
    }

    @Override
    public AstVisitor getSource() {
        return this.source;
    }

    public AstToken getToken() {
        return this.token;
    }

    public int getStartColumn() {
        return startColumn;
    }

    public int getStartLine() {
        return startLine;
    }

    public int getEndColumn() {
        return endColumn;
    }

    public int getEndLine() {
        return endLine;
    }

}
