package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;


/**
 * 引数を表すPDGノード
 * 
 * @author higo
 *
 */
public class PDGParameterNode extends PDGNormalNode<ParameterDeclarationStatementInfo> {

    /**
     * 引数のオブジェクトを与えて初期化
     * 
     * @param parameter
     */
    public PDGParameterNode(final ParameterInfo parameter) {

        if (null == parameter) {
            throw new IllegalArgumentException();
        }

        this.core = new ParameterDeclarationStatementInfo(parameter, parameter.getFromLine(),
                parameter.getFromColumn(), parameter.getToLine(), parameter.getToColumn());

        this.text = parameter.getType().getTypeName() + " " + parameter.getName() + " <"
                + parameter.getFromLine() + ">";
        ;
    }

    @Override
    public final SortedSet<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        final SortedSet<VariableInfo<? extends UnitInfo>> definedVariables = new TreeSet<VariableInfo<? extends UnitInfo>>();
        definedVariables.add(this.getCore().getParameter());
        return definedVariables;
    }

    @Override
    public SortedSet<VariableInfo<?>> getReferencedVariables() {
        return new TreeSet<VariableInfo<? extends UnitInfo>>();
    }
}
