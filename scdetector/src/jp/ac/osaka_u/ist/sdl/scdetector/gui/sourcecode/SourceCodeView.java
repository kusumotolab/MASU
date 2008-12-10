package jp.ac.osaka_u.ist.sdl.scdetector.gui.sourcecode;


import java.util.Observable;
import java.util.Observer;
import java.util.SortedSet;

import javax.swing.JSplitPane;

import jp.ac.osaka_u.ist.sdl.scdetector.ClonePairInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.SelectedEntities;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElement;


public class SourceCodeView extends JSplitPane implements Observer {

    public SourceCodeView() {

        this.leftSourceCodePanel = new SourceCodePanel();
        this.rightSourceCodePanel = new SourceCodePanel();

        this.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        this.setLeftComponent(this.leftSourceCodePanel);
        this.setRightComponent(this.rightSourceCodePanel);
    }

    public void update(Observable o, Object arg) {

        if (o instanceof SelectedEntities) {

            final SelectedEntities<?> selectedEntities = (SelectedEntities<?>) o;
            if (selectedEntities.getLabel().equals(ClonePairInfo.CLONEPAIR)) {

                final ClonePairInfo clonePair = SelectedEntities.<ClonePairInfo> getInstance(
                        ClonePairInfo.CLONEPAIR).get().first();
                
                final SortedSet<ExecutableElement> cloneA = clonePair.getCloneA();
                final SortedSet<ExecutableElement> cloneB = clonePair.getCloneB();
                
    
                final ExecutableElement firstElement = cloneA.first();
                
            }
        }
    }
    
    

    private final SourceCodePanel leftSourceCodePanel;

    private final SourceCodePanel rightSourceCodePanel;
}
