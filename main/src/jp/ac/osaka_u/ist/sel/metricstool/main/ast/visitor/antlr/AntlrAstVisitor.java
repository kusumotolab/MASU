package jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.antlr;


import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstTokenTranslator;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitListener;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitStrategy;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitor;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.PositionManager;

import antlr.collections.AST;


public class AntlrAstVisitor implements AstVisitor<AST>{

    public AntlrAstVisitor(final AstTokenTranslator<AST> translator){
        this(translator,true,true);
    }

    public AntlrAstVisitor(final AstTokenTranslator<AST> translator, boolean inClass, boolean inMethod){
        this(translator, new AntlrAstVisitStrategy(inClass,inMethod));
    }

    public AntlrAstVisitor(final AstTokenTranslator<AST> translator,
            final AstVisitStrategy<AST> strategy) {
        this.visitStrategy = strategy;
        this.translator = translator;
    }

    public void addVisitListener(AstVisitListener listener) {
        this.listeners.add(listener);
    }
    
    public void reset(){
        this.currentEvent = null;
        this.eventStack.clear();
        this.visitedNode.clear();
    }
    
    public void visit(final AST node) {
        if (null == node)
            return;
        
        this.visitedNode.add(node);

        //このノードのトークンを反映したAstTokenインスタンスに置き換えて，カレントトークンとして記録
        AstToken token = this.translator.translate(node);

        int startLine = 0;
        int startColumn = 0;
        int endLine = 0;
        int endColumn = 0;
        if (null != lineColumnManager){
            startLine = lineColumnManager.getStartLine(node);
            startColumn = lineColumnManager.getStartColumn(node);
            endLine = lineColumnManager.getEndLine(node);
            endColumn = lineColumnManager.getEndColumn(node);
        }
        
        AstVisitEvent event = new AstVisitEvent(this,token,startLine,startColumn,endLine,endColumn);
        
        this.currentEvent = event;
        
        //イベントを送る 
        fireVisitEvent(event);
        
        //次のノードへ誘導してもらう
        this.visitStrategy.guide(this, node, token);
    }

    public void enter() {
        //イベントを送る
        fireEnterEvent(this.eventStack.push(this.currentEvent));
    }

    public void exit() {
        //イベントを送る
        fireExitEvent(this.eventStack.pop());
    }

    public boolean isVisited(final AST node) {
        return this.visitedNode.contains(node);
    }


    public void removeVisitListener(AstVisitListener listener) {
        this.listeners.remove(listener);
    }
    
    public void setPositionManager(PositionManager lineColumn) {
        this.lineColumnManager = lineColumn;
    }
    
    private void fireVisitEvent(AstVisitEvent event){
        for(AstVisitListener listener : listeners){
            listener.visited(event);
        }
    }
    
    private void fireEnterEvent(AstVisitEvent event){
        for(AstVisitListener listener : listeners){
            listener.entered(event);
        }
    }
    
    private void fireExitEvent(AstVisitEvent event){
        for(AstVisitListener listener : listeners){
            listener.exited(event);
        }
    }

    private final AstVisitStrategy<AST> visitStrategy;

    private final AstTokenTranslator<AST> translator;
    
    private PositionManager lineColumnManager;

    private AstVisitEvent currentEvent;
    
    private final Stack<AstVisitEvent> eventStack = new Stack<AstVisitEvent>();

    private final Set<AST> visitedNode = new HashSet<AST>();

    private final Set<AstVisitListener> listeners = new LinkedHashSet<AstVisitListener>();
}
