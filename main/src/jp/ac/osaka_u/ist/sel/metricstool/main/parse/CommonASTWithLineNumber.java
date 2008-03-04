package jp.ac.osaka_u.ist.sel.metricstool.main.parse;


import com.sun.xml.internal.bind.marshaller.NioEscapeHandler;

import antlr.CommonAST;
import antlr.Token;


public class CommonASTWithLineNumber extends CommonAST {

    public final int getFromLine() {
        return this.fromLine;
    }

    public final int getFromColumn() {
        return this.fromColumn;
    }

    public final int getToLine() {
        return this.toLine;
    }

    public final int getToColumn() {
        return this.toColumn;
    }

    @Override
    public final int getLine() {
        return this.fromLine;
    }

    @Override
    public final int getColumn() {
        return this.fromColumn;
    }

    @Override
    public void initialize(final Token tok) {
        super.initialize(tok);

        this.initializeFromPosition(tok.getLine(), tok.getColumn());
        this.initializeToPosition(tok.getLine(), tok.getColumn() + tok.getText().length());
    }

    public void initializeFromPosition(final int fromLine, final int fromColumn) {
        this.fromLine = fromLine;
        this.fromColumn = fromColumn;
    }
    
    public void initializeToPosition(final int toLine, final int toColumn) {
        this.toLine = toLine;
        this.toColumn = toColumn;
    }
    
    

    private int fromLine = 0;

    private int fromColumn = 0;

    private int toLine = 0;

    private int toColumn = 0;
}
