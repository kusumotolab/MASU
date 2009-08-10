package jp.ac.osaka_u.ist.sdl.scdetector;


import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.PDGController;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.ICFGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.IPDGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.IntraProceduralPDG;


public class PDGBuildingThread implements Runnable {

    public PDGBuildingThread(final CallableUnitInfo method, final IPDGNodeFactory pdgNodeFactory,
            final ICFGNodeFactory cfgNodeFactory, final boolean data, final boolean control,
            final boolean execution, final int distance) {

        if (null == method || null == pdgNodeFactory || null == cfgNodeFactory) {
            throw new IllegalArgumentException();
        }

        this.pdgNodeFactory = pdgNodeFactory;
        this.cfgNodeFactory = cfgNodeFactory;
        this.method = method;
        this.data = data;
        this.control = control;
        this.execution = execution;
        this.distance = distance;
    }

    @Override
    public void run() {
        final IntraProceduralPDG pdg = new IntraProceduralPDG(this.method, this.pdgNodeFactory,
                this.cfgNodeFactory, this.data, this.control, this.execution, this.distance);
        PDGController.getInstance(Scorpio.ID).put(this.method, pdg);
    }

    private final IPDGNodeFactory pdgNodeFactory;

    private final ICFGNodeFactory cfgNodeFactory;

    private final CallableUnitInfo method;

    private final boolean data;

    private final boolean control;

    private final boolean execution;

    private final int distance;
}
