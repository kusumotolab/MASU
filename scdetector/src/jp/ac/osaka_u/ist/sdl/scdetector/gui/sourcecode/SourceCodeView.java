package jp.ac.osaka_u.ist.sdl.scdetector.gui.sourcecode;


import java.util.Observable;
import java.util.Observer;
import java.util.SortedSet;

import javax.swing.JSplitPane;

import jp.ac.osaka_u.ist.sdl.scdetector.ClonePairInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.SelectedEntities;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;


public class SourceCodeView extends JSplitPane implements Observer {

    public SourceCodeView() {
        this.setOrientation(JSplitPane.HORIZONTAL_SPLIT);

    }

    public void update(Observable o, Object arg) {

        if (o instanceof SelectedEntities) {

            final SelectedEntities<?> selectedEntities = (SelectedEntities<?>) o;
            if (selectedEntities.getLabel().equals(ClonePairInfo.CLONEPAIR)) {

                final ClonePairInfo clonePair = SelectedEntities.<ClonePairInfo> getInstance(
                        ClonePairInfo.CLONEPAIR).get().first();

                final SortedSet<ExecutableElement> cloneA = clonePair.getCloneA();
                final SortedSet<ExecutableElement> cloneB = clonePair.getCloneB();

                final SourceCodePanel leftSourceCodePanel = new SourceCodePanel();
                final SourceCodePanel rightSourceCodePanel = new SourceCodePanel();

                for (final ExecutableElement element : cloneA) {

                    if (element instanceof StatementInfo) {
                        final FileInfo ownerFile = SourceCodeView
                                .geOwnerFile((StatementInfo) element);
                        leftSourceCodePanel.readFile(ownerFile);
                        this.setLeftComponent(leftSourceCodePanel);
                        break;
                    }
                }

                for (final ExecutableElement element : cloneB) {

                    if (element instanceof StatementInfo) {
                        final FileInfo ownerFile = SourceCodeView
                                .geOwnerFile((StatementInfo) element);
                        rightSourceCodePanel.readFile(ownerFile);
                        this.setRightComponent(rightSourceCodePanel);
                        break;
                    }
                }

                final int width = this.getWidth();
                this.setDividerLocation(width / 2);

                leftSourceCodePanel.addHighlight(cloneA);
                rightSourceCodePanel.addHighlight(cloneB);

                leftSourceCodePanel.display(cloneA);
                rightSourceCodePanel.display(cloneB);
            }
        }
    }

    private static FileInfo geOwnerFile(final StatementInfo statement) {

        final LocalSpaceInfo ownerSpace = statement.getOwnerSpace();
        final ClassInfo ownerClass = ownerSpace.getOwnerClass();
        return ((TargetClassInfo) ownerClass).getFileInfo();
    }
}
