package jp.ac.osaka_u.ist.sdl.scdetector.gui.codefragment;


import java.util.Observable;
import java.util.Observer;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;

import jp.ac.osaka_u.ist.sdl.scdetector.data.CloneSetInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.data.CodeFragmentInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.SelectedEntities;


public class CodeFragmentListView extends JTable implements Observer {

    class SelectionEventHandler implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {

            if (!e.getValueIsAdjusting()) {

                final int[] selectedRow = CodeFragmentListView.this.getSelectedRows();
                final SortedSet<CodeFragmentInfo> selectedCodeFragments = new TreeSet<CodeFragmentInfo>();
                for (int i = 0; i < selectedRow.length; i++) {

                    final int modelIndex = CodeFragmentListView.this
                            .convertRowIndexToModel(selectedRow[i]);
                    final CodeFragmentListViewModel model = (CodeFragmentListViewModel) CodeFragmentListView.this
                            .getModel();
                    final CodeFragmentInfo codeFragment = model.getCodeFragment(modelIndex);
                    selectedCodeFragments.add(codeFragment);
                }

                SelectedEntities.<CodeFragmentInfo> getInstance(CodeFragmentInfo.CODEFRAGMENT)
                        .setAll(selectedCodeFragments, CodeFragmentListView.this);
            }
        }
    }

    public CodeFragmentListView() {

        this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        this.scrollPane = new JScrollPane();
        this.scrollPane.setViewportView(this);
        this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        this.selectionEventHandler = new SelectionEventHandler();
        this.getSelectionModel().addListSelectionListener(this.selectionEventHandler);
    }

    public void update(Observable o, Object arg) {

        if (o instanceof SelectedEntities) {

            final SelectedEntities<?> selectedEntities = (SelectedEntities<?>) o;
            if (selectedEntities.getLabel().equals(CloneSetInfo.CLONESET)) {

                final CloneSetInfo cloneSet = SelectedEntities.<CloneSetInfo> getInstance(
                        CloneSetInfo.CLONESET).get().first();

                final CodeFragmentListViewModel model = new CodeFragmentListViewModel(cloneSet);
                this.setModel(model);
                final RowSorter<CodeFragmentListViewModel> sorter = new TableRowSorter<CodeFragmentListViewModel>(
                        model);
                this.setRowSorter(sorter);
            }
        }

    }

    final public JScrollPane scrollPane;

    final SelectionEventHandler selectionEventHandler;
}
