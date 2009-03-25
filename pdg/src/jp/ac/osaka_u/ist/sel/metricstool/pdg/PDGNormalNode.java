package jp.ac.osaka_u.ist.sel.metricstool.pdg;


/**
 * 制御ノード以外のノードを表す
 * 
 * @author higo
 *
 * @param <T>
 */
public abstract class PDGNormalNode<T> extends PDGNode<T> {

    PDGNormalNode(final T core) {
        super(core);
    }
}
