package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReturnStatementInfo;


/**
 * return文を表すノード
 * 
 * @author higo
 *
 */
public class CFGReturnNode extends CFGStatementNode {

    /**
     * 生成するノードに対応するreturn文を与えて初期化
     * 
     * @param returnStatement
     */
    public CFGReturnNode(final ReturnStatementInfo returnStatement) {
        super(returnStatement);
    }
}
