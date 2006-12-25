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


/**
 * antlrのASTを訪問する {@link AstVisitor}.
 * 
 * 
 * @author kou-tngt
 *
 */
public class AntlrAstVisitor implements AstVisitor<AST> {

    /**
     * 引数translatorで指定された {@link AstTokenTranslator} とデフォルトの {@link AstVisitStrategy}を
     * 設定するコンストラクタ.
     * このコンストラクタから生成されたデフォルトのAstVisitStrategyはクラスやメソッド内部のノードも訪問するようにビジターを誘導する.
     * 
     * @param translator　このビジターが使用するASTノードの翻訳機
     */
    public AntlrAstVisitor(final AstTokenTranslator<AST> translator) {
        this(translator, true, true);
    }

    /**
     * 引数translatorで指定された {@link AstTokenTranslator} とデフォルトの {@link AstVisitStrategy}を
     * 設定するコンストラクタ.
     * 
     * クラスやメソッド内部のノードを訪問するかどうかを引数intoClassとintoMethodで指定する.
     * 
     * @param translator　このビジターが使用するASTノードの翻訳機
     * @param intoClass クラスを表すASTの内部を訪問するかどうかを指定する.訪問する場合はtrue.
     * @param intoMethod　メソッドを表すASTの内部を訪問するかどうかを指定する.訪問する場合はtrue.
     */
    public AntlrAstVisitor(final AstTokenTranslator<AST> translator, final boolean intoClass,
            final boolean intoMethod) {
        this(translator, new AntlrAstVisitStrategy(intoClass, intoMethod));
    }

    /**
     * 引数で指定された {@link AstTokenTranslator} と {@link AstVisitStrategy}を
     * 設定するコンストラクタ.
     * 
     * @param translator　このビジターが使用するASTノードの翻訳機
     * @param strategy　このビジターの訪問先を誘導するAstVisitStrategyインスタンス
     */
    public AntlrAstVisitor(final AstTokenTranslator<AST> translator,
            final AstVisitStrategy<AST> strategy) {
        if (null == translator){
            throw new NullPointerException("translator is null.");
        }
        if (null == strategy){
            throw new NullPointerException("starategy is null.");
        }
        
        this.visitStrategy = strategy;
        this.translator = translator;
    }

    /**
     * このビジターが発行する各 {@link AstVisitEvent} の通知を受けるリスナを登録する.
     * 
     * @param listener 登録するリスナ
     * @throws NullPointerException listenerがnullの場合
     */
    public void addVisitListener(final AstVisitListener listener) {
        if (null == listener){
            throw new NullPointerException("listener is null.");
        }
        
        this.listeners.add(listener);
    }
    
    /**
     * 直前に {@link #visit(T)} メソッドによって到達したノードの中に入る.
     */
    public void enter() {
        if (null != currentEvent){
            //イベントを送る
            this.fireEnterEvent(this.eventStack.push(this.currentEvent));
        }
    }

    /**
     * 現在のノードの中から外に出る.
     */
    public void exit() {
        if (!eventStack.isEmpty()){
            //イベントを送る
            this.fireExitEvent(this.eventStack.pop());
        }
    }
    
    /**
     * 引数で与えられたノードに既に到達済みかどうかを返す.
     * 
     * @param node　到達済みかどうかを判定したいノード
     * @return　到達済みであればtrue, そうでなかればfalse.
     */
    public boolean isVisited(final AST node) {
        return this.visitedNode.contains(node);
    }
    
    /**
     * このビジターが発行する各 {@link AstVisitEvent} の通知を受けるリスナを削除する.
     * 
     * @param listener　削除するリスナ
     * @throws NullPointerException listenerがnullの場合
     */
    public void removeVisitListener(final AstVisitListener listener) {
        this.listeners.remove(listener);
    }

    /**
     * このビジターの状態を初期状態に戻す.
     * イベントリスナは削除されない.
     */
    public void reset() {
        this.currentEvent = null;
        this.eventStack.clear();
        this.visitedNode.clear();
    }

    /**
     * ASTノードのソースコード上での位置情報を管理する {@link PositionManager} をセットする.
     * 
     * @param position　ASTノードのソースコード上での位置情報を管理する {@link PositionManager}
     */
    public void setPositionManager(final PositionManager lineColumn) {
        this.positionManager = lineColumn;
    }

    /**
     * 引数で与えられたノードを訪問する.
     * nodeがnullの場合は何もしない.
     * 
     * @param node 訪問するノード.
     */
    public void visit(final AST node) {
        if (null == node) {
            return;
        }
        
        //訪問済みのノードに登録
        this.visitedNode.add(node);

        //このノードのトークンからAstTokenに変換する
        final AstToken token = this.translator.translate(node);

        //位置情報が利用できるなら取得する.
        int startLine = 0;
        int startColumn = 0;
        int endLine = 0;
        int endColumn = 0;
        if (null != this.positionManager) {
            startLine = this.positionManager.getStartLine(node);
            startColumn = this.positionManager.getStartColumn(node);
            endLine = this.positionManager.getEndLine(node);
            endColumn = this.positionManager.getEndColumn(node);
        }

        //イベントを作成してカレントイベントとして登録
        final AstVisitEvent event = new AstVisitEvent(this, token, startLine, startColumn, endLine,
                endColumn);
        this.currentEvent = event;

        //イベントを送る 
        this.fireVisitEvent(event);

        //次のノードへ誘導してもらう
        this.visitStrategy.guide(this, node, token);
    }

    /**
     * 到達イベントを発行する
     * @param event 発行するイベント
     */
    private void fireVisitEvent(final AstVisitEvent event) {
        for (final AstVisitListener listener : this.listeners) {
            listener.visited(event);
        }
    }

    /**
     * 現在のノードの内部に入るイベントを発行する
     * @param event　発行するイベント
     */
    private void fireEnterEvent(final AstVisitEvent event) {
        for (final AstVisitListener listener : this.listeners) {
            listener.entered(event);
        }
    }

    /**
     * 現在のノードの内部から出るイベントを発行する
     * @param event　発行するイベント
     */
    private void fireExitEvent(final AstVisitEvent event) {
        for (final AstVisitListener listener : this.listeners) {
            listener.exited(event);
        }
    }

    /**
     * このビジターの訪問先を誘導する.
     */
    private final AstVisitStrategy<AST> visitStrategy;

    /**
     * 訪問したASTノードをAstTokenに変換する
     */
    private final AstTokenTranslator<AST> translator;

    /**
     * 訪問したASTノードの位置情報を管理する
     */
    private PositionManager positionManager;

    /**
     * 現在のノードに関連する訪問イベント
     */
    private AstVisitEvent currentEvent;

    /**
     * イベントを管理するスタック
     */
    private final Stack<AstVisitEvent> eventStack = new Stack<AstVisitEvent>();

    /**
     * 到達済みのノードのセット
     */
    private final Set<AST> visitedNode = new HashSet<AST>();

    /**
     * イベント通知を受け取るリスナーのセット
     */
    private final Set<AstVisitListener> listeners = new LinkedHashSet<AstVisitListener>();
}
