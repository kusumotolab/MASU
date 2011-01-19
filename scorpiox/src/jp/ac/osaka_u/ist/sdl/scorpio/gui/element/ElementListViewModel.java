package jp.ac.osaka_u.ist.sdl.scorpio.gui.element;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import javax.swing.table.AbstractTableModel;

import jp.ac.osaka_u.ist.sdl.scorpio.Scorpioui;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.CodeCloneInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.ElementInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.FileController;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.FileInfo;

public class ElementListViewModel extends AbstractTableModel {

	ElementListViewModel(final SortedSet<CodeCloneInfo> codeclones) {
		this.elements = new ArrayList<ElementInfo>();
		for (final CodeCloneInfo codeclone : codeclones) {
			this.elements.addAll(codeclone.getElements());
		}
	}

	@Override
	public int getRowCount() {
		return this.elements.size();
	}

	@Override
	public int getColumnCount() {
		return TITLES.length;
	}

	@Override
	public Object getValueAt(int row, int col) {

		switch (col) {
		case COL_FILE:
			final int fileID = this.elements.get(row).getFileID();
			final FileInfo file = FileController.getInstance(Scorpioui.ID)
					.getFile(fileID);
			final String path = file.getName();
			final int index = path.lastIndexOf(java.io.File.separatorChar);
			if (0 < index) {
				return path.substring(index + 1);
			} else {
				return path;
			}
		case COL_POSITION:
			return this.getPositionText(this.elements.get(row));
		default:
			assert false : "Here shouldn't be reached!";
			return null;
		}
	}

	@Override
	public Class<?> getColumnClass(int row) {
		return Integer.class;
	}

	@Override
	public String getColumnName(int col) {
		return TITLES[col];
	}

	public ElementInfo getElement(final int row) {
		return this.elements.get(row);
	}

	public ElementInfo[] getElements() {
		return this.elements.toArray(new ElementInfo[] {});
	}

	private String getPositionText(final ElementInfo element) {
		final StringBuilder text = new StringBuilder();
		text.append(element.getFromLine());
		text.append(".");
		text.append(element.getFromColumn());
		text.append(" - ");
		text.append(element.getToLine());
		text.append(".");
		text.append(element.getToColumn());
		return text.toString();
	}

	static final int COL_FILE = 0;

	static final int COL_POSITION = 1;

	static final String[] TITLES = new String[] { "FILE", "POSITION" };

	final private List<ElementInfo> elements;
}
