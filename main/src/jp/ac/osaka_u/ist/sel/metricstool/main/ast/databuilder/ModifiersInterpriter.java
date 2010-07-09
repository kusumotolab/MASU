package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StaticOrInstance;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Visualizable;


public interface ModifiersInterpriter {
    public void interprit(ModifierInfo[] modifiers, Visualizable visualizable,
            StaticOrInstance member);
}
