package jp.ac.osaka_u.ist.sdl.scdetector.gui.element;

import java.util.Observable;
import java.util.Observer;
import java.util.SortedSet;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;

import jp.ac.osaka_u.ist.sdl.scdetector.gui.SelectedEntities;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.CodeCloneInfo;

public class ElementListView extends JTable implements Observer {

	class SelectionEventHandler implements ListSelectionListener {

		public void valueChanged(ListSelectionEvent e) {

			if (!e.getValueIsAdjusting()) {

				// final int[] selectedRow =
				// CodeCloneListView.this.getSelectedRows();
				// final SortedSet<CodeCloneInfo> selectedCodeFragments = new
				// TreeSet<CodeCloneInfo>();
				// for (int i = 0; i < selectedRow.length; i++) {
				//
				// final int modelIndex = CodeCloneListView.this
				// .convertRowIndexToModel(selectedRow[i]);
				// final CodeCloneListViewModel model = (CodeCloneListViewModel)
				// CodeCloneListView.this
				// .getModel();
				// final CodeCloneInfo codeFragment =
				// model.getCodeClone(modelIndex);
				// selectedCodeFragments.add(codeFragment);
				// }
				//
				// SelectedEntities.<CodeCloneInfo>
				// getInstance(CodeCloneInfo.CODECLONE).setAll(
				// selectedCodeFragments, CodeCloneListView.this);
			}
		}
	}

	public ElementListView() {

		this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		this.scrollPane = new JScrollPane();
		this.scrollPane.setViewportView(this);
		this.scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.scrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		this.selectionEventHandler = new SelectionEventHandler();
		this.getSelectionModel().addListSelectionListener(
				this.selectionEventHandler);
	}

	@Override
	public void update(Observable o, Object arg) {

		if (o instanceof SelectedEntities) {

			final SelectedEntities<?> selectedEntities = (SelectedEntities<?>) o;
			if (selectedEntities.getLabel().equals(CodeCloneInfo.CODECLONE)) {

				final SortedSet<CodeCloneInfo> codeclones = SelectedEntities
						.<CodeCloneInfo> getInstance(CodeCloneInfo.CODECLONE)
						.get();

				final ElementListViewModel model = new ElementListViewModel(
						codeclones);
				this.setModel(model);
				final RowSorter<ElementListViewModel> sorter = new TableRowSorter<ElementListViewModel>(
						model);
				this.setRowSorter(sorter);
			}
		}

	}

	final public JScrollPane scrollPane;

	final SelectionEventHandler selectionEventHandler;
}
