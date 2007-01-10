package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.MemberSetting;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.VisualizableSetting;

public interface ModifiersInterpriter {
    public void interprit(ModifierInfo[] modifiers, VisualizableSetting visualizable,MemberSetting member);
}
