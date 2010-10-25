package jp.ac.osaka_u.ist.sdl.scdetector.gui.sourcecode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.JTabbedPane;

import jp.ac.osaka_u.ist.sdl.scdetector.Scorpioui;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.SelectedEntities;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.CodeCloneInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.ElementInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.FileController;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.FileInfo;

/**
 * ソースコード表示用コンポーネント
 * 
 * @author higo
 * 
 */
public class SourceCodeView extends JTabbedPane implements Observer {

	/**
	 * コンストラクタ
	 */
	public SourceCodeView() {
		this.map = new HashMap<CodeCloneInfo, Set<SourceCodePanel>>();
	}

	/**
	 * オブザーバパターン用メソッド
	 */
	@Override
	public void update(Observable o, Object arg) {

		if (o instanceof SelectedEntities) {

			final SelectedEntities<?> selectedEntities = (SelectedEntities<?>) o;
			if (selectedEntities.getLabel().equals(CodeCloneInfo.CODECLONE)) {

				final Set<CodeCloneInfo> codeclones = SelectedEntities
						.<CodeCloneInfo> getInstance(CodeCloneInfo.CODECLONE)
						.get();

				// 全てクリア
				this.removeAll();
				this.map.clear();

				// 新しく選択されているファイルを開く
				for (final CodeCloneInfo codeclone : codeclones) {

					final Set<FileInfo> files = this.getOwnerFiles(codeclone);
					final Set<SourceCodePanel> panels = new HashSet<SourceCodePanel>();
					for (final FileInfo file : files) {
						final StringBuilder title = new StringBuilder();
						title.append(codeclone.getID());
						title.append(" : ");
						title.append(file.getName());

						final SourceCodePanel panel = new SourceCodePanel(file);
						this.add(panel, title.toString());
						panel.addHighlight(codeclone);
						panel.display(codeclone);
						panels.add(panel);
					}

					this.map.put(codeclone, panels);
				}
			}
		}
	}

	private final Set<FileInfo> getOwnerFiles(final CodeCloneInfo codeclone) {

		final Set<FileInfo> files = new HashSet<FileInfo>();
		for (final ElementInfo element : codeclone.getElements()) {
			final int fileID = element.getFileID();
			final FileInfo file = FileController.getInstance(Scorpioui.ID)
					.getFile(fileID);
			files.add(file);
		}

		return files;
	}

	private final Map<CodeCloneInfo, Set<SourceCodePanel>> map;
}
