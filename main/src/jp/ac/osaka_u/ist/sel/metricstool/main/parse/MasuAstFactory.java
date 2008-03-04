package jp.ac.osaka_u.ist.sel.metricstool.main.parse;


import antlr.ASTFactory;
import antlr.ASTPair;
import antlr.collections.AST;


/**
 * @author t-miyake
 *
 */
public class MasuAstFactory extends ASTFactory {

    @Override
    public AST make(final AST[] nodes) {

        if (nodes == null || nodes.length == 0) {
            return null;
        }

        AST root = nodes[0];
        AST tail = null;

        int fromLine = 0;
        int fromColumn = 0;
        int toLine = 0;
        int toColumn = 0;
        
        if (null != root) {
            root.setFirstChild(null);
        }

        // 子ノードを関連付けて木構造を構築
        for (int i = 1; i < nodes.length; i++) {

            // nullのノードは無視
            if (nodes[i] == null) {
                continue;
            }

            if (null == root) {
                root = tail = nodes[i];
            } else if (null == tail) {
                root.setFirstChild(nodes[i]);
                tail = root.getFirstChild();
            } else {
                tail.setNextSibling(nodes[i]);
                tail = tail.getNextSibling();
            }
            
            if(0 == fromLine && tail instanceof CommonASTWithLineNumber) {
                final CommonASTWithLineNumber tailAst = (CommonASTWithLineNumber) tail;
                fromLine = tailAst.getFromLine();
                fromColumn = tailAst.getFromColumn();
            }
            
            // Chase tail to last sibling
            while (tail.getNextSibling() != null) {
                tail = tail.getNextSibling();
            }
        }
        
        if(tail instanceof CommonASTWithLineNumber) {
            final CommonASTWithLineNumber tailAst = (CommonASTWithLineNumber) tail;
            toLine = tailAst.getToLine();
            toColumn = tailAst.getToColumn();
        }
        
        if(root instanceof CommonASTWithLineNumber) {
            final CommonASTWithLineNumber rootAst = (CommonASTWithLineNumber) root;
            rootAst.initializeFromPosition(fromLine, fromColumn);
            rootAst.initializeToPosition(toLine, toColumn);
        }
        
        
        return root;
    }

    @Override
    public void addASTChild(ASTPair currentAST, AST child) {
        if (child != null) {
            if (currentAST.root == null) {
                // Make new child the current root
                currentAST.root = child;
            }
            else {
                if (currentAST.child == null) {
                    // Add new child to current root
                    currentAST.root.setFirstChild(child);
                }
                else {
                    currentAST.child.setNextSibling(child);
                }
                
                if(child instanceof CommonASTWithLineNumber && currentAST.root instanceof CommonASTWithLineNumber) {
                    CommonASTWithLineNumber childAst = (CommonASTWithLineNumber) child;
                    CommonASTWithLineNumber rootAst = (CommonASTWithLineNumber) currentAST.root;
                    
                    rootAst.initializeToPosition(childAst.getToLine(), childAst.getToColumn());
                }
            }
            // Make new child the current child
            currentAST.child = child;
            currentAST.advanceChildToEnd();
            
        }
    }
    
    
    
    
}
