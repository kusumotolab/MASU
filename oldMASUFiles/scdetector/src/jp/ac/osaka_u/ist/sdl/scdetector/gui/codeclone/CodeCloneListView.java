package jp.ac.osaka_u.ist.sdl.scdetector.gui.codeclone;


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

import jp.ac.osaka_u.ist.sdl.scdetector.gui.SelectedEntities;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.CloneSetInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.CodeCloneInfo;


/**
 * �R�[�h�N���[���ꗗ��\�����邽�߂̃p�l��
 * 
 * @author higo
 *
 */
public class CodeCloneListView extends JTable implements Observer {

    /**
     * �R�[�h�N���[���̑I���𐧌䂷�邽�߂̃N���X
     * 
     * @author higo
     *
     */
    class SelectionEventHandler implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {

            if (!e.getValueIsAdjusting()) {

                final int[] selectedRow = CodeCloneListView.this.getSelectedRows();
                final SortedSet<CodeCloneInfo> selectedCodeFragments = new TreeSet<CodeCloneInfo>();
                for (int i = 0; i < selectedRow.length; i++) {

                    final int modelIndex = CodeCloneListView.this
                            .convertRowIndexToModel(selectedRow[i]);
                    final CodeCloneListViewModel model = (CodeCloneListViewModel) CodeCloneListView.this
                            .getModel();
                    final CodeCloneInfo codeFragment = model.getCodeClone(modelIndex);
                    selectedCodeFragments.add(codeFragment);
                }

                SelectedEntities.<CodeCloneInfo> getInstance(CodeCloneInfo.CODECLONE).setAll(
                        selectedCodeFragments, CodeCloneListView.this);
            }
        }
    }

    /**
     * �R���X�g���N�^
     */
    public CodeCloneListView() {

        this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        this.scrollPane = new JScrollPane();
        this.scrollPane.setViewportView(this);
        this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        this.selectionEventHandler = new SelectionEventHandler();
        this.getSelectionModel().addListSelectionListener(this.selectionEventHandler);
    }

    /**
     * �I�u�U�[�o�p�^�[���p���\�b�h
     */
    @Override
    public void update(Observable o, Object arg) {

        if (o instanceof SelectedEntities) {

            final SelectedEntities<?> selectedEntities = (SelectedEntities<?>) o;
            if (selectedEntities.getLabel().equals(CloneSetInfo.CLONESET)) {

                final CloneSetInfo cloneSet = SelectedEntities.<CloneSetInfo> getInstance(
                        CloneSetInfo.CLONESET).get().first();

                final CodeCloneListViewModel model = new CodeCloneListViewModel(cloneSet);
                this.setModel(model);
                final RowSorter<CodeCloneListViewModel> sorter = new TableRowSorter<CodeCloneListViewModel>(
                        model);
                this.setRowSorter(sorter);
            }
        }

    }

    final public JScrollPane scrollPane;

    final SelectionEventHandler selectionEventHandler;
}
