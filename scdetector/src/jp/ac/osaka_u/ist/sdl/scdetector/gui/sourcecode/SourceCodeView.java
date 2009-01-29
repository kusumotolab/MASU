package jp.ac.osaka_u.ist.sdl.scdetector.gui.sourcecode;


import java.util.Observable;
import java.util.Observer;
import java.util.SortedSet;

import javax.swing.JSplitPane;

import jp.ac.osaka_u.ist.sdl.scdetector.ClonePairInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.SelectedEntities;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
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

                final SortedSet<ExecutableElementInfo> cloneA = clonePair.getCloneA();
                final SortedSet<ExecutableElementInfo> cloneB = clonePair.getCloneB();

                final SourceCodePanel leftSourceCodePanel = new SourceCodePanel();
                final SourceCodePanel rightSourceCodePanel = new SourceCodePanel();

                {
                    boolean set = false;
                    for (final ExecutableElementInfo element : cloneA) {

                        if (element instanceof StatementInfo) {
                            final FileInfo ownerFile = SourceCodeView
                                    .getOwnerFile((StatementInfo) element);
                            leftSourceCodePanel.readFile(ownerFile);
                            this.setLeftComponent(leftSourceCodePanel);
                            set = true;
                            break;
                        } else if (element instanceof ExpressionInfo) {
                            final FileInfo ownerFile = SourceCodeView
                                    .getOwnerFile(((ExpressionInfo) element).getOwnerStatement());
                            leftSourceCodePanel.readFile(ownerFile);
                            this.setLeftComponent(leftSourceCodePanel);
                            set = true;
                            break;
                        }
                    }

                    assert set : "State is illegal!";
                }

                {
                    boolean set = false;
                    for (final ExecutableElementInfo element : cloneB) {

                        if (element instanceof StatementInfo) {
                            final FileInfo ownerFile = SourceCodeView
                                    .getOwnerFile((StatementInfo) element);
                            rightSourceCodePanel.readFile(ownerFile);
                            this.setRightComponent(rightSourceCodePanel);
                            set = true;
                            break;
                        } else if (element instanceof ExpressionInfo) {
                            final FileInfo ownerFile = SourceCodeView
                                    .getOwnerFile(((ExpressionInfo) element).getOwnerStatement());
                            rightSourceCodePanel.readFile(ownerFile);
                            this.setRightComponent(rightSourceCodePanel);
                            set = true;
                            break;
                        }
                    }

                    assert set : "State is illegal!";
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

    private static FileInfo getOwnerFile(final StatementInfo statement) {

        final LocalSpaceInfo ownerSpace = statement.getOwnerSpace();
        final ClassInfo ownerClass = ownerSpace.getOwnerClass();
        return ((TargetClassInfo) ownerClass).getOwnerFile();
    }
}
