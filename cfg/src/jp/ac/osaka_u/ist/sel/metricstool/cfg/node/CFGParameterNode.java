package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;


import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterUsageInfo;


/**
 * ������\��CFG�m�[�h�D
 * CFG�݂̂𗘗p����Ƃ��͂Ȃ��Ă��������CPDG�𗘗p����Ƃ��ɗp����D
 * 
 * @author higo
 *
 */
public class CFGParameterNode extends CFGNormalNode<ParameterUsageInfo> {

    public static CFGParameterNode getInstance(final ParameterInfo parameter) {

        if (null == parameter) {
            throw new IllegalArgumentException();
        }

        final CallableUnitInfo ownerUnit = parameter.getDefinitionUnit();
        final int fromLine = parameter.getFromLine();
        final int fromColumn = parameter.getFromColumn();
        final int toLine = parameter.getToLine();
        final int toColumn = parameter.getToColumn();

        final ParameterUsageInfo usage = ParameterUsageInfo.getInstance(parameter, false, true,
                ownerUnit, fromLine, fromColumn, toLine, toColumn);

        return new CFGParameterNode(usage);
    }

    private CFGParameterNode(final ParameterUsageInfo parameterUsage) {
        super(parameterUsage);
    }

    @Override
    final ExpressionInfo getDissolvingTarget() {
        return null;
    }

    @Override
    ParameterUsageInfo makeNewElement(final LocalSpaceInfo ownerSpace,
            final ExpressionInfo... requiredExpressions) {
        return null;
    }

    @Override
    void replace(List<CFGNode<? extends ExecutableElementInfo>> dissolvedNodeList) {
    }
}