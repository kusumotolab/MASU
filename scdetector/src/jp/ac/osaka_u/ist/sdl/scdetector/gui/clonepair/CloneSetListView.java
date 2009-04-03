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

import jp.ac.osaka_u.ist.sdl.scdetector.CloneSetInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.SelectedEntities;


public class CloneSetListView extends JTable implements Observer {

    class SelectionEventHandler implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {

            if (!e.getValueIsAdjusting()) {

                final int[] selectedRow = CloneSetListView.this.getSelectedRows();
                final SortedSet<CloneSetInfo> selectedCloneSets = new TreeSet<CloneSetInfo>();
                for (int i = 0; i < selectedRow.length; i++) {

                    final int modelIndex = CloneSetListView.this
                            .convertRowIndexToModel(selectedRow[i]);
                    final CloneSetListViewModel model = (CloneSetListViewModel) CloneSetListView.this
                            .getModel();
                    final CloneSetInfo cloneSet = model.getClonePair(modelIndex);
                    selectedCloneSets.add(cloneSet);
                }

                SelectedEntities.<CloneSetInfo> getInstance(CloneSetInfo.CLONESET).setAll(
                        selectedCloneSets, CloneSetListView.this);
            }
        }
    }

    public CloneSetListView(final Set<CloneSetInfo> cloneSets) {

        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        this.scrollPane = new JScrollPane();
        this.scrollPane.setViewportView(this);
        this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        final CloneSetListViewModel model = new CloneSetListViewModel(cloneSets);
        this.setModel(model);
        final RowSorter<CloneSetListViewModel> sorter = new TableRowSorter<CloneSetListViewModel>(
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
