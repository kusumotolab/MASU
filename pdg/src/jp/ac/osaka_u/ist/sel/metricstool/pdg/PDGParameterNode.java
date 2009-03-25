package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;


/**
 * 引数を表すPDGノード
 * 
 * @author higo
 *
 */
public class PDGParameterNode extends PDGNormalNode<ParameterInfo> {

    /**
     * 引数のオブジェクトを与えて初期化
     * 
     * @param parameter
     */
    public PDGParameterNode(final ParameterInfo parameter) {
        super(parameter);
        this.text = parameter.getType().getTypeName() + " " + parameter.getName();
    }

    @Override
    public final Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        final Set<VariableInfo<? extends UnitInfo>> definedVariables = new HashSet<VariableInfo<? extends UnitInfo>>();
        definedVariables.add(this.getCore());
        return definedVariables;
    }

    @Override
    public Set<VariableInfo<?>> getReferencedVariables() {
        return new HashSet<VariableInfo<? extends UnitInfo>>();
    }
}
