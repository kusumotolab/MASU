package jp.ac.osaka_u.ist.sel.metricstool.main.parse;

import antlr.CommonAST;
import antlr.Token;

public class CommonASTWithLineNumber extends CommonAST {

    @Override
    public final int getLine() {
        return this.line;
    }
    
    @Override
    public final int getColumn() {
        return this.column;
    }
    
    @Override
    public void initialize(final Token tok) {
        super.initialize(tok);
        
        this.line = tok.getLine();
        this.column = tok.getColumn();
    }

    private int line = 0;
    
    private int column = 0;
}
