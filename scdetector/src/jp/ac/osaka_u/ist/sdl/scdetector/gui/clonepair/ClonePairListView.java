package jp.ac.osaka_u.ist.sdl.scdetector.gui.clonepair;


import java.util.Set;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.table.TableRowSorter;

import jp.ac.osaka_u.ist.sdl.scdetector.ClonePairInfo;


public class ClonePairListView extends JTable {

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
    }

    final public JScrollPane scrollPane;
}
