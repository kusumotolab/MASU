package jp.ac.osaka_u.ist.sdl.scdetector.gui.clonepair;


import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;

import jp.ac.osaka_u.ist.sdl.scdetector.ClonePairInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.SelectedEntities;


public class ClonePairListView extends JTable implements Observer {

    class SelectionEventHandler implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {

            if (!e.getValueIsAdjusting()) {

                final int[] selectedRow = ClonePairListView.this.getSelectedRows();
                final SortedSet<ClonePairInfo> selectedClonePairs = new TreeSet<ClonePairInfo>();
                for (int i = 0; i < selectedRow.length; i++) {

                    final int modelIndex = ClonePairListView.this
                            .convertRowIndexToModel(selectedRow[i]);
                    final ClonePairListViewModel model = (ClonePairListViewModel) ClonePairListView.this
                            .getModel();
                    final ClonePairInfo clonePair = model.getClonePair(modelIndex);
                    selectedClonePairs.add(clonePair);
                }

                SelectedEntities.<ClonePairInfo> getInstance(ClonePairInfo.CLONEPAIR).setAll(
                        selectedClonePairs, ClonePairListView.this);
            }
        }
    }

    public ClonePairListView(final Set<ClonePairInfo> clonePairs) {

        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        this.scrollPane = new JScrollPane();
        this.scrollPane.setViewportView(this);
        this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        final ClonePairListViewModel model = new ClonePairListViewModel(clonePairs);
        this.setModel(model);
        final RowSorter<ClonePairListViewModel> sorter = new TableRowSorter<ClonePairListViewModel>(
                model);
        this.setRowSorter(sorter);

        this.selectionEventHandler = new SelectionEventHandler();
        this.getSelectionModel().addListSelectionListener(this.selectionEventHandler);
    }

    public void update(Observable o, Object arg) {

    }

    final public JScrollPane scrollPane;

    final SelectionEventHandler selectionEventHandler;
}
